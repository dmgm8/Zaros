/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.GameState;

public class GameStateChanged {
    private GameState gameState;

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameStateChanged)) {
            return false;
        }
        GameStateChanged other = (GameStateChanged)o;
        if (!other.canEqual(this)) {
            return false;
        }
        GameState this$gameState = this.getGameState();
        GameState other$gameState = other.getGameState();
        return !(this$gameState == null ? other$gameState != null : !((Object)((Object)this$gameState)).equals((Object)other$gameState));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameStateChanged;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        GameState $gameState = this.getGameState();
        result = result * 59 + ($gameState == null ? 43 : ((Object)((Object)$gameState)).hashCode());
        return result;
    }

    public String toString() {
        return "GameStateChanged(gameState=" + (Object)((Object)this.getGameState()) + ")";
    }
}

