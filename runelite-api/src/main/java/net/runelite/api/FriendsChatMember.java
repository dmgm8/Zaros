/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.ChatPlayer;
import net.runelite.api.FriendsChatRank;

public interface FriendsChatMember
extends ChatPlayer {
    @Override
    public int getWorld();

    public FriendsChatRank getRank();
}

