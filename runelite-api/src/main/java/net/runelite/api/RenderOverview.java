/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Point;
import net.runelite.api.WorldMapData;
import net.runelite.api.WorldMapManager;
import net.runelite.api.coords.WorldPoint;

public interface RenderOverview {
    public Point getWorldMapPosition();

    public float getWorldMapZoom();

    public void setWorldMapPositionTarget(WorldPoint var1);

    public WorldMapManager getWorldMapManager();

    public void initializeWorldMap(WorldMapData var1);

    public WorldMapData getWorldMapData();
}

