/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.RenderOverview
 *  net.runelite.api.WorldMapData
 */
package rs.api;

import net.runelite.api.RenderOverview;
import net.runelite.api.WorldMapData;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSWorldMapData;
import net.runelite.rs.api.RSWorldMapManager;

public interface RSRenderOverview
extends RenderOverview {
    @Import(value="worldMapX")
    public int getWorldMapX();

    @Import(value="worldMapY")
    public int getWorldMapY();

    @Import(value="worldMapZoom")
    public float getWorldMapZoom();

    @Import(value="worldMapTargetX")
    public int getWorldMapTargetX();

    @Import(value="worldMapTargetY")
    public int getWorldMapTargetY();

    @Import(value="worldMapDisplayWidth")
    public int getWorldMapDisplayWidth();

    @Import(value="worldMapDisplayHeight")
    public int getWorldMapDisplayHeight();

    @Import(value="worldMapDisplayX")
    public int getWorldMapDisplayX();

    @Import(value="worldMapDisplayY")
    public int getWorldMapDisplayY();

    @Import(value="setWorldMapPosition")
    public void setWorldMapPosition(int var1, int var2, boolean var3);

    @Import(value="jumpToDisplayCoord")
    public void setWorldMapPositionTarget(int var1, int var2);

    @Import(value="worldMapManager")
    public RSWorldMapManager getWorldMapManager();

    @Import(value="initializeWorldMap")
    public void initializeWorldMap(WorldMapData var1);

    @Import(value="worldMapData")
    public RSWorldMapData getWorldMapData();
}

