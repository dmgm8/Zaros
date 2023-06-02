/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 *  javax.annotation.Nullable
 */
package net.runelite.client.plugins.itemcharges;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.runelite.client.plugins.itemcharges.ItemChargeType;

enum ItemWithCharge {
    ABRACE1(ItemChargeType.ABYSSAL_BRACELET, 11103, 1),
    ABRACE2(ItemChargeType.ABYSSAL_BRACELET, 11101, 2),
    ABRACE3(ItemChargeType.ABYSSAL_BRACELET, 11099, 3),
    ABRACE4(ItemChargeType.ABYSSAL_BRACELET, 11097, 4),
    ABRACE5(ItemChargeType.ABYSSAL_BRACELET, 11095, 5),
    ABSORPTION1(ItemChargeType.POTION, 11737, 1),
    ABSORPTION2(ItemChargeType.POTION, 11736, 2),
    ABSORPTION3(ItemChargeType.POTION, 11735, 3),
    ABSORPTION4(ItemChargeType.POTION, 11734, 4),
    AGILITY1(ItemChargeType.POTION, 3038, 1),
    AGILITY2(ItemChargeType.POTION, 3036, 2),
    AGILITY3(ItemChargeType.POTION, 3034, 3),
    AGILITY4(ItemChargeType.POTION, 3032, 4),
    ANTI1(ItemChargeType.POTION, 179, 1),
    ANTI2(ItemChargeType.POTION, 177, 2),
    ANTI3(ItemChargeType.POTION, 175, 3),
    ANTI4(ItemChargeType.POTION, 2446, 4),
    ANTIDOTE_P1(ItemChargeType.POTION, 5949, 1),
    ANTIDOTE_P2(ItemChargeType.POTION, 5947, 2),
    ANTIDOTE_P3(ItemChargeType.POTION, 5945, 3),
    ANTIDOTE_P4(ItemChargeType.POTION, 5943, 4),
    ANTIDOTE_PP1(ItemChargeType.POTION, 5958, 1),
    ANTIDOTE_PP2(ItemChargeType.POTION, 5956, 2),
    ANTIDOTE_PP3(ItemChargeType.POTION, 5954, 3),
    ANTIDOTE_PP4(ItemChargeType.POTION, 5952, 4),
    ANCIENT_BR1(ItemChargeType.POTION, 26346, 1),
    ANCIENT_BR2(ItemChargeType.POTION, 26344, 2),
    ANCIENT_BR3(ItemChargeType.POTION, 26342, 3),
    ANCIENT_BR4(ItemChargeType.POTION, 26340, 4),
    ANTIFIRE1(ItemChargeType.POTION, 2458, 1),
    ANTIFIRE2(ItemChargeType.POTION, 2456, 2),
    ANTIFIRE3(ItemChargeType.POTION, 2454, 3),
    ANTIFIRE4(ItemChargeType.POTION, 2452, 4),
    ANTIVEN1(ItemChargeType.POTION, 12911, 1),
    ANTIVEN2(ItemChargeType.POTION, 12909, 2),
    ANTIVEN3(ItemChargeType.POTION, 12907, 3),
    ANTIVEN4(ItemChargeType.POTION, 12905, 4),
    ANTIVENOM_P1(ItemChargeType.POTION, 12919, 1),
    ANTIVENOM_P2(ItemChargeType.POTION, 12917, 2),
    ANTIVENOM_P3(ItemChargeType.POTION, 12915, 3),
    ANTIVENOM_P4(ItemChargeType.POTION, 12913, 4),
    ATTACK1(ItemChargeType.POTION, 125, 1),
    ATTACK2(ItemChargeType.POTION, 123, 2),
    ATTACK3(ItemChargeType.POTION, 121, 3),
    ATTACK4(ItemChargeType.POTION, 2428, 4),
    BASKET_APPLES1(ItemChargeType.FRUIT_BASKET, 5378, 1),
    BASKET_APPLES2(ItemChargeType.FRUIT_BASKET, 5380, 2),
    BASKET_APPLES3(ItemChargeType.FRUIT_BASKET, 5382, 3),
    BASKET_APPLES4(ItemChargeType.FRUIT_BASKET, 5384, 4),
    BASKET_APPLES5(ItemChargeType.FRUIT_BASKET, 5386, 5),
    BASKET_BANANAS1(ItemChargeType.FRUIT_BASKET, 5408, 1),
    BASKET_BANANAS2(ItemChargeType.FRUIT_BASKET, 5410, 2),
    BASKET_BANANAS3(ItemChargeType.FRUIT_BASKET, 5412, 3),
    BASKET_BANANAS4(ItemChargeType.FRUIT_BASKET, 5414, 4),
    BASKET_BANANAS5(ItemChargeType.FRUIT_BASKET, 5416, 5),
    BASKET_ORANGES1(ItemChargeType.FRUIT_BASKET, 5388, 1),
    BASKET_ORANGES2(ItemChargeType.FRUIT_BASKET, 5390, 2),
    BASKET_ORANGES3(ItemChargeType.FRUIT_BASKET, 5392, 3),
    BASKET_ORANGES4(ItemChargeType.FRUIT_BASKET, 5394, 4),
    BASKET_ORANGES5(ItemChargeType.FRUIT_BASKET, 5396, 5),
    BASKET_STRAWBERRIES1(ItemChargeType.FRUIT_BASKET, 5398, 1),
    BASKET_STRAWBERRIES2(ItemChargeType.FRUIT_BASKET, 5400, 2),
    BASKET_STRAWBERRIES3(ItemChargeType.FRUIT_BASKET, 5402, 3),
    BASKET_STRAWBERRIES4(ItemChargeType.FRUIT_BASKET, 5404, 4),
    BASKET_STRAWBERRIES5(ItemChargeType.FRUIT_BASKET, 5406, 5),
    BASKET_TOMATOES1(ItemChargeType.FRUIT_BASKET, 5960, 1),
    BASKET_TOMATOES2(ItemChargeType.FRUIT_BASKET, 5962, 2),
    BASKET_TOMATOES3(ItemChargeType.FRUIT_BASKET, 5964, 3),
    BASKET_TOMATOES4(ItemChargeType.FRUIT_BASKET, 5966, 4),
    BASKET_TOMATOES5(ItemChargeType.FRUIT_BASKET, 5968, 5),
    BASTION1(ItemChargeType.POTION, 22470, 1),
    BASTION2(ItemChargeType.POTION, 22467, 2),
    BASTION3(ItemChargeType.POTION, 22464, 3),
    BASTION4(ItemChargeType.POTION, 22461, 4),
    BATTLEMAGE1(ItemChargeType.POTION, 22458, 1),
    BATTLEMAGE2(ItemChargeType.POTION, 22455, 2),
    BATTLEMAGE3(ItemChargeType.POTION, 22452, 3),
    BATTLEMAGE4(ItemChargeType.POTION, 22449, 4),
    BELLOWS0(ItemChargeType.BELLOWS, 2871, 0),
    BELLOWS1(ItemChargeType.BELLOWS, 2874, 1),
    BELLOWS2(ItemChargeType.BELLOWS, 2873, 2),
    BELLOWS3(ItemChargeType.BELLOWS, 2872, 3),
    BLIGHTED_SUPER_REST1(ItemChargeType.POTION, 24605, 1),
    BLIGHTED_SUPER_REST2(ItemChargeType.POTION, 24603, 2),
    BLIGHTED_SUPER_REST3(ItemChargeType.POTION, 24601, 3),
    BLIGHTED_SUPER_REST4(ItemChargeType.POTION, 24598, 4),
    BURNING1(ItemChargeType.TELEPORT, 21175, 1),
    BURNING2(ItemChargeType.TELEPORT, 21173, 2),
    BURNING3(ItemChargeType.TELEPORT, 21171, 3),
    BURNING4(ItemChargeType.TELEPORT, 21169, 4),
    BURNING5(ItemChargeType.TELEPORT, 21166, 5),
    CBRACE1(ItemChargeType.TELEPORT, 11124, 1),
    CBRACE2(ItemChargeType.TELEPORT, 11122, 2),
    CBRACE3(ItemChargeType.TELEPORT, 11120, 3),
    CBRACE4(ItemChargeType.TELEPORT, 11118, 4),
    CBRACE5(ItemChargeType.TELEPORT, 11974, 5),
    CBRACE6(ItemChargeType.TELEPORT, 11972, 6),
    COMBAT1(ItemChargeType.POTION, 9745, 1),
    COMBAT2(ItemChargeType.POTION, 9743, 2),
    COMBAT3(ItemChargeType.POTION, 9741, 3),
    COMBAT4(ItemChargeType.POTION, 9739, 4),
    COMPOST1(ItemChargeType.POTION, 6476, 1),
    COMPOST2(ItemChargeType.POTION, 6474, 2),
    COMPOST3(ItemChargeType.POTION, 6472, 3),
    COMPOST4(ItemChargeType.POTION, 6470, 4),
    DEFENCE1(ItemChargeType.POTION, 137, 1),
    DEFENCE2(ItemChargeType.POTION, 135, 2),
    DEFENCE3(ItemChargeType.POTION, 133, 3),
    DEFENCE4(ItemChargeType.POTION, 2432, 4),
    DIGSITE1(ItemChargeType.TELEPORT, 11190, 1),
    DIGSITE2(ItemChargeType.TELEPORT, 11191, 2),
    DIGSITE3(ItemChargeType.TELEPORT, 11192, 3),
    DIGSITE4(ItemChargeType.TELEPORT, 11193, 4),
    DIGSITE5(ItemChargeType.TELEPORT, 11194, 5),
    DIVINE_BASTION1(ItemChargeType.POTION, 24644, 1),
    DIVINE_BASTION2(ItemChargeType.POTION, 24641, 2),
    DIVINE_BASTION3(ItemChargeType.POTION, 24638, 3),
    DIVINE_BASTION4(ItemChargeType.POTION, 24635, 4),
    DIVINE_BATTLEMAGE1(ItemChargeType.POTION, 24632, 1),
    DIVINE_BATTLEMAGE2(ItemChargeType.POTION, 24629, 2),
    DIVINE_BATTLEMAGE3(ItemChargeType.POTION, 24626, 3),
    DIVINE_BATTLEMAGE4(ItemChargeType.POTION, 24623, 4),
    DIVINE_MAGIC1(ItemChargeType.POTION, 23754, 1),
    DIVINE_MAGIC2(ItemChargeType.POTION, 23751, 2),
    DIVINE_MAGIC3(ItemChargeType.POTION, 23748, 3),
    DIVINE_MAGIC4(ItemChargeType.POTION, 23745, 4),
    DIVINE_RANGING1(ItemChargeType.POTION, 23742, 1),
    DIVINE_RANGING2(ItemChargeType.POTION, 23739, 2),
    DIVINE_RANGING3(ItemChargeType.POTION, 23736, 3),
    DIVINE_RANGING4(ItemChargeType.POTION, 23733, 4),
    DIVINE_SUPER_ATTACK1(ItemChargeType.POTION, 23706, 1),
    DIVINE_SUPER_ATTACK2(ItemChargeType.POTION, 23703, 2),
    DIVINE_SUPER_ATTACK3(ItemChargeType.POTION, 23700, 3),
    DIVINE_SUPER_ATTACK4(ItemChargeType.POTION, 23697, 4),
    DIVINE_SUPER_COMBAT1(ItemChargeType.POTION, 23694, 1),
    DIVINE_SUPER_COMBAT2(ItemChargeType.POTION, 23691, 2),
    DIVINE_SUPER_COMBAT3(ItemChargeType.POTION, 23688, 3),
    DIVINE_SUPER_COMBAT4(ItemChargeType.POTION, 23685, 4),
    DIVINE_SUPER_DEFENCE1(ItemChargeType.POTION, 23730, 1),
    DIVINE_SUPER_DEFENCE2(ItemChargeType.POTION, 23727, 2),
    DIVINE_SUPER_DEFENCE3(ItemChargeType.POTION, 23724, 3),
    DIVINE_SUPER_DEFENCE4(ItemChargeType.POTION, 23721, 4),
    DIVINE_SUPER_STRENGTH1(ItemChargeType.POTION, 23718, 1),
    DIVINE_SUPER_STRENGTH2(ItemChargeType.POTION, 23715, 2),
    DIVINE_SUPER_STRENGTH3(ItemChargeType.POTION, 23712, 3),
    DIVINE_SUPER_STRENGTH4(ItemChargeType.POTION, 23709, 4),
    ELYRE1(ItemChargeType.TELEPORT, 3691, 1),
    ELYRE2(ItemChargeType.TELEPORT, 6125, 2),
    ELYRE3(ItemChargeType.TELEPORT, 6126, 3),
    ELYRE4(ItemChargeType.TELEPORT, 6127, 4),
    ELYRE5(ItemChargeType.TELEPORT, 13079, 5),
    ENERGY1(ItemChargeType.POTION, 3014, 1),
    ENERGY2(ItemChargeType.POTION, 3012, 2),
    ENERGY3(ItemChargeType.POTION, 3010, 3),
    ENERGY4(ItemChargeType.POTION, 3008, 4),
    EXTENDED_ANTIFI1(ItemChargeType.POTION, 11957, 1),
    EXTENDED_ANTIFI2(ItemChargeType.POTION, 11955, 2),
    EXTENDED_ANTIFI3(ItemChargeType.POTION, 11953, 3),
    EXTENDED_ANTIFI4(ItemChargeType.POTION, 11951, 4),
    EXTENDED_SUPER_ANTI1(ItemChargeType.POTION, 22218, 1),
    EXTENDED_SUPER_ANTI2(ItemChargeType.POTION, 22215, 2),
    EXTENDED_SUPER_ANTI3(ItemChargeType.POTION, 22212, 3),
    EXTENDED_SUPER_ANTI4(ItemChargeType.POTION, 22209, 4),
    FISHING1(ItemChargeType.POTION, 155, 1),
    FISHING2(ItemChargeType.POTION, 153, 2),
    FISHING3(ItemChargeType.POTION, 151, 3),
    FISHING4(ItemChargeType.POTION, 2438, 4),
    FUNGICIDE0(ItemChargeType.FUNGICIDE_SPRAY, 7431, 0),
    FUNGICIDE1(ItemChargeType.FUNGICIDE_SPRAY, 7430, 1),
    FUNGICIDE2(ItemChargeType.FUNGICIDE_SPRAY, 7429, 2),
    FUNGICIDE3(ItemChargeType.FUNGICIDE_SPRAY, 7428, 3),
    FUNGICIDE4(ItemChargeType.FUNGICIDE_SPRAY, 7427, 4),
    FUNGICIDE5(ItemChargeType.FUNGICIDE_SPRAY, 7426, 5),
    FUNGICIDE6(ItemChargeType.FUNGICIDE_SPRAY, 7425, 6),
    FUNGICIDE7(ItemChargeType.FUNGICIDE_SPRAY, 7424, 7),
    FUNGICIDE8(ItemChargeType.FUNGICIDE_SPRAY, 7423, 8),
    FUNGICIDE9(ItemChargeType.FUNGICIDE_SPRAY, 7422, 9),
    FUNGICIDE10(ItemChargeType.FUNGICIDE_SPRAY, 7421, 10),
    GAMES1(ItemChargeType.TELEPORT, 3867, 1),
    GAMES2(ItemChargeType.TELEPORT, 3865, 2),
    GAMES3(ItemChargeType.TELEPORT, 3863, 3),
    GAMES4(ItemChargeType.TELEPORT, 3861, 4),
    GAMES5(ItemChargeType.TELEPORT, 3859, 5),
    GAMES6(ItemChargeType.TELEPORT, 3857, 6),
    GAMES7(ItemChargeType.TELEPORT, 3855, 7),
    GAMES8(ItemChargeType.TELEPORT, 3853, 8),
    GLORY1(ItemChargeType.TELEPORT, 1706, 1),
    GLORY2(ItemChargeType.TELEPORT, 1708, 2),
    GLORY3(ItemChargeType.TELEPORT, 1710, 3),
    GLORY4(ItemChargeType.TELEPORT, 1712, 4),
    GLORY5(ItemChargeType.TELEPORT, 11976, 5),
    GLORY6(ItemChargeType.TELEPORT, 11978, 6),
    GLORYT1(ItemChargeType.TELEPORT, 10360, 1),
    GLORYT2(ItemChargeType.TELEPORT, 10358, 2),
    GLORYT3(ItemChargeType.TELEPORT, 10356, 3),
    GLORYT4(ItemChargeType.TELEPORT, 10354, 4),
    GLORYT5(ItemChargeType.TELEPORT, 11966, 5),
    GLORYT6(ItemChargeType.TELEPORT, 11964, 6),
    GREST1(ItemChargeType.GUTHIX_REST, 4423, 1),
    GREST2(ItemChargeType.GUTHIX_REST, 4421, 2),
    GREST3(ItemChargeType.GUTHIX_REST, 4419, 3),
    GREST4(ItemChargeType.GUTHIX_REST, 4417, 4),
    GUTHIX_BAL1(ItemChargeType.POTION, 7666, 1),
    GUTHIX_BAL2(ItemChargeType.POTION, 7664, 2),
    GUTHIX_BAL3(ItemChargeType.POTION, 7662, 3),
    GUTHIX_BAL4(ItemChargeType.POTION, 7660, 4),
    HUNTER1(ItemChargeType.POTION, 10004, 1),
    HUNTER2(ItemChargeType.POTION, 10002, 2),
    HUNTER3(ItemChargeType.POTION, 10000, 3),
    HUNTER4(ItemChargeType.POTION, 9998, 4),
    IMP_IN_A_BOX1(ItemChargeType.IMPBOX, 10028, 1),
    IMP_IN_A_BOX2(ItemChargeType.IMPBOX, 10027, 2),
    MAGIC1(ItemChargeType.POTION, 3046, 1),
    MAGIC2(ItemChargeType.POTION, 3044, 2),
    MAGIC3(ItemChargeType.POTION, 3042, 3),
    MAGIC4(ItemChargeType.POTION, 3040, 4),
    MAGIC_ESS1(ItemChargeType.POTION, 9024, 1),
    MAGIC_ESS2(ItemChargeType.POTION, 9023, 2),
    MAGIC_ESS3(ItemChargeType.POTION, 9022, 3),
    MAGIC_ESS4(ItemChargeType.POTION, 9021, 4),
    OVERLOAD1(ItemChargeType.POTION, 11733, 1),
    OVERLOAD2(ItemChargeType.POTION, 11732, 2),
    OVERLOAD3(ItemChargeType.POTION, 11731, 3),
    OVERLOAD4(ItemChargeType.POTION, 11730, 4),
    PASSAGE1(ItemChargeType.TELEPORT, 21155, 1),
    PASSAGE2(ItemChargeType.TELEPORT, 21153, 2),
    PASSAGE3(ItemChargeType.TELEPORT, 21151, 3),
    PASSAGE4(ItemChargeType.TELEPORT, 21149, 4),
    PASSAGE5(ItemChargeType.TELEPORT, 21146, 5),
    PRAYER1(ItemChargeType.POTION, 143, 1),
    PRAYER2(ItemChargeType.POTION, 141, 2),
    PRAYER3(ItemChargeType.POTION, 139, 3),
    PRAYER4(ItemChargeType.POTION, 2434, 4),
    RANGING1(ItemChargeType.POTION, 173, 1),
    RANGING2(ItemChargeType.POTION, 171, 2),
    RANGING3(ItemChargeType.POTION, 169, 3),
    RANGING4(ItemChargeType.POTION, 2444, 4),
    RELICYMS1(ItemChargeType.POTION, 4848, 1),
    RELICYMS2(ItemChargeType.POTION, 4846, 2),
    RELICYMS3(ItemChargeType.POTION, 4844, 3),
    RELICYMS4(ItemChargeType.POTION, 4842, 4),
    RESTORE1(ItemChargeType.POTION, 131, 1),
    RESTORE2(ItemChargeType.POTION, 129, 2),
    RESTORE3(ItemChargeType.POTION, 127, 3),
    RESTORE4(ItemChargeType.POTION, 2430, 4),
    RETURNING1(ItemChargeType.TELEPORT, 21138, 1),
    RETURNING2(ItemChargeType.TELEPORT, 21136, 2),
    RETURNING3(ItemChargeType.TELEPORT, 21134, 3),
    RETURNING4(ItemChargeType.TELEPORT, 21132, 4),
    RETURNING5(ItemChargeType.TELEPORT, 21129, 5),
    ROD1(ItemChargeType.TELEPORT, 2566, 1),
    ROD2(ItemChargeType.TELEPORT, 2564, 2),
    ROD3(ItemChargeType.TELEPORT, 2562, 3),
    ROD4(ItemChargeType.TELEPORT, 2560, 4),
    ROD5(ItemChargeType.TELEPORT, 2558, 5),
    ROD6(ItemChargeType.TELEPORT, 2556, 6),
    ROD7(ItemChargeType.TELEPORT, 2554, 7),
    ROD8(ItemChargeType.TELEPORT, 2552, 8),
    ROS1(ItemChargeType.TELEPORT, 11873, 1),
    ROS2(ItemChargeType.TELEPORT, 11872, 2),
    ROS3(ItemChargeType.TELEPORT, 11871, 3),
    ROS4(ItemChargeType.TELEPORT, 11870, 4),
    ROS5(ItemChargeType.TELEPORT, 11869, 5),
    ROS6(ItemChargeType.TELEPORT, 11868, 6),
    ROS7(ItemChargeType.TELEPORT, 11867, 7),
    ROS8(ItemChargeType.TELEPORT, 11866, 8),
    ROW1(ItemChargeType.TELEPORT, 11988, 1),
    ROW2(ItemChargeType.TELEPORT, 11986, 2),
    ROW3(ItemChargeType.TELEPORT, 11984, 3),
    ROW4(ItemChargeType.TELEPORT, 11982, 4),
    ROW5(ItemChargeType.TELEPORT, 11980, 5),
    ROWI1(ItemChargeType.TELEPORT, 20790, 1),
    ROWI2(ItemChargeType.TELEPORT, 20789, 2),
    ROWI3(ItemChargeType.TELEPORT, 20788, 3),
    ROWI4(ItemChargeType.TELEPORT, 20787, 4),
    ROWI5(ItemChargeType.TELEPORT, 20786, 5),
    SACK_CABBAGES1(ItemChargeType.SACK, 5460, 1),
    SACK_CABBAGES2(ItemChargeType.SACK, 5462, 2),
    SACK_CABBAGES3(ItemChargeType.SACK, 5464, 3),
    SACK_CABBAGES4(ItemChargeType.SACK, 5466, 4),
    SACK_CABBAGES5(ItemChargeType.SACK, 5468, 5),
    SACK_CABBAGES6(ItemChargeType.SACK, 5470, 6),
    SACK_CABBAGES7(ItemChargeType.SACK, 5472, 7),
    SACK_CABBAGES8(ItemChargeType.SACK, 5474, 8),
    SACK_CABBAGES9(ItemChargeType.SACK, 5476, 9),
    SACK_CABBAGES10(ItemChargeType.SACK, 5478, 10),
    SACK_ONIONS1(ItemChargeType.SACK, 5440, 1),
    SACK_ONIONS2(ItemChargeType.SACK, 5442, 2),
    SACK_ONIONS3(ItemChargeType.SACK, 5444, 3),
    SACK_ONIONS4(ItemChargeType.SACK, 5446, 4),
    SACK_ONIONS5(ItemChargeType.SACK, 5448, 5),
    SACK_ONIONS6(ItemChargeType.SACK, 5450, 6),
    SACK_ONIONS7(ItemChargeType.SACK, 5452, 7),
    SACK_ONIONS8(ItemChargeType.SACK, 5454, 8),
    SACK_ONIONS9(ItemChargeType.SACK, 5456, 9),
    SACK_ONIONS10(ItemChargeType.SACK, 5458, 10),
    SACK_POTATOES1(ItemChargeType.SACK, 5420, 1),
    SACK_POTATOES2(ItemChargeType.SACK, 5422, 2),
    SACK_POTATOES3(ItemChargeType.SACK, 5424, 3),
    SACK_POTATOES4(ItemChargeType.SACK, 5426, 4),
    SACK_POTATOES5(ItemChargeType.SACK, 5428, 5),
    SACK_POTATOES6(ItemChargeType.SACK, 5430, 6),
    SACK_POTATOES7(ItemChargeType.SACK, 5432, 7),
    SACK_POTATOES8(ItemChargeType.SACK, 5434, 8),
    SACK_POTATOES9(ItemChargeType.SACK, 5436, 9),
    SACK_POTATOES10(ItemChargeType.SACK, 5438, 10),
    SANFEW1(ItemChargeType.POTION, 10931, 1),
    SANFEW2(ItemChargeType.POTION, 10929, 2),
    SANFEW3(ItemChargeType.POTION, 10927, 3),
    SANFEW4(ItemChargeType.POTION, 10925, 4),
    SARADOMIN_BR1(ItemChargeType.POTION, 6691, 1),
    SARADOMIN_BR2(ItemChargeType.POTION, 6689, 2),
    SARADOMIN_BR3(ItemChargeType.POTION, 6687, 3),
    SARADOMIN_BR4(ItemChargeType.POTION, 6685, 4),
    SERUM_2071(ItemChargeType.POTION, 3414, 1),
    SERUM_2072(ItemChargeType.POTION, 3412, 2),
    SERUM_2073(ItemChargeType.POTION, 3410, 3),
    SERUM_2074(ItemChargeType.POTION, 3408, 4),
    SERUM_2081(ItemChargeType.POTION, 3419, 1),
    SERUM_2082(ItemChargeType.POTION, 3418, 2),
    SERUM_2083(ItemChargeType.POTION, 3417, 3),
    SERUM_2084(ItemChargeType.POTION, 3416, 4),
    SKILLS1(ItemChargeType.TELEPORT, 11111, 1),
    SKILLS2(ItemChargeType.TELEPORT, 11109, 2),
    SKILLS3(ItemChargeType.TELEPORT, 11107, 3),
    SKILLS4(ItemChargeType.TELEPORT, 11105, 4),
    SKILLS5(ItemChargeType.TELEPORT, 11970, 5),
    SKILLS6(ItemChargeType.TELEPORT, 11968, 6),
    STAMINA1(ItemChargeType.POTION, 12631, 1),
    STAMINA2(ItemChargeType.POTION, 12629, 2),
    STAMINA3(ItemChargeType.POTION, 12627, 3),
    STAMINA4(ItemChargeType.POTION, 12625, 4),
    STRENGTH1(ItemChargeType.POTION, 119, 1),
    STRENGTH2(ItemChargeType.POTION, 117, 2),
    STRENGTH3(ItemChargeType.POTION, 115, 3),
    STRENGTH4(ItemChargeType.POTION, 113, 4),
    SUPERANTI1(ItemChargeType.POTION, 185, 1),
    SUPERANTI2(ItemChargeType.POTION, 183, 2),
    SUPERANTI3(ItemChargeType.POTION, 181, 3),
    SUPERANTI4(ItemChargeType.POTION, 2448, 4),
    SUPER_ANTIFIRE1(ItemChargeType.POTION, 21987, 1),
    SUPER_ANTIFIRE2(ItemChargeType.POTION, 21984, 2),
    SUPER_ANTIFIRE3(ItemChargeType.POTION, 21981, 3),
    SUPER_ANTIFIRE4(ItemChargeType.POTION, 21978, 4),
    SUPER_ATT1(ItemChargeType.POTION, 149, 1),
    SUPER_ATT2(ItemChargeType.POTION, 147, 2),
    SUPER_ATT3(ItemChargeType.POTION, 145, 3),
    SUPER_ATT4(ItemChargeType.POTION, 2436, 4),
    SUPER_COMB1(ItemChargeType.POTION, 12701, 1),
    SUPER_COMB2(ItemChargeType.POTION, 12699, 2),
    SUPER_COMB3(ItemChargeType.POTION, 12697, 3),
    SUPER_COMB4(ItemChargeType.POTION, 12695, 4),
    SUPER_DEF1(ItemChargeType.POTION, 167, 1),
    SUPER_DEF2(ItemChargeType.POTION, 165, 2),
    SUPER_DEF3(ItemChargeType.POTION, 163, 3),
    SUPER_DEF4(ItemChargeType.POTION, 2442, 4),
    SUPER_ENERG1(ItemChargeType.POTION, 3022, 1),
    SUPER_ENERG2(ItemChargeType.POTION, 3020, 2),
    SUPER_ENERG3(ItemChargeType.POTION, 3018, 3),
    SUPER_ENERG4(ItemChargeType.POTION, 3016, 4),
    SUPER_MAG1(ItemChargeType.POTION, 11729, 1),
    SUPER_MAG2(ItemChargeType.POTION, 11728, 2),
    SUPER_MAG3(ItemChargeType.POTION, 11727, 3),
    SUPER_MAG4(ItemChargeType.POTION, 11726, 4),
    SUPER_RANG1(ItemChargeType.POTION, 11725, 1),
    SUPER_RANG2(ItemChargeType.POTION, 11724, 2),
    SUPER_RANG3(ItemChargeType.POTION, 11723, 3),
    SUPER_RANG4(ItemChargeType.POTION, 11722, 4),
    SUPER_REST1(ItemChargeType.POTION, 3030, 1),
    SUPER_REST2(ItemChargeType.POTION, 3028, 2),
    SUPER_REST3(ItemChargeType.POTION, 3026, 3),
    SUPER_REST4(ItemChargeType.POTION, 3024, 4),
    SUPER_STR1(ItemChargeType.POTION, 161, 1),
    SUPER_STR2(ItemChargeType.POTION, 159, 2),
    SUPER_STR3(ItemChargeType.POTION, 157, 3),
    SUPER_STR4(ItemChargeType.POTION, 2440, 4),
    TCRYSTAL1(ItemChargeType.TELEPORT, 6102, 1),
    TCRYSTAL2(ItemChargeType.TELEPORT, 6101, 2),
    TCRYSTAL3(ItemChargeType.TELEPORT, 6100, 3),
    TCRYSTAL4(ItemChargeType.TELEPORT, 6099, 4),
    TCRYSTAL5(ItemChargeType.TELEPORT, 13102, 5),
    WCAN0(ItemChargeType.WATERCAN, 5331, 0),
    WCAN1(ItemChargeType.WATERCAN, 5333, 1),
    WCAN2(ItemChargeType.WATERCAN, 5334, 2),
    WCAN3(ItemChargeType.WATERCAN, 5335, 3),
    WCAN4(ItemChargeType.WATERCAN, 5336, 4),
    WCAN5(ItemChargeType.WATERCAN, 5337, 5),
    WCAN6(ItemChargeType.WATERCAN, 5338, 6),
    WCAN7(ItemChargeType.WATERCAN, 5339, 7),
    WCAN8(ItemChargeType.WATERCAN, 5340, 8),
    WSKIN0(ItemChargeType.WATERSKIN, 1831, 0),
    WSKIN1(ItemChargeType.WATERSKIN, 1829, 1),
    WSKIN2(ItemChargeType.WATERSKIN, 1827, 2),
    WSKIN3(ItemChargeType.WATERSKIN, 1825, 3),
    WSKIN4(ItemChargeType.WATERSKIN, 1823, 4),
    ZAMORAK_BR1(ItemChargeType.POTION, 193, 1),
    ZAMORAK_BR2(ItemChargeType.POTION, 191, 2),
    ZAMORAK_BR3(ItemChargeType.POTION, 189, 3),
    ZAMORAK_BR4(ItemChargeType.POTION, 2450, 4),
    ELDER_MIN1(ItemChargeType.POTION, 20913, 1),
    ELDER_MIN2(ItemChargeType.POTION, 20914, 2),
    ELDER_MIN3(ItemChargeType.POTION, 20915, 3),
    ELDER_MIN4(ItemChargeType.POTION, 20916, 4),
    ELDER1(ItemChargeType.POTION, 20917, 1),
    ELDER2(ItemChargeType.POTION, 20918, 2),
    ELDER3(ItemChargeType.POTION, 20919, 3),
    ELDER4(ItemChargeType.POTION, 20920, 4),
    ELDER_MAX1(ItemChargeType.POTION, 20921, 1),
    ELDER_MAX2(ItemChargeType.POTION, 20922, 2),
    ELDER_MAX3(ItemChargeType.POTION, 20923, 3),
    ELDER_MAX4(ItemChargeType.POTION, 20924, 4),
    KODAI_MIN1(ItemChargeType.POTION, 20937, 1),
    KODAI_MIN2(ItemChargeType.POTION, 20938, 2),
    KODAI_MIN3(ItemChargeType.POTION, 20939, 3),
    KODAI_MIN4(ItemChargeType.POTION, 20940, 4),
    KODAI1(ItemChargeType.POTION, 20941, 1),
    KODAI2(ItemChargeType.POTION, 20942, 2),
    KODAI3(ItemChargeType.POTION, 20943, 3),
    KODAI4(ItemChargeType.POTION, 20944, 4),
    KODAI_MAX1(ItemChargeType.POTION, 20945, 1),
    KODAI_MAX2(ItemChargeType.POTION, 20946, 2),
    KODAI_MAX3(ItemChargeType.POTION, 20947, 3),
    KODAI_MAX4(ItemChargeType.POTION, 20948, 4),
    TWISTED_MIN1(ItemChargeType.POTION, 20925, 1),
    TWISTED_MIN2(ItemChargeType.POTION, 20926, 2),
    TWISTED_MIN3(ItemChargeType.POTION, 20927, 3),
    TWISTED_MIN4(ItemChargeType.POTION, 20928, 4),
    TWISTED1(ItemChargeType.POTION, 20929, 1),
    TWISTED2(ItemChargeType.POTION, 20930, 2),
    TWISTED3(ItemChargeType.POTION, 20931, 3),
    TWISTED4(ItemChargeType.POTION, 20932, 4),
    TWISTED_MAX1(ItemChargeType.POTION, 20933, 1),
    TWISTED_MAX2(ItemChargeType.POTION, 20934, 2),
    TWISTED_MAX3(ItemChargeType.POTION, 20935, 3),
    TWISTED_MAX4(ItemChargeType.POTION, 20936, 4),
    REVITALISATION_MIN1(ItemChargeType.POTION, 20949, 1),
    REVITALISATION_MIN2(ItemChargeType.POTION, 20950, 2),
    REVITALISATION_MIN3(ItemChargeType.POTION, 20951, 3),
    REVITALISATION_MIN4(ItemChargeType.POTION, 20952, 4),
    REVITALISATION1(ItemChargeType.POTION, 20953, 1),
    REVITALISATION2(ItemChargeType.POTION, 20954, 2),
    REVITALISATION3(ItemChargeType.POTION, 20955, 3),
    REVITALISATION4(ItemChargeType.POTION, 20956, 4),
    REVITALISATION_MAX1(ItemChargeType.POTION, 20957, 1),
    REVITALISATION_MAX2(ItemChargeType.POTION, 20958, 2),
    REVITALISATION_MAX3(ItemChargeType.POTION, 20959, 3),
    REVITALISATION_MAX4(ItemChargeType.POTION, 20960, 4),
    XERICS_AID_MIN1(ItemChargeType.POTION, 20973, 1),
    XERICS_AID_MIN2(ItemChargeType.POTION, 20974, 2),
    XERICS_AID_MIN3(ItemChargeType.POTION, 20975, 3),
    XERICS_AID_MIN4(ItemChargeType.POTION, 20976, 4),
    XERICS_AID1(ItemChargeType.POTION, 20977, 1),
    XERICS_AID2(ItemChargeType.POTION, 20978, 2),
    XERICS_AID3(ItemChargeType.POTION, 20979, 3),
    XERICS_AID4(ItemChargeType.POTION, 20980, 4),
    XERICS_AID_MAX1(ItemChargeType.POTION, 20981, 1),
    XERICS_AID_MAX2(ItemChargeType.POTION, 20982, 2),
    XERICS_AID_MAX3(ItemChargeType.POTION, 20983, 3),
    XERICS_AID_MAX4(ItemChargeType.POTION, 20984, 4),
    PRAYER_ENHANCE_MIN1(ItemChargeType.POTION, 20961, 1),
    PRAYER_ENHANCE_MIN2(ItemChargeType.POTION, 20962, 2),
    PRAYER_ENHANCE_MIN3(ItemChargeType.POTION, 20963, 3),
    PRAYER_ENHANCE_MIN4(ItemChargeType.POTION, 20964, 4),
    PRAYER_ENHANCE1(ItemChargeType.POTION, 20965, 1),
    PRAYER_ENHANCE2(ItemChargeType.POTION, 20966, 2),
    PRAYER_ENHANCE3(ItemChargeType.POTION, 20967, 3),
    PRAYER_ENHANCE4(ItemChargeType.POTION, 20968, 4),
    PRAYER_ENHANCE_MAX1(ItemChargeType.POTION, 20969, 1),
    PRAYER_ENHANCE_MAX2(ItemChargeType.POTION, 20970, 2),
    PRAYER_ENHANCE_MAX3(ItemChargeType.POTION, 20971, 3),
    PRAYER_ENHANCE_MAX4(ItemChargeType.POTION, 20972, 4),
    COX_OVERLOAD_MIN1(ItemChargeType.POTION, 20985, 1),
    COX_OVERLOAD_MIN2(ItemChargeType.POTION, 20986, 2),
    COX_OVERLOAD_MIN3(ItemChargeType.POTION, 20987, 3),
    COX_OVERLOAD_MIN4(ItemChargeType.POTION, 20988, 4),
    COX_OVERLOAD1(ItemChargeType.POTION, 20989, 1),
    COX_OVERLOAD2(ItemChargeType.POTION, 20990, 2),
    COX_OVERLOAD3(ItemChargeType.POTION, 20991, 3),
    COX_OVERLOAD4(ItemChargeType.POTION, 20992, 4),
    COX_OVERLOAD_MAX1(ItemChargeType.POTION, 20993, 1),
    COX_OVERLOAD_MAX2(ItemChargeType.POTION, 20994, 2),
    COX_OVERLOAD_MAX3(ItemChargeType.POTION, 20995, 3),
    COX_OVERLOAD_MAX4(ItemChargeType.POTION, 20996, 4);

    private final ItemChargeType type;
    private final int id;
    private final int charges;
    private static final Map<Integer, ItemWithCharge> ID_MAP;

    @Nullable
    static ItemWithCharge findItem(int itemId) {
        return ID_MAP.get(itemId);
    }

    private ItemWithCharge(ItemChargeType type, int id, int charges) {
        this.type = type;
        this.id = id;
        this.charges = charges;
    }

    public ItemChargeType getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public int getCharges() {
        return this.charges;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (ItemWithCharge itemCharge : ItemWithCharge.values()) {
            builder.put((Object)itemCharge.getId(), (Object)itemCharge);
        }
        ID_MAP = builder.build();
    }
}

