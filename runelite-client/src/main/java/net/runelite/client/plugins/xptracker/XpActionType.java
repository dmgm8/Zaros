/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.xptracker;

public enum XpActionType {
    EXPERIENCE("Actions"),
    ACTOR_HEALTH("Kills");

    private final String label;

    public String getLabel() {
        return this.label;
    }

    private XpActionType(String label) {
        this.label = label;
    }
}

