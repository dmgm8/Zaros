/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.GameObject;
import net.runelite.api.Tile;

public class GameObjectSpawned {
    private Tile tile;
    private GameObject gameObject;

    public Tile getTile() {
        return this.tile;
    }

    public GameObject getGameObject() {
        return this.gameObject;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameObjectSpawned)) {
            return false;
        }
        GameObjectSpawned other = (GameObjectSpawned)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Tile this$tile = this.getTile();
        Tile other$tile = other.getTile();
        if (this$tile == null ? other$tile != null : !this$tile.equals(other$tile)) {
            return false;
        }
        GameObject this$gameObject = this.getGameObject();
        GameObject other$gameObject = other.getGameObject();
        return !(this$gameObject == null ? other$gameObject != null : !this$gameObject.equals(other$gameObject));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameObjectSpawned;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Tile $tile = this.getTile();
        result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
        GameObject $gameObject = this.getGameObject();
        result = result * 59 + ($gameObject == null ? 43 : $gameObject.hashCode());
        return result;
    }

    public String toString() {
        return "GameObjectSpawned(tile=" + this.getTile() + ", gameObject=" + this.getGameObject() + ")";
    }
}

