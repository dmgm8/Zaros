/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Skill
 */
package net.runelite.client.plugins.xptracker;

import net.runelite.api.Skill;
import net.runelite.client.plugins.xptracker.XpActionType;

public interface XpTrackerService {
    public int getActions(Skill var1);

    public int getActionsHr(Skill var1);

    public int getActionsLeft(Skill var1);

    public XpActionType getActionType(Skill var1);

    public int getXpHr(Skill var1);

    public int getStartGoalXp(Skill var1);

    public int getEndGoalXp(Skill var1);

    public String getTimeTilGoal(Skill var1);
}

