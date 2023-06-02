/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.vars;

public enum AccountType {
    NORMAL,
    IRONMAN,
    ULTIMATE_IRONMAN,
    HARDCORE_IRONMAN,
    GROUP_IRONMAN,
    HARDCORE_GROUP_IRONMAN,
    ELITE_IRONMAN,
    FALLEN_ELITE_IRONMAN,
    KOTS,
    REALISM,
    REALISM_GROUP_IRONMAN,
    DEADMAN,
    DEADMAN_SPECTATOR;


    public boolean isIronman() {
        switch (this) {
            case ELITE_IRONMAN: 
            case FALLEN_ELITE_IRONMAN: 
            case KOTS: {
                return true;
            }
        }
        return this.ordinal() >= IRONMAN.ordinal() && this.ordinal() <= HARDCORE_IRONMAN.ordinal();
    }

    public boolean isGroupIronman() {
        if (this == REALISM_GROUP_IRONMAN) {
            return true;
        }
        return this.ordinal() >= GROUP_IRONMAN.ordinal() && this.ordinal() <= HARDCORE_GROUP_IRONMAN.ordinal();
    }
}

