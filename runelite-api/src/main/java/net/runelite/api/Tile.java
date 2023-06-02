/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.util.List;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.ItemLayer;
import net.runelite.api.Point;
import net.runelite.api.SceneTileModel;
import net.runelite.api.SceneTilePaint;
import net.runelite.api.TileItem;
import net.runelite.api.WallObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public interface Tile {
    public DecorativeObject getDecorativeObject();

    public GameObject[] getGameObjects();

    public ItemLayer getItemLayer();

    public GroundObject getGroundObject();

    public void setGroundObject(GroundObject var1);

    public WallObject getWallObject();

    public SceneTilePaint getSceneTilePaint();

    public SceneTileModel getSceneTileModel();

    public WorldPoint getWorldLocation();

    public Point getSceneLocation();

    public LocalPoint getLocalLocation();

    public int getPlane();

    public int getRenderLevel();

    public boolean hasLineOfSightTo(Tile var1);

    public List<TileItem> getGroundItems();

    public Tile getBridge();
}

