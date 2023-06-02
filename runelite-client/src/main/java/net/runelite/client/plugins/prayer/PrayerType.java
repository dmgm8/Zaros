/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Prayer
 */
package net.runelite.client.plugins.prayer;

import net.runelite.api.Prayer;

enum PrayerType {
    THICK_SKIN("Thick Skin", Prayer.THICK_SKIN, "+5% Defence", 115, false),
    BURST_OF_STRENGTH("Burst of Strength", Prayer.BURST_OF_STRENGTH, "+5% Strength", 116, false),
    CLARITY_OF_THOUGHT("Clarity of Thought", Prayer.CLARITY_OF_THOUGHT, "+5% Attack", 117, false),
    SHARP_EYE("Sharp Eye", Prayer.SHARP_EYE, "+5% Ranged", 133, false),
    MYSTIC_WILL("Mystic Will", Prayer.MYSTIC_WILL, "+5% Magical attack and defence", 134, false),
    ROCK_SKIN("Rock Skin", Prayer.ROCK_SKIN, "+10% Defence", 118, false),
    SUPERHUMAN_STRENGTH("Superhuman Strength", Prayer.SUPERHUMAN_STRENGTH, "+10% Strength", 119, false),
    IMPROVED_REFLEXES("Improved Reflexes", Prayer.IMPROVED_REFLEXES, "+10% Attack", 120, false),
    RAPID_RESTORE("Rapid Restore", Prayer.RAPID_RESTORE, "2 x Restore rate for all skills except Hitpoints and Prayer", 121, false),
    RAPID_HEAL("Rapid Heal", Prayer.RAPID_HEAL, "2 x Restore rate for Hitpoints", 122, false),
    PROTECT_ITEM("Protect Item", Prayer.PROTECT_ITEM, "Player keeps 1 extra item when they die", 123, false),
    HAWK_EYE("Hawk Eye", Prayer.HAWK_EYE, "+10% Ranged", 502, false),
    MYSTIC_LORE("Mystic Lore", Prayer.MYSTIC_LORE, "+10% Magical attack and defence", 503, false),
    STEEL_SKIN("Steel Skin", Prayer.STEEL_SKIN, "+15% Defence", 124, false),
    ULTIMATE_STRENGTH("Ultimate Strength", Prayer.ULTIMATE_STRENGTH, "+15% Strength", 125, false),
    INCREDIBLE_REFLEXES("Incredible reflexes", Prayer.INCREDIBLE_REFLEXES, "+15% Attack", 126, false),
    PROTECT_FROM_MAGIC("protect from magic", Prayer.PROTECT_FROM_MAGIC, "Protects against magic attacks", 127, true),
    PROTECT_FROM_MISSILES("Protect from missiles", Prayer.PROTECT_FROM_MISSILES, "Protects against ranged attacks", 128, true),
    PROTECT_FROM_MELEE("Protect from melee", Prayer.PROTECT_FROM_MELEE, "Protects against melee attacks", 129, true),
    EAGLE_EYE("Eagle Eye", Prayer.EAGLE_EYE, "+15% Ranged", 504, false),
    MYSTIC_MIGHT("Mystic Might", Prayer.MYSTIC_MIGHT, "+15% Magical attack and defence", 505, false),
    RETRIBUTION("Retribution", Prayer.RETRIBUTION, "Deals damage up to 25% of your Prayer level to nearby targets upon the user's death", 131, true),
    REDEMPTION("Redemption", Prayer.REDEMPTION, "Heals the player if they fall below 10% health", 130, true),
    SMITE("Smite", Prayer.SMITE, "Removes 1 Prayer point from an enemy for every 4 damage inflicted on the enemy", 132, true),
    PRESERVE("Preserve", Prayer.PRESERVE, "Boosted stats last 50% longer", 947, false),
    CHIVALRY("Chivalry", Prayer.CHIVALRY, "+15% Attack, +18% Strength, +20% Defence", 945, false),
    PIETY("Piety", Prayer.PIETY, "+20% Attack, +23% Strength, +25% Defence", 946, false),
    RIGOUR("Rigour", Prayer.RIGOUR, "+20% Ranged attack, +23% Ranged strength, +25% Defence", 1420, false),
    AUGURY("Augury", Prayer.AUGURY, "+25% Magical attack and defence, +25% Defence", 1421, false);

    private final String name;
    private final Prayer prayer;
    private final String description;
    private final int spriteID;
    private final boolean overhead;

    private PrayerType(String name, Prayer prayer, String description, int spriteID, boolean overhead) {
        this.name = name;
        this.prayer = prayer;
        this.description = description;
        this.spriteID = spriteID;
        this.overhead = overhead;
    }

    public String getName() {
        return this.name;
    }

    public Prayer getPrayer() {
        return this.prayer;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSpriteID() {
        return this.spriteID;
    }

    public boolean isOverhead() {
        return this.overhead;
    }
}

