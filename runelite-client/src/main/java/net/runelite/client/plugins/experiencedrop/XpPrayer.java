/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Prayer
 */
package net.runelite.client.plugins.experiencedrop;

import net.runelite.api.Prayer;
import net.runelite.client.plugins.experiencedrop.PrayerType;

enum XpPrayer {
    XP_BURST_OF_STRENGTH(Prayer.BURST_OF_STRENGTH, PrayerType.MELEE),
    XP_CLARITY_OF_THOUGHT(Prayer.CLARITY_OF_THOUGHT, PrayerType.MELEE),
    XP_SHARP_EYE(Prayer.SHARP_EYE, PrayerType.RANGE),
    XP_MYSTIC_WILL(Prayer.MYSTIC_WILL, PrayerType.MAGIC),
    XP_SUPERHUMAN_STRENGTH(Prayer.SUPERHUMAN_STRENGTH, PrayerType.MELEE),
    XP_IMPROVED_REFLEXES(Prayer.IMPROVED_REFLEXES, PrayerType.MELEE),
    XP_HAWK_EYE(Prayer.HAWK_EYE, PrayerType.RANGE),
    XP_MYSTIC_LORE(Prayer.MYSTIC_LORE, PrayerType.MAGIC),
    XP_ULTIMATE_STRENGTH(Prayer.ULTIMATE_STRENGTH, PrayerType.MELEE),
    XP_INCREDIBLE_REFLEXES(Prayer.INCREDIBLE_REFLEXES, PrayerType.MELEE),
    XP_EAGLE_EYE(Prayer.EAGLE_EYE, PrayerType.RANGE),
    XP_MYSTIC_MIGHT(Prayer.MYSTIC_MIGHT, PrayerType.MAGIC),
    XP_CHIVALRY(Prayer.CHIVALRY, PrayerType.MELEE),
    XP_PIETY(Prayer.PIETY, PrayerType.MELEE),
    XP_RIGOUR(Prayer.RIGOUR, PrayerType.RANGE),
    XP_AUGURY(Prayer.AUGURY, PrayerType.MAGIC);

    private final Prayer prayer;
    private final PrayerType type;

    private XpPrayer(Prayer prayer, PrayerType type) {
        this.prayer = prayer;
        this.type = type;
    }

    public Prayer getPrayer() {
        return this.prayer;
    }

    public PrayerType getType() {
        return this.type;
    }
}

