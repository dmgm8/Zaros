/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry;

import io.sentry.IHub;
import io.sentry.Integration;
import io.sentry.SentryOptions;
import io.sentry.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public final class ShutdownHookIntegration
implements Integration {
    @NotNull
    private final Runtime runtime;

    @TestOnly
    public ShutdownHookIntegration(@NotNull Runtime runtime) {
        this.runtime = Objects.requireNonNull(runtime, "Runtime is required");
    }

    public ShutdownHookIntegration() {
        this(Runtime.getRuntime());
    }

    @Override
    public void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        this.runtime.addShutdownHook(new Thread(() -> hub.close()));
    }
}

