/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Renderable;
import net.runelite.api.coords.LocalPoint;

public interface GraphicsObject
extends Renderable {
    public int getId();

    public LocalPoint getLocation();

    public int getStartCycle();

    public int getLevel();

    public int getZ();

    public boolean finished();

    public void setFinished(boolean var1);
}

