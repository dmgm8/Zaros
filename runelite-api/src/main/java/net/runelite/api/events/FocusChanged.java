/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public class FocusChanged {
    private boolean focused;

    public boolean isFocused() {
        return this.focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FocusChanged)) {
            return false;
        }
        FocusChanged other = (FocusChanged)o;
        if (!other.canEqual(this)) {
            return false;
        }
        return this.isFocused() == other.isFocused();
    }

    protected boolean canEqual(Object other) {
        return other instanceof FocusChanged;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isFocused() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "FocusChanged(focused=" + this.isFocused() + ")";
    }
}

