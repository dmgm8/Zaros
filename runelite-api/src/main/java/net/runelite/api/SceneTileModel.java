/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public interface SceneTileModel {
    public int getModelUnderlay();

    public int getModelOverlay();

    public int getShape();

    public int getRotation();

    public int[] getFaceX();

    public int[] getFaceY();

    public int[] getFaceZ();

    public int[] getVertexX();

    public int[] getVertexY();

    public int[] getVertexZ();

    public int[] getTriangleColorA();

    public int[] getTriangleColorB();

    public int[] getTriangleColorC();

    public int[] getTriangleTextureId();

    public int getBufferOffset();

    public void setBufferOffset(int var1);

    public int getUvBufferOffset();

    public void setUvBufferOffset(int var1);

    public int getBufferLen();

    public void setBufferLen(int var1);
}

