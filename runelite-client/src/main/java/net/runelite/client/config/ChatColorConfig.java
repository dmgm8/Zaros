/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.config;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(value="textrecolor")
public interface ChatColorConfig
extends Config {
    @ConfigSection(name="Opaque", description="The options that control the colours for the Opaque Chatbox", position=0, closedByDefault=true)
    public static final String opaqueSection = "opaqueSection";
    @ConfigSection(name="Transparent", description="The options that control the colours for the Transparent Chatbox", position=50, closedByDefault=true)
    public static final String transparentSection = "transparentSection";

    @ConfigItem(position=1, keyName="opaquePublicChat", name="Public chat", description="Color of Public chat", section="opaqueSection")
    public Color opaquePublicChat();

    @ConfigItem(position=2, keyName="opaquePublicChatHighlight", name="Public chat highlight", description="Color of highlights in Public chat", section="opaqueSection")
    default public Color opaquePublicChatHighlight() {
        return Color.decode("#000000");
    }

    @ConfigItem(position=3, keyName="opaquePrivateMessageSent", name="Sent private messages", description="Color of Private messages you've sent", section="opaqueSection")
    public Color opaquePrivateMessageSent();

    @ConfigItem(position=4, keyName="opaquePrivateMessageSentHighlight", name="Sent private messages highlight", description="Color of highlights in Private messages you've sent", section="opaqueSection")
    default public Color opaquePrivateMessageSentHighlight() {
        return Color.decode("#002783");
    }

    @ConfigItem(position=5, keyName="opaquePrivateMessageReceived", name="Received private messages", description="Color of Private messages you've received", section="opaqueSection")
    public Color opaquePrivateMessageReceived();

    @ConfigItem(position=6, keyName="opaquePrivateMessageReceivedHighlight", name="Received private messages highlight", description="Color of highlights in Private messages you've received", section="opaqueSection")
    default public Color opaquePrivateMessageReceivedHighlight() {
        return Color.decode("#002783");
    }

    @ConfigItem(position=7, keyName="opaqueFriendsChatInfo", name="Friends chat info", description="Friends Chat Information (eg. when joining a channel)", section="opaqueSection")
    public Color opaqueFriendsChatInfo();

    @ConfigItem(position=8, keyName="opaqueClanChatInfoHighlight", name="Friends chat info highlight", description="Friends Chat Information highlight (used for the Raids plugin)", section="opaqueSection")
    default public Color opaqueFriendsChatInfoHighlight() {
        return Color.RED;
    }

    @ConfigItem(position=9, keyName="opaqueClanChatMessage", name="Friends chat message", description="Color of Friends chat messages", section="opaqueSection")
    public Color opaqueFriendsChatMessage();

    @ConfigItem(position=10, keyName="opaqueClanChatMessageHighlight", name="Friends chat message highlight", description="Color of highlights in Friends Chat messages", section="opaqueSection")
    default public Color opaqueFriendsChatMessageHighlight() {
        return Color.decode("#000000");
    }

    @ConfigItem(position=11, keyName="opaqueClanInfo", name="Clan chat info", description="Clan Chat Information (eg. when joining a channel)", section="opaqueSection")
    public Color opaqueClanChatInfo();

    @ConfigItem(position=12, keyName="opaqueClanInfoHighlight", name="Clan chat info highlight", description="Clan Chat Information highlight", section="opaqueSection")
    default public Color opaqueClanChatInfoHighlight() {
        return Color.RED;
    }

    @ConfigItem(position=13, keyName="opaqueClanMessage", name="Clan chat message", description="Color of Clan chat messages", section="opaqueSection")
    public Color opaqueClanChatMessage();

    @ConfigItem(position=14, keyName="opaqueClanMessageHighlight", name="Clan chat message highlight", description="Color of highlights in Clan Chat messages", section="opaqueSection")
    default public Color opaqueClanChatMessageHighlight() {
        return Color.decode("#000000");
    }

    @ConfigItem(position=15, keyName="opaqueClanGuestInfo", name="Guest Clan chat info", description="Guest Clan Chat Information (eg. when joining a channel)", section="opaqueSection")
    public Color opaqueClanChatGuestInfo();

    @ConfigItem(position=16, keyName="opaqueClanGuestInfoHighlight", name="Guest Clan chat info highlight", description="Guest Clan Chat Information highlight", section="opaqueSection")
    default public Color opaqueClanChatGuestInfoHighlight() {
        return Color.RED;
    }

    @ConfigItem(position=17, keyName="opaqueClanGuestMessage", name="Guest Clan chat message", description="Color of Guest Clan chat messages", section="opaqueSection")
    public Color opaqueClanChatGuestMessage();

    @ConfigItem(position=18, keyName="opaqueClanChatGuestMessageHighlight", name="Guest Clan chat message highlight", description="Color of highlights in Guest Clan Chat messages", section="opaqueSection")
    default public Color opaqueClanChatGuestMessageHighlight() {
        return Color.decode("#000000");
    }

    @ConfigItem(position=19, keyName="opaqueAutochatMessage", name="Autochat", description="Color of Autochat messages", section="opaqueSection")
    public Color opaqueAutochatMessage();

    @ConfigItem(position=20, keyName="opaqueAutochatMessageHighlight", name="Autochat highlight", description="Color of highlights in Autochat messages", section="opaqueSection")
    public Color opaqueAutochatMessageHighlight();

    @ConfigItem(position=21, keyName="opaqueTradeChatMessage", name="Trade chat", description="Color of Trade Chat Messages", section="opaqueSection")
    public Color opaqueTradeChatMessage();

    @ConfigItem(position=22, keyName="opaqueTradeChatMessageHighlight", name="Trade chat highlight", description="Color of highlights in Trade Chat Messages", section="opaqueSection")
    public Color opaqueTradeChatMessageHighlight();

    @ConfigItem(position=23, keyName="opaqueServerMessage", name="Server message", description="Color of Server Messages (eg. 'Welcome to RuneScape')", section="opaqueSection")
    public Color opaqueServerMessage();

    @ConfigItem(position=24, keyName="opaqueServerMessageHighlight", name="Server message highlight", description="Color of highlights in Server Messages", section="opaqueSection")
    public Color opaqueServerMessageHighlight();

    @ConfigItem(position=25, keyName="opaqueGameMessage", name="Game message", description="Color of Game Messages", section="opaqueSection")
    public Color opaqueGameMessage();

    @ConfigItem(position=26, keyName="opaqueGameMessageHighlight", name="Game message highlight", description="Color of highlights in Game Messages", section="opaqueSection")
    default public Color opaqueGameMessageHighlight() {
        return Color.decode("#EF1020");
    }

    @ConfigItem(position=27, keyName="opaqueExamine", name="Examine", description="Color of Examine Text", section="opaqueSection")
    public Color opaqueExamine();

    @ConfigItem(position=28, keyName="opaqueExamineHighlight", name="Examine highlight", description="Color of highlights in Examine Text", section="opaqueSection")
    default public Color opaqueExamineHighlight() {
        return Color.decode("#0000FF");
    }

    @ConfigItem(position=29, keyName="opaqueFiltered", name="Filtered", description="Color of Filtered Text (messages that aren't shown when Game messages are filtered)", section="opaqueSection")
    public Color opaqueFiltered();

    @ConfigItem(position=30, keyName="opaqueFilteredHighlight", name="Filtered highlight", description="Color of highlights in Filtered Text", section="opaqueSection")
    public Color opaqueFilteredHighlight();

    @ConfigItem(position=31, keyName="opaqueUsername", name="Usernames", description="Color of Usernames", section="opaqueSection")
    public Color opaqueUsername();

    @ConfigItem(position=32, keyName="opaquePrivateUsernames", name="Private chat usernames", description="Color of Usernames in Private Chat", section="opaqueSection")
    public Color opaquePrivateUsernames();

    @ConfigItem(position=33, keyName="opaqueClanChannelName", name="Friends Chat channel name", description="Color of Friends chat channel name", section="opaqueSection")
    public Color opaqueFriendsChatChannelName();

    @ConfigItem(position=34, keyName="opaqueClanChatChannelName", name="Clan Chat channel name", description="Color of Clan chat channel name", section="opaqueSection")
    public Color opaqueClanChannelName();

    @ConfigItem(position=35, keyName="opaqueClanChatGuestChannelName", name="Guest Clan Chat channel name", description="Color of Guest clan chat channel name", section="opaqueSection")
    public Color opaqueClanGuestChatChannelName();

    @ConfigItem(position=36, keyName="opaqueClanUsernames", name="Friends chat usernames", description="Color of usernames in Friends chat", section="opaqueSection")
    public Color opaqueFriendsChatUsernames();

    @ConfigItem(position=37, keyName="opaqueClanChatUsernames", name="Clan chat usernames", description="Color of usernames in Clan chat", section="opaqueSection")
    public Color opaqueClanChatUsernames();

    @ConfigItem(position=38, keyName="opaqueClanChatGuestUsernames", name="Guest Clan chat usernames", description="Color of usernames in Guest Clan chat", section="opaqueSection")
    public Color opaqueClanChatGuestUsernames();

    @ConfigItem(position=39, keyName="opaquePublicFriendUsernames", name="Public friend usernames", description="Color of Friend Usernames in Public Chat", section="opaqueSection")
    public Color opaquePublicFriendUsernames();

    @ConfigItem(position=40, keyName="opaquePlayerUsername", name="Your username", description="Color of your username", section="opaqueSection")
    public Color opaquePlayerUsername();

    @ConfigItem(position=51, keyName="transparentPublicChat", name="Public chat (transparent)", description="Color of Public chat (transparent)", section="transparentSection")
    public Color transparentPublicChat();

    @ConfigItem(position=52, keyName="transparentPublicChatHighlight", name="Public chat highlight (transparent)", description="Color of highlights in Public chat (transparent)", section="transparentSection")
    default public Color transparentPublicChatHighlight() {
        return Color.decode("#FFFFFF");
    }

    @ConfigItem(position=53, keyName="transparentPrivateMessageSent", name="Sent private messages (transparent)", description="Color of Private messages you've sent (transparent)", section="transparentSection")
    public Color transparentPrivateMessageSent();

    @ConfigItem(position=54, keyName="transparentPrivateMessageSentHighlight", name="Sent private messages highlight (transparent)", description="Color of highlights in Private messages you've sent (transparent)", section="transparentSection")
    default public Color transparentPrivateMessageSentHighlight() {
        return Color.decode("#FFFFFF");
    }

    @ConfigItem(position=55, keyName="transparentPrivateMessageReceived", name="Received private messages (transparent)", description="Color of Private messages you've received (transparent)", section="transparentSection")
    public Color transparentPrivateMessageReceived();

    @ConfigItem(position=56, keyName="transparentPrivateMessageReceivedHighlight", name="Received private messages highlight (transparent)", description="Color of highlights in Private messages you've received (transparent)", section="transparentSection")
    default public Color transparentPrivateMessageReceivedHighlight() {
        return Color.decode("#FFFFFF");
    }

    @ConfigItem(position=57, keyName="transparentFriendsChatInfo", name="Friends chat info (transparent)", description="Friends chat information (eg. when joining a channel) (transparent)", section="transparentSection")
    public Color transparentFriendsChatInfo();

    @ConfigItem(position=58, keyName="transparentClanChatInfoHighlight", name="Friends chat info highlight (transparent)", description="Friends chat information highlight (used for the Raids plugin) (transparent)", section="transparentSection")
    default public Color transparentFriendsChatInfoHighlight() {
        return Color.RED;
    }

    @ConfigItem(position=59, keyName="transparentClanChatMessage", name="Friends chat message (transparent)", description="Color of Friends chat messages (transparent)", section="transparentSection")
    public Color transparentFriendsChatMessage();

    @ConfigItem(position=60, keyName="transparentClanChatMessageHighlight", name="Friends chat message highlight (transparent)", description="Color of highlights in Friends chat messages (transparent)", section="transparentSection")
    default public Color transparentFriendsChatMessageHighlight() {
        return Color.decode("#FFFFFF");
    }

    @ConfigItem(position=61, keyName="transparentClanInfo", name="Clan chat info (transparent)", description="Clan Chat Information (eg. when joining a channel) (transparent)", section="transparentSection")
    public Color transparentClanChatInfo();

    @ConfigItem(position=62, keyName="transparentClanInfoHighlight", name="Clan chat info highlight (transparent)", description="Clan Chat Information highlight (transparent)", section="transparentSection")
    default public Color transparentClanChatInfoHighlight() {
        return Color.RED;
    }

    @ConfigItem(position=63, keyName="transparentClanMessage", name="Clan chat message (transparent)", description="Color of Clan chat messages (transparent)", section="transparentSection")
    public Color transparentClanChatMessage();

    @ConfigItem(position=64, keyName="transparentClanMessageHighlight", name="Clan chat message highlight (transparent)", description="Color of highlights in Clan Chat messages (transparent)", section="transparentSection")
    default public Color transparentClanChatMessageHighlight() {
        return Color.decode("#FFFFFF");
    }

    @ConfigItem(position=65, keyName="transparentClanGuestInfo", name="Guest Clan chat info (transparent)", description="Guest Clan Chat Information (eg. when joining a channel) (transparent)", section="transparentSection")
    public Color transparentClanChatGuestInfo();

    @ConfigItem(position=66, keyName="transparentClanGuestInfoHighlight", name="Guest Clan chat info highlight (transparent)", description="Guest Clan Chat Information highlight (transparent)", section="transparentSection")
    default public Color transparentClanChatGuestInfoHighlight() {
        return Color.RED;
    }

    @ConfigItem(position=67, keyName="transparentClanGuestMessage", name="Guest Clan chat message (transparent)", description="Color of Guest Clan chat messages (transparent)", section="transparentSection")
    public Color transparentClanChatGuestMessage();

    @ConfigItem(position=68, keyName="transparentClanChatGuestMessageHighlight", name="Guest Clan chat message highlight (transparent)", description="Color of highlights in Guest Clan Chat messages (transparent)", section="transparentSection")
    default public Color transparentClanChatGuestMessageHighlight() {
        return Color.decode("#FFFFFF");
    }

    @ConfigItem(position=69, keyName="transparentAutochatMessage", name="Autochat (transparent)", description="Color of Autochat messages (transparent)", section="transparentSection")
    public Color transparentAutochatMessage();

    @ConfigItem(position=70, keyName="transparentAutochatMessageHighlight", name="Autochat highlight (transparent)", description="Color of highlights in Autochat messages (transparent)", section="transparentSection")
    public Color transparentAutochatMessageHighlight();

    @ConfigItem(position=71, keyName="transparentTradeChatMessage", name="Trade chat (transparent)", description="Color of Trade Chat Messages (transparent)", section="transparentSection")
    public Color transparentTradeChatMessage();

    @ConfigItem(position=72, keyName="transparentTradeChatMessageHighlight", name="Trade chat highlight (transparent)", description="Color of highlights in Trade Chat Messages (transparent)", section="transparentSection")
    public Color transparentTradeChatMessageHighlight();

    @ConfigItem(position=73, keyName="transparentServerMessage", name="Server message (transparent)", description="Color of Server Messages (eg. 'Welcome to RuneScape') (transparent)", section="transparentSection")
    public Color transparentServerMessage();

    @ConfigItem(position=74, keyName="transparentServerMessageHighlight", name="Server message highlight (transparent)", description="Color of highlights in Server Messages (transparent)", section="transparentSection")
    public Color transparentServerMessageHighlight();

    @ConfigItem(position=75, keyName="transparentGameMessage", name="Game message (transparent)", description="Color of Game Messages (transparent)", section="transparentSection")
    public Color transparentGameMessage();

    @ConfigItem(position=76, keyName="transparentGameMessageHighlight", name="Game message highlight (transparent)", description="Color of highlights in Game Messages (transparent)", section="transparentSection")
    default public Color transparentGameMessageHighlight() {
        return Color.decode("#EF1020");
    }

    @ConfigItem(position=77, keyName="transparentExamine", name="Examine (transparent)", description="Color of Examine Text (transparent)", section="transparentSection")
    public Color transparentExamine();

    @ConfigItem(position=78, keyName="transparentExamineHighlight", name="Examine highlight (transparent)", description="Color of highlights in Examine Text (transparent)", section="transparentSection")
    default public Color transparentExamineHighlight() {
        return Color.GREEN;
    }

    @ConfigItem(position=79, keyName="transparentFiltered", name="Filtered (transparent)", description="Color of Filtered Text (messages that aren't shown when Game messages are filtered) (transparent)", section="transparentSection")
    public Color transparentFiltered();

    @ConfigItem(position=80, keyName="transparentFilteredHighlight", name="Filtered highlight (transparent)", description="Color of highlights in Filtered Text (transparent)", section="transparentSection")
    public Color transparentFilteredHighlight();

    @ConfigItem(position=81, keyName="transparentUsername", name="Usernames (transparent)", description="Color of Usernames (transparent)", section="transparentSection")
    public Color transparentUsername();

    @ConfigItem(position=82, keyName="transparentPrivateUsernames", name="Private chat usernames (transparent)", description="Color of Usernames in Private Chat (transparent)", section="transparentSection")
    public Color transparentPrivateUsernames();

    @ConfigItem(position=83, keyName="transparentClanChannelName", name="Friends Chat channel name (transparent)", description="Color of Friends chat channel name (transparent)", section="transparentSection")
    public Color transparentFriendsChatChannelName();

    @ConfigItem(position=84, keyName="transparentClanChatChannelName", name="Clan Chat channel name (transparent)", description="Color of Clan chat channel name (transparent)", section="transparentSection")
    public Color transparentClanChannelName();

    @ConfigItem(position=85, keyName="transparentClanChatGuestChannelName", name="Guest Clan Chat channel name (transparent)", description="Color of Guest clan chat channel name (transparent)", section="transparentSection")
    public Color transparentClanChannelGuestName();

    @ConfigItem(position=86, keyName="transparentClanUsernames", name="Friends chat usernames (transparent)", description="Color of usernames in Friends chat (transparent)", section="transparentSection")
    public Color transparentFriendsChatUsernames();

    @ConfigItem(position=87, keyName="transparentClanClanUsernames", name="Clan chat usernames (transparent)", description="Color of usernames in Clan chat (transparent)", section="transparentSection")
    public Color transparentClanChatUsernames();

    @ConfigItem(position=88, keyName="transparentClanClanGuestUsernames", name="Guest Clan chat usernames (transparent)", description="Color of usernames in Guest Clan chat (transparent)", section="transparentSection")
    public Color transparentClanChatGuestUsernames();

    @ConfigItem(position=89, keyName="transparentPublicFriendUsernames", name="Public friend usernames (transparent)", description="Color of Friend Usernames in Public Chat (transparent)", section="transparentSection")
    public Color transparentPublicFriendUsernames();

    @ConfigItem(position=90, keyName="transparentPlayerUsername", name="Your username (transparent)", description="Color of your username (transparent)", section="transparentSection")
    public Color transparentPlayerUsername();
}

