/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.motherlode;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup(value="motherlode")
public interface MotherlodeConfig
extends Config {
    @ConfigItem(keyName="showVeins", name="Show pay-dirt mining spots", description="Configures whether or not the pay-dirt mining spots are displayed.")
    default public boolean showVeins() {
        return true;
    }

    @ConfigItem(keyName="showRocks", name="Show rocks obstacles", description="Configures whether or not the fallen rocks obstacles are displayed.")
    default public boolean showRockFalls() {
        return true;
    }

    @ConfigItem(keyName="statTimeout", name="Reset stats", description="Configures the time until statistics are reset")
    @Units(value=" mins")
    default public int statTimeout() {
        return 5;
    }

    @ConfigItem(keyName="showSack", name="Show pay-dirt sack", description="Configures whether the pay-dirt sack is displayed or not.")
    default public boolean showSack() {
        return true;
    }

    @ConfigItem(keyName="showMiningStats", name="Show mining session stats", description="Configures whether to display mining session stats")
    default public boolean showMiningStats() {
        return true;
    }

    @ConfigItem(keyName="showDepositsLeft", name="Show deposits left", description="Displays deposits left before sack is full")
    default public boolean showDepositsLeft() {
        return true;
    }

    @ConfigItem(keyName="showMiningState", name="Show current mining state", description="Shows current mining state. 'You are currently mining' / 'You are currently NOT mining'")
    default public boolean showMiningState() {
        return true;
    }

    @ConfigItem(keyName="showGemsFound", name="Show gems found", description="Shows gems found during current mining session")
    default public boolean showGemsFound() {
        return true;
    }

    @ConfigItem(keyName="showOresFound", name="Show ores found", description="Shows the ores found during current mining session")
    default public boolean showOresFound() {
        return true;
    }

    @ConfigItem(keyName="showBrokenStruts", name="Show broken struts", description="Shows broken water wheel struts")
    default public boolean showBrokenStruts() {
        return true;
    }

    @ConfigItem(keyName="showLootIcons", name="Show ore icons", description="Display collected ores and gems as item images instead of text")
    default public boolean showLootIcons() {
        return false;
    }
}

