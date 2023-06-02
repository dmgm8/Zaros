/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Animation;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Model;
import net.runelite.api.coords.LocalPoint;

public interface RuneLiteObject
extends GraphicsObject {
    public void setModel(Model var1);

    public void setAnimation(Animation var1);

    public void setShouldLoop(boolean var1);

    public void setLocation(LocalPoint var1, int var2);

    public void setActive(boolean var1);

    public boolean isActive();
}

