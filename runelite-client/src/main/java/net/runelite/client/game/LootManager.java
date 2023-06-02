/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ListMultimap
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.Player
 *  net.runelite.api.Tile
 *  net.runelite.api.TileItem
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.ActorDeath
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.InteractingChanged
 *  net.runelite.api.events.ItemDespawned
 *  net.runelite.api.events.ItemQuantityChanged
 *  net.runelite.api.events.ItemSpawned
 *  net.runelite.api.events.NpcChanged
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.PlayerDespawned
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.game;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemQuantityChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemStack;
import net.runelite.client.game.NpcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class LootManager {
    private static final Logger log = LoggerFactory.getLogger(LootManager.class);
    private static final Map<Integer, Integer> NPC_DEATH_ANIMATIONS = ImmutableMap.of((Object)492, (Object)3993);
    private static final Map<Integer, Integer> NPC_DEATH_TRANSMOGRIFICATIONS = ImmutableMap.of((Object)8061, (Object)8059);
    private static final int EDGEVILLE_PVP_REGION_ID = 6025;
    private final EventBus eventBus;
    private final Client client;
    private final NpcUtil npcUtil;
    private final ListMultimap<Integer, ItemStack> itemSpawns = ArrayListMultimap.create();
    private final Set<LocalPoint> killPoints = new HashSet<LocalPoint>();
    private WorldPoint playerLocationLastTick;
    private WorldPoint krakenPlayerLocation;
    private NPC delayedLootNpc;
    private int delayedLootTickLimit;
    private Player lastPlayerTarget;
    private Player delayedLootPlayer;
    private WorldPoint delayedLootPlayerLocation;
    private int delayedLootPlayerTickLimit;

    @Inject
    private LootManager(EventBus eventBus, Client client, NpcUtil npcUtil) {
        this.eventBus = eventBus;
        this.client = client;
        this.npcUtil = npcUtil;
        eventBus.register(this);
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        if (npc == this.delayedLootNpc) {
            this.delayedLootNpc = null;
            this.delayedLootTickLimit = 0;
        }
        if (!this.npcUtil.isDying(npc)) {
            int id = npc.getId();
            switch (id) {
                case 412: 
                case 413: 
                case 421: 
                case 422: 
                case 458: 
                case 459: 
                case 460: 
                case 461: 
                case 462: 
                case 463: 
                case 537: 
                case 1024: 
                case 1543: 
                case 7392: 
                case 7407: 
                case 7408: 
                case 7797: 
                case 7888: 
                case 7889: {
                    break;
                }
                default: {
                    return;
                }
            }
        }
        this.processNpcLoot(npc);
    }

    @Subscribe
    public void onPlayerDespawned(PlayerDespawned playerDespawned) {
        int y;
        Player player = playerDespawned.getPlayer();
        if (player.getHealthRatio() != 0) {
            return;
        }
        LocalPoint location = LocalPoint.fromWorld((Client)this.client, (WorldPoint)player.getWorldLocation());
        if (location == null || this.killPoints.contains((Object)location)) {
            return;
        }
        int x = location.getSceneX();
        int packed = x << 8 | (y = location.getSceneY());
        List items = this.itemSpawns.get((Object)packed);
        if (items.isEmpty()) {
            return;
        }
        this.killPoints.add(location);
        this.eventBus.post(new PlayerLootReceived(player, items));
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged interactingChanged) {
        if (interactingChanged.getSource() != this.client.getLocalPlayer()) {
            return;
        }
        Actor target = interactingChanged.getTarget();
        if (target instanceof Player) {
            this.lastPlayerTarget = (Player)target;
        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        Actor actor = actorDeath.getActor();
        if (!(actor instanceof Player)) {
            return;
        }
        Player player = (Player)actor;
        if (player == this.lastPlayerTarget) {
            this.delayedLootPlayer = player;
            this.delayedLootPlayerLocation = player.getWorldLocation();
            this.delayedLootPlayerTickLimit = 6;
        }
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned itemSpawned) {
        TileItem item = itemSpawned.getItem();
        Tile tile = itemSpawned.getTile();
        LocalPoint location = tile.getLocalLocation();
        int packed = location.getSceneX() << 8 | location.getSceneY();
        this.itemSpawns.put((Object)packed, (Object)new ItemStack(item.getId(), item.getQuantity(), location));
        log.debug("Item spawn {} ({}) location {}", new Object[]{item.getId(), item.getQuantity(), location});
    }

    @Subscribe
    public void onItemDespawned(ItemDespawned itemDespawned) {
        TileItem item = itemDespawned.getItem();
        LocalPoint location = itemDespawned.getTile().getLocalLocation();
        log.debug("Item despawn {} ({}) location {}", new Object[]{item.getId(), item.getQuantity(), location});
    }

    @Subscribe
    public void onItemQuantityChanged(ItemQuantityChanged itemQuantityChanged) {
        TileItem item = itemQuantityChanged.getItem();
        Tile tile = itemQuantityChanged.getTile();
        LocalPoint location = tile.getLocalLocation();
        int packed = location.getSceneX() << 8 | location.getSceneY();
        int diff = itemQuantityChanged.getNewQuantity() - itemQuantityChanged.getOldQuantity();
        if (diff <= 0) {
            return;
        }
        this.itemSpawns.put((Object)packed, (Object)new ItemStack(item.getId(), diff, location));
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged e) {
        if (!(e.getActor() instanceof NPC)) {
            return;
        }
        NPC npc = (NPC)e.getActor();
        int id = npc.getId();
        Integer deathAnim = NPC_DEATH_ANIMATIONS.get(id);
        if (deathAnim != null && deathAnim.intValue() == npc.getAnimation()) {
            if (id == 492) {
                this.krakenPlayerLocation = this.client.getLocalPlayer().getWorldLocation();
            } else {
                this.processNpcLoot(npc);
            }
        }
    }

    @Subscribe
    public void onNpcChanged(NpcChanged npcChanged) {
        NPC npc = npcChanged.getNpc();
        Integer transmog = NPC_DEATH_TRANSMOGRIFICATIONS.get(npcChanged.getOld().getId());
        if (transmog != null && transmog.intValue() == npc.getId()) {
            this.processNpcLoot(npcChanged.getNpc());
            return;
        }
        if (npc.getId() == 9433 || npc.getId() == 9424) {
            this.delayedLootNpc = npc;
            this.delayedLootTickLimit = 15;
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        if (this.delayedLootNpc != null && this.delayedLootTickLimit-- > 0) {
            this.processDelayedLoot();
        }
        if (this.delayedLootPlayer != null && this.delayedLootPlayerTickLimit-- > 0) {
            this.processDelayedPlayerLoot();
        }
        this.playerLocationLastTick = this.client.getLocalPlayer().getWorldLocation();
        this.itemSpawns.clear();
        this.killPoints.clear();
    }

    private void processDelayedPlayerLoot() {
        int sceneY;
        if (this.client.getLocalPlayer().getWorldLocation().getRegionID() != 6025) {
            this.delayedLootPlayer = null;
            this.delayedLootPlayerLocation = null;
            this.delayedLootPlayerTickLimit = 0;
            log.debug("Delayed loot information reset due to player not being in edgeville pvp anymore.");
            return;
        }
        LocalPoint localPoint = LocalPoint.fromWorld((Client)this.client, (WorldPoint)this.delayedLootPlayerLocation);
        if (localPoint == null) {
            this.delayedLootPlayer = null;
            this.delayedLootPlayerLocation = null;
            this.delayedLootPlayerTickLimit = 0;
            log.debug("Scene changed away from delayed player loot location");
            return;
        }
        int sceneX = localPoint.getSceneX();
        int packed = sceneX << 8 | (sceneY = localPoint.getSceneY());
        List itemStacks = this.itemSpawns.get((Object)packed);
        if (itemStacks.isEmpty()) {
            return;
        }
        log.debug("Got delayed player loot stack from {}: {}", (Object)this.delayedLootPlayer.getName(), (Object)itemStacks);
        this.eventBus.post(new PlayerLootReceived(this.delayedLootPlayer, itemStacks));
    }

    private void processDelayedLoot() {
        int sceneY;
        WorldPoint adjacentLootTile = this.getAdjacentSquareLootTile(this.delayedLootNpc);
        LocalPoint localPoint = LocalPoint.fromWorld((Client)this.client, (WorldPoint)adjacentLootTile);
        if (localPoint == null) {
            log.debug("Scene changed away from delayed loot location");
            this.delayedLootNpc = null;
            this.delayedLootTickLimit = 0;
            return;
        }
        int sceneX = localPoint.getSceneX();
        int packed = sceneX << 8 | (sceneY = localPoint.getSceneY());
        List itemStacks = this.itemSpawns.get((Object)packed);
        if (itemStacks.isEmpty()) {
            return;
        }
        log.debug("Got delayed loot stack from {}: {}", (Object)this.delayedLootNpc.getName(), (Object)itemStacks);
        this.eventBus.post(new NpcLootReceived(this.delayedLootNpc, itemStacks));
        this.delayedLootNpc = null;
        this.delayedLootTickLimit = 0;
    }

    private void processNpcLoot(NPC npc) {
        LocalPoint location = LocalPoint.fromWorld((Client)this.client, (WorldPoint)this.getDropLocation(npc, npc.getWorldLocation()));
        if (location == null || this.killPoints.contains((Object)location)) {
            return;
        }
        int x = location.getSceneX();
        int y = location.getSceneY();
        int size = npc.getComposition().getSize();
        ArrayList<ItemStack> allItems = new ArrayList<ItemStack>();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                int packed = x + i << 8 | y + j;
                List items = this.itemSpawns.get((Object)packed);
                allItems.addAll(items);
            }
        }
        if (allItems.isEmpty()) {
            return;
        }
        this.killPoints.add(location);
        this.eventBus.post(new NpcLootReceived(npc, allItems));
    }

    private WorldPoint getDropLocation(NPC npc, WorldPoint worldLocation) {
        block0 : switch (npc.getId()) {
            case 494: 
            case 6640: 
            case 6656: {
                worldLocation = this.playerLocationLastTick;
                break;
            }
            case 492: {
                worldLocation = this.krakenPlayerLocation;
                break;
            }
            case 2042: 
            case 2043: 
            case 2044: {
                for (Map.Entry entry : this.itemSpawns.entries()) {
                    if (((ItemStack)entry.getValue()).getId() != 12934) continue;
                    int packed = (Integer)entry.getKey();
                    int unpackedX = packed >> 8;
                    int unpackedY = packed & 0xFF;
                    worldLocation = WorldPoint.fromScene((Client)this.client, (int)unpackedX, (int)unpackedY, (int)worldLocation.getPlane());
                    break block0;
                }
                break;
            }
            case 8026: 
            case 8058: 
            case 8059: 
            case 8060: 
            case 8061: {
                int x = worldLocation.getX() + 3;
                int y = worldLocation.getY() + 3;
                if (this.playerLocationLastTick.getX() < x) {
                    x -= 4;
                } else if (this.playerLocationLastTick.getX() > x) {
                    x += 4;
                }
                if (this.playerLocationLastTick.getY() < y) {
                    y -= 4;
                } else if (this.playerLocationLastTick.getY() > y) {
                    y += 4;
                }
                worldLocation = new WorldPoint(x, y, worldLocation.getPlane());
                break;
            }
            case 11278: 
            case 11279: 
            case 11280: 
            case 11281: 
            case 11282: {
                int y;
                int x;
                int packed;
                LocalPoint localPoint = LocalPoint.fromWorld((Client)this.client, (WorldPoint)this.playerLocationLastTick);
                if (localPoint == null || !this.itemSpawns.containsKey((Object)(packed = (x = localPoint.getSceneX()) << 8 | (y = localPoint.getSceneY())))) break;
                return this.playerLocationLastTick;
            }
        }
        return worldLocation;
    }

    private WorldPoint getAdjacentSquareLootTile(NPC npc) {
        NPCComposition composition = npc.getComposition();
        WorldPoint worldLocation = npc.getWorldLocation();
        int x = worldLocation.getX();
        int y = worldLocation.getY();
        x = this.playerLocationLastTick.getX() < x ? --x : (x += Math.min(this.playerLocationLastTick.getX() - x, composition.getSize()));
        y = this.playerLocationLastTick.getY() < y ? --y : (y += Math.min(this.playerLocationLastTick.getY() - y, composition.getSize()));
        return new WorldPoint(x, y, worldLocation.getPlane());
    }

    public Collection<ItemStack> getItemSpawns(WorldPoint worldPoint) {
        LocalPoint localPoint = LocalPoint.fromWorld((Client)this.client, (WorldPoint)worldPoint);
        if (localPoint == null) {
            return Collections.emptyList();
        }
        int sceneX = localPoint.getSceneX();
        int sceneY = localPoint.getSceneY();
        int packed = sceneX << 8 | sceneY;
        List itemStacks = this.itemSpawns.get((Object)packed);
        return Collections.unmodifiableList(itemStacks);
    }
}

