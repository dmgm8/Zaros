/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Skill;

public final class FakeXpDrop {
    private final Skill skill;
    private final int xp;

    public FakeXpDrop(Skill skill, int xp) {
        this.skill = skill;
        this.xp = xp;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public int getXp() {
        return this.xp;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FakeXpDrop)) {
            return false;
        }
        FakeXpDrop other = (FakeXpDrop)o;
        if (this.getXp() != other.getXp()) {
            return false;
        }
        Skill this$skill = this.getSkill();
        Skill other$skill = other.getSkill();
        return !(this$skill == null ? other$skill != null : !((Object)((Object)this$skill)).equals((Object)other$skill));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getXp();
        Skill $skill = this.getSkill();
        result = result * 59 + ($skill == null ? 43 : ((Object)((Object)$skill)).hashCode());
        return result;
    }

    public String toString() {
        return "FakeXpDrop(skill=" + (Object)((Object)this.getSkill()) + ", xp=" + this.getXp() + ")";
    }
}

