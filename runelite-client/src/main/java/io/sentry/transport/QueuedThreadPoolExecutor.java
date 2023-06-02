/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.transport;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QueuedThreadPoolExecutor
extends ThreadPoolExecutor {
    private final int maxQueueSize;
    private final AtomicInteger currentlyRunning;
    @NotNull
    private final ILogger logger;

    public QueuedThreadPoolExecutor(int corePoolSize, int maxQueueSize, @NotNull ThreadFactory threadFactory, @NotNull RejectedExecutionHandler rejectedExecutionHandler, @NotNull ILogger logger) {
        super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory, rejectedExecutionHandler);
        this.maxQueueSize = maxQueueSize;
        this.currentlyRunning = new AtomicInteger();
        this.logger = logger;
    }

    @Override
    public Future<?> submit(@NotNull Runnable task) {
        if (this.isSchedulingAllowed()) {
            return super.submit(task);
        }
        this.logger.log(SentryLevel.WARNING, "Submit cancelled", new Object[0]);
        return new CancelledFuture();
    }

    @Override
    protected void beforeExecute(@NotNull Thread t, @NotNull Runnable r) {
        try {
            super.beforeExecute(t, r);
        }
        finally {
            this.currentlyRunning.incrementAndGet();
        }
    }

    @Override
    protected void afterExecute(@NotNull Runnable r, @Nullable Throwable t) {
        try {
            super.afterExecute(r, t);
        }
        finally {
            this.currentlyRunning.decrementAndGet();
        }
    }

    private boolean isSchedulingAllowed() {
        return this.getQueue().size() + this.currentlyRunning.get() < this.maxQueueSize;
    }

    private static final class CancelledFuture<T>
    implements Future<T> {
        private CancelledFuture() {
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return true;
        }

        @Override
        public boolean isCancelled() {
            return true;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public T get() {
            throw new CancellationException();
        }

        @Override
        public T get(long timeout, @NotNull TimeUnit unit) {
            throw new CancellationException();
        }
    }
}

