/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.barrows;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="barrows")
public interface BarrowsConfig
extends Config {
    @ConfigItem(keyName="showBrotherLoc", name="Show Brothers location", description="Configures whether or not the brothers location is displayed", position=1)
    default public boolean showBrotherLoc() {
        return true;
    }

    @ConfigItem(keyName="showChestValue", name="Show Value of Chests", description="Configure whether to show total exchange value of chest when opened", position=2)
    default public boolean showChestValue() {
        return true;
    }

    @ConfigItem(keyName="brotherLocColor", name="Brother location color", description="Change the color of the name displayed on the minimap", position=3)
    default public Color brotherLocColor() {
        return Color.CYAN;
    }

    @ConfigItem(keyName="deadBrotherLocColor", name="Dead Brother loc. color", description="Change the color of the name displayed on the minimap for a dead brother", position=4)
    default public Color deadBrotherLocColor() {
        return Color.RED;
    }

    @ConfigItem(keyName="showPuzzleAnswer", name="Show Puzzle Answer", description="Configures if the puzzle answer should be shown.", position=5)
    default public boolean showPuzzleAnswer() {
        return true;
    }

    @ConfigItem(keyName="showPrayerDrainTimer", name="Show Prayer Drain Timer", description="Configure whether or not a countdown until the next prayer drain is displayed", position=6)
    default public boolean showPrayerDrainTimer() {
        return true;
    }
}

