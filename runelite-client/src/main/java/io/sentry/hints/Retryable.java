/*
 * Decompiled with CFR 0.150.
 */
package io.sentry.hints;

public interface Retryable {
    public boolean isRetry();

    public void setRetry(boolean var1);
}

