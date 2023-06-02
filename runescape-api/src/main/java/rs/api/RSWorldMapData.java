/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.WorldMapData
 */
package rs.api;

import net.runelite.api.WorldMapData;
import net.runelite.mapping.Import;

public interface RSWorldMapData
extends WorldMapData {
    @Import(value="surfaceContainsPosition")
    public boolean surfaceContainsPosition(int var1, int var2);
}

