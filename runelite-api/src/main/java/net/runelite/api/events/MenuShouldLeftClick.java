/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public class MenuShouldLeftClick {
    private boolean forceRightClick;

    public boolean isForceRightClick() {
        return this.forceRightClick;
    }

    public void setForceRightClick(boolean forceRightClick) {
        this.forceRightClick = forceRightClick;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MenuShouldLeftClick)) {
            return false;
        }
        MenuShouldLeftClick other = (MenuShouldLeftClick)o;
        if (!other.canEqual(this)) {
            return false;
        }
        return this.isForceRightClick() == other.isForceRightClick();
    }

    protected boolean canEqual(Object other) {
        return other instanceof MenuShouldLeftClick;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isForceRightClick() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "MenuShouldLeftClick(forceRightClick=" + this.isForceRightClick() + ")";
    }
}

