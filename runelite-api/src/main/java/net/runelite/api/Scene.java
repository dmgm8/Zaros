/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.GameObject;
import net.runelite.api.Tile;

public interface Scene {
    public Tile[][][] getTiles();

    public int getDrawDistance();

    public void setDrawDistance(int var1);

    public int getMinLevel();

    public void setMinLevel(int var1);

    public void removeGameObject(GameObject var1);

    public void generateHouses();

    public void setRoofRemovalMode(int var1);

    public byte[][][] getUnderlayIds();

    public byte[][][] getOverlayIds();

    public byte[][][] getTileShapes();
}

