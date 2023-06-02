/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  lombok.NonNull
 *  net.runelite.api.NPC
 *  net.runelite.api.Skill
 */
package net.runelite.client.plugins.xptracker;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.NonNull;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.client.plugins.xptracker.XpAction;
import net.runelite.client.plugins.xptracker.XpActionType;
import net.runelite.client.plugins.xptracker.XpSnapshotSingle;
import net.runelite.client.plugins.xptracker.XpStateSingle;
import net.runelite.client.plugins.xptracker.XpTrackerConfig;
import net.runelite.client.plugins.xptracker.XpUpdateResult;

class XpState {
    private static final double DEFAULT_XP_MODIFIER = 4.0;
    private static final double SHARED_XP_MODIFIER = 1.3333333333333333;
    private final Map<Skill, XpStateSingle> xpSkills = new EnumMap<Skill, XpStateSingle>(Skill.class);
    private NPC interactedNPC;
    @Inject
    private XpTrackerConfig xpTrackerConfig;

    XpState() {
    }

    void reset() {
        this.xpSkills.clear();
    }

    void resetSkill(Skill skill, long currentXp) {
        this.xpSkills.remove((Object)skill);
        this.xpSkills.put(skill, new XpStateSingle(skill, currentXp));
    }

    void resetSkillPerHour(Skill skill) {
        this.xpSkills.get((Object)skill).resetPerHour();
    }

    XpUpdateResult updateSkill(Skill skill, long currentXp, int goalStartXp, int goalEndXp) {
        int gainedXp;
        XpStateSingle state = this.getSkill(skill);
        if (state.getStartXp() == -1L) {
            if (currentXp >= 0L) {
                this.initializeSkill(skill, currentXp);
                return XpUpdateResult.INITIALIZED;
            }
            return XpUpdateResult.NO_CHANGE;
        }
        long startXp = state.getStartXp();
        if (startXp + (long)(gainedXp = state.getTotalXpGained()) > currentXp) {
            this.initializeSkill(skill, currentXp);
            return XpUpdateResult.INITIALIZED;
        }
        return state.update(currentXp, goalStartXp, goalEndXp) ? XpUpdateResult.UPDATED : XpUpdateResult.NO_CHANGE;
    }

    private double getCombatXPModifier(Skill skill) {
        if (skill == Skill.HITPOINTS) {
            return 1.3333333333333333;
        }
        return 4.0;
    }

    void updateNpcExperience(Skill skill, NPC npc, Integer npcHealth, int xpModifier) {
        if (npc == null || npc.getCombatLevel() <= 0 || npcHealth == null) {
            return;
        }
        XpStateSingle state = this.getSkill(skill);
        int actionExp = (int)((double)npcHealth.intValue() * this.getCombatXPModifier(skill) * (double)xpModifier);
        XpAction action = state.getXpAction(XpActionType.ACTOR_HEALTH);
        if (action.isActionsHistoryInitialized()) {
            action.getActionExps()[action.getActionExpIndex()] = actionExp;
            if (this.interactedNPC != npc) {
                action.setActionExpIndex((action.getActionExpIndex() + 1) % action.getActionExps().length);
            }
        } else {
            Arrays.fill(action.getActionExps(), actionExp);
            action.setActionsHistoryInitialized(true);
        }
        this.interactedNPC = npc;
        state.setActionType(XpActionType.ACTOR_HEALTH);
    }

    XpUpdateResult updateNpcKills(Skill skill, NPC npc, Integer npcHealth) {
        XpStateSingle state = this.getSkill(skill);
        if (state.getXpGainedSinceReset() <= 0 || npcHealth == null || npc != this.interactedNPC) {
            return XpUpdateResult.NO_CHANGE;
        }
        XpAction xpAction = state.getXpAction(XpActionType.ACTOR_HEALTH);
        xpAction.setActionsSinceReset(xpAction.getActionsSinceReset() + 1);
        return xpAction.isActionsHistoryInitialized() ? XpUpdateResult.UPDATED : XpUpdateResult.NO_CHANGE;
    }

    void tick(Skill skill, long delta) {
        XpStateSingle state = this.getSkill(skill);
        state.tick(delta);
        int resetAfterMinutes = this.xpTrackerConfig.resetSkillRateAfter();
        if (resetAfterMinutes > 0) {
            long now = System.currentTimeMillis();
            int resetAfterMillis = resetAfterMinutes * 60 * 1000;
            long lastChangeMillis = state.getLastChangeMillis();
            if (lastChangeMillis != 0L && now - lastChangeMillis >= (long)resetAfterMillis) {
                state.resetPerHour();
            }
        }
    }

    void initializeSkill(Skill skill, long currentXp) {
        this.xpSkills.put(skill, new XpStateSingle(skill, currentXp));
    }

    boolean isInitialized(Skill skill) {
        XpStateSingle xpStateSingle = this.xpSkills.get((Object)skill);
        return xpStateSingle != null && xpStateSingle.getStartXp() != -1L;
    }

    @NonNull
    XpStateSingle getSkill(Skill skill) {
        return this.xpSkills.computeIfAbsent(skill, s -> new XpStateSingle((Skill)s, -1L));
    }

    @NonNull
    XpSnapshotSingle getSkillSnapshot(Skill skill) {
        return this.getSkill(skill).snapshot();
    }

    @NonNull
    XpSnapshotSingle getTotalSnapshot() {
        return this.getSkill(Skill.OVERALL).snapshot();
    }
}

