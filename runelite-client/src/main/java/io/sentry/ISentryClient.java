/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.Scope;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.Session;
import io.sentry.protocol.Message;
import io.sentry.protocol.SentryId;
import org.jetbrains.annotations.Nullable;

public interface ISentryClient {
    public boolean isEnabled();

    public SentryId captureEvent(SentryEvent var1, @Nullable Scope var2, @Nullable Object var3);

    public void close();

    public void flush(long var1);

    default public SentryId captureEvent(SentryEvent event) {
        return this.captureEvent(event, null, null);
    }

    default public SentryId captureEvent(SentryEvent event, @Nullable Scope scope) {
        return this.captureEvent(event, scope, null);
    }

    default public SentryId captureEvent(SentryEvent event, @Nullable Object hint) {
        return this.captureEvent(event, null, hint);
    }

    default public SentryId captureMessage(String message, SentryLevel level, @Nullable Scope scope) {
        SentryEvent event = new SentryEvent();
        Message sentryMessage = new Message();
        sentryMessage.setFormatted(message);
        event.setMessage(sentryMessage);
        event.setLevel(level);
        return this.captureEvent(event, scope);
    }

    default public SentryId captureMessage(String message, SentryLevel level) {
        return this.captureMessage(message, level, null);
    }

    default public SentryId captureException(Throwable throwable) {
        return this.captureException(throwable, null, null);
    }

    default public SentryId captureException(Throwable throwable, @Nullable Scope scope, @Nullable Object hint) {
        SentryEvent event = new SentryEvent(throwable);
        return this.captureEvent(event, scope, hint);
    }

    default public SentryId captureException(Throwable throwable, @Nullable Object hint) {
        return this.captureException(throwable, null, hint);
    }

    default public SentryId captureException(Throwable throwable, @Nullable Scope scope) {
        return this.captureException(throwable, scope, null);
    }

    public void captureSession(Session var1, @Nullable Object var2);

    default public void captureSession(Session session) {
        this.captureSession(session, null);
    }

    public SentryId captureEnvelope(SentryEnvelope var1, @Nullable Object var2);

    default public SentryId captureEnvelope(SentryEnvelope envelope) {
        return this.captureEnvelope(envelope, null);
    }
}

