/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.GroundObject;
import net.runelite.api.Tile;

public class GroundObjectDespawned {
    private Tile tile;
    private GroundObject groundObject;

    public Tile getTile() {
        return this.tile;
    }

    public GroundObject getGroundObject() {
        return this.groundObject;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setGroundObject(GroundObject groundObject) {
        this.groundObject = groundObject;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GroundObjectDespawned)) {
            return false;
        }
        GroundObjectDespawned other = (GroundObjectDespawned)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Tile this$tile = this.getTile();
        Tile other$tile = other.getTile();
        if (this$tile == null ? other$tile != null : !this$tile.equals(other$tile)) {
            return false;
        }
        GroundObject this$groundObject = this.getGroundObject();
        GroundObject other$groundObject = other.getGroundObject();
        return !(this$groundObject == null ? other$groundObject != null : !this$groundObject.equals(other$groundObject));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GroundObjectDespawned;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Tile $tile = this.getTile();
        result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
        GroundObject $groundObject = this.getGroundObject();
        result = result * 59 + ($groundObject == null ? 43 : $groundObject.hashCode());
        return result;
    }

    public String toString() {
        return "GroundObjectDespawned(tile=" + this.getTile() + ", groundObject=" + this.getGroundObject() + ")";
    }
}

