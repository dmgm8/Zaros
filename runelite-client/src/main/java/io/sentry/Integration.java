/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

import io.sentry.IHub;
import io.sentry.SentryOptions;

public interface Integration {
    public void register(IHub var1, SentryOptions var2);
}

