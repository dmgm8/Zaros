/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 *  net.runelite.api.WorldType
 */
package net.runelite.client.config;

import java.util.function.Predicate;
import net.runelite.api.Client;
import net.runelite.api.WorldType;

public enum RuneScapeProfileType {
    STANDARD(client -> true),
    BETA(client -> client.getWorldType().contains((Object)WorldType.NOSAVE_MODE)),
    QUEST_SPEEDRUNNING(client -> client.getWorldType().contains((Object)WorldType.QUEST_SPEEDRUNNING)),
    DEADMAN(client -> client.getWorldType().contains((Object)WorldType.DEADMAN)),
    PVP_ARENA(client -> client.getWorldType().contains((Object)WorldType.PVP_ARENA)),
    TRAILBLAZER_LEAGUE,
    DEADMAN_REBORN,
    SHATTERED_RELICS_LEAGUE(client -> client.getWorldType().contains((Object)WorldType.SEASONAL));

    private final Predicate<Client> test;

    private RuneScapeProfileType() {
        this(client -> false);
    }

    public static RuneScapeProfileType getCurrent(Client client) {
        RuneScapeProfileType[] types = RuneScapeProfileType.values();
        for (int i = types.length - 1; i >= 0; --i) {
            RuneScapeProfileType type = types[i];
            if (!types[i].test.test(client)) continue;
            return type;
        }
        return STANDARD;
    }

    public Predicate<Client> getTest() {
        return this.test;
    }

    private RuneScapeProfileType(Predicate<Client> test) {
        this.test = test;
    }
}

