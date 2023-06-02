/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api;

import java.awt.Polygon;
import javax.annotation.Nullable;
import net.runelite.api.Actor;
import net.runelite.api.HeadIcon;
import net.runelite.api.PlayerComposition;
import net.runelite.api.SkullIcon;

public interface Player
extends Actor {
    public int getId();

    @Override
    public int getCombatLevel();

    public PlayerComposition getPlayerComposition();

    public Polygon[] getPolygons();

    public int getTeam();

    public boolean isFriendsChatMember();

    public boolean isFriend();

    public boolean isClanMember();

    public HeadIcon getOverheadIcon();

    @Nullable
    public SkullIcon getSkullIcon();
}

