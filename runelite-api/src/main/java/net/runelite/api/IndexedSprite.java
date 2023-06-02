/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public interface IndexedSprite {
    public byte[] getPixels();

    public void setPixels(byte[] var1);

    public int[] getPalette();

    public void setPalette(int[] var1);

    public int getOffsetX();

    public void setOffsetX(int var1);

    public int getOffsetY();

    public void setOffsetY(int var1);

    public int getWidth();

    public void setWidth(int var1);

    public int getHeight();

    public void setHeight(int var1);

    public int getOriginalWidth();

    public void setOriginalWidth(int var1);

    public int getOriginalHeight();

    public void setOriginalHeight(int var1);
}

