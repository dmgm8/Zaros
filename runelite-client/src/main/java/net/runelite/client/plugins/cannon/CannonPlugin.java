/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.GameState
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Player
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldArea
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.ItemContainerChanged
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.cannon;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.VarPlayer;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.cannon.CannonConfig;
import net.runelite.client.plugins.cannon.CannonCounter;
import net.runelite.client.plugins.cannon.CannonOverlay;
import net.runelite.client.plugins.cannon.CannonSpotOverlay;
import net.runelite.client.plugins.cannon.CannonSpots;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Cannon", description="Show information about cannon placement and/or amount of cannonballs", tags={"combat", "notifications", "ranged", "overlay"})
public class CannonPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(CannonPlugin.class);
    static final int MAX_OVERLAY_DISTANCE = 4100;
    static final int MAX_CBALLS = 30;
    private CannonCounter counter;
    private boolean cannonBallNotificationSent;
    private WorldPoint clickedCannonLocation;
    private boolean firstCannonLoad;
    private int cballsLeft;
    private boolean cannonPlaced;
    private WorldArea cannonPosition;
    private int cannonWorld = -1;
    private GameObject cannon;
    private List<WorldPoint> spotPoints = new ArrayList<WorldPoint>();
    @Inject
    private ItemManager itemManager;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private Notifier notifier;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private CannonOverlay cannonOverlay;
    @Inject
    private CannonSpotOverlay cannonSpotOverlay;
    @Inject
    private CannonConfig config;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;

    @Provides
    CannonConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CannonConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.cannonOverlay);
        this.overlayManager.add(this.cannonSpotOverlay);
        this.clientThread.invoke(() -> {
            this.cballsLeft = this.client.getVarpValue(VarPlayer.CANNON_AMMO);
        });
    }

    @Override
    protected void shutDown() throws Exception {
        this.cannonSpotOverlay.setHidden(true);
        this.overlayManager.remove(this.cannonOverlay);
        this.overlayManager.remove(this.cannonSpotOverlay);
        this.cannonPlaced = false;
        this.cannonWorld = -1;
        this.cannonPosition = null;
        this.cannonBallNotificationSent = false;
        this.cballsLeft = 0;
        this.removeCounter();
        this.spotPoints.clear();
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getItemContainer() != this.client.getItemContainer(InventoryID.INVENTORY)) {
            return;
        }
        boolean hasBase = false;
        boolean hasStand = false;
        boolean hasBarrels = false;
        boolean hasFurnace = false;
        boolean hasAll = false;
        if (!this.cannonPlaced) {
            for (Item item : event.getItemContainer().getItems()) {
                if (item == null) continue;
                switch (item.getId()) {
                    case 6: 
                    case 26520: {
                        hasBase = true;
                        break;
                    }
                    case 8: 
                    case 26522: {
                        hasStand = true;
                        break;
                    }
                    case 10: 
                    case 26524: {
                        hasBarrels = true;
                        break;
                    }
                    case 12: 
                    case 26526: {
                        hasFurnace = true;
                    }
                }
                if (!hasBase || !hasStand || !hasBarrels || !hasFurnace) continue;
                hasAll = true;
                break;
            }
        }
        this.cannonSpotOverlay.setHidden(!hasAll);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("cannon")) {
            if (!this.config.showInfobox()) {
                this.removeCounter();
            } else if (this.cannonPlaced) {
                this.clientThread.invoke(this::addCounter);
            }
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        this.spotPoints.clear();
        for (WorldPoint spot : CannonSpots.getCannonSpots()) {
            if (!WorldPoint.isInScene((Client)this.client, (int)spot.getX(), (int)spot.getY())) continue;
            this.spotPoints.add(spot);
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        GameObject gameObject = event.getGameObject();
        Player localPlayer = this.client.getLocalPlayer();
        if (!(gameObject.getId() != 7 && gameObject.getId() != 43029 || this.cannonPlaced || localPlayer.getWorldLocation().distanceTo(gameObject.getWorldLocation()) > 2 || localPlayer.getAnimation() != 827)) {
            this.cannonPosition = CannonPlugin.buildCannonWorldArea(gameObject.getWorldLocation());
            this.cannonWorld = this.client.getWorld();
            this.cannon = gameObject;
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        Widget selected;
        int itemId;
        if (this.cannonPosition != null || event.getId() != 6 && event.getId() != 43027) {
            return;
        }
        if (event.getMenuAction() == MenuAction.WIDGET_TARGET_ON_GAME_OBJECT && this.client.getSelectedWidget().getId() == WidgetInfo.INVENTORY.getId() ? (itemId = (selected = this.client.getSelectedWidget()).getItemId()) != 2 && itemId != 21728 : event.getMenuAction() != MenuAction.GAME_OBJECT_FIRST_OPTION) {
            return;
        }
        this.clickedCannonLocation = WorldPoint.fromScene((Client)this.client, (int)event.getParam0(), (int)event.getParam1(), (int)this.client.getPlane());
        log.debug("Updated cannon location: {}", (Object)this.clickedCannonLocation);
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged varbitChanged) {
        if (varbitChanged.getVarpId() == VarPlayer.CANNON_AMMO.getId()) {
            this.cballsLeft = varbitChanged.getValue();
            if (this.config.showCannonNotifications() && !this.cannonBallNotificationSent && this.cballsLeft > 0 && this.config.lowWarningThreshold() >= this.cballsLeft) {
                this.notifier.notify(String.format("Your cannon has %d cannon balls remaining!", this.cballsLeft));
                this.cannonBallNotificationSent = true;
            }
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }
        if (event.getMessage().equals("You add the furnace.")) {
            this.cannonPlaced = true;
            this.addCounter();
            this.firstCannonLoad = true;
        } else if (event.getMessage().contains("You pick up the cannon") || event.getMessage().contains("Your cannon has decayed. Speak to Nulodion to get a new one!") || event.getMessage().contains("Your cannon has been destroyed!")) {
            this.cannonPlaced = false;
            this.removeCounter();
            this.cannonPosition = null;
        } else if (event.getMessage().startsWith("You load the cannon with") || event.getMessage().startsWith("Your cannon is automatically loaded")) {
            if (this.cannonPosition == null && this.clickedCannonLocation != null) {
                if (this.firstCannonLoad) {
                    this.firstCannonLoad = false;
                } else {
                    GameObject[] objects;
                    LocalPoint lp = LocalPoint.fromWorld((Client)this.client, (WorldPoint)this.clickedCannonLocation);
                    if (lp != null && (objects = this.client.getScene().getTiles()[this.client.getPlane()][lp.getSceneX()][lp.getSceneY()].getGameObjects()).length > 0 && this.client.getLocalPlayer().getWorldLocation().distanceTo(objects[0].getWorldLocation()) <= 2) {
                        this.cannonPlaced = true;
                        this.cannonWorld = this.client.getWorld();
                        this.cannon = objects[0];
                        this.cannonPosition = CannonPlugin.buildCannonWorldArea(this.cannon.getWorldLocation());
                    }
                }
                this.clickedCannonLocation = null;
            }
            this.cannonBallNotificationSent = false;
        } else if (event.getMessage().contains("Your cannon is out of ammo!")) {
            if (this.config.showCannonNotifications()) {
                this.notifier.notify("Your cannon is out of ammo!");
            }
        } else if (event.getMessage().equals("This isn't your cannon!") || event.getMessage().equals("This is not your cannon.")) {
            this.clickedCannonLocation = null;
        }
    }

    Color getStateColor() {
        if (this.cballsLeft > 15) {
            return Color.green;
        }
        if (this.cballsLeft > 5) {
            return Color.orange;
        }
        return Color.red;
    }

    private void addCounter() {
        if (!this.config.showInfobox() || this.counter != null) {
            return;
        }
        this.counter = new CannonCounter((BufferedImage)this.itemManager.getImage(2), this);
        this.counter.setTooltip("Cannonballs");
        this.infoBoxManager.addInfoBox(this.counter);
    }

    private void removeCounter() {
        if (this.counter == null) {
            return;
        }
        this.infoBoxManager.removeInfoBox(this.counter);
        this.counter = null;
    }

    private static WorldArea buildCannonWorldArea(WorldPoint worldPoint) {
        return new WorldArea(worldPoint.getX() - 1, worldPoint.getY() - 1, 3, 3, worldPoint.getPlane());
    }

    public int getCballsLeft() {
        return this.cballsLeft;
    }

    public boolean isCannonPlaced() {
        return this.cannonPlaced;
    }

    public WorldArea getCannonPosition() {
        return this.cannonPosition;
    }

    public int getCannonWorld() {
        return this.cannonWorld;
    }

    public GameObject getCannon() {
        return this.cannon;
    }

    public List<WorldPoint> getSpotPoints() {
        return this.spotPoints;
    }
}

