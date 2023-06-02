/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.DecorativeObject
 */
package rs.api;

import net.runelite.api.DecorativeObject;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRenderable;

public interface RSDecorativeObject
extends DecorativeObject {
    @Import(value="hash")
    public long getHash();

    @Import(value="x")
    public int getX();

    @Import(value="y")
    public int getY();

    @Import(value="z")
    public int getZ();

    @Import(value="offsetX")
    public int getXOffset();

    @Import(value="offsetY")
    public int getYOffset();

    @Import(value="rotation")
    public int getOrientation();

    @Import(value="renderable1")
    public RSRenderable getRenderable();

    @Import(value="renderable2")
    public RSRenderable getRenderable2();

    public void setPlane(int var1);

    @Import(value="config")
    public int getConfig();
}

