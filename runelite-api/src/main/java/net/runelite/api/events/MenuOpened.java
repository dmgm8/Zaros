/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import java.util.Arrays;
import net.runelite.api.MenuEntry;

public class MenuOpened {
    private MenuEntry[] menuEntries;

    public MenuEntry getFirstEntry() {
        if (this.menuEntries.length > 0) {
            return this.menuEntries[this.menuEntries.length - 1];
        }
        return null;
    }

    public MenuEntry[] getMenuEntries() {
        return this.menuEntries;
    }

    public void setMenuEntries(MenuEntry[] menuEntries) {
        this.menuEntries = menuEntries;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MenuOpened)) {
            return false;
        }
        MenuOpened other = (MenuOpened)o;
        if (!other.canEqual(this)) {
            return false;
        }
        return Arrays.deepEquals(this.getMenuEntries(), other.getMenuEntries());
    }

    protected boolean canEqual(Object other) {
        return other instanceof MenuOpened;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + Arrays.deepHashCode(this.getMenuEntries());
        return result;
    }

    public String toString() {
        return "MenuOpened(menuEntries=" + Arrays.deepToString(this.getMenuEntries()) + ")";
    }
}

