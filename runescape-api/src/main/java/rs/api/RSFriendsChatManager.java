/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.FriendsChatManager
 *  net.runelite.api.FriendsChatMember
 */
package rs.api;

import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNameableContainer;

public interface RSFriendsChatManager
extends RSNameableContainer<FriendsChatMember>,
FriendsChatManager {
    @Import(value="clanOwner")
    public String getOwner();

    @Import(value="clanChatName")
    public String getName();

    @Import(value="kickRank")
    public byte rs$getKickRank();

    @Import(value="selfRank")
    public int rs$getMyRank();
}

