/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Nameable;

public final class NameableNameChanged {
    private final Nameable nameable;

    public NameableNameChanged(Nameable nameable) {
        this.nameable = nameable;
    }

    public Nameable getNameable() {
        return this.nameable;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NameableNameChanged)) {
            return false;
        }
        NameableNameChanged other = (NameableNameChanged)o;
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
        return "NameableNameChanged(nameable=" + this.getNameable() + ")";
    }
}

