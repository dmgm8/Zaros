/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.GameObject
 *  net.runelite.api.Scene
 *  net.runelite.api.Tile
 */
package rs.api;

import net.runelite.api.GameObject;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSGameObject;
import net.runelite.rs.api.RSTile;

public interface RSScene
extends Scene {
    @Import(value="objects")
    public RSGameObject[] getObjects();

    @Import(value="tiles")
    public RSTile[][][] getTiles();

    @Import(value="draw")
    public void draw(Tile var1, boolean var2);

    @Import(value="tileHeights")
    public int[][][] getTileHeights();

    @Import(value="drawTile")
    public void drawTile(int[] var1, int var2, int var3, int var4, int var5, int var6);

    @Import(value="updateOccluders")
    public void updateOccluders();

    @Import(value="maxX")
    public int getMaxX();

    @Import(value="maxY")
    public int getMaxY();

    @Import(value="maxZ")
    public int getMaxZ();

    @Import(value="minLevel")
    public int getMinLevel();

    @Import(value="minLevel")
    public void setMinLevel(int var1);

    @Import(value="removeGameObject")
    public void removeGameObject(GameObject var1);

    public void setOverlayIds(byte[][][] var1);

    public void setUnderlayIds(byte[][][] var1);

    public void setTileShapes(byte[][][] var1);
}

