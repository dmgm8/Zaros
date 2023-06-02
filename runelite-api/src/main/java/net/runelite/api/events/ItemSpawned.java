/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Tile;
import net.runelite.api.TileItem;

public final class ItemSpawned {
    private final Tile tile;
    private final TileItem item;

    public ItemSpawned(Tile tile, TileItem item) {
        this.tile = tile;
        this.item = item;
    }

    public Tile getTile() {
        return this.tile;
    }

    public TileItem getItem() {
        return this.item;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemSpawned)) {
            return false;
        }
        ItemSpawned other = (ItemSpawned)o;
        Tile this$tile = this.getTile();
        Tile other$tile = other.getTile();
        if (this$tile == null ? other$tile != null : !this$tile.equals(other$tile)) {
            return false;
        }
        TileItem this$item = this.getItem();
        TileItem other$item = other.getItem();
        return !(this$item == null ? other$item != null : !this$item.equals(other$item));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Tile $tile = this.getTile();
        result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
        TileItem $item = this.getItem();
        result = result * 59 + ($item == null ? 43 : $item.hashCode());
        return result;
    }

    public String toString() {
        return "ItemSpawned(tile=" + this.getTile() + ", item=" + this.getItem() + ")";
    }
}

