/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.chat;

import net.runelite.client.events.ChatboxInput;
import net.runelite.client.events.PrivateMessageInput;

public interface ChatboxInputListener {
    public boolean onChatboxInput(ChatboxInput var1);

    public boolean onPrivateMessageInput(PrivateMessageInput var1);
}

