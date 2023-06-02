/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  lombok.NonNull
 *  net.runelite.api.Client
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.cluescrolls.clues;

import com.google.common.collect.ImmutableMap;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.NonNull;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.Enemy;
import net.runelite.client.plugins.cluescrolls.clues.LocationClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.TextClueScroll;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class CoordinateClue
extends ClueScroll
implements TextClueScroll,
LocationClueScroll {
    private static final ImmutableMap<WorldPoint, CoordinateClueInfo> CLUES = new ImmutableMap.Builder().put((Object)new WorldPoint(2479, 3158, 0), (Object)new CoordinateClueInfo("South of fruit tree patch, west of Tree Gnome Village.")).put((Object)new WorldPoint(2887, 3154, 0), (Object)new CoordinateClueInfo("West of Banana plantation on Karamja.")).put((Object)new WorldPoint(2743, 3151, 0), (Object)new CoordinateClueInfo("Entrance of Brimhaven dungeon.")).put((Object)new WorldPoint(3184, 3150, 0), (Object)new CoordinateClueInfo("South of Lumbridge Swamp.")).put((Object)new WorldPoint(3217, 3177, 0), (Object)new CoordinateClueInfo("East of Lumbridge Swamp.")).put((Object)new WorldPoint(3007, 3144, 0), (Object)new CoordinateClueInfo("Near the entrance to the Asgarnian Ice Dungeon, south of Port Sarim (AIQ).")).put((Object)new WorldPoint(2896, 3119, 0), (Object)new CoordinateClueInfo("Near Karambwan fishing spot (DKP).")).put((Object)new WorldPoint(2697, 3207, 0), (Object)new CoordinateClueInfo("Centre of Moss Giant Island, west of Brimhaven.")).put((Object)new WorldPoint(2679, 3110, 0), (Object)new CoordinateClueInfo("North of Hazelmere's house (CLS).")).put((Object)new WorldPoint(3510, 3074, 0), (Object)new CoordinateClueInfo("East of Uzer (DLQ).")).put((Object)new WorldPoint(3160, 3251, 0), (Object)new CoordinateClueInfo("West of trapdoor leading to H.A.M Hideout.")).put((Object)new WorldPoint(2643, 3252, 0), (Object)new CoordinateClueInfo("South of Ardougne Zoo, North of Tower of Life (DJP).")).put((Object)new WorldPoint(2322, 3061, 0), (Object)new CoordinateClueInfo("South-west of Castle wars (BKP).")).put((Object)new WorldPoint(2875, 3046, 0), (Object)new CoordinateClueInfo("North of nature altar, north of Shilo Village (CKR).")).put((Object)new WorldPoint(2849, 3033, 0), (Object)new CoordinateClueInfo("West of nature altar, north of Shilo Village (CKR).")).put((Object)new WorldPoint(2848, 3296, 0), (Object)new CoordinateClueInfo("North of Crandor island.")).put((Object)new WorldPoint(2583, 2990, 0), (Object)new CoordinateClueInfo("Feldip Hills, south-east of Gu'Thanoth (AKS).")).put((Object)new WorldPoint(3179, 3344, 0), (Object)new CoordinateClueInfo("In the cow pen north of the Lumbridge windmill.")).put((Object)new WorldPoint(2383, 3370, 0), (Object)new CoordinateClueInfo("West of the outpost")).put((Object)new WorldPoint(3312, 3375, 0), (Object)new CoordinateClueInfo("North-west of Exam Centre, on the hill.")).put((Object)new WorldPoint(3121, 3384, 0), (Object)new CoordinateClueInfo("North-east of Draynor Manor, near River Lum.")).put((Object)new WorldPoint(3430, 3388, 0), (Object)new CoordinateClueInfo("West of Mort Myre Swamp (BKR).")).put((Object)new WorldPoint(2920, 3403, 0), (Object)new CoordinateClueInfo("South-east of Taverley, near Lady of the Lake.")).put((Object)new WorldPoint(2594, 2899, 0), (Object)new CoordinateClueInfo("South-east of Feldip Hills, by the crimson swifts (AKS).")).put((Object)new WorldPoint(2387, 3435, 0), (Object)new CoordinateClueInfo("West of Tree Gnome Stronghold, near the pen containing terrorbirds.")).put((Object)new WorldPoint(2512, 3467, 0), (Object)new CoordinateClueInfo("Baxtorian Falls (Bring rope).")).put((Object)new WorldPoint(2381, 3468, 0), (Object)new CoordinateClueInfo("West of Tree Gnome Stronghold, north of the pen with terrorbirds.")).put((Object)new WorldPoint(3005, 3475, 0), (Object)new CoordinateClueInfo("Ice Mountain, west of Edgeville Monastery.")).put((Object)new WorldPoint(2585, 3505, 0), (Object)new CoordinateClueInfo("By the shore line north of the Coal Trucks.")).put((Object)new WorldPoint(3443, 3515, 0), (Object)new CoordinateClueInfo("South of Slayer Tower (CKS).")).put((Object)new WorldPoint(2416, 3516, 0), (Object)new CoordinateClueInfo("Tree Gnome Stronghold, west of Grand Tree, near swamp.")).put((Object)new WorldPoint(3429, 3523, 0), (Object)new CoordinateClueInfo("South of Slayer Tower (CKS).")).put((Object)new WorldPoint(2363, 3531, 0), (Object)new CoordinateClueInfo("North-east of Eagles' Peak (AKQ).")).put((Object)new WorldPoint(2919, 3535, 0), (Object)new CoordinateClueInfo("East of Burthorpe pub.")).put((Object)new WorldPoint(3548, 3560, 0), (Object)new CoordinateClueInfo("Inside Fenkenstrain's Castle.")).put((Object)new WorldPoint(1476, 3566, 0), (Object)new CoordinateClueInfo("Graveyard of Heroes in west Shayzien.")).put((Object)new WorldPoint(2735, 3638, 0), (Object)new CoordinateClueInfo("East of Rellekka, north-west of Golden Apple Tree (AJR).")).put((Object)new WorldPoint(2681, 3653, 0), (Object)new CoordinateClueInfo("Rellekka, in the garden of the south-east house.")).put((Object)new WorldPoint(2537, 3881, 0), (Object)new CoordinateClueInfo("Miscellania (CIP).")).put((Object)new WorldPoint(2828, 3234, 0), (Object)new CoordinateClueInfo("Southern coast of Crandor.")).put((Object)new WorldPoint(1247, 3726, 0), (Object)new CoordinateClueInfo("Just inside the Farming Guild")).put((Object)new WorldPoint(3770, 3898, 0), (Object)new CoordinateClueInfo("On the small island north-east of Fossil Island's mushroom forest.")).put((Object)new WorldPoint(2209, 3161, 0), (Object)new CoordinateClueInfo("North-east of Tyras Camp (BJS if 76 Agility).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2181, 3206, 0), (Object)new CoordinateClueInfo("South of Iorwerth Camp.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3081, 3209, 0), (Object)new CoordinateClueInfo("Small Island (CLP).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3399, 3246, 0), (Object)new CoordinateClueInfo("Behind the PvP Arena.")).put((Object)new WorldPoint(2699, 3251, 0), (Object)new CoordinateClueInfo("Little island (AIR).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3546, 3251, 0), (Object)new CoordinateClueInfo("North-east of Burgh de Rott.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3544, 3256, 0), (Object)new CoordinateClueInfo("North-east of Burgh de Rott.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2841, 3267, 0), (Object)new CoordinateClueInfo("Crandor island.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3168, 3041, 0), (Object)new CoordinateClueInfo("Bedabin Camp.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2542, 3031, 0), (Object)new CoordinateClueInfo("Gu'Tanoth, may require 20gp.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2581, 3030, 0), (Object)new CoordinateClueInfo("Gu'Tanoth island, enter cave north-west of Feldip Hills (AKS).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2961, 3024, 0), (Object)new CoordinateClueInfo("Ship yard (DKP).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2339, 3311, 0), (Object)new CoordinateClueInfo("East of Prifddinas on Arandar mountain pass.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3440, 3341, 0), (Object)new CoordinateClueInfo("Nature Spirit's grotto (BIP).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2763, 2974, 0), (Object)new CoordinateClueInfo("Cairn Isle, west of Shilo Village (CKR).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3138, 2969, 0), (Object)new CoordinateClueInfo("West of Bandit Camp in Kharidian Desert.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2924, 2963, 0), (Object)new CoordinateClueInfo("On the southern part of eastern Karamja.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2838, 2914, 0), (Object)new CoordinateClueInfo("Kharazi Jungle, near water pool (CKR).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3441, 3419, 0), (Object)new CoordinateClueInfo("Mort Myre Swamp (BKR).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2950, 2902, 0), (Object)new CoordinateClueInfo("South-east of Kharazi Jungle.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2775, 2891, 0), (Object)new CoordinateClueInfo("South-west of Kharazi Jungle.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3113, 3602, 0), (Object)new CoordinateClueInfo("Wilderness. South-west of Ferox Enclave (level 11).", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(2892, 3675, 0), (Object)new CoordinateClueInfo("On the summit of Trollheim.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3168, 3677, 0), (Object)new CoordinateClueInfo("Wilderness. Graveyard of Shadows.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(2853, 3690, 0), (Object)new CoordinateClueInfo("Entrance to the troll Stronghold.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3305, 3692, 0), (Object)new CoordinateClueInfo("Wilderness. West of eastern green dragon.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3055, 3696, 0), (Object)new CoordinateClueInfo("Wilderness. Bandit Camp.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3302, 3696, 0), (Object)new CoordinateClueInfo("Wilderness. West of eastern green dragon.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(1479, 3699, 0), (Object)new CoordinateClueInfo("Lizardman Canyon (DJR).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2712, 3732, 0), (Object)new CoordinateClueInfo("North-east of Rellekka (DKS).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2970, 3749, 0), (Object)new CoordinateClueInfo("Wilderness. Forgotten Cemetery.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3094, 3764, 0), (Object)new CoordinateClueInfo("Wilderness. Mining site north of Bandit Camp.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3311, 3769, 0), (Object)new CoordinateClueInfo("Wilderness. North of Venenatis.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(1460, 3782, 0), (Object)new CoordinateClueInfo("Lovakengj, near burning man.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3244, 3792, 0), (Object)new CoordinateClueInfo("Wilderness. South-east of Lava Dragon Isle by some Chaos Dwarves.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3140, 3804, 0), (Object)new CoordinateClueInfo("Wilderness. North of Ruins.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(2946, 3819, 0), (Object)new CoordinateClueInfo("Wilderness. Chaos Temple (level 38).", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3771, 3825, 0), (Object)new CoordinateClueInfo("Fossil Island. East of Museum Camp.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3013, 3846, 0), (Object)new CoordinateClueInfo("Wilderness. West of Lava Maze, before KBD's lair.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3058, 3884, 0), (Object)new CoordinateClueInfo("Wilderness. Near runite ore north of Lava Maze.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3290, 3889, 0), (Object)new CoordinateClueInfo("Wilderness. Demonic Ruins.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3770, 3897, 0), (Object)new CoordinateClueInfo("Small Island north of Fossil Island.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2505, 3899, 0), (Object)new CoordinateClueInfo("Small Island north-west of Miscellania (AJS).", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3285, 3942, 0), (Object)new CoordinateClueInfo("Wilderness. Rogues' Castle.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3159, 3959, 0), (Object)new CoordinateClueInfo("Wilderness. North of Deserted Keep, west of Resource Area.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3039, 3960, 0), (Object)new CoordinateClueInfo("Wilderness. Pirates' Hideout.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(2987, 3963, 0), (Object)new CoordinateClueInfo("Wilderness. West of Wilderness Agility Course.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3189, 3963, 0), (Object)new CoordinateClueInfo("Wilderness. North of Resource Area, near magic axe hut.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(2341, 3697, 0), (Object)new CoordinateClueInfo("North-east of the Piscatoris Fishing Colony bank.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(3143, 3774, 0), (Object)new CoordinateClueInfo("In level 32 Wilderness, by the black chinchompa hunting area.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(2992, 3941, 0), (Object)new CoordinateClueInfo("Wilderness Agility Course, past the log balance.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(1410, 3611, 0), (Object)new CoordinateClueInfo("Lake Molch dock west of Shayzien Encampment.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(1409, 3483, 0), (Object)new CoordinateClueInfo("South of Shayziens' Wall.", Enemy.SARADOMIN_WIZARD)).put((Object)new WorldPoint(2357, 3151, 0), (Object)new CoordinateClueInfo("Lletya.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3587, 3180, 0), (Object)new CoordinateClueInfo("Meiyerditch.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2820, 3078, 0), (Object)new CoordinateClueInfo("Tai Bwo Wannai. Hardwood Grove. 100 Trading sticks or elite Karamja diary completion is needed to enter.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3811, 3060, 0), (Object)new CoordinateClueInfo("Small island north-east of Mos Le'Harmless.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD, true, 6534)).put((Object)new WorldPoint(2180, 3282, 0), (Object)new CoordinateClueInfo("North of Iorwerth Camp.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2870, 2997, 0), (Object)new CoordinateClueInfo("North-east corner in Shilo Village.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3302, 2988, 0), (Object)new CoordinateClueInfo("On top of a cliff to the west of Pollnivneach.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2511, 2980, 0), (Object)new CoordinateClueInfo("Just south of Gu'Tanoth, west of gnome glider.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2732, 3372, 0), (Object)new CoordinateClueInfo("Legends' Guild.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3573, 3425, 0), (Object)new CoordinateClueInfo("North of Dessous's tomb from Desert Treasure.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3828, 2848, 0), (Object)new CoordinateClueInfo("East of Harmony Island.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3225, 2838, 0), (Object)new CoordinateClueInfo("South of Desert Treasure pyramid.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(1773, 3510, 0), (Object)new CoordinateClueInfo("Ruins north of the Hosidius mine.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3822, 3562, 0), (Object)new CoordinateClueInfo("North-east of Dragontooth Island. Bring a Ghostspeak Amulet and 25 Ecto-tokens to reach the island.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3603, 3564, 0), (Object)new CoordinateClueInfo("North of the wrecked ship, outside of Port Phasmatys.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2936, 2721, 0), (Object)new CoordinateClueInfo("Eastern shore of Crash Island.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2697, 2705, 0), (Object)new CoordinateClueInfo("South-west of Ape Atoll.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2778, 3678, 0), (Object)new CoordinateClueInfo("Mountain Camp.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2827, 3740, 0), (Object)new CoordinateClueInfo("West of the entrance to the Ice Path, where the Troll child resides.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2359, 3799, 0), (Object)new CoordinateClueInfo("Neitiznot.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2194, 3807, 0), (Object)new CoordinateClueInfo("Pirates' Cove.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2700, 3808, 0), (Object)new CoordinateClueInfo("Northwestern part of the Trollweiss and Rellekka Hunter area (DKS).", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3215, 3835, 0), (Object)new CoordinateClueInfo("Wilderness. Lava Dragon Isle.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3369, 3894, 0), (Object)new CoordinateClueInfo("Wilderness. Fountain of Rune.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2065, 3923, 0), (Object)new CoordinateClueInfo("Outside the western wall on Lunar Isle.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3188, 3933, 0), (Object)new CoordinateClueInfo("Wilderness. Resource Area.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2997, 3953, 0), (Object)new CoordinateClueInfo("Wilderness. Inside Agility Training Area.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3380, 3963, 0), (Object)new CoordinateClueInfo("Wilderness. North of Volcano.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3051, 3736, 0), (Object)new CoordinateClueInfo("East of the Wilderness Obelisk in 28 Wilderness.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2316, 3814, 0), (Object)new CoordinateClueInfo("West of Neitiznot, near the bridge.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2872, 3937, 0), (Object)new CoordinateClueInfo("Weiss.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2484, 4016, 0), (Object)new CoordinateClueInfo("Northeast corner of the Island of Stone.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2222, 3331, 0), (Object)new CoordinateClueInfo("Prifddinas, west of the Tower of Voices", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3560, 3987, 0), (Object)new CoordinateClueInfo("Lithkren. Digsite pendant teleport if unlocked, otherwise take rowboat from west of Mushroom Meadow Mushtree.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2318, 2954, 0), (Object)new CoordinateClueInfo("North-east corner of the Isle of Souls (BJP).", Enemy.BANDOSIAN_GUARD)).put((Object)new WorldPoint(2094, 2889, 0), (Object)new CoordinateClueInfo("West side of the Isle of Souls.", Enemy.ARMADYLEAN_GUARD)).put((Object)new WorldPoint(1451, 3509, 0), (Object)new CoordinateClueInfo("Ruins of Morra.", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(3318, 2706, 0), (Object)new CoordinateClueInfo("Necropolis mine", Enemy.ARMADYLEAN_OR_BANDOSIAN_GUARD)).put((Object)new WorldPoint(2178, 3209, 0), (Object)new CoordinateClueInfo("South of Iorwerth Camp.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(2155, 3100, 0), (Object)new CoordinateClueInfo("South of Port Tyras (BJS if 76 Agility).", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(2217, 3092, 0), (Object)new CoordinateClueInfo("Poison Waste island (DLR).", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(3830, 3060, 0), (Object)new CoordinateClueInfo("Small island located north-east of Mos Le'Harmless.", Enemy.BRASSICAN_MAGE, true, 6534)).put((Object)new WorldPoint(2834, 3271, 0), (Object)new CoordinateClueInfo("Crandor island.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(2732, 3284, 0), (Object)new CoordinateClueInfo("Witchaven.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(3622, 3320, 0), (Object)new CoordinateClueInfo("Meiyerditch. Outside mine.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(2303, 3328, 0), (Object)new CoordinateClueInfo("East of Prifddinas.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(3570, 3405, 0), (Object)new CoordinateClueInfo("North of Dessous's tomb from Desert Treasure.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(2840, 3423, 0), (Object)new CoordinateClueInfo("Water Obelisk Island.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(3604, 3564, 0), (Object)new CoordinateClueInfo("North of the wrecked ship, outside of Port Phasmatys (ALQ).", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(3085, 3569, 0), (Object)new CoordinateClueInfo("Wilderness. Obelisk of Air.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(2934, 2727, 0), (Object)new CoordinateClueInfo("Eastern shore of Crash Island.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(1451, 3695, 0), (Object)new CoordinateClueInfo("West side of Lizardman Canyon with Lizardman shaman.", Enemy.ANCIENT_WIZARDS)).put((Object)new WorldPoint(2538, 3739, 0), (Object)new CoordinateClueInfo("Waterbirth Island. Bring a pet rock and rune thrownaxe.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(1248, 3751, 0), (Object)new CoordinateClueInfo("In the north wing of the Farming Guild.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(1698, 3792, 0), (Object)new CoordinateClueInfo("Arceuus church.", Enemy.ANCIENT_WIZARDS)).put((Object)new WorldPoint(2951, 3820, 0), (Object)new CoordinateClueInfo("Wilderness. Chaos Temple (level 38).", Enemy.ANCIENT_WIZARDS)).put((Object)new WorldPoint(2202, 3825, 0), (Object)new CoordinateClueInfo("Pirates' Cove, between Lunar Isle and Rellekka.", Enemy.ANCIENT_WIZARDS)).put((Object)new WorldPoint(1761, 3853, 0), (Object)new CoordinateClueInfo("Arceuus essence mine (CIS).", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(2090, 3863, 0), (Object)new CoordinateClueInfo("South of Lunar Isle, west of Astral altar.", Enemy.ANCIENT_WIZARDS)).put((Object)new WorldPoint(1442, 3878, 0), (Object)new CoordinateClueInfo("Northern area of the Lovakengj Sulphur Mine. Facemask or Slayer Helmet recommended.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(3380, 3929, 0), (Object)new CoordinateClueInfo("Wilderness. Near Volcano.", Enemy.ANCIENT_WIZARDS)).put((Object)new WorldPoint(3188, 3939, 0), (Object)new CoordinateClueInfo("Wilderness. Resource Area.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(3304, 3941, 0), (Object)new CoordinateClueInfo("Wilderness. East of Rogues' Castle.", Enemy.ANCIENT_WIZARDS)).put((Object)new WorldPoint(2994, 3961, 0), (Object)new CoordinateClueInfo("Wilderness. Inside Agility Training Area.", Enemy.BRASSICAN_MAGE)).put((Object)new WorldPoint(1769, 3418, 0), (Object)new CoordinateClueInfo("Crabclaw Isle", Enemy.ANCIENT_WIZARDS)).put((Object)new WorldPoint(3363, 3808, 0), (Object)new CoordinateClueInfo("Wilderness. Eastern Dark Crabs.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3063, 3952, 0), (Object)new CoordinateClueInfo("Wilderness. Anvil east of Pirates` Hideout.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3230, 3955, 0), (Object)new CoordinateClueInfo("Wilderness. North of scorpion pit.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3293, 3755, 0), (Object)new CoordinateClueInfo("Wilderness. North wilderness slayer cave entrance.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3288, 3886, 0), (Object)new CoordinateClueInfo("Wilderness. Demonic ruins.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3019, 3893, 0), (Object)new CoordinateClueInfo("Wilderness. North of King Black Dragon entrance.", Enemy.ZAMORAK_WIZARD)).put((Object)new WorldPoint(3083, 3797, 0), (Object)new CoordinateClueInfo("Wilderness. South of the lava maze.", Enemy.ZAMORAK_WIZARD)).build();
    private final String text;
    private final WorldPoint location;
    @Nullable
    private final WorldPoint mirrorLocation;

    public CoordinateClue(String text, WorldPoint location, WorldPoint mirrorLocation) {
        this.text = text;
        this.location = location;
        this.mirrorLocation = mirrorLocation;
        CoordinateClueInfo clueInfo = (CoordinateClueInfo)CLUES.get((Object)location);
        if (clueInfo != null) {
            this.setFirePitVarbitId(clueInfo.getLightSourceVarbitId());
            this.setRequiresLight(clueInfo.lightRequired);
            this.setEnemy(clueInfo.getEnemy());
        }
        this.setRequiresSpade(true);
    }

    @Override
    public WorldPoint[] getLocations() {
        if (this.mirrorLocation != null) {
            return new WorldPoint[]{this.location, this.mirrorLocation};
        }
        return new WorldPoint[]{this.location};
    }

    @Override
    public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
        panelComponent.getChildren().add(TitleComponent.builder().text("Coordinate Clue").build());
        CoordinateClueInfo solution = (CoordinateClueInfo)CLUES.get((Object)this.location);
        if (solution != null) {
            panelComponent.getChildren().add(LineComponent.builder().left(solution.getDirections()).build());
            panelComponent.getChildren().add(LineComponent.builder().build());
        }
        panelComponent.getChildren().add(LineComponent.builder().left("Click the clue scroll on your world map to see dig location.").build());
    }

    @Override
    public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
        for (WorldPoint worldPoint : this.getLocations()) {
            LocalPoint localLocation = LocalPoint.fromWorld((Client)plugin.getClient(), (WorldPoint)worldPoint);
            if (localLocation == null) continue;
            OverlayUtil.renderTileOverlay(plugin.getClient(), graphics, localLocation, plugin.getSpadeImage(), Color.ORANGE);
        }
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public WorldPoint getLocation() {
        return this.location;
    }

    @Nullable
    public WorldPoint getMirrorLocation() {
        return this.mirrorLocation;
    }

    private static class CoordinateClueInfo {
        private final String directions;
        private final boolean lightRequired;
        private final int lightSourceVarbitId;
        private final Enemy enemy;

        private CoordinateClueInfo(@NonNull String directions) {
            this(directions, (Enemy)null);
            if (directions == null) {
                throw new NullPointerException("directions is marked non-null but is null");
            }
        }

        private CoordinateClueInfo(@NonNull String directions, Enemy enemy) {
            if (directions == null) {
                throw new NullPointerException("directions is marked non-null but is null");
            }
            this.directions = directions;
            this.enemy = enemy;
            this.lightRequired = false;
            this.lightSourceVarbitId = -1;
        }

        private CoordinateClueInfo(@Nonnull String directions, Enemy enemy, boolean lightRequired, int lightSourceVarbitId) {
            this.directions = directions;
            this.enemy = enemy;
            this.lightRequired = lightRequired;
            this.lightSourceVarbitId = lightSourceVarbitId;
        }

        public String getDirections() {
            return this.directions;
        }

        public boolean isLightRequired() {
            return this.lightRequired;
        }

        public Enemy getEnemy() {
            return this.enemy;
        }

        public int getLightSourceVarbitId() {
            return this.lightSourceVarbitId;
        }
    }
}

