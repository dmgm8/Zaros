/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.GrandExchangeOffer;

public class GrandExchangeOfferChanged {
    private GrandExchangeOffer offer;
    private int slot;

    public GrandExchangeOffer getOffer() {
        return this.offer;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setOffer(GrandExchangeOffer offer) {
        this.offer = offer;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GrandExchangeOfferChanged)) {
            return false;
        }
        GrandExchangeOfferChanged other = (GrandExchangeOfferChanged)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSlot() != other.getSlot()) {
            return false;
        }
        GrandExchangeOffer this$offer = this.getOffer();
        GrandExchangeOffer other$offer = other.getOffer();
        return !(this$offer == null ? other$offer != null : !this$offer.equals(other$offer));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GrandExchangeOfferChanged;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSlot();
        GrandExchangeOffer $offer = this.getOffer();
        result = result * 59 + ($offer == null ? 43 : $offer.hashCode());
        return result;
    }

    public String toString() {
        return "GrandExchangeOfferChanged(offer=" + this.getOffer() + ", slot=" + this.getSlot() + ")";
    }
}

