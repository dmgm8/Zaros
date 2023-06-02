/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public enum EquipmentInventorySlot {
    HEAD(0),
    CAPE(1),
    AMULET(2),
    WEAPON(3),
    BODY(4),
    SHIELD(5),
    LEGS(7),
    GLOVES(9),
    BOOTS(10),
    RING(12),
    AMMO(13);

    private final int slotIdx;

    private EquipmentInventorySlot(int slotIdx) {
        this.slotIdx = slotIdx;
    }

    public int getSlotIdx() {
        return this.slotIdx;
    }
}

