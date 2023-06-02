/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

import java.util.concurrent.Future;

interface ISentryExecutorService {
    public Future<?> submit(Runnable var1);

    public void close(long var1);
}

