/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.hiscore;

import java.util.Map;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.hiscore.Skill;

public final class HiscoreResult {
    private final int accountType;
    private final String player;
    private final Map<HiscoreSkill, Skill> skills;

    public Skill getSkill(HiscoreSkill skill) {
        return this.skills.get((Object)skill);
    }

    public HiscoreResult(int accountType, String player, Map<HiscoreSkill, Skill> skills) {
        this.accountType = accountType;
        this.player = player;
        this.skills = skills;
    }

    public int getAccountType() {
        return this.accountType;
    }

    public String getPlayer() {
        return this.player;
    }

    public Map<HiscoreSkill, Skill> getSkills() {
        return this.skills;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HiscoreResult)) {
            return false;
        }
        HiscoreResult other = (HiscoreResult)o;
        if (this.getAccountType() != other.getAccountType()) {
            return false;
        }
        String this$player = this.getPlayer();
        String other$player = other.getPlayer();
        if (this$player == null ? other$player != null : !this$player.equals(other$player)) {
            return false;
        }
        Map<HiscoreSkill, Skill> this$skills = this.getSkills();
        Map<HiscoreSkill, Skill> other$skills = other.getSkills();
        return !(this$skills == null ? other$skills != null : !((Object)this$skills).equals(other$skills));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAccountType();
        String $player = this.getPlayer();
        result = result * 59 + ($player == null ? 43 : $player.hashCode());
        Map<HiscoreSkill, Skill> $skills = this.getSkills();
        result = result * 59 + ($skills == null ? 43 : ((Object)$skills).hashCode());
        return result;
    }

    public String toString() {
        return "HiscoreResult(accountType=" + this.getAccountType() + ", player=" + this.getPlayer() + ", skills=" + this.getSkills() + ")";
    }
}

