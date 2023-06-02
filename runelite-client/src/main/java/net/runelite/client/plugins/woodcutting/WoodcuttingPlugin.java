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
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.GameObjectDespawned
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.ItemSpawned
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.woodcutting;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.woodcutting.Axe;
import net.runelite.client.plugins.woodcutting.Tree;
import net.runelite.client.plugins.woodcutting.TreeRespawn;
import net.runelite.client.plugins.woodcutting.WoodcuttingConfig;
import net.runelite.client.plugins.woodcutting.WoodcuttingOverlay;
import net.runelite.client.plugins.woodcutting.WoodcuttingSession;
import net.runelite.client.plugins.woodcutting.WoodcuttingTreesOverlay;
import net.runelite.client.plugins.woodcutting.config.ClueNestTier;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Woodcutting", description="Show woodcutting statistics and/or bird nest notifications", tags={"birds", "nest", "notifications", "overlay", "skilling", "wc"}, enabledByDefault=false)
@PluginDependency(value=XpTrackerPlugin.class)
public class WoodcuttingPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(WoodcuttingPlugin.class);
    private static final Pattern WOOD_CUT_PATTERN = Pattern.compile("You get (?:some|an)[\\w ]+(?:logs?|mushrooms)\\.");
    @Inject
    private Notifier notifier;
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private WoodcuttingOverlay overlay;
    @Inject
    private WoodcuttingTreesOverlay treesOverlay;
    @Inject
    private WoodcuttingConfig config;
    @Nullable
    private WoodcuttingSession session;
    @Nullable
    private Axe axe;
    private final Set<GameObject> treeObjects = new HashSet<GameObject>();
    private final List<TreeRespawn> respawns = new ArrayList<TreeRespawn>();
    private boolean recentlyLoggedIn;
    private int currentPlane;
    private ClueNestTier clueTierSpawned;

    @Provides
    WoodcuttingConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(WoodcuttingConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.overlay);
        this.overlayManager.add(this.treesOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.overlay);
        this.overlayManager.remove(this.treesOverlay);
        this.respawns.clear();
        this.treeObjects.clear();
        this.session = null;
        this.axe = null;
        this.clueTierSpawned = null;
    }

    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked overlayMenuClicked) {
        OverlayMenuEntry overlayMenuEntry = overlayMenuClicked.getEntry();
        if (overlayMenuEntry.getMenuAction() == MenuAction.RUNELITE_OVERLAY && overlayMenuClicked.getEntry().getOption().equals("Reset") && overlayMenuClicked.getOverlay() == this.overlay) {
            this.session = null;
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        this.recentlyLoggedIn = false;
        this.clueTierSpawned = null;
        this.currentPlane = this.client.getPlane();
        this.respawns.removeIf(TreeRespawn::isExpired);
        if (this.session == null || this.session.getLastChopping() == null) {
            return;
        }
        if (this.axe != null && this.axe.matchesChoppingAnimation(this.client.getLocalPlayer())) {
            this.session.setLastChopping();
            return;
        }
        Duration statTimeout = Duration.ofMinutes(this.config.statTimeout());
        Duration sinceCut = Duration.between(this.session.getLastChopping(), Instant.now());
        if (sinceCut.compareTo(statTimeout) >= 0) {
            this.session = null;
            this.axe = null;
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
            if (WOOD_CUT_PATTERN.matcher(event.getMessage()).matches()) {
                if (this.session == null) {
                    this.session = new WoodcuttingSession();
                }
                this.session.setLastChopping();
            }
            if (event.getMessage().contains("A bird's nest falls out of the tree") && this.config.showNestNotification()) {
                if (this.clueTierSpawned == null || this.clueTierSpawned.ordinal() >= this.config.clueNestNotifyTier().ordinal()) {
                    this.notifier.notify("A bird nest has spawned!");
                }
                this.clueTierSpawned = null;
            }
        }
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned itemSpawned) {
        if (this.clueTierSpawned == null) {
            this.clueTierSpawned = ClueNestTier.getTierFromItem(itemSpawned.getItem().getId());
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        GameObject gameObject = event.getGameObject();
        Tree tree = Tree.findTree(gameObject.getId());
        if (tree == Tree.REDWOOD) {
            this.treeObjects.add(gameObject);
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        GameObject object = event.getGameObject();
        Tree tree = Tree.findTree(object.getId());
        if (tree != null) {
            if (tree.getRespawnTime() != null && !this.recentlyLoggedIn && this.currentPlane == object.getPlane()) {
                log.debug("Adding respawn timer for {} tree at {}", (Object)tree, (Object)object.getLocalLocation());
                Point min = object.getSceneMinLocation();
                WorldPoint base = WorldPoint.fromScene((Client)this.client, (int)min.getX(), (int)min.getY(), (int)this.client.getPlane());
                TreeRespawn treeRespawn = new TreeRespawn(tree, object.sizeX() - 1, object.sizeY() - 1, base, Instant.now(), (int)tree.getRespawnTime(base.getRegionID()).toMillis());
                this.respawns.add(treeRespawn);
            }
            if (tree == Tree.REDWOOD) {
                this.treeObjects.remove((Object)event.getGameObject());
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        switch (event.getGameState()) {
            case HOPPING: {
                this.respawns.clear();
            }
            case LOADING: {
                this.treeObjects.clear();
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
        Axe axe = Axe.findAxeByAnimId(animId);
        if (axe != null) {
            this.axe = axe;
        }
    }

    @Nullable
    public WoodcuttingSession getSession() {
        return this.session;
    }

    @Nullable
    public Axe getAxe() {
        return this.axe;
    }

    public Set<GameObject> getTreeObjects() {
        return this.treeObjects;
    }

    List<TreeRespawn> getRespawns() {
        return this.respawns;
    }
}

