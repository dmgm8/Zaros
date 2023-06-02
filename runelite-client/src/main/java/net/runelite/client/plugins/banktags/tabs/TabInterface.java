/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.Lists
 *  com.google.common.util.concurrent.Runnables
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.Point
 *  net.runelite.api.ScriptEvent
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.events.ScriptCallbackEvent
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.banktags.tabs;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Point;
import net.runelite.api.ScriptEvent;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.chatbox.ChatboxItemSearch;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.bank.BankSearch;
import net.runelite.client.plugins.banktags.BankTagsConfig;
import net.runelite.client.plugins.banktags.TagManager;
import net.runelite.client.plugins.banktags.tabs.TabManager;
import net.runelite.client.plugins.banktags.tabs.TabSprites;
import net.runelite.client.plugins.banktags.tabs.TagTab;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

@Singleton
public class TabInterface {
    public static final IntPredicate FILTERED_CHARS = c -> "</>:".indexOf(c) == -1;
    private static final Color HILIGHT_COLOR = JagexColors.MENU_TARGET;
    private static final String SCROLL_UP = "Scroll up";
    private static final String SCROLL_DOWN = "Scroll down";
    private static final String NEW_TAB = "New tag tab";
    private static final String REMOVE_TAB = "Delete tag tab";
    private static final String EXPORT_TAB = "Export tag tab";
    private static final String IMPORT_TAB = "Import tag tab";
    private static final String VIEW_TAB = "View tag tab";
    private static final String RENAME_TAB = "Rename tag tab";
    private static final String CHANGE_ICON = "Change icon";
    private static final String REMOVE_TAG = "Remove-tag";
    private static final String TAG_GEAR = "Tag-equipment";
    private static final String TAG_INVENTORY = "Tag-inventory";
    private static final String TAB_MENU_KEY = "tagtabs";
    private static final String OPEN_TAB_MENU = "View tag tabs";
    private static final String SHOW_WORN = "Show worn items";
    private static final String SHOW_SETTINGS = "Show menu";
    private static final String SHOW_TUTORIAL = "Show tutorial";
    private static final int TAB_HEIGHT = 40;
    private static final int TAB_WIDTH = 39;
    private static final int BUTTON_HEIGHT = 20;
    private static final int MARGIN = 1;
    private static final int SCROLL_TICK = 500;
    private static final int INCINERATOR_WIDTH = 48;
    private static final int INCINERATOR_HEIGHT = 39;
    private static final int BANK_ITEM_WIDTH = 36;
    private static final int BANK_ITEM_HEIGHT = 32;
    private static final int BANK_ITEM_X_PADDING = 12;
    private static final int BANK_ITEM_Y_PADDING = 4;
    private static final int BANK_ITEMS_PER_ROW = 8;
    private static final int BANK_ITEM_START_X = 51;
    private static final int BANK_ITEM_START_Y = 0;
    private final Client client;
    private final ClientThread clientThread;
    private final ItemManager itemManager;
    private final TagManager tagManager;
    private final TabManager tabManager;
    private final ChatboxPanelManager chatboxPanelManager;
    private final BankTagsConfig config;
    private final Notifier notifier;
    private final BankSearch bankSearch;
    private final ChatboxItemSearch searchProvider;
    private final Rectangle bounds = new Rectangle();
    private final Rectangle canvasBounds = new Rectangle();
    private TagTab activeTab;
    private boolean tagTabActive;
    private int maxTabs;
    private int currentTabIndex;
    private Instant startScroll = Instant.now();
    private Widget upButton;
    private Widget downButton;
    private Widget newTab;
    private Widget parent;

    @Inject
    private TabInterface(Client client, ClientThread clientThread, ItemManager itemManager, TagManager tagManager, TabManager tabManager, ChatboxPanelManager chatboxPanelManager, BankTagsConfig config, Notifier notifier, BankSearch bankSearch, ChatboxItemSearch searchProvider) {
        this.client = client;
        this.clientThread = clientThread;
        this.itemManager = itemManager;
        this.tagManager = tagManager;
        this.tabManager = tabManager;
        this.chatboxPanelManager = chatboxPanelManager;
        this.config = config;
        this.notifier = notifier;
        this.bankSearch = bankSearch;
        this.searchProvider = searchProvider;
    }

    public boolean isActive() {
        return this.activeTab != null;
    }

    public void init() {
        if (this.isHidden()) {
            return;
        }
        this.currentTabIndex = this.config.position();
        this.parent = this.client.getWidget(WidgetInfo.BANK_CONTENT_CONTAINER);
        this.updateBounds();
        this.upButton = this.createGraphic("", TabSprites.UP_ARROW.getSpriteId(), -1, 39, 20, this.bounds.x, 0, true);
        this.upButton.setAction(1, SCROLL_UP);
        int clickmask = this.upButton.getClickMask();
        this.upButton.setClickMask(clickmask |= 0x100000);
        this.upButton.setOnOpListener(new Object[]{event -> this.scrollTab(-1)});
        this.downButton = this.createGraphic("", TabSprites.DOWN_ARROW.getSpriteId(), -1, 39, 20, this.bounds.x, 0, true);
        this.downButton.setAction(1, SCROLL_DOWN);
        clickmask = this.downButton.getClickMask();
        this.downButton.setClickMask(clickmask |= 0x100000);
        this.downButton.setOnOpListener(new Object[]{event -> this.scrollTab(1)});
        this.newTab = this.createGraphic("", TabSprites.NEW_TAB.getSpriteId(), -1, 39, 39, this.bounds.x, 0, true);
        this.newTab.setAction(1, NEW_TAB);
        this.newTab.setAction(2, IMPORT_TAB);
        this.newTab.setAction(3, OPEN_TAB_MENU);
        this.newTab.setOnOpListener(new Object[]{this::handleNewTab});
        this.tabManager.clear();
        this.tabManager.getAllTabs().forEach(this::loadTab);
        this.activateTab(null);
        this.scrollTab(0);
        if (this.config.rememberTab() && !Strings.isNullOrEmpty((String)this.config.tab())) {
            this.client.setVarbit(4150, 0);
            this.openTag(this.config.tab());
        }
        Widget equipmentButton = this.client.getWidget(WidgetInfo.BANK_EQUIPMENT_BUTTON);
        Widget titleBar = this.client.getWidget(WidgetInfo.BANK_TITLE_BAR);
        if (equipmentButton == null || titleBar == null || titleBar.getOriginalX() > 0) {
            return;
        }
        equipmentButton.setOriginalX(6);
        equipmentButton.setOriginalY(4);
        equipmentButton.revalidate();
        Widget bankItemCountTop = this.client.getWidget(WidgetInfo.BANK_ITEM_COUNT_TOP);
        if (bankItemCountTop == null) {
            return;
        }
        int equipmentButtonTotalWidth = equipmentButton.getWidth() + equipmentButton.getOriginalX() - bankItemCountTop.getOriginalX();
        for (int child = WidgetInfo.BANK_ITEM_COUNT_TOP.getChildId(); child <= WidgetInfo.BANK_ITEM_COUNT_BOTTOM.getChildId(); ++child) {
            Widget widget = this.client.getWidget(12, child);
            if (widget == null) {
                return;
            }
            widget.setOriginalX(widget.getOriginalX() + equipmentButtonTotalWidth);
            widget.revalidate();
        }
        titleBar.setOriginalX(equipmentButton.getWidth() / 2);
        titleBar.setOriginalWidth(titleBar.getWidth() - equipmentButton.getWidth());
        titleBar.revalidate();
        Widget groupStorageButton = this.client.getWidget(WidgetInfo.BANK_GROUP_STORAGE_BUTTON);
        if (groupStorageButton == null) {
            return;
        }
        groupStorageButton.setOriginalX(groupStorageButton.getOriginalX() + equipmentButtonTotalWidth);
        groupStorageButton.revalidate();
    }

    private void handleDeposit(MenuOptionClicked event, Boolean inventory) {
        ItemContainer container = this.client.getItemContainer(inventory != false ? InventoryID.INVENTORY : InventoryID.EQUIPMENT);
        if (container == null) {
            return;
        }
        List items = Arrays.stream(container.getItems()).filter(Objects::nonNull).map(Item::getId).filter(id -> id != -1).collect(Collectors.toList());
        if (!Strings.isNullOrEmpty((String)event.getMenuTarget())) {
            if (this.activeTab != null && Text.removeTags(event.getMenuTarget()).equals(this.activeTab.getTag())) {
                for (Integer item : items) {
                    this.tagManager.addTag(item, this.activeTab.getTag(), false);
                }
                this.openTag(this.activeTab.getTag());
            }
            return;
        }
        this.chatboxPanelManager.openTextInput((inventory != false ? "Inventory" : "Equipment") + " tags:").addCharValidator(FILTERED_CHARS).onDone(newTags -> this.clientThread.invoke(() -> {
            List<String> tags = Text.fromCSV(newTags.toLowerCase());
            for (Integer item : items) {
                this.tagManager.addTags(item, tags, false);
            }
            this.updateTabIfActive(tags);
        })).build();
    }

    private void handleNewTab(ScriptEvent event) {
        switch (event.getOp()) {
            case 2: {
                this.chatboxPanelManager.openTextInput("Tag name").addCharValidator(FILTERED_CHARS).onDone(tagName -> this.clientThread.invoke(() -> {
                    if (!Strings.isNullOrEmpty((String)tagName)) {
                        this.loadTab((String)tagName);
                        this.tabManager.save();
                        this.scrollTab(0);
                    }
                })).build();
                break;
            }
            case 3: {
                try {
                    String dataString = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString().trim();
                    Iterator<String> dataIter = Text.fromCSV(dataString).iterator();
                    String name = dataIter.next();
                    StringBuilder sb = new StringBuilder();
                    for (char c : name.toCharArray()) {
                        if (!FILTERED_CHARS.test(c)) continue;
                        sb.append(c);
                    }
                    if (sb.length() == 0) {
                        this.notifier.notify("Failed to import tag tab from clipboard, invalid format.");
                        return;
                    }
                    name = sb.toString();
                    String icon = dataIter.next();
                    this.tabManager.setIcon(name, icon);
                    while (dataIter.hasNext()) {
                        int itemId;
                        this.tagManager.addTag(itemId, name, (itemId = Integer.parseInt(dataIter.next())) < 0);
                    }
                    this.loadTab(name);
                    this.tabManager.save();
                    this.scrollTab(0);
                    if (this.activeTab != null && name.equals(this.activeTab.getTag())) {
                        this.openTag(this.activeTab.getTag());
                    }
                    this.notifier.notify("Tag tab " + name + " has been imported from your clipboard!");
                }
                catch (UnsupportedFlavorException | IOException | NumberFormatException | NoSuchElementException ex) {
                    this.notifier.notify("Failed to import tag tab from clipboard, invalid format.");
                }
                break;
            }
            case 4: {
                this.client.setVarbit(4150, 0);
                this.openTag(TAB_MENU_KEY);
            }
        }
    }

    private void handleTagTab(ScriptEvent event) {
        switch (event.getOp()) {
            case 2: {
                this.client.setVarbit(4150, 0);
                Widget clicked = event.getSource();
                TagTab tab = this.tabManager.find(Text.removeTags(clicked.getName()));
                if (tab.equals(this.activeTab)) {
                    this.activateTab(null);
                    this.bankSearch.reset(true);
                } else {
                    this.openTag(Text.removeTags(clicked.getName()));
                }
                this.client.playSoundEffect(2266);
                break;
            }
            case 3: {
                String tag = Text.removeTags(event.getOpbase());
                this.searchProvider.tooltipText("Change icon (" + tag + ")").onItemSelected(itemId -> {
                    TagTab iconToSet = this.tabManager.find(tag);
                    if (iconToSet != null) {
                        iconToSet.setIconItemId((int)itemId);
                        iconToSet.getIcon().setItemId(itemId.intValue());
                        iconToSet.getMenu().setItemId(itemId.intValue());
                        this.tabManager.setIcon(iconToSet.getTag(), itemId + "");
                    }
                }).build();
                break;
            }
            case 4: {
                String target = Text.standardize(event.getOpbase());
                this.chatboxPanelManager.openTextMenuInput("Delete " + target).option("1. Tab and tag from all items", () -> this.clientThread.invoke(() -> {
                    this.tagManager.removeTag(target);
                    this.deleteTab(target);
                })).option("2. Only tab", () -> this.clientThread.invoke(() -> this.deleteTab(target))).option("3. Cancel", Runnables.doNothing()).build();
                break;
            }
            case 5: {
                ArrayList<String> data = new ArrayList<String>();
                TagTab tagTab = this.tabManager.find(Text.removeTags(event.getOpbase()));
                data.add(tagTab.getTag());
                data.add(String.valueOf(tagTab.getIconItemId()));
                for (Integer item : this.tagManager.getItemsForTag(tagTab.getTag())) {
                    data.add(String.valueOf(item));
                }
                StringSelection stringSelection = new StringSelection(Text.toCSV(data));
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                this.notifier.notify("Tag tab " + tagTab.getTag() + " has been copied to your clipboard!");
                break;
            }
            case 6: {
                String renameTarget = Text.standardize(event.getOpbase());
                this.renameTab(renameTarget);
            }
        }
    }

    public void destroy() {
        this.activeTab = null;
        this.currentTabIndex = 0;
        this.maxTabs = 0;
        this.parent = null;
        if (this.upButton != null) {
            this.upButton.setHidden(true);
            this.downButton.setHidden(true);
            this.newTab.setHidden(true);
        }
        this.tabManager.clear();
    }

    public void update() {
        if (this.isHidden()) {
            this.parent = null;
            this.saveTab();
            return;
        }
        if (this.parent.isSelfHidden()) {
            return;
        }
        this.updateBounds();
        this.scrollTab(0);
    }

    private void saveTab() {
        if (this.currentTabIndex != this.config.position()) {
            this.config.position(this.currentTabIndex);
        }
        if (this.config.rememberTab()) {
            if (this.activeTab == null && !Strings.isNullOrEmpty((String)this.config.tab())) {
                this.config.tab("");
            } else if (this.activeTab != null && !this.activeTab.getTag().equals(this.config.tab())) {
                this.config.tab(this.activeTab.getTag());
            }
        } else if (!Strings.isNullOrEmpty((String)this.config.tab())) {
            this.config.tab("");
        }
    }

    private void setTabMenuVisible(boolean visible) {
        for (TagTab t : this.tabManager.getTabs()) {
            t.getMenu().setHidden(!visible);
        }
    }

    private boolean isTabMenuActive() {
        return this.tagTabActive;
    }

    public void handleScriptEvent(ScriptCallbackEvent event) {
        String eventName = event.getEventName();
        int[] intStack = this.client.getIntStack();
        int intStackSize = this.client.getIntStackSize();
        switch (eventName) {
            case "skipBankLayout": {
                if (!this.isTabMenuActive()) {
                    this.setTabMenuVisible(false);
                    return;
                }
                this.setTabMenuVisible(true);
                intStack[intStackSize - 1] = 1;
                break;
            }
            case "beforeBankLayout": {
                this.setTabMenuVisible(false);
            }
        }
    }

    public void handleWheel(MouseWheelEvent event) {
        if (this.parent == null || !this.canvasBounds.contains(event.getPoint())) {
            return;
        }
        event.consume();
        this.clientThread.invoke(() -> {
            if (this.isHidden()) {
                return;
            }
            this.scrollTab(event.getWheelRotation());
        });
    }

    public void handleAdd(MenuEntryAdded event) {
        if (this.isHidden()) {
            return;
        }
        if (this.activeTab != null && event.getActionParam1() == WidgetInfo.BANK_ITEM_CONTAINER.getId() && event.getOption().equals("Examine")) {
            this.createMenuEntry(event, "Remove-tag (" + this.activeTab.getTag() + ")", event.getTarget());
        } else if (event.getActionParam1() == WidgetInfo.BANK_DEPOSIT_INVENTORY.getId() && event.getOption().equals("Deposit inventory")) {
            this.createMenuEntry(event, TAG_INVENTORY, event.getTarget());
            if (this.activeTab != null) {
                this.createMenuEntry(event, TAG_INVENTORY, ColorUtil.wrapWithColorTag(this.activeTab.getTag(), HILIGHT_COLOR));
            }
        } else if (event.getActionParam1() == WidgetInfo.BANK_DEPOSIT_EQUIPMENT.getId() && event.getOption().equals("Deposit worn items")) {
            this.createMenuEntry(event, TAG_GEAR, event.getTarget());
            if (this.activeTab != null) {
                this.createMenuEntry(event, TAG_GEAR, ColorUtil.wrapWithColorTag(this.activeTab.getTag(), HILIGHT_COLOR));
            }
        }
    }

    public void handleClick(MenuOptionClicked event) {
        if (this.isHidden()) {
            return;
        }
        if (this.chatboxPanelManager.getCurrentInput() != null && event.getMenuAction() != MenuAction.CANCEL && !event.getMenuOption().equals(SCROLL_UP) && !event.getMenuOption().equals(SCROLL_DOWN)) {
            this.chatboxPanelManager.close();
        }
        if (this.activeTab != null && (event.getMenuOption().startsWith("View tab") || event.getMenuOption().equals("View all items"))) {
            this.activateTab(null);
        } else if (this.activeTab != null && event.getParam1() == WidgetInfo.BANK_ITEM_CONTAINER.getId() && event.getMenuAction() == MenuAction.RUNELITE && event.getMenuOption().startsWith(REMOVE_TAG)) {
            event.consume();
            ItemComposition item = this.getItem(event.getParam0());
            int itemId = item.getId();
            this.tagManager.removeTag(itemId, this.activeTab.getTag());
            this.bankSearch.layoutBank();
        } else if (event.getMenuAction() == MenuAction.RUNELITE && (event.getParam1() == WidgetInfo.BANK_DEPOSIT_INVENTORY.getId() && event.getMenuOption().equals(TAG_INVENTORY) || event.getParam1() == WidgetInfo.BANK_DEPOSIT_EQUIPMENT.getId() && event.getMenuOption().equals(TAG_GEAR))) {
            this.handleDeposit(event, event.getParam1() == WidgetInfo.BANK_DEPOSIT_INVENTORY.getId());
        } else if (this.activeTab != null && (event.getParam1() == WidgetInfo.BANK_EQUIPMENT_BUTTON.getId() && event.getMenuOption().equals(SHOW_WORN) || event.getParam1() == WidgetInfo.BANK_SETTINGS_BUTTON.getId() && event.getMenuOption().equals(SHOW_SETTINGS) || event.getParam1() == WidgetInfo.BANK_TUTORIAL_BUTTON.getId() && event.getMenuOption().equals(SHOW_TUTORIAL))) {
            this.saveTab();
        }
    }

    public void handleSearch() {
        if (this.activeTab != null) {
            this.activateTab(null);
            this.client.setVarcStrValue(359, "");
            this.client.setVarcIntValue(5, 0);
        }
    }

    public void updateTabIfActive(Collection<String> tags) {
        if (this.activeTab != null && tags.contains(this.activeTab.getTag())) {
            this.openTag(this.activeTab.getTag());
        }
    }

    public void handleDrag(boolean isDragging, boolean shiftDown) {
        MenuEntry[] entries;
        if (this.isHidden()) {
            return;
        }
        Widget draggedOn = this.client.getDraggedOnWidget();
        Widget draggedWidget = this.client.getDraggedWidget();
        if (draggedWidget.getId() == WidgetInfo.BANK_ITEM_CONTAINER.getId() && this.isActive() && this.config.preventTagTabDrags()) {
            this.client.setDraggedOnWidget(null);
        }
        if (!isDragging || draggedOn == null) {
            return;
        }
        if (this.client.getMouseCurrentButton() == 0) {
            if (!this.isTabMenuActive() && draggedWidget.getItemId() > 0 && draggedWidget.getId() != this.parent.getId()) {
                if (draggedOn.getId() == this.parent.getId()) {
                    this.tagManager.addTag(draggedWidget.getItemId(), draggedOn.getName(), shiftDown);
                    this.updateTabIfActive(Lists.newArrayList((Object[])new String[]{Text.standardize(draggedOn.getName())}));
                }
            } else if (this.isTabMenuActive() && draggedWidget.getId() == draggedOn.getId() && draggedOn.getId() != this.parent.getId() || this.parent.getId() == draggedOn.getId() && this.parent.getId() == draggedWidget.getId()) {
                this.moveTagTab(draggedWidget, draggedOn);
            }
        } else if (draggedWidget.getItemId() > 0 && (entries = this.client.getMenuEntries()).length > 0) {
            MenuEntry entry = entries[entries.length - 1];
            if (draggedWidget.getItemId() > 0 && entry.getOption().equals(VIEW_TAB) && draggedOn.getId() != draggedWidget.getId()) {
                entry.setOption("tag:" + Text.removeTags(entry.getTarget()) + (shiftDown ? "*" : ""));
                entry.setTarget(draggedWidget.getName());
            }
            if (entry.getOption().equals(SCROLL_UP)) {
                this.scrollTick(-1);
            } else if (entry.getOption().equals(SCROLL_DOWN)) {
                this.scrollTick(1);
            }
        }
    }

    private void moveTagTab(Widget source, Widget dest) {
        if (Strings.isNullOrEmpty((String)dest.getName())) {
            return;
        }
        if (this.client.getVarbitValue(3959) == 0) {
            this.tabManager.swap(source.getName(), dest.getName());
        } else {
            this.tabManager.insert(source.getName(), dest.getName());
        }
        this.tabManager.save();
        this.updateTabs();
    }

    private boolean isHidden() {
        Widget widget = this.client.getWidget(WidgetInfo.BANK_CONTAINER);
        return !this.config.tabs() || widget == null || widget.isHidden();
    }

    private void addTabActions(Widget w) {
        w.setAction(1, VIEW_TAB);
        w.setAction(2, CHANGE_ICON);
        w.setAction(3, REMOVE_TAB);
        w.setAction(4, EXPORT_TAB);
        w.setAction(5, RENAME_TAB);
        w.setOnOpListener(new Object[]{this::handleTagTab});
    }

    private void addTabOptions(Widget w) {
        int clickmask = w.getClickMask();
        clickmask |= 0x100000;
        w.setClickMask(clickmask |= 0x20000);
        w.setDragDeadTime(5);
        w.setDragDeadZone(5);
        w.setItemQuantity(10000);
        w.setItemQuantityMode(0);
    }

    private void loadTab(String tag) {
        TagTab tagTab = this.tabManager.load(tag);
        if (tagTab.getBackground() == null) {
            Widget btn = this.createGraphic(ColorUtil.wrapWithColorTag(tagTab.getTag(), HILIGHT_COLOR), TabSprites.TAB_BACKGROUND.getSpriteId(), -1, 39, 40, this.bounds.x, 1, true);
            this.addTabActions(btn);
            tagTab.setBackground(btn);
        }
        if (tagTab.getIcon() == null) {
            Widget icon = this.createGraphic(ColorUtil.wrapWithColorTag(tagTab.getTag(), HILIGHT_COLOR), -1, tagTab.getIconItemId(), 36, 32, this.bounds.x + 3, 1, false);
            this.addTabOptions(icon);
            tagTab.setIcon(icon);
        }
        if (tagTab.getMenu() == null) {
            Widget menu = this.createGraphic(this.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER), ColorUtil.wrapWithColorTag(tagTab.getTag(), HILIGHT_COLOR), -1, tagTab.getIconItemId(), 36, 32, 51, 0, true);
            this.addTabActions(menu);
            this.addTabOptions(menu);
            if (this.activeTab != null && this.activeTab.getTag().equals(TAB_MENU_KEY)) {
                menu.setHidden(false);
            } else {
                menu.setHidden(true);
            }
            tagTab.setMenu(menu);
        }
        this.tabManager.add(tagTab);
    }

    private void deleteTab(String tag) {
        if (this.activeTab != null && this.activeTab.getTag().equals(tag)) {
            this.activateTab(null);
            this.bankSearch.reset(true);
        }
        this.tabManager.remove(tag);
        this.tabManager.save();
        this.updateBounds();
        this.scrollTab(0);
    }

    private void renameTab(String oldTag) {
        this.chatboxPanelManager.openTextInput("Enter new tag name for tag \"" + oldTag + "\":").addCharValidator(FILTERED_CHARS).onDone(newTag -> this.clientThread.invoke(() -> {
            if (!Strings.isNullOrEmpty((String)newTag) && !newTag.equalsIgnoreCase(oldTag)) {
                if (this.tabManager.find((String)newTag) == null) {
                    TagTab tagTab = this.tabManager.find(oldTag);
                    tagTab.setTag((String)newTag);
                    String coloredName = ColorUtil.wrapWithColorTag(newTag, HILIGHT_COLOR);
                    tagTab.getIcon().setName(coloredName);
                    tagTab.getBackground().setName(coloredName);
                    tagTab.getMenu().setName(coloredName);
                    this.tabManager.removeIcon(oldTag);
                    this.tabManager.setIcon((String)newTag, tagTab.getIconItemId() + "");
                    this.tabManager.save();
                    this.tagManager.renameTag(oldTag, (String)newTag);
                    if (this.activeTab != null && this.activeTab.equals(tagTab)) {
                        this.openTag((String)newTag);
                    }
                } else {
                    this.chatboxPanelManager.openTextMenuInput("The specified bank tag already exists.").option("1. Merge into existing tag \"" + newTag + "\".", () -> this.clientThread.invoke(() -> {
                        this.tagManager.renameTag(oldTag, (String)newTag);
                        String activeTag = this.activeTab != null ? this.activeTab.getTag() : "";
                        this.deleteTab(oldTag);
                        if (activeTag.equals(oldTag)) {
                            this.openTag((String)newTag);
                        }
                    })).option("2. Choose a different name.", () -> this.clientThread.invoke(() -> this.renameTab(oldTag))).build();
                }
            }
        })).build();
    }

    private void scrollTick(int direction) {
        if (this.startScroll.until(Instant.now(), ChronoUnit.MILLIS) >= 500L) {
            this.startScroll = Instant.now();
            this.scrollTab(direction);
        }
    }

    private void scrollTab(int direction) {
        this.maxTabs = (this.bounds.height - 40 - 2) / 40;
        while (this.bounds.y + this.maxTabs * 40 + 1 * this.maxTabs + 40 + 1 > this.bounds.y + this.bounds.height) {
            --this.maxTabs;
        }
        int proposedIndex = this.currentTabIndex + direction;
        int numTabs = this.tabManager.size();
        if (proposedIndex >= numTabs || proposedIndex < 0) {
            this.currentTabIndex = 0;
        } else if (numTabs - proposedIndex >= this.maxTabs) {
            this.currentTabIndex = proposedIndex;
        } else if (this.maxTabs < numTabs && numTabs - proposedIndex < this.maxTabs) {
            this.currentTabIndex = proposedIndex;
            this.scrollTab(-1);
        }
        this.updateTabs();
    }

    private void activateTab(TagTab tagTab) {
        Widget tab;
        if (this.activeTab != null && this.activeTab.equals(tagTab)) {
            return;
        }
        if (this.activeTab != null) {
            tab = this.activeTab.getBackground();
            tab.setSpriteId(TabSprites.TAB_BACKGROUND.getSpriteId());
            tab.revalidate();
            this.activeTab = null;
        }
        if (tagTab != null) {
            tab = tagTab.getBackground();
            tab.setSpriteId(TabSprites.TAB_BACKGROUND_ACTIVE.getSpriteId());
            tab.revalidate();
            this.activeTab = tagTab;
        }
        this.tagTabActive = false;
    }

    private void updateBounds() {
        Widget itemContainer = this.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        if (itemContainer == null) {
            return;
        }
        int height = itemContainer.getHeight();
        if (itemContainer.getRelativeY() == 0) {
            height -= 41;
        }
        this.bounds.setSize(41, height);
        this.bounds.setLocation(1, 41);
        Widget incinerator = this.client.getWidget(WidgetInfo.BANK_INCINERATOR);
        if (incinerator != null && !incinerator.isHidden()) {
            incinerator.setOriginalHeight(39);
            incinerator.setOriginalWidth(48);
            incinerator.setOriginalY(39);
            Widget child = incinerator.getChild(0);
            child.setOriginalHeight(39);
            child.setOriginalWidth(48);
            child.setWidthMode(0);
            child.setHeightMode(0);
            child.setType(5);
            child.setSpriteId(TabSprites.INCINERATOR.getSpriteId());
            incinerator.revalidate();
            this.bounds.setSize(41, height - incinerator.getHeight());
        }
        if (this.upButton != null) {
            Point p = this.upButton.getCanvasLocation();
            this.canvasBounds.setBounds(p.getX(), p.getY() + 20, this.bounds.width, this.maxTabs * 40 + this.maxTabs * 1);
        }
    }

    private void updateTabs() {
        int y = this.bounds.y + 1 + 20;
        if (this.maxTabs >= this.tabManager.size()) {
            this.currentTabIndex = 0;
        } else {
            y -= this.currentTabIndex * 40 + this.currentTabIndex * 1;
        }
        int itemX = 51;
        int itemY = 0;
        int rowIndex = 0;
        for (TagTab tab : this.tabManager.getTabs()) {
            this.updateWidget(tab.getBackground(), y);
            this.updateWidget(tab.getIcon(), y + 4);
            tab.getIcon().setHidden(tab.getBackground().isHidden());
            if (this.client.getDraggedWidget() == tab.getIcon()) {
                tab.getIcon().setHidden(false);
            }
            y += 41;
            Widget item = tab.getMenu();
            item.setOriginalX(itemX);
            item.setOriginalY(itemY);
            item.revalidate();
            if (++rowIndex == 8) {
                itemX = 51;
                itemY += 36;
                rowIndex = 0;
                continue;
            }
            itemX += 48;
        }
        boolean hidden = this.tabManager.size() <= 0;
        this.upButton.setHidden(hidden);
        this.upButton.setOriginalY(this.bounds.y);
        this.upButton.revalidate();
        this.downButton.setHidden(hidden);
        this.downButton.setOriginalY(this.bounds.y + this.maxTabs * 40 + 1 * this.maxTabs + 20 + 1);
        this.downButton.revalidate();
    }

    private Widget createGraphic(Widget container, String name, int spriteId, int itemId, int width, int height, int x, int y, boolean hasListener) {
        Widget widget = container.createChild(-1, 5);
        widget.setOriginalWidth(width);
        widget.setOriginalHeight(height);
        widget.setOriginalX(x);
        widget.setOriginalY(y);
        widget.setSpriteId(spriteId);
        if (itemId > -1) {
            widget.setItemId(itemId);
            widget.setItemQuantity(-1);
            widget.setBorderType(1);
        }
        if (hasListener) {
            widget.setOnOpListener(new Object[]{11003});
            widget.setHasListener(true);
        }
        widget.setName(name);
        widget.revalidate();
        return widget;
    }

    private Widget createGraphic(String name, int spriteId, int itemId, int width, int height, int x, int y, boolean hasListener) {
        return this.createGraphic(this.parent, name, spriteId, itemId, width, height, x, y, hasListener);
    }

    private void updateWidget(Widget t, int y) {
        t.setOriginalY(y);
        t.setHidden(y < this.bounds.y + 20 + 1 || y > this.bounds.y + this.bounds.height - 40 - 1 - 20);
        t.revalidate();
    }

    private ItemComposition getItem(int idx) {
        ItemContainer bankContainer = this.client.getItemContainer(InventoryID.BANK);
        Item item = bankContainer.getItem(idx);
        return this.itemManager.getItemComposition(item.getId());
    }

    private void openTag(String tag) {
        this.activateTab(this.tabManager.find(tag));
        this.tagTabActive = TAB_MENU_KEY.equals(tag);
        this.bankSearch.reset(true);
        Widget searchButtonBackground = this.client.getWidget(WidgetInfo.BANK_SEARCH_BUTTON_BACKGROUND);
        searchButtonBackground.setOnTimerListener((Object[])null);
        searchButtonBackground.setSpriteId(170);
    }

    private void createMenuEntry(MenuEntryAdded event, String option, String target) {
        this.client.createMenuEntry(-1).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setTarget(target).setOption(option).setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier());
    }

    public TagTab getActiveTab() {
        return this.activeTab;
    }

    public boolean isTagTabActive() {
        return this.tagTabActive;
    }

    public Widget getUpButton() {
        return this.upButton;
    }

    public Widget getDownButton() {
        return this.downButton;
    }

    public Widget getNewTab() {
        return this.newTab;
    }

    public Widget getParent() {
        return this.parent;
    }
}

