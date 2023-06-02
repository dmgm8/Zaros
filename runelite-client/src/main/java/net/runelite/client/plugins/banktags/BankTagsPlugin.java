/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.primitives.Shorts
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.events.DraggingWidgetChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.GrandExchangeSearched
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.events.ScriptCallbackEvent
 *  net.runelite.api.events.ScriptPostFired
 *  net.runelite.api.events.ScriptPreFired
 *  net.runelite.api.events.WidgetLoaded
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.banktags;

import com.google.common.collect.Lists;
import com.google.common.primitives.Shorts;
import com.google.inject.Provides;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.DraggingWidgetChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GrandExchangeSearched;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.input.MouseWheelListener;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.banktags.BankTagsConfig;
import net.runelite.client.plugins.banktags.TagManager;
import net.runelite.client.plugins.banktags.tabs.TabInterface;
import net.runelite.client.plugins.banktags.tabs.TabSprites;
import net.runelite.client.plugins.banktags.tabs.TagTab;
import net.runelite.client.util.Text;

@PluginDescriptor(name="Bank Tags", description="Enable tagging of bank items and searching of bank tags", tags={"searching", "tagging"})
public class BankTagsPlugin
extends Plugin
implements MouseWheelListener {
    public static final String CONFIG_GROUP = "banktags";
    public static final String TAG_SEARCH = "tag:";
    private static final String EDIT_TAGS_MENU_OPTION = "Edit-tags";
    public static final String ICON_SEARCH = "icon_";
    public static final String TAG_TABS_CONFIG = "tagtabs";
    public static final String VAR_TAG_SUFFIX = "*";
    private static final int ITEMS_PER_ROW = 8;
    private static final int ITEM_VERTICAL_SPACING = 36;
    private static final int ITEM_HORIZONTAL_SPACING = 48;
    private static final int ITEM_ROW_START = 51;
    private static final int ITEM_CONTAINER_BOTTOM_PADDING = 4;
    private static final int MAX_RESULT_COUNT = 250;
    private static final String SEARCH_BANK_INPUT_TEXT = "Show items whose names or tags contain the following text:<br>(To show only tagged items, start your search with 'tag:')";
    private static final String SEARCH_BANK_INPUT_TEXT_FOUND = "Show items whose names or tags contain the following text: (%d found)<br>(To show only tagged items, start your search with 'tag:')";
    @Inject
    private ItemManager itemManager;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ChatboxPanelManager chatboxPanelManager;
    @Inject
    private MouseManager mouseManager;
    @Inject
    private BankTagsConfig config;
    @Inject
    private TagManager tagManager;
    @Inject
    private TabInterface tabInterface;
    @Inject
    private SpriteManager spriteManager;
    @Inject
    private ConfigManager configManager;

    @Provides
    BankTagsConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(BankTagsConfig.class);
    }

    @Override
    public void resetConfiguration() {
        ArrayList extraKeys = Lists.newArrayList((Object[])new String[]{"banktags.item_", "banktags.icon_", "banktags.tagtabs"});
        for (String prefix : extraKeys) {
            List<String> keys = this.configManager.getConfigurationKeys(prefix);
            for (String key : keys) {
                String[] str = key.split("\\.", 2);
                if (str.length != 2) continue;
                this.configManager.unsetConfiguration(str[0], str[1]);
            }
        }
        this.clientThread.invokeLater(() -> {
            this.tabInterface.destroy();
            this.tabInterface.init();
        });
    }

    @Override
    public void startUp() {
        this.cleanConfig();
        this.mouseManager.registerMouseWheelListener(this);
        this.clientThread.invokeLater(this.tabInterface::init);
        this.spriteManager.addSpriteOverrides(TabSprites.values());
    }

    @Deprecated
    private void cleanConfig() {
        this.removeInvalidTags(TAG_TABS_CONFIG);
        List<String> tags = this.configManager.getConfigurationKeys("banktags.item_");
        tags.forEach(s -> {
            String[] split = s.split("\\.", 2);
            this.removeInvalidTags(split[1]);
        });
        List<String> icons = this.configManager.getConfigurationKeys("banktags.icon_");
        icons.forEach(s -> {
            String replaced;
            String[] split = s.split("\\.", 2);
            if (!split[1].equals(replaced = split[1].replaceAll("[<>/]", ""))) {
                String value = this.configManager.getConfiguration(CONFIG_GROUP, split[1]);
                this.configManager.unsetConfiguration(CONFIG_GROUP, split[1]);
                if (replaced.length() > ICON_SEARCH.length()) {
                    this.configManager.setConfiguration(CONFIG_GROUP, replaced, value);
                }
            }
        });
    }

    @Deprecated
    private void removeInvalidTags(String key) {
        String value = this.configManager.getConfiguration(CONFIG_GROUP, key);
        if (value == null) {
            return;
        }
        String replaced = value.replaceAll("[<>:/]", "");
        if (!value.equals(replaced)) {
            if ((replaced = Text.toCSV(Text.fromCSV(replaced))).isEmpty()) {
                this.configManager.unsetConfiguration(CONFIG_GROUP, key);
            } else {
                this.configManager.setConfiguration(CONFIG_GROUP, key, replaced);
            }
        }
    }

    @Override
    public void shutDown() {
        this.mouseManager.unregisterMouseWheelListener(this);
        this.clientThread.invokeLater(this.tabInterface::destroy);
        this.spriteManager.removeSpriteOverrides(TabSprites.values());
    }

    @Subscribe
    public void onGrandExchangeSearched(GrandExchangeSearched event) {
        String input = this.client.getVarcStrValue(359);
        if (!input.startsWith(TAG_SEARCH)) {
            return;
        }
        event.consume();
        String tag = input.substring(TAG_SEARCH.length()).trim();
        Set ids = this.tagManager.getItemsForTag(tag).stream().mapToInt(Math::abs).mapToObj(ItemVariationMapping::getVariations).flatMap(Collection::stream).distinct().filter(i -> this.itemManager.getItemComposition((int)i).isTradeable()).limit(250L).collect(Collectors.toCollection(TreeSet::new));
        this.client.setGeSearchResultIndex(0);
        this.client.setGeSearchResultCount(ids.size());
        this.client.setGeSearchResultIds(Shorts.toArray((Collection)ids));
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent event) {
        String eventName = event.getEventName();
        int[] intStack = this.client.getIntStack();
        String[] stringStack = this.client.getStringStack();
        int intStackSize = this.client.getIntStackSize();
        int stringStackSize = this.client.getStringStackSize();
        this.tabInterface.handleScriptEvent(event);
        switch (eventName) {
            case "setSearchBankInputText": {
                stringStack[stringStackSize - 1] = SEARCH_BANK_INPUT_TEXT;
                break;
            }
            case "setSearchBankInputTextFound": {
                int matches = intStack[intStackSize - 1];
                stringStack[stringStackSize - 1] = String.format(SEARCH_BANK_INPUT_TEXT_FOUND, matches);
                break;
            }
            case "bankSearchFilter": {
                String search;
                int itemId = intStack[intStackSize - 1];
                String searchfilter = stringStack[stringStackSize - 1];
                TagTab activeTab = this.tabInterface.getActiveTab();
                boolean bankOpen = this.client.getItemContainer(InventoryID.BANK) != null;
                String string = search = activeTab != null && bankOpen ? TAG_SEARCH + activeTab.getTag() : searchfilter;
                if (search.isEmpty()) {
                    return;
                }
                boolean tagSearch = search.startsWith(TAG_SEARCH);
                if (tagSearch) {
                    search = search.substring(TAG_SEARCH.length()).trim();
                }
                if (this.tagManager.findTag(itemId, search)) {
                    intStack[intStackSize - 2] = 1;
                    break;
                }
                if (!tagSearch) break;
                intStack[intStackSize - 2] = 0;
                break;
            }
            case "getSearchingTagTab": {
                intStack[intStackSize - 1] = this.tabInterface.isActive() ? 1 : 0;
            }
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        if (event.getActionParam1() == WidgetInfo.BANK_ITEM_CONTAINER.getId() && event.getOption().equals("Examine")) {
            Widget container = this.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
            Widget item = container.getChild(event.getActionParam0());
            int itemID = item.getItemId();
            String text = EDIT_TAGS_MENU_OPTION;
            int tagCount = this.tagManager.getTags(itemID, false).size() + this.tagManager.getTags(itemID, true).size();
            if (tagCount > 0) {
                text = text + " (" + tagCount + ")";
            }
            this.client.createMenuEntry(-1).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setTarget(event.getTarget()).setOption(text).setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier()).onClick(this::editTags);
        }
        this.tabInterface.handleAdd(event);
    }

    private void editTags(MenuEntry entry) {
        int inventoryIndex = entry.getParam0();
        ItemContainer bankContainer = this.client.getItemContainer(InventoryID.BANK);
        if (bankContainer == null) {
            return;
        }
        Item[] items = bankContainer.getItems();
        if (inventoryIndex < 0 || inventoryIndex >= items.length) {
            return;
        }
        Item item = bankContainer.getItems()[inventoryIndex];
        if (item == null) {
            return;
        }
        int itemId = item.getId();
        ItemComposition itemComposition = this.itemManager.getItemComposition(itemId);
        String name = itemComposition.getName();
        Collection<String> tags = this.tagManager.getTags(itemId, false);
        this.tagManager.getTags(itemId, true).stream().map(i -> i + VAR_TAG_SUFFIX).forEach(tags::add);
        String initialValue = Text.toCSV(tags);
        this.chatboxPanelManager.openTextInput(name + " tags:<br>(append " + VAR_TAG_SUFFIX + " for variation tag)").addCharValidator(TabInterface.FILTERED_CHARS).value(initialValue).onDone(newValue -> this.clientThread.invoke(() -> {
            ArrayList<String> newTags = new ArrayList<String>(Text.fromCSV(newValue.toLowerCase()));
            Collection newVarTags = new ArrayList<String>(newTags).stream().filter(s -> s.endsWith(VAR_TAG_SUFFIX)).map(s -> {
                newTags.remove(s);
                return s.substring(0, s.length() - VAR_TAG_SUFFIX.length());
            }).collect(Collectors.toList());
            this.tagManager.setTagString(itemId, Text.toCSV(newTags), false);
            this.tagManager.setTagString(itemId, Text.toCSV(newVarTags), true);
            this.tabInterface.updateTabIfActive(Text.fromCSV(initialValue.toLowerCase().replaceAll(Pattern.quote(VAR_TAG_SUFFIX), "")));
            this.tabInterface.updateTabIfActive(Text.fromCSV(newValue.toLowerCase().replaceAll(Pattern.quote(VAR_TAG_SUFFIX), "")));
        })).build();
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        this.tabInterface.handleClick(event);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (configChanged.getGroup().equals(CONFIG_GROUP) && configChanged.getKey().equals("useTabs")) {
            if (this.config.tabs()) {
                this.clientThread.invokeLater(this.tabInterface::init);
            } else {
                this.clientThread.invokeLater(this.tabInterface::destroy);
            }
        }
    }

    @Subscribe
    public void onScriptPreFired(ScriptPreFired event) {
        int scriptId = event.getScriptId();
        if (scriptId == 505) {
            Widget bankTitle;
            TagTab activeTab = this.tabInterface.getActiveTab();
            if (this.tabInterface.isTagTabActive()) {
                bankTitle = this.client.getWidget(WidgetInfo.BANK_TITLE_BAR);
                bankTitle.setText("Tag tab tab");
            } else if (activeTab != null) {
                bankTitle = this.client.getWidget(WidgetInfo.BANK_TITLE_BAR);
                bankTitle.setText("Tag tab <col=ff0000>" + activeTab.getTag() + "</col>");
            }
            if (this.tabInterface.isTagTabActive() || this.tabInterface.isActive() && this.config.removeSeparators()) {
                Widget itemContainer = this.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
                Widget[] children = itemContainer.getChildren();
                int items = 0;
                for (Widget child : children) {
                    if (child == null || child.getItemId() == -1 || child.isHidden()) continue;
                    ++items;
                }
                int adjustedScrollHeight = Math.max(0, items - 1) / 8 * 36 + 36 + 4;
                int[] intStack = this.client.getIntStack();
                int intStackSize = this.client.getIntStackSize();
                intStack[intStackSize - 7] = adjustedScrollHeight;
            }
        } else if (scriptId == 281) {
            this.tabInterface.handleSearch();
        }
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired event) {
        if (event.getScriptId() == 514) {
            boolean bankOpen;
            boolean bl = bankOpen = this.client.getItemContainer(InventoryID.BANK) != null;
            if (bankOpen && (this.tabInterface.getActiveTab() != null || this.tabInterface.isTagTabActive())) {
                this.client.getIntStack()[this.client.getIntStackSize() - 1] = 1;
            }
            return;
        }
        if (event.getScriptId() != 277) {
            return;
        }
        Widget itemContainer = this.client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        if (itemContainer == null) {
            return;
        }
        if (!this.tabInterface.isActive() || !this.config.removeSeparators()) {
            return;
        }
        int items = 0;
        Widget[] containerChildren = itemContainer.getDynamicChildren();
        Arrays.sort(containerChildren, Comparator.comparingInt(Widget::getOriginalY).thenComparingInt(Widget::getOriginalX));
        for (Widget child : containerChildren) {
            if (child.getItemId() != -1 && !child.isHidden()) {
                int adjYOffset = items / 8 * 36;
                int adjXOffset = items % 8 * 48 + 51;
                if (child.getOriginalY() != adjYOffset || child.getOriginalX() != adjXOffset) {
                    child.setOriginalY(adjYOffset);
                    child.setOriginalX(adjXOffset);
                    child.revalidate();
                }
                ++items;
            }
            if (child.getSpriteId() != 897 && !child.getText().contains("Tab")) continue;
            child.setHidden(true);
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        this.tabInterface.update();
    }

    @Subscribe
    public void onDraggingWidgetChanged(DraggingWidgetChanged event) {
        boolean shiftPressed = this.client.isKeyPressed(81);
        this.tabInterface.handleDrag(event.isDraggingWidget(), shiftPressed);
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        if (event.getGroupId() == 12) {
            this.tabInterface.init();
        }
    }

    @Override
    public MouseWheelEvent mouseWheelMoved(MouseWheelEvent event) {
        this.tabInterface.handleWheel(event);
        return event;
    }
}

