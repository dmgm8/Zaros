/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import net.runelite.api.FriendsChatMember;

public final class FriendsChatMemberLeft {
    private final FriendsChatMember member;

    public FriendsChatMemberLeft(FriendsChatMember member) {
        this.member = member;
    }

    public FriendsChatMember getMember() {
        return this.member;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FriendsChatMemberLeft)) {
            return false;
        }
        FriendsChatMemberLeft other = (FriendsChatMemberLeft)o;
        FriendsChatMember this$member = this.getMember();
        FriendsChatMember other$member = other.getMember();
        return !(this$member == null ? other$member != null : !this$member.equals(other$member));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        FriendsChatMember $member = this.getMember();
        result = result * 59 + ($member == null ? 43 : $member.hashCode());
        return result;
    }

    public String toString() {
        return "FriendsChatMemberLeft(member=" + this.getMember() + ")";
    }
}

