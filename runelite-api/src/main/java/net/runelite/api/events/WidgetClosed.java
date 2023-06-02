/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public final class WidgetClosed {
    private final int groupId;
    private final int modalMode;
    private final boolean unload;

    public WidgetClosed(int groupId, int modalMode, boolean unload) {
        this.groupId = groupId;
        this.modalMode = modalMode;
        this.unload = unload;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public int getModalMode() {
        return this.modalMode;
    }

    public boolean isUnload() {
        return this.unload;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WidgetClosed)) {
            return false;
        }
        WidgetClosed other = (WidgetClosed)o;
        if (this.getGroupId() != other.getGroupId()) {
            return false;
        }
        if (this.getModalMode() != other.getModalMode()) {
            return false;
        }
        return this.isUnload() == other.isUnload();
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGroupId();
        result = result * 59 + this.getModalMode();
        result = result * 59 + (this.isUnload() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "WidgetClosed(groupId=" + this.getGroupId() + ", modalMode=" + this.getModalMode() + ", unload=" + this.isUnload() + ")";
    }
}

