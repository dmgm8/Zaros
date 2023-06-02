/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Texture
 */
package rs.api;

import net.runelite.api.Texture;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSTexture
extends Texture,
RSNode {
    @Import(value="pixels")
    public int[] getPixels();

    @Import(value="animationDirection")
    public int getAnimationDirection();

    @Import(value="animationSpeed")
    public int getAnimationSpeed();

    @Import(value="loaded")
    public boolean isLoaded();
}

