/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.item;

public class ItemPrice {
    private int id;
    private String name;
    private int price;
    private int wikiPrice;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public int getWikiPrice() {
        return this.wikiPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setWikiPrice(int wikiPrice) {
        this.wikiPrice = wikiPrice;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemPrice)) {
            return false;
        }
        ItemPrice other = (ItemPrice)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getPrice() != other.getPrice()) {
            return false;
        }
        if (this.getWikiPrice() != other.getWikiPrice()) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ItemPrice;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getPrice();
        result = result * 59 + this.getWikiPrice();
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "ItemPrice(id=" + this.getId() + ", name=" + this.getName() + ", price=" + this.getPrice() + ", wikiPrice=" + this.getWikiPrice() + ")";
    }
}

