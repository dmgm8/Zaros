/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 *  net.runelite.api.Skill
 */
package net.runelite.client.plugins.itemstats.potions;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.plugins.itemstats.Builders;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class GauntletPotion
implements Effect {
    private static final int PRAYER_RESTORE_DELTA = 7;
    private static final double PRAYER_RESTORE_PERCENT = 0.25;

    @Override
    public StatsChanges calculate(Client client) {
        int restorePerc = (int)((double)client.getRealSkillLevel(Skill.PRAYER) * 0.25);
        StatChange prayer = Builders.heal(Stats.PRAYER, restorePerc + 7).effect(client);
        StatChange runEnergy = Builders.heal(Stats.RUN_ENERGY, 40).effect(client);
        StatsChanges changes = new StatsChanges(2);
        changes.setStatChanges(new StatChange[]{runEnergy, prayer});
        return changes;
    }
}

