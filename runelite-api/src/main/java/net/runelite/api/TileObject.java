/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 */
package net.runelite.api;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public interface TileObject {
    public long getHash();

    public int getX();

    public int getY();

    public int getZ();

    public int getPlane();

    public int getId();

    @Nonnull
    public WorldPoint getWorldLocation();

    @Nonnull
    public LocalPoint getLocalLocation();

    @Nullable
    public Point getCanvasLocation();

    @Nullable
    public Point getCanvasLocation(int var1);

    @Nullable
    public Polygon getCanvasTilePoly();

    @Nullable
    public Point getCanvasTextLocation(Graphics2D var1, String var2, int var3);

    @Nullable
    public Point getMinimapLocation();

    @Nullable
    public Shape getClickbox();
}

