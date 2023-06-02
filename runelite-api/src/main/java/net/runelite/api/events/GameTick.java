/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public class GameTick {
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameTick)) {
            return false;
        }
        GameTick other = (GameTick)o;
        return other.canEqual(this);
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameTick;
    }

    public int hashCode() {
        boolean result = true;
        return 1;
    }

    public String toString() {
        return "GameTick()";
    }
}

