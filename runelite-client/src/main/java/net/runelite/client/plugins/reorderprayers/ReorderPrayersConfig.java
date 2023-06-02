/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Prayer
 */
package net.runelite.client.plugins.reorderprayers;

import net.runelite.api.Prayer;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.reorderprayers.ReorderPrayersPlugin;

@ConfigGroup(value="reorderprayers")
public interface ReorderPrayersConfig
extends Config {
    @ConfigItem(keyName="unlockPrayerReordering", name="Unlock Prayer Reordering", description="Configures whether or not you can reorder the prayers", position=1)
    default public boolean unlockPrayerReordering() {
        return false;
    }

    @ConfigItem(keyName="unlockPrayerReordering", name="", description="")
    public void unlockPrayerReordering(boolean var1);

    @ConfigItem(keyName="prayerOrder", name="Prayer Order", description="Configures the order of the prayers", hidden=true, position=2)
    default public String prayerOrder() {
        return ReorderPrayersPlugin.prayerOrderToString(Prayer.values());
    }

    @ConfigItem(keyName="prayerOrder", name="", description="")
    public void prayerOrder(String var1);
}

