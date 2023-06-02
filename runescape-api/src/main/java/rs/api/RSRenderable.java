/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Renderable
 */
package rs.api;

import net.runelite.api.Renderable;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSNode;

public interface RSRenderable
extends RSNode,
Renderable {
    @Import(value="modelHeight")
    public int getModelHeight();

    @Import(value="modelHeight")
    public void setModelHeight(int var1);

    @Import(value="getModel")
    public RSModel getModel();

    @Import(value="draw")
    public void draw(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9);
}

