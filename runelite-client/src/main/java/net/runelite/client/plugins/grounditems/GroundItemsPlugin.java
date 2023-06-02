/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  com.google.common.cache.LoadingCache
 *  com.google.common.collect.EvictingQueue
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  com.google.common.collect.Table
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.Tile
 *  net.runelite.api.TileItem
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.ClientTick
 *  net.runelite.api.events.FocusChanged
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.ItemDespawned
 *  net.runelite.api.events.ItemQuantityChanged
 *  net.runelite.api.events.ItemSpawned
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.grounditems;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Rectangle;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemQuantityChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.grounditems.GroundItem;
import net.runelite.client.plugins.grounditems.GroundItemHotkeyListener;
import net.runelite.client.plugins.grounditems.GroundItemMouseAdapter;
import net.runelite.client.plugins.grounditems.GroundItemsConfig;
import net.runelite.client.plugins.grounditems.GroundItemsOverlay;
import net.runelite.client.plugins.grounditems.LootType;
import net.runelite.client.plugins.grounditems.Lootbeam;
import net.runelite.client.plugins.grounditems.MenuEntryWithCount;
import net.runelite.client.plugins.grounditems.NamedQuantity;
import net.runelite.client.plugins.grounditems.WildcardMatchLoader;
import net.runelite.client.plugins.grounditems.config.HighlightTier;
import net.runelite.client.plugins.grounditems.config.ItemHighlightMode;
import net.runelite.client.plugins.grounditems.config.MenuHighlightMode;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;

@PluginDescriptor(name="Ground Items", description="Highlight ground items and/or show price information", tags={"grand", "exchange", "high", "alchemy", "prices", "highlight", "overlay", "lootbeam"})
public class GroundItemsPlugin
extends Plugin {
    static final int MAX_QUANTITY = 65535;
    private static final int COINS = 995;
    private Map.Entry<Rectangle, GroundItem> textBoxBounds;
    private Map.Entry<Rectangle, GroundItem> hiddenBoxBounds;
    private Map.Entry<Rectangle, GroundItem> highlightBoxBounds;
    private boolean hotKeyPressed;
    private boolean hideAll;
    private List<String> hiddenItemList = new CopyOnWriteArrayList<String>();
    private List<String> highlightedItemsList = new CopyOnWriteArrayList<String>();
    @Inject
    private GroundItemHotkeyListener hotkeyListener;
    @Inject
    private GroundItemMouseAdapter mouseAdapter;
    @Inject
    private MouseManager mouseManager;
    @Inject
    private KeyManager keyManager;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ItemManager itemManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private GroundItemsConfig config;
    @Inject
    private GroundItemsOverlay overlay;
    @Inject
    private Notifier notifier;
    @Inject
    private ScheduledExecutorService executor;
    private final Table<WorldPoint, Integer, GroundItem> collectedGroundItems = HashBasedTable.create();
    private List<PriceHighlight> priceChecks = ImmutableList.of();
    private LoadingCache<NamedQuantity, Boolean> highlightedItems;
    private LoadingCache<NamedQuantity, Boolean> hiddenItems;
    private final Queue<Integer> droppedItemQueue = EvictingQueue.create((int)16);
    private int lastUsedItem;
    private final Map<WorldPoint, Lootbeam> lootbeams = new HashMap<WorldPoint, Lootbeam>();

    @Provides
    GroundItemsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(GroundItemsConfig.class);
    }

    @Override
    protected void startUp() {
        this.overlayManager.add(this.overlay);
        this.mouseManager.registerMouseListener(this.mouseAdapter);
        this.keyManager.registerKeyListener(this.hotkeyListener);
        this.executor.execute(this::reset);
        this.lastUsedItem = -1;
    }

    @Override
    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
        this.mouseManager.unregisterMouseListener(this.mouseAdapter);
        this.keyManager.unregisterKeyListener(this.hotkeyListener);
        this.highlightedItems.invalidateAll();
        this.highlightedItems = null;
        this.hiddenItems.invalidateAll();
        this.hiddenItems = null;
        this.hiddenItemList = null;
        this.highlightedItemsList = null;
        this.collectedGroundItems.clear();
        this.clientThread.invokeLater(this::removeAllLootbeams);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("grounditems")) {
            this.executor.execute(this::reset);
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOADING) {
            this.collectedGroundItems.clear();
            this.lootbeams.clear();
        }
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned itemSpawned) {
        TileItem item = itemSpawned.getItem();
        Tile tile = itemSpawned.getTile();
        GroundItem groundItem = this.buildGroundItem(tile, item);
        GroundItem existing = (GroundItem)this.collectedGroundItems.get((Object)tile.getWorldLocation(), (Object)item.getId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + groundItem.getQuantity());
        } else {
            this.collectedGroundItems.put((Object)tile.getWorldLocation(), (Object)item.getId(), (Object)groundItem);
        }
        if (!this.config.onlyShowLoot()) {
            this.notifyHighlightedItem(groundItem);
        }
        this.handleLootbeam(tile.getWorldLocation());
    }

    @Subscribe
    public void onItemDespawned(ItemDespawned itemDespawned) {
        TileItem item = itemDespawned.getItem();
        Tile tile = itemDespawned.getTile();
        GroundItem groundItem = (GroundItem)this.collectedGroundItems.get((Object)tile.getWorldLocation(), (Object)item.getId());
        if (groundItem == null) {
            return;
        }
        if (groundItem.getQuantity() <= item.getQuantity()) {
            this.collectedGroundItems.remove((Object)tile.getWorldLocation(), (Object)item.getId());
        } else {
            groundItem.setQuantity(groundItem.getQuantity() - item.getQuantity());
            groundItem.setSpawnTime(null);
        }
        this.handleLootbeam(tile.getWorldLocation());
    }

    @Subscribe
    public void onItemQuantityChanged(ItemQuantityChanged itemQuantityChanged) {
        TileItem item = itemQuantityChanged.getItem();
        Tile tile = itemQuantityChanged.getTile();
        int oldQuantity = itemQuantityChanged.getOldQuantity();
        int newQuantity = itemQuantityChanged.getNewQuantity();
        int diff = newQuantity - oldQuantity;
        GroundItem groundItem = (GroundItem)this.collectedGroundItems.get((Object)tile.getWorldLocation(), (Object)item.getId());
        if (groundItem != null) {
            groundItem.setQuantity(groundItem.getQuantity() + diff);
        }
        this.handleLootbeam(tile.getWorldLocation());
    }

    @Subscribe
    public void onNpcLootReceived(NpcLootReceived npcLootReceived) {
        Collection<ItemStack> items = npcLootReceived.getItems();
        this.lootReceived(items, LootType.PVM);
    }

    @Subscribe
    public void onPlayerLootReceived(PlayerLootReceived playerLootReceived) {
        Collection<ItemStack> items = playerLootReceived.getItems();
        this.lootReceived(items, LootType.PVP);
    }

    @Subscribe
    public void onClientTick(ClientTick event) {
        if (!this.config.collapseEntries()) {
            return;
        }
        MenuEntry[] menuEntries = this.client.getMenuEntries();
        ArrayList<MenuEntryWithCount> newEntries = new ArrayList<MenuEntryWithCount>(menuEntries.length);
        block0: for (int i = menuEntries.length - 1; i >= 0; --i) {
            MenuEntry menuEntry = menuEntries[i];
            MenuAction menuType = menuEntry.getType();
            if (menuType == MenuAction.GROUND_ITEM_FIRST_OPTION || menuType == MenuAction.GROUND_ITEM_SECOND_OPTION || menuType == MenuAction.GROUND_ITEM_THIRD_OPTION || menuType == MenuAction.GROUND_ITEM_FOURTH_OPTION || menuType == MenuAction.GROUND_ITEM_FIFTH_OPTION || menuType == MenuAction.EXAMINE_ITEM_GROUND) {
                for (MenuEntryWithCount entryWCount : newEntries) {
                    if (!entryWCount.getEntry().equals((Object)menuEntry)) continue;
                    entryWCount.increment();
                    continue block0;
                }
            }
            newEntries.add(new MenuEntryWithCount(menuEntry));
        }
        Collections.reverse(newEntries);
        this.client.setMenuEntries((MenuEntry[])newEntries.stream().map(e -> {
            MenuEntry entry = e.getEntry();
            int count = e.getCount();
            if (count > 1) {
                entry.setTarget(entry.getTarget() + " x " + count);
            }
            return entry;
        }).toArray(MenuEntry[]::new));
    }

    private void lootReceived(Collection<ItemStack> items, LootType lootType) {
        for (ItemStack itemStack : items) {
            WorldPoint location = WorldPoint.fromLocal((Client)this.client, (LocalPoint)itemStack.getLocation());
            GroundItem groundItem = (GroundItem)this.collectedGroundItems.get((Object)location, (Object)itemStack.getId());
            if (groundItem == null) continue;
            groundItem.setLootType(lootType);
            if (!this.config.onlyShowLoot()) continue;
            this.notifyHighlightedItem(groundItem);
        }
        items.stream().map(ItemStack::getLocation).map(l -> WorldPoint.fromLocal((Client)this.client, (LocalPoint)l)).distinct().forEach(this::handleLootbeam);
    }

    private GroundItem buildGroundItem(Tile tile, TileItem item) {
        int itemId = item.getId();
        ItemComposition itemComposition = this.itemManager.getItemComposition(itemId);
        int realItemId = itemComposition.getNote() != -1 ? itemComposition.getLinkedNoteId() : itemId;
        int alchPrice = itemComposition.getHaPrice();
        boolean dropped = tile.getWorldLocation().equals((Object)this.client.getLocalPlayer().getWorldLocation()) && this.droppedItemQueue.remove(itemId);
        boolean table = itemId == this.lastUsedItem && tile.getItemLayer().getHeight() > 0;
        GroundItem groundItem = GroundItem.builder().id(itemId).location(tile.getWorldLocation()).itemId(realItemId).quantity(item.getQuantity()).name(itemComposition.getName()).haPrice(alchPrice).height(tile.getItemLayer().getHeight()).tradeable(itemComposition.isTradeable()).lootType(dropped ? LootType.DROPPED : (table ? LootType.TABLE : LootType.UNKNOWN)).spawnTime(Instant.now()).stackable(itemComposition.isStackable()).build();
        if (realItemId == 995) {
            groundItem.setHaPrice(1);
            groundItem.setGePrice(1);
        } else {
            groundItem.setGePrice(this.itemManager.getItemPrice(realItemId));
        }
        return groundItem;
    }

    private void reset() {
        this.hiddenItemList = Text.fromCSV(this.config.getHiddenItems());
        this.highlightedItemsList = Text.fromCSV(this.config.getHighlightItems());
        this.highlightedItems = CacheBuilder.newBuilder().maximumSize(512L).expireAfterAccess(10L, TimeUnit.MINUTES).build((CacheLoader)new WildcardMatchLoader(this.highlightedItemsList));
        this.hiddenItems = CacheBuilder.newBuilder().maximumSize(512L).expireAfterAccess(10L, TimeUnit.MINUTES).build((CacheLoader)new WildcardMatchLoader(this.hiddenItemList));
        ImmutableList.Builder priceCheckBuilder = ImmutableList.builder();
        if (this.config.insaneValuePrice() > 0) {
            priceCheckBuilder.add((Object)new PriceHighlight(this.config.insaneValuePrice(), this.config.insaneValueColor()));
        }
        if (this.config.highValuePrice() > 0) {
            priceCheckBuilder.add((Object)new PriceHighlight(this.config.highValuePrice(), this.config.highValueColor()));
        }
        if (this.config.mediumValuePrice() > 0) {
            priceCheckBuilder.add((Object)new PriceHighlight(this.config.mediumValuePrice(), this.config.mediumValueColor()));
        }
        if (this.config.lowValuePrice() > 0) {
            priceCheckBuilder.add((Object)new PriceHighlight(this.config.lowValuePrice(), this.config.lowValueColor()));
        }
        this.priceChecks = priceCheckBuilder.build();
        this.clientThread.invokeLater(this::handleLootbeams);
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        MenuAction type = MenuAction.of((int)event.getType());
        if (type == MenuAction.GROUND_ITEM_FIRST_OPTION || type == MenuAction.GROUND_ITEM_SECOND_OPTION || type == MenuAction.GROUND_ITEM_THIRD_OPTION || type == MenuAction.GROUND_ITEM_FOURTH_OPTION || type == MenuAction.GROUND_ITEM_FIFTH_OPTION || type == MenuAction.WIDGET_TARGET_ON_GROUND_ITEM) {
            boolean canBeRecolored;
            int itemId = event.getIdentifier();
            int sceneX = event.getActionParam0();
            int sceneY = event.getActionParam1();
            MenuEntry[] menuEntries = this.client.getMenuEntries();
            MenuEntry lastEntry = menuEntries[menuEntries.length - 1];
            WorldPoint worldPoint = WorldPoint.fromScene((Client)this.client, (int)sceneX, (int)sceneY, (int)this.client.getPlane());
            GroundItem groundItem = (GroundItem)this.collectedGroundItems.get((Object)worldPoint, (Object)itemId);
            int quantity = groundItem.getQuantity();
            int gePrice = groundItem.getGePrice();
            int haPrice = groundItem.getHaPrice();
            Color hidden = this.getHidden(new NamedQuantity(groundItem.getName(), quantity), gePrice, haPrice, groundItem.isTradeable());
            Color highlighted = this.getHighlighted(new NamedQuantity(groundItem.getName(), quantity), gePrice, haPrice);
            Color color = this.getItemColor(highlighted, hidden);
            boolean bl = canBeRecolored = highlighted != null || hidden != null && this.config.recolorMenuHiddenItems();
            if ((this.config.itemHighlightMode() == ItemHighlightMode.MENU || this.config.itemHighlightMode() == ItemHighlightMode.BOTH) && color != null && canBeRecolored && !color.equals(this.config.defaultColor())) {
                MenuHighlightMode mode = this.config.menuHighlightMode();
                if (mode == MenuHighlightMode.BOTH || mode == MenuHighlightMode.OPTION) {
                    lastEntry.setOption(ColorUtil.prependColorTag(lastEntry.getOption(), color));
                }
                if (mode == MenuHighlightMode.BOTH || mode == MenuHighlightMode.NAME) {
                    String target = lastEntry.getTarget();
                    int i = target.lastIndexOf(62);
                    lastEntry.setTarget(target.substring(0, i - 11) + ColorUtil.colorTag(color) + target.substring(i + 1));
                }
            }
            if (this.config.showMenuItemQuantities() && groundItem.isStackable() && quantity > 1) {
                lastEntry.setTarget(lastEntry.getTarget() + " (" + quantity + ")");
            }
            if (hidden != null && highlighted == null && this.config.deprioritizeHiddenItems()) {
                lastEntry.setDeprioritized(true);
            }
        }
    }

    void updateList(String item, boolean hiddenList) {
        ArrayList<String> hiddenItemSet = new ArrayList<String>(this.hiddenItemList);
        ArrayList<String> highlightedItemSet = new ArrayList<String>(this.highlightedItemsList);
        if (hiddenList) {
            highlightedItemSet.removeIf(item::equalsIgnoreCase);
        } else {
            hiddenItemSet.removeIf(item::equalsIgnoreCase);
        }
        ArrayList<String> items = hiddenList ? hiddenItemSet : highlightedItemSet;
        if (!items.removeIf(item::equalsIgnoreCase)) {
            items.add(item);
        }
        this.config.setHiddenItems(Text.toCSV(hiddenItemSet));
        this.config.setHighlightedItem(Text.toCSV(highlightedItemSet));
    }

    Color getHighlighted(NamedQuantity item, int gePrice, int haPrice) {
        if (Boolean.TRUE.equals(this.highlightedItems.getUnchecked((Object)item))) {
            return this.config.highlightedColor();
        }
        if (Boolean.TRUE.equals(this.hiddenItems.getUnchecked((Object)item))) {
            return null;
        }
        int price = this.getValueByMode(gePrice, haPrice);
        for (PriceHighlight highlight : this.priceChecks) {
            if (price <= highlight.getPrice()) continue;
            return highlight.getColor();
        }
        return null;
    }

    Color getHidden(NamedQuantity item, int gePrice, int haPrice, boolean isTradeable) {
        boolean isExplicitHidden = Boolean.TRUE.equals(this.hiddenItems.getUnchecked((Object)item));
        boolean isExplicitHighlight = Boolean.TRUE.equals(this.highlightedItems.getUnchecked((Object)item));
        boolean canBeHidden = gePrice > 0 || isTradeable || !this.config.dontHideUntradeables();
        boolean underGe = gePrice < this.config.getHideUnderValue();
        boolean underHa = haPrice < this.config.getHideUnderValue();
        return isExplicitHidden || !isExplicitHighlight && canBeHidden && underGe && underHa ? this.config.hiddenColor() : null;
    }

    Color getItemColor(Color highlighted, Color hidden) {
        if (highlighted != null) {
            return highlighted;
        }
        if (hidden != null) {
            return hidden;
        }
        return this.config.defaultColor();
    }

    @Subscribe
    public void onFocusChanged(FocusChanged focusChanged) {
        if (!focusChanged.isFocused()) {
            this.setHotKeyPressed(false);
        }
    }

    private void notifyHighlightedItem(GroundItem item) {
        String dropType;
        boolean shouldNotifyTier;
        boolean shouldNotifyHighlighted = this.config.notifyHighlightedDrops() && Boolean.TRUE.equals(this.highlightedItems.getUnchecked((Object)new NamedQuantity(item)));
        boolean bl = shouldNotifyTier = this.config.notifyTier() != HighlightTier.OFF && this.getValueByMode(item.getGePrice(), item.getHaPrice()) > this.config.notifyTier().getValueFromTier(this.config) && Boolean.FALSE.equals(this.hiddenItems.getUnchecked((Object)new NamedQuantity(item)));
        if (shouldNotifyHighlighted) {
            dropType = "highlighted";
        } else if (shouldNotifyTier) {
            dropType = "valuable";
        } else {
            return;
        }
        StringBuilder notificationStringBuilder = new StringBuilder().append("You received a ").append(dropType).append(" drop: ").append(item.getName());
        if (item.getQuantity() > 1) {
            if (item.getQuantity() >= 65535) {
                notificationStringBuilder.append(" (Lots!)");
            } else {
                notificationStringBuilder.append(" (").append(QuantityFormatter.quantityToStackSize(item.getQuantity())).append(')');
            }
        }
        this.notifier.notify(notificationStringBuilder.toString());
    }

    private int getValueByMode(int gePrice, int haPrice) {
        switch (this.config.valueCalculationMode()) {
            case GE: {
                return gePrice;
            }
            case HA: {
                return haPrice;
            }
        }
        return Math.max(gePrice, haPrice);
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
        if (menuOptionClicked.isItemOp() && menuOptionClicked.getMenuOption().equals("Drop")) {
            int itemId = menuOptionClicked.getItemId();
            this.droppedItemQueue.add(itemId);
        } else if (menuOptionClicked.getMenuAction() == MenuAction.WIDGET_TARGET_ON_GAME_OBJECT && this.client.getSelectedWidget().getId() == WidgetInfo.INVENTORY.getId()) {
            this.lastUsedItem = this.client.getSelectedWidget().getItemId();
        }
    }

    private void handleLootbeam(WorldPoint worldPoint) {
        if (!this.config.showLootbeamForHighlighted() && this.config.showLootbeamTier() == HighlightTier.OFF) {
            this.removeLootbeam(worldPoint);
            return;
        }
        int price = -1;
        Collection groundItems = this.collectedGroundItems.row((Object)worldPoint).values();
        for (GroundItem groundItem : groundItems) {
            if (this.config.onlyShowLoot() && !groundItem.isMine()) continue;
            NamedQuantity item = new NamedQuantity(groundItem);
            if (this.config.showLootbeamForHighlighted() && Boolean.TRUE.equals(this.highlightedItems.getUnchecked((Object)item))) {
                this.addLootbeam(worldPoint, this.config.highlightedColor());
                return;
            }
            if (Boolean.TRUE.equals(this.hiddenItems.getUnchecked((Object)item))) continue;
            int itemPrice = this.getValueByMode(groundItem.getGePrice(), groundItem.getHaPrice());
            price = Math.max(itemPrice, price);
        }
        if (this.config.showLootbeamTier() != HighlightTier.OFF) {
            for (PriceHighlight highlight : this.priceChecks) {
                if (price <= highlight.getPrice() || price <= this.config.showLootbeamTier().getValueFromTier(this.config)) continue;
                this.addLootbeam(worldPoint, highlight.color);
                return;
            }
        }
        this.removeLootbeam(worldPoint);
    }

    private void handleLootbeams() {
        for (WorldPoint worldPoint : this.collectedGroundItems.rowKeySet()) {
            this.handleLootbeam(worldPoint);
        }
    }

    private void removeAllLootbeams() {
        for (Lootbeam lootbeam : this.lootbeams.values()) {
            lootbeam.remove();
        }
        this.lootbeams.clear();
    }

    private void addLootbeam(WorldPoint worldPoint, Color color) {
        Lootbeam lootbeam = this.lootbeams.get((Object)worldPoint);
        if (lootbeam == null) {
            lootbeam = new Lootbeam(this.client, this.clientThread, worldPoint, color, this.config.lootbeamStyle());
            this.lootbeams.put(worldPoint, lootbeam);
        } else {
            lootbeam.setColor(color);
            lootbeam.setStyle(this.config.lootbeamStyle());
        }
    }

    private void removeLootbeam(WorldPoint worldPoint) {
        Lootbeam lootbeam = this.lootbeams.remove((Object)worldPoint);
        if (lootbeam != null) {
            lootbeam.remove();
        }
    }

    Map.Entry<Rectangle, GroundItem> getTextBoxBounds() {
        return this.textBoxBounds;
    }

    void setTextBoxBounds(Map.Entry<Rectangle, GroundItem> textBoxBounds) {
        this.textBoxBounds = textBoxBounds;
    }

    Map.Entry<Rectangle, GroundItem> getHiddenBoxBounds() {
        return this.hiddenBoxBounds;
    }

    void setHiddenBoxBounds(Map.Entry<Rectangle, GroundItem> hiddenBoxBounds) {
        this.hiddenBoxBounds = hiddenBoxBounds;
    }

    Map.Entry<Rectangle, GroundItem> getHighlightBoxBounds() {
        return this.highlightBoxBounds;
    }

    void setHighlightBoxBounds(Map.Entry<Rectangle, GroundItem> highlightBoxBounds) {
        this.highlightBoxBounds = highlightBoxBounds;
    }

    boolean isHotKeyPressed() {
        return this.hotKeyPressed;
    }

    void setHotKeyPressed(boolean hotKeyPressed) {
        this.hotKeyPressed = hotKeyPressed;
    }

    boolean isHideAll() {
        return this.hideAll;
    }

    void setHideAll(boolean hideAll) {
        this.hideAll = hideAll;
    }

    public Table<WorldPoint, Integer, GroundItem> getCollectedGroundItems() {
        return this.collectedGroundItems;
    }

    static final class PriceHighlight {
        private final int price;
        private final Color color;

        public PriceHighlight(int price, Color color) {
            this.price = price;
            this.color = color;
        }

        public int getPrice() {
            return this.price;
        }

        public Color getColor() {
            return this.color;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof PriceHighlight)) {
                return false;
            }
            PriceHighlight other = (PriceHighlight)o;
            if (this.getPrice() != other.getPrice()) {
                return false;
            }
            Color this$color = this.getColor();
            Color other$color = other.getColor();
            return !(this$color == null ? other$color != null : !((Object)this$color).equals(other$color));
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getPrice();
            Color $color = this.getColor();
            result = result * 59 + ($color == null ? 43 : ((Object)$color).hashCode());
            return result;
        }

        public String toString() {
            return "GroundItemsPlugin.PriceHighlight(price=" + this.getPrice() + ", color=" + this.getColor() + ")";
        }
    }
}

