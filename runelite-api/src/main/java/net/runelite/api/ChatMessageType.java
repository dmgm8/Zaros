/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.util.HashMap;
import java.util.Map;

public enum ChatMessageType {
    GAMEMESSAGE(0),
    MODCHAT(1),
    PUBLICCHAT(2),
    PRIVATECHAT(3),
    ENGINE(4),
    LOGINLOGOUTNOTIFICATION(5),
    PRIVATECHATOUT(6),
    MODPRIVATECHAT(7),
    FRIENDSCHAT(9),
    FRIENDSCHATNOTIFICATION(11),
    TRADE_SENT(12),
    BROADCAST(14),
    SNAPSHOTFEEDBACK(26),
    ITEM_EXAMINE(27),
    NPC_EXAMINE(28),
    OBJECT_EXAMINE(29),
    FRIENDNOTIFICATION(30),
    IGNORENOTIFICATION(31),
    CLAN_CHAT(41),
    CLAN_MESSAGE(43),
    CLAN_GUEST_CHAT(44),
    CLAN_GUEST_MESSAGE(46),
    AUTOTYPER(90),
    MODAUTOTYPER(91),
    CONSOLE(99),
    TRADEREQ(101),
    TRADE(102),
    CHALREQ_TRADE(103),
    CHALREQ_FRIENDSCHAT(104),
    SPAM(105),
    PLAYERRELATED(106),
    TENSECTIMEOUT(107),
    WELCOME(108),
    CLAN_CREATION_INVITATION(109),
    CHALREQ_CLANCHAT(110),
    CLAN_GIM_FORM_GROUP(111),
    CLAN_GIM_GROUP_WITH(112),
    CLAN_GIM_CHAT(-1),
    CLAN_GIM_MESSAGE(-1),
    YELLCHAT(1000),
    MODYELLCHAT(1001),
    BROADCAST_FRIEND(1002),
    BROADCAST_WORLD(1003),
    BROADCAST_GLOBAL(1004),
    UNKNOWN(-1);

    private final int type;
    private static final Map<Integer, ChatMessageType> CHAT_MESSAGE_TYPES;

    public static ChatMessageType of(int type) {
        return CHAT_MESSAGE_TYPES.getOrDefault(type, UNKNOWN);
    }

    private ChatMessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    static {
        CHAT_MESSAGE_TYPES = new HashMap<Integer, ChatMessageType>();
        for (ChatMessageType chatMessageType : ChatMessageType.values()) {
            if (chatMessageType.type == -1) continue;
            CHAT_MESSAGE_TYPES.put(chatMessageType.type, chatMessageType);
        }
    }
}

