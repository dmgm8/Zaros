/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.itemstats.stats;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.stats.Stat;

public class EnergyStat
extends Stat {
    EnergyStat() {
        super("Run Energy");
    }

    @Override
    public int getValue(Client client) {
        return client.getEnergy();
    }

    @Override
    public int getMaximum(Client client) {
        return 100;
    }
}

