/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Node;

public interface Script
extends Node {
    public int[] getIntOperands();

    public int[] getInstructions();
}

