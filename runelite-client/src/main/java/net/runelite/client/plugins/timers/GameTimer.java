/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.runelite.api.VarPlayer
 */
package net.runelite.client.plugins.timers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.runelite.api.VarPlayer;
import net.runelite.client.plugins.timers.GameTimerImageType;
import net.runelite.client.util.RSTimeUnit;

enum GameTimer {
    STAMINA(12625, GameTimerImageType.ITEM, "Stamina", false),
    ANTIFIRE(2452, GameTimerImageType.ITEM, "Antifire", 6L, ChronoUnit.MINUTES),
    EXANTIFIRE(11951, GameTimerImageType.ITEM, "Extended antifire", 12L, ChronoUnit.MINUTES),
    OVERLOAD(11730, GameTimerImageType.ITEM, "Overload", 5L, ChronoUnit.MINUTES, true),
    CANNON(10, GameTimerImageType.ITEM, "Cannon", 25L, ChronoUnit.MINUTES),
    CANNON_REPAIR(1, GameTimerImageType.ITEM, "Broken Cannon", 10L, ChronoUnit.MINUTES),
    MAGICIMBUE(552, GameTimerImageType.SPRITE, "Magic imbue", 21L, RSTimeUnit.GAME_TICKS),
    SUPERANTIFIRE(21978, GameTimerImageType.ITEM, "Super antifire", 3L, ChronoUnit.MINUTES),
    BIND(319, GameTimerImageType.SPRITE, "Bind", 181, 8L, RSTimeUnit.GAME_TICKS, true),
    SNARE(320, GameTimerImageType.SPRITE, "Snare", 180, 16L, RSTimeUnit.GAME_TICKS, true),
    ENTANGLE(321, GameTimerImageType.SPRITE, "Entangle", 179, 24L, RSTimeUnit.GAME_TICKS, true),
    BINDING_TENTACLE(12006, GameTimerImageType.ITEM, "Binding Tentacle", 341, 8L, RSTimeUnit.GAME_TICKS, true),
    ICERUSH(325, GameTimerImageType.SPRITE, "Ice rush", 361, 8L, RSTimeUnit.GAME_TICKS, true),
    ICEBURST(326, GameTimerImageType.SPRITE, "Ice burst", 363, 16L, RSTimeUnit.GAME_TICKS, true),
    ICEBLITZ(327, GameTimerImageType.SPRITE, "Ice blitz", 367, 24L, RSTimeUnit.GAME_TICKS, true),
    ICEBARRAGE(328, GameTimerImageType.SPRITE, "Ice barrage", 369, 32L, RSTimeUnit.GAME_TICKS, true),
    IMBUEDHEART(20724, GameTimerImageType.ITEM, "Imbued heart", 420L, ChronoUnit.SECONDS, true),
    VENGEANCE(564, GameTimerImageType.SPRITE, "Vengeance", 30L, ChronoUnit.SECONDS),
    EXSUPERANTIFIRE(22209, GameTimerImageType.ITEM, "Extended Super AntiFire", 6L, ChronoUnit.MINUTES),
    OVERLOAD_RAID(20996, GameTimerImageType.ITEM, "Overload", 5L, ChronoUnit.MINUTES, true),
    PRAYER_ENHANCE(20964, GameTimerImageType.ITEM, "Prayer enhance", 290L, ChronoUnit.SECONDS, true),
    GOD_WARS_ALTAR(201, GameTimerImageType.SPRITE, "God wars altar", 10L, ChronoUnit.MINUTES),
    CHARGE(322, GameTimerImageType.SPRITE, "Charge", false),
    STAFF_OF_THE_DEAD(11791, GameTimerImageType.ITEM, "Staff of the Dead", 1L, ChronoUnit.MINUTES),
    ABYSSAL_SIRE_STUN(13262, GameTimerImageType.ITEM, "Abyssal Sire Stun", 30L, ChronoUnit.SECONDS, true),
    HOME_TELEPORT(356, GameTimerImageType.SPRITE, "Home Teleport", 30L, ChronoUnit.MINUTES),
    MINIGAME_TELEPORT(1053, GameTimerImageType.SPRITE, "Minigame Teleport", 20L, ChronoUnit.MINUTES),
    DRAGON_FIRE_SHIELD(11284, GameTimerImageType.ITEM, "Dragonfire Shield Special", 115L, ChronoUnit.SECONDS),
    DIVINE_SUPER_ATTACK(23697, GameTimerImageType.ITEM, "Divine Super Attack", 5L, ChronoUnit.MINUTES),
    DIVINE_SUPER_STRENGTH(23709, GameTimerImageType.ITEM, "Divine Super Strength", 5L, ChronoUnit.MINUTES),
    DIVINE_SUPER_DEFENCE(23721, GameTimerImageType.ITEM, "Divine Super Defence", 5L, ChronoUnit.MINUTES),
    DIVINE_SUPER_COMBAT(23685, GameTimerImageType.ITEM, "Divine Super Combat", 5L, ChronoUnit.MINUTES),
    DIVINE_RANGING(23733, GameTimerImageType.ITEM, "Divine Ranging", 5L, ChronoUnit.MINUTES),
    DIVINE_MAGIC(23745, GameTimerImageType.ITEM, "Divine Magic", 5L, ChronoUnit.MINUTES),
    DIVINE_BASTION(24635, GameTimerImageType.ITEM, "Divine Bastion", 5L, ChronoUnit.MINUTES),
    DIVINE_BATTLEMAGE(24623, GameTimerImageType.ITEM, "Divine Battlemage", 5L, ChronoUnit.MINUTES),
    ANTIPOISON(2446, GameTimerImageType.ITEM, "Antipoison", false),
    ANTIVENOM(12905, GameTimerImageType.ITEM, "Anti-venom", false),
    TELEBLOCK(352, GameTimerImageType.SPRITE, "Teleblock", false),
    SHADOW_VEIL(1315, GameTimerImageType.SPRITE, "Shadow veil", true),
    RESURRECT_THRALL(2981, GameTimerImageType.SPRITE, "Resurrect thrall", false),
    WARD_OF_ARCEUUS(1306, GameTimerImageType.SPRITE, "Ward of Arceuus", true),
    DEATH_CHARGE(1310, GameTimerImageType.SPRITE, "Death charge", false),
    SHADOW_VEIL_COOLDOWN(1334, GameTimerImageType.SPRITE, "Shadow veil cooldown", 30L, ChronoUnit.SECONDS),
    RESURRECT_THRALL_COOLDOWN(2987, GameTimerImageType.SPRITE, "Resurrect thrall cooldown", 12L, RSTimeUnit.GAME_TICKS),
    WARD_OF_ARCEUUS_COOLDOWN(1325, GameTimerImageType.SPRITE, "Ward of Arceuus cooldown", 30L, ChronoUnit.SECONDS),
    DEATH_CHARGE_COOLDOWN(1329, GameTimerImageType.SPRITE, "Death charge cooldown", 60L, ChronoUnit.SECONDS),
    CORRUPTION_COOLDOWN(1327, GameTimerImageType.SPRITE, "Corruption cooldown", 30L, ChronoUnit.SECONDS),
    PICKPOCKET_STUN(206, GameTimerImageType.SPRITE, "Stunned", true),
    SMELLING_SALTS(27343, GameTimerImageType.ITEM, "Smelling salts", true),
    LIQUID_ADRENALINE(27339, GameTimerImageType.ITEM, "Liquid adrenaline", 150L, ChronoUnit.SECONDS, true),
    SILK_DRESSING(27323, GameTimerImageType.ITEM, "Silk dressing", 100L, RSTimeUnit.GAME_TICKS, true),
    BLESSED_CRYSTAL_SCARAB(27335, GameTimerImageType.ITEM, "Blessed crystal scarab", 40L, RSTimeUnit.GAME_TICKS, true),
    BONUS_XP(30006, GameTimerImageType.ITEM, "Bonus XP", VarPlayer.GAMETIMER_BONUSXP, false),
    DROP_BOOST(30012, GameTimerImageType.ITEM, "Drop Rate Boost", VarPlayer.GAMETIMER_DROPBOOST, false),
    PET_BOOST(30014, GameTimerImageType.ITEM, "Pet Boost", VarPlayer.GAMETIMER_PETBOOST, false),
    CLUE_BONUS(30016, GameTimerImageType.ITEM, "Clue Bonus", false),
    RAID_BONUS(30018, GameTimerImageType.ITEM, "Raids Boost", false),
    GOODWILL(22521, GameTimerImageType.ITEM, "Well of Goodwill", false);

    private static final Map<Integer, GameTimer> VARP_TO_TIMER;
    @Nullable
    private final Duration duration;
    @Nullable
    private final Integer graphicId;
    private final String description;
    private final boolean removedOnDeath;
    private final int imageId;
    private final GameTimerImageType imageType;
    @Nullable
    private final VarPlayer varPlayer;

    public static GameTimer getTimerForVarp(int varp) {
        return VARP_TO_TIMER.get(varp);
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, Integer graphicId, long time, TemporalUnit unit, boolean removedOnDeath) {
        this.description = description;
        this.graphicId = graphicId;
        this.duration = Duration.of(time, unit);
        this.imageId = imageId;
        this.imageType = idType;
        this.removedOnDeath = removedOnDeath;
        this.varPlayer = null;
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, long time, TemporalUnit unit, boolean removeOnDeath) {
        this(imageId, idType, description, null, time, unit, removeOnDeath);
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, long time, TemporalUnit unit) {
        this(imageId, idType, description, null, time, unit, false);
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, boolean removedOnDeath) {
        this.duration = null;
        this.graphicId = null;
        this.description = description;
        this.removedOnDeath = removedOnDeath;
        this.imageId = imageId;
        this.imageType = idType;
        this.varPlayer = null;
    }

    private GameTimer(int imageId, GameTimerImageType idType, String description, VarPlayer varp, boolean removedOnDeath) {
        this.duration = null;
        this.graphicId = null;
        this.description = description;
        this.removedOnDeath = removedOnDeath;
        this.imageId = imageId;
        this.imageType = idType;
        this.varPlayer = varp;
    }

    @Nullable
    Duration getDuration() {
        return this.duration;
    }

    @Nullable
    Integer getGraphicId() {
        return this.graphicId;
    }

    String getDescription() {
        return this.description;
    }

    boolean isRemovedOnDeath() {
        return this.removedOnDeath;
    }

    int getImageId() {
        return this.imageId;
    }

    GameTimerImageType getImageType() {
        return this.imageType;
    }

    @Nullable
    VarPlayer getVarPlayer() {
        return this.varPlayer;
    }

    static {
        VARP_TO_TIMER = new HashMap<Integer, GameTimer>();
        for (GameTimer timer : GameTimer.values()) {
            if (timer.varPlayer == null) continue;
            VARP_TO_TIMER.put(timer.varPlayer.getId(), timer);
        }
    }
}

