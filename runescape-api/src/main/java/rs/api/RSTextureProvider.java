/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.TextureProvider
 */
package rs.api;

import net.runelite.api.TextureProvider;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSTexture;

public interface RSTextureProvider
extends TextureProvider {
    @Import(value="brightness")
    public double getBrightness();

    @Import(value="setBrightness")
    public void setBrightness(double var1);

    @Import(value="maxSize")
    public void setMaxSize(int var1);

    @Import(value="size")
    public void setSize(int var1);

    @Import(value="textures")
    public RSTexture[] getTextures();

    @Import(value="load")
    public int[] load(int var1);
}

