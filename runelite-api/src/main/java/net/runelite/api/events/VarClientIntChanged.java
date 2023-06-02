/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public final class VarClientIntChanged {
    private final int index;

    public VarClientIntChanged(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VarClientIntChanged)) {
            return false;
        }
        VarClientIntChanged other = (VarClientIntChanged)o;
        return this.getIndex() == other.getIndex();
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIndex();
        return result;
    }

    public String toString() {
        return "VarClientIntChanged(index=" + this.getIndex() + ")";
    }
}

