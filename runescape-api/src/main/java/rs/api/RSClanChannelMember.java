/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.clan.ClanChannelMember
 */
package rs.api;

import net.runelite.api.clan.ClanChannelMember;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSName;

public interface RSClanChannelMember
extends ClanChannelMember {
    @Import(value="name")
    public RSName getRSName();

    @Import(value="rank")
    public byte getRSRank();

    @Import(value="world")
    public int getWorld();
}

