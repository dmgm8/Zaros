/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

import io.sentry.SentryLevel;

public interface ILogger {
    public void log(SentryLevel var1, String var2, Object ... var3);

    public void log(SentryLevel var1, String var2, Throwable var3);

    public void log(SentryLevel var1, Throwable var2, String var3, Object ... var4);
}

