/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.GraphicsObject
 */
package rs.api;

import net.runelite.api.GraphicsObject;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSAnimation;
import net.runelite.rs.api.RSRenderable;

public interface RSGraphicsObject
extends GraphicsObject,
RSRenderable {
    @Import(value="id")
    public int getId();

    @Import(value="id")
    public void setId(int var1);

    @Import(value="x")
    public int getX();

    @Import(value="x")
    public void setX(int var1);

    @Import(value="y")
    public int getY();

    @Import(value="y")
    public void setY(int var1);

    @Import(value="startCycle")
    public int getStartCycle();

    @Import(value="level")
    public int getLevel();

    @Import(value="level")
    public void setLevel(int var1);

    @Import(value="z")
    public int getZ();

    @Import(value="z")
    public void setZ(int var1);

    @Import(value="frame")
    public int getFrame();

    @Import(value="frame")
    public void setFrame(int var1);

    @Import(value="frameCycle")
    public int getFrameCycle();

    @Import(value="frameCycle")
    public void setFrameCycle(int var1);

    @Import(value="seq")
    public RSAnimation getSeq();

    @Import(value="seq")
    public void setSeq(RSAnimation var1);

    @Import(value="finished")
    public boolean finished();

    @Import(value="finished")
    public void setFinished(boolean var1);
}

