/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.ISerializer;
import io.sentry.SentryEnvelopeHeader;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryEvent;
import io.sentry.Session;
import io.sentry.protocol.SdkVersion;
import io.sentry.protocol.SentryId;
import io.sentry.util.Objects;
import java.io.IOException;
import java.util.ArrayList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class SentryEnvelope {
    @NotNull
    private final SentryEnvelopeHeader header;
    @NotNull
    private final Iterable<SentryEnvelopeItem> items;

    @NotNull
    public Iterable<SentryEnvelopeItem> getItems() {
        return this.items;
    }

    @NotNull
    public SentryEnvelopeHeader getHeader() {
        return this.header;
    }

    public SentryEnvelope(@NotNull SentryEnvelopeHeader header, @NotNull Iterable<SentryEnvelopeItem> items) {
        this.header = Objects.requireNonNull(header, "SentryEnvelopeHeader is required.");
        this.items = Objects.requireNonNull(items, "SentryEnvelope items are required.");
    }

    public SentryEnvelope(@Nullable SentryId eventId, @Nullable SdkVersion sdkVersion, @NotNull Iterable<SentryEnvelopeItem> items) {
        this.header = new SentryEnvelopeHeader(eventId, sdkVersion);
        this.items = Objects.requireNonNull(items, "SentryEnvelope items are required.");
    }

    public SentryEnvelope(@Nullable SentryId eventId, @Nullable SdkVersion sdkVersion, @NotNull SentryEnvelopeItem item) {
        Objects.requireNonNull(item, "SentryEnvelopeItem is required.");
        this.header = new SentryEnvelopeHeader(eventId, sdkVersion);
        ArrayList<SentryEnvelopeItem> items = new ArrayList<SentryEnvelopeItem>(1);
        items.add(item);
        this.items = items;
    }

    @NotNull
    public static SentryEnvelope fromSession(@NotNull ISerializer serializer, @NotNull Session session, @Nullable SdkVersion sdkVersion) throws IOException {
        Objects.requireNonNull(serializer, "Serializer is required.");
        Objects.requireNonNull(session, "session is required.");
        return new SentryEnvelope(null, sdkVersion, SentryEnvelopeItem.fromSession(serializer, session));
    }

    @NotNull
    public static SentryEnvelope fromEvent(@NotNull ISerializer serializer, @NotNull SentryEvent event, @Nullable SdkVersion sdkVersion) throws IOException {
        Objects.requireNonNull(serializer, "Serializer is required.");
        Objects.requireNonNull(event, "Event is required.");
        return new SentryEnvelope(event.getEventId(), sdkVersion, SentryEnvelopeItem.fromEvent(serializer, event));
    }
}

