/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.wintertodt;

enum WintertodtInterruptType {
    COLD("Damaged by Wintertodt Cold"),
    SNOWFALL("Damaged by Wintertodt Snowfall"),
    BRAZIER("Brazier Shattered"),
    INVENTORY_FULL("Inventory full of Bruma Roots"),
    OUT_OF_ROOTS("Out of Bruma Roots"),
    FIXED_BRAZIER("Fixed Brazier"),
    LIT_BRAZIER("Lit Brazier"),
    BRAZIER_WENT_OUT("Brazier went out");

    private final String interruptSourceString;

    private WintertodtInterruptType(String interruptSourceString) {
        this.interruptSourceString = interruptSourceString;
    }

    public String getInterruptSourceString() {
        return this.interruptSourceString;
    }
}

