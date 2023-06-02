/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.util.Collection;
import java.util.EnumSet;

public enum WorldType {
    MEMBERS(1),
    PVP(4),
    BOUNTY(32),
    PVP_ARENA(64),
    SKILL_TOTAL(128),
    QUEST_SPEEDRUNNING(256),
    HIGH_RISK(1024),
    LAST_MAN_STANDING(16384),
    NOSAVE_MODE(0x2000000),
    TOURNAMENT_WORLD(0x4000000),
    DEADMAN(0x20000000),
    SEASONAL(0x40000000);

    private final int mask;
    private static final EnumSet<WorldType> PVP_WORLD_TYPES;

    public static EnumSet<WorldType> fromMask(int mask) {
        EnumSet<WorldType> types = EnumSet.noneOf(WorldType.class);
        for (WorldType type : WorldType.values()) {
            if ((mask & type.mask) == 0) continue;
            types.add(type);
        }
        return types;
    }

    public static int toMask(EnumSet<WorldType> types) {
        int mask = 0;
        for (WorldType type : types) {
            mask |= type.mask;
        }
        return mask;
    }

    public static boolean isPvpWorld(Collection<WorldType> worldTypes) {
        return worldTypes.stream().anyMatch(PVP_WORLD_TYPES::contains);
    }

    private WorldType(int mask) {
        this.mask = mask;
    }

    static {
        PVP_WORLD_TYPES = EnumSet.of(DEADMAN, PVP);
    }
}

