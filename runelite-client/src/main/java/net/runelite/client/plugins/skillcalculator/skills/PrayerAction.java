/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.skillcalculator.skills;

import java.util.Arrays;
import java.util.EnumSet;
import net.runelite.client.plugins.skillcalculator.skills.ItemSkillAction;
import net.runelite.client.plugins.skillcalculator.skills.PrayerBonus;
import net.runelite.client.plugins.skillcalculator.skills.SkillBonus;

public enum PrayerAction implements ItemSkillAction
{
    ENSOULED_GOBLIN_HEAD(13447, 1, 130.0f, new PrayerBonus[0]),
    ENSOULED_MONKEY_HEAD(13450, 1, 182.0f, new PrayerBonus[0]),
    ENSOULED_IMP_HEAD(13453, 1, 286.0f, new PrayerBonus[0]),
    ENSOULED_MINOTAUR_HEAD(13456, 1, 364.0f, new PrayerBonus[0]),
    ENSOULED_SCORPION_HEAD(13459, 1, 454.0f, new PrayerBonus[0]),
    ENSOULED_BEAR_HEAD(13462, 1, 480.0f, new PrayerBonus[0]),
    ENSOULED_UNICORN_HEAD(13465, 1, 494.0f, new PrayerBonus[0]),
    ENSOULED_DOG_HEAD(13468, 1, 520.0f, new PrayerBonus[0]),
    ENSOULED_CHAOS_DRUID_HEAD(13471, 1, 584.0f, new PrayerBonus[0]),
    ENSOULED_GIANT_HEAD(13474, 1, 650.0f, new PrayerBonus[0]),
    ENSOULED_OGRE_HEAD(13477, 1, 716.0f, new PrayerBonus[0]),
    ENSOULED_ELF_HEAD(13480, 1, 754.0f, new PrayerBonus[0]),
    ENSOULED_TROLL_HEAD(13483, 1, 780.0f, new PrayerBonus[0]),
    ENSOULED_HORROR_HEAD(13486, 1, 832.0f, new PrayerBonus[0]),
    ENSOULED_KALPHITE_HEAD(13489, 1, 884.0f, new PrayerBonus[0]),
    ENSOULED_DAGANNOTH_HEAD(13492, 1, 936.0f, new PrayerBonus[0]),
    ENSOULED_BLOODVELD_HEAD(13495, 1, 1040.0f, new PrayerBonus[0]),
    ENSOULED_TZHAAR_HEAD(13498, 1, 1104.0f, new PrayerBonus[0]),
    ENSOULED_DEMON_HEAD(13501, 1, 1170.0f, new PrayerBonus[0]),
    ENSOULED_HELLHOUND_HEAD(26996, 1, 1200.0f, new PrayerBonus[0]),
    ENSOULED_AVIANSIE_HEAD(13504, 1, 1234.0f, new PrayerBonus[0]),
    ENSOULED_ABYSSAL_HEAD(13507, 1, 1300.0f, new PrayerBonus[0]),
    ENSOULED_DRAGON_HEAD(13510, 1, 1560.0f, new PrayerBonus[0]),
    FIENDISH_ASHES(25766, 1, 10.0f, PrayerBonus.DEMONIC_OFFERING),
    VILE_ASHES(25769, 1, 25.0f, PrayerBonus.DEMONIC_OFFERING),
    MALICIOUS_ASHES(25772, 1, 65.0f, PrayerBonus.DEMONIC_OFFERING),
    ABYSSAL_ASHES(25775, 1, 85.0f, PrayerBonus.DEMONIC_OFFERING),
    INFERNAL_ASHES(25778, 1, 110.0f, PrayerBonus.DEMONIC_OFFERING),
    BONES(526, 1, 4.5f, PrayerBonus.BONE_BONUSES),
    WOLF_BONES(2859, 1, 4.5f, PrayerBonus.BONE_BONUSES),
    LOAR_REMAINS(3396, 1, 33.0f, PrayerBonus.MORYTANIA_DIARY_3_SHADES),
    BURNT_BONES(528, 1, 4.5f, PrayerBonus.BONE_BONUSES),
    MONKEY_BONES(3183, 1, 5.0f, PrayerBonus.BONE_BONUSES),
    BAT_BONES(530, 1, 5.3f, PrayerBonus.BONE_BONUSES),
    JOGRE_BONES(3125, 1, 15.0f, PrayerBonus.BONE_BONUSES),
    BIG_BONES(532, 1, 15.0f, PrayerBonus.BONE_BONUSES),
    ZOGRE_BONES(4812, 1, 22.5f, PrayerBonus.BONE_BONUSES),
    SHAIKAHAN_BONES(3123, 1, 25.0f, PrayerBonus.BONE_BONUSES),
    BABYDRAGON_BONES(534, 1, 30.0f, PrayerBonus.BONE_BONUSES),
    PHRIN_REMAINS(3398, 1, 46.5f, PrayerBonus.MORYTANIA_DIARY_3_SHADES),
    WYRM_BONES(22780, 1, 50.0f, PrayerBonus.BONE_BONUSES),
    RIYL_REMAINS(3400, 1, 59.5f, PrayerBonus.MORYTANIA_DIARY_3_SHADES),
    WYVERN_BONES(6812, 1, 72.0f, PrayerBonus.BONE_BONUSES),
    DRAGON_BONES(536, 1, 72.0f, PrayerBonus.BONE_BONUSES),
    DRAKE_BONES(22783, 1, 80.0f, PrayerBonus.BONE_BONUSES),
    ASYN_REMAINS(3402, 1, 82.5f, PrayerBonus.MORYTANIA_DIARY_3_SHADES),
    FAYRG_BONES(4830, 1, 84.0f, PrayerBonus.BONE_BONUSES),
    FIYR_REMAINS(3404, 1, 84.0f, PrayerBonus.MORYTANIA_DIARY_3_SHADES),
    LAVA_DRAGON_BONES(11943, 1, 85.0f, PrayerBonus.BONE_BONUSES),
    RAURG_BONES(4832, 1, 96.0f, PrayerBonus.BONE_BONUSES),
    HYDRA_BONES(22786, 1, 110.0f, PrayerBonus.BONE_BONUSES),
    DAGANNOTH_BONES(6729, 1, 125.0f, PrayerBonus.BONE_BONUSES),
    OURG_BONES(4834, 1, 140.0f, PrayerBonus.BONE_BONUSES),
    URIUM_REMAINS(25419, 1, 120.0f, PrayerBonus.MORYTANIA_DIARY_3_SHADES),
    GUPPY(25654, 1, 4.0f, new PrayerBonus[0]),
    CAVEFISH(25660, 1, 7.0f, new PrayerBonus[0]),
    TETRA(25666, 1, 10.0f, new PrayerBonus[0]),
    CATFISH(25672, 1, 16.0f, new PrayerBonus[0]),
    SUPERIOR_DRAGON_BONES(22124, 70, 150.0f, PrayerBonus.BONE_BONUSES);

    private final int itemId;
    private final int level;
    private final float xp;
    private final EnumSet<PrayerBonus> applicableBonuses;

    private PrayerAction(int itemId, int level, float xp, PrayerBonus ... applicableBonuses) {
        this.itemId = itemId;
        this.level = level;
        this.xp = xp;
        this.applicableBonuses = EnumSet.noneOf(PrayerBonus.class);
        this.applicableBonuses.addAll(Arrays.asList(applicableBonuses));
    }

    @Override
    public boolean isBonusApplicable(SkillBonus skillBonus) {
        return skillBonus instanceof PrayerBonus && this.applicableBonuses.contains(skillBonus);
    }

    @Override
    public int getItemId() {
        return this.itemId;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public float getXp() {
        return this.xp;
    }

    public EnumSet<PrayerBonus> getApplicableBonuses() {
        return this.applicableBonuses;
    }
}

