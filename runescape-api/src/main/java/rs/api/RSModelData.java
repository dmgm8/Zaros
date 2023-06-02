/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ModelData
 */
package rs.api;

import net.runelite.api.ModelData;
import net.runelite.mapping.Construct;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSVertexNormal;

public interface RSModelData
extends ModelData {
    @Construct
    public RSModelData createModelData(RSModelData var1, boolean var2, boolean var3, boolean var4, boolean var5);

    @Import(value="faceColors")
    public short[] getFaceColors();

    @Import(value="faceColors")
    public void setFaceColors(short[] var1);

    @Import(value="light")
    public RSModel light(int var1, int var2, int var3, int var4, int var5);

    @Import(value="triangleFaceCount")
    public int getFaceCount();

    @Import(value="trianglePointsX")
    public int[] getFaceIndices1();

    @Import(value="trianglePointsY")
    public int[] getFaceIndices2();

    @Import(value="trianglePointsZ")
    public int[] getFaceIndices3();

    @Import(value="vertexX")
    public int[] getVerticesX();

    @Import(value="vertexX")
    public void setVerticesX(int[] var1);

    @Import(value="vertexY")
    public int[] getVerticesY();

    @Import(value="vertexY")
    public void setVerticesY(int[] var1);

    @Import(value="vertexZ")
    public int[] getVerticesZ();

    @Import(value="vertexZ")
    public void setVerticesZ(int[] var1);

    @Import(value="texTriangleX")
    public short[] getTexTriangleX();

    @Import(value="texTriangleY")
    public short[] getTexTriangleY();

    @Import(value="texTriangleZ")
    public short[] getTexTriangleZ();

    @Import(value="faceTextures")
    public short[] getFaceTextures();

    @Import(value="faceTextures")
    public void setFaceTextures(short[] var1);

    @Import(value="textureCoords")
    public byte[] getTextureCoords();

    @Import(value="textureRenderTypes")
    public byte[] getTextureRenderTypes();

    @Import(value="verticesCount")
    public int getVerticesCount();

    @Import(value="vertexNormals")
    public RSVertexNormal[] getVertexNormals();

    @Import(value="vertexVertices")
    public RSVertexNormal[] getVertexVertices();

    @Import(value="faceTransparencies")
    public byte[] getFaceTransparencies();

    @Import(value="faceTransparencies")
    public void setFaceTransparencies(byte[] var1);

    @Import(value="recolor")
    public void rs$recolor(short var1, short var2);

    @Import(value="retexture")
    public void rs$retexture(short var1, short var2);

    @Import(value="translate")
    public void rs$translate(int var1, int var2, int var3);

    @Import(value="scale")
    public void rs$scale(int var1, int var2, int var3);

    @Import(value="rotateY90Ccw")
    public void rs$rotateY90Ccw();

    @Import(value="rotateY180Ccw")
    public void rs$rotateY180Ccw();

    @Import(value="rotateY270Ccw")
    public void rs$rotateY270Ccw();

    @Import(value="ambient")
    public short getAmbient();

    @Import(value="contrast")
    public short getContrast();
}

