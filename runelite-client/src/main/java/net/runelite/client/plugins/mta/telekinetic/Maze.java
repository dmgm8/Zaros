/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.coords.LocalPoint
 */
package net.runelite.client.plugins.mta.telekinetic;

import net.runelite.api.coords.LocalPoint;

public enum Maze {
    MAZE_1(100, new LocalPoint(6848, 3904)),
    MAZE_2(124, new LocalPoint(4928, 6848)),
    MAZE_3(129, new LocalPoint(7104, 5312)),
    MAZE_4(53, new LocalPoint(6208, 4928)),
    MAZE_5(108, new LocalPoint(5056, 5184)),
    MAZE_6(121, new LocalPoint(3648, 5440)),
    MAZE_7(71, new LocalPoint(6080, 5696)),
    MAZE_8(98, new LocalPoint(5952, 7360)),
    MAZE_9(87, new LocalPoint(5184, 6208)),
    MAZE_10(91, new LocalPoint(5440, 9024));

    private final int walls;
    private final LocalPoint start;

    private Maze(int walls, LocalPoint start) {
        this.walls = walls;
        this.start = start;
    }

    public static Maze fromWalls(int walls) {
        for (Maze maze : Maze.values()) {
            if (maze.getWalls() != walls) continue;
            return maze;
        }
        return null;
    }

    public int getWalls() {
        return this.walls;
    }

    public LocalPoint getStart() {
        return this.start;
    }
}

