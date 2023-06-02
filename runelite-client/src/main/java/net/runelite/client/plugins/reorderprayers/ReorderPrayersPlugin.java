/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.HashTable
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.Prayer
 *  net.runelite.api.WidgetNode
 *  net.runelite.api.events.DraggingWidgetChanged
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.WidgetLoaded
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.reorderprayers;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.HashTable;
import net.runelite.api.MenuEntry;
import net.runelite.api.Prayer;
import net.runelite.api.WidgetNode;
import net.runelite.api.events.DraggingWidgetChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.menus.WidgetMenuOption;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.reorderprayers.PrayerTabState;
import net.runelite.client.plugins.reorderprayers.ReorderPrayersConfig;

@PluginDescriptor(name="Reorder Prayers", description="Reorder the prayers displayed on the Prayer panel")
public class ReorderPrayersPlugin
extends Plugin {
    static final String CONFIG_GROUP_KEY = "reorderprayers";
    static final String CONFIG_UNLOCK_REORDERING_KEY = "unlockPrayerReordering";
    static final String CONFIG_PRAYER_ORDER_KEY = "prayerOrder";
    private static final int PRAYER_WIDTH = 34;
    private static final int PRAYER_HEIGHT = 34;
    private static final int PRAYER_X_OFFSET = 37;
    private static final int PRAYER_Y_OFFSET = 37;
    private static final int QUICK_PRAYER_SPRITE_X_OFFSET = 2;
    private static final int QUICK_PRAYER_SPRITE_Y_OFFSET = 2;
    private static final int PRAYER_COLUMN_COUNT = 5;
    private static final int PRAYER_COUNT = Prayer.values().length;
    private static final List<WidgetInfo> PRAYER_WIDGET_INFO_LIST = ImmutableList.of((Object)WidgetInfo.PRAYER_THICK_SKIN, (Object)WidgetInfo.PRAYER_BURST_OF_STRENGTH, (Object)WidgetInfo.PRAYER_CLARITY_OF_THOUGHT, (Object)WidgetInfo.PRAYER_SHARP_EYE, (Object)WidgetInfo.PRAYER_MYSTIC_WILL, (Object)WidgetInfo.PRAYER_ROCK_SKIN, (Object)WidgetInfo.PRAYER_SUPERHUMAN_STRENGTH, (Object)WidgetInfo.PRAYER_IMPROVED_REFLEXES, (Object)WidgetInfo.PRAYER_RAPID_RESTORE, (Object)WidgetInfo.PRAYER_RAPID_HEAL, (Object)WidgetInfo.PRAYER_PROTECT_ITEM, (Object)WidgetInfo.PRAYER_HAWK_EYE, (Object[])new WidgetInfo[]{WidgetInfo.PRAYER_MYSTIC_LORE, WidgetInfo.PRAYER_STEEL_SKIN, WidgetInfo.PRAYER_ULTIMATE_STRENGTH, WidgetInfo.PRAYER_INCREDIBLE_REFLEXES, WidgetInfo.PRAYER_PROTECT_FROM_MAGIC, WidgetInfo.PRAYER_PROTECT_FROM_MISSILES, WidgetInfo.PRAYER_PROTECT_FROM_MELEE, WidgetInfo.PRAYER_EAGLE_EYE, WidgetInfo.PRAYER_MYSTIC_MIGHT, WidgetInfo.PRAYER_RETRIBUTION, WidgetInfo.PRAYER_REDEMPTION, WidgetInfo.PRAYER_SMITE, WidgetInfo.PRAYER_PRESERVE, WidgetInfo.PRAYER_CHIVALRY, WidgetInfo.PRAYER_PIETY, WidgetInfo.PRAYER_RIGOUR, WidgetInfo.PRAYER_AUGURY});
    private static final List<Integer> QUICK_PRAYER_CHILD_IDS = ImmutableList.of((Object)0, (Object)1, (Object)2, (Object)18, (Object)19, (Object)3, (Object)4, (Object)5, (Object)6, (Object)7, (Object)8, (Object)20, (Object[])new Integer[]{21, 9, 10, 11, 12, 13, 14, 22, 23, 15, 16, 17, 28, 25, 26, 24, 27});
    private static final String LOCK = "Lock";
    private static final String UNLOCK = "Unlock";
    private static final String MENU_TARGET = "Reordering";
    private static final WidgetMenuOption FIXED_PRAYER_TAB_LOCK = new WidgetMenuOption("Lock", "Reordering", WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB);
    private static final WidgetMenuOption FIXED_PRAYER_TAB_UNLOCK = new WidgetMenuOption("Unlock", "Reordering", WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB);
    private static final WidgetMenuOption RESIZABLE_PRAYER_TAB_LOCK = new WidgetMenuOption("Lock", "Reordering", WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB);
    private static final WidgetMenuOption RESIZABLE_PRAYER_TAB_UNLOCK = new WidgetMenuOption("Unlock", "Reordering", WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB);
    private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_PRAYER_TAB_LOCK = new WidgetMenuOption("Lock", "Reordering", WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_PRAYER_TAB);
    private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_PRAYER_TAB_UNLOCK = new WidgetMenuOption("Unlock", "Reordering", WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_PRAYER_TAB);
    @Inject
    private Client client;
    @Inject
    private ReorderPrayersConfig config;
    @Inject
    private MenuManager menuManager;
    private Prayer[] prayerOrder;

    static String prayerOrderToString(Prayer[] prayerOrder) {
        return Arrays.stream(prayerOrder).map(Enum::name).collect(Collectors.joining(","));
    }

    private static Prayer[] stringToPrayerOrder(String string) {
        return (Prayer[])Arrays.stream(string.split(",")).map(Prayer::valueOf).toArray(Prayer[]::new);
    }

    private static int getPrayerIndex(Widget widget) {
        int x = widget.getOriginalX() / 37;
        int y = widget.getOriginalY() / 37;
        return x + y * 5;
    }

    private static void setWidgetPosition(Widget widget, int x, int y) {
        widget.setRelativeX(x);
        widget.setRelativeY(y);
        widget.setOriginalX(x);
        widget.setOriginalY(y);
    }

    @Provides
    ReorderPrayersConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ReorderPrayersConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.refreshPrayerTabOption();
        this.prayerOrder = ReorderPrayersPlugin.stringToPrayerOrder(this.config.prayerOrder());
        this.reorderPrayers();
    }

    @Override
    protected void shutDown() throws Exception {
        this.clearPrayerTabMenus();
        this.prayerOrder = Prayer.values();
        this.reorderPrayers(false);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            this.reorderPrayers();
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals(CONFIG_GROUP_KEY)) {
            if (event.getKey().equals(CONFIG_PRAYER_ORDER_KEY)) {
                this.prayerOrder = ReorderPrayersPlugin.stringToPrayerOrder(this.config.prayerOrder());
            } else if (event.getKey().equals(CONFIG_UNLOCK_REORDERING_KEY)) {
                this.refreshPrayerTabOption();
            }
            this.reorderPrayers();
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        if (event.getGroupId() == 541 || event.getGroupId() == 77) {
            this.reorderPrayers();
        }
    }

    @Subscribe
    public void onDraggingWidgetChanged(DraggingWidgetChanged event) {
        if (event.isDraggingWidget() && this.client.getMouseCurrentButton() == 0) {
            Widget draggedWidget = this.client.getDraggedWidget();
            Widget draggedOnWidget = this.client.getDraggedOnWidget();
            if (draggedWidget != null && draggedOnWidget != null) {
                int draggedGroupId = WidgetInfo.TO_GROUP((int)draggedWidget.getId());
                int draggedOnGroupId = WidgetInfo.TO_GROUP((int)draggedOnWidget.getId());
                if (draggedGroupId != 541 || draggedOnGroupId != 541 || draggedOnWidget.getWidth() != 34 || draggedOnWidget.getHeight() != 34) {
                    return;
                }
                this.client.setDraggedOnWidget(null);
                int fromPrayerIndex = ReorderPrayersPlugin.getPrayerIndex(draggedWidget);
                int toPrayerIndex = ReorderPrayersPlugin.getPrayerIndex(draggedOnWidget);
                Prayer tmp = this.prayerOrder[toPrayerIndex];
                this.prayerOrder[toPrayerIndex] = this.prayerOrder[fromPrayerIndex];
                this.prayerOrder[fromPrayerIndex] = tmp;
                this.save();
            }
        }
    }

    private void clearPrayerTabMenus() {
        this.menuManager.removeManagedCustomMenu(FIXED_PRAYER_TAB_LOCK);
        this.menuManager.removeManagedCustomMenu(RESIZABLE_PRAYER_TAB_LOCK);
        this.menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_LOCK);
        this.menuManager.removeManagedCustomMenu(FIXED_PRAYER_TAB_UNLOCK);
        this.menuManager.removeManagedCustomMenu(RESIZABLE_PRAYER_TAB_UNLOCK);
        this.menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_UNLOCK);
    }

    private void refreshPrayerTabOption() {
        this.clearPrayerTabMenus();
        if (this.config.unlockPrayerReordering()) {
            this.menuManager.addManagedCustomMenu(FIXED_PRAYER_TAB_LOCK, this::unlockReordering);
            this.menuManager.addManagedCustomMenu(RESIZABLE_PRAYER_TAB_LOCK, this::unlockReordering);
            this.menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_LOCK, this::unlockReordering);
        } else {
            this.menuManager.addManagedCustomMenu(FIXED_PRAYER_TAB_UNLOCK, this::unlockReordering);
            this.menuManager.addManagedCustomMenu(RESIZABLE_PRAYER_TAB_UNLOCK, this::unlockReordering);
            this.menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_UNLOCK, this::unlockReordering);
        }
    }

    private void unlockReordering(MenuEntry entry) {
        this.config.unlockPrayerReordering(entry.getOption().equals(UNLOCK));
    }

    private PrayerTabState getPrayerTabState() {
        HashTable componentTable = this.client.getComponentTable();
        for (WidgetNode widgetNode : componentTable.getNodes()) {
            if (widgetNode.getId() == 541) {
                return PrayerTabState.PRAYERS;
            }
            if (widgetNode.getId() != 77) continue;
            return PrayerTabState.QUICK_PRAYERS;
        }
        return PrayerTabState.NONE;
    }

    private void save() {
        this.config.prayerOrder(ReorderPrayersPlugin.prayerOrderToString(this.prayerOrder));
    }

    private void reorderPrayers() {
        this.reorderPrayers(this.config.unlockPrayerReordering());
    }

    private void reorderPrayers(boolean unlocked) {
        block9: {
            PrayerTabState prayerTabState;
            block8: {
                if (this.client.getGameState() != GameState.LOGGED_IN) {
                    return;
                }
                prayerTabState = this.getPrayerTabState();
                if (prayerTabState != PrayerTabState.PRAYERS) break block8;
                List prayerWidgets = PRAYER_WIDGET_INFO_LIST.stream().map(((Client)this.client)::getWidget).filter(Objects::nonNull).collect(Collectors.toList());
                if (prayerWidgets.size() != PRAYER_WIDGET_INFO_LIST.size()) {
                    return;
                }
                for (int index = 0; index < this.prayerOrder.length; ++index) {
                    Prayer prayer = this.prayerOrder[index];
                    Widget prayerWidget = (Widget)prayerWidgets.get(prayer.ordinal());
                    int widgetConfig = prayerWidget.getClickMask();
                    if (unlocked) {
                        widgetConfig |= 0x100000;
                        widgetConfig |= 0x20000;
                    } else {
                        widgetConfig &= 0xFFEFFFFF;
                        widgetConfig &= 0xFFFDFFFF;
                    }
                    prayerWidget.setClickMask(widgetConfig);
                    int x = index % 5;
                    int y = index / 5;
                    int widgetX = x * 37;
                    int widgetY = y * 37;
                    ReorderPrayersPlugin.setWidgetPosition(prayerWidget, widgetX, widgetY);
                }
                break block9;
            }
            if (prayerTabState != PrayerTabState.QUICK_PRAYERS) break block9;
            Widget prayersContainer = this.client.getWidget(WidgetInfo.QUICK_PRAYER_PRAYERS);
            if (prayersContainer == null) {
                return;
            }
            Widget[] prayerWidgets = prayersContainer.getDynamicChildren();
            if (prayerWidgets == null || prayerWidgets.length != PRAYER_COUNT * 3) {
                return;
            }
            for (int index = 0; index < this.prayerOrder.length; ++index) {
                Prayer prayer = this.prayerOrder[index];
                int x = index % 5;
                int y = index / 5;
                Widget prayerWidget = prayerWidgets[QUICK_PRAYER_CHILD_IDS.get(prayer.ordinal())];
                ReorderPrayersPlugin.setWidgetPosition(prayerWidget, x * 37, y * 37);
                int childId = PRAYER_COUNT + 2 * prayer.ordinal();
                Widget prayerSpriteWidget = prayerWidgets[childId];
                ReorderPrayersPlugin.setWidgetPosition(prayerSpriteWidget, 2 + x * 37, 2 + y * 37);
                Widget prayerToggleWidget = prayerWidgets[childId + 1];
                ReorderPrayersPlugin.setWidgetPosition(prayerToggleWidget, x * 37, y * 37);
            }
        }
    }
}

