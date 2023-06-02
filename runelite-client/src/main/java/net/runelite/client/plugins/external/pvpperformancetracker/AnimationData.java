/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.runelite.api.HeadIcon
 *  org.apache.commons.lang3.ArrayUtils
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import com.google.common.collect.ImmutableMap;
import java.security.InvalidParameterException;
import java.util.Map;
import net.runelite.api.HeadIcon;
import org.apache.commons.lang3.ArrayUtils;

public enum AnimationData {
    MELEE_DAGGER_SLASH(376, AttackStyle.SLASH),
    MELEE_SPEAR_STAB(381, AttackStyle.STAB),
    MELEE_SWORD_STAB(386, AttackStyle.STAB),
    MELEE_SCIM_SLASH(390, AttackStyle.CRUSH),
    MELEE_STAFF_CRUSH(393, AttackStyle.CRUSH),
    MELEE_BATTLEAXE_SLASH(395, AttackStyle.SLASH),
    MELEE_MACE_STAB(400, AttackStyle.STAB),
    MELEE_BATTLEAXE_CRUSH(401, AttackStyle.CRUSH),
    MELEE_2H_CRUSH(406, AttackStyle.CRUSH),
    MELEE_2H_SLASH(407, AttackStyle.SLASH),
    MELEE_STAFF_CRUSH_2(414, AttackStyle.CRUSH),
    MELEE_STAFF_CRUSH_3(419, AttackStyle.CRUSH),
    MELEE_PUNCH(422, AttackStyle.CRUSH),
    MELEE_KICK(423, AttackStyle.CRUSH),
    MELEE_STAFF_STAB(428, AttackStyle.STAB),
    MELEE_SPEAR_CRUSH(429, AttackStyle.CRUSH),
    MELEE_STAFF_SLASH(440, AttackStyle.SLASH),
    MELEE_SCEPTRE_CRUSH(1058, AttackStyle.CRUSH),
    MELEE_DRAGON_MACE_SPEC(1060, AttackStyle.CRUSH, true),
    MELEE_DRAGON_DAGGER_SPEC(1062, AttackStyle.SLASH, true),
    MELEE_DRAGON_WARHAMMER_SPEC(1378, AttackStyle.CRUSH, true),
    MELEE_ABYSSAL_WHIP(1658, AttackStyle.SLASH),
    MELEE_GRANITE_MAUL(1665, AttackStyle.CRUSH),
    MELEE_GRANITE_MAUL_SPEC(1667, AttackStyle.CRUSH, true),
    MELEE_DHAROKS_GREATAXE_CRUSH(2066, AttackStyle.CRUSH),
    MELEE_DHAROKS_GREATAXE_SLASH(2067, AttackStyle.SLASH),
    MELEE_AHRIMS_STAFF_CRUSH(2078, AttackStyle.CRUSH),
    MELEE_OBBY_MAUL_CRUSH(2661, AttackStyle.CRUSH),
    MELEE_ABYSSAL_DAGGER_STAB(3297, AttackStyle.STAB),
    MELEE_ABYSSAL_BLUDGEON_CRUSH(3298, AttackStyle.CRUSH),
    MELEE_LEAF_BLADED_BATTLEAXE_CRUSH(3852, AttackStyle.CRUSH),
    MELEE_BARRELCHEST_ANCHOR_CRUSH(5865, AttackStyle.CRUSH),
    MELEE_LEAF_BLADED_BATTLEAXE_SLASH(7004, AttackStyle.SLASH),
    MELEE_GODSWORD_SLASH(7045, AttackStyle.SLASH),
    MELEE_GODSWORD_CRUSH(7054, AttackStyle.CRUSH),
    MELEE_DRAGON_CLAWS_SPEC(7514, AttackStyle.SLASH, true),
    MELEE_DRAGON_SWORD_SPEC(7515, AttackStyle.STAB, true),
    MELEE_ELDER_MAUL(7516, AttackStyle.CRUSH),
    MELEE_ZAMORAK_GODSWORD_SPEC(7638, AttackStyle.SLASH, true),
    MELEE_SARADOMIN_GODSWORD_SPEC(7640, AttackStyle.SLASH, true),
    MELEE_BANDOS_GODSWORD_SPEC(7642, AttackStyle.SLASH, true),
    MELEE_ARMADYL_GODSWORD_SPEC(7644, AttackStyle.SLASH, true),
    MELEE_SCYTHE(8056, AttackStyle.SLASH),
    MELEE_GHAZI_RAPIER_STAB(8145, AttackStyle.STAB),
    RANGED_SHORTBOW(426, AttackStyle.RANGED),
    RANGED_RUNE_KNIFE_PVP(929, AttackStyle.RANGED),
    RANGED_MAGIC_SHORTBOW_SPEC(1074, AttackStyle.RANGED, true),
    RANGED_CROSSBOW_PVP(4230, AttackStyle.RANGED),
    RANGED_BLOWPIPE(5061, AttackStyle.RANGED),
    RANGED_DARTS(6600, AttackStyle.RANGED),
    RANGED_BALLISTA(7218, AttackStyle.RANGED),
    RANGED_DRAGON_THROWNAXE_SPEC(7521, AttackStyle.RANGED, true),
    RANGED_RUNE_CROSSBOW(7552, AttackStyle.RANGED),
    RANGED_BALLISTA_2(7555, AttackStyle.RANGED),
    RANGED_RUNE_KNIFE(7617, AttackStyle.RANGED),
    RANGED_DRAGON_KNIFE(8194, AttackStyle.RANGED),
    RANGED_DRAGON_KNIFE_POISONED(8195, AttackStyle.RANGED),
    RANGED_DRAGON_KNIFE_SPEC(8292, AttackStyle.RANGED, true),
    MAGIC_STANDARD_BIND(710, AttackStyle.MAGIC),
    MAGIC_STANDARD_STRIKE_BOLT_BLAST(711, AttackStyle.MAGIC, 16),
    MAGIC_STANDARD_BIND_STAFF(1161, AttackStyle.MAGIC),
    MAGIC_STANDARD_STRIKE_BOLT_BLAST_STAFF(1162, AttackStyle.MAGIC, 16),
    MAGIC_STANDARD_WAVE_STAFF(1167, AttackStyle.MAGIC, 20),
    MAGIC_STANDARD_SURGE_STAFF(7855, AttackStyle.MAGIC, 24),
    MAGIC_ANCIENT_SINGLE_TARGET(1978, AttackStyle.MAGIC, 26),
    MAGIC_ANCIENT_MULTI_TARGET(1979, AttackStyle.MAGIC, 30);

    private static final Map<Integer, AnimationData> DATA;
    int animationId;
    boolean isSpecial;
    AttackStyle attackStyle;
    int baseSpellDamage;

    private AnimationData(int animationId, AttackStyle attackStyle) {
        if (attackStyle == null) {
            throw new InvalidParameterException("Attack Style must be valid for AnimationData");
        }
        this.animationId = animationId;
        this.attackStyle = attackStyle;
        this.isSpecial = false;
        this.baseSpellDamage = 0;
    }

    private AnimationData(int animationId, AttackStyle attackStyle, boolean isSpecial) {
        if (attackStyle == null) {
            throw new InvalidParameterException("Attack Style must be valid for AnimationData");
        }
        this.animationId = animationId;
        this.attackStyle = attackStyle;
        this.isSpecial = isSpecial;
        this.baseSpellDamage = 0;
    }

    private AnimationData(int animationId, AttackStyle attackStyle, int baseSpellDamage) {
        if (attackStyle == null) {
            throw new InvalidParameterException("Attack Style and Attack Type must be valid for AnimationData");
        }
        this.animationId = animationId;
        this.attackStyle = attackStyle;
        this.isSpecial = false;
        this.baseSpellDamage = baseSpellDamage;
    }

    public static AnimationData dataForAnimation(int animationId) {
        return DATA.get(animationId);
    }

    public static boolean isStandardSpellbookSpell(AnimationData animationData) {
        return animationData == MAGIC_STANDARD_STRIKE_BOLT_BLAST_STAFF || animationData == MAGIC_STANDARD_WAVE_STAFF || animationData == MAGIC_STANDARD_SURGE_STAFF;
    }

    public static boolean isFireSpell(AnimationData animationData) {
        return animationData == MAGIC_STANDARD_STRIKE_BOLT_BLAST_STAFF || animationData == MAGIC_STANDARD_STRIKE_BOLT_BLAST || animationData == MAGIC_STANDARD_WAVE_STAFF || animationData == MAGIC_STANDARD_SURGE_STAFF;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (AnimationData data : AnimationData.values()) {
            builder.put((Object)data.animationId, (Object)data);
        }
        DATA = builder.build();
    }

    public static enum AttackStyle {
        STAB(HeadIcon.MELEE),
        SLASH(HeadIcon.MELEE),
        CRUSH(HeadIcon.MELEE),
        RANGED(HeadIcon.RANGED),
        MAGIC(HeadIcon.MAGIC);

        static AttackStyle[] MELEE_STYLES;
        private final HeadIcon protection;

        private AttackStyle(HeadIcon protection) {
            this.protection = protection;
        }

        public boolean isMelee() {
            return ArrayUtils.contains((Object[])MELEE_STYLES, (Object)((Object)this));
        }

        public boolean isUsingSuccessfulOffensivePray(int pray) {
            return pray > 0 && (this.isMelee() && (pray == 946 || pray == 125) || this == RANGED && (pray == 1420 || pray == 504) || this == MAGIC && (pray == 1421 || pray == 505));
        }

        public HeadIcon getProtection() {
            return this.protection;
        }

        static {
            MELEE_STYLES = new AttackStyle[]{STAB, SLASH, CRUSH};
        }
    }
}

