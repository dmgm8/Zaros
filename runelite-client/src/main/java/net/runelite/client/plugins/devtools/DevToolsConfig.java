/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.devtools;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="devtools")
public interface DevToolsConfig
extends Config {
    @ConfigItem(keyName="inspectorAlwaysOnTop", name="", description="", hidden=true)
    default public boolean inspectorAlwaysOnTop() {
        return false;
    }

    @ConfigItem(keyName="inspectorAlwaysOnTop", name="", description="")
    public void inspectorAlwaysOnTop(boolean var1);
}

