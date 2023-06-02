/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Singleton
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.itemstats;

import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.runelite.client.plugins.itemstats.BoostedStatBoost;
import net.runelite.client.plugins.itemstats.Builders;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.SimpleStatBoost;
import net.runelite.client.plugins.itemstats.delta.DeltaPercentage;
import net.runelite.client.plugins.itemstats.food.Anglerfish;
import net.runelite.client.plugins.itemstats.potions.AncientBrew;
import net.runelite.client.plugins.itemstats.potions.GauntletPotion;
import net.runelite.client.plugins.itemstats.potions.MixedPotion;
import net.runelite.client.plugins.itemstats.potions.PrayerPotion;
import net.runelite.client.plugins.itemstats.potions.SaradominBrew;
import net.runelite.client.plugins.itemstats.potions.StaminaPotion;
import net.runelite.client.plugins.itemstats.potions.SuperRestore;
import net.runelite.client.plugins.itemstats.special.CastleWarsBandage;
import net.runelite.client.plugins.itemstats.special.CaveNightshade;
import net.runelite.client.plugins.itemstats.special.NettleTeaRunEnergy;
import net.runelite.client.plugins.itemstats.special.SpicyStew;
import net.runelite.client.plugins.itemstats.stats.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ItemStatChanges {
    private static final Logger log = LoggerFactory.getLogger(ItemStatChanges.class);
    private final Map<Integer, Effect> effects = new HashMap<Integer, Effect>();

    ItemStatChanges() {
        this.init();
    }

    private void init() {
        this.add(Builders.food(-5), 3146);
        this.add(Builders.food(1), 1942, 1957, 1965, 2130, 1871, 319, 4237, 2128, 7943, 4291, 4293);
        this.add(Builders.food(2), 1982, 1869, 1963, 3162, 2108, 2112, 2110, 2118, 2116, 7072, 1985, 1969, 2102, 2104, 2106, 2120, 2122, 2124, 2126, 2162, 7070, 9994, 4485);
        this.add(Builders.food(3), 315, 2142, 2140, 11324, 1973, 1861, 2152, 1875, 9996, 11916, 9052, 11439, 11437, 11435, 11433);
        this.add(Builders.food(4), 325, 1891, 1893, 1895, 1977, 6701, 403, 2955, 5817, 5815, 5813, 5811, 4012);
        this.add(Builders.food(5), 2309, 347, 1897, 1899, 1901, 3228, 7062, 7082, 7084, 2325, 2333, 11326, 20856, 24785, 7078, 4014, 7573, 7575, 7572, 7574);
        this.add(Builders.food(6), 6794, 355, 2327, 2331, 20871, 9980, 6965, 6963, 6961, 6962, 337, 5749, 5897, 5895, 5893, 5891, 25631, 4517, 11507, 11505, 11962, 11960, 21997, 21994, 22224, 22221, 11475, 11473, 11503, 11501);
        this.add(Builders.food(7), 333, 339, 2289, 2291, 2323, 2335, 7223, 2239, 2209, 2241, 2213);
        this.add(Builders.food(8), 351, 9988, 2293, 2295, 2237, 2205, 2243, 2217, 7064, 20873, 6883, 20858);
        this.add(Builders.food(9), 2036, 2048, 2034, 2084, 329, 2297, 2299);
        this.add(Builders.food(10), 361, 7521, 7086, 2878, 7934, 22929, 26149);
        this.add(Builders.food(11), 10136, 2003, 2301, 2303, 7530, 2227, 2281, 2219, 2253, 2221, 2255, 2223, 2259, 2225, 2277, 7066, 20875, 2149, 20860, 4016);
        this.add(Builders.food(12), 379, 2233, 2191, 2235, 2195);
        this.add(Builders.food(13), 365, 7068);
        this.add(Builders.food(14), 6703, 7054, 373, 20877, 1959, 1961, 20862, 2343);
        this.add(Builders.food(15), 2231, 2187, 2229, 2185, 7568);
        this.add(Builders.food(16), 7946, 6705, 7056);
        this.add(Builders.food(17), 20864, 20879);
        this.add(Builders.food(18), 3144, 24595, 23533);
        this.add(Builders.food(19), 2011, 1883, 1885);
        this.add(Builders.food(20), 7058, 385, 20866, 20881, 7579, 20390);
        this.add(Builders.food(21), 397);
        this.add(Builders.food(22), 391, 24589, 11936, 7060);
        this.add(Builders.food(23), 20868, 20883);
        this.add(new Anglerfish(), 13441, 24592);
        this.add(Builders.food(maxHP -> (int)Math.ceil((double)maxHP * 0.06)), 5504);
        this.add(Builders.food(maxHP -> (int)Math.ceil((double)maxHP * 0.05)), 5984);
        this.add(Builders.food(Builders.perc(0.1, 1)), 5988, 7088);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.DEFENCE, Builders.perc(0.02, 1))), 1967);
        this.add(Builders.combo(Builders.food(8), Builders.heal(Stats.RUN_ENERGY, 5)), 5972);
        this.add(Builders.combo(Builders.food(3), Builders.boost(Stats.ATTACK, Builders.perc(0.02, 2))), 1978);
        this.add(Builders.combo(Builders.food(3), new NettleTeaRunEnergy()), 4239, 4240, 4242, 4243);
        this.add(Builders.range(Builders.food(5), Builders.food(7)), 3369);
        this.add(Builders.range(Builders.food(5), Builders.food(8)), 3371);
        this.add(Builders.range(Builders.food(7), Builders.food(9)), 3373);
        this.add(Builders.range(Builders.food(7), Builders.food(10)), 6297, 6299);
        this.add(Builders.food(2), 10964, 10963, 10969, 10965, 10962, 10961, 10960, 10966, 10968, 10967);
        this.add(Builders.food(3), 10970);
        this.add(Builders.range(Builders.food(3), Builders.food(6)), 5004);
        this.add(Builders.range(Builders.food(6), Builders.food(10)), 3381);
        this.add(Builders.range(Builders.food(8), Builders.food(12)), 5003);
        this.add(Builders.food(10), 10971);
        this.add(Builders.combo(Builders.food(11), Builders.dec(Stats.ATTACK, 2)), 1993);
        this.add(Builders.combo(Builders.food(14), Builders.dec(Stats.ATTACK, 3)), 7919);
        this.add(Builders.combo(Builders.food(7), Builders.dec(Stats.ATTACK, 2)), 1989);
        this.add(Builders.dec(Stats.ATTACK, 3), 1991);
        this.add(Builders.combo(Builders.food(5), new SimpleStatBoost(Stats.STRENGTH, true, Builders.perc(0.05, 1)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -3))), 2038, 2080, 2021, 2019, 2015, 2017);
        this.add(Builders.combo(Builders.food(7), new SimpleStatBoost(Stats.STRENGTH, true, Builders.perc(0.05, 2)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -3))), 2028, 2064);
        this.add(Builders.combo(Builders.food(5), new SimpleStatBoost(Stats.STRENGTH, true, Builders.perc(0.05, 2)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -3))), 2032, 2092, 2030, 2074);
        this.add(Builders.combo(Builders.food(5), new SimpleStatBoost(Stats.STRENGTH, true, Builders.perc(0.06, 1)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -3))), 2040, 2054);
        this.add(Builders.combo(Builders.food(3), new SimpleStatBoost(Stats.STRENGTH, true, Builders.perc(0.04, 1)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -3))), 1915);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.STRENGTH, Builders.perc(0.02, 1)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.06, -1))), 1917, 7740);
        this.add(Builders.combo(Builders.food(4), Builders.boost(Stats.STRENGTH, Builders.perc(0.04, 2)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.1, -2))), 3803);
        this.add(Builders.combo(Builders.food(15), Builders.boost(Stats.STRENGTH, Builders.perc(0.1, 2)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.5, -4))), 3801);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, 5), Builders.boost(Stats.STRENGTH, 5), Builders.heal(Stats.MAGIC, -5), Builders.heal(Stats.PRAYER, -5)), 24774);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.STRENGTH, 2), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -2))), 1905, 5779, 5781, 5783, 5785, 7744);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.STRENGTH, 3), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -3))), 5739, 5859, 5861, 5863, 5865);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.WOODCUTTING, 1), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -2)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.02, -2))), 5751, 5819, 5821, 5823, 5825);
        this.add(Builders.combo(Builders.food(2), Builders.boost(Stats.WOODCUTTING, 2), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -3)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.02, -3))), 5753, 5899, 5901, 5903, 5905);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.THIEVING, 1), Builders.boost(Stats.ATTACK, 1), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.06, -3)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.06, -3))), 4627);
        this.add(Builders.combo(Builders.food(1), new SimpleStatBoost(Stats.COOKING, true, Builders.perc(0.05, 1)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -2)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.05, -2))), 5755, 5827, 5829, 5831, 5833, 7754);
        this.add(Builders.combo(Builders.food(2), new SimpleStatBoost(Stats.COOKING, true, Builders.perc(0.05, 2)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -3)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.05, -3))), 5757, 5907, 5909, 5911, 5913);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.FARMING, 1), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -2)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.02, -2))), 5763, 5843, 5845, 5847, 5849, 7752);
        this.add(Builders.combo(Builders.food(2), Builders.boost(Stats.FARMING, 2), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -3)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.02, -3))), 5765, 5923, 5925, 5927, 5929);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.STRENGTH, 2), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -2))), 1911, 5803, 5805, 5807, 5809, 7748);
        this.add(Builders.combo(Builders.food(2), Builders.boost(Stats.STRENGTH, 3), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -2))), 5745, 5883, 5885, 5887, 5889);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.MINING, 1), Builders.boost(Stats.SMITHING, 1), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.04, -2)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.04, -2)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.04, -2))), 1913, 5771, 5773, 5775, 5777);
        this.add(Builders.combo(Builders.food(2), Builders.boost(Stats.MINING, 2), Builders.boost(Stats.SMITHING, 2), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.04, -3)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.04, -3)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.04, -3))), 5747, 5851, 5853, 5855, 5857);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.HERBLORE, 1), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.04, -2)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.04, -2)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.04, -2))), 1909, 5787, 5789, 5791, 5793, 7746);
        this.add(Builders.combo(Builders.food(2), Builders.boost(Stats.HERBLORE, 2), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.04, -3)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.04, -3)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.04, -3))), 5743, 5867, 5869, 5871, 5873);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.SLAYER, 2), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -2)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.02, -2)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.02, -2))), 5759, 5835, 5837, 5839, 5841);
        this.add(Builders.combo(Builders.food(2), Builders.boost(Stats.SLAYER, 4), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.02, -3)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.02, -3)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.02, -3))), 5761, 5915, 5917, 5919, 5921);
        this.add(Builders.combo(Builders.food(1), new SimpleStatBoost(Stats.MAGIC, true, Builders.perc(0.02, 2)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -1)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.05, -1)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.05, -1))), 1907, 5795, 5797, 5799, 5801);
        this.add(Builders.combo(Builders.food(2), new SimpleStatBoost(Stats.MAGIC, true, Builders.perc(0.02, 3)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.05, -2)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.05, -2)), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.05, -2))), 5741, 5875, 5877, 5879, 5881);
        this.add(Builders.combo(Builders.food(14), Builders.boost(Stats.STRENGTH, 3), Builders.boost(Stats.MINING, 1), Builders.heal(Stats.PRAYER, Builders.perc(0.06, -1)), new BoostedStatBoost(Stats.AGILITY, false, Builders.perc(0.09, -3)), new BoostedStatBoost(Stats.ATTACK, false, Builders.perc(0.06, -1)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.08, -2)), new BoostedStatBoost(Stats.HERBLORE, false, Builders.perc(0.06, -1)), new BoostedStatBoost(Stats.MAGIC, false, Builders.perc(0.05, -1)), new BoostedStatBoost(Stats.RANGED, false, Builders.perc(0.06, -1))), 7157);
        this.add(Builders.combo(Builders.food(2), Builders.heal(Stats.PRAYER, Builders.perc(0.04, -2))), 22430);
        this.add(Builders.combo(Builders.food(1), Builders.boost(Stats.AGILITY, 1), Builders.heal(Stats.STRENGTH, -1)), 23948);
        this.add(Builders.combo(Builders.boost(Stats.RANGED, 4), new BoostedStatBoost(Stats.STRENGTH, false, Builders.perc(0.04, -2)), new BoostedStatBoost(Stats.MAGIC, false, Builders.perc(0.04, -2))), 25826);
        this.add(Builders.heal(Stats.RUN_ENERGY, 5), 10851);
        this.add(Builders.combo(Builders.heal(Stats.RUN_ENERGY, 10), Builders.boost(Stats.THIEVING, 1)), 10848);
        this.add(Builders.combo(Builders.heal(Stats.RUN_ENERGY, 15), Builders.boost(Stats.THIEVING, 2)), 10850);
        this.add(Builders.combo(Builders.heal(Stats.RUN_ENERGY, 20), Builders.boost(Stats.THIEVING, 3)), 10849);
        SimpleStatBoost attackPot = Builders.boost(Stats.ATTACK, Builders.perc(0.1, 3));
        SimpleStatBoost strengthPot = Builders.boost(Stats.STRENGTH, Builders.perc(0.1, 3));
        SimpleStatBoost defencePot = Builders.boost(Stats.DEFENCE, Builders.perc(0.1, 3));
        Effect combatPot = Builders.combo(attackPot, strengthPot);
        SimpleStatBoost magicEssence = Builders.boost(Stats.MAGIC, 3);
        SimpleStatBoost magicPot = Builders.boost(Stats.MAGIC, 4);
        SimpleStatBoost imbuedHeart = Builders.boost(Stats.MAGIC, Builders.perc(0.1, 1));
        SimpleStatBoost rangingPot = Builders.boost(Stats.RANGED, Builders.perc(0.1, 4));
        SimpleStatBoost superAttackPot = Builders.boost(Stats.ATTACK, Builders.perc(0.15, 5));
        SimpleStatBoost superStrengthPot = Builders.boost(Stats.STRENGTH, Builders.perc(0.15, 5));
        SimpleStatBoost superDefencePot = Builders.boost(Stats.DEFENCE, Builders.perc(0.15, 5));
        SimpleStatBoost superMagicPot = Builders.boost(Stats.MAGIC, Builders.perc(0.15, 5));
        SimpleStatBoost superRangingPot = Builders.boost(Stats.RANGED, Builders.perc(0.15, 5));
        SimpleStatBoost divinePot = Builders.heal(Stats.HITPOINTS, -10);
        Effect zamorakBrew = Builders.combo(Builders.boost(Stats.ATTACK, Builders.perc(0.2, 2)), Builders.boost(Stats.STRENGTH, Builders.perc(0.12, 2)), Builders.heal(Stats.PRAYER, Builders.perc(0.1, 0)), new BoostedStatBoost(Stats.DEFENCE, false, Builders.perc(0.1, -2)), new BoostedStatBoost(Stats.HITPOINTS, false, Builders.perc(-0.12, 0)));
        AncientBrew ancientBrew = new AncientBrew();
        this.add(attackPot, 125, 123, 121, 2428);
        this.add(strengthPot, 119, 117, 115, 113);
        this.add(defencePot, 137, 135, 133, 2432);
        this.add(magicPot, 3046, 3044, 3042, 3040);
        this.add(rangingPot, 173, 171, 169, 2444, 23551, 23553, 23555, 23557);
        this.add(combatPot, 9745, 9743, 9741, 9739, 26150, 26151, 26152, 26153);
        this.add(superAttackPot, 149, 147, 145, 2436);
        this.add(superStrengthPot, 161, 159, 157, 2440);
        this.add(superDefencePot, 167, 165, 163, 2442);
        this.add(magicEssence, 9024, 9023, 9022, 9021);
        this.add(Builders.combo(superAttackPot, superStrengthPot, superDefencePot), 12701, 12699, 12697, 12695);
        this.add(zamorakBrew, 193, 191, 189, 2450);
        this.add(new SaradominBrew(0.15, 0.2, 0.1, 2, 2), 6691, 6689, 6687, 6685, 23575, 23577, 23579, 23581);
        this.add(superRangingPot, 11725, 11724, 11723, 11722);
        this.add(superMagicPot, 11729, 11728, 11727, 11726);
        this.add(Builders.combo(rangingPot, superDefencePot), 22470, 22467, 22464, 22461);
        this.add(Builders.combo(magicPot, superDefencePot), 22458, 22455, 22452, 22449);
        this.add(Builders.combo(magicPot, divinePot), 23754, 23751, 23748, 23745);
        this.add(Builders.combo(rangingPot, divinePot), 23742, 23739, 23736, 23733);
        this.add(Builders.combo(superAttackPot, divinePot), 23706, 23703, 23700, 23697);
        this.add(Builders.combo(superStrengthPot, divinePot), 23718, 23715, 23712, 23709);
        this.add(Builders.combo(superDefencePot, divinePot), 23730, 23727, 23724, 23721);
        this.add(Builders.combo(superAttackPot, superStrengthPot, superDefencePot, divinePot), 23694, 23691, 23688, 23685);
        this.add(Builders.combo(rangingPot, superDefencePot, divinePot), 24644, 24641, 24638, 24635);
        this.add(Builders.combo(magicPot, superDefencePot, divinePot), 24632, 24629, 24626, 24623);
        this.add(Builders.combo(superAttackPot, superStrengthPot, superDefencePot, rangingPot, imbuedHeart), 25159, 25160, 25161, 25162);
        this.add(Builders.combo(superAttackPot, superStrengthPot), 23543, 23545, 23547, 23549);
        this.add(ancientBrew, 26346, 26344, 26342, 26340);
        this.add(new MixedPotion(3, attackPot), 11431, 11429);
        this.add(new MixedPotion(3, strengthPot), 11441, 11443);
        this.add(new MixedPotion(6, defencePot), 11459, 11457);
        this.add(new MixedPotion(6, magicPot), 11515, 11513);
        this.add(new MixedPotion(6, rangingPot), 11511, 11509);
        this.add(new MixedPotion(6, combatPot), 11447, 11445);
        this.add(new MixedPotion(6, superAttackPot), 11471, 11469);
        this.add(new MixedPotion(6, superStrengthPot), 11487, 11485);
        this.add(new MixedPotion(6, superDefencePot), 11499, 11497);
        this.add(new MixedPotion(6, magicEssence), 11491, 11489);
        this.add(new MixedPotion(6, zamorakBrew), 11523, 11521);
        this.add(new MixedPotion(6, ancientBrew), 26353, 26350);
        this.add(Builders.combo(superAttackPot, superStrengthPot, superDefencePot, superRangingPot, superMagicPot, Builders.heal(Stats.HITPOINTS, -50)), 11733, 11732, 11731, 11730);
        this.add(new CastleWarsBandage(), 4049);
        this.add(Builders.combo(Builders.food(20), Builders.heal(Stats.PRAYER, Builders.perc(0.25, 5)), Builders.heal(Stats.RUN_ENERGY, 20), Builders.boost(Stats.ATTACK, Builders.perc(0.15, 4)), Builders.boost(Stats.STRENGTH, Builders.perc(0.15, 4)), Builders.boost(Stats.DEFENCE, Builders.perc(0.15, 4)), rangingPot, magicPot), 25730);
        Effect restorePot = Builders.combo(Builders.heal(Stats.ATTACK, Builders.perc(0.3, 10)), Builders.heal(Stats.STRENGTH, Builders.perc(0.3, 10)), Builders.heal(Stats.DEFENCE, Builders.perc(0.3, 10)), Builders.heal(Stats.RANGED, Builders.perc(0.3, 10)), Builders.heal(Stats.MAGIC, Builders.perc(0.3, 10)));
        SimpleStatBoost energyPot = Builders.heal(Stats.RUN_ENERGY, 10);
        PrayerPotion prayerPot = new PrayerPotion(7);
        SimpleStatBoost superEnergyPot = Builders.heal(Stats.RUN_ENERGY, 20);
        SuperRestore superRestorePot = new SuperRestore(0.25, 8);
        StaminaPotion staminaPot = new StaminaPotion();
        DeltaPercentage remedyHeal = Builders.perc(0.16, 6);
        this.add(restorePot, 131, 129, 127, 2430);
        this.add(energyPot, 3014, 3012, 3010, 3008);
        this.add(prayerPot, 143, 141, 139, 2434);
        this.add(superEnergyPot, 3022, 3020, 3018, 3016);
        this.add(superRestorePot, 3030, 3028, 3026, 3024, 24605, 24603, 24601, 24598, 23567, 23569, 23571, 23573);
        this.add(new SuperRestore(0.3, 4), 10931, 10929, 10927, 10925, 23559, 23561, 23563, 23565);
        this.add(Builders.combo(Builders.heal(Stats.ATTACK, remedyHeal), Builders.heal(Stats.STRENGTH, remedyHeal), Builders.heal(Stats.DEFENCE, remedyHeal), Builders.heal(Stats.RANGED, remedyHeal), Builders.heal(Stats.MAGIC, remedyHeal)), 27211, 27208, 27205, 27202);
        this.add(staminaPot, 12631, 12629, 12627, 12625);
        this.add(new MixedPotion(3, restorePot), 11451, 11449);
        this.add(new MixedPotion(6, energyPot), 11455, 11453);
        this.add(new MixedPotion(6, prayerPot), 11467, 11465);
        this.add(new MixedPotion(6, superEnergyPot), 11483, 11481);
        this.add(new MixedPotion(6, superRestorePot), 11495, 11493);
        this.add(new MixedPotion(6, staminaPot), 12635, 12633);
        DeltaPercentage coxPlusPotionBoost = Builders.perc(0.16, 6);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, coxPlusPotionBoost), Builders.boost(Stats.STRENGTH, coxPlusPotionBoost), Builders.boost(Stats.DEFENCE, coxPlusPotionBoost), Builders.boost(Stats.RANGED, coxPlusPotionBoost), Builders.boost(Stats.MAGIC, coxPlusPotionBoost), Builders.heal(Stats.HITPOINTS, -50)), 20993, 20994, 20995, 20996);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, coxPlusPotionBoost), Builders.boost(Stats.STRENGTH, coxPlusPotionBoost), Builders.boost(Stats.DEFENCE, coxPlusPotionBoost)), 20921, 20922, 20923, 20924);
        this.add(Builders.combo(Builders.boost(Stats.RANGED, coxPlusPotionBoost), Builders.boost(Stats.DEFENCE, coxPlusPotionBoost)), 20933, 20934, 20935, 20936);
        this.add(Builders.combo(Builders.boost(Stats.MAGIC, coxPlusPotionBoost), Builders.boost(Stats.DEFENCE, coxPlusPotionBoost)), 20945, 20946, 20947, 20948);
        this.add(new SuperRestore(0.3, 11), 20957, 20958, 20959, 20960);
        this.add(new SaradominBrew(0.15, 0.2, 0.1, 5, 4), 20981, 20982, 20983, 20984);
        DeltaPercentage coxPotionBoost = Builders.perc(0.13, 5);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, coxPotionBoost), Builders.boost(Stats.STRENGTH, coxPotionBoost), Builders.boost(Stats.DEFENCE, coxPotionBoost), Builders.boost(Stats.RANGED, coxPotionBoost), Builders.boost(Stats.MAGIC, coxPotionBoost), Builders.heal(Stats.HITPOINTS, -50)), 20989, 20990, 20991, 20992);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, coxPotionBoost), Builders.boost(Stats.STRENGTH, coxPotionBoost), Builders.boost(Stats.DEFENCE, coxPotionBoost)), 20917, 20918, 20919, 20920);
        this.add(Builders.combo(Builders.boost(Stats.RANGED, coxPotionBoost), Builders.boost(Stats.DEFENCE, coxPotionBoost)), 20929, 20930, 20931, 20932);
        this.add(Builders.combo(Builders.boost(Stats.MAGIC, coxPotionBoost), Builders.boost(Stats.DEFENCE, coxPotionBoost)), 20941, 20942, 20943, 20944);
        DeltaPercentage coxMinusPotionBoost = Builders.perc(0.1, 4);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, coxMinusPotionBoost), Builders.boost(Stats.STRENGTH, coxMinusPotionBoost), Builders.boost(Stats.DEFENCE, coxMinusPotionBoost), Builders.boost(Stats.RANGED, coxMinusPotionBoost), Builders.boost(Stats.MAGIC, coxMinusPotionBoost), Builders.heal(Stats.HITPOINTS, -50)), 20985, 20986, 20987, 20988);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, coxMinusPotionBoost), Builders.boost(Stats.STRENGTH, coxMinusPotionBoost), Builders.boost(Stats.DEFENCE, coxMinusPotionBoost)), 20913, 20914, 20915, 20916);
        this.add(Builders.combo(Builders.boost(Stats.RANGED, coxMinusPotionBoost), Builders.boost(Stats.DEFENCE, coxMinusPotionBoost)), 20925, 20926, 20927, 20928);
        this.add(Builders.combo(Builders.boost(Stats.MAGIC, coxMinusPotionBoost), Builders.boost(Stats.DEFENCE, coxMinusPotionBoost)), 20937, 20938, 20939, 20940);
        SimpleStatBoost agilityPot = Builders.boost(Stats.AGILITY, 3);
        SimpleStatBoost fishingPot = Builders.boost(Stats.FISHING, 3);
        SimpleStatBoost hunterPot = Builders.boost(Stats.HUNTER, 3);
        this.add(agilityPot, 3038, 3036, 3034, 3032);
        this.add(fishingPot, 155, 153, 151, 2438);
        this.add(hunterPot, 10004, 10002, 10000, 9998);
        this.add(Builders.combo(Builders.boost(Stats.HITPOINTS, 5), Builders.heal(Stats.RUN_ENERGY, 5)), 4423, 4421, 4419, 4417);
        this.add(new MixedPotion(6, agilityPot), 11463, 11461);
        this.add(new MixedPotion(6, fishingPot), 11479, 11477);
        this.add(new MixedPotion(6, hunterPot), 11519, 11517);
        this.add(Builders.combo(Builders.food(3), Builders.range(Builders.heal(Stats.RUN_ENERGY, 5), Builders.heal(Stats.RUN_ENERGY, 10))), 6469);
        this.add(Builders.heal(Stats.RUN_ENERGY, 30), 464);
        this.add(Builders.heal(Stats.RUN_ENERGY, 50), 9475);
        this.add(Builders.combo(Builders.food(12), Builders.heal(Stats.RUN_ENERGY, 50)), 6311);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, 6), Builders.boost(Stats.FARMING, 3)), 7178, 7180);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, 6), Builders.boost(Stats.FISHING, 3)), 7188, 7190);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, 7), Builders.boost(Stats.HERBLORE, 4)), 19662, 19659);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, 8), Builders.boost(Stats.CRAFTING, 4)), 21690, 21687);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, 8), Builders.boost(Stats.FISHING, 5)), 7198, 7200);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, 11), Builders.boost(Stats.SLAYER, 5), Builders.boost(Stats.RANGED, 4)), 7208, 7210);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, 11), Builders.boost(Stats.AGILITY, 5), Builders.heal(Stats.RUN_ENERGY, 10)), 7218, 7220);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, 10), Builders.boost(Stats.FLETCHING, 4)), 22795, 22792);
        this.add(Builders.combo(Builders.range(Builders.food(1), Builders.food(3)), Builders.heal(Stats.RUN_ENERGY, 10)), 10476);
        this.add(new SpicyStew(), 7479);
        this.add(imbuedHeart, 20724);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, 2), Builders.boost(Stats.STRENGTH, 1), Builders.heal(Stats.PRAYER, 1), Builders.heal(Stats.DEFENCE, -1)), 247);
        this.add(new CaveNightshade(), 2398);
        this.add(Builders.heal(Stats.HITPOINTS, 16), 25960, 25958);
        this.add(Builders.heal(Stats.HITPOINTS, 20), 23874);
        this.add(new GauntletPotion(), 23882, 23883, 23884, 23885);
        this.add(Builders.combo(Builders.heal(Stats.HITPOINTS, Builders.perc(0.15, 1)), Builders.heal(Stats.RUN_ENERGY, 100)), 25202);
        this.add(Builders.combo(Builders.boost(Stats.ATTACK, Builders.perc(0.15, 5)), Builders.boost(Stats.STRENGTH, Builders.perc(0.15, 5)), Builders.boost(Stats.DEFENCE, Builders.perc(0.15, 5)), Builders.boost(Stats.RANGED, Builders.perc(0.15, 5)), Builders.boost(Stats.MAGIC, Builders.perc(0.15, 5)), Builders.heal(Stats.PRAYER, Builders.perc(0.25, 8))), 25206, 25205, 25204, 25203);
        log.debug("{} items; {} behaviours loaded", (Object)this.effects.size(), (Object)new HashSet<Effect>(this.effects.values()).size());
    }

    private void add(Effect effect, int ... items) {
        assert (items.length > 0);
        for (int item : items) {
            Effect prev = this.effects.put(item, effect);
            assert (prev == null) : "Item already added: " + item;
        }
    }

    public Effect get(int id) {
        return this.effects.get(id);
    }
}

