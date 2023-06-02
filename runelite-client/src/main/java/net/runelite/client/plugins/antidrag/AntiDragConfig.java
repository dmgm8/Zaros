/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.antidrag;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="antiDrag")
public interface AntiDragConfig
extends Config {
    @ConfigItem(keyName="dragDelay", name="Drag Delay", description="Configures the inventory drag delay in client ticks (20ms)", position=1)
    default public int dragDelay() {
        return 30;
    }

    @ConfigItem(keyName="onShiftOnly", name="On Shift Only", description="Configures whether to only adjust the delay while holding shift.", position=2)
    default public boolean onShiftOnly() {
        return true;
    }

    @ConfigItem(keyName="disableOnCtrl", name="Disable On Control Pressed", description="Configures whether to ignore the delay while holding control.", position=3)
    default public boolean disableOnCtrl() {
        return false;
    }
}

