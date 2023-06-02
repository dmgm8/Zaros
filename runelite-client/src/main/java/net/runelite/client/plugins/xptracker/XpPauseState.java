/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Skill
 */
package net.runelite.client.plugins.xptracker;

import java.util.EnumMap;
import java.util.Map;
import net.runelite.api.Skill;
import net.runelite.client.plugins.xptracker.XpPauseStateSingle;

class XpPauseState {
    private final Map<Skill, XpPauseStateSingle> skillPauses = new EnumMap<Skill, XpPauseStateSingle>(Skill.class);
    private boolean cachedIsLoggedIn = false;

    XpPauseState() {
    }

    boolean pauseSkill(Skill skill) {
        return this.findPauseState(skill).manualPause();
    }

    boolean unpauseSkill(Skill skill) {
        return this.findPauseState(skill).unpause();
    }

    boolean isPaused(Skill skill) {
        return this.findPauseState(skill).isPaused();
    }

    void tickXp(Skill skill, long currentXp, int pauseAfterMinutes) {
        XpPauseStateSingle state = this.findPauseState(skill);
        if (state.getXp() != currentXp) {
            state.xpChanged(currentXp);
        } else if (pauseAfterMinutes > 0) {
            long now = System.currentTimeMillis();
            int pauseAfterMillis = pauseAfterMinutes * 60 * 1000;
            long lastChangeMillis = state.getLastChangeMillis();
            if (lastChangeMillis != 0L && now - lastChangeMillis >= (long)pauseAfterMillis) {
                state.timeout();
            }
        }
    }

    void tickLogout(boolean pauseOnLogout, boolean loggedIn) {
        block4: {
            block3: {
                if (this.cachedIsLoggedIn || !loggedIn) break block3;
                this.cachedIsLoggedIn = true;
                for (Skill skill : Skill.values()) {
                    this.findPauseState(skill).login();
                }
                break block4;
            }
            if (!this.cachedIsLoggedIn || loggedIn) break block4;
            this.cachedIsLoggedIn = false;
            if (pauseOnLogout) {
                for (Skill skill : Skill.values()) {
                    this.findPauseState(skill).logout();
                }
            }
        }
    }

    private XpPauseStateSingle findPauseState(Skill skill) {
        return this.skillPauses.computeIfAbsent(skill, XpPauseStateSingle::new);
    }
}

