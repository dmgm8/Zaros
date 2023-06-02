/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 *  net.runelite.api.VarPlayer
 */
package net.runelite.client.plugins.achievementdiary;

import net.runelite.api.Client;
import net.runelite.api.VarPlayer;
import net.runelite.client.plugins.achievementdiary.Requirement;

public class QuestPointRequirement
implements Requirement {
    private final int qp;

    public String toString() {
        return this.qp + " Quest points";
    }

    @Override
    public boolean satisfiesRequirement(Client client) {
        return client.getVarpValue(VarPlayer.QUEST_POINTS) >= this.qp;
    }

    public QuestPointRequirement(int qp) {
        this.qp = qp;
    }

    public int getQp() {
        return this.qp;
    }
}

