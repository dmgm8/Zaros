/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Nameable;

public final class RemovedFriend {
    private final Nameable nameable;

    public RemovedFriend(Nameable nameable) {
        this.nameable = nameable;
    }

    public Nameable getNameable() {
        return this.nameable;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RemovedFriend)) {
            return false;
        }
        RemovedFriend other = (RemovedFriend)o;
        Nameable this$nameable = this.getNameable();
        Nameable other$nameable = other.getNameable();
        return !(this$nameable == null ? other$nameable != null : !this$nameable.equals(other$nameable));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Nameable $nameable = this.getNameable();
        result = result * 59 + ($nameable == null ? 43 : $nameable.hashCode());
        return result;
    }

    public String toString() {
        return "RemovedFriend(nameable=" + this.getNameable() + ")";
    }
}

