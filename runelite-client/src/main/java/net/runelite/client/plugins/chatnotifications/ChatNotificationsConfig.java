/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.chatnotifications;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(value="chatnotification")
public interface ChatNotificationsConfig
extends Config {
    @ConfigSection(name="Highlight Lists", description="Custom single word and regex filter lists", position=0)
    public static final String highlightLists = "highlightLists";

    @ConfigItem(position=1, keyName="highlightWordsString", name="Highlight words", description="Highlights the following words in chat, separated by commas", section="highlightLists")
    default public String highlightWordsString() {
        return "";
    }

    @ConfigItem(position=2, keyName="highlightRegexString", name="Highlight Regex", description="Highlights the following regular expressions in chat, one per line", section="highlightLists")
    default public String highlightRegexString() {
        return "";
    }

    @ConfigItem(position=1, keyName="highlightOwnName", name="Highlight own name", description="Highlights any instance of your username in chat")
    default public boolean highlightOwnName() {
        return true;
    }

    @ConfigItem(position=2, keyName="notifyOnOwnName", name="Notify on own name", description="Notifies you whenever someone mentions you by name")
    default public boolean notifyOnOwnName() {
        return false;
    }

    @ConfigItem(position=3, keyName="notifyOnHighlight", name="Notify on highlight", description="Notifies you whenever a highlighted word is matched")
    default public boolean notifyOnHighlight() {
        return false;
    }

    @ConfigItem(position=4, keyName="notifyOnTrade", name="Notify on trade", description="Notifies you whenever you are traded")
    default public boolean notifyOnTrade() {
        return false;
    }

    @ConfigItem(position=5, keyName="notifyOnDuel", name="Notify on duel", description="Notifies you whenever you are challenged to a duel")
    default public boolean notifyOnDuel() {
        return false;
    }

    @ConfigItem(position=6, keyName="notifyOnBroadcast", name="Notify on broadcast", description="Notifies you whenever you receive a broadcast message")
    default public boolean notifyOnBroadcast() {
        return false;
    }

    @ConfigItem(position=7, keyName="notifyOnPM", name="Notify on private message", description="Notifies you whenever you receive a private message")
    default public boolean notifyOnPM() {
        return false;
    }
}

