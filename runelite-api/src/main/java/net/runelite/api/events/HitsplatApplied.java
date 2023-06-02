/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Actor;
import net.runelite.api.Hitsplat;

public class HitsplatApplied {
    private Actor actor;
    private Hitsplat hitsplat;

    public Actor getActor() {
        return this.actor;
    }

    public Hitsplat getHitsplat() {
        return this.hitsplat;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public void setHitsplat(Hitsplat hitsplat) {
        this.hitsplat = hitsplat;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HitsplatApplied)) {
            return false;
        }
        HitsplatApplied other = (HitsplatApplied)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Actor this$actor = this.getActor();
        Actor other$actor = other.getActor();
        if (this$actor == null ? other$actor != null : !this$actor.equals(other$actor)) {
            return false;
        }
        Hitsplat this$hitsplat = this.getHitsplat();
        Hitsplat other$hitsplat = other.getHitsplat();
        return !(this$hitsplat == null ? other$hitsplat != null : !this$hitsplat.equals(other$hitsplat));
    }

    protected boolean canEqual(Object other) {
        return other instanceof HitsplatApplied;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Actor $actor = this.getActor();
        result = result * 59 + ($actor == null ? 43 : $actor.hashCode());
        Hitsplat $hitsplat = this.getHitsplat();
        result = result * 59 + ($hitsplat == null ? 43 : $hitsplat.hashCode());
        return result;
    }

    public String toString() {
        return "HitsplatApplied(actor=" + this.getActor() + ", hitsplat=" + this.getHitsplat() + ")";
    }
}

