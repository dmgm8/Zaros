/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.TileObject
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.game;

import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;

public enum AgilityShortcut {
    GENERIC_SHORTCUT(1, "Shortcut", null, 3790, 3791, 29993, 30938, 30939, 30940, 30941, 30606, 30198, 12982, 2830, 2831, 5948, 5949, 6673, 16115, 2618, 51, 30959, 30966, 30766, 30767, 30964, 30962, 30961, 2186, 12776, 993, 8729, 31485, 19846, 19847, 18416),
    BRIMHAVEN_DUNGEON_MEDIUM_PIPE_RETURN(1, "Pipe Squeeze", null, new WorldPoint(2698, 9491, 0), 21727),
    BRIMHAVEN_DUNGEON_PIPE_RETURN(1, "Pipe Squeeze", null, new WorldPoint(2655, 9573, 0), 21728),
    BRIMHAVEN_DUNGEON_STEPPING_STONES_RETURN(1, "Pipe Squeeze", null, 21739),
    BRIMHAVEN_DUNGEON_LOG_BALANCE_RETURN(1, "Log Balance", null, 20884),
    AGILITY_PYRAMID_ROCKS_WEST(1, "Rocks", null, 11948),
    CAIRN_ISLE_CLIMBING_ROCKS(1, "Rocks", null, 2236),
    KARAMJA_GLIDER_LOG(1, "Log Balance", new WorldPoint(2906, 3050, 0), 23644),
    FALADOR_CRUMBLING_WALL(5, "Crumbling Wall", new WorldPoint(2936, 3357, 0), 24222),
    RIVER_LUM_GRAPPLE_WEST(8, "Grapple Broken Raft", new WorldPoint(3245, 3179, 0), 17068),
    RIVER_LUM_GRAPPLE_EAST(8, "Grapple Broken Raft", new WorldPoint(3258, 3179, 0), 17068),
    CORSAIR_COVE_ROCKS(10, "Rocks", new WorldPoint(2545, 2871, 0), 31757),
    KARAMJA_MOSS_GIANT_SWING(10, "Rope", null, 23568, 23569),
    FALADOR_GRAPPLE_WALL(11, "Grapple Wall", new WorldPoint(3031, 3391, 0), 17049, 17050),
    BRIMHAVEN_DUNGEON_STEPPING_STONES(12, "Stepping Stones", null, 21738),
    VARROCK_SOUTH_FENCE(13, "Fence", new WorldPoint(3239, 3334, 0), 16518),
    GOBLIN_VILLAGE_WALL(14, "Wall", new WorldPoint(2925, 3523, 0), 16468),
    CORSAIR_COVE_DUNGEON_PILLAR(15, "Pillar Jump", new WorldPoint(1980, 8996, 0), 31809),
    EDGEVILLE_DUNGEON_MONKEYBARS(15, "Monkey Bars", null, 23566),
    TROLLHEIM_ROCKS(15, "Rocks", null, new WorldPoint(2838, 3614, 0), 3748),
    YANILLE_UNDERWALL_TUNNEL(16, "Underwall Tunnel", new WorldPoint(2574, 3109, 0), 16520, 16519),
    KOUREND_CATACOMBS_SOUTH_WEST_CREVICE_NORTH(17, "Crevice", new WorldPoint(1647, 10008, 0), 28892),
    KOUREND_CATACOMBS_SOUTH_WEST_CREVICE_SOUTH(17, "Crevice", new WorldPoint(1645, 10001, 0), 28892),
    CRABCLAW_CAVES_CREVICE(18, "Crevice", new WorldPoint(1710, 9822, 0), 31695, 31696),
    CRABCLAW_CAVES_ROCKS(18, "Rocks", new WorldPoint(1687, 9802, 0), 31697),
    CRABCLAW_CAVES_STEPPING_STONES(18, "Stepping Stones", new WorldPoint(1704, 9800, 0), 31699),
    YANILLE_WATCHTOWER_TRELLIS(18, "Trellis", null, 20056),
    COAL_TRUCKS_LOG_BALANCE(20, "Log Balance", new WorldPoint(2598, 3475, 0), 23274),
    GRAND_EXCHANGE_UNDERWALL_TUNNEL(21, "Underwall Tunnel", new WorldPoint(3139, 3515, 0), 16529, 16530),
    BRIMHAVEN_DUNGEON_PIPE(22, "Pipe Squeeze", new WorldPoint(2654, 9569, 0), 21728),
    OBSERVATORY_SCALE_CLIFF(23, "Grapple Rocks", new WorldPoint(2447, 3155, 0), 31849, 31852),
    EAGLES_PEAK_ROCK_CLIMB(25, "Rock Climb", new WorldPoint(2320, 3499, 0), 19849),
    FALADOR_UNDERWALL_TUNNEL(26, "Underwall Tunnel", new WorldPoint(2947, 3313, 0), 16527, 16528),
    KOUREND_CATACOMBS_PILLAR_JUMP_NORTH(28, "Pillar Jump", new WorldPoint(1613, 10071, 0), new int[0]),
    KOUREND_CATACOMBS_PILLAR_JUMP_SOUTH(28, "Pillar Jump", new WorldPoint(1609, 10060, 0), new int[0]),
    MOUNT_KARUULM_LOWER(29, "Rocks", new WorldPoint(1324, 3782, 0), 34397),
    CORSAIR_COVE_RESOURCE_ROCKS(30, "Rocks", new WorldPoint(2486, 2898, 0), 31758, 31759),
    SOUTHEAST_KARAJMA_STEPPING_STONES(30, "Stepping Stones", new WorldPoint(2924, 2946, 0), 23645, 23646, 23647),
    BRIMHAVEN_DUNGEON_LOG_BALANCE(30, "Log Balance", null, 20882),
    AGILITY_PYRAMID_ROCKS_EAST(30, "Rocks", null, 11949),
    DRAYNOR_MANOR_STEPPING_STONES(31, "Stepping Stones", new WorldPoint(3150, 3362, 0), 16533),
    CATHERBY_CLIFFSIDE_GRAPPLE(32, "Grapple Rock", new WorldPoint(2868, 3429, 0), 17042),
    CAIRN_ISLE_ROCKS(32, "Rocks", null, 2231),
    ARDOUGNE_LOG_BALANCE(33, "Log Balance", new WorldPoint(2602, 3336, 0), 16546, 16547, 16548),
    BRIMHAVEN_DUNGEON_MEDIUM_PIPE(34, "Pipe Squeeze", null, new WorldPoint(2698, 9501, 0), 21727),
    KOUREND_CATACOMBS_NORTH_EAST_CREVICE_NORTH(34, "Crevice", new WorldPoint(1715, 10057, 0), 28892),
    KOUREND_CATACOMBS_NORTH_EAST_CREVICE_SOUTH(34, "Crevice", new WorldPoint(1705, 10077, 0), 28892),
    CATHERBY_OBELISK_GRAPPLE(36, "Grapple Rock", null, 17062),
    GNOME_STRONGHOLD_ROCKS(37, "Rocks", new WorldPoint(2485, 3515, 0), 16534, 16535),
    AL_KHARID_MINING_PITCLIFF_SCRAMBLE(38, "Rocks", new WorldPoint(3305, 3315, 0), 16549, 16550),
    YANILLE_WALL_GRAPPLE(39, "Grapple Wall", new WorldPoint(2552, 3072, 0), 17047),
    NEITIZNOT_BRIDGE_REPAIR(0, "Bridge Repair - Quest", new WorldPoint(2315, 3828, 0), 21306, 21307),
    NEITIZNOT_BRIDGE_SOUTHEAST(0, "Rope Bridge", null, 21308, 21309),
    NEITIZNOT_BRIDGE_NORTHWEST(0, "Rope Bridge", null, 21310, 21311),
    NEITIZNOT_BRIDGE_NORTH(0, "Rope Bridge", null, 21312, 21313),
    NEITIZNOT_BRIDGE_NORTHEAST(40, "Broken Rope bridge", null, 21314, 21315),
    KOUREND_LAKE_JUMP_EAST(40, "Stepping Stones", new WorldPoint(1612, 3570, 0), 29729, 29730),
    KOUREND_LAKE_JUMP_WEST(40, "Stepping Stones", new WorldPoint(1604, 3572, 0), 29729, 29730),
    YANILLE_DUNGEON_BALANCE(40, "Balancing Ledge", null, 23548),
    TROLLHEIM_EASY_CLIFF_SCRAMBLE(41, "Rocks", new WorldPoint(2869, 3670, 0), 16521),
    DWARVEN_MINE_NARROW_CREVICE(42, "Narrow Crevice", new WorldPoint(3034, 9806, 0), 16543),
    DRAYNOR_UNDERWALL_TUNNEL(42, "Underwall Tunnel", new WorldPoint(3068, 3261, 0), 19032, 19036),
    TROLLHEIM_MEDIUM_CLIFF_SCRAMBLE_NORTH(43, "Rocks", new WorldPoint(2886, 3684, 0), 3803, 3804, 16522),
    TROLLHEIM_MEDIUM_CLIFF_SCRAMBLE_SOUTH(43, "Rocks", new WorldPoint(2876, 3666, 0), 3803, 3804, 16522),
    TROLLHEIM_ADVANCED_CLIFF_SCRAMBLE(44, "Rocks", new WorldPoint(2907, 3686, 0), 16523, 3748),
    KOUREND_RIVER_STEPPING_STONES(45, "Stepping Stones", new WorldPoint(1720, 3551, 0), 29728),
    TIRANNWN_LOG_BALANCE(45, "Log Balance", null, 3933, 3931, 3930, 3929, 3932),
    COSMIC_ALTAR_MEDIUM_WALKWAY(46, "Narrow Walkway", new WorldPoint(2399, 4403, 0), 17002),
    DEEP_WILDERNESS_DUNGEON_CREVICE_NORTH(46, "Narrow Crevice", new WorldPoint(3047, 10335, 0), 19043),
    DEEP_WILDERNESS_DUNGEON_CREVICE_SOUTH(46, "Narrow Crevice", new WorldPoint(3045, 10327, 0), 19043),
    TROLLHEIM_HARD_CLIFF_SCRAMBLE(47, "Rocks", new WorldPoint(2902, 3680, 0), 16524),
    FREMENNIK_LOG_BALANCE(48, "Log Balance", new WorldPoint(2721, 3591, 0), 16540, 16541, 16542),
    YANILLE_DUNGEON_PIPE_SQUEEZE(49, "Pipe Squeeze", null, 23140),
    ARCEUUS_ESSENCE_MINE_BOULDER(49, "Boulder", new WorldPoint(1774, 3888, 0), 27990),
    MORYTANIA_STEPPING_STONE(50, "Stepping Stone", new WorldPoint(3418, 3326, 0), 13504),
    VARROCK_SEWERS_PIPE_SQUEEZE(51, "Pipe Squeeze", new WorldPoint(3152, 9905, 0), 16511),
    ARCEUUS_ESSENCE_MINE_EAST_SCRAMBLE(52, "Rock Climb", new WorldPoint(1770, 3851, 0), 27987, 27988),
    KARAMJA_VOLCANO_GRAPPLE_NORTH(53, "Grapple Rock", new WorldPoint(2873, 3143, 0), 17074),
    KARAMJA_VOLCANO_GRAPPLE_SOUTH(53, "Grapple Rock", new WorldPoint(2874, 3128, 0), 17074),
    MOTHERLODE_MINE_WALL_EAST(54, "Wall", new WorldPoint(3124, 9703, 0), 10047),
    MOTHERLODE_MINE_WALL_WEST(54, "Wall", new WorldPoint(3118, 9702, 0), 10047),
    MISCELLANIA_DOCK_STEPPING_STONE(55, "Stepping Stone", new WorldPoint(2572, 3862, 0), 11768),
    ISAFDAR_FOREST_OBSTACLES(56, "Trap", null, 3938, 3939, 3998, 3999, 3937, 3923, 3924, 3925, 3922, 3920, 3921),
    RELEKKA_EAST_FENCE(57, "Fence", new WorldPoint(2688, 3697, 0), 544),
    YANILLE_DUNGEON_MONKEY_BARS(57, "Monkey Bars", null, 23567),
    PHASMATYS_ECTOPOOL_SHORTCUT(58, "Weathered Wall", null, 16525, 16526),
    ELVEN_OVERPASS_CLIFF_SCRAMBLE(59, "Rocks", new WorldPoint(2345, 3300, 0), 16514, 16515),
    ELVEN_OVERPASS_CLIFF_SCRAMBLE_PRIFDDINAS(59, "Rocks", new WorldPoint(3369, 6052, 0), 16514, 16515),
    WILDERNESS_GWD_CLIMB_EAST(60, "Rocks", new WorldPoint(2943, 3770, 0), 26400, 26401, 26402, 26404, 26405, 26406),
    WILDERNESS_GWD_CLIMB_WEST(60, "Rocks", new WorldPoint(2928, 3760, 0), 26400, 26401, 26402, 26404, 26405, 26406),
    MOS_LEHARMLESS_STEPPING_STONE(60, "Stepping Stone", new WorldPoint(3710, 2970, 0), 19042),
    WINTERTODT_GAP(60, "Gap", new WorldPoint(1629, 4023, 0), 29326),
    UNGAEL_ICE(60, "Ice Chunks", null, 25337, 29868, 29869, 29870, 31822, 31823, 31990),
    GWD_LITTLE_CRACK(60, "Little Crack", new WorldPoint(2900, 3712, 0), 26382),
    SLAYER_TOWER_MEDIUM_CHAIN_FIRST(61, "Spiked Chain (Floor 1)", new WorldPoint(3421, 3550, 0), 16537),
    SLAYER_TOWER_MEDIUM_CHAIN_SECOND(61, "Spiked Chain (Floor 2)", new WorldPoint(3420, 3551, 0), 16538),
    SLAYER_DUNGEON_CREVICE(62, "Narrow Crevice", new WorldPoint(2729, 10008, 0), 16539),
    MOUNT_KARUULM_UPPER(62, "Rocks", new WorldPoint(1322, 3791, 0), 34396),
    NECROPOLIS_STEPPING_STONE_NORTH(62, "Stepping Stone", new WorldPoint(3293, 2706, 0), 43990),
    NECROPOLIS_STEPPING_STONES_SOUTH(62, "Stepping Stones", new WorldPoint(3291, 2700, 0), 43989),
    TAVERLEY_DUNGEON_RAILING(63, "Loose Railing", new WorldPoint(2935, 9811, 0), 28849),
    DARKMEYER_WALL(63, "Wall (Long rope)", new WorldPoint(3669, 3375, 0), 39541, 39542),
    TROLLHEIM_WILDERNESS_ROCKS_EAST(64, "Rocks", new WorldPoint(2945, 3678, 0), 16545),
    TROLLHEIM_WILDERNESS_ROCKS_WEST(64, "Rocks", new WorldPoint(2917, 3672, 0), 16545),
    FOSSIL_ISLAND_VOLCANO(64, "Rope", new WorldPoint(3780, 3822, 0), 30916, 30917),
    MORYTANIA_TEMPLE(65, "Loose Railing", new WorldPoint(3422, 3476, 0), 16998, 16999, 16552, 17000),
    REVENANT_CAVES_GREEN_DRAGONS(65, "Jump", new WorldPoint(3220, 10086, 0), 31561),
    COSMIC_ALTAR_ADVANCED_WALKWAY(66, "Narrow Walkway", new WorldPoint(2408, 4401, 0), 17002),
    LUMBRIDGE_DESERT_STEPPING_STONE(66, "Stepping Stone", new WorldPoint(3210, 3135, 0), 16513),
    HEROES_GUILD_TUNNEL_EAST(67, "Crevice", new WorldPoint(2898, 9901, 0), 9739, 9740),
    HEROES_GUILD_TUNNEL_WEST(67, "Crevice", new WorldPoint(2913, 9895, 0), 9739, 9740),
    YANILLE_DUNGEON_RUBBLE_CLIMB(67, "Pile of Rubble", null, 23563, 23564),
    ELVEN_OVERPASS_MEDIUM_CLIFF(68, "Rocks", new WorldPoint(2337, 3288, 0), 16514, 16515),
    ELVEN_OVERPASS_MEDIUM_CLIFF_PRIFDDINAS(68, "Rocks", new WorldPoint(3361, 6040, 0), 16514, 16515),
    WEISS_OBSTACLES(68, "Shortcut", null, 33312, 33184, 33185, 33327, 33328, 33190, 33191, 33192),
    ARCEUUS_ESSENSE_NORTH(69, "Rock Climb", new WorldPoint(1759, 3873, 0), 34741),
    TAVERLEY_DUNGEON_PIPE_BLUE_DRAGON(70, "Pipe Squeeze", new WorldPoint(2886, 9798, 0), 16509),
    TAVERLEY_DUNGEON_ROCKS_NORTH(70, "Rocks", new WorldPoint(2887, 9823, 0), 154, 14106),
    TAVERLEY_DUNGEON_ROCKS_SOUTH(70, "Rocks", new WorldPoint(2887, 9631, 0), 154, 14106),
    FOSSIL_ISLAND_HARDWOOD_NORTH(70, "Hole", new WorldPoint(3712, 3828, 0), 31481, 31482),
    FOSSIL_ISLAND_HARDWOOD_SOUTH(70, "Hole", new WorldPoint(3714, 3816, 0), 31481, 31482),
    AL_KHARID_WINDOW(70, "Window", new WorldPoint(3293, 3158, 0), new int[]{33344, 33348}){

        @Override
        public boolean matches(TileObject object) {
            return object.getId() != 33348 || object.getWorldLocation().equals((Object)new WorldPoint(3295, 3158, 0));
        }
    }
    ,
    GWD_SARADOMIN_ROPE_NORTH(70, "Rope Descent", new WorldPoint(2912, 5300, 0), 26371, 26561),
    GWD_SARADOMIN_ROPE_SOUTH(70, "Rope Descent", new WorldPoint(2951, 5267, 0), 26375, 26562),
    GU_TANOTH_CRUMBLING_WALL(71, "Rocks", new WorldPoint(2545, 3032, 0), 40355, 40356),
    SLAYER_TOWER_ADVANCED_CHAIN_FIRST(71, "Spiked Chain (Floor 2)", new WorldPoint(3447, 3578, 0), 16537),
    SLAYER_TOWER_ADVANCED_CHAIN_SECOND(71, "Spiked Chain (Floor 3)", new WorldPoint(3446, 3576, 0), 16538),
    STRONGHOLD_SLAYER_CAVE_TUNNEL(72, "Tunnel", new WorldPoint(2431, 9806, 0), 30174, 30175),
    TROLL_STRONGHOLD_WALL_CLIMB(73, "Rocks", new WorldPoint(2841, 3694, 0), 16464),
    ARCEUUS_ESSENSE_MINE_WEST(73, "Rock Climb", new WorldPoint(1742, 3853, 0), 27984, 27985),
    LAVA_DRAGON_ISLE_JUMP(74, "Stepping Stone", new WorldPoint(3200, 3807, 0), 14918),
    MEIYERDITCH_LAB_TUNNELS_NORTH(74, "Cave", new WorldPoint(3623, 9811, 0), 43755, 43756),
    MEIYERDITCH_LAB_TUNNELS_SOUTH(74, "Cave", new WorldPoint(3618, 9786, 0), 43757, 43758),
    FORTHOS_DUNGEON_SPIKED_BLADES(75, "Spiked Blades", new WorldPoint(1819, 9946, 0), 34834),
    REVENANT_CAVES_DEMONS_JUMP(75, "Jump", new WorldPoint(3199, 10135, 0), 31561),
    REVENANT_CAVES_ANKOU_EAST(75, "Jump", new WorldPoint(3201, 10195, 0), 31561),
    REVENANT_CAVES_ANKOU_NORTH(75, "Jump", new WorldPoint(3180, 10209, 0), 31561),
    ZUL_ANDRA_ISLAND_CROSSING(76, "Stepping Stone", new WorldPoint(2156, 3073, 0), 10663),
    SHILO_VILLAGE_STEPPING_STONES(77, "Stepping Stones", new WorldPoint(2863, 2974, 0), 16466),
    IORWERTHS_DUNGEON_NORTHERN_SHORTCUT_EAST(78, "Tight Gap", new WorldPoint(3221, 12441, 0), 36692),
    IORWERTHS_DUNGEON_NORTHERN_SHORTCUT_WEST(78, "Tight Gap", new WorldPoint(3215, 12441, 0), 36693),
    KHARAZI_JUNGLE_VINE_CLIMB(79, "Vine", new WorldPoint(2897, 2939, 0), 26884, 26886),
    TAVERLEY_DUNGEON_SPIKED_BLADES(80, "Strange Floor", new WorldPoint(2877, 9813, 0), 16510),
    SLAYER_DUNGEON_CHASM_JUMP(81, "Spiked Blades", new WorldPoint(2770, 10003, 0), 16544),
    LAVA_MAZE_NORTH_JUMP(82, "Stepping Stone", new WorldPoint(3092, 3880, 0), 14917),
    BRIMHAVEN_DUNGEON_EAST_STEPPING_STONES_NORTH(83, "Stepping Stones", new WorldPoint(2685, 9547, 0), 19040),
    BRIMHAVEN_DUNGEON_EAST_STEPPING_STONES_SOUTH(83, "Stepping Stones", new WorldPoint(2693, 9529, 0), 19040),
    IORWERTHS_DUNGEON_SOUTHERN_SHORTCUT_EAST(84, "Tight Gap", new WorldPoint(3241, 12420, 0), 36694),
    IORWERTHS_DUNGEON_SOUTHERN_SHORTCUT_WEST(84, "Tight Gap", new WorldPoint(3231, 12420, 0), 36695),
    ELVEN_ADVANCED_CLIFF_SCRAMBLE(85, "Rocks", new WorldPoint(2337, 3253, 0), 16514, 16515),
    ELVEN_ADVANCED_CLIFF_SCRAMBLE_PRIFDDINAS(85, "Rocks", new WorldPoint(3361, 6005, 0), 16514, 16515),
    KALPHITE_WALL(86, "Crevice", new WorldPoint(3214, 9508, 0), 16465),
    BRIMHAVEN_DUNGEON_VINE_EAST(87, "Vine", new WorldPoint(2672, 9582, 0), 26880, 26882),
    BRIMHAVEN_DUNGEON_VINE_WEST(87, "Vine", new WorldPoint(2606, 9584, 0), 26880, 26882),
    MOUNT_KARUULM_PIPE_SOUTH(88, "Pipe", new WorldPoint(1316, 10214, 0), 34655),
    MOUNT_KARUULM_PIPE_NORTH(88, "Pipe", new WorldPoint(1345, 10230, 0), 34655),
    REVENANT_CAVES_CHAMBER_JUMP(89, "Jump", new WorldPoint(3240, 10144, 0), 31561),
    MEIYERDITCH_LAB_ADVANCED_TUNNELS_WEST(93, "Cave", new WorldPoint(3499, 9802, 0), 43759),
    MEIYERDITCH_LAB_ADVANCED_TUNNELS_MIDDLE(93, "Cave", new WorldPoint(3597, 9768, 0), 43840),
    MEIYERDITCH_LAB_ADVANCED_TUNNELS_EAST(93, "Cave", new WorldPoint(3604, 9772, 0), 43762, 43763);

    private final int level;
    private final String description;
    private final WorldPoint worldMapLocation;
    private final WorldPoint worldLocation;
    private final int[] obstacleIds;

    private AgilityShortcut(int level, String description, WorldPoint mapLocation, WorldPoint worldLocation, int ... obstacleIds) {
        this.level = level;
        this.description = description;
        this.worldMapLocation = mapLocation;
        this.worldLocation = worldLocation;
        this.obstacleIds = obstacleIds;
    }

    private AgilityShortcut(int level, String description, WorldPoint location, int ... obstacleIds) {
        this(level, description, location, location, obstacleIds);
    }

    public String getTooltip() {
        return this.description + " - Level " + this.level;
    }

    public boolean matches(TileObject object) {
        return true;
    }

    public int getLevel() {
        return this.level;
    }

    public String getDescription() {
        return this.description;
    }

    public WorldPoint getWorldMapLocation() {
        return this.worldMapLocation;
    }

    public WorldPoint getWorldLocation() {
        return this.worldLocation;
    }

    public int[] getObstacleIds() {
        return this.obstacleIds;
    }
}

