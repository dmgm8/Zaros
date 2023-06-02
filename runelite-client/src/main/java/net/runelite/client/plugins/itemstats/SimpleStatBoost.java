/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.itemstats;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.StatBoost;
import net.runelite.client.plugins.itemstats.delta.DeltaCalculator;
import net.runelite.client.plugins.itemstats.stats.Stat;

public class SimpleStatBoost
extends StatBoost {
    private final DeltaCalculator deltaCalculator;

    public SimpleStatBoost(Stat stat, boolean boost, DeltaCalculator deltaCalculator) {
        super(stat, boost);
        this.deltaCalculator = deltaCalculator;
    }

    @Override
    public int heals(Client client) {
        int max = this.getStat().getMaximum(client);
        return this.deltaCalculator.calculateDelta(max);
    }
}

