/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.util.HashMap;
import java.util.Map;

public enum FriendsChatRank {
    UNRANKED(-1),
    FRIEND(0),
    RECRUIT(1),
    CORPORAL(2),
    SERGEANT(3),
    LIEUTENANT(4),
    CAPTAIN(5),
    GENERAL(6),
    OWNER(7),
    JMOD(127);

    private static final Map<Integer, FriendsChatRank> RANKS;
    private final int value;

    public static FriendsChatRank valueOf(int rank) {
        return RANKS.get(rank);
    }

    private FriendsChatRank(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    static {
        RANKS = new HashMap<Integer, FriendsChatRank>();
        for (FriendsChatRank friendsChatRank : FriendsChatRank.values()) {
            RANKS.put(friendsChatRank.value, friendsChatRank);
        }
    }
}

