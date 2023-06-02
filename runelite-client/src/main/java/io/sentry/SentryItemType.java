/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public enum SentryItemType {
    Session("session"),
    Event("event"),
    Attachment("attachment"),
    Transaction("transaction"),
    Unknown("__unknown__");

    private final String itemType;

    private SentryItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return this.itemType;
    }
}

