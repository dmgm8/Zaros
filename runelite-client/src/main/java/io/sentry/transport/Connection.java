/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.transport;

import io.sentry.SentryEnvelope;
import java.io.IOException;
import org.jetbrains.annotations.Nullable;

public interface Connection {
    public void send(SentryEnvelope var1, @Nullable Object var2) throws IOException;

    default public void send(SentryEnvelope envelope) throws IOException {
        this.send(envelope, null);
    }

    public void close() throws IOException;
}

