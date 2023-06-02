/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public class WidgetLoaded {
    private int groupId;

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WidgetLoaded)) {
            return false;
        }
        WidgetLoaded other = (WidgetLoaded)o;
        if (!other.canEqual(this)) {
            return false;
        }
        return this.getGroupId() == other.getGroupId();
    }

    protected boolean canEqual(Object other) {
        return other instanceof WidgetLoaded;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGroupId();
        return result;
    }

    public String toString() {
        return "WidgetLoaded(groupId=" + this.getGroupId() + ")";
    }
}

