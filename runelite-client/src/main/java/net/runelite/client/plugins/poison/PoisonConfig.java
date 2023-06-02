/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.poison;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="poison")
public interface PoisonConfig
extends Config {
    public static final String GROUP = "poison";

    @ConfigItem(keyName="showInfoboxes", name="Show Infoboxes", description="Configures whether to show the infoboxes")
    default public boolean showInfoboxes() {
        return false;
    }

    @ConfigItem(keyName="changeHealthIcon", name="Change HP Orb Icon", description="Configures whether the hp orb icon should change color to match poison/disease")
    default public boolean changeHealthIcon() {
        return true;
    }
}

