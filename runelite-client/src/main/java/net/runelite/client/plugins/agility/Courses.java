/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.agility;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.runelite.api.coords.WorldPoint;

enum Courses {
    GNOME(86.5, 46, 9781, new WorldPoint[0]),
    SHAYZIEN_BASIC(133.2, 92, 6200, new WorldPoint[0]),
    DRAYNOR(120.0, 79, 12338, new WorldPoint[0]),
    AL_KHARID(180.0, 0, 13105, new WorldPoint(3299, 3194, 0)),
    PYRAMID(722.0, 0, 13356, new WorldPoint(3364, 2830, 0)),
    VARROCK(238.0, 125, 12853, new WorldPoint[0]),
    PENGUIN(540.0, 65, 10559, new WorldPoint[0]),
    BARBARIAN(139.5, 60, 10039, new WorldPoint[0]),
    CANIFIS(240.0, 175, 13878, new WorldPoint[0]),
    APE_ATOLL(580.0, 300, 11050, new WorldPoint[0]),
    SHAYZIEN_ADVANCED(474.3, 382, 5944, new WorldPoint[0]),
    FALADOR(440.0, 180, 12084, new WorldPoint[0]),
    WILDERNESS(571.0, 499, 11837, new WorldPoint[0]),
    WEREWOLF(730.0, 380, 14234, new WorldPoint[0]),
    SEERS(570.0, 435, 10806, new WorldPoint[0]),
    POLLNIVNEACH(890.0, 540, 13358, new WorldPoint[0]),
    RELLEKA(780.0, 475, 10553, new WorldPoint[0]),
    PRIFDDINAS(1337.0, 1037, 12895, new WorldPoint[0]),
    ARDOUGNE(793.0, 529, 10547, new WorldPoint[0]);

    private static final Map<Integer, Courses> coursesByRegion;
    private final double totalXp;
    private final int lastObstacleXp;
    private final int regionId;
    private final WorldPoint[] courseEndWorldPoints;

    private Courses(double totalXp, int lastObstacleXp, int regionId, WorldPoint ... courseEndWorldPoints) {
        this.totalXp = totalXp;
        this.lastObstacleXp = lastObstacleXp;
        this.regionId = regionId;
        this.courseEndWorldPoints = courseEndWorldPoints;
    }

    static Courses getCourse(int regionId) {
        return coursesByRegion.get(regionId);
    }

    public double getTotalXp() {
        return this.totalXp;
    }

    public int getLastObstacleXp() {
        return this.lastObstacleXp;
    }

    public int getRegionId() {
        return this.regionId;
    }

    public WorldPoint[] getCourseEndWorldPoints() {
        return this.courseEndWorldPoints;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Courses course : Courses.values()) {
            builder.put((Object)course.regionId, (Object)course);
        }
        coursesByRegion = builder.build();
    }
}

