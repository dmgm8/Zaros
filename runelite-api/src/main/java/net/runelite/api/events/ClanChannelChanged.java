/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api.events;

import javax.annotation.Nullable;
import net.runelite.api.clan.ClanChannel;

public final class ClanChannelChanged {
    @Nullable
    private final ClanChannel clanChannel;
    private final int clanId;
    private final boolean guest;

    public ClanChannelChanged(@Nullable ClanChannel clanChannel, int clanId, boolean guest) {
        this.clanChannel = clanChannel;
        this.clanId = clanId;
        this.guest = guest;
    }

    @Nullable
    public ClanChannel getClanChannel() {
        return this.clanChannel;
    }

    public int getClanId() {
        return this.clanId;
    }

    public boolean isGuest() {
        return this.guest;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClanChannelChanged)) {
            return false;
        }
        ClanChannelChanged other = (ClanChannelChanged)o;
        if (this.getClanId() != other.getClanId()) {
            return false;
        }
        if (this.isGuest() != other.isGuest()) {
            return false;
        }
        ClanChannel this$clanChannel = this.getClanChannel();
        ClanChannel other$clanChannel = other.getClanChannel();
        return !(this$clanChannel == null ? other$clanChannel != null : !this$clanChannel.equals(other$clanChannel));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getClanId();
        result = result * 59 + (this.isGuest() ? 79 : 97);
        ClanChannel $clanChannel = this.getClanChannel();
        result = result * 59 + ($clanChannel == null ? 43 : $clanChannel.hashCode());
        return result;
    }

    public String toString() {
        return "ClanChannelChanged(clanChannel=" + this.getClanChannel() + ", clanId=" + this.getClanId() + ", guest=" + this.isGuest() + ")";
    }
}

