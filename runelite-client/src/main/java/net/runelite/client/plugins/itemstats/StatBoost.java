/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.itemstats;

import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.Positivity;
import net.runelite.client.plugins.itemstats.SingleEffect;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.stats.Stat;

public abstract class StatBoost
extends SingleEffect {
    private Stat stat;
    private boolean boost;

    public StatBoost(Stat stat, boolean boost) {
        this.stat = stat;
        this.boost = boost;
    }

    public abstract int heals(Client var1);

    @Override
    public StatChange effect(Client client) {
        int newValue;
        int value = this.stat.getValue(client);
        int max = this.stat.getMaximum(client);
        boolean hitCap = false;
        int calcedDelta = this.heals(client);
        if (this.boost && calcedDelta > 0) {
            max += calcedDelta;
        }
        if (value > max) {
            max = value;
        }
        if ((newValue = value + calcedDelta) > max) {
            newValue = max;
            hitCap = true;
        }
        if (newValue < 0) {
            newValue = 0;
        }
        int delta = newValue - value;
        StatChange out = new StatChange();
        out.setStat(this.stat);
        if (delta > 0) {
            out.setPositivity(hitCap ? Positivity.BETTER_CAPPED : Positivity.BETTER_UNCAPPED);
        } else if (delta == 0) {
            out.setPositivity(Positivity.NO_CHANGE);
        } else {
            out.setPositivity(Positivity.WORSE);
        }
        out.setAbsolute(newValue);
        out.setRelative(delta);
        out.setTheoretical(calcedDelta);
        return out;
    }

    public Stat getStat() {
        return this.stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public boolean isBoost() {
        return this.boost;
    }

    public void setBoost(boolean boost) {
        this.boost = boost;
    }
}

