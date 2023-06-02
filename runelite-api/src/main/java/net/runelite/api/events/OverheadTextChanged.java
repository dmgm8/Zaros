/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Actor;

public final class OverheadTextChanged {
    private final Actor actor;
    private final String overheadText;

    public OverheadTextChanged(Actor actor, String overheadText) {
        this.actor = actor;
        this.overheadText = overheadText;
    }

    public Actor getActor() {
        return this.actor;
    }

    public String getOverheadText() {
        return this.overheadText;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OverheadTextChanged)) {
            return false;
        }
        OverheadTextChanged other = (OverheadTextChanged)o;
        Actor this$actor = this.getActor();
        Actor other$actor = other.getActor();
        if (this$actor == null ? other$actor != null : !this$actor.equals(other$actor)) {
            return false;
        }
        String this$overheadText = this.getOverheadText();
        String other$overheadText = other.getOverheadText();
        return !(this$overheadText == null ? other$overheadText != null : !this$overheadText.equals(other$overheadText));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Actor $actor = this.getActor();
        result = result * 59 + ($actor == null ? 43 : $actor.hashCode());
        String $overheadText = this.getOverheadText();
        result = result * 59 + ($overheadText == null ? 43 : $overheadText.hashCode());
        return result;
    }

    public String toString() {
        return "OverheadTextChanged(actor=" + this.getActor() + ", overheadText=" + this.getOverheadText() + ")";
    }
}

