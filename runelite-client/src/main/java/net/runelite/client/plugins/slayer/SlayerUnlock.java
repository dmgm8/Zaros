/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 *  net.runelite.api.VarPlayer
 */
package net.runelite.client.plugins.slayer;

import net.runelite.api.Client;
import net.runelite.api.VarPlayer;

final class SlayerUnlock
extends Enum<SlayerUnlock> {
    public static final /* enum */ SlayerUnlock GARGOYLE_SMASHER = new SlayerUnlock(0);
    public static final /* enum */ SlayerUnlock SLUG_SALTER = new SlayerUnlock(1);
    public static final /* enum */ SlayerUnlock REPTILE_FREEZER = new SlayerUnlock(2);
    public static final /* enum */ SlayerUnlock SHROOM_SPRAYER = new SlayerUnlock(3);
    public static final /* enum */ SlayerUnlock DARK_BEAST_EXTEND = new SlayerUnlock(4);
    public static final /* enum */ SlayerUnlock SLAYER_HELMET = new SlayerUnlock(5);
    public static final /* enum */ SlayerUnlock SLAYER_RINGS = new SlayerUnlock(6);
    public static final /* enum */ SlayerUnlock BROADER_FLETCHING = new SlayerUnlock(7);
    public static final /* enum */ SlayerUnlock ANKOU_EXTEND = new SlayerUnlock(8);
    public static final /* enum */ SlayerUnlock SUQAH_EXTEND = new SlayerUnlock(9);
    public static final /* enum */ SlayerUnlock BLACK_DRAGON_EXTEND = new SlayerUnlock(10);
    public static final /* enum */ SlayerUnlock METAL_DRAGON_EXTEND = new SlayerUnlock(11);
    public static final /* enum */ SlayerUnlock SPIRITUAL_MAGE_EXTEND = new SlayerUnlock(12);
    public static final /* enum */ SlayerUnlock ABYSSAL_DEMON_EXTEND = new SlayerUnlock(13);
    public static final /* enum */ SlayerUnlock BLACK_DEMON_EXTEND = new SlayerUnlock(14);
    public static final /* enum */ SlayerUnlock GREATER_DEMON_EXTEND = new SlayerUnlock(15);
    public static final /* enum */ SlayerUnlock MITHRIL_DRAGON_UNLOCK = new SlayerUnlock(16);
    public static final /* enum */ SlayerUnlock AVIANSIES_ENABLE = new SlayerUnlock(17);
    public static final /* enum */ SlayerUnlock TZHAAR_ENABLE = new SlayerUnlock(18);
    public static final /* enum */ SlayerUnlock BOSS_ENABLE = new SlayerUnlock(19);
    public static final /* enum */ SlayerUnlock BLOODVELD_EXTEND = new SlayerUnlock(20);
    public static final /* enum */ SlayerUnlock ABERRANT_SPECTRE_EXTEND = new SlayerUnlock(21);
    public static final /* enum */ SlayerUnlock AVIANSIES_EXTEND = new SlayerUnlock(22);
    public static final /* enum */ SlayerUnlock MITHRIL_DRAGON_EXTEND = new SlayerUnlock(23);
    public static final /* enum */ SlayerUnlock CAVE_HORROR_EXTEND = new SlayerUnlock(24);
    public static final /* enum */ SlayerUnlock DUST_DEVIL_EXTEND = new SlayerUnlock(25);
    public static final /* enum */ SlayerUnlock SKELETAL_WYVERN_EXTEND = new SlayerUnlock(26);
    public static final /* enum */ SlayerUnlock GARGOYLE_EXTEND = new SlayerUnlock(27);
    public static final /* enum */ SlayerUnlock NECHRYAEL_EXTEND = new SlayerUnlock(28);
    public static final /* enum */ SlayerUnlock CAVE_KRAKEN_EXTEND = new SlayerUnlock(29);
    public static final /* enum */ SlayerUnlock LIZARDMEN_ENABLE = new SlayerUnlock(30);
    public static final /* enum */ SlayerUnlock KBD_SLAYER_HELM = new SlayerUnlock(31);
    public static final /* enum */ SlayerUnlock KALPHITE_QUEEN_SLAYER_HELM = new SlayerUnlock(32);
    public static final /* enum */ SlayerUnlock ABYSSAL_DEMON_SAYER_HELM = new SlayerUnlock(33);
    public static final /* enum */ SlayerUnlock RED_DRAGON_ENABLE = new SlayerUnlock(34);
    public static final /* enum */ SlayerUnlock SUPERIOR_ENABLE = new SlayerUnlock(35, 5362);
    public static final /* enum */ SlayerUnlock SCABARITE_EXTEND = new SlayerUnlock(36);
    public static final /* enum */ SlayerUnlock MITHRIL_DRAGON_NOTES = new SlayerUnlock(37);
    public static final /* enum */ SlayerUnlock SKOTIZO_SLAYER_HELM = new SlayerUnlock(38);
    public static final /* enum */ SlayerUnlock FOSSIL_ISLAND_WYVERN_EXTEND = new SlayerUnlock(39);
    public static final /* enum */ SlayerUnlock ADAMANT_DRAGON_EXTEND = new SlayerUnlock(40);
    public static final /* enum */ SlayerUnlock RUNE_DRAGON_EXTEND = new SlayerUnlock(41);
    public static final /* enum */ SlayerUnlock VORKATH_SLAYER_HELM = new SlayerUnlock(42);
    public static final /* enum */ SlayerUnlock FOSSIL_ISLAND_WYVERN_DISABLE = new SlayerUnlock(43, 6251);
    public static final /* enum */ SlayerUnlock GROTESQUE_GUARDIAN_DOUBLE_COUNT = new SlayerUnlock(44);
    public static final /* enum */ SlayerUnlock HYDRA_SLAYER_HELM = new SlayerUnlock(45);
    public static final /* enum */ SlayerUnlock BASILISK_EXTEND = new SlayerUnlock(46);
    public static final /* enum */ SlayerUnlock BASILISK_UNLOCK = new SlayerUnlock(47);
    public static final /* enum */ SlayerUnlock OLM_SLAYER_HELM = new SlayerUnlock(48);
    public static final /* enum */ SlayerUnlock VAMPYRE_EXTEND = new SlayerUnlock(49);
    public static final /* enum */ SlayerUnlock VAMPYRE_UNLOCK = new SlayerUnlock(50);
    private final int toggleVarbit;
    private static final /* synthetic */ SlayerUnlock[] $VALUES;

    public static SlayerUnlock[] values() {
        return (SlayerUnlock[])$VALUES.clone();
    }

    public static SlayerUnlock valueOf(String name) {
        return Enum.valueOf(SlayerUnlock.class, name);
    }

    private SlayerUnlock(int index) {
        assert (index == this.ordinal());
        this.toggleVarbit = -1;
    }

    private SlayerUnlock(int index, int varbit) {
        assert (index == this.ordinal());
        this.toggleVarbit = varbit;
    }

    public boolean isOwned(Client client) {
        VarPlayer varp = this.ordinal() > 32 ? VarPlayer.SLAYER_UNLOCK_2 : VarPlayer.SLAYER_UNLOCK_1;
        return (client.getVarpValue(varp) & 1 << this.ordinal() % 32) != 0;
    }

    public boolean isEnabled(Client client) {
        if (this.isOwned(client)) {
            if (this.toggleVarbit == -1) {
                return true;
            }
            return client.getVarbitValue(this.toggleVarbit) == 0;
        }
        return false;
    }

    static {
        $VALUES = new SlayerUnlock[]{GARGOYLE_SMASHER, SLUG_SALTER, REPTILE_FREEZER, SHROOM_SPRAYER, DARK_BEAST_EXTEND, SLAYER_HELMET, SLAYER_RINGS, BROADER_FLETCHING, ANKOU_EXTEND, SUQAH_EXTEND, BLACK_DRAGON_EXTEND, METAL_DRAGON_EXTEND, SPIRITUAL_MAGE_EXTEND, ABYSSAL_DEMON_EXTEND, BLACK_DEMON_EXTEND, GREATER_DEMON_EXTEND, MITHRIL_DRAGON_UNLOCK, AVIANSIES_ENABLE, TZHAAR_ENABLE, BOSS_ENABLE, BLOODVELD_EXTEND, ABERRANT_SPECTRE_EXTEND, AVIANSIES_EXTEND, MITHRIL_DRAGON_EXTEND, CAVE_HORROR_EXTEND, DUST_DEVIL_EXTEND, SKELETAL_WYVERN_EXTEND, GARGOYLE_EXTEND, NECHRYAEL_EXTEND, CAVE_KRAKEN_EXTEND, LIZARDMEN_ENABLE, KBD_SLAYER_HELM, KALPHITE_QUEEN_SLAYER_HELM, ABYSSAL_DEMON_SAYER_HELM, RED_DRAGON_ENABLE, SUPERIOR_ENABLE, SCABARITE_EXTEND, MITHRIL_DRAGON_NOTES, SKOTIZO_SLAYER_HELM, FOSSIL_ISLAND_WYVERN_EXTEND, ADAMANT_DRAGON_EXTEND, RUNE_DRAGON_EXTEND, VORKATH_SLAYER_HELM, FOSSIL_ISLAND_WYVERN_DISABLE, GROTESQUE_GUARDIAN_DOUBLE_COUNT, HYDRA_SLAYER_HELM, BASILISK_EXTEND, BASILISK_UNLOCK, OLM_SLAYER_HELM, VAMPYRE_EXTEND, VAMPYRE_UNLOCK};
    }
}

