/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

public final class FriendsChatChanged {
    private final boolean joined;

    public FriendsChatChanged(boolean joined) {
        this.joined = joined;
    }

    public boolean isJoined() {
        return this.joined;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FriendsChatChanged)) {
            return false;
        }
        FriendsChatChanged other = (FriendsChatChanged)o;
        return this.isJoined() == other.isJoined();
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isJoined() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "FriendsChatChanged(joined=" + this.isJoined() + ")";
    }
}

