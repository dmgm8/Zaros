/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.itemprices;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.itemprices.ItemPricesConfig;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.QuantityFormatter;

class ItemPricesOverlay
extends Overlay {
    private static final int INVENTORY_ITEM_WIDGETID = WidgetInfo.INVENTORY.getPackedId();
    private static final int BANK_INVENTORY_ITEM_WIDGETID = WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getPackedId();
    private static final int BANK_ITEM_WIDGETID = WidgetInfo.BANK_ITEM_CONTAINER.getPackedId();
    private static final int EXPLORERS_RING_ITEM_WIDGETID = WidgetInfo.EXPLORERS_RING_ALCH_INVENTORY.getPackedId();
    private static final int SEED_VAULT_ITEM_WIDGETID = WidgetInfo.SEED_VAULT_ITEM_CONTAINER.getPackedId();
    private static final int SEED_VAULT_INVENTORY_ITEM_WIDGETID = WidgetInfo.SEED_VAULT_INVENTORY_ITEMS_CONTAINER.getPackedId();
    private static final int POH_TREASURE_CHEST_INVENTORY_ITEM_WIDGETID = WidgetInfo.POH_TREASURE_CHEST_INVENTORY_CONTAINER.getPackedId();
    private final Client client;
    private final ItemPricesConfig config;
    private final TooltipManager tooltipManager;
    private final StringBuilder itemStringBuilder = new StringBuilder();
    @Inject
    ItemManager itemManager;

    @Inject
    ItemPricesOverlay(Client client, ItemPricesConfig config, TooltipManager tooltipManager) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.config = config;
        this.tooltipManager = tooltipManager;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (this.client.isMenuOpen()) {
            return null;
        }
        MenuEntry[] menuEntries = this.client.getMenuEntries();
        int last = menuEntries.length - 1;
        if (last < 0) {
            return null;
        }
        MenuEntry menuEntry = menuEntries[last];
        MenuAction action = menuEntry.getType();
        int widgetId = menuEntry.getParam1();
        int groupId = WidgetInfo.TO_GROUP((int)widgetId);
        boolean isAlching = menuEntry.getOption().equals("Cast") && menuEntry.getTarget().contains("High Level Alchemy");
        switch (action) {
            case WIDGET_TARGET_ON_WIDGET: {
                if (menuEntry.getWidget().getId() != WidgetInfo.INVENTORY.getId()) break;
            }
            case WIDGET_USE_ON_ITEM: {
                if (!this.config.showWhileAlching() || !isAlching) break;
            }
            case CC_OP: 
            case ITEM_USE: 
            case ITEM_FIRST_OPTION: 
            case ITEM_SECOND_OPTION: 
            case ITEM_THIRD_OPTION: 
            case ITEM_FOURTH_OPTION: 
            case ITEM_FIFTH_OPTION: {
                this.addTooltip(menuEntry, isAlching, groupId);
                break;
            }
            case WIDGET_TARGET: {
                if (menuEntry.getWidget().getId() != WidgetInfo.INVENTORY.getId()) break;
                this.addTooltip(menuEntry, isAlching, groupId);
            }
        }
        return null;
    }

    private void addTooltip(MenuEntry menuEntry, boolean isAlching, int groupId) {
        switch (groupId) {
            case 483: {
                if (!this.config.showWhileAlching()) {
                    return;
                }
            }
            case 149: 
            case 674: {
                if (!(!this.config.hideInventory() || this.config.showWhileAlching() && isAlching)) {
                    return;
                }
            }
            case 12: 
            case 15: 
            case 630: 
            case 631: {
                String text = this.makeValueTooltip(menuEntry);
                if (text == null) break;
                this.tooltipManager.add(new Tooltip(ColorUtil.prependColorTag(text, new Color(238, 238, 238))));
            }
        }
    }

    private String makeValueTooltip(MenuEntry menuEntry) {
        if (!this.config.showGEPrice() && !this.config.showHAValue()) {
            return null;
        }
        int widgetId = menuEntry.getParam1();
        ItemContainer container = null;
        if (widgetId == INVENTORY_ITEM_WIDGETID || widgetId == BANK_INVENTORY_ITEM_WIDGETID || widgetId == EXPLORERS_RING_ITEM_WIDGETID || widgetId == SEED_VAULT_INVENTORY_ITEM_WIDGETID || widgetId == POH_TREASURE_CHEST_INVENTORY_ITEM_WIDGETID) {
            container = this.client.getItemContainer(InventoryID.INVENTORY);
        } else if (widgetId == BANK_ITEM_WIDGETID) {
            container = this.client.getItemContainer(InventoryID.BANK);
        } else if (widgetId == SEED_VAULT_ITEM_WIDGETID) {
            container = this.client.getItemContainer(InventoryID.SEED_VAULT);
        }
        if (container == null) {
            return null;
        }
        int index = menuEntry.getParam0();
        Item item = container.getItem(index);
        if (item != null) {
            return this.getItemStackValueText(item);
        }
        return null;
    }

    private String getItemStackValueText(Item item) {
        int id = this.itemManager.canonicalize(item.getId());
        int qty = item.getQuantity();
        if (id == 995) {
            return QuantityFormatter.formatNumber(qty) + " gp";
        }
        if (id == 13204) {
            return QuantityFormatter.formatNumber((long)qty * 1000L) + " gp";
        }
        ItemComposition itemDef = this.itemManager.getItemComposition(id);
        if (itemDef.getPrice() <= 0) {
            return null;
        }
        int gePrice = 0;
        int haPrice = 0;
        int haProfit = 0;
        int itemHaPrice = itemDef.getHaPrice();
        if (this.config.showGEPrice()) {
            gePrice = this.itemManager.getItemPrice(id);
        }
        if (this.config.showHAValue()) {
            haPrice = itemHaPrice;
        }
        if (gePrice > 0 && itemHaPrice > 0 && this.config.showAlchProfit()) {
            haProfit = this.calculateHAProfit(itemHaPrice, gePrice);
        }
        if (gePrice > 0 || haPrice > 0) {
            return this.stackValueText(qty, gePrice, haPrice, haProfit);
        }
        return null;
    }

    private String stackValueText(int qty, int gePrice, int haValue, int haProfit) {
        if (gePrice > 0) {
            this.itemStringBuilder.append("GE: ").append(QuantityFormatter.quantityToStackSize((long)gePrice * (long)qty)).append(" gp");
            if (this.config.showEA() && qty > 1) {
                this.itemStringBuilder.append(" (").append(QuantityFormatter.quantityToStackSize(gePrice)).append(" ea)");
            }
        }
        if (haValue > 0) {
            if (gePrice > 0) {
                this.itemStringBuilder.append("</br>");
            }
            this.itemStringBuilder.append("HA: ").append(QuantityFormatter.quantityToStackSize((long)haValue * (long)qty)).append(" gp");
            if (this.config.showEA() && qty > 1) {
                this.itemStringBuilder.append(" (").append(QuantityFormatter.quantityToStackSize(haValue)).append(" ea)");
            }
        }
        if (haProfit != 0) {
            Color haColor = ItemPricesOverlay.haProfitColor(haProfit);
            this.itemStringBuilder.append("</br>");
            this.itemStringBuilder.append("HA Profit: ").append(ColorUtil.wrapWithColorTag(String.valueOf((long)haProfit * (long)qty), haColor)).append(" gp");
            if (this.config.showEA() && qty > 1) {
                this.itemStringBuilder.append(" (").append(ColorUtil.wrapWithColorTag(String.valueOf(haProfit), haColor)).append(" ea)");
            }
        }
        String text = this.itemStringBuilder.toString();
        this.itemStringBuilder.setLength(0);
        return text;
    }

    private int calculateHAProfit(int haPrice, int gePrice) {
        int natureRunePrice = this.itemManager.getItemPrice(561);
        return haPrice - gePrice - natureRunePrice;
    }

    private static Color haProfitColor(int haProfit) {
        return haProfit >= 0 ? Color.GREEN : Color.RED;
    }
}

