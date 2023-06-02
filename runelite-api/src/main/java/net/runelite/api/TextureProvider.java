/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Texture;

public interface TextureProvider {
    public double getBrightness();

    public void setBrightness(double var1);

    public Texture[] getTextures();

    public int[] load(int var1);
}

