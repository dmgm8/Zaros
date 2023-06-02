/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.SpritePixels
 */
package rs.api;

import net.runelite.api.SpritePixels;
import net.runelite.mapping.Import;

public interface RSSpritePixels
extends SpritePixels {
    @Import(value="drawAt")
    public void drawAt(int var1, int var2);

    @Import(value="width")
    public int getWidth();

    @Import(value="height")
    public int getHeight();

    @Import(value="maxWidth")
    public int getMaxWidth();

    @Import(value="maxHeight")
    public int getMaxHeight();

    @Import(value="offsetX")
    public int getOffsetX();

    @Import(value="offsetY")
    public int getOffsetY();

    @Import(value="maxWidth")
    public void setMaxWidth(int var1);

    @Import(value="maxHeight")
    public void setMaxHeight(int var1);

    @Import(value="offsetX")
    public void setOffsetX(int var1);

    @Import(value="offsetY")
    public void setOffsetY(int var1);

    @Import(value="pixels")
    public int[] getPixels();

    @Import(value="setRaster")
    public void setRaster();
}

