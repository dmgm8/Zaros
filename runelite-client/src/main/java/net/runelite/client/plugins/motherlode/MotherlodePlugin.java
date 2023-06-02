/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultiset
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Multiset
 *  com.google.common.collect.Multisets
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.GameState
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Perspective
 *  net.runelite.api.WallObject
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.GameObjectDespawned
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.ItemContainerChanged
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.events.WallObjectDespawned
 *  net.runelite.api.events.WallObjectSpawned
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.motherlode;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.Perspective;
import net.runelite.api.WallObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.motherlode.MotherlodeConfig;
import net.runelite.client.plugins.motherlode.MotherlodeGemOverlay;
import net.runelite.client.plugins.motherlode.MotherlodeOreOverlay;
import net.runelite.client.plugins.motherlode.MotherlodeOverlay;
import net.runelite.client.plugins.motherlode.MotherlodeSackOverlay;
import net.runelite.client.plugins.motherlode.MotherlodeSceneOverlay;
import net.runelite.client.plugins.motherlode.MotherlodeSession;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;

@PluginDescriptor(name="Motherlode Mine", description="Show helpful information inside the Motherload Mine", tags={"pay", "dirt", "mining", "mlm", "skilling", "overlay"}, enabledByDefault=true)
public class MotherlodePlugin
extends Plugin {
    private static final Set<Integer> MOTHERLODE_MAP_REGIONS = ImmutableSet.of((Object)14679, (Object)14680, (Object)14681, (Object)14935, (Object)14936, (Object)14937, (Object[])new Integer[]{15191, 15192, 15193});
    private static final Set<Integer> MINE_SPOTS = ImmutableSet.of((Object)26661, (Object)26662, (Object)26663, (Object)26664);
    private static final Set<Integer> MLM_ORE_TYPES = ImmutableSet.of((Object)451, (Object)449, (Object)447, (Object)444, (Object)453, (Object)12012, (Object[])new Integer[0]);
    private static final Set<Integer> ROCK_OBSTACLES = ImmutableSet.of((Object)26679, (Object)26680);
    private static final int MAX_INVENTORY_SIZE = 28;
    private static final int SACK_LARGE_SIZE = 250;
    private static final int SACK_SIZE = 250;
    private static final int UPPER_FLOOR_HEIGHT = -490;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private MotherlodeOverlay overlay;
    @Inject
    private MotherlodeSceneOverlay sceneOverlay;
    @Inject
    private MotherlodeSackOverlay motherlodeSackOverlay;
    @Inject
    private MotherlodeGemOverlay motherlodeGemOverlay;
    @Inject
    private MotherlodeOreOverlay motherlodeOreOverlay;
    @Inject
    private MotherlodeConfig config;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    private boolean inMlm;
    private int curSackSize;
    private int maxSackSize;
    private Integer depositsLeft;
    @Inject
    private MotherlodeSession session;
    private boolean shouldUpdateOres;
    private Multiset<Integer> inventorySnapshot;
    private final Set<WallObject> veins = new HashSet<WallObject>();
    private final Set<GameObject> rocks = new HashSet<GameObject>();
    private final Set<GameObject> brokenStruts = new HashSet<GameObject>();

    @Provides
    MotherlodeConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(MotherlodeConfig.class);
    }

    @Override
    protected void startUp() {
        this.overlayManager.add(this.overlay);
        this.overlayManager.add(this.sceneOverlay);
        this.overlayManager.add(this.motherlodeGemOverlay);
        this.overlayManager.add(this.motherlodeOreOverlay);
        this.overlayManager.add(this.motherlodeSackOverlay);
        this.inMlm = this.checkInMlm();
        if (this.inMlm) {
            this.clientThread.invokeLater(this::refreshSackValues);
        }
    }

    @Override
    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
        this.overlayManager.remove(this.sceneOverlay);
        this.overlayManager.remove(this.motherlodeGemOverlay);
        this.overlayManager.remove(this.motherlodeOreOverlay);
        this.overlayManager.remove(this.motherlodeSackOverlay);
        this.veins.clear();
        this.rocks.clear();
        this.brokenStruts.clear();
        Widget sack = this.client.getWidget(WidgetInfo.MOTHERLODE_MINE);
        this.clientThread.invokeLater(() -> {
            if (sack != null && sack.isHidden()) {
                sack.setHidden(false);
            }
        });
    }

    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked overlayMenuClicked) {
        OverlayMenuEntry overlayMenuEntry = overlayMenuClicked.getEntry();
        if (overlayMenuEntry.getMenuAction() == MenuAction.RUNELITE_OVERLAY && overlayMenuClicked.getEntry().getOption().equals("Reset") && overlayMenuClicked.getOverlay() == this.overlay) {
            this.session.resetRecent();
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        if (this.inMlm) {
            ItemContainer itemContainer;
            int lastSackValue = this.curSackSize;
            this.refreshSackValues();
            boolean bl = this.shouldUpdateOres = this.curSackSize < lastSackValue;
            if (this.shouldUpdateOres && (itemContainer = this.client.getItemContainer(InventoryID.INVENTORY)) != null) {
                this.inventorySnapshot = HashMultiset.create();
                Arrays.stream(itemContainer.getItems()).filter(item -> MLM_ORE_TYPES.contains(item.getId())).forEach(item -> this.inventorySnapshot.add((Object)item.getId(), item.getQuantity()));
            }
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String chatMessage;
        if (!this.inMlm || event.getType() != ChatMessageType.SPAM) {
            return;
        }
        switch (chatMessage = event.getMessage()) {
            case "You get some pay-dirt.": {
                this.session.incrementPayDirtMined();
                break;
            }
            case "You just found a Diamond!": {
                this.session.incrementGemFound(1617);
                break;
            }
            case "You just found a Ruby!": {
                this.session.incrementGemFound(1619);
                break;
            }
            case "You just found an Emerald!": {
                this.session.incrementGemFound(1621);
                break;
            }
            case "You just found a Sapphire!": {
                this.session.incrementGemFound(1623);
            }
        }
    }

    @Schedule(period=1L, unit=ChronoUnit.SECONDS)
    public void checkMining() {
        if (!this.inMlm) {
            return;
        }
        this.depositsLeft = this.calculateDepositsLeft();
        Instant lastPayDirtMined = this.session.getLastPayDirtMined();
        if (lastPayDirtMined == null) {
            return;
        }
        Duration statTimeout = Duration.ofMinutes(this.config.statTimeout());
        Duration sinceMined = Duration.between(lastPayDirtMined, Instant.now());
        if (sinceMined.compareTo(statTimeout) >= 0) {
            this.session.resetRecent();
        }
    }

    @Subscribe
    public void onWallObjectSpawned(WallObjectSpawned event) {
        if (!this.inMlm) {
            return;
        }
        WallObject wallObject = event.getWallObject();
        if (MINE_SPOTS.contains(wallObject.getId())) {
            this.veins.add(wallObject);
        }
    }

    @Subscribe
    public void onWallObjectDespawned(WallObjectDespawned event) {
        if (!this.inMlm) {
            return;
        }
        WallObject wallObject = event.getWallObject();
        this.veins.remove((Object)wallObject);
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        if (!this.inMlm) {
            return;
        }
        this.addGameObject(event.getGameObject());
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        if (!this.inMlm) {
            return;
        }
        this.removeGameObject(event.getGameObject());
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            this.veins.clear();
            this.rocks.clear();
            this.brokenStruts.clear();
            this.inMlm = this.checkInMlm();
        } else if (event.getGameState() == GameState.LOGIN_SCREEN) {
            this.inMlm = false;
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        ItemContainer container = event.getItemContainer();
        if (!this.inMlm || !this.shouldUpdateOres || this.inventorySnapshot == null || container != this.client.getItemContainer(InventoryID.INVENTORY)) {
            return;
        }
        HashMultiset current = HashMultiset.create();
        Arrays.stream(container.getItems()).filter(item -> MLM_ORE_TYPES.contains(item.getId())).forEach(arg_0 -> MotherlodePlugin.lambda$onItemContainerChanged$4((Multiset)current, arg_0));
        Multiset delta = Multisets.difference((Multiset)current, this.inventorySnapshot);
        delta.forEachEntry((arg_0, arg_1) -> this.session.updateOreFound(arg_0, arg_1));
        this.inventorySnapshot = null;
        this.shouldUpdateOres = false;
    }

    private Integer calculateDepositsLeft() {
        if (this.maxSackSize == 0) {
            this.refreshSackValues();
        }
        double depositsLeft = 0.0;
        int nonPayDirtItems = 0;
        ItemContainer inventory = this.client.getItemContainer(InventoryID.INVENTORY);
        if (inventory == null) {
            return null;
        }
        Item[] result = inventory.getItems();
        assert (result != null);
        for (Item item : result) {
            if (item.getId() == 12011 || item.getId() == -1 || MLM_ORE_TYPES.contains(item.getId())) continue;
            ++nonPayDirtItems;
        }
        double inventorySpace = 28 - nonPayDirtItems;
        double sackSizeRemaining = this.maxSackSize - this.curSackSize;
        if (inventorySpace > 0.0 && sackSizeRemaining > 0.0) {
            depositsLeft = Math.ceil(sackSizeRemaining / inventorySpace);
        } else if (inventorySpace == 0.0) {
            return null;
        }
        return (int)depositsLeft;
    }

    private boolean checkInMlm() {
        int[] currentMapRegions;
        GameState gameState = this.client.getGameState();
        if (gameState != GameState.LOGGED_IN && gameState != GameState.LOADING) {
            return false;
        }
        for (int region : currentMapRegions = this.client.getMapRegions()) {
            if (MOTHERLODE_MAP_REGIONS.contains(region)) continue;
            return false;
        }
        return true;
    }

    private void refreshSackValues() {
        this.curSackSize = this.client.getVarbitValue(5558);
        boolean sackUpgraded = this.client.getVarbitValue(5556) == 1;
        this.maxSackSize = sackUpgraded ? 250 : 250;
    }

    boolean isUpstairs(LocalPoint localPoint) {
        return Perspective.getTileHeight((Client)this.client, (LocalPoint)localPoint, (int)0) < -490;
    }

    private void addGameObject(GameObject gameObject) {
        if (ROCK_OBSTACLES.contains(gameObject.getId())) {
            this.rocks.add(gameObject);
        }
        if (26670 == gameObject.getId()) {
            this.brokenStruts.add(gameObject);
        }
    }

    private void removeGameObject(GameObject gameObject) {
        this.rocks.remove((Object)gameObject);
        this.brokenStruts.remove((Object)gameObject);
    }

    boolean isInMlm() {
        return this.inMlm;
    }

    int getCurSackSize() {
        return this.curSackSize;
    }

    int getMaxSackSize() {
        return this.maxSackSize;
    }

    Integer getDepositsLeft() {
        return this.depositsLeft;
    }

    Set<WallObject> getVeins() {
        return this.veins;
    }

    Set<GameObject> getRocks() {
        return this.rocks;
    }

    Set<GameObject> getBrokenStruts() {
        return this.brokenStruts;
    }

    private static /* synthetic */ void lambda$onItemContainerChanged$4(Multiset current, Item item) {
        current.add((Object)item.getId(), item.getQuantity());
    }
}

