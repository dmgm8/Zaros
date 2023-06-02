/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Tile;
import net.runelite.api.WallObject;

public class WallObjectDespawned {
    private Tile tile;
    private WallObject wallObject;

    public Tile getTile() {
        return this.tile;
    }

    public WallObject getWallObject() {
        return this.wallObject;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setWallObject(WallObject wallObject) {
        this.wallObject = wallObject;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WallObjectDespawned)) {
            return false;
        }
        WallObjectDespawned other = (WallObjectDespawned)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Tile this$tile = this.getTile();
        Tile other$tile = other.getTile();
        if (this$tile == null ? other$tile != null : !this$tile.equals(other$tile)) {
            return false;
        }
        WallObject this$wallObject = this.getWallObject();
        WallObject other$wallObject = other.getWallObject();
        return !(this$wallObject == null ? other$wallObject != null : !this$wallObject.equals(other$wallObject));
    }

    protected boolean canEqual(Object other) {
        return other instanceof WallObjectDespawned;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Tile $tile = this.getTile();
        result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
        WallObject $wallObject = this.getWallObject();
        result = result * 59 + ($wallObject == null ? 43 : $wallObject.hashCode());
        return result;
    }

    public String toString() {
        return "WallObjectDespawned(tile=" + this.getTile() + ", wallObject=" + this.getWallObject() + ")";
    }
}

