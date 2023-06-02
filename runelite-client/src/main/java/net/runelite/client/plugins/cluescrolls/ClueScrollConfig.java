/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.cluescrolls;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="cluescroll")
public interface ClueScrollConfig
extends Config {
    @ConfigItem(keyName="displayHintArrows", name="Display hint arrows", description="Configures whether or not to display hint arrows for clues")
    default public boolean displayHintArrows() {
        return true;
    }
}

