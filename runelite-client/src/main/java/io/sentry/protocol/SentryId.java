/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.protocol;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SentryId {
    @NotNull
    private final UUID uuid;
    public static final SentryId EMPTY_ID = new SentryId(new UUID(0L, 0L));

    public SentryId() {
        this((UUID)null);
    }

    public SentryId(@Nullable UUID uuid) {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        this.uuid = uuid;
    }

    public SentryId(String sentryIdString) {
        this.uuid = this.fromStringSentryId(sentryIdString);
    }

    public String toString() {
        return this.uuid.toString().replace("-", "");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SentryId sentryId = (SentryId)o;
        return this.uuid.compareTo(sentryId.uuid) == 0;
    }

    public int hashCode() {
        return this.uuid.hashCode();
    }

    private UUID fromStringSentryId(String sentryIdString) {
        if (sentryIdString == null) {
            return null;
        }
        if (sentryIdString.length() == 32) {
            sentryIdString = new StringBuilder(sentryIdString).insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-").toString();
        }
        if (sentryIdString.length() != 36) {
            throw new IllegalArgumentException("String representation of SentryId has either 32 (UUID no dashes) or 36 characters long (completed UUID). Received: " + sentryIdString);
        }
        return UUID.fromString(sentryIdString);
    }
}

