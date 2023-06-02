/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Mesh;
import net.runelite.api.Model;
import net.runelite.api.Renderable;

public interface ModelData
extends Mesh<ModelData>,
Renderable {
    public static final int DEFAULT_AMBIENT = 64;
    public static final int DEFAULT_CONTRAST = 768;
    public static final int DEFAULT_X = -50;
    public static final int DEFAULT_Y = -10;
    public static final int DEFAULT_Z = -50;

    public short[] getFaceColors();

    public Model light(int var1, int var2, int var3, int var4, int var5);

    public Model light();

    public ModelData recolor(short var1, short var2);

    public ModelData retexture(short var1, short var2);

    public ModelData shallowCopy();

    public ModelData cloneVertices();

    public ModelData cloneColors();

    public ModelData cloneTextures();

    public ModelData cloneTransparencies();
}

