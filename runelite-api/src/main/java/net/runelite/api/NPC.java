/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.Actor;
import net.runelite.api.NPCComposition;

public interface NPC
extends Actor {
    public int getId();

    @Override
    public String getName();

    @Override
    public int getCombatLevel();

    public int getIndex();

    public NPCComposition getComposition();

    @Nullable
    public NPCComposition getTransformedComposition();
}

