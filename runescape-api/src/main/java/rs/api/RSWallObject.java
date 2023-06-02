/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.WallObject
 */
package rs.api;

import net.runelite.api.WallObject;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRenderable;

public interface RSWallObject
extends WallObject {
    @Import(value="hash")
    public long getHash();

    @Import(value="x")
    public int getX();

    @Import(value="y")
    public int getY();

    @Import(value="z")
    public int getZ();

    @Import(value="orientationA")
    public int getOrientationA();

    @Import(value="orientationB")
    public int getOrientationB();

    @Import(value="renderable1")
    public RSRenderable getRenderable1();

    @Import(value="renderable2")
    public RSRenderable getRenderable2();

    @Import(value="config")
    public int getConfig();

    public void setPlane(int var1);
}

