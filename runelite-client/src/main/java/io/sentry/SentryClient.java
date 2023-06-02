/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry;

import io.sentry.AsyncConnectionFactory;
import io.sentry.Breadcrumb;
import io.sentry.EventProcessor;
import io.sentry.HttpTransportFactory;
import io.sentry.ISentryClient;
import io.sentry.Scope;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeHeader;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.Session;
import io.sentry.hints.DiskFlushNotification;
import io.sentry.protocol.SentryId;
import io.sentry.transport.Connection;
import io.sentry.transport.ITransport;
import io.sentry.transport.NoOpTransport;
import io.sentry.util.ApplyScopeUtils;
import io.sentry.util.Objects;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

public final class SentryClient
implements ISentryClient {
    static final String SENTRY_PROTOCOL_VERSION = "7";
    private boolean enabled;
    @NotNull
    private final SentryOptions options;
    @NotNull
    private final Connection connection;
    @Nullable
    private final Random random;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    SentryClient(@NotNull SentryOptions options) {
        this(options, null);
    }

    public SentryClient(@NotNull SentryOptions options, @Nullable Connection connection) {
        this.options = Objects.requireNonNull(options, "SentryOptions is required.");
        this.enabled = true;
        ITransport transport = options.getTransport();
        if (transport instanceof NoOpTransport) {
            transport = HttpTransportFactory.create(options);
            options.setTransport(transport);
        }
        if (connection == null) {
            connection = AsyncConnectionFactory.create(options, options.getEnvelopeDiskCache());
        }
        this.connection = connection;
        this.random = options.getSampleRate() == null ? null : new Random();
    }

    @Override
    @NotNull
    public SentryId captureEvent(@NotNull SentryEvent event, @Nullable Scope scope, @Nullable Object hint) {
        Objects.requireNonNull(event, "SentryEvent is required.");
        this.options.getLogger().log(SentryLevel.DEBUG, "Capturing event: %s", event.getEventId());
        if (ApplyScopeUtils.shouldApplyScopeData(hint)) {
            if ((event = this.applyScope(event, scope, hint)) == null) {
                this.options.getLogger().log(SentryLevel.DEBUG, "Event was dropped by applyScope", new Object[0]);
            }
        } else {
            this.options.getLogger().log(SentryLevel.DEBUG, "Event was cached so not applying scope: %s", event.getEventId());
        }
        event = this.processEvent(event, hint, this.options.getEventProcessors());
        Session session = null;
        if (event != null) {
            session = this.updateSessionData(event, hint, scope);
            if (!this.sample()) {
                this.options.getLogger().log(SentryLevel.DEBUG, "Event %s was dropped due to sampling decision.", event.getEventId());
                event = null;
            }
        }
        if (event != null && (event = this.executeBeforeSend(event, hint)) == null) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Event was dropped by beforeSend", new Object[0]);
        }
        SentryId sentryId = SentryId.EMPTY_ID;
        if (event != null) {
            sentryId = event.getEventId();
        }
        try {
            SentryEnvelope envelope = this.buildEnvelope(event, session);
            if (envelope != null) {
                this.connection.send(envelope, hint);
            }
        }
        catch (IOException e) {
            this.options.getLogger().log(SentryLevel.WARNING, e, "Capturing event %s failed.", sentryId);
            sentryId = SentryId.EMPTY_ID;
        }
        return sentryId;
    }

    @Nullable
    private SentryEnvelope buildEnvelope(@Nullable SentryEvent event, @Nullable Session session) throws IOException {
        SentryId sentryId = null;
        ArrayList<SentryEnvelopeItem> envelopeItems = new ArrayList<SentryEnvelopeItem>();
        if (event != null) {
            SentryEnvelopeItem eventItem = SentryEnvelopeItem.fromEvent(this.options.getSerializer(), event);
            envelopeItems.add(eventItem);
            sentryId = event.getEventId();
        }
        if (session != null) {
            SentryEnvelopeItem sessionItem = SentryEnvelopeItem.fromSession(this.options.getSerializer(), session);
            envelopeItems.add(sessionItem);
        }
        if (!envelopeItems.isEmpty()) {
            SentryEnvelopeHeader envelopeHeader = new SentryEnvelopeHeader(sentryId, this.options.getSdkVersion());
            return new SentryEnvelope(envelopeHeader, envelopeItems);
        }
        return null;
    }

    @Nullable
    private SentryEvent processEvent(@NotNull SentryEvent event, @Nullable Object hint, @NotNull List<EventProcessor> eventProcessors) {
        for (EventProcessor processor : eventProcessors) {
            try {
                event = processor.process(event, hint);
            }
            catch (Exception e) {
                this.options.getLogger().log(SentryLevel.ERROR, e, "An exception occurred while processing event by processor: %s", processor.getClass().getName());
            }
            if (event != null) continue;
            this.options.getLogger().log(SentryLevel.DEBUG, "Event was dropped by a processor: %s", processor.getClass().getName());
            break;
        }
        return event;
    }

    @TestOnly
    @Nullable
    Session updateSessionData(@NotNull SentryEvent event, @Nullable Object hint, @Nullable Scope scope) {
        Session clonedSession = null;
        if (ApplyScopeUtils.shouldApplyScopeData(hint)) {
            if (scope != null) {
                clonedSession = scope.withSession(session -> {
                    if (session != null) {
                        Session.State status = null;
                        if (event.isCrashed()) {
                            status = Session.State.Crashed;
                        }
                        boolean crashedOrErrored = false;
                        if (Session.State.Crashed == status || event.isErrored()) {
                            crashedOrErrored = true;
                        }
                        String userAgent = null;
                        if (event.getRequest() != null && event.getRequest().getHeaders() != null && event.getRequest().getHeaders().containsKey("user-agent")) {
                            userAgent = event.getRequest().getHeaders().get("user-agent");
                        }
                        if (session.update(status, userAgent, crashedOrErrored) && hint instanceof DiskFlushNotification) {
                            session.end();
                        }
                    } else {
                        this.options.getLogger().log(SentryLevel.INFO, "Session is null on scope.withSession", new Object[0]);
                    }
                });
            } else {
                this.options.getLogger().log(SentryLevel.INFO, "Scope is null on client.captureEvent", new Object[0]);
            }
        }
        return clonedSession;
    }

    @Override
    @ApiStatus.Internal
    public void captureSession(@NotNull Session session, @Nullable Object hint) {
        SentryEnvelope envelope;
        Objects.requireNonNull(session, "Session is required.");
        if (session.getRelease() == null || session.getRelease().isEmpty()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Sessions can't be captured without setting a release.", new Object[0]);
            return;
        }
        try {
            envelope = SentryEnvelope.fromSession(this.options.getSerializer(), session, this.options.getSdkVersion());
        }
        catch (IOException e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Failed to capture session.", e);
            return;
        }
        this.captureEnvelope(envelope, hint);
    }

    @Override
    @ApiStatus.Internal
    @Nullable
    public SentryId captureEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        Objects.requireNonNull(envelope, "SentryEnvelope is required.");
        try {
            this.connection.send(envelope, hint);
        }
        catch (IOException e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Failed to capture envelope.", e);
            return SentryId.EMPTY_ID;
        }
        return envelope.getHeader().getEventId();
    }

    @Nullable
    private SentryEvent applyScope(@NotNull SentryEvent event, @Nullable Scope scope, @Nullable Object hint) {
        if (scope != null) {
            if (event.getTransaction() == null) {
                event.setTransaction(scope.getTransaction());
            }
            if (event.getUser() == null) {
                event.setUser(scope.getUser());
            }
            if (event.getFingerprints() == null) {
                event.setFingerprints(scope.getFingerprint());
            }
            if (event.getBreadcrumbs() == null) {
                event.setBreadcrumbs(new ArrayList<Breadcrumb>(scope.getBreadcrumbs()));
            } else {
                event.getBreadcrumbs().addAll(scope.getBreadcrumbs());
            }
            if (event.getTags() == null) {
                event.setTags(new HashMap<String, String>(scope.getTags()));
            } else {
                for (Map.Entry<String, String> entry : scope.getTags().entrySet()) {
                    if (event.getTags().containsKey(entry.getKey())) continue;
                    event.getTags().put(entry.getKey(), entry.getValue());
                }
            }
            if (event.getExtras() == null) {
                event.setExtras(new HashMap<String, Object>(scope.getExtras()));
            } else {
                for (Map.Entry<String, Object> entry : scope.getExtras().entrySet()) {
                    if (event.getExtras().containsKey(entry.getKey())) continue;
                    event.getExtras().put(entry.getKey(), entry.getValue());
                }
            }
            try {
                for (Map.Entry<String, Object> entry : scope.getContexts().clone().entrySet()) {
                    if (event.getContexts().containsKey(entry.getKey())) continue;
                    event.getContexts().put(entry.getKey(), entry.getValue());
                }
            }
            catch (CloneNotSupportedException e) {
                this.options.getLogger().log(SentryLevel.ERROR, "An error has occurred when cloning Contexts", e);
            }
            if (scope.getLevel() != null) {
                event.setLevel(scope.getLevel());
            }
            event = this.processEvent(event, hint, scope.getEventProcessors());
        }
        return event;
    }

    @Nullable
    private SentryEvent executeBeforeSend(@NotNull SentryEvent event, @Nullable Object hint) {
        SentryOptions.BeforeSendCallback beforeSend = this.options.getBeforeSend();
        if (beforeSend != null) {
            try {
                event = beforeSend.execute(event, hint);
            }
            catch (Exception e) {
                this.options.getLogger().log(SentryLevel.ERROR, "The BeforeSend callback threw an exception. It will be added as breadcrumb and continue.", e);
                Breadcrumb breadcrumb = new Breadcrumb();
                breadcrumb.setMessage("BeforeSend callback failed.");
                breadcrumb.setCategory("SentryClient");
                breadcrumb.setLevel(SentryLevel.ERROR);
                breadcrumb.setData("sentry:message", e.getMessage());
                event.addBreadcrumb(breadcrumb);
            }
        }
        return event;
    }

    @Override
    public void close() {
        this.options.getLogger().log(SentryLevel.INFO, "Closing SentryClient.", new Object[0]);
        try {
            this.flush(this.options.getShutdownTimeout());
            this.connection.close();
        }
        catch (IOException e) {
            this.options.getLogger().log(SentryLevel.WARNING, "Failed to close the connection to the Sentry Server.", e);
        }
        this.enabled = false;
    }

    @Override
    public void flush(long timeoutMillis) {
    }

    private boolean sample() {
        if (this.options.getSampleRate() != null && this.random != null) {
            double sampling = this.options.getSampleRate();
            return !(sampling < this.random.nextDouble());
        }
        return true;
    }
}

