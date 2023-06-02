/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import java.util.Arrays;
import net.runelite.api.World;

public final class WorldListLoad {
    private final World[] worlds;

    public WorldListLoad(World[] worlds) {
        this.worlds = worlds;
    }

    public World[] getWorlds() {
        return this.worlds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WorldListLoad)) {
            return false;
        }
        WorldListLoad other = (WorldListLoad)o;
        return Arrays.deepEquals(this.getWorlds(), other.getWorlds());
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + Arrays.deepHashCode(this.getWorlds());
        return result;
    }

    public String toString() {
        return "WorldListLoad(worlds=" + Arrays.deepToString(this.getWorlds()) + ")";
    }
}

