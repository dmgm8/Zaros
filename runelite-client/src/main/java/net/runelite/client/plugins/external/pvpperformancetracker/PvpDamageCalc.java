/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Player
 *  net.runelite.api.PlayerComposition
 *  net.runelite.api.kit.KitType
 *  net.runelite.http.api.item.ItemEquipmentStats
 *  net.runelite.http.api.item.ItemStats
 *  org.apache.commons.lang3.ArrayUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import java.util.Arrays;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.kit.KitType;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.external.pvpperformancetracker.AnimationData;
import net.runelite.client.plugins.external.pvpperformancetracker.EquipmentData;
import net.runelite.client.plugins.external.pvpperformancetracker.FightLogEntry;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerConfig;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;
import net.runelite.client.plugins.external.pvpperformancetracker.RangeAmmoData;
import net.runelite.client.plugins.external.pvpperformancetracker.RingData;
import net.runelite.http.api.item.ItemEquipmentStats;
import net.runelite.http.api.item.ItemStats;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvpDamageCalc {
    private static final Logger log = LoggerFactory.getLogger(PvpDamageCalc.class);
    private static final int WEAPON_SLOT = 3;
    private static final int STAB_ATTACK = 0;
    private static final int SLASH_ATTACK = 1;
    private static final int CRUSH_ATTACK = 2;
    private static final int MAGIC_ATTACK = 3;
    private static final int RANGE_ATTACK = 4;
    private static final int STAB_DEF = 5;
    private static final int SLASH_DEF = 6;
    private static final int CRUSH_DEF = 7;
    private static final int MAGIC_DEF = 8;
    private static final int RANGE_DEF = 9;
    private static final int STRENGTH_BONUS = 10;
    private static final int RANGE_STRENGTH = 11;
    private static final int MAGIC_DAMAGE = 12;
    private static PvpPerformanceTrackerConfig config;
    private static final int STANCE_BONUS = 0;
    private static final double UNSUCCESSFUL_PRAY_DMG_MODIFIER = 0.6;
    private static final double ATTACK_OFFENSIVE_PRAYER_MODIFIER = 1.2;
    private static final double STRENGTH_OFFENSIVE_PRAYER_MODIFIER = 1.23;
    private static final double MAGIC_OFFENSIVE_PRAYER_MODIFIER = 1.25;
    private static final double RANGE_OFFENSIVE_PRAYER_DMG_MODIFIER = 1.23;
    private static final double RANGE_OFFENSIVE_PRAYER_ATTACK_MODIFIER = 1.2;
    private static final double MELEE_DEFENSIVE_PRAYER_MODIFIER = 1.25;
    private static final double MAGIC_DEFENSIVE_DEF_PRAYER_MODIFIER = 1.25;
    private static final double MAGIC_DEFENSIVE_MAGE_PRAYER_MODIFIER = 1.0;
    private static final double RANGE_DEFENSIVE_PRAYER_MODIFIER = 1.25;
    private static final double BALLISTA_SPEC_ACCURACY_MODIFIER = 1.25;
    private static final double BALLISTA_SPEC_DMG_MODIFIER = 1.25;
    private static final int ACB_SPEC_ACCURACY_MODIFIER = 2;
    private static final int DBOW_DMG_MODIFIER = 2;
    private static final int DBOW_SPEC_DMG_MODIFIER = 3;
    private static final int DBOW_SPEC_MIN_HIT = 16;
    private static final double DDS_SPEC_ACCURACY_MODIFIER = 1.25;
    private static final double DDS_SPEC_DMG_MODIFIER = 2.3;
    private static final int AGS_SPEC_ACCURACY_MODIFIER = 2;
    private static final double AGS_SPEC_INITIAL_DMG_MODIFIER = 1.1;
    private static final double AGS_SPEC_FINAL_DMG_MODIFIER = 1.25;
    private static final double VLS_SPEC_DMG_MODIFIER = 1.2;
    private static final double VLS_SPEC_MIN_DMG_MODIFIER = 0.2;
    private static final double VLS_SPEC_DEFENCE_SCALE = 0.25;
    private static final double SWH_SPEC_DMG_MODIFIER = 1.25;
    private static final double SWH_SPEC_MIN_DMG_MODIFIER = 0.25;
    public static final double BRIMSTONE_RING_OPPONENT_DEF_MODIFIER = 0.975;
    public static final double SMOKE_BATTLESTAFF_DMG_ACC_MODIFIER = 1.1;
    public static final double TOME_OF_FIRE_DMG_MODIFIER = 1.5;
    private ItemManager itemManager;
    private double averageHit = 0.0;
    private double accuracy = 0.0;
    private int minHit = 0;
    private int maxHit = 0;
    private Player attacker;

    public PvpDamageCalc(ItemManager itemManager) {
        config = PvpPerformanceTrackerPlugin.CONFIG;
        this.itemManager = itemManager;
    }

    public void updateDamageStats(Player attacker, Player defender, boolean success, AnimationData animationData) {
        if (attacker == null || defender == null) {
            return;
        }
        this.averageHit = 0.0;
        this.accuracy = 0.0;
        this.minHit = 0;
        this.maxHit = 0;
        this.attacker = attacker;
        AnimationData.AttackStyle attackStyle = animationData.attackStyle;
        int[] attackerItems = attacker.getPlayerComposition().getEquipmentIds();
        int[] defenderItems = defender.getPlayerComposition().getEquipmentIds();
        int[] playerStats = this.calculateBonuses(attackerItems);
        int[] opponentStats = this.calculateBonuses(defenderItems);
        boolean isSpecial = animationData.isSpecial;
        int weaponId = attackerItems[3] > 512 ? attackerItems[3] - 512 : attackerItems[3];
        EquipmentData weapon = EquipmentData.getEquipmentDataFor(weaponId);
        VoidStyle voidStyle = VoidStyle.getVoidStyleFor(attacker.getPlayerComposition());
        if (attackStyle.isMelee()) {
            this.getMeleeMaxHit(playerStats[10], isSpecial, weapon, voidStyle);
            this.getMeleeAccuracy(playerStats, opponentStats, attackStyle, isSpecial, weapon, voidStyle);
        } else if (attackStyle == AnimationData.AttackStyle.RANGED) {
            this.getRangedMaxHit(playerStats[11], isSpecial, weapon, voidStyle);
            this.getRangeAccuracy(playerStats[4], opponentStats[9], isSpecial, weapon, voidStyle);
        } else if (attackStyle == AnimationData.AttackStyle.MAGIC) {
            this.getMagicMaxHit(playerStats[12], animationData, weapon, voidStyle);
            this.getMagicAccuracy(playerStats[3], opponentStats[8], weapon, animationData, voidStyle);
        }
        this.getAverageHit(success, weapon, isSpecial);
        this.maxHit = (int)((double)this.maxHit * (success ? 1.0 : 0.6));
        log.info("attackStyle: " + attackStyle.toString() + ", avgHit: " + FightLogEntry.nf.format(this.averageHit) + ", acc: " + FightLogEntry.nf.format(this.accuracy) + "\nattacker(" + attacker.getName() + ")stats: " + Arrays.toString(playerStats) + "\ndefender(" + defender.getName() + ")stats: " + Arrays.toString(opponentStats));
    }

    private void getAverageHit(boolean success, EquipmentData weapon, boolean usingSpec) {
        double averageSuccessfulHit;
        double prayerModifier;
        boolean dbow = weapon == EquipmentData.DARK_BOW;
        boolean ags = weapon == EquipmentData.ARMADYL_GODSWORD;
        boolean claws = weapon == EquipmentData.DRAGON_CLAWS;
        boolean vls = weapon == EquipmentData.VESTAS_LONGSWORD || weapon == EquipmentData.BLIGHTED_VESTAS_LONGSWORD;
        boolean swh = weapon == EquipmentData.STATIUS_WARHAMMER;
        double agsModifier = ags ? 1.25 : 1.0;
        double d = prayerModifier = success ? 1.0 : 0.6;
        if (usingSpec && (dbow || vls || swh)) {
            double accuracyAdjuster = dbow ? this.accuracy : 1.0;
            this.minHit = dbow ? 16 : 0;
            this.minHit = vls ? (int)((double)this.maxHit * 0.2) : this.minHit;
            this.minHit = swh ? (int)((double)this.maxHit * 0.25) : this.minHit;
            int total = 0;
            for (int i = 0; i <= this.maxHit; ++i) {
                total = (int)((double)total + (i < this.minHit ? (double)this.minHit / accuracyAdjuster : (double)i));
            }
            averageSuccessfulHit = (double)total / (double)this.maxHit;
        } else {
            if (usingSpec && claws) {
                double invertedAccuracy = 1.0 - this.accuracy;
                double averageSuccessfulRegularHit = this.maxHit / 2;
                double higherModifierChance = this.accuracy + this.accuracy * invertedAccuracy;
                double lowerModifierChance = this.accuracy * Math.pow(invertedAccuracy, 2.0) + this.accuracy * Math.pow(invertedAccuracy, 3.0);
                double averageSpecialHit = (higherModifierChance * 2.0 + lowerModifierChance * 1.5) * averageSuccessfulRegularHit;
                this.averageHit = averageSpecialHit * prayerModifier;
                this.accuracy = higherModifierChance + lowerModifierChance;
                this.maxHit = this.maxHit * 2 + 1;
                return;
            }
            averageSuccessfulHit = (double)this.maxHit / 2.0;
        }
        this.averageHit = this.accuracy * averageSuccessfulHit * prayerModifier * agsModifier;
    }

    private void getMeleeMaxHit(int meleeStrength, boolean usingSpec, EquipmentData weapon, VoidStyle voidStyle) {
        boolean ags = weapon == EquipmentData.ARMADYL_GODSWORD;
        boolean dds = ArrayUtils.contains((Object[])EquipmentData.DRAGON_DAGGERS, (Object)((Object)weapon));
        boolean vls = weapon == EquipmentData.VESTAS_LONGSWORD || weapon == EquipmentData.BLIGHTED_VESTAS_LONGSWORD;
        boolean swh = weapon == EquipmentData.STATIUS_WARHAMMER;
        int effectiveLevel = (int)Math.floor((double)config.strengthLevel() * 1.23 + 8.0 + 3.0);
        if (voidStyle == VoidStyle.VOID_ELITE_MELEE || voidStyle == VoidStyle.VOID_MELEE) {
            effectiveLevel = (int)((double)effectiveLevel * voidStyle.dmgModifier);
        }
        int baseDamage = (int)Math.floor(0.5 + (double)(effectiveLevel * (meleeStrength + 64) / 640));
        double modifier = ags && usingSpec ? 1.1 : 1.0;
        modifier = swh && usingSpec ? 1.25 : modifier;
        modifier = dds && usingSpec ? 2.3 : modifier;
        modifier = vls && usingSpec ? 1.2 : modifier;
        this.maxHit = (int)(modifier * (double)baseDamage);
    }

    private void getRangedMaxHit(int rangeStrength, boolean usingSpec, EquipmentData weapon, VoidStyle voidStyle) {
        RangeAmmoData weaponAmmo = EquipmentData.getWeaponAmmo(weapon);
        boolean ballista = weapon == EquipmentData.HEAVY_BALLISTA;
        boolean dbow = weapon == EquipmentData.DARK_BOW;
        int ammoStrength = weaponAmmo == null ? 0 : weaponAmmo.getRangeStr();
        rangeStrength += ammoStrength;
        int effectiveLevel = (int)Math.floor((double)config.rangedLevel() * 1.23 + 8.0);
        if (voidStyle == VoidStyle.VOID_ELITE_RANGE || voidStyle == VoidStyle.VOID_RANGE) {
            effectiveLevel = (int)((double)effectiveLevel * voidStyle.dmgModifier);
        }
        int baseDamage = (int)Math.floor(0.5 + (double)(effectiveLevel * (rangeStrength + 64) / 640));
        double modifier = weaponAmmo == null ? 1.0 : weaponAmmo.getDmgModifier();
        modifier = ballista && usingSpec ? 1.25 : modifier;
        modifier = dbow && !usingSpec ? 2.0 : modifier;
        modifier = dbow && usingSpec ? 3.0 : modifier;
        this.maxHit = weaponAmmo == null ? (int)(modifier * (double)baseDamage) : (int)(modifier * (double)baseDamage + weaponAmmo.getBonusMaxHit());
    }

    private void getMagicMaxHit(int mageDamageBonus, AnimationData animationData, EquipmentData weapon, VoidStyle voidStyle) {
        boolean smokeBstaff = weapon == EquipmentData.SMOKE_BATTLESTAFF;
        boolean tome = EquipmentData.getEquipmentDataFor(this.attacker.getPlayerComposition().getEquipmentId(KitType.SHIELD)) == EquipmentData.TOME_OF_FIRE;
        double magicBonus = 1.0 + (double)mageDamageBonus / 100.0;
        if (smokeBstaff && AnimationData.isStandardSpellbookSpell(animationData)) {
            magicBonus *= 1.1;
        }
        if (tome && AnimationData.isFireSpell(animationData)) {
            magicBonus *= 1.5;
        }
        if (voidStyle == VoidStyle.VOID_ELITE_MAGE || voidStyle == VoidStyle.VOID_MAGE) {
            magicBonus *= voidStyle.dmgModifier;
        }
        this.maxHit = (int)((double)animationData.baseSpellDamage * magicBonus);
    }

    private void getMeleeAccuracy(int[] playerStats, int[] opponentStats, AnimationData.AttackStyle attackStyle, boolean usingSpec, EquipmentData weapon, VoidStyle voidStyle) {
        double attackBonus;
        boolean vls = weapon == EquipmentData.VESTAS_LONGSWORD || weapon == EquipmentData.BLIGHTED_VESTAS_LONGSWORD;
        boolean ags = weapon == EquipmentData.ARMADYL_GODSWORD;
        boolean dds = weapon == EquipmentData.DRAGON_DAGGER;
        double stabBonusPlayer = playerStats[0];
        double slashBonusPlayer = playerStats[1];
        double crushBonusPlayer = playerStats[2];
        double stabBonusTarget = opponentStats[5];
        double slashBonusTarget = opponentStats[6];
        double crushBonusTarget = opponentStats[7];
        double defenderChance = 0.0;
        double accuracyModifier = dds ? 1.25 : (ags ? 2.0 : 1.0);
        double effectiveLevelPlayer = Math.floor((double)config.attackLevel() * 1.2 + 0.0 + 8.0);
        if (voidStyle == VoidStyle.VOID_ELITE_MELEE || voidStyle == VoidStyle.VOID_MELEE) {
            effectiveLevelPlayer *= voidStyle.accuracyModifier;
        }
        double d = attackStyle == AnimationData.AttackStyle.STAB ? stabBonusPlayer : (attackBonus = attackStyle == AnimationData.AttackStyle.SLASH ? slashBonusPlayer : crushBonusPlayer);
        double targetDefenceBonus = attackStyle == AnimationData.AttackStyle.STAB ? stabBonusTarget : (attackStyle == AnimationData.AttackStyle.SLASH ? slashBonusTarget : crushBonusTarget);
        double baseChance = Math.floor(effectiveLevelPlayer * (attackBonus + 64.0));
        if (usingSpec) {
            baseChance *= accuracyModifier;
        }
        double attackerChance = baseChance;
        double effectiveLevelTarget = Math.floor((double)config.defenceLevel() * 1.25 + 0.0 + 8.0);
        defenderChance = vls && usingSpec ? Math.floor(effectiveLevelTarget * (stabBonusTarget + 64.0) * 0.25) : Math.floor(effectiveLevelTarget * (targetDefenceBonus + 64.0));
        this.accuracy = attackerChance > defenderChance ? 1.0 - (defenderChance + 2.0) / (2.0 * (attackerChance + 1.0)) : attackerChance / (2.0 * (defenderChance + 1.0));
    }

    private void getRangeAccuracy(int playerRangeAtt, int opponentRangeDef, boolean usingSpec, EquipmentData weapon, VoidStyle voidStyle) {
        double attackerChance;
        RangeAmmoData weaponAmmo = EquipmentData.getWeaponAmmo(weapon);
        boolean diamonds = ArrayUtils.contains((Object[])RangeAmmoData.DIAMOND_BOLTS, (Object)weaponAmmo);
        double effectiveLevelPlayer = Math.floor((double)config.rangedLevel() * 1.2 + 0.0 + 8.0);
        if (voidStyle == VoidStyle.VOID_ELITE_RANGE || voidStyle == VoidStyle.VOID_RANGE) {
            effectiveLevelPlayer *= voidStyle.accuracyModifier;
        }
        double rangeModifier = Math.floor(effectiveLevelPlayer * ((double)playerRangeAtt + 64.0));
        if (usingSpec) {
            boolean ballista;
            boolean acb = weapon == EquipmentData.ARMADYL_CROSSBOW;
            boolean bl = ballista = weapon == EquipmentData.HEAVY_BALLISTA;
            double specAccuracyModifier = acb ? 2.0 : (ballista ? 1.25 : 1.0);
            attackerChance = Math.floor(rangeModifier * specAccuracyModifier);
        } else {
            attackerChance = rangeModifier;
        }
        double effectiveLevelTarget = Math.floor((double)config.defenceLevel() * 1.25 + 0.0 + 8.0);
        double defenderChance = Math.floor(effectiveLevelTarget * ((double)opponentRangeDef + 64.0));
        this.accuracy = attackerChance > defenderChance ? 1.0 - (defenderChance + 2.0) / (2.0 * (attackerChance + 1.0)) : attackerChance / (2.0 * (defenderChance + 1.0));
        this.accuracy = diamonds ? this.accuracy * 0.9 + 0.1 : this.accuracy;
    }

    private void getMagicAccuracy(int playerMageAtt, int opponentMageDef, EquipmentData weapon, AnimationData animationData, VoidStyle voidStyle) {
        boolean smokeBstaff;
        double magicModifier;
        double effectiveLevelPlayer = Math.floor((double)config.magicLevel() * 1.25 + 8.0);
        if (voidStyle == VoidStyle.VOID_ELITE_MAGE || voidStyle == VoidStyle.VOID_MAGE) {
            effectiveLevelPlayer *= voidStyle.accuracyModifier;
        }
        double attackerChance = magicModifier = Math.floor(effectiveLevelPlayer * ((double)playerMageAtt + 64.0));
        double effectiveLevelTarget = Math.floor((double)config.defenceLevel() * 1.25 + 0.0 + 8.0);
        double effectiveMagicLevelTarget = Math.floor((double)config.magicLevel() * 1.0 * 0.7);
        double reducedDefenceLevelTarget = Math.floor(effectiveLevelTarget * 0.3);
        double effectiveMagicDefenceTarget = effectiveMagicLevelTarget + reducedDefenceLevelTarget;
        double defenderChance = config.ringChoice() == RingData.BRIMSTONE_RING ? Math.floor(effectiveMagicDefenceTarget * (0.975 * (double)opponentMageDef + 64.0)) : Math.floor(effectiveMagicDefenceTarget * ((double)opponentMageDef + 64.0));
        this.accuracy = attackerChance > defenderChance ? 1.0 - (defenderChance + 2.0) / (2.0 * (attackerChance + 1.0)) : attackerChance / (2.0 * (defenderChance + 1.0));
        boolean bl = smokeBstaff = weapon == EquipmentData.SMOKE_BATTLESTAFF;
        if (smokeBstaff && AnimationData.isStandardSpellbookSpell(animationData)) {
            this.accuracy *= 1.1;
        }
    }

    public int[] getItemStats(int itemId) {
        EquipmentData itemData;
        ItemStats itemStats = this.itemManager.getItemStats(itemId, false);
        if (itemStats == null && (itemData = EquipmentData.getEquipmentDataFor(itemId)) != null) {
            itemId = itemData.getItemId();
            itemStats = this.itemManager.getItemStats(itemId, false);
        }
        if (itemStats != null) {
            ItemEquipmentStats equipmentStats = itemStats.getEquipment();
            return new int[]{equipmentStats.getAstab(), equipmentStats.getAslash(), equipmentStats.getAcrush(), equipmentStats.getAmagic(), equipmentStats.getArange(), equipmentStats.getDstab(), equipmentStats.getDslash(), equipmentStats.getDcrush(), equipmentStats.getDmagic(), equipmentStats.getDrange(), equipmentStats.getStr(), equipmentStats.getRstr(), equipmentStats.getMdmg()};
        }
        return null;
    }

    private int[] calculateBonuses(int[] itemIds) {
        int[] equipmentBonuses;
        int[] arrn;
        if (config.ringChoice() == RingData.NONE) {
            int[] arrn2 = new int[13];
            arrn2[0] = 0;
            arrn2[1] = 0;
            arrn2[2] = 0;
            arrn2[3] = 0;
            arrn2[4] = 0;
            arrn2[5] = 0;
            arrn2[6] = 0;
            arrn2[7] = 0;
            arrn2[8] = 0;
            arrn2[9] = 0;
            arrn2[10] = 0;
            arrn2[11] = 0;
            arrn = arrn2;
            arrn2[12] = 0;
        } else {
            arrn = equipmentBonuses = this.getItemStats(config.ringChoice().getItemId());
        }
        if (equipmentBonuses == null) {
            equipmentBonuses = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }
        for (int item : itemIds) {
            int[] bonuses;
            if (item <= 512 || (bonuses = this.getItemStats(item - 512)) == null) continue;
            for (int id = 0; id < bonuses.length; ++id) {
                int n = id;
                equipmentBonuses[n] = equipmentBonuses[n] + bonuses[id];
            }
        }
        return equipmentBonuses;
    }

    public double getAverageHit() {
        return this.averageHit;
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public int getMinHit() {
        return this.minHit;
    }

    public int getMaxHit() {
        return this.maxHit;
    }

    static enum VoidStyle {
        VOID_MELEE(1.1, 1.1),
        VOID_RANGE(1.1, 1.1),
        VOID_MAGE(1.45, 1.0),
        VOID_ELITE_MELEE(1.1, 1.1),
        VOID_ELITE_RANGE(1.125, 1.125),
        VOID_ELITE_MAGE(1.45, 1.025),
        NONE(1.0, 1.0);

        double accuracyModifier;
        double dmgModifier;

        private VoidStyle(double accuracyModifier, double dmgModifier) {
            this.accuracyModifier = accuracyModifier;
            this.dmgModifier = dmgModifier;
        }

        public static VoidStyle getVoidStyleFor(PlayerComposition playerComposition) {
            if (playerComposition == null) {
                return NONE;
            }
            EquipmentData gloves = EquipmentData.getEquipmentDataFor(playerComposition.getEquipmentId(KitType.HANDS));
            if (gloves != EquipmentData.VOID_GLOVES) {
                return NONE;
            }
            EquipmentData helm = EquipmentData.getEquipmentDataFor(playerComposition.getEquipmentId(KitType.HEAD));
            EquipmentData torso = EquipmentData.getEquipmentDataFor(playerComposition.getEquipmentId(KitType.TORSO));
            EquipmentData legs = EquipmentData.getEquipmentDataFor(playerComposition.getEquipmentId(KitType.LEGS));
            if (torso == EquipmentData.VOID_BODY && legs == EquipmentData.VOID_LEGS) {
                return helm == EquipmentData.VOID_MAGE_HELM ? VOID_MAGE : (helm == EquipmentData.VOID_RANGE_HELM ? VOID_RANGE : (helm == EquipmentData.VOID_MELEE_HELM ? VOID_MELEE : NONE));
            }
            if (torso == EquipmentData.VOID_ELITE_BODY && legs == EquipmentData.VOID_ELITE_LEGS) {
                return helm == EquipmentData.VOID_MAGE_HELM ? VOID_ELITE_MAGE : (helm == EquipmentData.VOID_RANGE_HELM ? VOID_ELITE_RANGE : (helm == EquipmentData.VOID_MELEE_HELM ? VOID_ELITE_MELEE : NONE));
            }
            return NONE;
        }
    }
}

