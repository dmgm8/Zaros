/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.IndexedSprite
 */
package rs.api;

import net.runelite.api.IndexedSprite;
import net.runelite.mapping.Import;

public interface RSIndexedSprite
extends IndexedSprite {
    @Import(value="pixels")
    public byte[] getPixels();

    @Import(value="pixels")
    public void setPixels(byte[] var1);

    @Import(value="palette")
    public int[] getPalette();

    @Import(value="palette")
    public void setPalette(int[] var1);

    @Import(value="originalWidth")
    public int getOriginalWidth();

    @Import(value="originalWidth")
    public void setOriginalWidth(int var1);

    @Import(value="originalHeight")
    public int getOriginalHeight();

    @Import(value="originalHeight")
    public void setOriginalHeight(int var1);

    @Import(value="height")
    public int getHeight();

    @Import(value="height")
    public void setHeight(int var1);

    @Import(value="offsetX")
    public int getOffsetX();

    @Import(value="offsetX")
    public void setOffsetX(int var1);

    @Import(value="offsetY")
    public int getOffsetY();

    @Import(value="offsetY")
    public void setOffsetY(int var1);

    @Import(value="width")
    public int getWidth();

    @Import(value="width")
    public void setWidth(int var1);
}

