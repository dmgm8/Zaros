/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 */
package net.runelite.client.plugins.itemidentification;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.Predicate;
import net.runelite.client.plugins.itemidentification.ItemIdentificationConfig;

enum ItemIdentification {
    GUAM_SEED(Type.SEED_HERB, "Guam", "G", 5291),
    MARRENTILL_SEED(Type.SEED_HERB, "Marren", "M", 5292),
    TARROMIN_SEED(Type.SEED_HERB, "Tarro", "TAR", 5293),
    HARRALANDER_SEED(Type.SEED_HERB, "Harra", "H", 5294),
    RANARR_SEED(Type.SEED_HERB, "Ranarr", "R", 5295),
    TOADFLAX_SEED(Type.SEED_HERB, "Toad", "TOA", 5296),
    IRIT_SEED(Type.SEED_HERB, "Irit", "I", 5297),
    AVANTOE_SEED(Type.SEED_HERB, "Avan", "A", 5298),
    KWUARM_SEED(Type.SEED_HERB, "Kwuarm", "K", 5299),
    SNAPDRAGON_SEED(Type.SEED_HERB, "Snap", "S", 5300),
    CADANTINE_SEED(Type.SEED_HERB, "Cadan", "C", 5301),
    LANTADYME_SEED(Type.SEED_HERB, "Lanta", "L", 5302),
    DWARF_WEED_SEED(Type.SEED_HERB, "Dwarf", "D", 5303),
    TORSTOL_SEED(Type.SEED_HERB, "Torstol", "TOR", 5304),
    REDBERRY_SEED(Type.SEED_BERRY, "Redberry", "RED", 5101),
    CADAVABERRY_SEED(Type.SEED_BERRY, "Cadava", "CAD", 5102),
    DWELLBERRY_SEED(Type.SEED_BERRY, "Dwell", "DWEL", 5103),
    JANGERBERRY_SEED(Type.SEED_BERRY, "Janger", "JANG", 5104),
    WHITEBERRY_SEED(Type.SEED_BERRY, "White", "WHIT", 5105),
    POISON_IVY_SEED(Type.SEED_BERRY, "Ivy", "POI", 5106),
    GRAPE_SEED(Type.SEED_SPECIAL, "Grape", "GRA", 13657),
    MUSHROOM_SPORE(Type.SEED_SPECIAL, "Mushroom", "MUSH", 5282),
    BELLADONNA_SEED(Type.SEED_SPECIAL, "Bella", "BELL", 5281),
    SEAWEED_SPORE(Type.SEED_SPECIAL, "Seaweed", "SW", 21490),
    HESPORI_SEED(Type.SEED_SPECIAL, "Hespori", "HES", 22875),
    KRONOS_SEED(Type.SEED_SPECIAL, "Kronos", "KRO", 22885),
    IASOR_SEED(Type.SEED_SPECIAL, "Iasor", "IA", 22883),
    ATTAS_SEED(Type.SEED_SPECIAL, "Attas", "AT", 22881),
    CACTUS_SEED(Type.SEED_SPECIAL, "Cactus", "CAC", 5280),
    POTATO_CACTUS_SEED(Type.SEED_SPECIAL, "P.Cact", "P.CAC", 22873),
    ACORN(Type.SEED_TREE, "Oak", "OAK", 5312),
    WILLOW_SEED(Type.SEED_TREE, "Willow", "WIL", 5313),
    MAPLE_SEED(Type.SEED_TREE, "Maple", "MAP", 5314),
    YEW_SEED(Type.SEED_TREE, "Yew", "YEW", 5315),
    MAGIC_SEED(Type.SEED_TREE, "Magic", "MAG", 5316),
    REDWOOD_SEED(Type.SEED_TREE, "Red", "RED", 22871),
    TEAK_SEED(Type.SEED_TREE, "Teak", "TEAK", 21486),
    MAHOGANY_SEED(Type.SEED_TREE, "Mahog", "MAH", 21488),
    CRYSTAL_ACORN(Type.SEED_TREE, "Crystal", "CRY", 23661),
    CELASTRUS_SEED(Type.SEED_TREE, "Celas", "CEL", 22869),
    SPIRIT_SEED(Type.SEED_TREE, "Spirit", "SPI", 5317),
    CALQUAT_SEED(Type.SEED_TREE, "Calquat", "CAL", 5290),
    APPLE_TREE_SEED(Type.SEED_FRUIT_TREE, "Apple", "APP", 5283),
    BANANA_TREE_SEED(Type.SEED_FRUIT_TREE, "Banana", "BAN", 5284),
    ORANGE_TREE_SEED(Type.SEED_FRUIT_TREE, "Orange", "ORA", 5285),
    CURRY_TREE_SEED(Type.SEED_FRUIT_TREE, "Curry", "CUR", 5286),
    PINEAPPLE_SEED(Type.SEED_FRUIT_TREE, "Pine.A", "PINE", 5287),
    PAPAYA_TREE_SEED(Type.SEED_FRUIT_TREE, "Papaya", "PAPA", 5288),
    PALM_TREE_SEED(Type.SEED_FRUIT_TREE, "Palm", "PALM", 5289),
    DRAGONFRIUT_TREE_SEED(Type.SEED_FRUIT_TREE, "Dragon", "DRA", 22877),
    POTATO_SEED(Type.SEED_ALLOTMENT, "Potato", "POT", 5318),
    ONION_SEED(Type.SEED_ALLOTMENT, "Onion", "ONI", 5319),
    CABBAGE_SEED(Type.SEED_ALLOTMENT, "Cabbage", "CAB", 5324),
    TOMATO_SEED(Type.SEED_ALLOTMENT, "Tomato", "TOM", 5322),
    SWEETCORN_SEED(Type.SEED_ALLOTMENT, "S.Corn", "CORN", 5320),
    STRAWBERRY_SEED(Type.SEED_ALLOTMENT, "S.Berry", "STRA", 5323),
    WATERMELON_SEED(Type.SEED_ALLOTMENT, "Melon", "MEL", 5321),
    SNAPE_GRASS_SEED(Type.SEED_ALLOTMENT, "Snape", "SNA", 22879),
    MARIGOLD_SEED(Type.SEED_FLOWER, "Marigo", "MARI", 5096),
    ROSEMARY_SEED(Type.SEED_FLOWER, "Rosemar", "ROSE", 5097),
    NASTURTIUM_SEED(Type.SEED_FLOWER, "Nastur", "NAS", 5098),
    WOAD_SEED(Type.SEED_FLOWER, "Woad", "WOAD", 5099),
    LIMPWURT_SEED(Type.SEED_FLOWER, "Limpwurt", "LIMP", 5100),
    WHITE_LILY_SEED(Type.SEED_FLOWER, "Lily", "LILY", 22887),
    BARLEY_SEED(Type.HOPS_SEED, "Barley", "BAR", 5305),
    HAMMERSTONE_SEED(Type.HOPS_SEED, "Hammer", "HAMM", 5307),
    ASGARNIAN_SEED(Type.HOPS_SEED, "Asgar", "ASG", 5308),
    JUTE_SEED(Type.HOPS_SEED, "Jute", "JUTE", 5306),
    YANILLIAN_SEED(Type.HOPS_SEED, "Yani", "YAN", 5309),
    KRANDORIAN_SEED(Type.HOPS_SEED, "Krand", "KRA", 5310),
    WILDBLOOD_SEED(Type.HOPS_SEED, "Wild.B", "WILD", 5311),
    SACK(Type.SACK, "Empty", "EMP", 5418),
    CABBAGE_SACK(Type.SACK, "Cabbage", "CAB", 5460, 5462, 5464, 5466, 5468, 5470, 5472, 5474, 5476, 5478),
    ONION_SACK(Type.SACK, "Onion", "ONI", 5440, 5442, 5444, 5446, 5448, 5450, 5452, 5454, 5456, 5458),
    POTATO_SACK(Type.SACK, "Potato", "POT", 5420, 5422, 5424, 5426, 5428, 5430, 5432, 5434, 5436, 5438),
    GUAM(Type.HERB, "Guam", "G", 249, 199),
    MARRENTILL(Type.HERB, "Marren", "M", 251, 201),
    TARROMIN(Type.HERB, "Tarro", "TAR", 253, 203),
    HARRALANDER(Type.HERB, "Harra", "H", 255, 205),
    RANARR(Type.HERB, "Ranarr", "R", 257, 207),
    TOADFLAX(Type.HERB, "Toad", "TOA", 2998, 3049),
    IRIT(Type.HERB, "Irit", "I", 259, 209),
    AVANTOE(Type.HERB, "Avan", "A", 261, 211),
    KWUARM(Type.HERB, "Kwuarm", "K", 263, 213),
    SNAPDRAGON(Type.HERB, "Snap", "S", 3000, 3051),
    CADANTINE(Type.HERB, "Cadan", "C", 265, 215),
    LANTADYME(Type.HERB, "Lanta", "L", 2481, 2485),
    DWARF_WEED(Type.HERB, "Dwarf", "D", 267, 217),
    TORSTOL(Type.HERB, "Torstol", "TOR", 269, 219),
    ARDRIGAL(Type.HERB, "Ardrig", "ARD", 1528, 1527),
    ROGUES_PURSE(Type.HERB, "Rogue", "ROG", 1534, 1533),
    SITO_FOIL(Type.HERB, "Sito", "SF", 1530, 1529),
    SNAKE_WEED(Type.HERB, "Snake", "SW", 1526, 1525),
    VOLENCIA_MOSS(Type.HERB, "Volenc", "V", 1532, 1531),
    RED_LOGS(Type.LOGS, "Red", "RED", 7404),
    GREEN_LOGS(Type.LOGS, "Green", "GRE", 7405),
    BLUE_LOGS(Type.LOGS, "Blue", "BLU", 7406),
    WHITE_LOGS(Type.LOGS, "White", "WHI", 10328),
    PURPLE_LOGS(Type.LOGS, "Purple", "PUR", 10329),
    SCRAPEY_TREE_LOGS(Type.LOGS, "Scrapey", "SCRAP", 8934),
    LOG(Type.LOGS, "Log", "LOG", 1511),
    ACHEY_TREE_LOG(Type.LOGS, "Achey", "ACH", 2862),
    OAK_LOG(Type.LOGS, "Oak", "OAK", 1521),
    WILLOW_LOG(Type.LOGS, "Willow", "WIL", 1519),
    TEAK_LOG(Type.LOGS, "Teak", "TEAK", 6333),
    JUNIPER_LOG(Type.LOGS, "Juniper", "JUN", 13355),
    MAPLE_LOG(Type.LOGS, "Maple", "MAP", 1517),
    MAHOGANY_LOG(Type.LOGS, "Mahog", "MAH", 6332),
    ARCTIC_PINE_LOG(Type.LOGS, "Arctic", "ARC", 10810),
    YEW_LOG(Type.LOGS, "Yew", "YEW", 1515),
    BLISTERWOOD_LOG(Type.LOGS, "Blister", "BLI", 24691),
    MAGIC_LOG(Type.LOGS, "Magic", "MAG", 1513),
    REDWOOD_LOG(Type.LOGS, "Red", "RED", 19669),
    PYRE_LOGS(Type.LOGS_PYRE, "Pyre", "P", 3438),
    ARCTIC_PYRE_LOGS(Type.LOGS_PYRE, "Art P", "AP", 10808),
    OAK_PYRE_LOGS(Type.LOGS_PYRE, "Oak P", "OAKP", 3440),
    WILLOW_PYRE_LOGS(Type.LOGS_PYRE, "Wil P", "WILP", 3442),
    TEAK_PYRE_LOGS(Type.LOGS_PYRE, "Teak P", "TEAKP", 6211),
    MAPLE_PYRE_LOGS(Type.LOGS_PYRE, "Map P", "MAPP", 3444),
    MAHOGANY_PYRE_LOGS(Type.LOGS_PYRE, "Mah P", "MAHP", 6213),
    YEW_PYRE_LOGS(Type.LOGS_PYRE, "Yew P", "YEWP", 3446),
    MAGIC_PYRE_LOGS(Type.LOGS_PYRE, "Mag P", "MAGP", 3448),
    REDWOOD_PYRE_LOGS(Type.LOGS_PYRE, "Red P", "REDP", 19672),
    PLANK(Type.PLANK, "Plank", "PLANK", 960),
    OAK_PLANK(Type.PLANK, "Oak", "OAK", 8778),
    TEAK_PLANK(Type.PLANK, "Teak", "TEAK", 8780),
    MAHOGANY_PLANK(Type.PLANK, "Mahog", "MAH", 8782),
    WAXWOOD_PLANK(Type.PLANK, "Wax", "WAX", 24939),
    MALLIGNUM_ROOT_PLANK(Type.PLANK, "Mallig", "MALL", 21036),
    OAK_SAPLING(Type.SAPLING, "Oak", "OAK", 5370, 5358, 5364),
    WILLOW_SAPLING(Type.SAPLING, "Willow", "WIL", 5371, 5359, 5365),
    MAPLE_SAPLING(Type.SAPLING, "Maple", "MAP", 5372, 5360, 5366),
    YEW_SAPLING(Type.SAPLING, "Yew", "YEW", 5373, 5361, 5367),
    MAGIC_SAPLING(Type.SAPLING, "Magic", "MAG", 5374, 5362, 5368),
    REDWOOD_SAPLING(Type.SAPLING, "Red", "RED", 22859, 22850, 22854),
    SPIRIT_SAPLING(Type.SAPLING, "Spirit", "SPI", 5375, 5363, 5369),
    CRYSTAL_SAPLING(Type.SAPLING, "Crystal", "CRY", 23659, 23655, 23657),
    APPLE_SAPLING(Type.SAPLING, "Apple", "APP", 5496, 5480, 5488),
    BANANA_SAPLING(Type.SAPLING, "Banana", "BAN", 5497, 5481, 5489),
    ORANGE_SAPLING(Type.SAPLING, "Orange", "ORA", 5498, 5482, 5490),
    CURRY_SAPLING(Type.SAPLING, "Curry", "CUR", 5499, 5483, 5491),
    PINEAPPLE_SAPLING(Type.SAPLING, "Pine", "PINE", 5500, 5484, 5492),
    PAPAYA_SAPLING(Type.SAPLING, "Papaya", "PAP", 5501, 5485, 5493),
    PALM_SAPLING(Type.SAPLING, "Palm", "PALM", 5502, 5486, 5494),
    DRAGONFRUIT_SAPLING(Type.SAPLING, "Dragon", "DRAG", 22866, 22862, 22864),
    TEAK_SAPLING(Type.SAPLING, "Teak", "TEAK", 21477, 21469, 21473),
    MAHOGANY_SAPLING(Type.SAPLING, "Mahog", "MAHOG", 21480, 21471, 21475),
    CALQUAT_SAPLING(Type.SAPLING, "Calquat", "CALQ", 5503, 5487, 5495),
    CELASTRUS_SAPLING(Type.SAPLING, "Celas", "CEL", 22856, 22848, 22852),
    COMPOST(Type.COMPOST, "Compost", "COM", 6032),
    SUPERCOMPOST(Type.COMPOST, "Sup Com", "SCOM", 6034),
    ULTRACOMPOST(Type.COMPOST, "Ult Com", "UCOM", 21483),
    COPPER_ORE(Type.ORE, "Copper", "COP", 436),
    TIN_ORE(Type.ORE, "Tin", "TIN", 438),
    IRON_ORE(Type.ORE, "Iron", "IRO", 440),
    SILVER_ORE(Type.ORE, "Silver", "SIL", 442),
    COAL_ORE(Type.ORE, "Coal", "COA", 453),
    GOLD_ORE(Type.ORE, "Gold", "GOL", 444),
    MITHRIL_ORE(Type.ORE, "Mithril", "MIT", 447),
    ADAMANTITE_ORE(Type.ORE, "Adaman", "ADA", 449),
    RUNITE_ORE(Type.ORE, "Runite", "RUN", 451),
    RUNE_ESSENCE(Type.ORE, "R.Ess", "R.E.", 1436),
    PURE_ESSENCE(Type.ORE, "P.Ess", "P.E.", 7936),
    PAYDIRT(Type.ORE, "Paydirt", "PAY", 12011),
    AMETHYST(Type.ORE, "Amethy", "AME", 21347),
    LOVAKITE_ORE(Type.ORE, "Lovakit", "LOV", 13356),
    BLURITE_ORE(Type.ORE, "Blurite", "BLU", 668),
    ELEMENTAL_ORE(Type.ORE, "Element", "ELE", 2892),
    DAEYALT_ORE(Type.ORE, "Daeyalt", "DAE", 9632),
    LUNAR_ORE(Type.ORE, "Lunar", "LUN", 9076),
    BRONZE_BAR(Type.BAR, "Bronze", "BRO", 2349),
    IRON_BAR(Type.BAR, "Iron", "IRO", 2351),
    SILVER_BAR(Type.BAR, "Silver", "SIL", 2355),
    STEEL_BAR(Type.BAR, "Steel", "STE", 2353),
    GOLD_BAR(Type.BAR, "Gold", "GOL", 2357),
    MITHRIL_BAR(Type.BAR, "Mithril", "MIT", 2359),
    ADAMANTITE_BAR(Type.BAR, "Adaman", "ADA", 2361),
    RUNITE_BAR(Type.BAR, "Runite", "RUN", 2363),
    SAPPHIRE(Type.GEM, "Sapphir", "S", 1623, 1607),
    EMERALD(Type.GEM, "Emerald", "E", 1621, 1605),
    RUBY(Type.GEM, "Ruby", "R", 1619, 1603),
    DIAMOND(Type.GEM, "Diamon", "DI", 1617, 1601),
    OPAL(Type.GEM, "Opal", "OP", 1625, 1609),
    JADE(Type.GEM, "Jade", "J", 1627, 1611),
    RED_TOPAZ(Type.GEM, "Topaz", "T", 1629, 1613),
    DRAGONSTONE(Type.GEM, "Dragon", "DR", 1631, 1615),
    ONYX(Type.GEM, "Onyx", "ON", 6571, 6573),
    ZENYTE(Type.GEM, "Zenyte", "Z", 19496, 19493),
    SHADOW_DIAMOND(Type.GEM, "Shadow", "SHD", 4673),
    BLOOD_DIAMOND(Type.GEM, "Blood", "BD", 4670),
    ICE_DIAMOND(Type.GEM, "Ice", "ID", 4671),
    SMOKE_DIAMOND(Type.GEM, "Smoke", "SMD", 4672),
    ATTACK(Type.POTION, "Att", "A", 2428, 121, 123, 125),
    STRENGTH(Type.POTION, "Str", "S", 113, 115, 117, 119),
    DEFENCE(Type.POTION, "Def", "D", 2432, 133, 135, 137),
    COMBAT(Type.POTION, "Com", "C", 9739, 9741, 9743, 9745),
    MAGIC(Type.POTION, "Magic", "M", 3040, 3042, 3044, 3046),
    RANGING(Type.POTION, "Range", "R", 2444, 169, 171, 173),
    BASTION(Type.POTION, "Bastion", "B", 22461, 22464, 22467, 22470),
    BATTLEMAGE(Type.POTION, "BatMage", "B.M", 22449, 22452, 22455, 22458),
    SUPER_ATTACK(Type.POTION, "S.Att", "S.A", 2436, 145, 147, 149),
    SUPER_STRENGTH(Type.POTION, "S.Str", "S.S", 2440, 157, 159, 161),
    SUPER_DEFENCE(Type.POTION, "S.Def", "S.D", 2442, 163, 165, 167),
    SUPER_COMBAT(Type.POTION, "S.Com", "S.C", 12695, 12697, 12699, 12701),
    SUPER_RANGING(Type.POTION, "S.Range", "S.Ra", 11722, 11723, 11724, 11725),
    SUPER_MAGIC(Type.POTION, "S.Magic", "S.M", 11726, 11727, 11728, 11729),
    DIVINE_SUPER_ATTACK(Type.POTION, "S.Att", "S.A", 23697, 23700, 23703, 23706),
    DIVINE_SUPER_DEFENCE(Type.POTION, "S.Def", "S.D", 23721, 23724, 23727, 23730),
    DIVINE_SUPER_STRENGTH(Type.POTION, "S.Str", "S.S", 23709, 23712, 23715, 23718),
    DIVINE_SUPER_COMBAT(Type.POTION, "S.Com", "S.C", 23685, 23688, 23691, 23694),
    DIVINE_RANGING(Type.POTION, "Range", "R", 23733, 23736, 23739, 23742),
    DIVINE_MAGIC(Type.POTION, "Magic", "M", 23745, 23748, 23751, 23754),
    DIVINE_BASTION(Type.POTION, "Bastion", "B", 24635, 24638, 24641, 24644),
    DIVINE_BATTLEMAGE(Type.POTION, "BatMage", "B.M", 24623, 24626, 24629, 24632),
    RESTORE(Type.POTION, "Restore", "Re", 2430, 127, 129, 131),
    GUTHIX_BALANCE(Type.POTION, "GuthBal", "G.B.", 7660, 7662, 7664, 7666),
    SUPER_RESTORE(Type.POTION, "S.Rest", "S.Re", 3024, 3026, 3028, 3030),
    PRAYER(Type.POTION, "Prayer", "P", 2434, 139, 141, 143),
    ENERGY(Type.POTION, "Energy", "En", 3008, 3010, 3012, 3014),
    SUPER_ENERGY(Type.POTION, "S.Energ", "S.En", 3016, 3018, 3020, 3022),
    STAMINA(Type.POTION, "Stamina", "St", 12625, 12627, 12629, 12631),
    OVERLOAD(Type.POTION, "Overloa", "OL", 11730, 11731, 11732, 11733),
    ABSORPTION(Type.POTION, "Absorb", "Ab", 11734, 11735, 11736, 11737),
    ZAMORAK_BREW(Type.POTION, "ZammyBr", "Za", 2450, 189, 191, 193),
    SARADOMIN_BREW(Type.POTION, "SaraBr", "Sa", 6685, 6687, 6689, 6691),
    ANCIENT_BREW(Type.POTION, "AncBr", "A.Br", 26340, 26342, 26344, 26346),
    ANTIPOISON(Type.POTION, "AntiP", "AP", 2446, 175, 177, 179),
    SUPERANTIPOISON(Type.POTION, "S.AntiP", "S.AP", 2448, 181, 183, 185),
    ANTIDOTE_P(Type.POTION, "Antid+", "A+", 5943, 5945, 5947, 5949),
    ANTIDOTE_PP(Type.POTION, "Antid++", "A++", 5952, 5954, 5956, 5958),
    ANTIVENOM(Type.POTION, "Anti-V", "AV", 12905, 12907, 12909, 12911),
    ANTIVENOM_P(Type.POTION, "Anti-V+", "AV+", 12913, 12915, 12917, 12919),
    RELICYMS_BALM(Type.POTION, "Relicym", "R.B", 4842, 4844, 4846, 4848),
    SANFEW_SERUM(Type.POTION, "Sanfew", "Sf", 10925, 10927, 10929, 10931),
    ANTIFIRE(Type.POTION, "Antif", "Af", 2452, 2454, 2456, 2458),
    EXTENDED_ANTIFIRE(Type.POTION, "E.Antif", "E.Af", 11951, 11953, 11955, 11957),
    SUPER_ANTIFIRE(Type.POTION, "S.Antif", "S.Af", 21978, 21981, 21984, 21987),
    EXTENDED_SUPER_ANTIFIRE(Type.POTION, "ES.Antif", "ES.Af", 22209, 22212, 22215, 22218),
    SERUM_207(Type.POTION, "Ser207", "S7", 3408, 3410, 3412, 3414),
    SERUM_208(Type.POTION, "Ser208", "S8", 3416, 3417, 3418, 3419),
    COMPOST_POTION(Type.POTION, "Compost", "COM", 6470, 6472, 6474, 6476),
    AGILITY(Type.POTION, "Agility", "Ag", 3032, 3034, 3036, 3038),
    FISHING(Type.POTION, "Fishing", "Fi", 2438, 151, 153, 155),
    HUNTER(Type.POTION, "Hunter", "Hu", 9998, 10000, 10002, 10004),
    GOBLIN(Type.POTION, "Goblin", "G", 26581, 26583, 26585, 26587),
    MAGIC_ESS(Type.POTION, "MagEss", "M.E", 9021, 9022, 9023, 9024),
    REJUVENATION(Type.POTION, "Rejuv", "Rj", 20699, 20700, 20701, 20702),
    GUAM_POTION(Type.POTION, "Guam", "G", 91),
    MARRENTILL_POTION(Type.POTION, "Marren", "M", 93),
    TARROMIN_POTION(Type.POTION, "Tarro", "TAR", 95),
    HARRALANDER_POTION(Type.POTION, "Harra", "H", 97),
    RANARR_POTION(Type.POTION, "Ranarr", "R", 99),
    TOADFLAX_POTION(Type.POTION, "Toad", "TOA", 3002),
    IRIT_POTION(Type.POTION, "Irit", "I", 101),
    AVANTOE_POTION(Type.POTION, "Avan", "A", 103),
    KWUARM_POTION(Type.POTION, "Kwuarm", "K", 105),
    SNAPDRAGON_POTION(Type.POTION, "Snap", "S", 3004),
    CADANTINE_POTION(Type.POTION, "Cadan", "C", 107),
    LANTADYME_POTION(Type.POTION, "Lanta", "L", 2483),
    DWARF_WEED_POTION(Type.POTION, "Dwarf", "D", 109),
    TORSTOL_POTION(Type.POTION, "Torstol", "TOR", 111),
    BABY_IMPLING(Type.IMPLING_JAR, "Baby", "B", 11238),
    YOUNG_IMPLING(Type.IMPLING_JAR, "Young", "Y", 11240),
    GOURMET_IMPLING(Type.IMPLING_JAR, "Gourmet", "G", 11242),
    EARTH_IMPLING(Type.IMPLING_JAR, "Earth", "EAR", 11244),
    ESSENCE_IMPLING(Type.IMPLING_JAR, "Essen", "ESS", 11246),
    ECLECTIC_IMPLING(Type.IMPLING_JAR, "Eclect", "ECL", 11248),
    NATURE_IMPLING(Type.IMPLING_JAR, "Nature", "NAT", 11250),
    MAGPIE_IMPLING(Type.IMPLING_JAR, "Magpie", "M", 11252),
    NINJA_IMPLING(Type.IMPLING_JAR, "Ninja", "NIN", 11254),
    CRYSTAL_IMPLING(Type.IMPLING_JAR, "Crystal", "C", 23768),
    DRAGON_IMPLING(Type.IMPLING_JAR, "Dragon", "D", 11256),
    LUCKY_IMPLING(Type.IMPLING_JAR, "Lucky", "L", 19732),
    VARROCK_TELEPORT(Type.TABLET, "Varro", "VAR", 8007),
    LUMBRIDGE_TELEPORT(Type.TABLET, "Lumbr", "LUM", 8008),
    FALADOR_TELEPORT(Type.TABLET, "Fala", "FAL", 8009),
    CAMELOT_TELEPORT(Type.TABLET, "Cammy", "CAM", 8010),
    ARDOUGNE_TELEPORT(Type.TABLET, "Ardoug", "ARD", 8011),
    WATCHTOWER_TELEPORT(Type.TABLET, "W.tow", "WT", 8012),
    TELEPORT_TO_HOUSE(Type.TABLET, "House", "POH", 8013),
    ENCHANT_SAPPHIRE_OR_OPAL(Type.TABLET, "E.Saph", "E SO", 8016),
    ENCHANT_EMERALD_OR_JADE(Type.TABLET, "E.Emer", "E EJ", 8017),
    ENCHANT_RUBY_OR_TOPAZ(Type.TABLET, "E.Ruby", "E RT", 8018),
    ENCHANT_DIAMOND(Type.TABLET, "E.Diam", "E DIA", 8019),
    ENCHANT_DRAGONSTONE(Type.TABLET, "E.Dstn", "E DS", 8020),
    ENCHANT_ONYX(Type.TABLET, "E.Onyx", "E ONX", 8021),
    TELEKINETIC_GRAB(Type.TABLET, "T.grab", "T.GRB", 8022),
    BONES_TO_PEACHES(Type.TABLET, "Peach", "BtP", 8015),
    BONES_TO_BANANAS(Type.TABLET, "Banana", "BtB", 8014),
    RIMMINGTON_TELEPORT(Type.TABLET, "Rimmi", "RIM", 11741),
    TAVERLEY_TELEPORT(Type.TABLET, "Taver", "TAV", 11742),
    POLLNIVNEACH_TELEPORT(Type.TABLET, "Pollnv", "POL", 11743),
    RELLEKKA_TELEPORT(Type.TABLET, "Rell", "REL", 11744),
    BRIMHAVEN_TELEPORT(Type.TABLET, "Brimh", "BRIM", 11745),
    YANILLE_TELEPORT(Type.TABLET, "Yanille", "YAN", 11746),
    TROLLHEIM_TELEPORT(Type.TABLET, "Trollh", "T.HM", 11747),
    PRIFDDINAS_TELEPORT(Type.TABLET, "Prifd", "PRIF", 23771),
    HOSIDIUS_TELEPORT(Type.TABLET, "Hosid", "HOS", 19651),
    ANNAKARL_TELEPORT(Type.TABLET, "Annak", "GDZ", 12775),
    CARRALLANGAR_TELEPORT(Type.TABLET, "Carra", "CAR", 12776),
    DAREEYAK_TELEPORT(Type.TABLET, "Dareey", "DAR", 12777),
    GHORROCK_TELEPORT(Type.TABLET, "Ghorr", "GHRK", 12778),
    KHARYRLL_TELEPORT(Type.TABLET, "Khary", "KHRL", 12779),
    LASSAR_TELEPORT(Type.TABLET, "Lass", "LSR", 12780),
    PADDEWWA_TELEPORT(Type.TABLET, "Paddew", "PDW", 12781),
    SENNTISTEN_TELEPORT(Type.TABLET, "Sennt", "SNT", 12782),
    ARCEUUS_LIBRARY_TELEPORT(Type.TABLET, "Arc.Lib", "A.LIB", 19613),
    DRAYNOR_MANOR_TELEPORT(Type.TABLET, "D.Manor", "D.MNR", 19615),
    MIND_ALTAR_TELEPORT(Type.TABLET, "M.Altar", "M.ALT", 19617),
    SALVE_GRAVEYARD_TELEPORT(Type.TABLET, "S.Grave", "S.GRV", 19619),
    FENKENSTRAINS_CASTLE_TELEPORT(Type.TABLET, "Fenk", "FNK", 19621),
    WEST_ARDOUGNE_TELEPORT(Type.TABLET, "W.Ardy", "W.ARD", 19623),
    HARMONY_ISLAND_TELEPORT(Type.TABLET, "H.Isle", "HRM", 19625),
    CEMETERY_TELEPORT(Type.TABLET, "Cemet", "CEM", 19627),
    BARROWS_TELEPORT(Type.TABLET, "Barrow", "BAR", 19629),
    APE_ATOLL_TELEPORT(Type.TABLET, "Atoll", "APE", 19631),
    BATTLEFRONT_TELEPORT(Type.TABLET, "B.Front", "BF", 22949),
    MOONCLAN_TELEPORT(Type.TABLET, "Moon", "MOON", 24949),
    OURANIA_TELEPORT(Type.TABLET, "Ourania", "ZMI", 24951),
    WATERBIRTH_TELEPORT(Type.TABLET, "W.Birth", "WAT", 24953),
    BARBARIAN_TELEPORT(Type.TABLET, "Barb", "BARB", 24955),
    KHAZARD_TELEPORT(Type.TABLET, "Khaz", "KHA", 24957),
    FISHING_GUILD_TELEPORT(Type.TABLET, "Fish G.", "FIS", 24959),
    CATHERBY_TELEPORT(Type.TABLET, "Cathy", "CATH", 24961),
    ICE_PLATEAU(Type.TABLET, "Ice Pl.", "ICE", 24963),
    TARGET_TELEPORT(Type.TABLET, "Target", "TRG", 24336),
    VOLCANIC_MINE_TELEPORT(Type.TABLET, "V.Mine", "VM", 21541),
    WILDERNESS_CRABS_TELEPORT(Type.TABLET, "W.Crab", "CRAB", 24251),
    NARDAH_TELEPORT(Type.SCROLL, "Nardah", "NAR", 12402),
    DIGSITE_TELEPORT(Type.SCROLL, "Digsite", "DIG", 12403),
    FELDIP_HILLS_TELEPORT(Type.SCROLL, "F.Hills", "F.H", 12404),
    LUNAR_ISLE_TELEPORT(Type.SCROLL, "L.Isle", "L.I", 12405),
    MORTTON_TELEPORT(Type.SCROLL, "Mort'ton", "MORT", 12406),
    PEST_CONTROL_TELEPORT(Type.SCROLL, "P.Cont", "PEST", 12407),
    PISCATORIS_TELEPORT(Type.SCROLL, "Pisca", "PISC", 12408),
    TAI_BWO_WANNAI_TELEPORT(Type.SCROLL, "TaiBwo", "TAI", 12409),
    IORWERTH_CAMP_TELEPORT(Type.SCROLL, "Iorwerth", "IOR", 12410),
    MOS_LEHARMLESS_TELEPORT(Type.SCROLL, "M.LeHarm", "M.L", 12411),
    LUMBERYARD_TELEPORT(Type.SCROLL, "Lumber", "LUMB", 12642),
    ZUL_ANDRA_TELEPORT(Type.SCROLL, "Zul-andra", "ZUL", 12938),
    KEY_MASTER_TELEPORT(Type.SCROLL, "Key master", "KEY", 13249),
    REVENANT_CAVE_TELEPORT(Type.SCROLL, "Rev cave", "REV", 21802),
    WATSON_TELEPORT(Type.SCROLL, "Watson", "WATS", 23387),
    OPAL_JEWELLERY(Type.JEWELLERY, "Opal", "OP", 21081, 21090, 21117, 21099, 21108),
    JADE_JEWELLERY(Type.JEWELLERY, "Jade", "J", 21084, 21093, 21120, 21102, 21111),
    TOPAZ_JEWELLERY(Type.JEWELLERY, "Topaz", "T", 21087, 21096, 21123, 21105, 21114),
    GOLD_JEWELLERY(Type.JEWELLERY, "Gold", "GOL", 1635, 1654, 11068, 1673, 1692),
    SAPPHIRE_JEWELLERY(Type.JEWELLERY, "Sapphir", "S", 1637, 1656, 11071, 1675, 1694),
    EMERALD_JEWELLERY(Type.JEWELLERY, "Emerald", "E", 1639, 1658, 11076, 1677, 1696),
    RUBY_JEWELLERY(Type.JEWELLERY, "Ruby", "R", 1641, 1660, 11085, 1679, 1698),
    DIAMOND_JEWELLERY(Type.JEWELLERY, "Diamon", "DI", 1643, 1662, 11092, 1681, 1700),
    DRAGONSTONE_JEWELLERY(Type.JEWELLERY, "Dragon", "DR", 1645, 1664, 11115, 1683, 1702),
    ONYX_JEWELLERY(Type.JEWELLERY, "Onyx", "ON", 6575, 6577, 11130, 6579, 6581),
    ZENYTE_JEWELLERY(Type.JEWELLERY, "Zenyte", "Z", 19538, 19535, 19492, 19501, 19541),
    RING_OF_PURSUIT(Type.JEWELLERY_ENCHANTED, "Pursuit", "PUR", 21126),
    DODGY_NECKLACE(Type.JEWELLERY_ENCHANTED, "Dodgy", "DOD", 21143),
    EXPEDITIOUS_BRACELET(Type.JEWELLERY_ENCHANTED, "Exped", "EXP", 21177),
    AMULET_OF_BOUNTY(Type.JEWELLERY_ENCHANTED, "Bounty", "BOU", 21160),
    RING_OF_RETURNING(Type.JEWELLERY_ENCHANTED, "Return", "RET", 21138, 21136, 21134, 21132, 21129),
    NECKLACE_OF_PASSAGE(Type.JEWELLERY_ENCHANTED, "Passage", "PASS", 21155, 21153, 21151, 21149, 21146),
    FLAMTAER_BRACELET(Type.JEWELLERY_ENCHANTED, "Flamta", "FLA", 21180),
    AMULET_OF_CHEMISTRY(Type.JEWELLERY_ENCHANTED, "Chem", "CH", 21163),
    EFARITAYS_AID(Type.JEWELLERY_ENCHANTED, "Efarit", "EFA", 21140),
    NECKLACE_OF_FAITH(Type.JEWELLERY_ENCHANTED, "Faith", "FAI", 21157),
    BRACELET_OF_SLAUGHTER(Type.JEWELLERY_ENCHANTED, "Slaught", "SLA", 21183),
    BURNING_AMULET(Type.JEWELLERY_ENCHANTED, "Burning", "BUR", 21175, 21173, 21171, 21169, 21166),
    RING_OF_RECOIL(Type.JEWELLERY_ENCHANTED, "Recoil", "REC", 2550),
    GAMES_NECKLACE(Type.JEWELLERY_ENCHANTED, "Games", "GAME", 3867, 3865, 3863, 3861, 3859, 3857, 3855, 3853),
    BRACELET_OF_CLAY(Type.JEWELLERY_ENCHANTED, "Clay", "CLA", 11074),
    AMULET_OF_MAGIC(Type.JEWELLERY_ENCHANTED, "Magic", "MAG", 1727),
    RING_OF_DUELING(Type.JEWELLERY_ENCHANTED, "Duel", "DU", 2566, 2564, 2562, 2560, 2558, 2556, 2554, 2552),
    BINDING_NECKLACE(Type.JEWELLERY_ENCHANTED, "Binding", "BIND", 5521),
    CASTLE_WARS_BRACELET(Type.JEWELLERY_ENCHANTED, "Castle", "CAS", 11083, 11081, 11079),
    AMULET_OF_DEFENCE(Type.JEWELLERY_ENCHANTED, "Defence", "DEF", 1729),
    AMULET_OF_NATURE(Type.JEWELLERY_ENCHANTED, "Nature", "NAT", 6040),
    RING_OF_FORGING(Type.JEWELLERY_ENCHANTED, "Forging", "FOR", 2568),
    DIGSITE_PENDANT(Type.JEWELLERY_ENCHANTED, "Digsite", "DIG", 11190, 11191, 11192, 11193, 11194),
    INOCULATION_BRACELET(Type.JEWELLERY_ENCHANTED, "Inocul", "INO", 11088),
    AMULET_OF_STRENGTH(Type.JEWELLERY_ENCHANTED, "Strengt", "STR", 1725),
    RING_OF_LIFE(Type.JEWELLERY_ENCHANTED, "Life", "LI", 2570),
    PHOENIX_NECKLACE(Type.JEWELLERY_ENCHANTED, "Phoenix", "PHO", 11090),
    ABYSSAL_BRACELET(Type.JEWELLERY_ENCHANTED, "Abyss", "ABY", 11103, 11101, 11099, 11097, 11095),
    AMULET_OF_POWER(Type.JEWELLERY_ENCHANTED, "Power", "POW", 1731),
    RING_OF_WEALTH(Type.JEWELLERY_ENCHANTED, "Wealth", "WE", 2572, 11988, 11986, 11984, 11982, 11980),
    SKILLS_NECKLACE(Type.JEWELLERY_ENCHANTED, "Skill", "SK", 11113, 11111, 11109, 11107, 11105, 11970, 11968),
    COMBAT_BRACELET(Type.JEWELLERY_ENCHANTED, "Combat", "COM", 11126, 11124, 11122, 11120, 11118, 11974, 11972),
    AMULET_OF_GLORY(Type.JEWELLERY_ENCHANTED, "Glory", "GLO", 1704, 1706, 1708, 1710, 1712, 11976, 11978, 19707),
    RING_OF_STONE(Type.JEWELLERY_ENCHANTED, "Stone", "STO", 6583),
    BERSERKER_NECKLACE(Type.JEWELLERY_ENCHANTED, "Berserk", "BER", 11128),
    REGEN_BRACELET(Type.JEWELLERY_ENCHANTED, "Regen", "REG", 11133),
    AMULET_OF_FURY(Type.JEWELLERY_ENCHANTED, "Fury", "FU", 6585),
    RING_OF_SUFFERING(Type.JEWELLERY_ENCHANTED, "Suffer", "SUF", 19550),
    NECKLACE_OF_ANGUISH(Type.JEWELLERY_ENCHANTED, "Anguish", "ANG", 19547),
    TORMENTED_BRACELET(Type.JEWELLERY_ENCHANTED, "Torment", "TOR", 19544),
    AMULET_OF_TORTURE(Type.JEWELLERY_ENCHANTED, "Torture", "TOR", 19553),
    OCCULT_NECKLACE(Type.JEWELLERY_ENCHANTED, "Occult", "OCC", 12002),
    DRAGONBONE_NECKLACE(Type.JEWELLERY_ENCHANTED, "Dragon", "DRA", 22111),
    SLAYER_RING(Type.JEWELLERY_ENCHANTED, "Slayer", "SLA", 11873, 11872, 11871, 11870, 11869, 11868, 11867, 11866, 21268);

    final Type type;
    final String medName;
    final String shortName;
    final int[] itemIDs;
    private static final Map<Integer, ItemIdentification> itemIdentifications;

    private ItemIdentification(Type type, String medName, String shortName, int ... ids) {
        this.type = type;
        this.medName = medName;
        this.shortName = shortName;
        this.itemIDs = ids;
    }

    static ItemIdentification get(int id) {
        return itemIdentifications.get(id);
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (ItemIdentification i : ItemIdentification.values()) {
            for (int id : i.itemIDs) {
                builder.put((Object)id, (Object)i);
            }
        }
        itemIdentifications = builder.build();
    }

    static enum Type {
        SEED_HERB(ItemIdentificationConfig::showHerbSeeds),
        SEED_BERRY(ItemIdentificationConfig::showBerrySeeds),
        SEED_ALLOTMENT(ItemIdentificationConfig::showAllotmentSeeds),
        SEED_SPECIAL(ItemIdentificationConfig::showSpecialSeeds),
        SEED_TREE(ItemIdentificationConfig::showTreeSeeds),
        SEED_FRUIT_TREE(ItemIdentificationConfig::showFruitTreeSeeds),
        SEED_FLOWER(ItemIdentificationConfig::showFlowerSeeds),
        HOPS_SEED(ItemIdentificationConfig::showHopsSeeds),
        SACK(ItemIdentificationConfig::showSacks),
        HERB(ItemIdentificationConfig::showHerbs),
        LOGS(ItemIdentificationConfig::showLogs),
        LOGS_PYRE(ItemIdentificationConfig::showPyreLogs),
        PLANK(ItemIdentificationConfig::showPlanks),
        SAPLING(ItemIdentificationConfig::showSaplings),
        COMPOST(ItemIdentificationConfig::showComposts),
        ORE(ItemIdentificationConfig::showOres),
        BAR(ItemIdentificationConfig::showBars),
        GEM(ItemIdentificationConfig::showGems),
        POTION(ItemIdentificationConfig::showPotions),
        IMPLING_JAR(ItemIdentificationConfig::showImplingJars),
        TABLET(ItemIdentificationConfig::showTablets),
        SCROLL(ItemIdentificationConfig::showTeleportScrolls),
        JEWELLERY(ItemIdentificationConfig::showJewellery),
        JEWELLERY_ENCHANTED(ItemIdentificationConfig::showEnchantedJewellery);

        final Predicate<ItemIdentificationConfig> enabled;

        private Type(Predicate<ItemIdentificationConfig> enabled) {
            this.enabled = enabled;
        }
    }
}

