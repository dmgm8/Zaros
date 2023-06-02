/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 */
package net.runelite.client.plugins.mining;

import com.google.common.collect.ImmutableMap;
import java.time.Duration;
import java.util.Map;
import net.runelite.client.util.RSTimeUnit;

enum Rock {
    TIN(Duration.of(4L, RSTimeUnit.GAME_TICKS), 0, 11360, 11361),
    COPPER(Duration.of(4L, RSTimeUnit.GAME_TICKS), 0, 10943, 11161),
    IRON(Duration.of(9L, RSTimeUnit.GAME_TICKS), 0, new int[]{11364, 11365, 36203}){

        @Override
        Duration getRespawnTime(int region) {
            return region == 12183 ? Duration.of(4L, RSTimeUnit.GAME_TICKS) : ((Rock)this).respawnTime;
        }
    }
    ,
    COAL(Duration.of(49L, RSTimeUnit.GAME_TICKS), 0, new int[]{11366, 11367, 36204}){

        @Override
        Duration getRespawnTime(int region) {
            switch (region) {
                case 12183: {
                    return Duration.of(24L, RSTimeUnit.GAME_TICKS);
                }
                case 10044: {
                    return Duration.of(11L, RSTimeUnit.GAME_TICKS);
                }
            }
            return ((Rock)this).respawnTime;
        }
    }
    ,
    SILVER(Duration.of(100L, RSTimeUnit.GAME_TICKS), 0, 11368, 11369, 36205),
    SANDSTONE(Duration.of(9L, RSTimeUnit.GAME_TICKS), 0, 11386),
    GOLD(Duration.of(100L, RSTimeUnit.GAME_TICKS), 0, 11370, 11371, 36206),
    GRANITE(Duration.of(9L, RSTimeUnit.GAME_TICKS), 0, 11387),
    MITHRIL(Duration.of(200L, RSTimeUnit.GAME_TICKS), 0, new int[]{11372, 11373, 36207}){

        @Override
        Duration getRespawnTime(int region) {
            return region == 12183 ? Duration.of(100L, RSTimeUnit.GAME_TICKS) : ((Rock)this).respawnTime;
        }
    }
    ,
    LOVAKITE(Duration.of(65L, RSTimeUnit.GAME_TICKS), 0, 28596, 28597),
    ADAMANTITE(Duration.of(400L, RSTimeUnit.GAME_TICKS), 0, new int[]{11374, 11375, 36208}){

        @Override
        Duration getRespawnTime(int region) {
            return region == 12183 || region == 12605 ? Duration.of(200L, RSTimeUnit.GAME_TICKS) : ((Rock)this).respawnTime;
        }
    }
    ,
    RUNITE(Duration.of(1200L, RSTimeUnit.GAME_TICKS), 0, new int[]{11376, 11377, 36209}){

        @Override
        Duration getRespawnTime(int region) {
            return region == 12183 ? Duration.of(600L, RSTimeUnit.GAME_TICKS) : ((Rock)this).respawnTime;
        }
    }
    ,
    ORE_VEIN(Duration.of(277L, RSTimeUnit.GAME_TICKS), 150, new int[0]),
    AMETHYST(Duration.of(125L, RSTimeUnit.GAME_TICKS), 120, new int[0]),
    ASH_VEIN(Duration.of(50L, RSTimeUnit.GAME_TICKS), 0, 30985),
    GEM_ROCK(Duration.of(99L, RSTimeUnit.GAME_TICKS), 0, 11380, 11381),
    URT_SALT(Duration.of(9L, RSTimeUnit.GAME_TICKS), 0, 33254),
    EFH_SALT(Duration.of(9L, RSTimeUnit.GAME_TICKS), 0, 33255),
    TE_SALT(Duration.of(9L, RSTimeUnit.GAME_TICKS), 0, 33256),
    BASALT(Duration.of(9L, RSTimeUnit.GAME_TICKS), 0, 33257),
    DAEYALT_ESSENCE(Duration.of(110L, RSTimeUnit.GAME_TICKS), 0, 39095),
    BARRONITE(Duration.of(89L, RSTimeUnit.GAME_TICKS), 140, new int[0]),
    MINERAL_VEIN(Duration.of(100L, RSTimeUnit.GAME_TICKS), 150, new int[0]);

    private static final int WILDERNESS_RESOURCE_AREA = 12605;
    private static final int MISCELLANIA = 10044;
    private static final int MINING_GUILD = 12183;
    private static final Map<Integer, Rock> ROCKS;
    private final Duration respawnTime;
    private final int zOffset;
    private final int[] ids;

    private Rock(Duration respawnTime, int zOffset, int ... ids) {
        this.respawnTime = respawnTime;
        this.zOffset = zOffset;
        this.ids = ids;
    }

    Duration getRespawnTime(int region) {
        return this.respawnTime;
    }

    static Rock getRock(int id) {
        return ROCKS.get(id);
    }

    int getZOffset() {
        return this.zOffset;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Rock rock : Rock.values()) {
            for (int id : rock.ids) {
                builder.put((Object)id, (Object)rock);
            }
        }
        ROCKS = builder.build();
    }
}

