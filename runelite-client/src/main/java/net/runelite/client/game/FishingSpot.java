/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 */
package net.runelite.client.game;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public enum FishingSpot {
    SHRIMP("Shrimp, Anchovies, Sardine, Herring", "Anchovies", 317, 1514, 1517, 1518, 1521, 1523, 1524, 1525, 1528, 1530, 1544, 3913, 7155, 7459, 7462, 7467, 7469, 7947, 10513),
    LOBSTER("Lobster, Swordfish, Tuna", "Lobster", 377, 1510, 1519, 1522, 3914, 5820, 7199, 7460, 7465, 7470, 7946, 9173, 9174, 10515, 10635),
    SHARK("Shark, Bass", "Shark", 383, 1511, 1520, 3419, 3915, 4476, 4477, 5233, 5234, 5821, 7200, 7461, 7466, 8525, 8526, 8527, 9171, 9172, 10514),
    MONKFISH("Monkfish", 7944, 4316),
    SALMON("Salmon, Trout, Pike", "Salmon", 331, 394, 1506, 1507, 1508, 1509, 1513, 1515, 1516, 1526, 1527, 3417, 3418, 7463, 7464, 7468, 8524),
    LAVA_EEL("Lava eel", 2149, 4928, 6784),
    BARB_FISH("Sturgeon, Salmon, Trout", 11332, 1542, 7323),
    ANGLERFISH("Anglerfish", 13439, 6825),
    MINNOW("Minnow", 21356, 7730, 7731, 7732, 7733),
    HARPOONFISH("Harpoonfish", 25564, 10565, 10568, 10569),
    INFERNAL_EEL("Infernal Eel", 21293, 7676),
    KARAMBWAN("Karambwan", 3142, 4712, 4713),
    KARAMBWANJI("Karambwanji, Shrimp", "Karambwanji", 21394, 4710),
    SACRED_EEL("Sacred eel", 13339, 6488),
    CAVE_EEL("Frog spawn, Cave eel", 5001, 1497, 1498, 1499, 1500),
    SLIMY_EEL("Slimy eel", 3379, 2653, 2654, 2655),
    DARK_CRAB("Dark Crab", 11934, 1535, 1536),
    COMMON_TENCH("Common tench, Bluegill, Greater siren, Mottled eel", "Greater siren", 22829, 8523),
    TUTORIAL_SHRIMP("Shrimp", 317, 3317),
    ETCETERIA_LOBSTER("Lobster", "Lobster (Approval only)", 377, 3657),
    QUEST_RUM_DEAL("Sluglings", "Rum deal (Quest)", 6715, 635),
    QUEST_TAI_BWO_WANNAI_TRIO("Karambwan", "Tai Bwo Wannai Trio (Quest)", 3142, 4714),
    QUEST_FISHING_CONTEST("Giant carp", "Fishing Contest (Quest)", 337, 4079, 4080, 4081, 4082);

    private static final Map<Integer, FishingSpot> SPOTS;
    private final String name;
    private final String worldMapTooltip;
    private final int fishSpriteId;
    private final int[] ids;

    private FishingSpot(String spot, int fishSpriteId, int ... ids) {
        this(spot, spot, fishSpriteId, ids);
    }

    private FishingSpot(String spot, String worldMapTooltip, int fishSpriteId, int ... ids) {
        this.name = spot;
        this.worldMapTooltip = worldMapTooltip;
        this.fishSpriteId = fishSpriteId;
        this.ids = ids;
    }

    public static FishingSpot findSpot(int id) {
        return SPOTS.get(id);
    }

    public String getName() {
        return this.name;
    }

    public String getWorldMapTooltip() {
        return this.worldMapTooltip;
    }

    public int getFishSpriteId() {
        return this.fishSpriteId;
    }

    public int[] getIds() {
        return this.ids;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (FishingSpot spot : FishingSpot.values()) {
            for (int spotId : spot.getIds()) {
                builder.put((Object)spotId, (Object)spot);
            }
        }
        SPOTS = builder.build();
    }
}

