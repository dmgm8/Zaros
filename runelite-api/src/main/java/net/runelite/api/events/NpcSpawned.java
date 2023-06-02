/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Actor;
import net.runelite.api.NPC;

public final class NpcSpawned {
    private final NPC npc;

    public Actor getActor() {
        return this.npc;
    }

    public NpcSpawned(NPC npc) {
        this.npc = npc;
    }

    public NPC getNpc() {
        return this.npc;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NpcSpawned)) {
            return false;
        }
        NpcSpawned other = (NpcSpawned)o;
        NPC this$npc = this.getNpc();
        NPC other$npc = other.getNpc();
        return !(this$npc == null ? other$npc != null : !this$npc.equals(other$npc));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        NPC $npc = this.getNpc();
        result = result * 59 + ($npc == null ? 43 : $npc.hashCode());
        return result;
    }

    public String toString() {
        return "NpcSpawned(npc=" + this.getNpc() + ")";
    }
}

