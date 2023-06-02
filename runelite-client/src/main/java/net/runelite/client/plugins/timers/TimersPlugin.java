/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.EquipmentInventorySlot
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Skill
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.ActorDeath
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.GraphicChanged
 *  net.runelite.api.events.ItemContainerChanged
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.VarbitChanged
 *  org.apache.commons.lang3.ArrayUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.timers;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.timers.ElapsedTimer;
import net.runelite.client.plugins.timers.GameIndicator;
import net.runelite.client.plugins.timers.GameTimer;
import net.runelite.client.plugins.timers.IndicatorIndicator;
import net.runelite.client.plugins.timers.TimerTimer;
import net.runelite.client.plugins.timers.TimersConfig;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.RSTimeUnit;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Timers", description="Show various timers in an infobox", tags={"combat", "items", "magic", "potions", "prayer", "overlay", "abyssal", "sire", "inferno", "fight", "caves", "cape", "timer", "tzhaar", "thieving", "pickpocket"})
public class TimersPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(TimersPlugin.class);
    public static final Duration UNLIMITED_DURATION = Duration.ofDays(32767L);
    private static final String ABYSSAL_SIRE_STUN_MESSAGE = "The Sire has been disorientated temporarily.";
    private static final String ANTIFIRE_DRINK_MESSAGE = "You drink some of your antifire potion.";
    private static final String ANTIFIRE_EXPIRED_MESSAGE = "<col=7f007f>Your antifire potion has expired.</col>";
    private static final String CANNON_BASE_MESSAGE = "You place the cannon base on the ground.";
    private static final String CANNON_STAND_MESSAGE = "You add the stand.";
    private static final String CANNON_BARRELS_MESSAGE = "You add the barrels.";
    private static final String CANNON_FURNACE_MESSAGE = "You add the furnace.";
    private static final String CANNON_PICKUP_MESSAGE = "You pick up the cannon. It's really heavy.";
    private static final String CANNON_REPAIR_MESSAGE = "You repair your cannon, restoring it to working order.";
    private static final String CANNON_DESTROYED_MESSAGE = "Your cannon has been destroyed!";
    private static final String CANNON_BROKEN_MESSAGE = "<col=ef1020>Your cannon has broken!";
    private static final String EXTENDED_ANTIFIRE_DRINK_MESSAGE = "You drink some of your extended antifire potion.";
    private static final String EXTENDED_SUPER_ANTIFIRE_DRINK_MESSAGE = "You drink some of your extended super antifire potion.";
    private static final String FROZEN_MESSAGE = "<col=ef1020>You have been frozen!</col>";
    private static final String GOD_WARS_ALTAR_MESSAGE = "you recharge your prayer.";
    private static final String MAGIC_IMBUE_EXPIRED_MESSAGE = "Your Magic Imbue charge has ended.";
    private static final String MAGIC_IMBUE_MESSAGE = "You are charged to combine runes!";
    private static final String STAFF_OF_THE_DEAD_SPEC_EXPIRED_MESSAGE = "Your protection fades away";
    private static final String STAFF_OF_THE_DEAD_SPEC_MESSAGE = "Spirits of deceased evildoers offer you their protection";
    private static final String SUPER_ANTIFIRE_DRINK_MESSAGE = "You drink some of your super antifire potion";
    private static final String SUPER_ANTIFIRE_EXPIRED_MESSAGE = "<col=7f007f>Your super antifire potion has expired.</col>";
    private static final String PRAYER_ENHANCE_EXPIRED = "<col=ff0000>Your prayer enhance effect has worn off.</col>";
    private static final String SHADOW_VEIL_MESSAGE = ">Your thieving abilities have been enhanced.</col>";
    private static final String DEATH_CHARGE_MESSAGE = ">Upon the death of your next foe, some of your special attack energy will be restored.</col>";
    private static final String DEATH_CHARGE_ACTIVATE_MESSAGE = ">Some of your special attack energy has been restored.</col>";
    private static final String RESURRECT_THRALL_MESSAGE_START = ">You resurrect a ";
    private static final String RESURRECT_THRALL_MESSAGE_END = " thrall.</col>";
    private static final String RESURRECT_THRALL_DISAPPEAR_MESSAGE_START = ">Your ";
    private static final String RESURRECT_THRALL_DISAPPEAR_MESSAGE_END = " thrall returns to the grave.</col>";
    private static final String WARD_OF_ARCEUUS_MESSAGE = ">Your defence against Arceuus magic has been strengthened.</col>";
    private static final String PICKPOCKET_FAILURE_MESSAGE = "You fail to pick ";
    private static final String DODGY_NECKLACE_PROTECTION_MESSAGE = "Your dodgy necklace protects you.";
    private static final String SHADOW_VEIL_PROTECTION_MESSAGE = "Your attempt to steal goes unnoticed.";
    private static final String SILK_DRESSING_MESSAGE = "You quickly apply the dressing to your wounds.";
    private static final String BLESSED_CRYSTAL_SCARAB_MESSAGE = "You crack the crystal in your hand.";
    private static final String LIQUID_ADRENALINE_MESSAGE = "You drink some of the potion, reducing the energy cost of your special attacks.</col>";
    private static final Pattern DIVINE_POTION_PATTERN = Pattern.compile("You drink some of your divine (.+) potion\\.");
    private static final int VENOM_VALUE_CUTOFF = -40;
    private static final int POISON_TICK_LENGTH = 30;
    static final int FIGHT_CAVES_REGION_ID = 9551;
    static final int INFERNO_REGION_ID = 9043;
    private static final int NMZ_MAP_REGION_ID = 9033;
    private static final Pattern TZHAAR_WAVE_MESSAGE = Pattern.compile("Wave: (\\d+)");
    private static final String TZHAAR_DEFEATED_MESSAGE = "You have been defeated!";
    private static final Pattern TZHAAR_PAUSED_MESSAGE = Pattern.compile("The (?:Inferno|Fight Cave) has been paused. You may now log out.");
    private TimerTimer freezeTimer;
    private int freezeTime = -1;
    private TimerTimer staminaTimer;
    private TimerTimer buffTimer;
    private boolean imbuedHeartTimerActive;
    private int nextPoisonTick;
    private Map<Integer, TimerTimer> varpTimers = new HashMap<Integer, TimerTimer>();
    private Map<Integer, Integer> varpTimersLastTimes = new HashMap<Integer, Integer>();
    private WorldPoint lastPoint;
    private int lastAnimation;
    private ElapsedTimer tzhaarTimer;
    @Inject
    private ItemManager itemManager;
    @Inject
    private SpriteManager spriteManager;
    @Inject
    private Client client;
    @Inject
    private TimersConfig config;
    @Inject
    private InfoBoxManager infoBoxManager;

    @Provides
    TimersConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(TimersConfig.class);
    }

    @Override
    public void startUp() {
        if (this.config.showHomeMinigameTeleports()) {
            this.checkTeleport(VarPlayer.LAST_HOME_TELEPORT);
            this.checkTeleport(VarPlayer.LAST_MINIGAME_TELEPORT);
        }
    }

    @Override
    protected void shutDown() throws Exception {
        this.infoBoxManager.removeIf(t -> t instanceof TimerTimer);
        this.lastPoint = null;
        this.lastAnimation = -1;
        this.nextPoisonTick = 0;
        this.removeTzhaarTimer();
        this.removeVarpTimers();
        this.staminaTimer = null;
        this.imbuedHeartTimerActive = false;
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        if (event.getVarbitId() == 5432) {
            this.removeGameTimer(GameTimer.OVERLOAD_RAID);
            this.removeGameTimer(GameTimer.PRAYER_ENHANCE);
        }
        if (event.getVarbitId() == 2451 && this.config.showVengeance()) {
            if (event.getValue() == 1) {
                this.createGameTimer(GameTimer.VENGEANCE);
            } else {
                this.removeGameTimer(GameTimer.VENGEANCE);
            }
        }
        if (event.getVarbitId() == 12288 && this.config.showArceuusCooldown()) {
            if (event.getValue() == 1) {
                this.createGameTimer(GameTimer.CORRUPTION_COOLDOWN);
            } else {
                this.removeGameTimer(GameTimer.CORRUPTION_COOLDOWN);
            }
        }
        if (event.getVarbitId() == 2450 && this.config.showVengeanceActive()) {
            if (event.getValue() == 1) {
                this.createGameIndicator(GameIndicator.VENGEANCE_ACTIVE);
            } else {
                this.removeGameIndicator(GameIndicator.VENGEANCE_ACTIVE);
            }
        }
        if (event.getVarpId() == VarPlayer.POISON.getId() && this.config.showAntiPoison()) {
            Duration duration;
            int poisonVarp = event.getValue();
            int tickCount = this.client.getTickCount();
            if (poisonVarp == 0) {
                this.nextPoisonTick = -1;
            } else if (this.nextPoisonTick - tickCount <= 0) {
                this.nextPoisonTick = tickCount + 30;
            }
            if (poisonVarp >= 0) {
                this.removeGameTimer(GameTimer.ANTIPOISON);
                this.removeGameTimer(GameTimer.ANTIVENOM);
            } else if (poisonVarp >= -40) {
                duration = Duration.ofMillis(600L * (long)(this.nextPoisonTick - tickCount + Math.abs((poisonVarp + 1) * 30)));
                this.removeGameTimer(GameTimer.ANTIVENOM);
                this.createGameTimer(GameTimer.ANTIPOISON, duration);
            } else {
                duration = Duration.ofMillis(600L * (long)(this.nextPoisonTick - tickCount + Math.abs((poisonVarp + 1 - -40) * 30)));
                this.removeGameTimer(GameTimer.ANTIPOISON);
                this.createGameTimer(GameTimer.ANTIVENOM, duration);
            }
        }
        if (event.getVarbitId() == 4163 && this.config.showTeleblock()) {
            int teleblockVarb = event.getValue();
            if (teleblockVarb > 100) {
                this.createGameTimer(GameTimer.TELEBLOCK, Duration.of(teleblockVarb - 100, RSTimeUnit.GAME_TICKS));
            } else {
                this.removeGameTimer(GameTimer.TELEBLOCK);
            }
        }
        if (event.getVarpId() == VarPlayer.CHARGE_GOD_SPELL.getId() && this.config.showCharge()) {
            int chargeSpellVarp = event.getValue();
            if (chargeSpellVarp > 0) {
                this.createGameTimer(GameTimer.CHARGE, Duration.of((long)chargeSpellVarp * 2L, RSTimeUnit.GAME_TICKS));
            } else {
                this.removeGameTimer(GameTimer.CHARGE);
            }
        }
        if (event.getVarbitId() == 5361 && this.config.showImbuedHeart()) {
            int imbuedHeartCooldownVarb = event.getValue();
            if (imbuedHeartCooldownVarb == 0) {
                this.removeGameTimer(GameTimer.IMBUEDHEART);
                this.imbuedHeartTimerActive = false;
            } else if (!this.imbuedHeartTimerActive) {
                this.createGameTimer(GameTimer.IMBUEDHEART, Duration.of(10L * (long)imbuedHeartCooldownVarb, RSTimeUnit.GAME_TICKS));
                this.imbuedHeartTimerActive = true;
            }
        }
        if (event.getVarpId() == VarPlayer.LAST_HOME_TELEPORT.getId() && this.config.showHomeMinigameTeleports()) {
            this.checkTeleport(VarPlayer.LAST_HOME_TELEPORT);
        }
        if (event.getVarpId() == VarPlayer.LAST_MINIGAME_TELEPORT.getId() && this.config.showHomeMinigameTeleports()) {
            this.checkTeleport(VarPlayer.LAST_MINIGAME_TELEPORT);
        }
        if (event.getVarbitId() == 25 || event.getVarbitId() == 24 || event.getVarbitId() == 10385) {
            int staminaEffectActive = this.client.getVarbitValue(25);
            int staminaPotionEffectVarb = this.client.getVarbitValue(24);
            int enduranceRingEffectVarb = this.client.getVarbitValue(10385);
            int totalStaminaEffect = staminaPotionEffectVarb + enduranceRingEffectVarb;
            if (staminaEffectActive == 1 && this.config.showStamina()) {
                Duration staminaDuration = Duration.of(10L * (long)totalStaminaEffect, RSTimeUnit.GAME_TICKS);
                if (totalStaminaEffect == 0) {
                    this.removeGameTimer(GameTimer.STAMINA);
                    this.staminaTimer = null;
                } else if (this.staminaTimer == null) {
                    this.staminaTimer = this.createGameTimer(GameTimer.STAMINA, staminaDuration);
                } else {
                    this.staminaTimer.updateDuration(staminaDuration);
                }
            }
        }
        if (event.getVarbitId() == 14344 && this.config.showOverload()) {
            int serverTicks = event.getValue() * 25;
            Duration duration = Duration.of(serverTicks, RSTimeUnit.GAME_TICKS);
            if (serverTicks == 0) {
                this.removeGameTimer(GameTimer.SMELLING_SALTS);
                this.buffTimer = null;
            } else if (this.buffTimer == null) {
                this.buffTimer = this.createGameTimer(GameTimer.SMELLING_SALTS, duration);
            } else {
                this.buffTimer.updateDuration(duration);
            }
        }
        if (event.getVarbitId() == 14361 && this.config.showLiquidAdrenaline()) {
            if (event.getValue() == 1) {
                this.createGameTimer(GameTimer.LIQUID_ADRENALINE);
            } else {
                this.removeGameTimer(GameTimer.LIQUID_ADRENALINE);
            }
        }
        GameTimer varpTimer = GameTimer.getTimerForVarp(event.getVarpId());
        if (this.config.showPlayerBoosts() && varpTimer != null) {
            TimerTimer timer;
            int time = this.client.getVarpValue(varpTimer.getVarPlayer());
            int previousTime = this.varpTimersLastTimes.getOrDefault(event.getVarpId(), Integer.MIN_VALUE);
            Duration duration = Duration.ofMillis(600 * time);
            if (time == -1) {
                duration = UNLIMITED_DURATION;
            }
            if (previousTime == Integer.MIN_VALUE && time != 0) {
                timer = this.createGameTimer(varpTimer, duration);
                if (duration == UNLIMITED_DURATION) {
                    timer.syncDuration(duration);
                }
                this.varpTimers.put(event.getVarpId(), timer);
                this.varpTimersLastTimes.put(event.getVarpId(), time);
                return;
            }
            if (time != previousTime) {
                if (time == 0) {
                    this.removeGameTimer(varpTimer);
                    this.varpTimers.remove(event.getVarpId());
                    this.varpTimersLastTimes.remove(event.getVarpId());
                    return;
                }
                timer = this.varpTimers.get(event.getVarpId());
                timer.syncDuration(duration);
                this.varpTimersLastTimes.put(event.getVarpId(), time);
            }
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals("timers")) {
            return;
        }
        if (!this.config.showHomeMinigameTeleports()) {
            this.removeGameTimer(GameTimer.HOME_TELEPORT);
            this.removeGameTimer(GameTimer.MINIGAME_TELEPORT);
        } else {
            this.checkTeleport(VarPlayer.LAST_HOME_TELEPORT);
            this.checkTeleport(VarPlayer.LAST_MINIGAME_TELEPORT);
        }
        if (!this.config.showAntiFire()) {
            this.removeGameTimer(GameTimer.ANTIFIRE);
            this.removeGameTimer(GameTimer.EXANTIFIRE);
            this.removeGameTimer(GameTimer.SUPERANTIFIRE);
        }
        if (!this.config.showStamina()) {
            this.removeGameTimer(GameTimer.STAMINA);
            this.staminaTimer = null;
        }
        if (!this.config.showOverload()) {
            this.removeGameTimer(GameTimer.OVERLOAD);
            this.removeGameTimer(GameTimer.OVERLOAD_RAID);
            this.removeGameTimer(GameTimer.SMELLING_SALTS);
        }
        if (!this.config.showPrayerEnhance()) {
            this.removeGameTimer(GameTimer.PRAYER_ENHANCE);
        }
        if (!this.config.showDivine()) {
            this.removeGameTimer(GameTimer.DIVINE_SUPER_ATTACK);
            this.removeGameTimer(GameTimer.DIVINE_SUPER_STRENGTH);
            this.removeGameTimer(GameTimer.DIVINE_SUPER_DEFENCE);
            this.removeGameTimer(GameTimer.DIVINE_SUPER_COMBAT);
            this.removeGameTimer(GameTimer.DIVINE_RANGING);
            this.removeGameTimer(GameTimer.DIVINE_MAGIC);
        }
        if (!this.config.showCannon()) {
            this.removeGameTimer(GameTimer.CANNON);
        }
        if (!this.config.showMagicImbue()) {
            this.removeGameTimer(GameTimer.MAGICIMBUE);
        }
        if (!this.config.showCharge()) {
            this.removeGameTimer(GameTimer.CHARGE);
        }
        if (!this.config.showImbuedHeart()) {
            this.removeGameTimer(GameTimer.IMBUEDHEART);
            this.imbuedHeartTimerActive = false;
        }
        if (!this.config.showStaffOfTheDead()) {
            this.removeGameTimer(GameTimer.STAFF_OF_THE_DEAD);
        }
        if (!this.config.showVengeance()) {
            this.removeGameTimer(GameTimer.VENGEANCE);
        }
        if (!this.config.showVengeanceActive()) {
            this.removeGameIndicator(GameIndicator.VENGEANCE_ACTIVE);
        }
        if (!this.config.showTeleblock()) {
            this.removeGameTimer(GameTimer.TELEBLOCK);
        }
        if (!this.config.showFreezes()) {
            this.removeGameTimer(GameTimer.BIND);
            this.removeGameTimer(GameTimer.SNARE);
            this.removeGameTimer(GameTimer.ENTANGLE);
            this.removeGameTimer(GameTimer.ICERUSH);
            this.removeGameTimer(GameTimer.ICEBURST);
            this.removeGameTimer(GameTimer.ICEBLITZ);
            this.removeGameTimer(GameTimer.ICEBARRAGE);
        }
        if (!this.config.showAntiPoison()) {
            this.removeGameTimer(GameTimer.ANTIPOISON);
            this.removeGameTimer(GameTimer.ANTIVENOM);
        }
        if (!this.config.showTzhaarTimers()) {
            this.removeTzhaarTimer();
        } else {
            this.createTzhaarTimer();
        }
        if (!this.config.showLiquidAdrenaline()) {
            this.removeGameTimer(GameTimer.LIQUID_ADRENALINE);
        }
        if (!this.config.showSilkDressing()) {
            this.removeGameTimer(GameTimer.SILK_DRESSING);
        }
        if (!this.config.showBlessedCrystalScarab()) {
            this.removeGameTimer(GameTimer.BLESSED_CRYSTAL_SCARAB);
        }
        if (!this.config.showPlayerBoosts()) {
            this.removeVarpTimers();
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (event.isItemOp() && event.getMenuOption().equals("Drink")) {
            if ((event.getItemId() == 11507 || event.getItemId() == 11505) && this.config.showAntiFire()) {
                this.createGameTimer(GameTimer.ANTIFIRE);
                return;
            }
            if ((event.getItemId() == 11962 || event.getItemId() == 11960) && this.config.showAntiFire()) {
                this.createGameTimer(GameTimer.EXANTIFIRE);
                return;
            }
            if ((event.getItemId() == 21997 || event.getItemId() == 21994) && this.config.showAntiFire()) {
                this.createGameTimer(GameTimer.SUPERANTIFIRE);
                return;
            }
            if ((event.getItemId() == 22224 || event.getItemId() == 22221) && this.config.showAntiFire()) {
                this.createGameTimer(GameTimer.EXSUPERANTIFIRE);
                return;
            }
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        Matcher mDivine;
        String message = event.getMessage();
        if (event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }
        if (message.contains(DODGY_NECKLACE_PROTECTION_MESSAGE) || message.contains(SHADOW_VEIL_PROTECTION_MESSAGE)) {
            this.removeGameTimer(GameTimer.PICKPOCKET_STUN);
        }
        if (message.contains(PICKPOCKET_FAILURE_MESSAGE) && this.config.showPickpocketStun() && message.contains("pocket")) {
            if (message.contains("hero") || message.contains("elf")) {
                this.createGameTimer(GameTimer.PICKPOCKET_STUN, Duration.ofSeconds(6L));
            } else {
                this.createGameTimer(GameTimer.PICKPOCKET_STUN, Duration.ofSeconds(5L));
            }
        }
        if (message.equals(ABYSSAL_SIRE_STUN_MESSAGE) && this.config.showAbyssalSireStun()) {
            this.createGameTimer(GameTimer.ABYSSAL_SIRE_STUN);
        }
        if (this.config.showAntiFire() && message.equals(ANTIFIRE_DRINK_MESSAGE)) {
            this.createGameTimer(GameTimer.ANTIFIRE);
        }
        if (this.config.showAntiFire() && message.equals(EXTENDED_ANTIFIRE_DRINK_MESSAGE)) {
            this.createGameTimer(GameTimer.EXANTIFIRE);
        }
        if (this.config.showGodWarsAltar() && message.equalsIgnoreCase(GOD_WARS_ALTAR_MESSAGE)) {
            this.createGameTimer(GameTimer.GOD_WARS_ALTAR);
        }
        if (this.config.showAntiFire() && message.equals(EXTENDED_SUPER_ANTIFIRE_DRINK_MESSAGE)) {
            this.createGameTimer(GameTimer.EXSUPERANTIFIRE);
        }
        if (this.config.showAntiFire() && message.equals(ANTIFIRE_EXPIRED_MESSAGE)) {
            this.removeGameTimer(GameTimer.ANTIFIRE);
            this.removeGameTimer(GameTimer.EXANTIFIRE);
        }
        if (this.config.showOverload() && message.startsWith("You drink some of your") && message.contains("overload")) {
            if (this.client.getVarbitValue(5432) == 1) {
                this.createGameTimer(GameTimer.OVERLOAD_RAID);
            } else {
                this.createGameTimer(GameTimer.OVERLOAD);
            }
        }
        if (this.config.showCannon()) {
            TimerTimer cannonTimer;
            if (message.equals(CANNON_BASE_MESSAGE) || message.equals(CANNON_STAND_MESSAGE) || message.equals(CANNON_BARRELS_MESSAGE) || message.equals(CANNON_FURNACE_MESSAGE) || message.contains(CANNON_REPAIR_MESSAGE)) {
                this.removeGameTimer(GameTimer.CANNON_REPAIR);
                cannonTimer = this.createGameTimer(GameTimer.CANNON);
                cannonTimer.setTooltip(cannonTimer.getTooltip() + " - World " + this.client.getWorld());
            } else if (message.equals(CANNON_BROKEN_MESSAGE)) {
                this.removeGameTimer(GameTimer.CANNON);
                cannonTimer = this.createGameTimer(GameTimer.CANNON_REPAIR);
                cannonTimer.setTooltip(cannonTimer.getTooltip() + " - World " + this.client.getWorld());
            } else if (message.equals(CANNON_PICKUP_MESSAGE) || message.equals(CANNON_DESTROYED_MESSAGE)) {
                this.removeGameTimer(GameTimer.CANNON);
                this.removeGameTimer(GameTimer.CANNON_REPAIR);
            }
        }
        if (this.config.showMagicImbue() && message.equals(MAGIC_IMBUE_MESSAGE)) {
            this.createGameTimer(GameTimer.MAGICIMBUE);
        }
        if (message.equals(MAGIC_IMBUE_EXPIRED_MESSAGE)) {
            this.removeGameTimer(GameTimer.MAGICIMBUE);
        }
        if (this.config.showAntiFire() && message.contains(SUPER_ANTIFIRE_DRINK_MESSAGE)) {
            this.createGameTimer(GameTimer.SUPERANTIFIRE);
        }
        if (this.config.showAntiFire() && message.equals(SUPER_ANTIFIRE_EXPIRED_MESSAGE)) {
            this.removeGameTimer(GameTimer.SUPERANTIFIRE);
        }
        if (this.config.showPrayerEnhance() && message.startsWith("You drink some of your") && message.contains("prayer enhance")) {
            this.createGameTimer(GameTimer.PRAYER_ENHANCE);
        }
        if (this.config.showPrayerEnhance() && message.equals(PRAYER_ENHANCE_EXPIRED)) {
            this.removeGameTimer(GameTimer.PRAYER_ENHANCE);
        }
        if (this.config.showStaffOfTheDead() && message.contains(STAFF_OF_THE_DEAD_SPEC_MESSAGE)) {
            this.createGameTimer(GameTimer.STAFF_OF_THE_DEAD);
        }
        if (this.config.showStaffOfTheDead() && message.contains(STAFF_OF_THE_DEAD_SPEC_EXPIRED_MESSAGE)) {
            this.removeGameTimer(GameTimer.STAFF_OF_THE_DEAD);
        }
        if (this.config.showFreezes() && message.equals(FROZEN_MESSAGE)) {
            this.freezeTimer = this.createGameTimer(GameTimer.ICEBARRAGE);
            this.freezeTime = this.client.getTickCount();
        }
        if (this.config.showDivine() && (mDivine = DIVINE_POTION_PATTERN.matcher(message)).find()) {
            switch (mDivine.group(1)) {
                case "super attack": {
                    this.createGameTimer(GameTimer.DIVINE_SUPER_ATTACK);
                    break;
                }
                case "super strength": {
                    this.createGameTimer(GameTimer.DIVINE_SUPER_STRENGTH);
                    break;
                }
                case "super defence": {
                    this.createGameTimer(GameTimer.DIVINE_SUPER_DEFENCE);
                    break;
                }
                case "combat": {
                    this.createGameTimer(GameTimer.DIVINE_SUPER_COMBAT);
                    break;
                }
                case "ranging": {
                    this.createGameTimer(GameTimer.DIVINE_RANGING);
                    break;
                }
                case "magic": {
                    this.createGameTimer(GameTimer.DIVINE_MAGIC);
                    break;
                }
                case "bastion": {
                    this.createGameTimer(GameTimer.DIVINE_BASTION);
                    break;
                }
                case "battlemage": {
                    this.createGameTimer(GameTimer.DIVINE_BATTLEMAGE);
                }
            }
        }
        if (this.config.showArceuus()) {
            Duration duration = Duration.of(this.client.getRealSkillLevel(Skill.MAGIC), RSTimeUnit.GAME_TICKS);
            if (message.endsWith(SHADOW_VEIL_MESSAGE)) {
                this.createGameTimer(GameTimer.SHADOW_VEIL, duration);
            } else if (message.endsWith(WARD_OF_ARCEUUS_MESSAGE)) {
                this.createGameTimer(GameTimer.WARD_OF_ARCEUUS, duration);
            } else if (message.endsWith(DEATH_CHARGE_MESSAGE)) {
                this.createGameTimer(GameTimer.DEATH_CHARGE, duration);
            } else if (message.endsWith(DEATH_CHARGE_ACTIVATE_MESSAGE)) {
                this.removeGameTimer(GameTimer.DEATH_CHARGE);
            } else if (message.contains(RESURRECT_THRALL_MESSAGE_START) && message.endsWith(RESURRECT_THRALL_MESSAGE_END)) {
                this.createGameTimer(GameTimer.RESURRECT_THRALL, Duration.of(this.client.getBoostedSkillLevel(Skill.MAGIC), RSTimeUnit.GAME_TICKS));
            } else if (message.contains(RESURRECT_THRALL_DISAPPEAR_MESSAGE_START) && message.endsWith(RESURRECT_THRALL_DISAPPEAR_MESSAGE_END)) {
                this.removeGameTimer(GameTimer.RESURRECT_THRALL);
            }
        }
        if (this.config.showArceuusCooldown()) {
            if (message.endsWith(SHADOW_VEIL_MESSAGE)) {
                this.createGameTimer(GameTimer.SHADOW_VEIL_COOLDOWN);
            } else if (message.endsWith(DEATH_CHARGE_MESSAGE)) {
                this.createGameTimer(GameTimer.DEATH_CHARGE_COOLDOWN);
            } else if (message.endsWith(WARD_OF_ARCEUUS_MESSAGE)) {
                this.createGameTimer(GameTimer.WARD_OF_ARCEUUS_COOLDOWN);
            } else if (message.contains(RESURRECT_THRALL_MESSAGE_START) && message.endsWith(RESURRECT_THRALL_MESSAGE_END)) {
                this.createGameTimer(GameTimer.RESURRECT_THRALL_COOLDOWN);
            }
        }
        if (message.equals(TZHAAR_DEFEATED_MESSAGE)) {
            log.debug("Stopping tzhaar timer");
            this.removeTzhaarTimer();
            this.config.tzhaarStartTime(null);
            this.config.tzhaarLastTime(null);
            return;
        }
        if (TZHAAR_PAUSED_MESSAGE.matcher(message).find()) {
            log.debug("Pausing tzhaar timer");
            this.config.tzhaarLastTime(Instant.now());
            if (this.config.showTzhaarTimers()) {
                this.createTzhaarTimer();
            }
            return;
        }
        Matcher matcher = TZHAAR_WAVE_MESSAGE.matcher(message);
        if (matcher.find()) {
            int wave = Integer.parseInt(matcher.group(1));
            if (wave == 1) {
                log.debug("Starting tzhaar timer");
                Instant now = Instant.now();
                if (this.isInInferno()) {
                    this.config.tzhaarStartTime(now.minus(Duration.ofSeconds(6L)));
                } else {
                    this.config.tzhaarStartTime(now);
                }
                this.config.tzhaarLastTime(null);
                if (this.config.showTzhaarTimers()) {
                    this.createTzhaarTimer();
                }
            } else if (this.config.tzhaarStartTime() != null && this.config.tzhaarLastTime() != null) {
                log.debug("Unpausing tzhaar timer");
                Instant tzhaarStartTime = this.config.tzhaarStartTime();
                tzhaarStartTime = tzhaarStartTime.plus(Duration.between(this.config.tzhaarLastTime(), Instant.now()));
                this.config.tzhaarStartTime(tzhaarStartTime);
                this.config.tzhaarLastTime(null);
                if (this.config.showTzhaarTimers()) {
                    this.createTzhaarTimer();
                }
            }
        }
        if (message.equals(SILK_DRESSING_MESSAGE) && this.config.showSilkDressing()) {
            this.createGameTimer(GameTimer.SILK_DRESSING);
        }
        if (message.equals(BLESSED_CRYSTAL_SCARAB_MESSAGE) && this.config.showBlessedCrystalScarab()) {
            this.createGameTimer(GameTimer.BLESSED_CRYSTAL_SCARAB);
        }
        if (message.equals(LIQUID_ADRENALINE_MESSAGE) && this.config.showLiquidAdrenaline()) {
            this.createGameTimer(GameTimer.LIQUID_ADRENALINE);
        }
    }

    private boolean isInFightCaves() {
        return this.client.getMapRegions() != null && ArrayUtils.contains((int[])this.client.getMapRegions(), (int)9551);
    }

    private boolean isInInferno() {
        return this.client.getMapRegions() != null && ArrayUtils.contains((int[])this.client.getMapRegions(), (int)9043);
    }

    private boolean isInNightmareZone() {
        return this.client.getLocalPlayer() != null && this.client.getLocalPlayer().getWorldLocation().getPlane() > 0 && ArrayUtils.contains((int[])this.client.getMapRegions(), (int)9033);
    }

    private void createTzhaarTimer() {
        int imageItem;
        this.removeTzhaarTimer();
        int n = this.isInFightCaves() ? 6570 : (imageItem = this.isInInferno() ? 21295 : -1);
        if (imageItem == -1) {
            return;
        }
        this.tzhaarTimer = new ElapsedTimer(this.itemManager.getImage(imageItem), this, this.config.tzhaarStartTime(), this.config.tzhaarLastTime());
        this.infoBoxManager.addInfoBox(this.tzhaarTimer);
    }

    private void removeTzhaarTimer() {
        if (this.tzhaarTimer != null) {
            this.infoBoxManager.removeInfoBox(this.tzhaarTimer);
            this.tzhaarTimer = null;
        }
    }

    private void checkTeleport(VarPlayer varPlayer) {
        GameTimer teleport;
        switch (varPlayer) {
            case LAST_HOME_TELEPORT: {
                teleport = GameTimer.HOME_TELEPORT;
                break;
            }
            case LAST_MINIGAME_TELEPORT: {
                teleport = GameTimer.MINIGAME_TELEPORT;
                break;
            }
            default: {
                return;
            }
        }
        int lastTeleport = this.client.getVarpValue(varPlayer);
        long lastTeleportSeconds = (long)lastTeleport * 60L;
        Instant teleportExpireInstant = Instant.ofEpochSecond(lastTeleportSeconds).plus(teleport.getDuration());
        Duration remainingTime = Duration.between(Instant.now(), teleportExpireInstant);
        if (remainingTime.getSeconds() > 0L) {
            this.createGameTimer(teleport, remainingTime);
        } else {
            this.removeGameTimer(teleport);
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        Player player = this.client.getLocalPlayer();
        WorldPoint currentWorldPoint = player.getWorldLocation();
        if (this.freezeTimer != null && this.freezeTime != this.client.getTickCount() && !currentWorldPoint.equals((Object)this.lastPoint)) {
            this.removeGameTimer(this.freezeTimer.getTimer());
            this.freezeTimer = null;
        }
        this.lastPoint = currentWorldPoint;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        switch (gameStateChanged.getGameState()) {
            case LOADING: {
                if (!this.isInNightmareZone()) {
                    this.removeGameTimer(GameTimer.OVERLOAD);
                }
                if (this.tzhaarTimer == null || this.isInFightCaves() || this.isInInferno()) break;
                this.removeTzhaarTimer();
                this.config.tzhaarStartTime(null);
                this.config.tzhaarLastTime(null);
                break;
            }
            case LOGIN_SCREEN: 
            case HOPPING: {
                if (this.config.tzhaarStartTime() != null && this.config.tzhaarLastTime() == null) {
                    this.config.tzhaarLastTime(Instant.now());
                    log.debug("Pausing tzhaar timer");
                }
                this.removeTzhaarTimer();
                this.removeGameTimer(GameTimer.TELEBLOCK);
                this.removeVarpTimers();
            }
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        Actor actor = event.getActor();
        if (actor != this.client.getLocalPlayer()) {
            return;
        }
        if (this.config.showDFSSpecial() && this.lastAnimation == 6696) {
            this.createGameTimer(GameTimer.DRAGON_FIRE_SHIELD);
        }
        this.lastAnimation = this.client.getLocalPlayer().getAnimation();
    }

    @Subscribe
    public void onGraphicChanged(GraphicChanged event) {
        Actor actor = event.getActor();
        if (actor != this.client.getLocalPlayer()) {
            return;
        }
        if (this.config.showFreezes()) {
            if (actor.getGraphic() == GameTimer.BIND.getGraphicId().intValue()) {
                this.createGameTimer(GameTimer.BIND);
            }
            if (actor.getGraphic() == GameTimer.SNARE.getGraphicId().intValue()) {
                this.createGameTimer(GameTimer.SNARE);
            }
            if (actor.getGraphic() == GameTimer.ENTANGLE.getGraphicId().intValue()) {
                this.createGameTimer(GameTimer.ENTANGLE);
            }
            if (this.freezeTime == this.client.getTickCount()) {
                if (actor.getGraphic() == GameTimer.ICERUSH.getGraphicId().intValue()) {
                    this.removeGameTimer(GameTimer.ICEBARRAGE);
                    this.freezeTimer = this.createGameTimer(GameTimer.ICERUSH);
                }
                if (actor.getGraphic() == GameTimer.ICEBURST.getGraphicId().intValue()) {
                    this.removeGameTimer(GameTimer.ICEBARRAGE);
                    this.freezeTimer = this.createGameTimer(GameTimer.ICEBURST);
                }
                if (actor.getGraphic() == GameTimer.ICEBLITZ.getGraphicId().intValue()) {
                    this.removeGameTimer(GameTimer.ICEBARRAGE);
                    this.freezeTimer = this.createGameTimer(GameTimer.ICEBLITZ);
                }
                if (actor.getGraphic() == GameTimer.BINDING_TENTACLE.getGraphicId().intValue()) {
                    this.removeGameTimer(GameTimer.ICEBARRAGE);
                    this.freezeTimer = this.createGameTimer(GameTimer.BINDING_TENTACLE);
                }
            }
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged itemContainerChanged) {
        if (itemContainerChanged.getContainerId() != InventoryID.EQUIPMENT.getId()) {
            return;
        }
        ItemContainer container = itemContainerChanged.getItemContainer();
        Item weapon = container.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
        if (weapon == null || weapon.getId() != 11791 && weapon.getId() != 12904 && weapon.getId() != 22296 && weapon.getId() != 12902) {
            this.removeGameTimer(GameTimer.STAFF_OF_THE_DEAD);
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        if (!npc.isDead()) {
            return;
        }
        int npcId = npc.getId();
        if (npcId == 8062 || npcId == 8063) {
            this.removeGameTimer(GameTimer.ICEBARRAGE);
        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        if (actorDeath.getActor() == this.client.getLocalPlayer()) {
            this.infoBoxManager.removeIf(t -> t instanceof TimerTimer && ((TimerTimer)t).getTimer().isRemovedOnDeath());
        }
    }

    private TimerTimer createGameTimer(GameTimer timer) {
        if (timer.getDuration() == null) {
            throw new IllegalArgumentException("Timer with no duration");
        }
        return this.createGameTimer(timer, timer.getDuration());
    }

    private TimerTimer createGameTimer(GameTimer timer, Duration duration) {
        this.removeGameTimer(timer);
        TimerTimer t = new TimerTimer(timer, duration, this);
        switch (timer.getImageType()) {
            case SPRITE: {
                this.spriteManager.getSpriteAsync(timer.getImageId(), 0, t);
                break;
            }
            case ITEM: {
                t.setImage(this.itemManager.getImage(timer.getImageId()));
            }
        }
        t.setTooltip(timer.getDescription());
        this.infoBoxManager.addInfoBox(t);
        return t;
    }

    private void removeGameTimer(GameTimer timer) {
        this.infoBoxManager.removeIf(t -> t instanceof TimerTimer && ((TimerTimer)t).getTimer() == timer);
    }

    private IndicatorIndicator createGameIndicator(GameIndicator gameIndicator) {
        this.removeGameIndicator(gameIndicator);
        IndicatorIndicator indicator = new IndicatorIndicator(gameIndicator, (Plugin)this);
        switch (gameIndicator.getImageType()) {
            case SPRITE: {
                this.spriteManager.getSpriteAsync(gameIndicator.getImageId(), 0, indicator);
                break;
            }
            case ITEM: {
                indicator.setImage(this.itemManager.getImage(gameIndicator.getImageId()));
            }
        }
        indicator.setTooltip(gameIndicator.getDescription());
        this.infoBoxManager.addInfoBox(indicator);
        return indicator;
    }

    private void removeGameIndicator(GameIndicator indicator) {
        this.infoBoxManager.removeIf(t -> t instanceof IndicatorIndicator && ((IndicatorIndicator)t).getIndicator() == indicator);
    }

    private void removeVarpTimers() {
        this.infoBoxManager.removeIf(t -> t instanceof TimerTimer && this.varpTimers.containsValue(t));
        this.varpTimers.clear();
        this.varpTimersLastTimes.clear();
    }
}

