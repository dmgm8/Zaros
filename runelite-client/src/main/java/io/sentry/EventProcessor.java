/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.SentryEvent;
import org.jetbrains.annotations.Nullable;

public interface EventProcessor {
    @Nullable
    public SentryEvent process(SentryEvent var1, @Nullable Object var2);
}

