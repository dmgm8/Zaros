/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.plugins.skillcalculator.skills.SkillBonus;

public enum PrayerBonus implements SkillBonus
{
    LIT_GILDED_ALTAR("Lit Gilded Altar (350%)", 3.5f),
    ECTOFUNTUS("Ectofuntus (400%)", 4.0f),
    CHAOS_ALTAR("Chaos Altar (700%)", 7.0f),
    MORYTANIA_DIARY_3_SHADES("Morytania Diary 3 Shades (150%)", 1.5f),
    BONECRUSHER("Bonecrusher (50%)", 0.5f),
    SINISTER_OFFERING("Sinister Offering (300%)", 3.0f),
    DEMONIC_OFFERING("Demonic Offering (300%)", 3.0f),
    SACRED_BONE_BURNER("Sacred Bone Burner (300%)", 3.0f);

    static final PrayerBonus[] BONE_BONUSES;
    private final String name;
    private final float value;

    private PrayerBonus(String name, float value) {
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

    static {
        BONE_BONUSES = new PrayerBonus[]{LIT_GILDED_ALTAR, ECTOFUNTUS, CHAOS_ALTAR, BONECRUSHER, SINISTER_OFFERING, SACRED_BONE_BURNER};
    }
}

