/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.fairyring;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="fairyrings")
public interface FairyRingConfig
extends Config {
    @ConfigItem(keyName="autoOpen", name="Open search automatically", description="Open the search widget every time you enter a fairy ring")
    default public boolean autoOpen() {
        return true;
    }
}

