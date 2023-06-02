/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.xptracker;

import java.util.Arrays;

class XpAction {
    private int actions = 0;
    private int actionsSinceReset = 0;
    private boolean actionsHistoryInitialized = false;
    private int[] actionExps = new int[10];
    private int actionExpIndex = 0;

    public int getActions() {
        return this.actions;
    }

    public int getActionsSinceReset() {
        return this.actionsSinceReset;
    }

    public boolean isActionsHistoryInitialized() {
        return this.actionsHistoryInitialized;
    }

    public int[] getActionExps() {
        return this.actionExps;
    }

    public int getActionExpIndex() {
        return this.actionExpIndex;
    }

    public void setActions(int actions) {
        this.actions = actions;
    }

    public void setActionsSinceReset(int actionsSinceReset) {
        this.actionsSinceReset = actionsSinceReset;
    }

    public void setActionsHistoryInitialized(boolean actionsHistoryInitialized) {
        this.actionsHistoryInitialized = actionsHistoryInitialized;
    }

    public void setActionExps(int[] actionExps) {
        this.actionExps = actionExps;
    }

    public void setActionExpIndex(int actionExpIndex) {
        this.actionExpIndex = actionExpIndex;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof XpAction)) {
            return false;
        }
        XpAction other = (XpAction)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getActions() != other.getActions()) {
            return false;
        }
        if (this.getActionsSinceReset() != other.getActionsSinceReset()) {
            return false;
        }
        if (this.isActionsHistoryInitialized() != other.isActionsHistoryInitialized()) {
            return false;
        }
        if (this.getActionExpIndex() != other.getActionExpIndex()) {
            return false;
        }
        return Arrays.equals(this.getActionExps(), other.getActionExps());
    }

    protected boolean canEqual(Object other) {
        return other instanceof XpAction;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getActions();
        result = result * 59 + this.getActionsSinceReset();
        result = result * 59 + (this.isActionsHistoryInitialized() ? 79 : 97);
        result = result * 59 + this.getActionExpIndex();
        result = result * 59 + Arrays.hashCode(this.getActionExps());
        return result;
    }

    public String toString() {
        return "XpAction(actions=" + this.getActions() + ", actionsSinceReset=" + this.getActionsSinceReset() + ", actionsHistoryInitialized=" + this.isActionsHistoryInitialized() + ", actionExps=" + Arrays.toString(this.getActionExps()) + ", actionExpIndex=" + this.getActionExpIndex() + ")";
    }
}

