/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.DirectoryProcessor;
import io.sentry.IEnvelopeReader;
import io.sentry.IEnvelopeSender;
import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.ISerializer;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryEvent;
import io.sentry.SentryItemType;
import io.sentry.SentryLevel;
import io.sentry.hints.Flushable;
import io.sentry.hints.Retryable;
import io.sentry.hints.SubmissionResult;
import io.sentry.util.CollectionUtils;
import io.sentry.util.LogUtils;
import io.sentry.util.Objects;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class OutboxSender
extends DirectoryProcessor
implements IEnvelopeSender {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @NotNull
    private final IHub hub;
    @NotNull
    private final IEnvelopeReader envelopeReader;
    @NotNull
    private final ISerializer serializer;
    @NotNull
    private final ILogger logger;

    public OutboxSender(@NotNull IHub hub, @NotNull IEnvelopeReader envelopeReader, @NotNull ISerializer serializer, @NotNull ILogger logger, long flushTimeoutMillis) {
        super(logger, flushTimeoutMillis);
        this.hub = Objects.requireNonNull(hub, "Hub is required.");
        this.envelopeReader = Objects.requireNonNull(envelopeReader, "Envelope reader is required.");
        this.serializer = Objects.requireNonNull(serializer, "Serializer is required.");
        this.logger = Objects.requireNonNull(logger, "Logger is required.");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void processFile(@NotNull File file, @Nullable Object hint) {
        block29: {
            Objects.requireNonNull(file, "File is required.");
            if (!this.isRelevantFileName(file.getName())) {
                this.logger.log(SentryLevel.DEBUG, "File '%s' should be ignored.", file.getAbsolutePath());
                return;
            }
            try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));){
                SentryEnvelope envelope = this.envelopeReader.read(stream);
                if (envelope == null) {
                    this.logger.log(SentryLevel.ERROR, "Stream from path %s resulted in a null envelope.", file.getAbsolutePath());
                } else {
                    this.processEnvelope(envelope, hint);
                    this.logger.log(SentryLevel.DEBUG, "File '%s' is done.", file.getAbsolutePath());
                }
            }
            catch (IOException e) {
                try {
                    this.logger.log(SentryLevel.ERROR, "Error processing envelope.", e);
                }
                catch (Throwable throwable) {
                    if (hint instanceof Retryable) {
                        if (!((Retryable)hint).isRetry()) {
                            try {
                                if (!file.delete()) {
                                    this.logger.log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
                                }
                            }
                            catch (RuntimeException e2) {
                                this.logger.log(SentryLevel.ERROR, e2, "Failed to delete: %s", file.getAbsolutePath());
                            }
                        }
                    } else {
                        LogUtils.logIfNotRetryable(this.logger, hint);
                    }
                    throw throwable;
                }
                if (hint instanceof Retryable) {
                    if (((Retryable)hint).isRetry()) break block29;
                    try {
                        if (!file.delete()) {
                            this.logger.log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
                        }
                    }
                    catch (RuntimeException e3) {
                        this.logger.log(SentryLevel.ERROR, e3, "Failed to delete: %s", file.getAbsolutePath());
                    }
                }
                LogUtils.logIfNotRetryable(this.logger, hint);
            }
            if (hint instanceof Retryable) {
                if (!((Retryable)hint).isRetry()) {
                    try {
                        if (!file.delete()) {
                            this.logger.log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
                        }
                    }
                    catch (RuntimeException e) {
                        this.logger.log(SentryLevel.ERROR, e, "Failed to delete: %s", file.getAbsolutePath());
                    }
                }
            } else {
                LogUtils.logIfNotRetryable(this.logger, hint);
            }
        }
    }

    @Override
    protected boolean isRelevantFileName(@Nullable String fileName) {
        return fileName != null && !fileName.startsWith("session");
    }

    @Override
    public void processEnvelopeFile(@NotNull String path, @Nullable Object hint) {
        Objects.requireNonNull(path, "Path is required.");
        this.processFile(new File(path), hint);
    }

    private void processEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) throws IOException {
        this.logger.log(SentryLevel.DEBUG, "Processing Envelope with %d item(s)", CollectionUtils.size(envelope.getItems()));
        int items = 0;
        for (SentryEnvelopeItem item : envelope.getItems()) {
            block18: {
                ++items;
                if (item.getHeader() == null) {
                    this.logger.log(SentryLevel.ERROR, "Item %d has no header", items);
                    continue;
                }
                if (SentryItemType.Event.equals((Object)item.getHeader().getType())) {
                    try {
                        BufferedReader eventReader = new BufferedReader(new InputStreamReader((InputStream)new ByteArrayInputStream(item.getData()), UTF_8));
                        Throwable throwable = null;
                        try {
                            SentryEvent event = this.serializer.deserializeEvent(eventReader);
                            if (event == null) {
                                this.logger.log(SentryLevel.ERROR, "Item %d of type %s returned null by the parser.", new Object[]{items, item.getHeader().getType()});
                            } else {
                                if (envelope.getHeader().getEventId() != null && !envelope.getHeader().getEventId().equals(event.getEventId())) {
                                    this.logger.log(SentryLevel.ERROR, "Item %d of has a different event id (%s) to the envelope header (%s)", items, envelope.getHeader().getEventId(), event.getEventId());
                                    continue;
                                }
                                this.hub.captureEvent(event, hint);
                                this.logger.log(SentryLevel.DEBUG, "Item %d is being captured.", items);
                                if (hint instanceof Flushable) {
                                    if (!((Flushable)hint).waitFlush()) {
                                        this.logger.log(SentryLevel.WARNING, "Timed out waiting for event submission: %s", event.getEventId());
                                        break;
                                    }
                                } else {
                                    LogUtils.logIfNotFlushable(this.logger, hint);
                                }
                            }
                            break block18;
                        }
                        catch (Throwable throwable2) {
                            throwable = throwable2;
                            throw throwable2;
                        }
                        finally {
                            OutboxSender.$closeResource(throwable, eventReader);
                            continue;
                        }
                    }
                    catch (Exception e) {
                        this.logger.log(SentryLevel.ERROR, "Item failed to process.", e);
                        break block18;
                    }
                }
                this.logger.log(SentryLevel.WARNING, "Item %d of type: %s ignored.", new Object[]{items, item.getHeader().getType()});
            }
            if (!(hint instanceof SubmissionResult) || ((SubmissionResult)hint).isSuccess()) continue;
            this.logger.log(SentryLevel.WARNING, "Envelope had a failed capture at item %d. No more items will be sent.", items);
            break;
        }
    }
}

