/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.woodcutting;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;
import net.runelite.client.plugins.woodcutting.config.ClueNestTier;

@ConfigGroup(value="woodcutting")
public interface WoodcuttingConfig
extends Config {
    @ConfigItem(position=1, keyName="statTimeout", name="Reset stats", description="Configures the time until statistic is reset. Also configures when tree indicator is hidden")
    @Units(value=" mins")
    default public int statTimeout() {
        return 5;
    }

    @ConfigItem(position=2, keyName="showNestNotification", name="Bird nest notification", description="Configures whether to notify you of a bird nest spawn")
    default public boolean showNestNotification() {
        return true;
    }

    @ConfigItem(position=3, keyName="clueNestNotifyTier", name="Clue nest notification", description="Configures the clue tier from which to start notifying of a clue nest spawn")
    default public ClueNestTier clueNestNotifyTier() {
        return ClueNestTier.BEGINNER;
    }

    @ConfigItem(position=4, keyName="showWoodcuttingStats", name="Show session stats", description="Configures whether to display woodcutting session stats")
    default public boolean showWoodcuttingStats() {
        return true;
    }

    @ConfigItem(position=5, keyName="showRedwoods", name="Show Redwood trees", description="Configures whether to show a indicator for redwood trees")
    default public boolean showRedwoodTrees() {
        return true;
    }

    @ConfigItem(position=6, keyName="showRespawnTimers", name="Show respawn timers", description="Configures whether to display the respawn timer overlay")
    default public boolean showRespawnTimers() {
        return true;
    }
}

