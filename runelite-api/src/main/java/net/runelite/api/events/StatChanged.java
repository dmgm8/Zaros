/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.Skill;

public final class StatChanged {
    private final Skill skill;
    private final int xp;
    private final int level;
    private final int boostedLevel;

    public StatChanged(Skill skill, int xp, int level, int boostedLevel) {
        this.skill = skill;
        this.xp = xp;
        this.level = level;
        this.boostedLevel = boostedLevel;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public int getXp() {
        return this.xp;
    }

    public int getLevel() {
        return this.level;
    }

    public int getBoostedLevel() {
        return this.boostedLevel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StatChanged)) {
            return false;
        }
        StatChanged other = (StatChanged)o;
        if (this.getXp() != other.getXp()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getBoostedLevel() != other.getBoostedLevel()) {
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
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getBoostedLevel();
        Skill $skill = this.getSkill();
        result = result * 59 + ($skill == null ? 43 : ((Object)((Object)$skill)).hashCode());
        return result;
    }

    public String toString() {
        return "StatChanged(skill=" + (Object)((Object)this.getSkill()) + ", xp=" + this.getXp() + ", level=" + this.getLevel() + ", boostedLevel=" + this.getBoostedLevel() + ")";
    }
}

