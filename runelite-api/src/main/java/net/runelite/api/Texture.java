/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Node;

public interface Texture
extends Node {
    public int[] getPixels();

    public int getAnimationDirection();

    public int getAnimationSpeed();

    public boolean isLoaded();

    public float getU();

    public void setU(float var1);

    public float getV();

    public void setV(float var1);
}

