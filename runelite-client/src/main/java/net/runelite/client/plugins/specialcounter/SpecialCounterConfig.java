/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.specialcounter;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="specialcounter")
public interface SpecialCounterConfig
extends Config {
    @ConfigItem(position=0, keyName="thresholdNotification", name="Threshold Notifications", description="Sends a notification when your special attack counter exceeds the threshold")
    default public boolean thresholdNotification() {
        return false;
    }

    @ConfigItem(position=1, keyName="specDrops", name="Spec Drops", description="Draws an overlay over the player when a special attack hits")
    default public boolean specDrops() {
        return true;
    }

    @ConfigItem(position=2, keyName="specDropColor", name="Spec Drop Color", description="Text color for spec drops")
    default public Color specDropColor() {
        return Color.WHITE;
    }

    @ConfigItem(position=3, keyName="infobox", name="Infobox", description="Adds an infobox counting special attacks")
    default public boolean infobox() {
        return true;
    }

    @ConfigItem(position=10, keyName="dragonWarhammerThreshold", name="Dragon Warhammer", description="Threshold for Dragon Warhammer (0 to disable)")
    default public int dragonWarhammerThreshold() {
        return 0;
    }

    @ConfigItem(position=20, keyName="arclightThreshold", name="Arclight", description="Threshold for Arclight (0 to disable)")
    default public int arclightThreshold() {
        return 0;
    }

    @ConfigItem(position=30, keyName="darklightThreshold", name="Darklight", description="Threshold for Darklight (0 to disable)")
    default public int darklightThreshold() {
        return 0;
    }

    @ConfigItem(position=40, keyName="bandosGodswordThreshold", name="Bandos Godsword", description="Threshold for Bandos Godsword (0 to disable)")
    default public int bandosGodswordThreshold() {
        return 0;
    }

    @ConfigItem(position=50, keyName="bulwarkThreshold", name="Dinh's Bulwark", description="Threshold for Dinh's Bulwark (0 to disable)")
    default public int bulwarkThreshold() {
        return 0;
    }
}

