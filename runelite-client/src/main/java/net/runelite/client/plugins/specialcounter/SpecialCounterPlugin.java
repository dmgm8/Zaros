/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  javax.inject.Named
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.EquipmentInventorySlot
 *  net.runelite.api.GameState
 *  net.runelite.api.Hitsplat
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.coords.WorldArea
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.CommandExecuted
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.HitsplatApplied
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.ScriptPostFired
 *  net.runelite.api.events.VarbitChanged
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.specialcounter;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.Hitsplat;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.VarPlayer;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.party.PartyService;
import net.runelite.client.party.WSClient;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.specialcounter.PlayerInfoDrop;
import net.runelite.client.plugins.specialcounter.PlayerInfoDropOverlay;
import net.runelite.client.plugins.specialcounter.SpecialCounter;
import net.runelite.client.plugins.specialcounter.SpecialCounterConfig;
import net.runelite.client.plugins.specialcounter.SpecialCounterUpdate;
import net.runelite.client.plugins.specialcounter.SpecialWeapon;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Special Attack Counter", description="Track special attacks used on NPCs", tags={"combat", "npcs", "overlay"}, enabledByDefault=false)
public class SpecialCounterPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SpecialCounterPlugin.class);
    private static final Set<Integer> IGNORED_NPCS = ImmutableSet.of((Object)320, (Object)8062, (Object)8063, (Object)2668, (Object)7413, (Object)6613, (Object[])new Integer[]{6614, 5916, 5918});
    private int currentWorld;
    private int specialPercentage;
    private SpecialWeapon specialWeapon;
    private int hitsplatTick;
    private Hitsplat lastSpecHitsplat;
    private NPC lastSpecTarget;
    private final Set<Integer> interactedNpcIndexes = new HashSet<Integer>();
    private final SpecialCounter[] specialCounter = new SpecialCounter[SpecialWeapon.values().length];
    private final List<PlayerInfoDrop> playerInfoDrops = new ArrayList<PlayerInfoDrop>();
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private WSClient wsClient;
    @Inject
    private PartyService party;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private Notifier notifier;
    @Inject
    private SpecialCounterConfig config;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private PlayerInfoDropOverlay playerInfoDropOverlay;
    @Inject
    @Named(value="developerMode")
    boolean developerMode;

    @Provides
    SpecialCounterConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(SpecialCounterConfig.class);
    }

    @Override
    protected void startUp() {
        this.overlayManager.add(this.playerInfoDropOverlay);
        this.wsClient.registerMessage(SpecialCounterUpdate.class);
        this.currentWorld = -1;
        this.specialPercentage = -1;
        this.interactedNpcIndexes.clear();
    }

    @Override
    protected void shutDown() {
        this.specialWeapon = null;
        this.lastSpecTarget = null;
        this.lastSpecHitsplat = null;
        this.removeCounters();
        this.overlayManager.remove(this.playerInfoDropOverlay);
        this.wsClient.unregisterMessage(SpecialCounterUpdate.class);
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired event) {
        if (event.getScriptId() == 2308) {
            log.debug("Resetting spec counter as sotetseg maze script was ran");
            this.removeCounters();
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.lastSpecHitsplat != null && this.specialWeapon != null && this.lastSpecTarget != null) {
            if (this.lastSpecHitsplat.getAmount() > 0) {
                this.specialAttackHit(this.specialWeapon, this.lastSpecHitsplat, this.lastSpecTarget);
            }
            this.specialWeapon = null;
            this.lastSpecHitsplat = null;
            this.lastSpecTarget = null;
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            if (this.currentWorld == -1) {
                this.currentWorld = this.client.getWorld();
            } else if (this.currentWorld != this.client.getWorld()) {
                this.currentWorld = this.client.getWorld();
                this.removeCounters();
            }
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        if (event.getVarpId() != VarPlayer.SPECIAL_ATTACK_PERCENT.getId()) {
            return;
        }
        int specialPercentage = event.getValue();
        if (this.specialPercentage == -1 || specialPercentage >= this.specialPercentage) {
            this.specialPercentage = specialPercentage;
            return;
        }
        this.specialPercentage = specialPercentage;
        this.specialWeapon = this.usedSpecialWeapon();
        if (this.specialWeapon == null) {
            return;
        }
        log.debug("Special attack used - percent: {} weapon: {}", (Object)specialPercentage, (Object)this.specialWeapon);
        this.clientThread.invokeLater(() -> {
            Actor target = this.client.getLocalPlayer().getInteracting();
            this.lastSpecTarget = target instanceof NPC ? (NPC)target : null;
            this.hitsplatTick = this.client.getTickCount() + this.getHitDelay(this.specialWeapon, target);
        });
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        Actor target = hitsplatApplied.getActor();
        Hitsplat hitsplat = hitsplatApplied.getHitsplat();
        if (!hitsplat.isMine() || target == this.client.getLocalPlayer()) {
            return;
        }
        if (this.lastSpecTarget == null || target != this.lastSpecTarget) {
            return;
        }
        NPC npc = (NPC)target;
        int interactingId = npc.getId();
        int npcIndex = npc.getIndex();
        if (IGNORED_NPCS.contains(interactingId)) {
            return;
        }
        if (!this.interactedNpcIndexes.contains(npcIndex)) {
            this.removeCounters();
            this.interactedNpcIndexes.add(npcIndex);
        }
        if (this.hitsplatTick == this.client.getTickCount()) {
            this.lastSpecHitsplat = hitsplat;
        }
    }

    private void specialAttackHit(SpecialWeapon specialWeapon, Hitsplat hitsplat, NPC target) {
        int hit = this.getHit(specialWeapon, hitsplat);
        int localPlayerId = this.client.getLocalPlayer().getId();
        log.debug("Special attack hit {} hitsplat {}", (Object)specialWeapon, (Object)hitsplat.getAmount());
        if (this.config.infobox()) {
            this.updateCounter(specialWeapon, null, hit);
        }
        if (this.party.isInParty()) {
            int npcIndex = target.getIndex();
            SpecialCounterUpdate specialCounterUpdate = new SpecialCounterUpdate(npcIndex, specialWeapon, hit, this.client.getWorld(), localPlayerId);
            this.party.send(specialCounterUpdate);
        }
        this.playerInfoDrops.add(this.createSpecInfoDrop(specialWeapon, hit, localPlayerId));
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC actor = npcDespawned.getNpc();
        if (this.lastSpecTarget == actor) {
            this.lastSpecTarget = null;
        }
        if (actor.isDead() && this.interactedNpcIndexes.contains(actor.getIndex())) {
            this.removeCounters();
        }
    }

    @Subscribe
    public void onSpecialCounterUpdate(SpecialCounterUpdate event) {
        if (this.party.getLocalMember().getMemberId() == event.getMemberId() || event.getWorld() != this.client.getWorld()) {
            return;
        }
        String name = this.party.getMemberById(event.getMemberId()).getDisplayName();
        if (name == null) {
            return;
        }
        this.clientThread.invoke(() -> {
            if (this.interactedNpcIndexes.isEmpty()) {
                this.interactedNpcIndexes.add(event.getNpcIndex());
            }
            if (this.interactedNpcIndexes.contains(event.getNpcIndex()) && this.config.infobox()) {
                this.updateCounter(event.getWeapon(), name, event.getHit());
            }
            this.playerInfoDrops.add(this.createSpecInfoDrop(event.getWeapon(), event.getHit(), event.getPlayerId()));
        });
    }

    @Subscribe
    public void onCommandExecuted(CommandExecuted commandExecuted) {
        if (this.developerMode && commandExecuted.getCommand().equals("spec")) {
            this.playerInfoDrops.add(this.createSpecInfoDrop(SpecialWeapon.BANDOS_GODSWORD, 42, this.client.getLocalPlayer().getId()));
        }
    }

    private SpecialWeapon usedSpecialWeapon() {
        ItemContainer equipment = this.client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipment == null) {
            return null;
        }
        Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
        if (weapon == null) {
            return null;
        }
        for (SpecialWeapon specialWeapon : SpecialWeapon.values()) {
            if (!Arrays.stream(specialWeapon.getItemID()).anyMatch(id -> id == weapon.getId())) continue;
            return specialWeapon;
        }
        return null;
    }

    private void updateCounter(SpecialWeapon specialWeapon, String name, int hit) {
        SpecialCounter counter = this.specialCounter[specialWeapon.ordinal()];
        if (counter == null) {
            counter = new SpecialCounter(this.itemManager.getImage(specialWeapon.getItemID()[0]), this, this.config, hit, specialWeapon);
            this.infoBoxManager.addInfoBox(counter);
            this.specialCounter[specialWeapon.ordinal()] = counter;
        } else {
            counter.addHits(hit);
        }
        this.sendNotification(specialWeapon, counter);
        Map<String, Integer> partySpecs = counter.getPartySpecs();
        if (this.party.isInParty()) {
            if (partySpecs.containsKey(name)) {
                partySpecs.put(name, hit + partySpecs.get(name));
            } else {
                partySpecs.put(name, hit);
            }
        }
    }

    private void sendNotification(SpecialWeapon weapon, SpecialCounter counter) {
        int threshold = weapon.getThreshold().apply(this.config);
        if (threshold > 0 && counter.getCount() >= threshold && this.config.thresholdNotification()) {
            this.notifier.notify(weapon.getName() + " special attack threshold reached!");
        }
    }

    private void removeCounters() {
        this.interactedNpcIndexes.clear();
        for (int i = 0; i < this.specialCounter.length; ++i) {
            SpecialCounter counter = this.specialCounter[i];
            if (counter == null) continue;
            this.infoBoxManager.removeInfoBox(counter);
            this.specialCounter[i] = null;
        }
    }

    private int getHit(SpecialWeapon specialWeapon, Hitsplat hitsplat) {
        return specialWeapon.isDamage() ? hitsplat.getAmount() : 1;
    }

    private PlayerInfoDrop createSpecInfoDrop(SpecialWeapon weapon, int hit, int playerId) {
        int cycle = this.client.getGameCycle();
        BufferedImage image = ImageUtil.resizeImage(this.itemManager.getImage(weapon.getItemID()[0]), 24, 24);
        return PlayerInfoDrop.builder(cycle, cycle + 100, playerId, Integer.toString(hit)).color(this.config.specDropColor()).startHeightOffset(100).endHeightOffset(400).image(image).build();
    }

    private int getHitDelay(SpecialWeapon specialWeapon, Actor target) {
        if (specialWeapon != SpecialWeapon.DORGESHUUN_CROSSBOW || target == null) {
            return 0;
        }
        Player player = this.client.getLocalPlayer();
        if (player == null) {
            return 0;
        }
        WorldPoint playerWp = player.getWorldLocation();
        if (playerWp == null) {
            return 0;
        }
        WorldArea targetArea = target.getWorldArea();
        if (targetArea == null) {
            return 0;
        }
        int distance = targetArea.distanceTo(playerWp);
        int cycles = 19 + distance * 3;
        return (cycles + 29) / 30;
    }

    List<PlayerInfoDrop> getPlayerInfoDrops() {
        return this.playerInfoDrops;
    }
}

