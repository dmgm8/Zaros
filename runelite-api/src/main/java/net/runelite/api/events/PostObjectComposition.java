/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.ObjectComposition;

public final class PostObjectComposition {
    private final ObjectComposition objectComposition;

    public PostObjectComposition(ObjectComposition objectComposition) {
        this.objectComposition = objectComposition;
    }

    public ObjectComposition getObjectComposition() {
        return this.objectComposition;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PostObjectComposition)) {
            return false;
        }
        PostObjectComposition other = (PostObjectComposition)o;
        ObjectComposition this$objectComposition = this.getObjectComposition();
        ObjectComposition other$objectComposition = other.getObjectComposition();
        return !(this$objectComposition == null ? other$objectComposition != null : !this$objectComposition.equals(other$objectComposition));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        ObjectComposition $objectComposition = this.getObjectComposition();
        result = result * 59 + ($objectComposition == null ? 43 : $objectComposition.hashCode());
        return result;
    }

    public String toString() {
        return "PostObjectComposition(objectComposition=" + this.getObjectComposition() + ")";
    }
}

