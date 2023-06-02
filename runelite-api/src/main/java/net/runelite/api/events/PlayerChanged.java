/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Player;

public final class PlayerChanged {
    private final Player player;

    public PlayerChanged(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerChanged)) {
            return false;
        }
        PlayerChanged other = (PlayerChanged)o;
        Player this$player = this.getPlayer();
        Player other$player = other.getPlayer();
        return !(this$player == null ? other$player != null : !this$player.equals(other$player));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Player $player = this.getPlayer();
        result = result * 59 + ($player == null ? 43 : $player.hashCode());
        return result;
    }

    public String toString() {
        return "PlayerChanged(player=" + this.getPlayer() + ")";
    }
}

