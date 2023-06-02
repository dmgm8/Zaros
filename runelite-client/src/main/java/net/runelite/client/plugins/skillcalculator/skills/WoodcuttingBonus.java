/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.plugins.skillcalculator.skills.SkillBonus;

public enum WoodcuttingBonus implements SkillBonus
{
    LUMBERJACK_OUTFIT("Lumberjack Outfit (+2.5%)", 1.025f);

    private final String name;
    private final float value;

    private WoodcuttingBonus(String name, float value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getValue() {
        return this.value;
    }
}

