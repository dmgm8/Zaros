/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.FriendsChatMember;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.NameableContainer;

public interface FriendsChatManager
extends NameableContainer<FriendsChatMember> {
    public String getOwner();

    public String getName();

    public FriendsChatRank getMyRank();

    public FriendsChatRank getKickRank();
}

