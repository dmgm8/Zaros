/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.cluescrolls.clues;

import net.runelite.api.coords.WorldPoint;

public interface LocationClueScroll {
    public WorldPoint getLocation();

    default public WorldPoint[] getLocations() {
        WorldPoint[] arrworldPoint;
        WorldPoint location = this.getLocation();
        if (location == null) {
            arrworldPoint = new WorldPoint[]{};
        } else {
            WorldPoint[] arrworldPoint2 = new WorldPoint[1];
            arrworldPoint = arrworldPoint2;
            arrworldPoint2[0] = location;
        }
        return arrworldPoint;
    }
}

