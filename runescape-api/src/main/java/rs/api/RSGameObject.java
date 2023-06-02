/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.GameObject
 */
package rs.api;

import net.runelite.api.GameObject;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRenderable;

public interface RSGameObject
extends GameObject {
    @Import(value="renderable")
    public RSRenderable getRenderable();

    @Import(value="plane")
    public int getPlane();

    @Import(value="relativeX")
    public int getRelativeX();

    @Import(value="relativeY")
    public int getRelativeY();

    @Import(value="offsetX")
    public int getOffsetX();

    @Import(value="offsetY")
    public int getOffsetY();

    @Import(value="x")
    public int getX();

    @Import(value="y")
    public int getY();

    @Import(value="z")
    public int getZ();

    @Import(value="orientation")
    public int getModelOrientation();

    @Import(value="hash")
    public long getHash();

    @Import(value="flags")
    public int getConfig();
}

