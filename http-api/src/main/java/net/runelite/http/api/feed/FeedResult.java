/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.feed;

import java.util.List;
import net.runelite.http.api.feed.FeedItem;

public class FeedResult {
    private List<FeedItem> items;

    public List<FeedItem> getItems() {
        return this.items;
    }

    public void setItems(List<FeedItem> items) {
        this.items = items;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FeedResult)) {
            return false;
        }
        FeedResult other = (FeedResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<FeedItem> this$items = this.getItems();
        List<FeedItem> other$items = other.getItems();
        return !(this$items == null ? other$items != null : !((Object)this$items).equals(other$items));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FeedResult;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<FeedItem> $items = this.getItems();
        result = result * 59 + ($items == null ? 43 : ((Object)$items).hashCode());
        return result;
    }

    public String toString() {
        return "FeedResult(items=" + this.getItems() + ")";
    }

    public FeedResult(List<FeedItem> items) {
        this.items = items;
    }
}

