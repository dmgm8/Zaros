/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface SpritePixels {
    public static final int DEFAULT_SHADOW_COLOR = 0x302020;

    public void drawAt(int var1, int var2);

    public int getWidth();

    public int getHeight();

    public int getMaxWidth();

    public int getMaxHeight();

    public int getOffsetX();

    public int getOffsetY();

    public void setMaxWidth(int var1);

    public void setMaxHeight(int var1);

    public void setOffsetX(int var1);

    public void setOffsetY(int var1);

    public int[] getPixels();

    public BufferedImage toBufferedImage();

    public void toBufferedImage(BufferedImage var1) throws IllegalArgumentException;

    public BufferedImage toBufferedOutline(Color var1);

    public void toBufferedOutline(BufferedImage var1, int var2);
}

