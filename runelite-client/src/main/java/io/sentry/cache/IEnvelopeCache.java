/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.cache;

import io.sentry.SentryEnvelope;
import org.jetbrains.annotations.Nullable;

public interface IEnvelopeCache
extends Iterable<SentryEnvelope> {
    public void store(SentryEnvelope var1, @Nullable Object var2);

    default public void store(SentryEnvelope envelope) {
        this.store(envelope, null);
    }

    public void discard(SentryEnvelope var1);
}

