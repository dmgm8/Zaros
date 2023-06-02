/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

import io.sentry.SentryOptions;
import io.sentry.cache.IEnvelopeCache;
import io.sentry.transport.AsyncConnection;

final class AsyncConnectionFactory {
    private AsyncConnectionFactory() {
    }

    public static AsyncConnection create(SentryOptions options, IEnvelopeCache envelopeCache) {
        return new AsyncConnection(options.getTransport(), options.getTransportGate(), envelopeCache, options.getMaxQueueSize(), options);
    }
}

