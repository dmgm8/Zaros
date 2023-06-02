/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.ItemComposition;

public final class PostItemComposition {
    private final ItemComposition itemComposition;

    public PostItemComposition(ItemComposition itemComposition) {
        this.itemComposition = itemComposition;
    }

    public ItemComposition getItemComposition() {
        return this.itemComposition;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PostItemComposition)) {
            return false;
        }
        PostItemComposition other = (PostItemComposition)o;
        ItemComposition this$itemComposition = this.getItemComposition();
        ItemComposition other$itemComposition = other.getItemComposition();
        return !(this$itemComposition == null ? other$itemComposition != null : !this$itemComposition.equals(other$itemComposition));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        ItemComposition $itemComposition = this.getItemComposition();
        result = result * 59 + ($itemComposition == null ? 43 : $itemComposition.hashCode());
        return result;
    }

    public String toString() {
        return "PostItemComposition(itemComposition=" + this.getItemComposition() + ")";
    }
}

