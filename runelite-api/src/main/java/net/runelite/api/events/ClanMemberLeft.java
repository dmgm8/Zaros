/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;

public final class ClanMemberLeft {
    private final ClanChannel clanChannel;
    private final ClanChannelMember clanMember;

    public ClanMemberLeft(ClanChannel clanChannel, ClanChannelMember clanMember) {
        this.clanChannel = clanChannel;
        this.clanMember = clanMember;
    }

    public ClanChannel getClanChannel() {
        return this.clanChannel;
    }

    public ClanChannelMember getClanMember() {
        return this.clanMember;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClanMemberLeft)) {
            return false;
        }
        ClanMemberLeft other = (ClanMemberLeft)o;
        ClanChannel this$clanChannel = this.getClanChannel();
        ClanChannel other$clanChannel = other.getClanChannel();
        if (this$clanChannel == null ? other$clanChannel != null : !this$clanChannel.equals(other$clanChannel)) {
            return false;
        }
        ClanChannelMember this$clanMember = this.getClanMember();
        ClanChannelMember other$clanMember = other.getClanMember();
        return !(this$clanMember == null ? other$clanMember != null : !this$clanMember.equals(other$clanMember));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        ClanChannel $clanChannel = this.getClanChannel();
        result = result * 59 + ($clanChannel == null ? 43 : $clanChannel.hashCode());
        ClanChannelMember $clanMember = this.getClanMember();
        result = result * 59 + ($clanMember == null ? 43 : $clanMember.hashCode());
        return result;
    }

    public String toString() {
        return "ClanMemberLeft(clanChannel=" + this.getClanChannel() + ", clanMember=" + this.getClanMember() + ")";
    }
}

