/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.awt.Shape;
import net.runelite.api.Renderable;
import net.runelite.api.TileObject;

public interface GroundObject
extends TileObject {
    public Renderable getRenderable();

    public Shape getConvexHull();

    public int getConfig();
}

