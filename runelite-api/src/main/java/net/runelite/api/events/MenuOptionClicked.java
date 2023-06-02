/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api.events;

import javax.annotation.Nullable;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;

public class MenuOptionClicked {
    private final MenuEntry menuEntry;
    private boolean consumed;

    public int getParam0() {
        return this.menuEntry.getParam0();
    }

    public int getParam1() {
        return this.menuEntry.getParam1();
    }

    public String getMenuOption() {
        return this.menuEntry.getOption();
    }

    public String getMenuTarget() {
        return this.menuEntry.getTarget();
    }

    public MenuAction getMenuAction() {
        return this.menuEntry.getType();
    }

    public int getId() {
        return this.menuEntry.getIdentifier();
    }

    public boolean isItemOp() {
        return this.menuEntry.isItemOp();
    }

    public int getItemOp() {
        return this.menuEntry.getItemOp();
    }

    public int getItemId() {
        return this.menuEntry.getItemId();
    }

    @Nullable
    public Widget getWidget() {
        return this.menuEntry.getWidget();
    }

    public void consume() {
        this.consumed = true;
    }

    @Deprecated
    public int getActionParam() {
        return this.menuEntry.getParam0();
    }

    @Deprecated
    public int getWidgetId() {
        return this.menuEntry.getParam1();
    }

    public MenuOptionClicked(MenuEntry menuEntry) {
        this.menuEntry = menuEntry;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MenuOptionClicked)) {
            return false;
        }
        MenuOptionClicked other = (MenuOptionClicked)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getParam0() != other.getParam0()) {
            return false;
        }
        if (this.getParam1() != other.getParam1()) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        String this$$getMenuOption = this.getMenuOption();
        String other$$getMenuOption = other.getMenuOption();
        if (this$$getMenuOption == null ? other$$getMenuOption != null : !this$$getMenuOption.equals(other$$getMenuOption)) {
            return false;
        }
        String this$$getMenuTarget = this.getMenuTarget();
        String other$$getMenuTarget = other.getMenuTarget();
        if (this$$getMenuTarget == null ? other$$getMenuTarget != null : !this$$getMenuTarget.equals(other$$getMenuTarget)) {
            return false;
        }
        MenuAction this$$getMenuAction = this.getMenuAction();
        MenuAction other$$getMenuAction = other.getMenuAction();
        return !(this$$getMenuAction == null ? other$$getMenuAction != null : !((Object)((Object)this$$getMenuAction)).equals((Object)other$$getMenuAction));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MenuOptionClicked;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getParam0();
        result = result * 59 + this.getParam1();
        result = result * 59 + this.getId();
        String $$getMenuOption = this.getMenuOption();
        result = result * 59 + ($$getMenuOption == null ? 43 : $$getMenuOption.hashCode());
        String $$getMenuTarget = this.getMenuTarget();
        result = result * 59 + ($$getMenuTarget == null ? 43 : $$getMenuTarget.hashCode());
        MenuAction $$getMenuAction = this.getMenuAction();
        result = result * 59 + ($$getMenuAction == null ? 43 : ((Object)((Object)$$getMenuAction)).hashCode());
        return result;
    }

    public String toString() {
        return "MenuOptionClicked(getParam0=" + this.getParam0() + ", getParam1=" + this.getParam1() + ", getMenuOption=" + this.getMenuOption() + ", getMenuTarget=" + this.getMenuTarget() + ", getMenuAction=" + (Object)((Object)this.getMenuAction()) + ", getId=" + this.getId() + ")";
    }

    public MenuEntry getMenuEntry() {
        return this.menuEntry;
    }

    public boolean isConsumed() {
        return this.consumed;
    }
}

