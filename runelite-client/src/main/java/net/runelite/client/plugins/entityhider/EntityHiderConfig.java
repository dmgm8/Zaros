/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.entityhider;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="entityhider")
public interface EntityHiderConfig
extends Config {
    public static final String GROUP = "entityhider";

    @ConfigItem(position=1, keyName="hidePlayers", name="Hide Others", description="Configures whether or not other players are hidden")
    default public boolean hideOthers() {
        return true;
    }

    @ConfigItem(position=2, keyName="hidePlayers2D", name="Hide Others 2D", description="Configures whether or not other players 2D elements are hidden")
    default public boolean hideOthers2D() {
        return true;
    }

    @ConfigItem(position=3, keyName="hideFriends", name="Hide Friends", description="Configures whether or not friends are hidden")
    default public boolean hideFriends() {
        return false;
    }

    @ConfigItem(position=4, keyName="hideClanMates", name="Hide Friends Chat members", description="Configures whether or not friends chat members are hidden")
    default public boolean hideFriendsChatMembers() {
        return false;
    }

    @ConfigItem(position=5, keyName="hideClanChatMembers", name="Hide Clan Chat members", description="Configures whether or not clan chat members are hidden")
    default public boolean hideClanChatMembers() {
        return false;
    }

    @ConfigItem(position=6, keyName="hideIgnores", name="Hide Ignores", description="Configures whether or not ignored players are hidden")
    default public boolean hideIgnores() {
        return false;
    }

    @ConfigItem(position=7, keyName="hideLocalPlayer", name="Hide Local Player", description="Configures whether or not the local player is hidden")
    default public boolean hideLocalPlayer() {
        return false;
    }

    @ConfigItem(position=8, keyName="hideLocalPlayer2D", name="Hide Local Player 2D", description="Configures whether or not the local player's 2D elements are hidden")
    default public boolean hideLocalPlayer2D() {
        return false;
    }

    @ConfigItem(position=9, keyName="hideNPCs", name="Hide NPCs", description="Configures whether or not NPCs are hidden")
    default public boolean hideNPCs() {
        return false;
    }

    @ConfigItem(position=10, keyName="hideNPCs2D", name="Hide NPCs 2D", description="Configures whether or not NPCs 2D elements are hidden")
    default public boolean hideNPCs2D() {
        return false;
    }

    @ConfigItem(position=11, keyName="hidePets", name="Hide Pets", description="Configures whether or not other player pets are hidden")
    default public boolean hidePets() {
        return false;
    }

    @ConfigItem(position=12, keyName="hideAttackers", name="Hide Attackers", description="Configures whether or not NPCs/players attacking you are hidden")
    default public boolean hideAttackers() {
        return false;
    }

    @ConfigItem(position=13, keyName="hideProjectiles", name="Hide Projectiles", description="Configures whether or not projectiles are hidden")
    default public boolean hideProjectiles() {
        return false;
    }

    @ConfigItem(position=14, keyName="hideDeadNpcs", name="Hide Dead NPCs", description="Hides NPCs when their health reaches 0")
    default public boolean hideDeadNpcs() {
        return false;
    }
}

