/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Mesh;
import net.runelite.api.Renderable;

public interface Model
extends Mesh<Model>,
Renderable {
    public int[] getFaceColors1();

    public int[] getFaceColors2();

    public int[] getFaceColors3();

    public int getSceneId();

    public void setSceneId(int var1);

    public int getBufferOffset();

    public void setBufferOffset(int var1);

    public int getUvBufferOffset();

    public void setUvBufferOffset(int var1);

    public int getBottomY();

    public void calculateBoundsCylinder();

    public byte[] getFaceRenderPriorities();

    public int getRadius();

    public int getDiameter();

    public float[] getFaceTextureUVCoordinates();

    public void calculateExtreme(int var1);

    public int getCenterX();

    public int getCenterY();

    public int getCenterZ();

    public int getExtremeX();

    public int getExtremeY();

    public int getExtremeZ();

    public int getXYZMag();

    public boolean isClickable();

    public int[] getVertexNormalsX();

    public int[] getVertexNormalsY();

    public int[] getVertexNormalsZ();

    public byte getOverrideAmount();

    public byte getOverrideHue();

    public byte getOverrideSaturation();

    public byte getOverrideLuminance();
}

