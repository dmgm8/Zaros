/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.SceneTilePaint
 */
package rs.api;

import net.runelite.api.SceneTilePaint;
import net.runelite.mapping.Import;

public interface RSSceneTilePaint
extends SceneTilePaint {
    @Import(value="rgb")
    public int getRBG();

    @Import(value="swColor")
    public int getSwColor();

    @Import(value="seColor")
    public int getSeColor();

    @Import(value="nwColor")
    public int getNwColor();

    @Import(value="neColor")
    public int getNeColor();

    @Import(value="texture")
    public int getTexture();
}

