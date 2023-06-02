/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.EventProcessor;
import io.sentry.SentryEvent;
import io.sentry.SentryExceptionFactory;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.SentryStackTraceFactory;
import io.sentry.SentryThreadFactory;
import io.sentry.protocol.SentryException;
import io.sentry.util.ApplyScopeUtils;
import io.sentry.util.Objects;
import java.util.ArrayList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class MainEventProcessor
implements EventProcessor {
    private final SentryOptions options;
    private final SentryThreadFactory sentryThreadFactory;
    private final SentryExceptionFactory sentryExceptionFactory;

    MainEventProcessor(SentryOptions options) {
        this.options = Objects.requireNonNull(options, "The SentryOptions is required.");
        SentryStackTraceFactory sentryStackTraceFactory = new SentryStackTraceFactory(options.getInAppExcludes(), options.getInAppIncludes());
        this.sentryExceptionFactory = new SentryExceptionFactory(sentryStackTraceFactory);
        this.sentryThreadFactory = new SentryThreadFactory(sentryStackTraceFactory, this.options);
    }

    MainEventProcessor(SentryOptions options, SentryThreadFactory sentryThreadFactory, SentryExceptionFactory sentryExceptionFactory) {
        this.options = Objects.requireNonNull(options, "The SentryOptions is required.");
        this.sentryThreadFactory = Objects.requireNonNull(sentryThreadFactory, "The SentryThreadFactory is required.");
        this.sentryExceptionFactory = Objects.requireNonNull(sentryExceptionFactory, "The SentryExceptionFactory is required.");
    }

    @Override
    @NotNull
    public SentryEvent process(SentryEvent event, @Nullable Object hint) {
        Throwable throwable;
        if (event.getPlatform() == null) {
            event.setPlatform("java");
        }
        if ((throwable = event.getThrowable()) != null) {
            event.setExceptions(this.sentryExceptionFactory.getSentryExceptions(throwable));
        }
        if (ApplyScopeUtils.shouldApplyScopeData(hint)) {
            this.processNonCachedEvent(event);
        } else {
            this.options.getLogger().log(SentryLevel.DEBUG, "Event was cached so not applying data relevant to the current app execution/version: %s", event.getEventId());
        }
        return event;
    }

    private void processNonCachedEvent(SentryEvent event) {
        if (event.getRelease() == null) {
            event.setRelease(this.options.getRelease());
        }
        if (event.getEnvironment() == null) {
            event.setEnvironment(this.options.getEnvironment());
        }
        if (event.getServerName() == null) {
            event.setServerName(this.options.getServerName());
        }
        if (event.getDist() == null) {
            event.setDist(this.options.getDist());
        }
        if (event.getSdk() == null) {
            event.setSdk(this.options.getSdkVersion());
        }
        if (event.getThreads() == null) {
            ArrayList<Long> mechanismThreadIds = null;
            if (event.getExceptions() != null) {
                for (SentryException item : event.getExceptions()) {
                    if (item.getMechanism() == null || item.getThreadId() == null) continue;
                    if (mechanismThreadIds == null) {
                        mechanismThreadIds = new ArrayList<Long>();
                    }
                    mechanismThreadIds.add(item.getThreadId());
                }
            }
            if (this.options.isAttachThreads()) {
                event.setThreads(this.sentryThreadFactory.getCurrentThreads(mechanismThreadIds));
            } else if (this.options.isAttachStacktrace()) {
                event.setThreads(this.sentryThreadFactory.getCurrentThread(mechanismThreadIds));
            }
        }
    }
}

