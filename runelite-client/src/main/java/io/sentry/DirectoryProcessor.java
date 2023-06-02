/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.hints.Cached;
import io.sentry.hints.Flushable;
import io.sentry.hints.Retryable;
import io.sentry.hints.SubmissionResult;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class DirectoryProcessor {
    @NotNull
    private final ILogger logger;
    private final long flushTimeoutMillis;

    DirectoryProcessor(@NotNull ILogger logger, long flushTimeoutMillis) {
        this.logger = logger;
        this.flushTimeoutMillis = flushTimeoutMillis;
    }

    public void processDirectory(@NotNull File directory) {
        try {
            this.logger.log(SentryLevel.DEBUG, "Processing dir. %s", directory.getAbsolutePath());
            if (!directory.exists()) {
                this.logger.log(SentryLevel.WARNING, "Directory '%s' doesn't exist. No cached events to send.", directory.getAbsolutePath());
                return;
            }
            if (!directory.isDirectory()) {
                this.logger.log(SentryLevel.ERROR, "Cache dir %s is not a directory.", directory.getAbsolutePath());
                return;
            }
            File[] listFiles = directory.listFiles();
            if (listFiles == null) {
                this.logger.log(SentryLevel.ERROR, "Cache dir %s is null.", directory.getAbsolutePath());
                return;
            }
            File[] filteredListFiles = directory.listFiles((d, name) -> this.isRelevantFileName(name));
            this.logger.log(SentryLevel.DEBUG, "Processing %d items from cache dir %s", filteredListFiles != null ? filteredListFiles.length : 0, directory.getAbsolutePath());
            for (File file : listFiles) {
                if (!file.isFile()) {
                    this.logger.log(SentryLevel.DEBUG, "File %s is not a File.", file.getAbsolutePath());
                    continue;
                }
                this.logger.log(SentryLevel.DEBUG, "Processing file: %s", file.getAbsolutePath());
                SendCachedEnvelopeHint hint = new SendCachedEnvelopeHint(this.flushTimeoutMillis, this.logger);
                this.processFile(file, hint);
            }
        }
        catch (Exception e) {
            this.logger.log(SentryLevel.ERROR, e, "Failed processing '%s'", directory.getAbsolutePath());
        }
    }

    protected abstract void processFile(@NotNull File var1, @Nullable Object var2);

    protected abstract boolean isRelevantFileName(String var1);

    private static final class SendCachedEnvelopeHint
    implements Cached,
    Retryable,
    SubmissionResult,
    Flushable {
        boolean retry = false;
        boolean succeeded = false;
        private final CountDownLatch latch;
        private final long flushTimeoutMillis;
        @NotNull
        private final ILogger logger;

        public SendCachedEnvelopeHint(long flushTimeoutMillis, @NotNull ILogger logger) {
            this.flushTimeoutMillis = flushTimeoutMillis;
            this.latch = new CountDownLatch(1);
            this.logger = logger;
        }

        @Override
        public boolean isRetry() {
            return this.retry;
        }

        @Override
        public void setRetry(boolean retry) {
            this.retry = retry;
        }

        @Override
        public boolean waitFlush() {
            try {
                return this.latch.await(this.flushTimeoutMillis, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.logger.log(SentryLevel.ERROR, "Exception while awaiting on lock.", e);
                return false;
            }
        }

        @Override
        public void setResult(boolean succeeded) {
            this.succeeded = succeeded;
            this.latch.countDown();
        }

        @Override
        public boolean isSuccess() {
            return this.succeeded;
        }
    }
}

