/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.WorldMapManager
 */
package rs.api;

import net.runelite.api.WorldMapManager;
import net.runelite.mapping.Import;

public interface RSWorldMapManager
extends WorldMapManager {
    @Import(value="loaded")
    public boolean isLoaded();

    @Import(value="mapSurfaceBaseOffsetX")
    public int getSurfaceOffsetX();

    @Import(value="mapSurfaceBaseOffsetY")
    public int getSurfaceOffsetY();
}

