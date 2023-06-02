/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.vars;

public enum InputType {
    RUNELITE_CHATBOX_PANEL(-3),
    RUNELITE(-2),
    NONE(0),
    PRIVATE_MESSAGE(6),
    SEARCH(11);

    private final int type;

    public int getType() {
        return this.type;
    }

    private InputType(int type) {
        this.type = type;
    }
}

