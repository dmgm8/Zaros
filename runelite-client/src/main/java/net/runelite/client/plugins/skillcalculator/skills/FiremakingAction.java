/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.plugins.skillcalculator.skills.ItemSkillAction;

public enum FiremakingAction implements ItemSkillAction
{
    LOGS(1511, 1, 40.0f),
    ACHEY_TREE_LOGS(2862, 1, 40.0f),
    OAK_LOGS(1521, 15, 60.0f),
    WILLOW_LOGS(1519, 30, 90.0f),
    TEAK_LOGS(6333, 35, 105.0f),
    ARCTIC_PINE_LOGS(10810, 42, 125.0f),
    MAPLE_LOGS(1517, 45, 135.0f),
    MAHOGANY_LOGS(6332, 50, 157.5f),
    YEW_LOGS(1515, 60, 202.5f),
    BLISTERWOOD_LOGS(24691, 62, 96.0f),
    MAGIC_LOGS(1513, 75, 303.8f),
    REDWOOD_LOGS(19669, 90, 350.0f);

    private final int itemId;
    private final int level;
    private final float xp;

    private FiremakingAction(int itemId, int level, float xp) {
        this.itemId = itemId;
        this.level = level;
        this.xp = xp;
    }

    @Override
    public int getItemId() {
        return this.itemId;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public float getXp() {
        return this.xp;
    }
}

