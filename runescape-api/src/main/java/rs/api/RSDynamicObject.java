/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Renderable
 */
package rs.api;

import net.runelite.api.Renderable;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRenderable;

public interface RSDynamicObject
extends Renderable,
RSRenderable {
    @Import(value="id")
    public int getId();

    @Import(value="animFrame")
    public int getAnimFrame();

    @Import(value="animFrame")
    public void setAnimFrame(int var1);

    @Import(value="animCycleCount")
    public int getAnimCycleCount();
}

