/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.kit;

public enum KitType {
    HEAD,
    CAPE,
    AMULET,
    WEAPON,
    TORSO,
    SHIELD,
    ARMS,
    LEGS,
    HAIR,
    HANDS,
    BOOTS,
    JAW;


    public int getIndex() {
        return this.ordinal();
    }
}

