/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.chatcommands;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup(value="chatcommands")
public interface ChatCommandsConfig
extends Config {
    @ConfigItem(position=0, keyName="price", name="Price Command", description="Configures whether the Price command is enabled<br> !price [item]")
    default public boolean price() {
        return true;
    }

    @ConfigItem(position=1, keyName="lvl", name="Level Command", description="Configures whether the Level command is enabled<br> !lvl [skill]")
    default public boolean lvl() {
        return true;
    }

    @ConfigItem(position=2, keyName="clue", name="Clue Command", description="Configures whether the Clue command is enabled<br> !clues")
    default public boolean clue() {
        return true;
    }

    @ConfigItem(position=3, keyName="killcount", name="Killcount Command", description="Configures whether the Killcount command is enabled<br> !kc [boss]")
    default public boolean killcount() {
        return true;
    }

    @ConfigItem(position=4, keyName="qp", name="QP Command", description="Configures whether the quest point command is enabled<br> !qp")
    default public boolean qp() {
        return true;
    }

    @ConfigItem(position=5, keyName="pb", name="PB Command", description="Configures whether the personal best command is enabled<br> !pb")
    default public boolean pb() {
        return true;
    }

    @ConfigItem(position=6, keyName="gc", name="GC Command", description="Configures whether the Barbarian Assault High gamble count command is enabled<br> !gc")
    default public boolean gc() {
        return true;
    }

    @ConfigItem(position=7, keyName="duels", name="Duels Command", description="Configures whether the duel arena command is enabled<br> !duels")
    default public boolean duels() {
        return true;
    }

    @ConfigItem(position=8, keyName="bh", name="BH Command", description="Configures whether the Bounty Hunter - Hunter command is enabled<br> !bh")
    default public boolean bh() {
        return true;
    }

    @ConfigItem(position=9, keyName="bhRogue", name="BH Rogue Command", description="Configures whether the Bounty Hunter - Rogue command is enabled<br> !bhrogue")
    default public boolean bhRogue() {
        return true;
    }

    @ConfigItem(position=10, keyName="lms", name="LMS Command", description="Configures whether the Last Man Standing command is enabled<br> !lms")
    default public boolean lms() {
        return true;
    }

    @ConfigItem(position=11, keyName="lp", name="LP Command", description="Configures whether the League Points command is enabled<br> !lp")
    default public boolean lp() {
        return true;
    }

    @ConfigItem(position=12, keyName="sw", name="SW Command", description="Configures whether the Soul Wars Zeal command is enabled<br> !sw")
    default public boolean sw() {
        return true;
    }

    @ConfigItem(position=13, keyName="pets", name="Pets Command", description="Configures whether the player pet list command is enabled<br> !pets<br> Note: Update your pet list by looking at the All Pets tab in the Collection Log")
    default public boolean pets() {
        return true;
    }

    @ConfigItem(position=20, keyName="clearSingleWord", name="Clear Single Word", description="Enable hot key to clear single word at a time")
    default public Keybind clearSingleWord() {
        return new Keybind(8, 128);
    }

    @ConfigItem(position=21, keyName="clearEntireChatBox", name="Clear Chat Box", description="Enable hotkey to clear entire chat box")
    default public Keybind clearChatBox() {
        return Keybind.NOT_SET;
    }
}

