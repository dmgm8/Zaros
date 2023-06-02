/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.cluescrolls.clues;

import java.awt.Graphics2D;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.Enemy;
import net.runelite.client.ui.overlay.components.PanelComponent;

public abstract class ClueScroll {
    private boolean requiresSpade;
    private boolean requiresLight;
    private int firePitVarbitId = -1;
    private Enemy enemy;

    public abstract void makeOverlayHint(PanelComponent var1, ClueScrollPlugin var2);

    public abstract void makeWorldOverlayHint(Graphics2D var1, ClueScrollPlugin var2);

    protected void setRequiresSpade(boolean requiresSpade) {
        this.requiresSpade = requiresSpade;
    }

    public boolean isRequiresSpade() {
        return this.requiresSpade;
    }

    protected void setRequiresLight(boolean requiresLight) {
        this.requiresLight = requiresLight;
    }

    public boolean isRequiresLight() {
        return this.requiresLight;
    }

    protected void setFirePitVarbitId(int firePitVarbitId) {
        this.firePitVarbitId = firePitVarbitId;
    }

    public int getFirePitVarbitId() {
        return this.firePitVarbitId;
    }

    protected void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Enemy getEnemy() {
        return this.enemy;
    }
}

