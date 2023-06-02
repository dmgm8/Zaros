/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.prayer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.prayer.PrayerFlickLocation;

@ConfigGroup(value="prayer")
public interface PrayerConfig
extends Config {
    @ConfigItem(position=0, keyName="prayerFlickLocation", name="Pray flick location", description="Choose where to display the prayer flick helper.")
    default public PrayerFlickLocation prayerFlickLocation() {
        return PrayerFlickLocation.NONE;
    }

    @ConfigItem(position=1, keyName="prayerFlickAlwaysOn", name="Never hide prayer flick helper", description="Show prayer flick helper regardless of if you're praying or not.")
    default public boolean prayerFlickAlwaysOn() {
        return false;
    }

    @ConfigItem(position=2, keyName="prayerIndicator", name="Boost indicator", description="Enable infoboxes for prayers.")
    default public boolean prayerIndicator() {
        return false;
    }

    @ConfigItem(position=3, keyName="prayerIndicatorOverheads", name="Overhead indicator", description="Also enable infoboxes for overheads.")
    default public boolean prayerIndicatorOverheads() {
        return false;
    }

    @ConfigItem(position=4, keyName="showPrayerDoseIndicator", name="Show prayer dose indicator", description="Enables the prayer dose indicator.")
    default public boolean showPrayerDoseIndicator() {
        return true;
    }

    @ConfigItem(position=5, keyName="showPrayerTooltip", name="Show prayer orb tooltip", description="Displays time remaining and prayer bonus as a tooltip on the quick-prayer icon.")
    default public boolean showPrayerStatistics() {
        return true;
    }

    @ConfigItem(position=6, keyName="showPrayerBar", name="Show prayer bar", description="Displays prayer bar under HP bar when praying.")
    default public boolean showPrayerBar() {
        return false;
    }

    @ConfigItem(position=7, keyName="prayerBarHideIfNotPraying", name="Hide bar while prayer is inactive", description="Prayer bar will be hidden while prayers are inactive.")
    default public boolean hideIfNotPraying() {
        return true;
    }

    @ConfigItem(position=8, keyName="prayerBarHideIfNonCombat", name="Hide bar while out-of-combat", description="Prayer bar will be hidden while out-of-combat.")
    default public boolean hideIfOutOfCombat() {
        return false;
    }

    @ConfigItem(position=9, keyName="replaceOrbText", name="Show time left", description="Show time remaining of current prayers in the prayer orb.")
    default public boolean replaceOrbText() {
        return false;
    }
}

