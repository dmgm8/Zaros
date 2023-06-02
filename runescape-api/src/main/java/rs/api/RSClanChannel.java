/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.clan.ClanChannel
 *  net.runelite.api.clan.ClanChannelMember
 */
package rs.api;

import java.util.List;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.mapping.Import;

public interface RSClanChannel
extends ClanChannel {
    @Import(value="clanName")
    public String getName();

    @Import(value="users")
    public List<ClanChannelMember> getMembers();

    @Import(value="getSortedUserSlots")
    public int[] getSortedUserSlots();
}

