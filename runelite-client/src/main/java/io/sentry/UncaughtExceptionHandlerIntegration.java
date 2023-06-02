/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry;

import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.Integration;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.UncaughtExceptionHandler;
import io.sentry.exception.ExceptionMechanismException;
import io.sentry.hints.DiskFlushNotification;
import io.sentry.hints.Flushable;
import io.sentry.hints.SessionEnd;
import io.sentry.protocol.Mechanism;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

public final class UncaughtExceptionHandlerIntegration
implements Integration,
Thread.UncaughtExceptionHandler,
Closeable {
    @Nullable
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    @Nullable
    private IHub hub;
    @Nullable
    private SentryOptions options;
    private boolean registered = false;
    @NotNull
    private final UncaughtExceptionHandler threadAdapter;

    public UncaughtExceptionHandlerIntegration() {
        this(UncaughtExceptionHandler.Adapter.getInstance());
    }

    UncaughtExceptionHandlerIntegration(@NotNull UncaughtExceptionHandler threadAdapter) {
        this.threadAdapter = Objects.requireNonNull(threadAdapter, "threadAdapter is required.");
    }

    @Override
    public final void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        if (this.registered) {
            options.getLogger().log(SentryLevel.ERROR, "Attempt to register a UncaughtExceptionHandlerIntegration twice.", new Object[0]);
            return;
        }
        this.registered = true;
        this.hub = Objects.requireNonNull(hub, "Hub is required");
        this.options = Objects.requireNonNull(options, "SentryOptions is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "UncaughtExceptionHandlerIntegration enabled: %s", this.options.isEnableUncaughtExceptionHandler());
        if (this.options.isEnableUncaughtExceptionHandler()) {
            Thread.UncaughtExceptionHandler currentHandler = this.threadAdapter.getDefaultUncaughtExceptionHandler();
            if (currentHandler != null) {
                this.options.getLogger().log(SentryLevel.DEBUG, "default UncaughtExceptionHandler class='" + currentHandler.getClass().getName() + "'", new Object[0]);
                this.defaultExceptionHandler = currentHandler;
            }
            this.threadAdapter.setDefaultUncaughtExceptionHandler(this);
            this.options.getLogger().log(SentryLevel.DEBUG, "UncaughtExceptionHandlerIntegration installed.", new Object[0]);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable thrown) {
        if (this.options != null && this.hub != null) {
            this.options.getLogger().log(SentryLevel.INFO, "Uncaught exception received.", new Object[0]);
            try {
                UncaughtExceptionHint hint = new UncaughtExceptionHint(this.options.getFlushTimeoutMillis(), this.options.getLogger());
                Throwable throwable = UncaughtExceptionHandlerIntegration.getUnhandledThrowable(thread, thrown);
                SentryEvent event = new SentryEvent(throwable);
                event.setLevel(SentryLevel.FATAL);
                this.hub.captureEvent(event, hint);
                if (!hint.waitFlush()) {
                    this.options.getLogger().log(SentryLevel.WARNING, "Timed out waiting to flush event to disk before crashing. Event: %s", event.getEventId());
                }
            }
            catch (Exception e) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error sending uncaught exception to Sentry.", e);
            }
            if (this.defaultExceptionHandler != null) {
                this.options.getLogger().log(SentryLevel.INFO, "Invoking inner uncaught exception handler.", new Object[0]);
                this.defaultExceptionHandler.uncaughtException(thread, thrown);
            }
        }
    }

    @TestOnly
    @NotNull
    static Throwable getUnhandledThrowable(@NotNull Thread thread, @NotNull Throwable thrown) {
        Mechanism mechanism = new Mechanism();
        mechanism.setHandled(false);
        mechanism.setType("UncaughtExceptionHandler");
        return new ExceptionMechanismException(mechanism, thrown, thread);
    }

    @Override
    public void close() {
        if (this.defaultExceptionHandler != null && this == this.threadAdapter.getDefaultUncaughtExceptionHandler()) {
            this.threadAdapter.setDefaultUncaughtExceptionHandler(this.defaultExceptionHandler);
            if (this.options != null) {
                this.options.getLogger().log(SentryLevel.DEBUG, "UncaughtExceptionHandlerIntegration removed.", new Object[0]);
            }
        }
    }

    private static final class UncaughtExceptionHint
    implements DiskFlushNotification,
    Flushable,
    SessionEnd {
        private final CountDownLatch latch;
        private final long flushTimeoutMillis;
        @NotNull
        private final ILogger logger;

        UncaughtExceptionHint(long flushTimeoutMillis, @NotNull ILogger logger) {
            this.flushTimeoutMillis = flushTimeoutMillis;
            this.latch = new CountDownLatch(1);
            this.logger = logger;
        }

        @Override
        public boolean waitFlush() {
            try {
                return this.latch.await(this.flushTimeoutMillis, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.logger.log(SentryLevel.ERROR, "Exception while awaiting for flush in UncaughtExceptionHint", e);
                return false;
            }
        }

        @Override
        public void markFlushed() {
            this.latch.countDown();
        }
    }
}

