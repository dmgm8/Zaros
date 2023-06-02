/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.HeadIcon;
import net.runelite.api.ParamHolder;

public interface NPCComposition
extends ParamHolder {
    public String getName();

    public int[] getModels();

    public String[] getActions();

    public boolean isClickable();

    public boolean isInteractible();

    public boolean isMinimapVisible();

    public boolean isVisible();

    public int getId();

    public int getCombatLevel();

    public int[] getConfigs();

    public NPCComposition transform();

    public int getSize();

    public HeadIcon getOverheadIcon();

    public boolean isFollower();
}

