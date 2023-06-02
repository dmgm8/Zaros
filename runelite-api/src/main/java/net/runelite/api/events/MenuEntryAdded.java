/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.MenuEntry;

public class MenuEntryAdded {
    private final MenuEntry menuEntry;

    public String getOption() {
        return this.menuEntry.getOption();
    }

    public String getTarget() {
        return this.menuEntry.getTarget();
    }

    public int getType() {
        return this.menuEntry.getType().getId();
    }

    public int getIdentifier() {
        return this.menuEntry.getIdentifier();
    }

    public int getActionParam0() {
        return this.menuEntry.getParam0();
    }

    public int getActionParam1() {
        return this.menuEntry.getParam1();
    }

    public MenuEntryAdded(MenuEntry menuEntry) {
        this.menuEntry = menuEntry;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MenuEntryAdded)) {
            return false;
        }
        MenuEntryAdded other = (MenuEntryAdded)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getIdentifier() != other.getIdentifier()) {
            return false;
        }
        if (this.getActionParam0() != other.getActionParam0()) {
            return false;
        }
        if (this.getActionParam1() != other.getActionParam1()) {
            return false;
        }
        String this$$getOption = this.getOption();
        String other$$getOption = other.getOption();
        if (this$$getOption == null ? other$$getOption != null : !this$$getOption.equals(other$$getOption)) {
            return false;
        }
        String this$$getTarget = this.getTarget();
        String other$$getTarget = other.getTarget();
        return !(this$$getTarget == null ? other$$getTarget != null : !this$$getTarget.equals(other$$getTarget));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MenuEntryAdded;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getIdentifier();
        result = result * 59 + this.getActionParam0();
        result = result * 59 + this.getActionParam1();
        String $$getOption = this.getOption();
        result = result * 59 + ($$getOption == null ? 43 : $$getOption.hashCode());
        String $$getTarget = this.getTarget();
        result = result * 59 + ($$getTarget == null ? 43 : $$getTarget.hashCode());
        return result;
    }

    public String toString() {
        return "MenuEntryAdded(getOption=" + this.getOption() + ", getTarget=" + this.getTarget() + ", getType=" + this.getType() + ", getIdentifier=" + this.getIdentifier() + ", getActionParam0=" + this.getActionParam0() + ", getActionParam1=" + this.getActionParam1() + ")";
    }

    public MenuEntry getMenuEntry() {
        return this.menuEntry;
    }
}

