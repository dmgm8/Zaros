/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import net.runelite.client.plugins.external.pvpperformancetracker.EquipmentData;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;

public interface RangeAmmoData {
    public static final RangeAmmoData[] DIAMOND_BOLTS = new RangeAmmoData[]{BoltAmmo.DIAMOND_BOLTS_E, StrongBoltAmmo.DIAMOND_BOLTS_E, StrongBoltAmmo.DIAMOND_DRAGON_BOLTS_E};

    public int getRangeStr();

    public double getBonusMaxHit();

    public double getDmgModifier();

    public static enum OtherAmmo implements RangeAmmoData
    {
        AMETHYST_ARROWS(55),
        DRAGON_ARROW(60),
        DRAGON_JAVELIN(150);

        private int rangeStr;
        private double bonusMaxHit;
        private double dmgModifier;

        private OtherAmmo(int rangeStr) {
            this.rangeStr = rangeStr;
            this.bonusMaxHit = 0.0;
            this.dmgModifier = 1.0;
        }

        @Override
        public int getRangeStr() {
            return this.rangeStr;
        }

        @Override
        public double getBonusMaxHit() {
            return this.bonusMaxHit;
        }

        @Override
        public double getDmgModifier() {
            return this.dmgModifier;
        }
    }

    public static enum DartAmmo implements RangeAmmoConfigData
    {
        ADAMANT_DARTS("Adamant Darts", 10),
        RUNE_DARTS("Rune Darts", 14),
        DRAGON_DARTS("Dragon Darts", 20);

        static EquipmentData[] WEAPONS_USING;
        private String name;
        private int rangeStr;
        private double bonusMaxHit;
        private double dmgModifier;

        private DartAmmo(String name, int rangeStr) {
            this.name = name;
            this.rangeStr = rangeStr;
            this.bonusMaxHit = 0.0;
            this.dmgModifier = 1.0;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getRangeStr() {
            return this.rangeStr;
        }

        @Override
        public double getBonusMaxHit() {
            return this.bonusMaxHit;
        }

        @Override
        public double getDmgModifier() {
            return this.dmgModifier;
        }

        static {
            WEAPONS_USING = new EquipmentData[]{EquipmentData.TOXIC_BLOWPIPE};
        }
    }

    public static enum StrongBoltAmmo implements RangeAmmoConfigData
    {
        RUNITE_BOLTS("Runite Bolts", 115, 1.0),
        DRAGONSTONE_BOLTS_E("Dstone Bolts (e)", 117, 0.2, 0.06, 1.0),
        DIAMOND_BOLTS_E("Diamond Bolts (e)", 105, 1.015),
        DRAGONSTONE_DRAGON_BOLTS_E("Dstone DBolts (e)", 122, 0.2, 0.06, 1.0),
        OPAL_DRAGON_BOLTS_E("Opal DBolts (e)", 122, 0.1, 0.05, 1.0),
        DIAMOND_DRAGON_BOLTS_E("Diamond DBolts (e)", 122, 1.015);

        static EquipmentData[] WEAPONS_USING;
        private String name;
        private int rangeStr;
        private double specRangeLevelModifier;
        private double specChance;
        private double dmgModifier;

        private StrongBoltAmmo(String name, int rangeStr, double specRangeLevelModifier, double specChance, double dmgModifier) {
            this.name = name;
            this.rangeStr = rangeStr;
            this.specRangeLevelModifier = specRangeLevelModifier;
            this.specChance = specChance;
            this.dmgModifier = dmgModifier;
        }

        private StrongBoltAmmo(String name, int rangeStr, double dmgModifier) {
            this.name = name;
            this.rangeStr = rangeStr;
            this.specRangeLevelModifier = 0.0;
            this.specChance = 0.0;
            this.dmgModifier = dmgModifier;
        }

        @Override
        public double getBonusMaxHit() {
            return (double)((int)((double)PvpPerformanceTrackerPlugin.CONFIG.rangedLevel() * this.specRangeLevelModifier)) * this.specChance;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getRangeStr() {
            return this.rangeStr;
        }

        public double getSpecRangeLevelModifier() {
            return this.specRangeLevelModifier;
        }

        public double getSpecChance() {
            return this.specChance;
        }

        @Override
        public double getDmgModifier() {
            return this.dmgModifier;
        }

        static {
            WEAPONS_USING = new EquipmentData[]{EquipmentData.ARMADYL_CROSSBOW, EquipmentData.DRAGON_CROSSBOW, EquipmentData.DRAGON_HUNTER_CROSSBOW};
        }
    }

    public static enum BoltAmmo implements RangeAmmoConfigData
    {
        RUNITE_BOLTS("Runite Bolts", 115, 1.0),
        DRAGONSTONE_BOLTS_E("Dstone Bolts (e)", 117, 0.2, 0.06, 1.0),
        DIAMOND_BOLTS_E("Diamond Bolts (e)", 105, 1.015);

        static EquipmentData[] WEAPONS_USING;
        private String name;
        private int rangeStr;
        private double specRangeLevelModifier;
        private double specChance;
        private double dmgModifier;

        private BoltAmmo(String name, int rangeStr, double specRangeLevelModifier, double specChance, double dmgModifier) {
            this.name = name;
            this.rangeStr = rangeStr;
            this.specRangeLevelModifier = specRangeLevelModifier;
            this.specChance = specChance;
            this.dmgModifier = dmgModifier;
        }

        private BoltAmmo(String name, int rangeStr, double dmgModifier) {
            this.name = name;
            this.rangeStr = rangeStr;
            this.specRangeLevelModifier = 0.0;
            this.specChance = 0.0;
            this.dmgModifier = dmgModifier;
        }

        @Override
        public double getBonusMaxHit() {
            return (double)((int)((double)PvpPerformanceTrackerPlugin.CONFIG.rangedLevel() * this.specRangeLevelModifier)) * this.specChance;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getRangeStr() {
            return this.rangeStr;
        }

        public double getSpecRangeLevelModifier() {
            return this.specRangeLevelModifier;
        }

        public double getSpecChance() {
            return this.specChance;
        }

        @Override
        public double getDmgModifier() {
            return this.dmgModifier;
        }

        static {
            WEAPONS_USING = new EquipmentData[]{EquipmentData.RUNE_CROSSBOW};
        }
    }

    public static interface RangeAmmoConfigData
    extends RangeAmmoData {
        public String getName();
    }
}

