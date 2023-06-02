/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.attackstyles;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="attackIndicator")
public interface AttackStylesConfig
extends Config {
    @ConfigItem(keyName="alwaysShowStyle", name="Always show style", description="Show attack style indicator at all times", position=1)
    default public boolean alwaysShowStyle() {
        return true;
    }

    @ConfigItem(keyName="warnForDefensive", name="Warn for defence", description="Show warning when a Defence skill combat option is selected", position=2)
    default public boolean warnForDefence() {
        return false;
    }

    @ConfigItem(keyName="warnForAttack", name="Warn for attack", description="Show warning when an Attack skill combat option is selected", position=3)
    default public boolean warnForAttack() {
        return false;
    }

    @ConfigItem(keyName="warnForStrength", name="Warn for strength", description="Show warning when a Strength skill combat option is selected", position=4)
    default public boolean warnForStrength() {
        return false;
    }

    @ConfigItem(keyName="warnForRanged", name="Warn for ranged", description="Show warning when a Ranged skill combat option is selected", position=5)
    default public boolean warnForRanged() {
        return false;
    }

    @ConfigItem(keyName="warnForMagic", name="Warn for magic", description="Show warning when a Magic skill combat option is selected", position=6)
    default public boolean warnForMagic() {
        return false;
    }

    @ConfigItem(keyName="hideAutoRetaliate", name="Hide auto retaliate", description="Hide auto retaliate from the combat options tab", position=7)
    default public boolean hideAutoRetaliate() {
        return false;
    }

    @ConfigItem(keyName="removeWarnedStyles", name="Remove warned styles", description="Remove warned styles from the combat options tab", position=8)
    default public boolean removeWarnedStyles() {
        return false;
    }
}

