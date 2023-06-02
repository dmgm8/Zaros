/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Tile;
import net.runelite.api.TileItem;

public final class ItemQuantityChanged {
    private final TileItem item;
    private final Tile tile;
    private final int oldQuantity;
    private final int newQuantity;

    public ItemQuantityChanged(TileItem item, Tile tile, int oldQuantity, int newQuantity) {
        this.item = item;
        this.tile = tile;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
    }

    public TileItem getItem() {
        return this.item;
    }

    public Tile getTile() {
        return this.tile;
    }

    public int getOldQuantity() {
        return this.oldQuantity;
    }

    public int getNewQuantity() {
        return this.newQuantity;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemQuantityChanged)) {
            return false;
        }
        ItemQuantityChanged other = (ItemQuantityChanged)o;
        if (this.getOldQuantity() != other.getOldQuantity()) {
            return false;
        }
        if (this.getNewQuantity() != other.getNewQuantity()) {
            return false;
        }
        TileItem this$item = this.getItem();
        TileItem other$item = other.getItem();
        if (this$item == null ? other$item != null : !this$item.equals(other$item)) {
            return false;
        }
        Tile this$tile = this.getTile();
        Tile other$tile = other.getTile();
        return !(this$tile == null ? other$tile != null : !this$tile.equals(other$tile));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getOldQuantity();
        result = result * 59 + this.getNewQuantity();
        TileItem $item = this.getItem();
        result = result * 59 + ($item == null ? 43 : $item.hashCode());
        Tile $tile = this.getTile();
        result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
        return result;
    }

    public String toString() {
        return "ItemQuantityChanged(item=" + this.getItem() + ", tile=" + this.getTile() + ", oldQuantity=" + this.getOldQuantity() + ", newQuantity=" + this.getNewQuantity() + ")";
    }
}

