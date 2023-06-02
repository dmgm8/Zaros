/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public enum IconID {
    PLAYER_MODERATOR(0),
    JAGEX_MODERATOR(1),
    IRONMAN(2),
    ULTIMATE_IRONMAN(3),
    DMM_SKULL_5_KEYS(4),
    DMM_SKULL_4_KEYS(5),
    DMM_SKULL_3_KEYS(6),
    DMM_SKULL_2_KEYS(7),
    DMM_SKULL_1_KEYS(8),
    SKULL(9),
    HARDCORE_IRONMAN(10),
    NO_ENTRY(11),
    CHAIN_LINK(12),
    BOUNTY_HUNTER_EMBLEM(20),
    LEAGUE(22);

    private final int index;

    public String toString() {
        return "<img=" + String.valueOf(this.index) + ">";
    }

    private IconID(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}

