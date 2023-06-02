/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Model
 */
package rs.api;

import java.awt.Shape;
import net.runelite.api.Model;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSFrames;
import net.runelite.rs.api.RSRenderable;

public interface RSModel
extends RSRenderable,
Model {
    @Import(value="verticesCount")
    public int getVerticesCount();

    @Import(value="verticesX")
    public int[] getVerticesX();

    @Import(value="verticesY")
    public int[] getVerticesY();

    @Import(value="verticesZ")
    public int[] getVerticesZ();

    @Import(value="faceCount")
    public int getFaceCount();

    @Import(value="faceIndices1")
    public int[] getFaceIndices1();

    @Import(value="faceIndices2")
    public int[] getFaceIndices2();

    @Import(value="faceIndices3")
    public int[] getFaceIndices3();

    @Import(value="faceColors1")
    public int[] getFaceColors1();

    @Import(value="faceColors2")
    public int[] getFaceColors2();

    @Import(value="faceColors3")
    public int[] getFaceColors3();

    @Import(value="faceTransparencies")
    public byte[] getFaceTransparencies();

    @Import(value="faceRenderPriorities")
    public byte[] getFaceRenderPriorities();

    @Import(value="vertexGroups")
    public int[][] getVertexGroups();

    @Override
    @Import(value="modelHeight")
    public int getModelHeight();

    @Import(value="animate")
    public void animate(int var1, int[] var2, int var3, int var4, int var5);

    @Import(value="bottomY")
    public int getBottomY();

    @Import(value="calculateBoundsCylinder")
    public void calculateBoundsCylinder();

    @Import(value="calculateExtreme")
    public void calculateExtreme(int var1);

    @Import(value="resetBounds")
    public void resetBounds();

    @Import(value="toSharedModel")
    public RSModel toSharedModel(boolean var1);

    @Import(value="toSharedSpotAnimModel")
    public RSModel toSharedSpotAnimModel(boolean var1);

    @Import(value="rotateY90Ccw")
    public void rs$rotateY90Ccw();

    @Import(value="rotateY180Ccw")
    public void rs$rotateY180Ccw();

    @Import(value="rotateY270Ccw")
    public void rs$rotateY270Ccw();

    @Import(value="translate")
    public void rs$translate(int var1, int var2, int var3);

    @Import(value="scale")
    public void rs$scale(int var1, int var2, int var3);

    @Import(value="isClickable")
    public boolean isClickable();

    @Import(value="radius")
    public int getRadius();

    @Import(value="diameter")
    public int getDiameter();

    @Import(value="centerX")
    public int getCenterX();

    @Import(value="centerY")
    public int getCenterY();

    @Import(value="centerZ")
    public int getCenterZ();

    @Import(value="extremeX")
    public int getExtremeX();

    @Import(value="extremeY")
    public int getExtremeY();

    @Import(value="extremeZ")
    public int getExtremeZ();

    @Import(value="faceTextures")
    public short[] getFaceTextures();

    @Import(value="XYZMag")
    public int getXYZMag();

    public void interpolateFrames(RSFrames var1, int var2, RSFrames var3, int var4, int var5, int var6);

    public Shape getConvexHull(int var1, int var2, int var3, int var4);

    public float[] getFaceTextureUVCoordinates();

    public void setFaceTextureUVCoordinates(float[] var1);

    public void setVertexNormalsY(int[] var1);

    public void setVertexNormalsX(int[] var1);

    public void setVertexNormalsZ(int[] var1);

    @Import(value="overrideHue")
    public byte getOverrideHue();

    @Import(value="overrideSaturation")
    public byte getOverrideSaturation();

    @Import(value="overrideLuminance")
    public byte getOverrideLuminance();

    @Import(value="overrideAmount")
    public byte getOverrideAmount();
}

