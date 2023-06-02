/*
 * Decompiled with CFR 0.150.
 */
package io.sentry.transport;

import io.sentry.SentryEnvelope;
import io.sentry.transport.TransportResult;
import java.io.Closeable;
import java.io.IOException;

public interface ITransport
extends Closeable {
    public boolean isRetryAfter(String var1);

    public TransportResult send(SentryEnvelope var1) throws IOException;
}

