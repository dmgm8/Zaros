/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.awt.Shape;
import net.runelite.api.Renderable;
import net.runelite.api.TileObject;

public interface DecorativeObject
extends TileObject {
    public Shape getConvexHull();

    public Shape getConvexHull2();

    public Renderable getRenderable();

    public Renderable getRenderable2();

    public int getXOffset();

    public int getYOffset();

    public int getConfig();
}

