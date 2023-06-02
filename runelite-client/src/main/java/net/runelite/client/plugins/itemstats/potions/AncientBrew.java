/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.itemstats.potions;

import java.util.Comparator;
import java.util.stream.Stream;
import net.runelite.api.Client;
import net.runelite.client.plugins.itemstats.BoostedStatBoost;
import net.runelite.client.plugins.itemstats.Builders;
import net.runelite.client.plugins.itemstats.CappedStatBoost;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.SimpleStatBoost;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.stats.Stat;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class AncientBrew
implements Effect {
    private static final Stat[] LOWERED_STATS = new Stat[]{Stats.ATTACK, Stats.STRENGTH, Stats.DEFENCE};
    private static final CappedStatBoost PRAYER_BOOST = new CappedStatBoost(Stats.PRAYER, Builders.perc(0.1, 2), Builders.perc(0.05, 0));
    private static final SimpleStatBoost MAGIC_BOOST = new SimpleStatBoost(Stats.MAGIC, true, Builders.perc(0.05, 2));
    private static final BoostedStatBoost MELEE_DRAIN = new BoostedStatBoost(null, false, Builders.perc(0.1, -2));

    @Override
    public StatsChanges calculate(Client client) {
        StatsChanges changes = new StatsChanges(0);
        changes.setStatChanges((StatChange[])Stream.of(Stream.of(PRAYER_BOOST.effect(client)), Stream.of(MAGIC_BOOST.effect(client)), Stream.of(LOWERED_STATS).filter(stat -> 1 < stat.getValue(client)).map(stat -> {
            MELEE_DRAIN.setStat((Stat)stat);
            return MELEE_DRAIN.effect(client);
        })).reduce(Stream::concat).orElseGet(Stream::empty).toArray(StatChange[]::new));
        changes.setPositivity(Stream.of(changes.getStatChanges()).map(StatChange::getPositivity).max(Comparator.naturalOrder()).get());
        return changes;
    }
}

