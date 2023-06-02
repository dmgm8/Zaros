/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ItemLayer
 */
package rs.api;

import net.runelite.api.ItemLayer;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRenderable;

public interface RSItemLayer
extends ItemLayer {
    @Import(value="x")
    public int getX();

    @Import(value="y")
    public int getY();

    @Import(value="z")
    public int getZ();

    @Import(value="hash")
    public long getHash();

    @Import(value="height")
    public int getHeight();

    @Import(value="bottom")
    public RSRenderable getBottom();

    @Import(value="middle")
    public RSRenderable getMiddle();

    @Import(value="top")
    public RSRenderable getTop();

    public void setPlane(int var1);
}

