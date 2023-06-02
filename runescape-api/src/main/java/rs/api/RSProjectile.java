/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Projectile
 */
package rs.api;

import net.runelite.api.Projectile;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRenderable;

public interface RSProjectile
extends RSRenderable,
Projectile {
    @Import(value="id")
    public int getId();

    @Import(value="height")
    public int getHeight();

    @Import(value="endHeight")
    public int getEndHeight();

    @Import(value="x1")
    public int getX1();

    @Import(value="y1")
    public int getY1();

    @Import(value="floor")
    public int getFloor();

    @Import(value="startCycle")
    public int getStartCycle();

    @Import(value="endCycle")
    public int getEndCycle();

    @Import(value="endCycle")
    public void setEndCycle(int var1);

    @Import(value="slope")
    public int getSlope();

    @Import(value="startHeight")
    public int getStartHeight();

    @Import(value="x")
    public double getX();

    @Import(value="y")
    public double getY();

    @Import(value="z")
    public double getZ();

    @Import(value="scalar")
    public double getScalar();

    @Import(value="velocityX")
    public double getVelocityX();

    @Import(value="velocityY")
    public double getVelocityY();

    @Import(value="velocityZ")
    public double getVelocityZ();

    @Import(value="setTarget")
    public void setTarget(int var1, int var2, int var3, int var4);

    @Import(value="interacting")
    public int rs$getInteracting();
}

