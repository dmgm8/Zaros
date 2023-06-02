/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.SentryItemType;
import io.sentry.util.Objects;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class SentryEnvelopeItemHeader {
    @Nullable
    private final String contentType;
    @Nullable
    private final String fileName;
    @NotNull
    private final SentryItemType type;
    private final int length;
    @Nullable
    private final Callable<Integer> getLength;

    @NotNull
    public SentryItemType getType() {
        return this.type;
    }

    public int getLength() {
        if (this.getLength != null) {
            try {
                return this.getLength.call();
            }
            catch (Exception ignored) {
                return -1;
            }
        }
        return this.length;
    }

    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    @Nullable
    public String getFileName() {
        return this.fileName;
    }

    SentryEnvelopeItemHeader(@NotNull SentryItemType type, int length, @Nullable String contentType, @Nullable String fileName) {
        this.type = Objects.requireNonNull(type, "type is required");
        this.contentType = contentType;
        this.length = length;
        this.fileName = fileName;
        this.getLength = null;
    }

    SentryEnvelopeItemHeader(@NotNull SentryItemType type, @Nullable Callable<Integer> getLength, @Nullable String contentType, @Nullable String fileName) {
        this.type = Objects.requireNonNull(type, "type is required");
        this.contentType = contentType;
        this.length = -1;
        this.fileName = fileName;
        this.getLength = getLength;
    }
}

