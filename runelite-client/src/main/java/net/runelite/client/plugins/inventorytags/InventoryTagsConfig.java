/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.inventorytags;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(value="inventorytags")
public interface InventoryTagsConfig
extends Config {
    public static final String GROUP = "inventorytags";
    @ConfigSection(name="Tag display mode", description="How tags are displayed in the inventory", position=0)
    public static final String tagStyleSection = "tagStyleSection";

    @ConfigItem(position=0, keyName="showTagOutline", name="Outline", description="Configures whether or not item tags show be outlined", section="tagStyleSection")
    default public boolean showTagOutline() {
        return true;
    }

    @ConfigItem(position=1, keyName="tagUnderline", name="Underline", description="Configures whether or not item tags should be underlined", section="tagStyleSection")
    default public boolean showTagUnderline() {
        return false;
    }

    @ConfigItem(position=2, keyName="tagFill", name="Fill", description="Configures whether or not item tags should be filled", section="tagStyleSection")
    default public boolean showTagFill() {
        return false;
    }

    @Range(max=255)
    @ConfigItem(position=3, keyName="fillOpacity", name="Fill opacity", description="Configures the opacity of the tag \"Fill\"", section="tagStyleSection")
    default public int fillOpacity() {
        return 50;
    }

    @ConfigItem(position=1, keyName="groupColor1", name="Group 1 Color", description="Color of the Tag", hidden=true)
    default public Color getGroup1Color() {
        return new Color(255, 0, 0);
    }

    @ConfigItem(position=2, keyName="groupColor2", name="Group 2 Color", description="Color of the Tag", hidden=true)
    default public Color getGroup2Color() {
        return new Color(0, 255, 0);
    }

    @ConfigItem(position=3, keyName="groupColor3", name="Group 3 Color", description="Color of the Tag", hidden=true)
    default public Color getGroup3Color() {
        return new Color(0, 0, 255);
    }

    @ConfigItem(position=4, keyName="groupColor4", name="Group 4 Color", description="Color of the Tag", hidden=true)
    default public Color getGroup4Color() {
        return new Color(255, 0, 255);
    }

    @ConfigItem(position=5, keyName="groupColor5", name="Group 5 Color", description="Color of the Tag", hidden=true)
    default public Color getGroup5Color() {
        return new Color(255, 255, 0);
    }

    @ConfigItem(position=6, keyName="groupColor6", name="Group 6 Color", description="Color of the Tag", hidden=true)
    default public Color getGroup6Color() {
        return new Color(0, 255, 255);
    }
}

