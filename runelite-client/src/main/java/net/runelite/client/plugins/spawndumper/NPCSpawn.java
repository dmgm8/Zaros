/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.spawndumper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.runelite.api.coords.WorldPoint;

public class NPCSpawn {
    private final int npc;
    private final int index;
    private final Set<WorldPoint> points = new HashSet<WorldPoint>();
    private int orientation;

    public static void main(String[] args) throws FileNotFoundException {
        Map names = (Map)new Gson().fromJson((Reader)new InputStreamReader(new FileInputStream("npc_names.json")), new TypeToken<Map<Integer, String>>(){}.getType());
        List spawns = (List)new Gson().fromJson((Reader)new InputStreamReader(new FileInputStream("spawns.json")), new TypeToken<List<NPCSpawn>>(){}.getType());
        spawns.sort(Comparator.comparingInt(NPCSpawn::getNpc));
        System.out.println("[");
        for (NPCSpawn spawn : spawns) {
            int deltaX = spawn.getMaxX() - spawn.getMinX();
            int deltaY = spawn.getMaxY() - spawn.getMinY();
            int walkRadius = Math.max(deltaX, deltaY);
            int spawnX = spawn.getMinX() + deltaX / 2;
            int spawnY = spawn.getMinY() + deltaY / 2;
            StringBuilder builder = new StringBuilder();
            builder.append("  { ");
            builder.append("\"id\": ").append(spawn.getNpc());
            builder.append(", \"x\": ").append(spawnX);
            builder.append(", \"y\": ").append(spawnY);
            builder.append(", \"z\": ").append(spawn.getMinZ());
            if (walkRadius != 0) {
                builder.append(", \"walkRange\": ").append(walkRadius);
            }
            if (spawn.getOrientation() != -1) {
                builder.append(", \"direction\": ").append('\"').append(NPCSpawn.orientationToString(spawn.getOrientation())).append('\"');
            }
            builder.append(" },");
            builder.append(" // ").append((String)names.get(spawn.getNpc()));
            System.out.println(builder);
        }
        System.out.println("]");
    }

    private static String orientationToString(int orientation) {
        switch (orientation) {
            case 768: {
                return "NW";
            }
            case 1024: {
                return "N";
            }
            case 1280: {
                return "NE";
            }
            case 512: {
                return "W";
            }
            case 1536: {
                return "E";
            }
            case 256: {
                return "SW";
            }
            case 0: {
                return "S";
            }
            case 1792: {
                return "SE";
            }
        }
        return "S";
    }

    public int getMinX() {
        int minX = Integer.MAX_VALUE;
        for (WorldPoint point : this.points) {
            minX = Math.min(minX, point.getX());
        }
        return minX;
    }

    public int getMaxX() {
        int maxX = Integer.MIN_VALUE;
        for (WorldPoint point : this.points) {
            maxX = Math.max(maxX, point.getX());
        }
        return maxX;
    }

    public int getMinY() {
        int minY = Integer.MAX_VALUE;
        for (WorldPoint point : this.points) {
            minY = Math.min(minY, point.getY());
        }
        return minY;
    }

    public int getMaxY() {
        int maxY = Integer.MIN_VALUE;
        for (WorldPoint point : this.points) {
            maxY = Math.max(maxY, point.getY());
        }
        return maxY;
    }

    public int getMinZ() {
        int minZ = Integer.MAX_VALUE;
        for (WorldPoint point : this.points) {
            minZ = Math.min(minZ, point.getPlane());
        }
        return minZ;
    }

    public int getMaxZ() {
        int maxZ = Integer.MIN_VALUE;
        for (WorldPoint point : this.points) {
            maxZ = Math.max(maxZ, point.getPlane());
        }
        return maxZ;
    }

    public NPCSpawn(int npc, int index) {
        this.npc = npc;
        this.index = index;
    }

    public int getNpc() {
        return this.npc;
    }

    public int getIndex() {
        return this.index;
    }

    public Set<WorldPoint> getPoints() {
        return this.points;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NPCSpawn)) {
            return false;
        }
        NPCSpawn other = (NPCSpawn)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getNpc() != other.getNpc()) {
            return false;
        }
        if (this.getIndex() != other.getIndex()) {
            return false;
        }
        if (this.getOrientation() != other.getOrientation()) {
            return false;
        }
        Set<WorldPoint> this$points = this.getPoints();
        Set<WorldPoint> other$points = other.getPoints();
        return !(this$points == null ? other$points != null : !((Object)this$points).equals(other$points));
    }

    protected boolean canEqual(Object other) {
        return other instanceof NPCSpawn;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNpc();
        result = result * 59 + this.getIndex();
        result = result * 59 + this.getOrientation();
        Set<WorldPoint> $points = this.getPoints();
        result = result * 59 + ($points == null ? 43 : ((Object)$points).hashCode());
        return result;
    }

    public String toString() {
        return "NPCSpawn(npc=" + this.getNpc() + ", index=" + this.getIndex() + ", points=" + this.getPoints() + ", orientation=" + this.getOrientation() + ")";
    }
}

