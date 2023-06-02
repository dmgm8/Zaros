/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.herbiboars;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="herbiboar")
public interface HerbiboarConfig
extends Config {
    @ConfigItem(position=0, keyName="showStart", name="Show Start Objects", description="Show highlights for starting rocks and logs")
    default public boolean isStartShown() {
        return true;
    }

    @ConfigItem(position=1, keyName="showClickboxes", name="Show Clickboxes", description="Show clickboxes on trail objects and tunnels instead of tiles")
    default public boolean showClickBoxes() {
        return false;
    }

    @Alpha
    @ConfigItem(position=2, keyName="colorStart", name="Start Color", description="Color for rocks that start the trails")
    default public Color getStartColor() {
        return Color.CYAN;
    }

    @ConfigItem(position=3, keyName="showTunnel", name="Show End Tunnels", description="Show highlights for tunnels with herbiboars")
    default public boolean isTunnelShown() {
        return true;
    }

    @Alpha
    @ConfigItem(position=4, keyName="colorTunnel", name="Tunnel Color", description="Color for tunnels with herbiboars")
    default public Color getTunnelColor() {
        return Color.GREEN;
    }

    @ConfigItem(position=5, keyName="showObject", name="Show Trail Objects", description="Show highlights for mushrooms, mud, seaweed, etc")
    default public boolean isObjectShown() {
        return true;
    }

    @Alpha
    @ConfigItem(position=6, keyName="colorGameObject", name="Trail Object Color", description="Color for mushrooms, mud, seaweed, etc")
    default public Color getObjectColor() {
        return Color.CYAN;
    }

    @ConfigItem(position=7, keyName="showTrail", name="Show Trail", description="Show highlights for trail prints")
    default public boolean isTrailShown() {
        return true;
    }

    @Alpha
    @ConfigItem(position=8, keyName="colorTrail", name="Trail Color", description="Color for mushrooms, mud, seaweed, etc")
    default public Color getTrailColor() {
        return Color.WHITE;
    }
}

