/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.boosts;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="boosts")
public interface BoostsConfig
extends Config {
    @ConfigItem(keyName="displayBoosts", name="Display Boosts", description="Configures which skill boosts to display", position=1)
    default public DisplayBoosts displayBoosts() {
        return DisplayBoosts.COMBAT;
    }

    @ConfigItem(keyName="relativeBoost", name="Show relative boosts", description="Configures whether or not relative boost is used", position=2)
    default public boolean useRelativeBoost() {
        return false;
    }

    @ConfigItem(keyName="displayIndicators", name="Display infoboxes", description="Configures whether to display boost infoboxes", position=3)
    default public boolean displayInfoboxes() {
        return true;
    }

    @ConfigItem(keyName="displayPanel", name="Display panel", description="Configures whether to display the boost panel", position=3)
    default public boolean displayPanel() {
        return false;
    }

    @ConfigItem(keyName="compactDisplay", name="Compact display", description="Displays skill boosts in a more compact panel", position=4)
    default public boolean compactDisplay() {
        return false;
    }

    @ConfigItem(keyName="displayNextBuffChange", name="Next buff change", description="Configures whether or not to display when the next buffed stat change will be", position=10)
    default public DisplayChangeMode displayNextBuffChange() {
        return DisplayChangeMode.BOOSTED;
    }

    @ConfigItem(keyName="displayNextDebuffChange", name="Next debuff change", description="Configures whether or not to display when the next debuffed stat change will be", position=11)
    default public DisplayChangeMode displayNextDebuffChange() {
        return DisplayChangeMode.NEVER;
    }

    @ConfigItem(keyName="boostThreshold", name="Boost threshold", description="The threshold at which boosted levels will be displayed in a different color. A value of 0 will disable the feature.", position=12)
    default public int boostThreshold() {
        return 0;
    }

    @ConfigItem(keyName="notifyOnBoost", name="Notify on boost threshold", description="Configures whether or not a notification will be sent for boosted stats.", position=13)
    default public boolean notifyOnBoost() {
        return true;
    }

    public static enum DisplayBoosts {
        NONE,
        COMBAT,
        NON_COMBAT,
        BOTH;

    }

    public static enum DisplayChangeMode {
        ALWAYS,
        BOOSTED,
        NEVER;

    }
}

