/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.GroundObject
 */
package rs.api;

import net.runelite.api.GroundObject;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRenderable;

public interface RSGroundObject
extends GroundObject {
    @Import(value="hash")
    public long getHash();

    @Import(value="x")
    public int getX();

    @Import(value="y")
    public int getY();

    @Import(value="z")
    public int getZ();

    @Import(value="renderable")
    public RSRenderable getRenderable();

    public void setPlane(int var1);

    @Import(value="config")
    public int getConfig();
}

