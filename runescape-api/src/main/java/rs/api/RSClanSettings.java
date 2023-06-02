/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.clan.ClanSettings
 */
package rs.api;

import net.runelite.api.clan.ClanSettings;
import net.runelite.mapping.Import;

public interface RSClanSettings
extends ClanSettings {
    @Import(value="clanName")
    public String getName();

    @Import(value="memberCount")
    public int getMemberCount();

    @Import(value="memberNames")
    public String[] getMemberNames();

    @Import(value="memberRanks")
    public byte[] getMemberRanks();

    @Import(value="getSortedMemberSlots")
    public int[] getSortedMemberSlots();

    @Import(value="getVarValue")
    public Integer getVarValue(int var1);
}

