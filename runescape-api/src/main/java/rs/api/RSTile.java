/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.DecorativeObject
 *  net.runelite.api.GameObject
 *  net.runelite.api.GroundObject
 *  net.runelite.api.ItemLayer
 *  net.runelite.api.SceneTileModel
 *  net.runelite.api.SceneTilePaint
 *  net.runelite.api.Tile
 *  net.runelite.api.WallObject
 */
package rs.api;

import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.ItemLayer;
import net.runelite.api.SceneTileModel;
import net.runelite.api.SceneTilePaint;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.mapping.Import;

public interface RSTile
extends Tile {
    @Import(value="objects")
    public GameObject[] getGameObjects();

    @Import(value="itemLayer")
    public ItemLayer getItemLayer();

    @Import(value="decorativeObject")
    public DecorativeObject getDecorativeObject();

    @Import(value="groundObject")
    public GroundObject getGroundObject();

    @Import(value="groundObject")
    public void setGroundObject(GroundObject var1);

    @Import(value="wallObject")
    public WallObject getWallObject();

    @Import(value="paint")
    public SceneTilePaint getSceneTilePaint();

    @Import(value="overlay")
    public SceneTileModel getSceneTileModel();

    @Import(value="x")
    public int getX();

    @Import(value="y")
    public int getY();

    @Import(value="plane")
    public int getPlane();

    @Import(value="renderLevel")
    public int getRenderLevel();

    @Import(value="physicalLevel")
    public int getPhysicalLevel();

    @Import(value="flags")
    public int getFlags();

    @Import(value="bridge")
    public RSTile getBridge();

    @Import(value="draw")
    public boolean isDraw();

    @Import(value="draw")
    public void setDraw(boolean var1);

    @Import(value="visible")
    public boolean isVisible();

    @Import(value="visible")
    public void setVisible(boolean var1);

    @Import(value="drawEntities")
    public void setDrawEntities(boolean var1);

    @Import(value="wallCullDirection")
    public void setWallCullDirection(int var1);
}

