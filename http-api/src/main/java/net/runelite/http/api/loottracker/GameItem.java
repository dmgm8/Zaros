/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.loottracker;

public class GameItem {
    private int id;
    private int qty;

    public int getId() {
        return this.id;
    }

    public int getQty() {
        return this.qty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameItem)) {
            return false;
        }
        GameItem other = (GameItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        return this.getQty() == other.getQty();
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameItem;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getQty();
        return result;
    }

    public String toString() {
        return "GameItem(id=" + this.getId() + ", qty=" + this.getQty() + ")";
    }

    public GameItem() {
    }

    public GameItem(int id, int qty) {
        this.id = id;
        this.qty = qty;
    }
}

