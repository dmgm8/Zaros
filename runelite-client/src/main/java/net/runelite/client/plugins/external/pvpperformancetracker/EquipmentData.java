/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.ArrayUtils
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import java.util.HashMap;
import java.util.Map;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;
import net.runelite.client.plugins.external.pvpperformancetracker.RangeAmmoData;
import org.apache.commons.lang3.ArrayUtils;

public enum EquipmentData {
    DRAGON_DAGGER_P(1231),
    DRAGON_DAGGER_PP(5680),
    DRAGON_DAGGER_PPP(5698),
    DRAGON_CROSSBOW(21902),
    DRAGON_HUNTER_CROSSBOW(21012),
    LIGHT_BALLISTA(19478),
    MAGIC_SHORTBOW(861),
    MAGIC_SHORTBOW_I(12788),
    TOXIC_BLOWPIPE(12926),
    BLIGHTED_VESTAS_LONGSWORD(24617),
    SMOKE_BATTLESTAFF(11998),
    TOME_OF_FIRE(20714),
    VOID_MAGE_HELM(11663, 11674),
    VOID_RANGE_HELM(11664, 11675),
    VOID_MELEE_HELM(11665, 11676),
    VOID_ELITE_BODY(13072),
    VOID_ELITE_LEGS(13073),
    VOID_GLOVES(8842),
    VOID_BODY(8839, 10611),
    VOID_LEGS(8840),
    RUNE_CROSSBOW(23601, 9185),
    ARMADYL_CROSSBOW(23611, 11785),
    DARK_BOW(20408, 11235),
    HEAVY_BALLISTA(23630, 19481),
    STATIUS_WARHAMMER(23620, 22622),
    VESTAS_LONGSWORD(23615, 22613),
    ARMADYL_GODSWORD(20593, 11802),
    DRAGON_CLAWS(20784, 13652),
    DRAGON_DAGGER(20407, 1215),
    GRANITE_MAUL(20557, 4153),
    AMULET_OF_FURY(23640, 6585),
    BANDOS_TASSETS(23646, 11834),
    BLESSED_SPIRIT_SHIELD(23642, 12831),
    DHAROKS_HELM(23639, 4716),
    DHAROKS_PLATELEGS(23633, 4722),
    GUTHANS_HELM(23638, 4724),
    KARILS_TOP(23632, 4736),
    TORAGS_HELM(23637, 4745),
    TORAGS_PLATELEGS(23634, 4751),
    VERACS_HELM(23636, 4753),
    VERACS_PLATESKIRT(23635, 4759),
    MORRIGANS_JAVELIN(23619, 22636),
    SPIRIT_SHIELD(23599, 12829),
    HELM_OF_NEITIZNOT(23591, 10828),
    AMULET_OF_GLORY(20586, 1704),
    ABYSSAL_WHIP(20405, 4151),
    DRAGON_DEFENDER(23597, 12954),
    BLACK_DHIDE_BODY(20423, 2503),
    RUNE_PLATELEGS(20422, 1079),
    ROCK_CLIMBING_BOOTS(20578, 2203),
    BARROWS_GLOVES(23593, 7462),
    ELDER_MAUL(21205, 21003),
    INFERNAL_CAPE(23622, 21295),
    GHRAZI_RAPIER(23628, 22324),
    ZURIELS_STAFF(23617, 22647),
    STAFF_OF_THE_DEAD(23613, 11791),
    KODAI_WAND(23626, 21006),
    AHRIMS_STAFF(23653, 4710),
    MYSTIC_ROBE_TOP(20425, 4091),
    MYSTIC_ROBE_BOTTOM(20426, 4093),
    AHRIMS_ROBE_TOP(20598, 4712),
    AHRIMS_ROBE_SKIRT(20599, 4714),
    OCCULT_NECKLACE(23654, 12002),
    MAGES_BOOK(23652, 6889),
    ETERNAL_BOOTS(23644, 13235),
    IMBUED_ZAMORAK_CAPE(23605, 21795),
    IMBUED_GUTHIX_CAPE(23603, 21793),
    IMBUED_SARADOMIN_CAPE(23607, 21791);

    public static final EquipmentData[] DRAGON_DAGGERS;
    private static final Map<Integer, EquipmentData> itemData;
    private final int lmsItemId;
    private final int itemId;

    private EquipmentData(int lmsItemId, int itemId) {
        this.lmsItemId = lmsItemId;
        this.itemId = itemId;
    }

    private EquipmentData(int itemId) {
        this.lmsItemId = itemId;
        this.itemId = itemId;
    }

    public static EquipmentData getEquipmentDataFor(int itemId) {
        return itemData.get(itemId);
    }

    public static RangeAmmoData getWeaponAmmo(EquipmentData weapon) {
        if (ArrayUtils.contains((Object[])RangeAmmoData.BoltAmmo.WEAPONS_USING, (Object)((Object)weapon))) {
            return PvpPerformanceTrackerPlugin.CONFIG.boltChoice();
        }
        if (ArrayUtils.contains((Object[])RangeAmmoData.StrongBoltAmmo.WEAPONS_USING, (Object)((Object)weapon))) {
            return PvpPerformanceTrackerPlugin.CONFIG.strongBoltChoice();
        }
        if (ArrayUtils.contains((Object[])RangeAmmoData.DartAmmo.WEAPONS_USING, (Object)((Object)weapon))) {
            return PvpPerformanceTrackerPlugin.CONFIG.bpDartChoice();
        }
        if (weapon == HEAVY_BALLISTA || weapon == LIGHT_BALLISTA) {
            return RangeAmmoData.OtherAmmo.DRAGON_JAVELIN;
        }
        if (weapon == DARK_BOW) {
            return RangeAmmoData.OtherAmmo.DRAGON_ARROW;
        }
        if (weapon == MAGIC_SHORTBOW || weapon == MAGIC_SHORTBOW_I) {
            return RangeAmmoData.OtherAmmo.AMETHYST_ARROWS;
        }
        return null;
    }

    public int getLmsItemId() {
        return this.lmsItemId;
    }

    public int getItemId() {
        return this.itemId;
    }

    static {
        DRAGON_DAGGERS = new EquipmentData[]{DRAGON_DAGGER, DRAGON_DAGGER_P, DRAGON_DAGGER_PP, DRAGON_DAGGER_PPP};
        itemData = new HashMap<Integer, EquipmentData>();
        for (EquipmentData data : EquipmentData.values()) {
            itemData.put(data.getLmsItemId(), data);
            if (itemData.containsKey(data.getItemId())) continue;
            itemData.put(data.getItemId(), data);
        }
    }
}

