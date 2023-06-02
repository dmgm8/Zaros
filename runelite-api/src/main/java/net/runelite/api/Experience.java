/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public class Experience {
    public static final int MAX_REAL_LEVEL = 99;
    public static final int MAX_VIRT_LEVEL = 126;
    public static final int MAX_SKILL_XP = 200000000;
    public static final int MAX_COMBAT_LEVEL = 126;
    private static final int[] XP_FOR_LEVEL = new int[126];

    public static int getXpForLevel(int level) {
        if (level < 1 || level > 126) {
            throw new IllegalArgumentException(level + " is not a valid level");
        }
        return XP_FOR_LEVEL[level - 1];
    }

    public static int getLevelForXp(int xp) {
        if (xp < 0) {
            throw new IllegalArgumentException("XP (" + xp + ") must not be negative");
        }
        int low = 0;
        int high = XP_FOR_LEVEL.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int xpForLevel = XP_FOR_LEVEL[mid];
            if (xp < xpForLevel) {
                high = mid - 1;
                continue;
            }
            if (xp > xpForLevel) {
                low = mid + 1;
                continue;
            }
            return mid + 1;
        }
        return high + 1;
    }

    private static double getMeleeRangeOrMagicCombatLevelContribution(int attackLevel, int strengthLevel, int magicLevel, int rangeLevel) {
        double melee = 0.325 * (double)(attackLevel + strengthLevel);
        double range = 0.325 * (double)(rangeLevel / 2 + rangeLevel);
        double magic = 0.325 * (double)(magicLevel / 2 + magicLevel);
        return Math.max(melee, Math.max(range, magic));
    }

    public static double getCombatLevelPrecise(int attackLevel, int strengthLevel, int defenceLevel, int hitpointsLevel, int magicLevel, int rangeLevel, int prayerLevel) {
        double base = 0.25 * (double)(defenceLevel + hitpointsLevel + prayerLevel / 2);
        double typeContribution = Experience.getMeleeRangeOrMagicCombatLevelContribution(attackLevel, strengthLevel, magicLevel, rangeLevel);
        return base + typeContribution;
    }

    public static int getCombatLevel(int attackLevel, int strengthLevel, int defenceLevel, int hitpointsLevel, int magicLevel, int rangeLevel, int prayerLevel) {
        return (int)Experience.getCombatLevelPrecise(attackLevel, strengthLevel, defenceLevel, hitpointsLevel, magicLevel, rangeLevel, prayerLevel);
    }

    public static int getNextCombatLevelMelee(int attackLevel, int strengthLevel, int defenceLevel, int hitpointsLevel, int magicLevel, int rangeLevel, int prayerLevel) {
        int nextCombatLevel = Experience.getCombatLevel(attackLevel, strengthLevel, defenceLevel, hitpointsLevel, magicLevel, rangeLevel, prayerLevel) + 1;
        return (int)Math.ceil(-0.7692307692307693 * ((double)(defenceLevel + hitpointsLevel) + Math.floor(prayerLevel / 2) - (double)(4 * nextCombatLevel))) - strengthLevel - attackLevel;
    }

    public static int getNextCombatLevelHpDef(int attackLevel, int strengthLevel, int defenceLevel, int hitpointsLevel, int magicLevel, int rangeLevel, int prayerLevel) {
        int nextCombatLevel = Experience.getCombatLevel(attackLevel, strengthLevel, defenceLevel, hitpointsLevel, magicLevel, rangeLevel, prayerLevel) + 1;
        double typeContribution = Experience.getMeleeRangeOrMagicCombatLevelContribution(attackLevel, strengthLevel, magicLevel, rangeLevel);
        return (int)Math.ceil((double)(4 * nextCombatLevel) - Math.floor(prayerLevel / 2) - 4.0 * typeContribution) - hitpointsLevel - defenceLevel;
    }

    public static int getNextCombatLevelMagic(int attackLevel, int strengthLevel, int defenceLevel, int hitpointsLevel, int magicLevel, int rangeLevel, int prayerLevel) {
        int nextCombatLevel = Experience.getCombatLevel(attackLevel, strengthLevel, defenceLevel, hitpointsLevel, magicLevel, rangeLevel, prayerLevel) + 1;
        return (int)Math.ceil(0.6666666666666666 * Math.ceil(-0.7692307692307693 * ((double)(hitpointsLevel + defenceLevel - 4 * nextCombatLevel) + Math.floor(prayerLevel / 2)))) - magicLevel;
    }

    public static int getNextCombatLevelRange(int attackLevel, int strengthLevel, int defenceLevel, int hitpointsLevel, int magicLevel, int rangeLevel, int prayerLevel) {
        int nextCombatLevel = Experience.getCombatLevel(attackLevel, strengthLevel, defenceLevel, hitpointsLevel, magicLevel, rangeLevel, prayerLevel) + 1;
        return (int)Math.ceil(0.6666666666666666 * Math.ceil(-0.7692307692307693 * ((double)(hitpointsLevel + defenceLevel - 4 * nextCombatLevel) + Math.floor(prayerLevel / 2)))) - rangeLevel;
    }

    public static int getNextCombatLevelPrayer(int attackLevel, int strengthLevel, int defenceLevel, int hitpointsLevel, int magicLevel, int rangeLevel, int prayerLevel) {
        int nextCombatLevel = Experience.getCombatLevel(attackLevel, strengthLevel, defenceLevel, hitpointsLevel, magicLevel, rangeLevel, prayerLevel) + 1;
        double typeContribution = Experience.getMeleeRangeOrMagicCombatLevelContribution(attackLevel, strengthLevel, magicLevel, rangeLevel);
        return 2 * (int)Math.ceil((double)(-hitpointsLevel - defenceLevel + 4 * nextCombatLevel) - 4.0 * typeContribution) - prayerLevel;
    }

    static {
        int xp = 0;
        for (int level = 1; level <= 126; ++level) {
            Experience.XP_FOR_LEVEL[level - 1] = xp / 4;
            int difference = (int)((double)level + 300.0 * Math.pow(2.0, (double)level / 7.0));
            xp += difference;
        }
    }
}

