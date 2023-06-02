/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.BufferProvider
 */
package rs.api;

import net.runelite.api.BufferProvider;
import net.runelite.mapping.Import;

public interface RSBufferProvider
extends BufferProvider {
    @Import(value="pixels")
    public int[] getPixels();

    @Import(value="width")
    public int getWidth();

    @Import(value="height")
    public int getHeight();

    @Import(value="setRaster")
    public void setRaster();
}

