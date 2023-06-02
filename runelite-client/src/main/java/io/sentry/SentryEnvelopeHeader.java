/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.protocol.SdkVersion;
import io.sentry.protocol.SentryId;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class SentryEnvelopeHeader {
    @Nullable
    private final SentryId eventId;
    @Nullable
    private final SdkVersion sdkVersion;

    public SentryEnvelopeHeader(@Nullable SentryId eventId, @Nullable SdkVersion sdkVersion) {
        this.eventId = eventId;
        this.sdkVersion = sdkVersion;
    }

    public SentryEnvelopeHeader(@Nullable SentryId eventId) {
        this(eventId, null);
    }

    public SentryEnvelopeHeader() {
        this(new SentryId());
    }

    @Nullable
    public SentryId getEventId() {
        return this.eventId;
    }

    @Nullable
    public SdkVersion getSdkVersion() {
        return this.sdkVersion;
    }
}

