/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.woodcutting;

import java.time.Instant;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.woodcutting.Tree;

class TreeRespawn {
    private final Tree tree;
    private final int lenX;
    private final int lenY;
    private final WorldPoint worldLocation;
    private final Instant startTime;
    private final int respawnTime;

    boolean isExpired() {
        return Instant.now().isAfter(this.startTime.plusMillis(this.respawnTime));
    }

    public TreeRespawn(Tree tree, int lenX, int lenY, WorldPoint worldLocation, Instant startTime, int respawnTime) {
        this.tree = tree;
        this.lenX = lenX;
        this.lenY = lenY;
        this.worldLocation = worldLocation;
        this.startTime = startTime;
        this.respawnTime = respawnTime;
    }

    public Tree getTree() {
        return this.tree;
    }

    public int getLenX() {
        return this.lenX;
    }

    public int getLenY() {
        return this.lenY;
    }

    public WorldPoint getWorldLocation() {
        return this.worldLocation;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public int getRespawnTime() {
        return this.respawnTime;
    }
}

