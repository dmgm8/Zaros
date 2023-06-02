/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.chatfilter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.chatfilter.ChatFilterType;

@ConfigGroup(value="chatfilter")
public interface ChatFilterConfig
extends Config {
    @ConfigSection(name="Filter Lists", description="Custom Word, Regex, and Username filter lists", position=0, closedByDefault=true)
    public static final String filterLists = "filterLists";

    @ConfigItem(keyName="filteredWords", name="Filtered Words", description="List of filtered words, separated by commas", position=1, section="filterLists")
    default public String filteredWords() {
        return "";
    }

    @ConfigItem(keyName="filteredRegex", name="Filtered Regex", description="List of regular expressions to filter, one per line", position=2, section="filterLists")
    default public String filteredRegex() {
        return "";
    }

    @ConfigItem(keyName="filteredNames", name="Filtered Names", description="List of filtered names, one per line. Accepts regular expressions", position=3, section="filterLists")
    default public String filteredNames() {
        return "";
    }

    @ConfigItem(keyName="filterType", name="Filter type", description="Configures how the messages are filtered", position=4)
    default public ChatFilterType filterType() {
        return ChatFilterType.CENSOR_WORDS;
    }

    @ConfigItem(keyName="filterFriends", name="Filter Friends", description="Filter your friends' messages", position=5)
    default public boolean filterFriends() {
        return false;
    }

    @ConfigItem(keyName="filterClan", name="Filter Friends Chat Members", description="Filter your friends chat members' messages", position=6)
    default public boolean filterFriendsChat() {
        return false;
    }

    @ConfigItem(keyName="filterClanChat", name="Filter clan Chat Members", description="Filter your clan chat members' messages", position=7)
    default public boolean filterClanChat() {
        return false;
    }

    @ConfigItem(keyName="filterGameChat", name="Filter Game Chat", description="Filter your game chat messages", position=9)
    default public boolean filterGameChat() {
        return false;
    }

    @ConfigItem(keyName="collapseGameChat", name="Collapse Game Chat", description="Collapse duplicate game chat messages into a single line", position=10)
    default public boolean collapseGameChat() {
        return false;
    }

    @ConfigItem(keyName="collapsePlayerChat", name="Collapse Player Chat", description="Collapse duplicate player chat messages into a single line", position=11)
    default public boolean collapsePlayerChat() {
        return false;
    }

    @ConfigItem(keyName="maxRepeatedPublicChats", name="Repeat filter", description="Block player chat message if repeated this many times. 0 is off", position=12)
    default public int maxRepeatedPublicChats() {
        return 0;
    }

    @ConfigItem(keyName="stripAccents", name="Strip accents", description="Remove accents before applying filters", position=13)
    default public boolean stripAccents() {
        return false;
    }
}

