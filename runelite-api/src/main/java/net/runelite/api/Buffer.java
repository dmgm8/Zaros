/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Node;

public interface Buffer
extends Node {
    public byte[] getPayload();

    public int getOffset();
}

