/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.IterableHashTable
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.Node
 */
package rs.api;

import net.runelite.api.IterableHashTable;
import net.runelite.api.NPCComposition;
import net.runelite.api.Node;
import net.runelite.mapping.Import;

public interface RSNPCComposition
extends NPCComposition {
    @Import(value="name")
    public String getName();

    @Import(value="models")
    public int[] getModels();

    @Import(value="actions")
    public String[] getActions();

    @Import(value="isClickable")
    public boolean isClickable();

    @Import(value="interactable")
    public boolean isInteractible();

    @Import(value="isMinimapVisible")
    public boolean isMinimapVisible();

    @Import(value="isVisible")
    public boolean isVisible();

    @Import(value="isFollower")
    public boolean isFollower();

    @Import(value="id")
    public int getId();

    @Import(value="combatLevel")
    public int getCombatLevel();

    @Import(value="configs")
    public int[] getConfigs();

    @Import(value="transform")
    public RSNPCComposition transform();

    @Import(value="size")
    public int getSize();

    @Import(value="headIcon")
    public int getRsOverheadIcon();

    @Import(value="params")
    public IterableHashTable<Node> getParams();

    @Import(value="params")
    public void setParams(IterableHashTable<Node> var1);
}

