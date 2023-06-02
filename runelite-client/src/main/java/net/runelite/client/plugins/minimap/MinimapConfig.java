/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.minimap;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(value="minimap")
public interface MinimapConfig
extends Config {
    public static final String GROUP = "minimap";
    @ConfigSection(name="Minimap dot colors", description="The colors of dots on the minimap.", position=0)
    public static final String minimapDotSection = "minimapDotSection";

    @ConfigItem(keyName="hideMinimap", name="Hide minimap", description="Do not show the minimap on screen (Resizable only)")
    default public boolean hideMinimap() {
        return false;
    }

    @ConfigItem(keyName="item", name="Item color", description="Set the minimap color items are drawn in", section="minimapDotSection")
    public Color itemColor();

    @ConfigItem(keyName="npc", name="NPC color", description="Set the minimap color NPCs are drawn in", section="minimapDotSection")
    public Color npcColor();

    @ConfigItem(keyName="player", name="Player color", description="Set the minimap Color players are drawn in", section="minimapDotSection")
    public Color playerColor();

    @ConfigItem(keyName="friend", name="Friends color", description="Set the minimap color your friends are drawn in", section="minimapDotSection")
    public Color friendColor();

    @ConfigItem(keyName="team", name="Team color", description="Set the minimap color your team is drawn in", section="minimapDotSection")
    public Color teamColor();

    @ConfigItem(keyName="clan", name="Friends Chat color", description="Set the minimap color your friends chat members are drawn in", section="minimapDotSection")
    public Color friendsChatColor();

    @ConfigItem(keyName="clanchat", name="Clan Chat color", description="Set the minimap color your clan chat members are drawn in", section="minimapDotSection")
    public Color clanChatColor();

    @ConfigItem(keyName="prioritizeTeamAndClan", name="Prioritize Team and Friends Chat", description="Makes friends that have same team cape or are in the same<br>friends chat take priority over showing them as friends", position=7)
    default public boolean prioritizeTeamAndClan() {
        return false;
    }
}

