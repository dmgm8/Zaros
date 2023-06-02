/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.GameState
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Player
 *  net.runelite.api.WallObject
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.GameObjectDespawned
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.WallObjectSpawned
 */
package net.runelite.client.plugins.mining;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.WallObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.mining.MiningConfig;
import net.runelite.client.plugins.mining.MiningOverlay;
import net.runelite.client.plugins.mining.MiningRocksOverlay;
import net.runelite.client.plugins.mining.MiningSession;
import net.runelite.client.plugins.mining.Pickaxe;
import net.runelite.client.plugins.mining.Rock;
import net.runelite.client.plugins.mining.RockRespawn;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;

@PluginDescriptor(name="Mining", description="Show mining statistics and ore respawn timers", tags={"overlay", "skilling", "timers"}, enabledByDefault=false)
@PluginDependency(value=XpTrackerPlugin.class)
public class MiningPlugin
extends Plugin {
    private static final Pattern MINING_PATTERN = Pattern.compile("You (?:manage to|just) (?:mined?|quarry) (?:some|an?) (?:copper|tin|clay|iron|silver|coal|gold|mithril|adamantite|runeite|amethyst|sandstone|granite|barronite shards|barronite deposit|Opal|piece of Jade|Red Topaz|Emerald|Sapphire|Ruby|Diamond)(?:\\.|!)");
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private MiningOverlay overlay;
    @Inject
    private MiningRocksOverlay rocksOverlay;
    @Inject
    private MiningConfig config;
    @Nullable
    private MiningSession session;
    private final List<RockRespawn> respawns = new ArrayList<RockRespawn>();
    private boolean recentlyLoggedIn;
    @Nullable
    private Pickaxe pickaxe;

    @Provides
    MiningConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(MiningConfig.class);
    }

    @Override
    protected void startUp() {
        this.overlayManager.add(this.overlay);
        this.overlayManager.add(this.rocksOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        this.session = null;
        this.pickaxe = null;
        this.overlayManager.remove(this.overlay);
        this.overlayManager.remove(this.rocksOverlay);
        this.respawns.forEach(respawn -> this.clearHintArrowAt(respawn.getWorldPoint()));
        this.respawns.clear();
    }

    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked overlayMenuClicked) {
        OverlayMenuEntry overlayMenuEntry = overlayMenuClicked.getEntry();
        if (overlayMenuEntry.getMenuAction() == MenuAction.RUNELITE_OVERLAY && overlayMenuClicked.getEntry().getOption().equals("Reset") && overlayMenuClicked.getOverlay() == this.overlay) {
            this.session = null;
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        switch (event.getGameState()) {
            case HOPPING: {
                this.respawns.clear();
                break;
            }
            case LOGGED_IN: {
                this.recentlyLoggedIn = true;
            }
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        Player local = this.client.getLocalPlayer();
        if (event.getActor() != local) {
            return;
        }
        int animId = local.getAnimation();
        if (animId == 7201) {
            if (this.session == null) {
                this.session = new MiningSession();
            }
            this.session.setLastMined();
        } else {
            Pickaxe pickaxe = Pickaxe.fromAnimation(animId);
            if (pickaxe != null) {
                this.pickaxe = pickaxe;
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        this.clearExpiredRespawns();
        this.recentlyLoggedIn = false;
        if (this.session == null || this.session.getLastMined() == null) {
            return;
        }
        if (this.pickaxe != null && this.pickaxe.matchesMiningAnimation(this.client.getLocalPlayer())) {
            this.session.setLastMined();
            return;
        }
        Duration statTimeout = Duration.ofMinutes(this.config.statTimeout());
        Duration sinceMined = Duration.between(this.session.getLastMined(), Instant.now());
        if (sinceMined.compareTo(statTimeout) >= 0) {
            this.resetSession();
        }
    }

    private void clearExpiredRespawns() {
        this.respawns.removeIf(rockRespawn -> {
            boolean expired = rockRespawn.isExpired();
            if (expired && rockRespawn.getRock() == Rock.DAEYALT_ESSENCE) {
                this.clearHintArrowAt(rockRespawn.getWorldPoint());
            }
            return expired;
        });
    }

    public void resetSession() {
        this.session = null;
        this.pickaxe = null;
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        if (this.client.getGameState() != GameState.LOGGED_IN || this.recentlyLoggedIn) {
            return;
        }
        GameObject object = event.getGameObject();
        int region = this.client.getLocalPlayer().getWorldLocation().getRegionID();
        Rock rock = Rock.getRock(object.getId());
        if (rock != null) {
            WorldPoint point = object.getWorldLocation();
            if (rock == Rock.DAEYALT_ESSENCE) {
                this.respawns.removeIf(rockRespawn -> rockRespawn.getWorldPoint().equals((Object)point));
                this.clearHintArrowAt(point);
            } else {
                RockRespawn rockRespawn2 = new RockRespawn(rock, point, Instant.now(), (int)rock.getRespawnTime(region).toMillis(), rock.getZOffset());
                this.respawns.add(rockRespawn2);
            }
        }
    }

    private void clearHintArrowAt(WorldPoint worldPoint) {
        if (this.client.getHintArrowType() == 2 && this.client.getHintArrowPoint().equals((Object)worldPoint)) {
            this.client.clearHintArrow();
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        if (this.client.getGameState() != GameState.LOGGED_IN || this.recentlyLoggedIn) {
            return;
        }
        GameObject object = event.getGameObject();
        Rock rock = Rock.getRock(object.getId());
        if (rock == Rock.DAEYALT_ESSENCE) {
            int region = this.client.getLocalPlayer().getWorldLocation().getRegionID();
            RockRespawn rockRespawn2 = new RockRespawn(rock, object.getWorldLocation(), Instant.now(), (int)rock.getRespawnTime(region).toMillis(), rock.getZOffset());
            this.respawns.add(rockRespawn2);
            this.client.setHintArrow(object.getWorldLocation());
        } else if (rock == Rock.LOVAKITE) {
            WorldPoint point = object.getWorldLocation();
            this.respawns.removeIf(rockRespawn -> rockRespawn.getWorldPoint().equals((Object)point));
        }
    }

    @Subscribe
    public void onWallObjectSpawned(WallObjectSpawned event) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        WallObject object = event.getWallObject();
        int region = this.client.getLocalPlayer().getWorldLocation().getRegionID();
        switch (object.getId()) {
            case 11393: {
                Rock rock = Rock.AMETHYST;
                RockRespawn rockRespawn2 = new RockRespawn(rock, object.getWorldLocation(), Instant.now(), (int)rock.getRespawnTime(region).toMillis(), rock.getZOffset());
                this.respawns.add(rockRespawn2);
                break;
            }
            case 26665: 
            case 26666: 
            case 26667: 
            case 26668: {
                Rock rock = Rock.ORE_VEIN;
                RockRespawn rockRespawn3 = new RockRespawn(rock, object.getWorldLocation(), Instant.now(), (int)rock.getRespawnTime(region).toMillis(), rock.getZOffset());
                this.respawns.add(rockRespawn3);
                break;
            }
            case 41549: 
            case 41550: {
                Rock rock = Rock.BARRONITE;
                RockRespawn rockRespawn4 = new RockRespawn(rock, object.getWorldLocation(), Instant.now(), (int)rock.getRespawnTime(region).toMillis(), rock.getZOffset());
                this.respawns.add(rockRespawn4);
                break;
            }
            case 5992: {
                Rock rock = Rock.MINERAL_VEIN;
                RockRespawn rockRespawn5 = new RockRespawn(rock, object.getWorldLocation(), Instant.now(), (int)rock.getRespawnTime(region).toMillis(), rock.getZOffset());
                this.respawns.add(rockRespawn5);
                break;
            }
            case 5989: 
            case 5990: 
            case 5991: 
            case 26661: 
            case 26662: 
            case 26663: 
            case 26664: 
            case 41547: 
            case 41548: {
                WorldPoint point = object.getWorldLocation();
                this.respawns.removeIf(rockRespawn -> rockRespawn.getWorldPoint().equals((Object)point));
                break;
            }
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if ((event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) && MINING_PATTERN.matcher(event.getMessage()).matches()) {
            if (this.session == null) {
                this.session = new MiningSession();
            }
            this.session.setLastMined();
        }
    }

    @Nullable
    public MiningSession getSession() {
        return this.session;
    }

    List<RockRespawn> getRespawns() {
        return this.respawns;
    }

    @Nullable
    public Pickaxe getPickaxe() {
        return this.pickaxe;
    }
}

