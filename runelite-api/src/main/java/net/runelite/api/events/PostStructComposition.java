/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.StructComposition;

public class PostStructComposition {
    private StructComposition structComposition;

    public StructComposition getStructComposition() {
        return this.structComposition;
    }

    public void setStructComposition(StructComposition structComposition) {
        this.structComposition = structComposition;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PostStructComposition)) {
            return false;
        }
        PostStructComposition other = (PostStructComposition)o;
        if (!other.canEqual(this)) {
            return false;
        }
        StructComposition this$structComposition = this.getStructComposition();
        StructComposition other$structComposition = other.getStructComposition();
        return !(this$structComposition == null ? other$structComposition != null : !this$structComposition.equals(other$structComposition));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PostStructComposition;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        StructComposition $structComposition = this.getStructComposition();
        result = result * 59 + ($structComposition == null ? 43 : $structComposition.hashCode());
        return result;
    }

    public String toString() {
        return "PostStructComposition(structComposition=" + this.getStructComposition() + ")";
    }
}

