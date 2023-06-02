/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Experience
 *  net.runelite.api.Skill
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.xptracker;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.client.plugins.xptracker.XpAction;
import net.runelite.client.plugins.xptracker.XpActionType;
import net.runelite.client.plugins.xptracker.XpGoalTimeType;
import net.runelite.client.plugins.xptracker.XpSnapshotSingle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class XpStateSingle {
    private static final Logger log = LoggerFactory.getLogger(XpStateSingle.class);
    private final Skill skill;
    private final Map<XpActionType, XpAction> actions = new EnumMap<XpActionType, XpAction>(XpActionType.class);
    private long startXp;
    private int xpGainedSinceReset = 0;
    private int xpGainedBeforeReset = 0;
    private XpActionType actionType = XpActionType.EXPERIENCE;
    private long skillTime = 0L;
    private long lastChangeMillis;
    private int startLevelExp = 0;
    private int endLevelExp = 0;

    XpStateSingle(Skill skill, long startXp) {
        this.skill = skill;
        this.startXp = startXp;
    }

    XpAction getXpAction(XpActionType type) {
        return this.actions.computeIfAbsent(type, k -> new XpAction());
    }

    long getCurrentXp() {
        return this.startXp + (long)this.getTotalXpGained();
    }

    void setXpGainedSinceReset(int xpGainedSinceReset) {
        this.xpGainedSinceReset = xpGainedSinceReset;
        this.lastChangeMillis = System.currentTimeMillis();
    }

    int getTotalXpGained() {
        return this.xpGainedBeforeReset + this.xpGainedSinceReset;
    }

    private int getActionsHr() {
        return this.toHourly(this.getXpAction(this.actionType).getActionsSinceReset());
    }

    private int toHourly(int value) {
        return (int)(1.0 / ((double)this.getTimeElapsedInSeconds() / 3600.0) * (double)value);
    }

    private long getTimeElapsedInSeconds() {
        return Math.max(60L, this.skillTime / 1000L);
    }

    private int getXpRemaining() {
        return this.endLevelExp - (int)this.getCurrentXp();
    }

    private int getActionsRemaining() {
        XpAction action = this.getXpAction(this.actionType);
        if (action.isActionsHistoryInitialized()) {
            long xpRemaining = this.getXpRemaining() * action.getActionExps().length;
            long totalActionXp = 0L;
            for (int actionXp : action.getActionExps()) {
                totalActionXp += (long)actionXp;
            }
            if (totalActionXp > 0L) {
                long remainder = xpRemaining % totalActionXp;
                long quotient = xpRemaining / totalActionXp;
                return Math.toIntExact(quotient + (long)(remainder > 0L ? 1 : 0));
            }
        }
        return Integer.MAX_VALUE;
    }

    private double getSkillProgress() {
        double xpGained = this.getCurrentXp() - (long)this.startLevelExp;
        double xpGoal = this.endLevelExp - this.startLevelExp;
        return xpGained / xpGoal * 100.0;
    }

    private long getSecondsTillLevel() {
        long seconds = this.getTimeElapsedInSeconds();
        if (seconds <= 0L || this.xpGainedSinceReset <= 0) {
            return -1L;
        }
        return (long)this.getXpRemaining() * seconds / (long)this.xpGainedSinceReset;
    }

    private String getTimeTillLevel(XpGoalTimeType goalTimeType) {
        long remainingSeconds = this.getSecondsTillLevel();
        if (remainingSeconds < 0L) {
            return "\u221e";
        }
        long durationDays = remainingSeconds / 86400L;
        long durationHours = remainingSeconds % 86400L / 3600L;
        long durationHoursTotal = remainingSeconds / 3600L;
        long durationMinutes = remainingSeconds % 3600L / 60L;
        long durationSeconds = remainingSeconds % 60L;
        switch (goalTimeType) {
            case DAYS: {
                if (durationDays > 1L) {
                    return String.format("%d days %02d:%02d:%02d", durationDays, durationHours, durationMinutes, durationSeconds);
                }
                if (durationDays == 1L) {
                    return String.format("1 day %02d:%02d:%02d", durationHours, durationMinutes, durationSeconds);
                }
            }
            case HOURS: {
                if (durationHoursTotal > 1L) {
                    return String.format("%d hours %02d:%02d", durationHoursTotal, durationMinutes, durationSeconds);
                }
                if (durationHoursTotal != 1L) break;
                return String.format("1 hour %02d:%02d", durationMinutes, durationSeconds);
            }
        }
        if (durationHoursTotal > 0L) {
            return String.format("%d:%02d:%02d", durationHoursTotal, durationMinutes, durationSeconds);
        }
        return String.format("%02d:%02d", durationMinutes, durationSeconds);
    }

    int getXpHr() {
        return this.toHourly(this.xpGainedSinceReset);
    }

    void resetPerHour() {
        for (XpAction action : this.actions.values()) {
            action.setActions(action.getActions() + action.getActionsSinceReset());
            action.setActionsSinceReset(0);
        }
        this.xpGainedBeforeReset += this.xpGainedSinceReset;
        this.setXpGainedSinceReset(0);
        this.setSkillTime(0L);
    }

    boolean update(long currentXp, int goalStartXp, int goalEndXp) {
        if (this.startXp == -1L) {
            log.warn("Attempted to update skill state {} but was not initialized with current xp", (Object)this.skill);
            return false;
        }
        long originalXp = (long)this.getTotalXpGained() + this.startXp;
        int actionExp = (int)(currentXp - originalXp);
        if (actionExp == 0) {
            return false;
        }
        XpAction action = this.getXpAction(XpActionType.EXPERIENCE);
        if (action.isActionsHistoryInitialized()) {
            action.getActionExps()[action.getActionExpIndex()] = actionExp;
        } else {
            Arrays.fill(action.getActionExps(), actionExp);
            action.setActionsHistoryInitialized(true);
        }
        action.setActionExpIndex((action.getActionExpIndex() + 1) % action.getActionExps().length);
        action.setActionsSinceReset(action.getActionsSinceReset() + 1);
        this.setXpGainedSinceReset((int)(currentXp - (this.startXp + (long)this.xpGainedBeforeReset)));
        if (this.skill != Skill.OVERALL) {
            int currentLevel;
            this.startLevelExp = goalStartXp < 0 || currentXp > (long)goalEndXp ? Experience.getXpForLevel((int)Experience.getLevelForXp((int)((int)currentXp))) : goalStartXp;
            this.endLevelExp = goalEndXp <= 0 || currentXp > (long)goalEndXp ? ((currentLevel = Experience.getLevelForXp((int)((int)currentXp))) + 1 <= 126 ? Experience.getXpForLevel((int)(currentLevel + 1)) : 200000000) : goalEndXp;
        }
        return true;
    }

    public void tick(long delta) {
        if (this.xpGainedSinceReset <= 0) {
            return;
        }
        this.skillTime += delta;
    }

    XpSnapshotSingle snapshot() {
        return XpSnapshotSingle.builder().startLevel(Experience.getLevelForXp((int)this.startLevelExp)).endLevel(Experience.getLevelForXp((int)this.endLevelExp)).xpGainedInSession(this.getTotalXpGained()).xpRemainingToGoal(this.getXpRemaining()).xpPerHour(this.getXpHr()).skillProgressToGoal(this.getSkillProgress()).actionType(this.actionType).actionsInSession(this.getXpAction(this.actionType).getActions() + this.getXpAction(this.actionType).getActionsSinceReset()).actionsRemainingToGoal(this.getActionsRemaining()).actionsPerHour(this.getActionsHr()).timeTillGoal(this.getTimeTillLevel(XpGoalTimeType.DAYS)).timeTillGoalHours(this.getTimeTillLevel(XpGoalTimeType.HOURS)).timeTillGoalShort(this.getTimeTillLevel(XpGoalTimeType.SHORT)).startGoalXp(this.startLevelExp).endGoalXp(this.endLevelExp).build();
    }

    public long getStartXp() {
        return this.startXp;
    }

    public void setStartXp(long startXp) {
        this.startXp = startXp;
    }

    public int getXpGainedSinceReset() {
        return this.xpGainedSinceReset;
    }

    public void setActionType(XpActionType actionType) {
        this.actionType = actionType;
    }

    public void setSkillTime(long skillTime) {
        this.skillTime = skillTime;
    }

    public long getLastChangeMillis() {
        return this.lastChangeMillis;
    }
}

