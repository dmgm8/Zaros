/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Stopwatch
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.DecorativeObject
 *  net.runelite.api.GameObject
 *  net.runelite.api.GroundObject
 *  net.runelite.api.Model
 *  net.runelite.api.Perspective
 *  net.runelite.api.Point
 *  net.runelite.api.Renderable
 *  net.runelite.api.Scene
 *  net.runelite.api.SceneTileModel
 *  net.runelite.api.SceneTilePaint
 *  net.runelite.api.Tile
 *  net.runelite.api.WallObject
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.gpu;

import com.google.common.base.Stopwatch;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.SceneTileModel;
import net.runelite.api.SceneTilePaint;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.client.plugins.gpu.GpuFloatBuffer;
import net.runelite.client.plugins.gpu.GpuIntBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class SceneUploader {
    private static final Logger log = LoggerFactory.getLogger(SceneUploader.class);
    @Inject
    private Client client;
    int sceneId = (int)System.nanoTime();
    private int offset;
    private int uvoffset;
    private static int[] distances;
    private static char[] distanceFaceCount;
    private static char[][] distanceToFaces;
    private static float[] modelCanvasX;
    private static float[] modelCanvasY;
    private static int[] modelLocalX;
    private static int[] modelLocalY;
    private static int[] modelLocalZ;
    private static int[] numOfPriority;
    private static int[] eq10;
    private static int[] eq11;
    private static int[] lt10;
    private static int[][] orderedFaces;

    SceneUploader() {
    }

    void upload(Scene scene, GpuIntBuffer vertexbuffer, GpuFloatBuffer uvBuffer) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ++this.sceneId;
        this.offset = 0;
        this.uvoffset = 0;
        vertexbuffer.clear();
        uvBuffer.clear();
        for (int z = 0; z < 4; ++z) {
            for (int x = 0; x < 104; ++x) {
                for (int y = 0; y < 104; ++y) {
                    Tile tile = scene.getTiles()[z][x][y];
                    if (tile == null) continue;
                    this.upload(tile, vertexbuffer, uvBuffer);
                }
            }
        }
        stopwatch.stop();
        log.debug("Scene upload time: {}", (Object)stopwatch);
    }

    private void upload(Tile tile, GpuIntBuffer vertexBuffer, GpuFloatBuffer uvBuffer) {
        GameObject[] gameObjects;
        DecorativeObject decorativeObject;
        Renderable renderable;
        GroundObject groundObject;
        WallObject wallObject;
        SceneTileModel sceneTileModel;
        SceneTilePaint sceneTilePaint;
        Tile bridge = tile.getBridge();
        if (bridge != null) {
            this.upload(bridge, vertexBuffer, uvBuffer);
        }
        if ((sceneTilePaint = tile.getSceneTilePaint()) != null) {
            sceneTilePaint.setBufferOffset(this.offset);
            if (sceneTilePaint.getTexture() != -1) {
                sceneTilePaint.setUvBufferOffset(this.uvoffset);
            } else {
                sceneTilePaint.setUvBufferOffset(-1);
            }
            Point tilePoint = tile.getSceneLocation();
            int len = this.upload(sceneTilePaint, tile.getRenderLevel(), tilePoint.getX(), tilePoint.getY(), vertexBuffer, uvBuffer, 0, 0, false);
            sceneTilePaint.setBufferLen(len);
            this.offset += len;
            if (sceneTilePaint.getTexture() != -1) {
                this.uvoffset += len;
            }
        }
        if ((sceneTileModel = tile.getSceneTileModel()) != null) {
            sceneTileModel.setBufferOffset(this.offset);
            if (sceneTileModel.getTriangleTextureId() != null) {
                sceneTileModel.setUvBufferOffset(this.uvoffset);
            } else {
                sceneTileModel.setUvBufferOffset(-1);
            }
            Point tilePoint = tile.getSceneLocation();
            int len = this.upload(sceneTileModel, tilePoint.getX(), tilePoint.getY(), vertexBuffer, uvBuffer, 0, 0, false);
            sceneTileModel.setBufferLen(len);
            this.offset += len;
            if (sceneTileModel.getTriangleTextureId() != null) {
                this.uvoffset += len;
            }
        }
        if ((wallObject = tile.getWallObject()) != null) {
            Renderable renderable2;
            Renderable renderable1 = wallObject.getRenderable1();
            if (renderable1 instanceof Model) {
                this.uploadSceneModel((Model)renderable1, vertexBuffer, uvBuffer);
            }
            if ((renderable2 = wallObject.getRenderable2()) instanceof Model) {
                this.uploadSceneModel((Model)renderable2, vertexBuffer, uvBuffer);
            }
        }
        if ((groundObject = tile.getGroundObject()) != null && (renderable = groundObject.getRenderable()) instanceof Model) {
            this.uploadSceneModel((Model)renderable, vertexBuffer, uvBuffer);
        }
        if ((decorativeObject = tile.getDecorativeObject()) != null) {
            Renderable renderable2;
            Renderable renderable3 = decorativeObject.getRenderable();
            if (renderable3 instanceof Model) {
                this.uploadSceneModel((Model)renderable3, vertexBuffer, uvBuffer);
            }
            if ((renderable2 = decorativeObject.getRenderable2()) instanceof Model) {
                this.uploadSceneModel((Model)renderable2, vertexBuffer, uvBuffer);
            }
        }
        for (GameObject gameObject : gameObjects = tile.getGameObjects()) {
            Renderable renderable4;
            if (gameObject == null || !((renderable4 = gameObject.getRenderable()) instanceof Model)) continue;
            this.uploadSceneModel((Model)gameObject.getRenderable(), vertexBuffer, uvBuffer);
        }
    }

    int upload(SceneTilePaint tile, int tileZ, int tileX, int tileY, GpuIntBuffer vertexBuffer, GpuFloatBuffer uvBuffer, int offsetX, int offsetY, boolean padUvs) {
        int[][][] tileHeights = this.client.getTileHeights();
        int localX = offsetX;
        int localY = offsetY;
        int swHeight = tileHeights[tileZ][tileX][tileY];
        int seHeight = tileHeights[tileZ][tileX + 1][tileY];
        int neHeight = tileHeights[tileZ][tileX + 1][tileY + 1];
        int nwHeight = tileHeights[tileZ][tileX][tileY + 1];
        int neColor = tile.getNeColor();
        int nwColor = tile.getNwColor();
        int seColor = tile.getSeColor();
        int swColor = tile.getSwColor();
        if (neColor == 12345678) {
            return 0;
        }
        vertexBuffer.ensureCapacity(24);
        uvBuffer.ensureCapacity(24);
        int vertexDx = localX;
        int vertexDy = localY;
        int vertexDz = swHeight;
        int c1 = swColor;
        int vertexCx = localX + 128;
        int vertexCy = localY;
        int vertexCz = seHeight;
        int c2 = seColor;
        int vertexAx = localX + 128;
        int vertexAy = localY + 128;
        int vertexAz = neHeight;
        int c3 = neColor;
        int vertexBx = localX;
        int vertexBy = localY + 128;
        int vertexBz = nwHeight;
        int c4 = nwColor;
        vertexBuffer.put(vertexAx, vertexAz, vertexAy, c3);
        vertexBuffer.put(vertexBx, vertexBz, vertexBy, c4);
        vertexBuffer.put(vertexCx, vertexCz, vertexCy, c2);
        vertexBuffer.put(vertexDx, vertexDz, vertexDy, c1);
        vertexBuffer.put(vertexCx, vertexCz, vertexCy, c2);
        vertexBuffer.put(vertexBx, vertexBz, vertexBy, c4);
        if (padUvs || tile.getTexture() != -1) {
            float tex = (float)tile.getTexture() + 1.0f;
            uvBuffer.put(tex, 1.0f, 1.0f, 0.0f);
            uvBuffer.put(tex, 0.0f, 1.0f, 0.0f);
            uvBuffer.put(tex, 1.0f, 0.0f, 0.0f);
            uvBuffer.put(tex, 0.0f, 0.0f, 0.0f);
            uvBuffer.put(tex, 1.0f, 0.0f, 0.0f);
            uvBuffer.put(tex, 0.0f, 1.0f, 0.0f);
        }
        return 6;
    }

    int upload(SceneTileModel sceneTileModel, int tileX, int tileY, GpuIntBuffer vertexBuffer, GpuFloatBuffer uvBuffer, int offsetX, int offsetY, boolean padUvs) {
        int[] faceX = sceneTileModel.getFaceX();
        int[] faceY = sceneTileModel.getFaceY();
        int[] faceZ = sceneTileModel.getFaceZ();
        int[] vertexX = sceneTileModel.getVertexX();
        int[] vertexY = sceneTileModel.getVertexY();
        int[] vertexZ = sceneTileModel.getVertexZ();
        int[] triangleColorA = sceneTileModel.getTriangleColorA();
        int[] triangleColorB = sceneTileModel.getTriangleColorB();
        int[] triangleColorC = sceneTileModel.getTriangleColorC();
        int[] triangleTextures = sceneTileModel.getTriangleTextureId();
        int faceCount = faceX.length;
        vertexBuffer.ensureCapacity(faceCount * 12);
        uvBuffer.ensureCapacity(faceCount * 12);
        int baseX = 128 * tileX;
        int baseY = 128 * tileY;
        int cnt = 0;
        for (int i = 0; i < faceCount; ++i) {
            int triangleA = faceX[i];
            int triangleB = faceY[i];
            int triangleC = faceZ[i];
            int colorA = triangleColorA[i];
            int colorB = triangleColorB[i];
            int colorC = triangleColorC[i];
            if (colorA == 12345678) continue;
            cnt += 3;
            int vertexXA = vertexX[triangleA] - baseX;
            int vertexZA = vertexZ[triangleA] - baseY;
            int vertexXB = vertexX[triangleB] - baseX;
            int vertexZB = vertexZ[triangleB] - baseY;
            int vertexXC = vertexX[triangleC] - baseX;
            int vertexZC = vertexZ[triangleC] - baseY;
            vertexBuffer.put(vertexXA + offsetX, vertexY[triangleA], vertexZA + offsetY, colorA);
            vertexBuffer.put(vertexXB + offsetX, vertexY[triangleB], vertexZB + offsetY, colorB);
            vertexBuffer.put(vertexXC + offsetX, vertexY[triangleC], vertexZC + offsetY, colorC);
            if (!padUvs && triangleTextures == null) continue;
            if (triangleTextures != null && triangleTextures[i] != -1) {
                float tex = (float)triangleTextures[i] + 1.0f;
                uvBuffer.put(tex, (float)vertexXA / 128.0f, (float)vertexZA / 128.0f, 0.0f);
                uvBuffer.put(tex, (float)vertexXB / 128.0f, (float)vertexZB / 128.0f, 0.0f);
                uvBuffer.put(tex, (float)vertexXC / 128.0f, (float)vertexZC / 128.0f, 0.0f);
                continue;
            }
            uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
            uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
            uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
        }
        return cnt;
    }

    private void uploadSceneModel(Model model, GpuIntBuffer vertexBuffer, GpuFloatBuffer uvBuffer) {
        if (model.getSceneId() == this.sceneId) {
            return;
        }
        model.setBufferOffset(this.offset);
        if (model.getFaceTextures() != null) {
            model.setUvBufferOffset(this.uvoffset);
        } else {
            model.setUvBufferOffset(-1);
        }
        model.setSceneId(this.sceneId);
        int len = this.pushModel(model, vertexBuffer, uvBuffer);
        this.offset += len;
        if (model.getFaceTextures() != null) {
            this.uvoffset += len;
        }
    }

    public int pushModel(Model model, GpuIntBuffer vertexBuffer, GpuFloatBuffer uvBuffer) {
        int triangleCount = Math.min(model.getFaceCount(), 6144);
        vertexBuffer.ensureCapacity(triangleCount * 12);
        uvBuffer.ensureCapacity(triangleCount * 12);
        int[] vertexX = model.getVerticesX();
        int[] vertexY = model.getVerticesY();
        int[] vertexZ = model.getVerticesZ();
        int[] indices1 = model.getFaceIndices1();
        int[] indices2 = model.getFaceIndices2();
        int[] indices3 = model.getFaceIndices3();
        int[] color1s = model.getFaceColors1();
        int[] color2s = model.getFaceColors2();
        int[] color3s = model.getFaceColors3();
        byte[] transparencies = model.getFaceTransparencies();
        short[] faceTextures = model.getFaceTextures();
        byte[] facePriorities = model.getFaceRenderPriorities();
        float[] uv = model.getFaceTextureUVCoordinates();
        byte overrideAmount = model.getOverrideAmount();
        byte overrideHue = model.getOverrideHue();
        byte overrideSat = model.getOverrideSaturation();
        byte overrideLum = model.getOverrideLuminance();
        int len = 0;
        for (int face = 0; face < triangleCount; ++face) {
            int color1 = color1s[face];
            int color2 = color2s[face];
            int color3 = color3s[face];
            if (color3 == -1) {
                color2 = color3 = color1;
            } else if (color3 == -2) {
                vertexBuffer.put(0, 0, 0, 0);
                vertexBuffer.put(0, 0, 0, 0);
                vertexBuffer.put(0, 0, 0, 0);
                if (faceTextures != null) {
                    uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
                    uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
                    uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
                }
                len += 3;
                continue;
            }
            if ((faceTextures == null || faceTextures[face] == -1) && overrideAmount > 0) {
                color1 = SceneUploader.interpolateHSL(color1, overrideHue, overrideSat, overrideLum, overrideAmount);
                color2 = SceneUploader.interpolateHSL(color2, overrideHue, overrideSat, overrideLum, overrideAmount);
                color3 = SceneUploader.interpolateHSL(color3, overrideHue, overrideSat, overrideLum, overrideAmount);
            }
            int packAlphaPriority = SceneUploader.packAlphaPriority(faceTextures, transparencies, facePriorities, face);
            int triangleA = indices1[face];
            int triangleB = indices2[face];
            int triangleC = indices3[face];
            vertexBuffer.put(vertexX[triangleA], vertexY[triangleA], vertexZ[triangleA], packAlphaPriority | color1);
            vertexBuffer.put(vertexX[triangleB], vertexY[triangleB], vertexZ[triangleB], packAlphaPriority | color2);
            vertexBuffer.put(vertexX[triangleC], vertexY[triangleC], vertexZ[triangleC], packAlphaPriority | color3);
            if (faceTextures != null) {
                SceneUploader.pushUvForFace(faceTextures, uv, face, uvBuffer);
            }
            len += 3;
        }
        return len;
    }

    void initSortingBuffers() {
        int MAX_VERTEX_COUNT = 6500;
        int MAX_DIAMETER = 6000;
        distances = new int[MAX_VERTEX_COUNT];
        distanceFaceCount = new char[MAX_DIAMETER];
        distanceToFaces = new char[MAX_DIAMETER][512];
        modelCanvasX = new float[MAX_VERTEX_COUNT];
        modelCanvasY = new float[MAX_VERTEX_COUNT];
        modelLocalX = new int[MAX_VERTEX_COUNT];
        modelLocalY = new int[MAX_VERTEX_COUNT];
        modelLocalZ = new int[MAX_VERTEX_COUNT];
        numOfPriority = new int[12];
        eq10 = new int[2000];
        eq11 = new int[2000];
        lt10 = new int[12];
        orderedFaces = new int[12][2000];
    }

    void releaseSortingBuffers() {
        distances = null;
        distanceFaceCount = null;
        distanceToFaces = null;
        modelCanvasX = null;
        modelCanvasY = null;
        modelLocalX = null;
        modelLocalY = null;
        modelLocalZ = null;
        numOfPriority = null;
        eq10 = null;
        eq11 = null;
        lt10 = null;
        orderedFaces = null;
    }

    int pushSortedModel(Model model, int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, GpuIntBuffer vertexBuffer, GpuFloatBuffer uvBuffer) {
        int cnt;
        int i;
        int vertexCount = model.getVerticesCount();
        int[] verticesX = model.getVerticesX();
        int[] verticesY = model.getVerticesY();
        int[] verticesZ = model.getVerticesZ();
        int faceCount = model.getFaceCount();
        int[] indices1 = model.getFaceIndices1();
        int[] indices2 = model.getFaceIndices2();
        int[] indices3 = model.getFaceIndices3();
        int[] faceColors3 = model.getFaceColors3();
        byte[] faceRenderPriorities = model.getFaceRenderPriorities();
        int centerX = this.client.getCenterX();
        int centerY = this.client.getCenterY();
        int zoom = this.client.get3dZoom();
        int cameraX = this.client.getCameraX2();
        int cameraY = this.client.getCameraY2();
        int cameraZ = this.client.getCameraZ2();
        x += cameraX;
        y += cameraY;
        z += cameraZ;
        int orientSine = 0;
        int orientCosine = 0;
        if (orientation != 0) {
            orientSine = Perspective.SINE[orientation];
            orientCosine = Perspective.COSINE[orientation];
        }
        for (int v = 0; v < vertexCount; ++v) {
            int vertexX = verticesX[v];
            int vertexY = verticesY[v];
            int vertexZ = verticesZ[v];
            if (orientation != 0) {
                int i2 = vertexZ * orientSine + vertexX * orientCosine >> 16;
                vertexZ = vertexZ * orientCosine - vertexX * orientSine >> 16;
                vertexX = i2;
            }
            int d = yawCos * vertexZ - vertexX * yawSin >> 16;
            SceneUploader.distances[v] = d = pitchCos * d + vertexY * pitchSin >> 16;
            SceneUploader.modelLocalX[v] = vertexX += x;
            SceneUploader.modelLocalY[v] = vertexY += y;
            SceneUploader.modelLocalZ[v] = vertexZ += z;
            float fpitchSin = (float)pitchSin / 65536.0f;
            float fpitchCos = (float)pitchCos / 65536.0f;
            float fyawSin = (float)yawSin / 65536.0f;
            float fyawCos = (float)yawCos / 65536.0f;
            float rotatedX = (float)(vertexZ -= cameraZ) * fyawSin + (float)(vertexX -= cameraX) * fyawCos;
            float rotatedZ = (float)vertexZ * fyawCos - (float)vertexX * fyawSin;
            float var13 = (float)(vertexY -= cameraY) * fpitchCos - rotatedZ * fpitchSin;
            float var12 = (float)vertexY * fpitchSin + rotatedZ * fpitchCos;
            SceneUploader.modelCanvasX[v] = rotatedX * (float)zoom / var12 + (float)centerX;
            SceneUploader.modelCanvasY[v] = var13 * (float)zoom / var12 + (float)centerY;
        }
        int diameter = model.getDiameter();
        int radius = model.getRadius();
        if (diameter >= 6000) {
            return 0;
        }
        Arrays.fill(distanceFaceCount, 0, diameter, '\u0000');
        for (int i3 = 0; i3 < faceCount; i3 = (int)((char)(i3 + 1))) {
            float aY;
            float cX;
            float bY;
            int v3;
            float cY;
            int v2;
            float bX;
            int v1;
            float aX;
            if (faceColors3[i3] == -2 || !(((aX = modelCanvasX[v1 = indices1[i3]]) - (bX = modelCanvasX[v2 = indices2[i3]])) * ((cY = modelCanvasY[v3 = indices3[i3]]) - (bY = modelCanvasY[v2])) - ((cX = modelCanvasX[v3]) - bX) * ((aY = modelCanvasY[v1]) - bY) > 0.0f)) continue;
            int distance = radius + (distances[v1] + distances[v2] + distances[v3]) / 3;
            assert (distance >= 0 && distance < diameter);
            int n = distance;
            char c = distanceFaceCount[n];
            distanceFaceCount[n] = (char)(c + '\u0001');
            SceneUploader.distanceToFaces[distance][c] = i3;
        }
        vertexBuffer.ensureCapacity(12 * faceCount);
        uvBuffer.ensureCapacity(12 * faceCount);
        int len = 0;
        if (faceRenderPriorities == null) {
            for (i = diameter - 1; i >= 0; --i) {
                cnt = distanceFaceCount[i];
                if (cnt <= 0) continue;
                char[] faces = distanceToFaces[i];
                for (int faceIdx = 0; faceIdx < cnt; ++faceIdx) {
                    char face = faces[faceIdx];
                    len += this.pushFace(model, face, vertexBuffer, uvBuffer);
                }
            }
        } else {
            Arrays.fill(numOfPriority, 0);
            Arrays.fill(lt10, 0);
            for (i = diameter - 1; i >= 0; --i) {
                cnt = distanceFaceCount[i];
                if (cnt <= 0) continue;
                char[] faces = distanceToFaces[i];
                for (int faceIdx = 0; faceIdx < cnt; ++faceIdx) {
                    byte pri;
                    int face = faces[faceIdx];
                    byte by = pri = faceRenderPriorities[face];
                    numOfPriority[by] = numOfPriority[by] + 1;
                    SceneUploader.orderedFaces[pri][distIdx] = face;
                    if (pri < 10) {
                        byte by2 = pri;
                        lt10[by2] = lt10[by2] + i;
                        continue;
                    }
                    if (pri == 10) {
                        SceneUploader.eq10[distIdx] = i;
                        continue;
                    }
                    SceneUploader.eq11[distIdx] = i;
                }
            }
            int avg12 = 0;
            if (numOfPriority[1] > 0 || numOfPriority[2] > 0) {
                avg12 = (lt10[1] + lt10[2]) / (numOfPriority[1] + numOfPriority[2]);
            }
            int avg34 = 0;
            if (numOfPriority[3] > 0 || numOfPriority[4] > 0) {
                avg34 = (lt10[3] + lt10[4]) / (numOfPriority[3] + numOfPriority[4]);
            }
            int avg68 = 0;
            if (numOfPriority[6] > 0 || numOfPriority[8] > 0) {
                avg68 = (lt10[8] + lt10[6]) / (numOfPriority[8] + numOfPriority[6]);
            }
            int drawnFaces = 0;
            int numDynFaces = numOfPriority[10];
            int[] dynFaces = orderedFaces[10];
            int[] dynFaceDistances = eq10;
            if (drawnFaces == numDynFaces) {
                drawnFaces = 0;
                numDynFaces = numOfPriority[11];
                dynFaces = orderedFaces[11];
                dynFaceDistances = eq11;
            }
            int currFaceDistance = drawnFaces < numDynFaces ? dynFaceDistances[drawnFaces] : -1000;
            for (int pri = 0; pri < 10; ++pri) {
                int face;
                while (pri == 0 && currFaceDistance > avg12) {
                    face = dynFaces[drawnFaces++];
                    len += this.pushFace(model, face, vertexBuffer, uvBuffer);
                    if (drawnFaces == numDynFaces && dynFaces != orderedFaces[11]) {
                        drawnFaces = 0;
                        numDynFaces = numOfPriority[11];
                        dynFaces = orderedFaces[11];
                        dynFaceDistances = eq11;
                    }
                    if (drawnFaces < numDynFaces) {
                        currFaceDistance = dynFaceDistances[drawnFaces];
                        continue;
                    }
                    currFaceDistance = -1000;
                }
                while (pri == 3 && currFaceDistance > avg34) {
                    face = dynFaces[drawnFaces++];
                    len += this.pushFace(model, face, vertexBuffer, uvBuffer);
                    if (drawnFaces == numDynFaces && dynFaces != orderedFaces[11]) {
                        drawnFaces = 0;
                        numDynFaces = numOfPriority[11];
                        dynFaces = orderedFaces[11];
                        dynFaceDistances = eq11;
                    }
                    if (drawnFaces < numDynFaces) {
                        currFaceDistance = dynFaceDistances[drawnFaces];
                        continue;
                    }
                    currFaceDistance = -1000;
                }
                while (pri == 5 && currFaceDistance > avg68) {
                    face = dynFaces[drawnFaces++];
                    len += this.pushFace(model, face, vertexBuffer, uvBuffer);
                    if (drawnFaces == numDynFaces && dynFaces != orderedFaces[11]) {
                        drawnFaces = 0;
                        numDynFaces = numOfPriority[11];
                        dynFaces = orderedFaces[11];
                        dynFaceDistances = eq11;
                    }
                    if (drawnFaces < numDynFaces) {
                        currFaceDistance = dynFaceDistances[drawnFaces];
                        continue;
                    }
                    currFaceDistance = -1000;
                }
                int priNum = numOfPriority[pri];
                int[] priFaces = orderedFaces[pri];
                for (int faceIdx = 0; faceIdx < priNum; ++faceIdx) {
                    int face2 = priFaces[faceIdx];
                    len += this.pushFace(model, face2, vertexBuffer, uvBuffer);
                }
            }
            while (currFaceDistance != -1000) {
                int face = dynFaces[drawnFaces++];
                len += this.pushFace(model, face, vertexBuffer, uvBuffer);
                if (drawnFaces == numDynFaces && dynFaces != orderedFaces[11]) {
                    drawnFaces = 0;
                    dynFaces = orderedFaces[11];
                    numDynFaces = numOfPriority[11];
                    dynFaceDistances = eq11;
                }
                if (drawnFaces < numDynFaces) {
                    currFaceDistance = dynFaceDistances[drawnFaces];
                    continue;
                }
                currFaceDistance = -1000;
            }
        }
        return len;
    }

    private int pushFace(Model model, int face, GpuIntBuffer vertexBuffer, GpuFloatBuffer uvBuffer) {
        int[] indices1 = model.getFaceIndices1();
        int[] indices2 = model.getFaceIndices2();
        int[] indices3 = model.getFaceIndices3();
        int[] faceColors1 = model.getFaceColors1();
        int[] faceColors2 = model.getFaceColors2();
        int[] faceColors3 = model.getFaceColors3();
        byte overrideAmount = model.getOverrideAmount();
        byte overrideHue = model.getOverrideHue();
        byte overrideSat = model.getOverrideSaturation();
        byte overrideLum = model.getOverrideLuminance();
        short[] faceTextures = model.getFaceTextures();
        float[] faceTextureUVCoordinates = model.getFaceTextureUVCoordinates();
        byte[] faceRenderPriorities = model.getFaceRenderPriorities();
        byte[] transparencies = model.getFaceTransparencies();
        int packAlphaPriority = SceneUploader.packAlphaPriority(faceTextures, transparencies, faceRenderPriorities, face);
        int triangleA = indices1[face];
        int triangleB = indices2[face];
        int triangleC = indices3[face];
        int color1 = faceColors1[face];
        int color2 = faceColors2[face];
        int color3 = faceColors3[face];
        if (color3 == -1) {
            color2 = color3 = color1;
        }
        if ((faceTextures == null || faceTextures[face] == -1) && overrideAmount > 0) {
            color1 = SceneUploader.interpolateHSL(color1, overrideHue, overrideSat, overrideLum, overrideAmount);
            color2 = SceneUploader.interpolateHSL(color2, overrideHue, overrideSat, overrideLum, overrideAmount);
            color3 = SceneUploader.interpolateHSL(color3, overrideHue, overrideSat, overrideLum, overrideAmount);
        }
        vertexBuffer.put(modelLocalX[triangleA], modelLocalY[triangleA], modelLocalZ[triangleA], packAlphaPriority | color1);
        vertexBuffer.put(modelLocalX[triangleB], modelLocalY[triangleB], modelLocalZ[triangleB], packAlphaPriority | color2);
        vertexBuffer.put(modelLocalX[triangleC], modelLocalY[triangleC], modelLocalZ[triangleC], packAlphaPriority | color3);
        SceneUploader.pushUvForFace(faceTextures, faceTextureUVCoordinates, face, uvBuffer);
        return 3;
    }

    private static int packAlphaPriority(short[] faceTextures, byte[] faceTransparencies, byte[] facePriorities, int face) {
        int alpha = 0;
        if (faceTransparencies != null && (faceTextures == null || faceTextures[face] == -1)) {
            alpha = (faceTransparencies[face] & 0xFF) << 24;
        }
        int priority = 0;
        if (facePriorities != null) {
            priority = (facePriorities[face] & 0xFF) << 16;
        }
        return alpha | priority;
    }

    private static void pushUvForFace(short[] faceTextures, float[] uv, int face, GpuFloatBuffer uvBuffer) {
        if (faceTextures != null && faceTextures[face] != -1 && uv != null) {
            int idx = face * 6;
            float texture = (float)faceTextures[face] + 1.0f;
            uvBuffer.put(texture, uv[idx], uv[idx + 1], 0.0f);
            uvBuffer.put(texture, uv[idx + 2], uv[idx + 3], 0.0f);
            uvBuffer.put(texture, uv[idx + 4], uv[idx + 5], 0.0f);
        } else {
            uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
            uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
            uvBuffer.put(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    private static int interpolateHSL(int hsl, byte hue2, byte sat2, byte lum2, byte lerp) {
        int hue = hsl >> 10 & 0x3F;
        int sat = hsl >> 7 & 7;
        int lum = hsl & 0x7F;
        int var9 = lerp & 0xFF;
        if (hue2 != -1) {
            hue += var9 * (hue2 - hue) >> 7;
        }
        if (sat2 != -1) {
            sat += var9 * (sat2 - sat) >> 7;
        }
        if (lum2 != -1) {
            lum += var9 * (lum2 - lum) >> 7;
        }
        return (hue << 10 | sat << 7 | lum) & 0xFFFF;
    }
}

