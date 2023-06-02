/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Actor;

public final class InteractingChanged {
    private final Actor source;
    private final Actor target;

    public InteractingChanged(Actor source, Actor target) {
        this.source = source;
        this.target = target;
    }

    public Actor getSource() {
        return this.source;
    }

    public Actor getTarget() {
        return this.target;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InteractingChanged)) {
            return false;
        }
        InteractingChanged other = (InteractingChanged)o;
        Actor this$source = this.getSource();
        Actor other$source = other.getSource();
        if (this$source == null ? other$source != null : !this$source.equals(other$source)) {
            return false;
        }
        Actor this$target = this.getTarget();
        Actor other$target = other.getTarget();
        return !(this$target == null ? other$target != null : !this$target.equals(other$target));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Actor $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : $source.hashCode());
        Actor $target = this.getTarget();
        result = result * 59 + ($target == null ? 43 : $target.hashCode());
        return result;
    }

    public String toString() {
        return "InteractingChanged(source=" + this.getSource() + ", target=" + this.getTarget() + ")";
    }
}

