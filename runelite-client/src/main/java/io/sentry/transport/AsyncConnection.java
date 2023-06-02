/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry.transport;

import io.sentry.ILogger;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.cache.IEnvelopeCache;
import io.sentry.hints.Cached;
import io.sentry.hints.DiskFlushNotification;
import io.sentry.hints.Retryable;
import io.sentry.hints.SubmissionResult;
import io.sentry.transport.Connection;
import io.sentry.transport.ITransport;
import io.sentry.transport.ITransportGate;
import io.sentry.transport.NoOpEnvelopeCache;
import io.sentry.transport.QueuedThreadPoolExecutor;
import io.sentry.transport.TransportResult;
import io.sentry.util.LogUtils;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

@ApiStatus.Internal
public final class AsyncConnection
implements Closeable,
Connection {
    @NotNull
    private final ITransport transport;
    @NotNull
    private final ITransportGate transportGate;
    @NotNull
    private final ExecutorService executor;
    @NotNull
    private final IEnvelopeCache envelopeCache;
    @NotNull
    private final SentryOptions options;

    public AsyncConnection(ITransport transport, ITransportGate transportGate, IEnvelopeCache envelopeCache, int maxQueueSize, SentryOptions options) {
        this(transport, transportGate, envelopeCache, AsyncConnection.initExecutor(maxQueueSize, envelopeCache, options.getLogger()), options);
    }

    @TestOnly
    AsyncConnection(@NotNull ITransport transport, @NotNull ITransportGate transportGate, @NotNull IEnvelopeCache envelopeCache, @NotNull ExecutorService executorService, @NotNull SentryOptions options) {
        this.transport = transport;
        this.transportGate = transportGate;
        this.envelopeCache = envelopeCache;
        this.options = options;
        this.executor = executorService;
    }

    private static QueuedThreadPoolExecutor initExecutor(int maxQueueSize, @NotNull IEnvelopeCache envelopeCache, @NotNull ILogger logger) {
        RejectedExecutionHandler storeEvents = (r, executor) -> {
            if (r instanceof EnvelopeSender) {
                EnvelopeSender envelopeSender = (EnvelopeSender)r;
                if (!(envelopeSender.hint instanceof Cached)) {
                    envelopeCache.store(envelopeSender.envelope, envelopeSender.hint);
                }
                AsyncConnection.markHintWhenSendingFailed(envelopeSender.hint, true);
                logger.log(SentryLevel.WARNING, "Envelope rejected", new Object[0]);
            }
        };
        return new QueuedThreadPoolExecutor(1, maxQueueSize, new AsyncConnectionThreadFactory(), storeEvents, logger);
    }

    private static void markHintWhenSendingFailed(@Nullable Object hint, boolean retry) {
        if (hint instanceof SubmissionResult) {
            ((SubmissionResult)hint).setResult(false);
        }
        if (hint instanceof Retryable) {
            ((Retryable)hint).setRetry(retry);
        }
    }

    @Override
    public void send(@NotNull SentryEnvelope envelope, @Nullable Object hint) throws IOException {
        IEnvelopeCache currentEnvelopeCache = this.envelopeCache;
        boolean cached = false;
        if (hint instanceof Cached) {
            currentEnvelopeCache = NoOpEnvelopeCache.getInstance();
            cached = true;
            this.options.getLogger().log(SentryLevel.DEBUG, "Captured Envelope is already cached", new Object[0]);
        }
        ArrayList<SentryEnvelopeItem> dropItems = null;
        for (SentryEnvelopeItem item : envelope.getItems()) {
            if (this.transport.isRetryAfter(item.getHeader().getType().getItemType())) {
                if (dropItems == null) {
                    dropItems = new ArrayList<SentryEnvelopeItem>();
                }
                dropItems.add(item);
            }
            if (dropItems == null) continue;
            this.options.getLogger().log(SentryLevel.INFO, "%d items will be dropped due rate limiting.", dropItems.size());
        }
        if (dropItems != null) {
            ArrayList<SentryEnvelopeItem> toSend = new ArrayList<SentryEnvelopeItem>();
            for (SentryEnvelopeItem item : envelope.getItems()) {
                if (dropItems.contains(item)) continue;
                toSend.add(item);
            }
            if (toSend.isEmpty()) {
                if (cached) {
                    this.envelopeCache.discard(envelope);
                }
                this.options.getLogger().log(SentryLevel.INFO, "Envelope discarded due all items rate limited.", new Object[0]);
                AsyncConnection.markHintWhenSendingFailed(hint, false);
                return;
            }
            envelope = new SentryEnvelope(envelope.getHeader(), toSend);
        }
        this.executor.submit(new EnvelopeSender(envelope, hint, currentEnvelopeCache));
    }

    @Override
    public void close() throws IOException {
        this.executor.shutdown();
        this.options.getLogger().log(SentryLevel.DEBUG, "Shutting down", new Object[0]);
        try {
            if (!this.executor.awaitTermination(1L, TimeUnit.MINUTES)) {
                this.options.getLogger().log(SentryLevel.WARNING, "Failed to shutdown the async connection async sender within 1 minute. Trying to force it now.", new Object[0]);
                this.executor.shutdownNow();
            }
            this.transport.close();
        }
        catch (InterruptedException e) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Thread interrupted while closing the connection.", new Object[0]);
            Thread.currentThread().interrupt();
        }
    }

    private final class EnvelopeSender
    implements Runnable {
        @NotNull
        private final SentryEnvelope envelope;
        @Nullable
        private final Object hint;
        @NotNull
        private final IEnvelopeCache envelopeCache;
        private final TransportResult failedResult = TransportResult.error();

        EnvelopeSender(@Nullable SentryEnvelope envelope, @NotNull Object hint, IEnvelopeCache envelopeCache) {
            this.envelope = Objects.requireNonNull(envelope, "Envelope is required.");
            this.hint = hint;
            this.envelopeCache = Objects.requireNonNull(envelopeCache, "EnvelopeCache is required.");
        }

        @Override
        public void run() {
            TransportResult result = this.failedResult;
            try {
                result = this.flush();
                AsyncConnection.this.options.getLogger().log(SentryLevel.DEBUG, "Envelope flushed", new Object[0]);
            }
            catch (Exception e) {
                try {
                    AsyncConnection.this.options.getLogger().log(SentryLevel.ERROR, e, "Envelope submission failed", new Object[0]);
                    throw e;
                }
                catch (Throwable throwable) {
                    if (this.hint instanceof SubmissionResult) {
                        AsyncConnection.this.options.getLogger().log(SentryLevel.DEBUG, "Marking envelope submission result: %s", result.isSuccess());
                        ((SubmissionResult)this.hint).setResult(result.isSuccess());
                    }
                    throw throwable;
                }
            }
            if (this.hint instanceof SubmissionResult) {
                AsyncConnection.this.options.getLogger().log(SentryLevel.DEBUG, "Marking envelope submission result: %s", result.isSuccess());
                ((SubmissionResult)this.hint).setResult(result.isSuccess());
            }
        }

        @NotNull
        private TransportResult flush() {
            TransportResult result = this.failedResult;
            this.envelopeCache.store(this.envelope, this.hint);
            if (this.hint instanceof DiskFlushNotification) {
                ((DiskFlushNotification)this.hint).markFlushed();
                AsyncConnection.this.options.getLogger().log(SentryLevel.DEBUG, "Disk flush envelope fired", new Object[0]);
            }
            if (AsyncConnection.this.transportGate.isConnected()) {
                try {
                    result = AsyncConnection.this.transport.send(this.envelope);
                    if (!result.isSuccess()) {
                        String message = "The transport failed to send the envelope with response code " + result.getResponseCode();
                        AsyncConnection.this.options.getLogger().log(SentryLevel.ERROR, message, new Object[0]);
                        throw new IllegalStateException(message);
                    }
                    this.envelopeCache.discard(this.envelope);
                }
                catch (IOException e) {
                    if (this.hint instanceof Retryable) {
                        ((Retryable)this.hint).setRetry(true);
                    } else {
                        LogUtils.logIfNotRetryable(AsyncConnection.this.options.getLogger(), this.hint);
                    }
                    throw new IllegalStateException("Sending the event failed.", e);
                }
            } else if (this.hint instanceof Retryable) {
                ((Retryable)this.hint).setRetry(true);
            } else {
                LogUtils.logIfNotRetryable(AsyncConnection.this.options.getLogger(), this.hint);
            }
            return result;
        }
    }

    private static final class AsyncConnectionThreadFactory
    implements ThreadFactory {
        private int cnt;

        private AsyncConnectionThreadFactory() {
        }

        @Override
        @NotNull
        public Thread newThread(@NotNull Runnable r) {
            Thread ret = new Thread(r, "SentryAsyncConnection-" + this.cnt++);
            ret.setDaemon(true);
            return ret;
        }
    }
}

