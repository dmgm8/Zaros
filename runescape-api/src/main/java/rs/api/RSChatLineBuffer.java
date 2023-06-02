/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ChatLineBuffer
 *  net.runelite.api.MessageNode
 */
package rs.api;

import net.runelite.api.ChatLineBuffer;
import net.runelite.api.MessageNode;
import net.runelite.mapping.Import;

public interface RSChatLineBuffer
extends ChatLineBuffer {
    @Import(value="lines")
    public MessageNode[] getLines();

    @Import(value="length")
    public int getLength();

    @Import(value="length")
    public void setLength(int var1);
}

