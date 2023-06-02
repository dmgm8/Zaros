/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.coords.LocalPoint;

public interface AmbientSoundEffect {
    public int getSoundEffectId();

    public int getPlane();

    public LocalPoint getMinPosition();

    public LocalPoint getMaxPosition();
}

