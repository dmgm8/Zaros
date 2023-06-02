/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry;

import io.sentry.ISentryExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

final class SentryExecutorService
implements ISentryExecutorService {
    @NotNull
    private final ExecutorService executorService;

    @TestOnly
    SentryExecutorService(@NotNull ExecutorService executorService) {
        this.executorService = executorService;
    }

    SentryExecutorService() {
        this(Executors.newSingleThreadExecutor());
    }

    @Override
    public Future<?> submit(@NotNull Runnable runnable) {
        return this.executorService.submit(runnable);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close(long timeoutMillis) {
        ExecutorService executorService = this.executorService;
        synchronized (executorService) {
            if (!this.executorService.isShutdown()) {
                this.executorService.shutdown();
                try {
                    if (!this.executorService.awaitTermination(timeoutMillis, TimeUnit.MILLISECONDS)) {
                        this.executorService.shutdownNow();
                    }
                }
                catch (InterruptedException e) {
                    this.executorService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

