/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public class DraggingWidgetChanged {
    private boolean draggingWidget;

    public boolean isDraggingWidget() {
        return this.draggingWidget;
    }

    public void setDraggingWidget(boolean draggingWidget) {
        this.draggingWidget = draggingWidget;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DraggingWidgetChanged)) {
            return false;
        }
        DraggingWidgetChanged other = (DraggingWidgetChanged)o;
        if (!other.canEqual(this)) {
            return false;
        }
        return this.isDraggingWidget() == other.isDraggingWidget();
    }

    protected boolean canEqual(Object other) {
        return other instanceof DraggingWidgetChanged;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isDraggingWidget() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "DraggingWidgetChanged(draggingWidget=" + this.isDraggingWidget() + ")";
    }
}

