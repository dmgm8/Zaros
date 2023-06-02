/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.exception;

import io.sentry.protocol.Mechanism;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class ExceptionMechanismException
extends RuntimeException {
    private static final long serialVersionUID = 142345454265713915L;
    private final Mechanism exceptionMechanism;
    private final Throwable throwable;
    private final Thread thread;

    public ExceptionMechanismException(@Nullable Mechanism mechanism, @Nullable Throwable throwable, @Nullable Thread thread) {
        this.exceptionMechanism = mechanism;
        this.throwable = throwable;
        this.thread = thread;
    }

    public Mechanism getExceptionMechanism() {
        return this.exceptionMechanism;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public Thread getThread() {
        return this.thread;
    }
}

