/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 */
package net.runelite.client.plugins.bosstimer;

import com.google.common.collect.ImmutableMap;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

enum Boss {
    GENERAL_GRAARDOR(2215, 90L, ChronoUnit.SECONDS, 12650),
    KRIL_TSUTSAROTH(3129, 90L, ChronoUnit.SECONDS, 12652),
    KREEARRA(3162, 90L, ChronoUnit.SECONDS, 12649),
    COMMANDER_ZILYANA(2205, 90L, ChronoUnit.SECONDS, 12651),
    CALLISTO(6503, 30L, ChronoUnit.SECONDS, 13178),
    CHAOS_ELEMENTAL(2054, 30L, ChronoUnit.SECONDS, 11995),
    CHAOS_FANATIC(6619, 30L, ChronoUnit.SECONDS, 4675),
    CRAZY_ARCHAEOLOGIST(6618, 30L, ChronoUnit.SECONDS, 11990),
    KING_BLACK_DRAGON(239, 9600L, ChronoUnit.MILLIS, 12653),
    SCORPIA(6615, 9600L, ChronoUnit.MILLIS, 13181),
    VENENATIS(6504, 30L, ChronoUnit.SECONDS, 13177),
    VETION(6612, 30L, ChronoUnit.SECONDS, 13179),
    DAGANNOTH_PRIME(2266, 90L, ChronoUnit.SECONDS, 12644),
    DAGANNOTH_REX(2267, 90L, ChronoUnit.SECONDS, 12645),
    DAGANNOTH_SUPREME(2265, 90L, ChronoUnit.SECONDS, 12643),
    CORPOREAL_BEAST(319, 30L, ChronoUnit.SECONDS, 12816),
    GIANT_MOLE(5779, 9000L, ChronoUnit.MILLIS, 12646),
    DERANGED_ARCHAEOLOGIST(7806, 30L, ChronoUnit.SECONDS, 21566),
    CERBERUS(5862, 8400L, ChronoUnit.MILLIS, 13247),
    THERMONUCLEAR_SMOKE_DEVIL(499, 8400L, ChronoUnit.MILLIS, 12648),
    KRAKEN(494, 8400L, ChronoUnit.MILLIS, 12655),
    KALPHITE_QUEEN(965, 30L, ChronoUnit.SECONDS, 12647),
    DUSK(7889, 5L, ChronoUnit.MINUTES, 21748),
    ALCHEMICAL_HYDRA(8622, 25200L, ChronoUnit.MILLIS, 22746),
    SARACHNIS(8713, 9600L, ChronoUnit.MILLIS, 23495),
    ZALCANO(9050, 21600L, ChronoUnit.MILLIS, 23760);

    private static final Map<Integer, Boss> bosses;
    private final int id;
    private final Duration spawnTime;
    private final int itemSpriteId;

    private Boss(int id, long period, ChronoUnit unit, int itemSpriteId) {
        this.id = id;
        this.spawnTime = Duration.of(period, unit);
        this.itemSpriteId = itemSpriteId;
    }

    public int getId() {
        return this.id;
    }

    public Duration getSpawnTime() {
        return this.spawnTime;
    }

    public int getItemSpriteId() {
        return this.itemSpriteId;
    }

    public static Boss find(int id) {
        return bosses.get(id);
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Boss boss : Boss.values()) {
            builder.put((Object)boss.getId(), (Object)boss);
        }
        bosses = builder.build();
    }
}

