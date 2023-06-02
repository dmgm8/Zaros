/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public class ResizeableChanged {
    private boolean isResized;

    public boolean isResized() {
        return this.isResized;
    }

    public void setResized(boolean isResized) {
        this.isResized = isResized;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResizeableChanged)) {
            return false;
        }
        ResizeableChanged other = (ResizeableChanged)o;
        if (!other.canEqual(this)) {
            return false;
        }
        return this.isResized() == other.isResized();
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResizeableChanged;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isResized() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "ResizeableChanged(isResized=" + this.isResized() + ")";
    }
}

