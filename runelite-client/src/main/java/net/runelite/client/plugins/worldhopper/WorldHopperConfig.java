/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.worldhopper;

import java.util.Collections;
import java.util.Set;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.plugins.worldhopper.RegionFilterMode;
import net.runelite.client.plugins.worldhopper.SubscriptionFilterMode;

@ConfigGroup(value="worldhopper")
public interface WorldHopperConfig
extends Config {
    public static final String GROUP = "worldhopper";

    @ConfigItem(keyName="previousKey", name="Quick-hop previous", description="When you press this key you'll hop to the previous world", position=0)
    default public Keybind previousKey() {
        return new Keybind(37, 192);
    }

    @ConfigItem(keyName="nextKey", name="Quick-hop next", description="When you press this key you'll hop to the next world", position=1)
    default public Keybind nextKey() {
        return new Keybind(39, 192);
    }

    @ConfigItem(keyName="quickhopOutOfDanger", name="Quick-hop out of dangerous worlds", description="Don't hop to a PVP/high risk world when quick-hopping", position=2)
    default public boolean quickhopOutOfDanger() {
        return true;
    }

    @ConfigItem(keyName="quickHopRegionFilter", name="Quick-hop region", description="Limit quick-hopping to worlds of a specific region", position=3)
    default public Set<RegionFilterMode> quickHopRegionFilter() {
        return Collections.emptySet();
    }

    @ConfigItem(keyName="showSidebar", name="Show world switcher sidebar", description="Show sidebar containing all worlds that mimics in-game interface", position=4)
    default public boolean showSidebar() {
        return true;
    }

    @ConfigItem(keyName="ping", name="Show world ping", description="Shows ping to each game world", position=5)
    default public boolean ping() {
        return true;
    }

    @ConfigItem(keyName="showMessage", name="Show world hop message in chat", description="Shows what world is being hopped to in the chat", position=6)
    default public boolean showWorldHopMessage() {
        return true;
    }

    @ConfigItem(keyName="menuOption", name="Show Hop-to menu option", description="Adds Hop-to menu option to the friends list and friends chat members list", position=7)
    default public boolean menuOption() {
        return true;
    }

    @ConfigItem(keyName="subscriptionFilter", name="Show subscription types", description="Only show free worlds, member worlds, or both types of worlds in sidebar", position=8)
    default public SubscriptionFilterMode subscriptionFilter() {
        return SubscriptionFilterMode.BOTH;
    }

    @ConfigItem(keyName="regionFilter", name="Filter worlds by region", description="Restrict sidebar worlds to one region", position=8)
    default public Set<RegionFilterMode> regionFilter() {
        return Collections.emptySet();
    }

    @ConfigItem(keyName="displayPing", name="Display current ping", description="Displays ping to current game world", position=9)
    default public boolean displayPing() {
        return false;
    }
}

