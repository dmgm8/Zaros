/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.MenuAction
 */
package net.runelite.client.ui.overlay;

import net.runelite.api.MenuAction;

public final class OverlayMenuEntry {
    private final MenuAction menuAction;
    private final String option;
    private final String target;

    public OverlayMenuEntry(MenuAction menuAction, String option, String target) {
        this.menuAction = menuAction;
        this.option = option;
        this.target = target;
    }

    public MenuAction getMenuAction() {
        return this.menuAction;
    }

    public String getOption() {
        return this.option;
    }

    public String getTarget() {
        return this.target;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OverlayMenuEntry)) {
            return false;
        }
        OverlayMenuEntry other = (OverlayMenuEntry)o;
        MenuAction this$menuAction = this.getMenuAction();
        MenuAction other$menuAction = other.getMenuAction();
        if (this$menuAction == null ? other$menuAction != null : !this$menuAction.equals((Object)other$menuAction)) {
            return false;
        }
        String this$option = this.getOption();
        String other$option = other.getOption();
        if (this$option == null ? other$option != null : !this$option.equals(other$option)) {
            return false;
        }
        String this$target = this.getTarget();
        String other$target = other.getTarget();
        return !(this$target == null ? other$target != null : !this$target.equals(other$target));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        MenuAction $menuAction = this.getMenuAction();
        result = result * 59 + ($menuAction == null ? 43 : $menuAction.hashCode());
        String $option = this.getOption();
        result = result * 59 + ($option == null ? 43 : $option.hashCode());
        String $target = this.getTarget();
        result = result * 59 + ($target == null ? 43 : $target.hashCode());
        return result;
    }

    public String toString() {
        return "OverlayMenuEntry(menuAction=" + (Object)this.getMenuAction() + ", option=" + this.getOption() + ", target=" + this.getTarget() + ")";
    }
}

