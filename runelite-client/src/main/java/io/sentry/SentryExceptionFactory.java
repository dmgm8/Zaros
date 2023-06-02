/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry;

import io.sentry.SentryStackTraceFactory;
import io.sentry.exception.ExceptionMechanismException;
import io.sentry.protocol.Mechanism;
import io.sentry.protocol.SentryException;
import io.sentry.protocol.SentryStackTrace;
import io.sentry.util.Objects;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

final class SentryExceptionFactory {
    @NotNull
    private final SentryStackTraceFactory sentryStackTraceFactory;

    public SentryExceptionFactory(@NotNull SentryStackTraceFactory sentryStackTraceFactory) {
        this.sentryStackTraceFactory = Objects.requireNonNull(sentryStackTraceFactory, "The SentryStackTraceFactory is required.");
    }

    @NotNull
    List<SentryException> getSentryExceptions(@NotNull Throwable throwable) {
        return this.getSentryExceptions(this.extractExceptionQueue(throwable));
    }

    @NotNull
    private List<SentryException> getSentryExceptions(@NotNull Deque<SentryException> exceptions) {
        return new ArrayList<SentryException>(exceptions);
    }

    @NotNull
    private SentryException getSentryException(@NotNull Throwable throwable, @Nullable Mechanism exceptionMechanism, @Nullable Thread thread) {
        Package exceptionPackage = throwable.getClass().getPackage();
        String fullClassName = throwable.getClass().getName();
        SentryException exception = new SentryException();
        String exceptionMessage = throwable.getMessage();
        String exceptionClassName = exceptionPackage != null ? fullClassName.replace(exceptionPackage.getName() + ".", "") : fullClassName;
        String exceptionPackageName = exceptionPackage != null ? exceptionPackage.getName() : null;
        SentryStackTrace sentryStackTrace = new SentryStackTrace();
        sentryStackTrace.setFrames(this.sentryStackTraceFactory.getStackFrames(throwable.getStackTrace()));
        if (thread != null) {
            exception.setThreadId(thread.getId());
        }
        exception.setStacktrace(sentryStackTrace);
        exception.setType(exceptionClassName);
        exception.setMechanism(exceptionMechanism);
        exception.setModule(exceptionPackageName);
        exception.setValue(exceptionMessage);
        return exception;
    }

    @TestOnly
    @NotNull
    Deque<SentryException> extractExceptionQueue(@NotNull Throwable throwable) {
        ArrayDeque<SentryException> exceptions = new ArrayDeque<SentryException>();
        HashSet<Throwable> circularityDetector = new HashSet<Throwable>();
        for (Throwable currentThrowable = throwable; currentThrowable != null && circularityDetector.add(currentThrowable); currentThrowable = currentThrowable.getCause()) {
            Thread thread;
            Mechanism exceptionMechanism;
            if (currentThrowable instanceof ExceptionMechanismException) {
                ExceptionMechanismException exceptionMechanismThrowable = (ExceptionMechanismException)currentThrowable;
                exceptionMechanism = exceptionMechanismThrowable.getExceptionMechanism();
                currentThrowable = exceptionMechanismThrowable.getThrowable();
                thread = exceptionMechanismThrowable.getThread();
            } else {
                exceptionMechanism = null;
                thread = Thread.currentThread();
            }
            SentryException exception = this.getSentryException(currentThrowable, exceptionMechanism, thread);
            exceptions.addFirst(exception);
        }
        return exceptions;
    }
}

