/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Tile
 *  net.runelite.api.TileItem
 */
package rs.api;

import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRenderable;

public interface RSTileItem
extends RSRenderable,
TileItem {
    @Import(value="id")
    public int getId();

    @Import(value="id")
    public void setId(int var1);

    @Import(value="quantity")
    public int getQuantity();

    @Import(value="quantity")
    public void setQuantity(int var1);

    public int getX();

    public void setX(int var1);

    public int getY();

    public void setY(int var1);

    public Tile getTile();
}

