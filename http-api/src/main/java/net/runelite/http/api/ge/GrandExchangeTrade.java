/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.ge;

import java.time.Instant;
import net.runelite.http.api.worlds.WorldType;

public class GrandExchangeTrade {
    private boolean buy;
    private boolean cancel;
    private boolean login;
    private int itemId;
    private int qty;
    private int dqty;
    private int total;
    private int spent;
    private int dspent;
    private int offer;
    private int slot;
    private WorldType worldType;
    private int seq;
    private Instant resetTime;

    public boolean isBuy() {
        return this.buy;
    }

    public boolean isCancel() {
        return this.cancel;
    }

    public boolean isLogin() {
        return this.login;
    }

    public int getItemId() {
        return this.itemId;
    }

    public int getQty() {
        return this.qty;
    }

    public int getDqty() {
        return this.dqty;
    }

    public int getTotal() {
        return this.total;
    }

    public int getSpent() {
        return this.spent;
    }

    public int getDspent() {
        return this.dspent;
    }

    public int getOffer() {
        return this.offer;
    }

    public int getSlot() {
        return this.slot;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public int getSeq() {
        return this.seq;
    }

    public Instant getResetTime() {
        return this.resetTime;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setDqty(int dqty) {
        this.dqty = dqty;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setSpent(int spent) {
        this.spent = spent;
    }

    public void setDspent(int dspent) {
        this.dspent = dspent;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setWorldType(WorldType worldType) {
        this.worldType = worldType;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setResetTime(Instant resetTime) {
        this.resetTime = resetTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GrandExchangeTrade)) {
            return false;
        }
        GrandExchangeTrade other = (GrandExchangeTrade)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isBuy() != other.isBuy()) {
            return false;
        }
        if (this.isCancel() != other.isCancel()) {
            return false;
        }
        if (this.isLogin() != other.isLogin()) {
            return false;
        }
        if (this.getItemId() != other.getItemId()) {
            return false;
        }
        if (this.getQty() != other.getQty()) {
            return false;
        }
        if (this.getDqty() != other.getDqty()) {
            return false;
        }
        if (this.getTotal() != other.getTotal()) {
            return false;
        }
        if (this.getSpent() != other.getSpent()) {
            return false;
        }
        if (this.getDspent() != other.getDspent()) {
            return false;
        }
        if (this.getOffer() != other.getOffer()) {
            return false;
        }
        if (this.getSlot() != other.getSlot()) {
            return false;
        }
        if (this.getSeq() != other.getSeq()) {
            return false;
        }
        WorldType this$worldType = this.getWorldType();
        WorldType other$worldType = other.getWorldType();
        if (this$worldType == null ? other$worldType != null : !((Object)((Object)this$worldType)).equals((Object)other$worldType)) {
            return false;
        }
        Instant this$resetTime = this.getResetTime();
        Instant other$resetTime = other.getResetTime();
        return !(this$resetTime == null ? other$resetTime != null : !((Object)this$resetTime).equals(other$resetTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GrandExchangeTrade;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isBuy() ? 79 : 97);
        result = result * 59 + (this.isCancel() ? 79 : 97);
        result = result * 59 + (this.isLogin() ? 79 : 97);
        result = result * 59 + this.getItemId();
        result = result * 59 + this.getQty();
        result = result * 59 + this.getDqty();
        result = result * 59 + this.getTotal();
        result = result * 59 + this.getSpent();
        result = result * 59 + this.getDspent();
        result = result * 59 + this.getOffer();
        result = result * 59 + this.getSlot();
        result = result * 59 + this.getSeq();
        WorldType $worldType = this.getWorldType();
        result = result * 59 + ($worldType == null ? 43 : ((Object)((Object)$worldType)).hashCode());
        Instant $resetTime = this.getResetTime();
        result = result * 59 + ($resetTime == null ? 43 : ((Object)$resetTime).hashCode());
        return result;
    }

    public String toString() {
        return "GrandExchangeTrade(buy=" + this.isBuy() + ", cancel=" + this.isCancel() + ", login=" + this.isLogin() + ", itemId=" + this.getItemId() + ", qty=" + this.getQty() + ", dqty=" + this.getDqty() + ", total=" + this.getTotal() + ", spent=" + this.getSpent() + ", dspent=" + this.getDspent() + ", offer=" + this.getOffer() + ", slot=" + this.getSlot() + ", worldType=" + (Object)((Object)this.getWorldType()) + ", seq=" + this.getSeq() + ", resetTime=" + this.getResetTime() + ")";
    }
}

