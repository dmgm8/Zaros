/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

public interface Mesh<T extends Mesh<T>> {
    public int getVerticesCount();

    public int[] getVerticesX();

    public int[] getVerticesY();

    public int[] getVerticesZ();

    public int getFaceCount();

    public int[] getFaceIndices1();

    public int[] getFaceIndices2();

    public int[] getFaceIndices3();

    public byte[] getFaceTransparencies();

    public short[] getFaceTextures();

    public T rotateY90Ccw();

    public T rotateY180Ccw();

    public T rotateY270Ccw();

    public T translate(int var1, int var2, int var3);

    public T scale(int var1, int var2, int var3);
}

