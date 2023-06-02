/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.ItemContainer;

public final class ItemContainerChanged {
    private final int containerId;
    private final ItemContainer itemContainer;

    public ItemContainerChanged(int containerId, ItemContainer itemContainer) {
        this.containerId = containerId;
        this.itemContainer = itemContainer;
    }

    public int getContainerId() {
        return this.containerId;
    }

    public ItemContainer getItemContainer() {
        return this.itemContainer;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemContainerChanged)) {
            return false;
        }
        ItemContainerChanged other = (ItemContainerChanged)o;
        if (this.getContainerId() != other.getContainerId()) {
            return false;
        }
        ItemContainer this$itemContainer = this.getItemContainer();
        ItemContainer other$itemContainer = other.getItemContainer();
        return !(this$itemContainer == null ? other$itemContainer != null : !this$itemContainer.equals(other$itemContainer));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getContainerId();
        ItemContainer $itemContainer = this.getItemContainer();
        result = result * 59 + ($itemContainer == null ? 43 : $itemContainer.hashCode());
        return result;
    }

    public String toString() {
        return "ItemContainerChanged(containerId=" + this.getContainerId() + ", itemContainer=" + this.getItemContainer() + ")";
    }
}

