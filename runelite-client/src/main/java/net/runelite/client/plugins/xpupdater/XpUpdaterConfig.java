/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.xpupdater;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="xpupdater")
public interface XpUpdaterConfig
extends Config {
    @ConfigItem(position=1, keyName="cml", name="Crystal Math Labs", description="Automatically updates your stats on crystalmathlabs.com when you log out")
    default public boolean cml() {
        return false;
    }

    @ConfigItem(position=2, keyName="templeosrs", name="TempleOSRS", description="Automatically updates your stats on templeosrs.com when you log out")
    default public boolean templeosrs() {
        return false;
    }

    @ConfigItem(position=3, keyName="wiseoldman", name="Wise Old Man", description="Automatically updates your stats on wiseoldman.net when you log out")
    default public boolean wiseoldman() {
        return false;
    }
}

