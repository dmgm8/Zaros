/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  javax.annotation.Nullable
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.chathistory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.runelite.api.ChatMessageType;
import net.runelite.api.widgets.WidgetInfo;

enum ChatboxTab {
    ALL("Switch tab", WidgetInfo.CHATBOX_TAB_ALL, ChatMessageType.values()),
    PRIVATE(null, WidgetInfo.CHATBOX_TAB_PRIVATE, ChatMessageType.PRIVATECHAT, ChatMessageType.PRIVATECHATOUT, ChatMessageType.MODPRIVATECHAT, ChatMessageType.LOGINLOGOUTNOTIFICATION),
    PUBLIC(null, WidgetInfo.CHATBOX_TAB_PUBLIC, ChatMessageType.PUBLICCHAT, ChatMessageType.AUTOTYPER, ChatMessageType.MODCHAT, ChatMessageType.MODAUTOTYPER),
    GAME("Filter", WidgetInfo.CHATBOX_TAB_GAME, ChatMessageType.GAMEMESSAGE, ChatMessageType.ENGINE, ChatMessageType.BROADCAST, ChatMessageType.SNAPSHOTFEEDBACK, ChatMessageType.ITEM_EXAMINE, ChatMessageType.NPC_EXAMINE, ChatMessageType.OBJECT_EXAMINE, ChatMessageType.FRIENDNOTIFICATION, ChatMessageType.IGNORENOTIFICATION, ChatMessageType.CONSOLE, ChatMessageType.SPAM, ChatMessageType.PLAYERRELATED, ChatMessageType.TENSECTIMEOUT, ChatMessageType.WELCOME, ChatMessageType.YELLCHAT, ChatMessageType.MODYELLCHAT, ChatMessageType.BROADCAST_FRIEND, ChatMessageType.BROADCAST_WORLD, ChatMessageType.BROADCAST_GLOBAL, ChatMessageType.UNKNOWN),
    CHANNEL(null, WidgetInfo.CHATBOX_TAB_CHANNEL, ChatMessageType.FRIENDSCHATNOTIFICATION, ChatMessageType.FRIENDSCHAT, ChatMessageType.CHALREQ_FRIENDSCHAT),
    CLAN(null, WidgetInfo.CHATBOX_TAB_CLAN, ChatMessageType.CLAN_CHAT, ChatMessageType.CLAN_MESSAGE, ChatMessageType.CLAN_GUEST_CHAT, ChatMessageType.CLAN_GUEST_MESSAGE),
    TRADE_GROUP("Trade:</col> Show none", WidgetInfo.CHATBOX_TAB_TRADE, ChatMessageType.TRADE_SENT, ChatMessageType.TRADEREQ, ChatMessageType.TRADE, ChatMessageType.CHALREQ_TRADE, ChatMessageType.CLAN_GIM_CHAT, ChatMessageType.CLAN_GIM_MESSAGE);

    private static final Map<Integer, ChatboxTab> TAB_MESSAGE_TYPES;
    @Nullable
    private final String after;
    private final int widgetId;
    private final ChatMessageType[] messageTypes;

    private ChatboxTab(String after, WidgetInfo widgetId, ChatMessageType ... messageTypes) {
        this.after = after;
        this.widgetId = widgetId.getId();
        this.messageTypes = messageTypes;
    }

    static ChatboxTab of(int widgetId) {
        return TAB_MESSAGE_TYPES.get(widgetId);
    }

    @Nullable
    public String getAfter() {
        return this.after;
    }

    public int getWidgetId() {
        return this.widgetId;
    }

    public ChatMessageType[] getMessageTypes() {
        return this.messageTypes;
    }

    static {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (ChatboxTab t : ChatboxTab.values()) {
            builder.put((Object)t.widgetId, (Object)t);
        }
        TAB_MESSAGE_TYPES = builder.build();
    }
}

