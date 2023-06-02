/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public enum Prayer {
    THICK_SKIN(4104, 5.0),
    BURST_OF_STRENGTH(4105, 5.0),
    CLARITY_OF_THOUGHT(4106, 5.0),
    SHARP_EYE(4122, 5.0),
    MYSTIC_WILL(4123, 5.0),
    ROCK_SKIN(4107, 10.0),
    SUPERHUMAN_STRENGTH(4108, 10.0),
    IMPROVED_REFLEXES(4109, 10.0),
    RAPID_RESTORE(4110, 1.6666666666666667),
    RAPID_HEAL(4111, 3.3333333333333335),
    PROTECT_ITEM(4112, 3.3333333333333335),
    HAWK_EYE(4124, 10.0),
    MYSTIC_LORE(4125, 10.0),
    STEEL_SKIN(4113, 20.0),
    ULTIMATE_STRENGTH(4114, 20.0),
    INCREDIBLE_REFLEXES(4115, 20.0),
    PROTECT_FROM_MAGIC(4116, 20.0),
    PROTECT_FROM_MISSILES(4117, 20.0),
    PROTECT_FROM_MELEE(4118, 20.0),
    EAGLE_EYE(4126, 20.0),
    MYSTIC_MIGHT(4127, 20.0),
    RETRIBUTION(4119, 5.0),
    REDEMPTION(4120, 10.0),
    SMITE(4121, 30.0),
    CHIVALRY(4128, 40.0),
    PIETY(4129, 40.0),
    PRESERVE(5466, 3.3333333333333335),
    RIGOUR(5464, 40.0),
    AUGURY(5465, 40.0);

    private final int varbit;
    private final double drainRate;

    private Prayer(int varbit, double drainRate) {
        this.varbit = varbit;
        this.drainRate = drainRate;
    }

    public int getVarbit() {
        return this.varbit;
    }

    public double getDrainRate() {
        return this.drainRate;
    }
}

