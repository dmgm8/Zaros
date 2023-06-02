/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.coords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;

public final class WorldPoint {
    private static final int[] REGION_MIRRORS = new int[]{12894, 8755, 12895, 8756, 13150, 9011, 13151, 9012};
    private final int x;
    private final int y;
    private final int plane;

    public WorldPoint dx(int dx) {
        return new WorldPoint(this.x + dx, this.y, this.plane);
    }

    public WorldPoint dy(int dy) {
        return new WorldPoint(this.x, this.y + dy, this.plane);
    }

    public WorldPoint dz(int dz) {
        return new WorldPoint(this.x, this.y, this.plane + dz);
    }

    public static boolean isInScene(Client client, int x, int y) {
        int baseX = client.getBaseX();
        int baseY = client.getBaseY();
        int maxX = baseX + 104;
        int maxY = baseY + 104;
        return x >= baseX && x < maxX && y >= baseY && y < maxY;
    }

    public boolean isInScene(Client client) {
        return client.getPlane() == this.plane && WorldPoint.isInScene(client, this.x, this.y);
    }

    public static WorldPoint fromLocal(Client client, LocalPoint local) {
        return WorldPoint.fromLocal(client, local.getX(), local.getY(), client.getPlane());
    }

    public static WorldPoint fromLocal(Client client, int x, int y, int plane) {
        return new WorldPoint((x >>> 7) + client.getBaseX(), (y >>> 7) + client.getBaseY(), plane);
    }

    public static WorldPoint fromLocalInstance(Client client, LocalPoint localPoint) {
        return WorldPoint.fromLocalInstance(client, localPoint, client.getPlane());
    }

    public static WorldPoint fromLocalInstance(Client client, LocalPoint localPoint, int plane) {
        if (client.isInInstancedRegion()) {
            int sceneX = localPoint.getSceneX();
            int sceneY = localPoint.getSceneY();
            int chunkX = sceneX / 8;
            int chunkY = sceneY / 8;
            int[][][] instanceTemplateChunks = client.getInstanceTemplateChunks();
            int templateChunk = instanceTemplateChunks[plane][chunkX][chunkY];
            int rotation = templateChunk >> 1 & 3;
            int templateChunkY = (templateChunk >> 3 & 0x7FF) * 8;
            int templateChunkX = (templateChunk >> 14 & 0x3FF) * 8;
            int templateChunkPlane = templateChunk >> 24 & 3;
            int x = templateChunkX + (sceneX & 7);
            int y = templateChunkY + (sceneY & 7);
            return WorldPoint.rotate(new WorldPoint(x, y, templateChunkPlane), 4 - rotation);
        }
        return WorldPoint.fromLocal(client, localPoint.getX(), localPoint.getY(), plane);
    }

    public static Collection<WorldPoint> toLocalInstance(Client client, WorldPoint worldPoint) {
        if (!client.isInInstancedRegion()) {
            return Collections.singleton(worldPoint);
        }
        ArrayList<WorldPoint> worldPoints = new ArrayList<WorldPoint>();
        int[][][] instanceTemplateChunks = client.getInstanceTemplateChunks();
        for (int z = 0; z < instanceTemplateChunks.length; ++z) {
            for (int x = 0; x < instanceTemplateChunks[z].length; ++x) {
                for (int y = 0; y < instanceTemplateChunks[z][x].length; ++y) {
                    int chunkData = instanceTemplateChunks[z][x][y];
                    int rotation = chunkData >> 1 & 3;
                    int templateChunkY = (chunkData >> 3 & 0x7FF) * 8;
                    int templateChunkX = (chunkData >> 14 & 0x3FF) * 8;
                    int plane = chunkData >> 24 & 3;
                    if (worldPoint.getX() < templateChunkX || worldPoint.getX() >= templateChunkX + 8 || worldPoint.getY() < templateChunkY || worldPoint.getY() >= templateChunkY + 8 || plane != worldPoint.getPlane()) continue;
                    WorldPoint p = new WorldPoint(client.getBaseX() + x * 8 + (worldPoint.getX() & 7), client.getBaseY() + y * 8 + (worldPoint.getY() & 7), z);
                    p = WorldPoint.rotate(p, rotation);
                    worldPoints.add(p);
                }
            }
        }
        return worldPoints;
    }

    private static WorldPoint rotate(WorldPoint point, int rotation) {
        int chunkX = point.getX() & 0xFFFFFFF8;
        int chunkY = point.getY() & 0xFFFFFFF8;
        int x = point.getX() & 7;
        int y = point.getY() & 7;
        switch (rotation) {
            case 1: {
                return new WorldPoint(chunkX + y, chunkY + (7 - x), point.getPlane());
            }
            case 2: {
                return new WorldPoint(chunkX + (7 - x), chunkY + (7 - y), point.getPlane());
            }
            case 3: {
                return new WorldPoint(chunkX + (7 - y), chunkY + x, point.getPlane());
            }
        }
        return point;
    }

    public int distanceTo(WorldArea other) {
        return new WorldArea(this, 1, 1).distanceTo(other);
    }

    public int distanceTo(WorldPoint other) {
        if (other.plane != this.plane) {
            return Integer.MAX_VALUE;
        }
        return this.distanceTo2D(other);
    }

    public int distanceTo2D(WorldPoint other) {
        return Math.max(Math.abs(this.getX() - other.getX()), Math.abs(this.getY() - other.getY()));
    }

    public static WorldPoint fromScene(Client client, int x, int y, int plane) {
        return new WorldPoint(x + client.getBaseX(), y + client.getBaseY(), plane);
    }

    public int getRegionID() {
        return this.x >> 6 << 8 | this.y >> 6;
    }

    public static WorldPoint fromRegion(int regionId, int regionX, int regionY, int plane) {
        return new WorldPoint((regionId >>> 8 << 6) + regionX, ((regionId & 0xFF) << 6) + regionY, plane);
    }

    public int getRegionX() {
        return WorldPoint.getRegionOffset(this.x);
    }

    public int getRegionY() {
        return WorldPoint.getRegionOffset(this.y);
    }

    private static int getRegionOffset(int position) {
        return position & 0x3F;
    }

    public static WorldPoint getMirrorPoint(WorldPoint worldPoint, boolean toOverworld) {
        int region = worldPoint.getRegionID();
        for (int i = 0; i < REGION_MIRRORS.length; i += 2) {
            int real = REGION_MIRRORS[i];
            int overworld = REGION_MIRRORS[i + 1];
            if (region != (toOverworld ? real : overworld)) continue;
            return WorldPoint.fromRegion(toOverworld ? overworld : real, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane());
        }
        return worldPoint;
    }

    public boolean isInArea(WorldArea ... worldAreas) {
        for (WorldArea area : worldAreas) {
            if (!area.contains(this)) continue;
            return true;
        }
        return false;
    }

    public boolean isInArea2D(WorldArea ... worldAreas) {
        for (WorldArea area : worldAreas) {
            if (!area.contains2D(this)) continue;
            return true;
        }
        return false;
    }

    public WorldPoint(int x, int y, int plane) {
        this.x = x;
        this.y = y;
        this.plane = plane;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getPlane() {
        return this.plane;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorldPoint)) {
            return false;
        }
        WorldPoint other = (WorldPoint)o;
        if (this.getX() != other.getX()) {
            return false;
        }
        if (this.getY() != other.getY()) {
            return false;
        }
        return this.getPlane() == other.getPlane();
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getPlane();
        return result;
    }

    public String toString() {
        return "WorldPoint(x=" + this.getX() + ", y=" + this.getY() + ", plane=" + this.getPlane() + ")";
    }
}

