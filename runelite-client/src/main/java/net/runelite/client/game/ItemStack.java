/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.coords.LocalPoint
 */
package net.runelite.client.game;

import net.runelite.api.coords.LocalPoint;

public final class ItemStack {
    private final int id;
    private final int quantity;
    private final LocalPoint location;

    public ItemStack(int id, int quantity, LocalPoint location) {
        this.id = id;
        this.quantity = quantity;
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public LocalPoint getLocation() {
        return this.location;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStack)) {
            return false;
        }
        ItemStack other = (ItemStack)o;
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getQuantity() != other.getQuantity()) {
            return false;
        }
        LocalPoint this$location = this.getLocation();
        LocalPoint other$location = other.getLocation();
        return !(this$location == null ? other$location != null : !this$location.equals((Object)other$location));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getQuantity();
        LocalPoint $location = this.getLocation();
        result = result * 59 + ($location == null ? 43 : $location.hashCode());
        return result;
    }

    public String toString() {
        return "ItemStack(id=" + this.getId() + ", quantity=" + this.getQuantity() + ", location=" + (Object)this.getLocation() + ")";
    }
}

