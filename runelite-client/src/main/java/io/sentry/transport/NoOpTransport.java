/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry.transport;

import io.sentry.SentryEnvelope;
import io.sentry.transport.ITransport;
import io.sentry.transport.TransportResult;
import java.io.IOException;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class NoOpTransport
implements ITransport {
    private static final NoOpTransport instance = new NoOpTransport();

    public static NoOpTransport getInstance() {
        return instance;
    }

    private NoOpTransport() {
    }

    @Override
    public boolean isRetryAfter(String type) {
        return false;
    }

    @Override
    public TransportResult send(SentryEnvelope envelope) throws IOException {
        return TransportResult.success();
    }

    @Override
    public void close() throws IOException {
    }
}

