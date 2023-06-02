/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.MessageNode;

public interface ChatLineBuffer {
    public MessageNode[] getLines();

    public int getLength();

    public void removeMessageNode(MessageNode var1);
}

