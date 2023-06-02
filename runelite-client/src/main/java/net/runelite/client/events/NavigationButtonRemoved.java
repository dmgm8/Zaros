/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.events;

import net.runelite.client.ui.NavigationButton;

public final class NavigationButtonRemoved {
    private final NavigationButton button;

    public NavigationButtonRemoved(NavigationButton button) {
        this.button = button;
    }

    public NavigationButton getButton() {
        return this.button;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NavigationButtonRemoved)) {
            return false;
        }
        NavigationButtonRemoved other = (NavigationButtonRemoved)o;
        NavigationButton this$button = this.getButton();
        NavigationButton other$button = other.getButton();
        return !(this$button == null ? other$button != null : !((Object)this$button).equals(other$button));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        NavigationButton $button = this.getButton();
        result = result * 59 + ($button == null ? 43 : ((Object)$button).hashCode());
        return result;
    }

    public String toString() {
        return "NavigationButtonRemoved(button=" + this.getButton() + ")";
    }
}

