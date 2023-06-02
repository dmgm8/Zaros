/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.woodcutting;

import java.time.Instant;

class WoodcuttingSession {
    private Instant lastChopping;

    WoodcuttingSession() {
    }

    void setLastChopping() {
        this.lastChopping = Instant.now();
    }

    Instant getLastChopping() {
        return this.lastChopping;
    }
}

