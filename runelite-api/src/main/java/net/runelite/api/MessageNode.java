/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Node;

public interface MessageNode
extends Node {
    public int getId();

    public ChatMessageType getType();

    public String getName();

    public void setName(String var1);

    public String getSender();

    public void setSender(String var1);

    public String getValue();

    public void setValue(String var1);

    public String getRuneLiteFormatMessage();

    public void setRuneLiteFormatMessage(String var1);

    public int getTimestamp();

    public void setTimestamp(int var1);
}

