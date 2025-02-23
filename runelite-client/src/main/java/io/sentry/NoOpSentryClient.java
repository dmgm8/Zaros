/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.ISentryClient;
import io.sentry.Scope;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEvent;
import io.sentry.Session;
import io.sentry.protocol.SentryId;
import org.jetbrains.annotations.Nullable;

final class NoOpSentryClient
implements ISentryClient {
    private static final NoOpSentryClient instance = new NoOpSentryClient();

    private NoOpSentryClient() {
    }

    public static NoOpSentryClient getInstance() {
        return instance;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public SentryId captureEvent(SentryEvent event, @Nullable Scope scope, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }

    @Override
    public void close() {
    }

    @Override
    public void flush(long timeoutMillis) {
    }

    @Override
    public void captureSession(Session session, @Nullable Object hint) {
    }

    @Override
    public SentryId captureEnvelope(SentryEnvelope envelope, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }
}

