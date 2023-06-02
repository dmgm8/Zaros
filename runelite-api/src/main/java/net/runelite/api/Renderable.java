/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Model;
import net.runelite.api.Node;

public interface Renderable
extends Node {
    public Model getModel();

    public int getModelHeight();

    public void setModelHeight(int var1);

    public void draw(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9);
}

