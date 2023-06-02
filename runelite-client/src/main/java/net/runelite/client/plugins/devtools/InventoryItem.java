/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Item
 */
package net.runelite.client.plugins.devtools;

import net.runelite.api.Item;

class InventoryItem {
    private final int slot;
    private Item item;
    private final String name;
    private final boolean stackable;

    public int getSlot() {
        return this.slot;
    }

    public Item getItem() {
        return this.item;
    }

    public String getName() {
        return this.name;
    }

    public boolean isStackable() {
        return this.stackable;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InventoryItem)) {
            return false;
        }
        InventoryItem other = (InventoryItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSlot() != other.getSlot()) {
            return false;
        }
        if (this.isStackable() != other.isStackable()) {
            return false;
        }
        Item this$item = this.getItem();
        Item other$item = other.getItem();
        if (this$item == null ? other$item != null : !this$item.equals((Object)other$item)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InventoryItem;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSlot();
        result = result * 59 + (this.isStackable() ? 79 : 97);
        Item $item = this.getItem();
        result = result * 59 + ($item == null ? 43 : $item.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "InventoryItem(slot=" + this.getSlot() + ", item=" + (Object)this.getItem() + ", name=" + this.getName() + ", stackable=" + this.isStackable() + ")";
    }

    public InventoryItem(int slot, Item item, String name, boolean stackable) {
        this.slot = slot;
        this.item = item;
        this.name = name;
        this.stackable = stackable;
    }
}

