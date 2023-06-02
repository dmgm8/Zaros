/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.EquipmentInventorySlot
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.http.api.item.ItemEquipmentStats
 *  net.runelite.http.api.item.ItemStats
 *  org.apache.commons.lang3.time.DurationFormatUtils
 */
package net.runelite.client.plugins.itemstats;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.itemstats.Effect;
import net.runelite.client.plugins.itemstats.ItemStatChanges;
import net.runelite.client.plugins.itemstats.ItemStatConfig;
import net.runelite.client.plugins.itemstats.Positivity;
import net.runelite.client.plugins.itemstats.StatChange;
import net.runelite.client.plugins.itemstats.StatsChanges;
import net.runelite.client.plugins.itemstats.potions.PotionDuration;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.http.api.item.ItemEquipmentStats;
import net.runelite.http.api.item.ItemStats;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class ItemStatOverlay
extends Overlay {
    @VisibleForTesting
    static final ItemStats UNARMED = new ItemStats(true, 0.0, 0, ItemEquipmentStats.builder().aspeed(4).build());
    @Inject
    private Client client;
    @Inject
    private ItemManager itemManager;
    @Inject
    private TooltipManager tooltipManager;
    @Inject
    private ItemStatChanges statChanges;
    @Inject
    private ItemStatConfig config;

    @Override
    public Dimension render(Graphics2D graphics) {
        String tooltip;
        ItemStats stats;
        if (this.client.isMenuOpen() || !this.config.relative() && !this.config.absolute() && !this.config.theoretical()) {
            return null;
        }
        MenuEntry[] menu = this.client.getMenuEntries();
        int menuSize = menu.length;
        if (menuSize <= 0) {
            return null;
        }
        MenuEntry entry = menu[menuSize - 1];
        Widget widget = entry.getWidget();
        if (widget == null) {
            return null;
        }
        int group = WidgetInfo.TO_GROUP((int)widget.getId());
        int itemId = -1;
        if (group == WidgetInfo.EQUIPMENT.getGroupId() || group == 12 && widget.getParentId() == WidgetInfo.BANK_EQUIPMENT_CONTAINER.getId()) {
            Widget widgetItem = widget.getChild(1);
            if (widgetItem != null) {
                itemId = widgetItem.getItemId();
            }
        } else if (widget.getId() == WidgetInfo.INVENTORY.getId() || group == WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER.getGroupId() || widget.getId() == WidgetInfo.BANK_ITEM_CONTAINER.getId() && this.config.showStatsInBank() || group == WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getGroupId() && this.config.showStatsInBank()) {
            itemId = widget.getItemId();
        }
        if (itemId == -1) {
            return null;
        }
        if (this.config.consumableStats()) {
            PotionDuration p;
            Effect change = this.statChanges.get(itemId);
            if (change != null) {
                StringBuilder b = new StringBuilder();
                StatsChanges statsChanges = change.calculate(this.client);
                for (StatChange c : statsChanges.getStatChanges()) {
                    b.append(this.buildStatChangeString(c));
                }
                String tooltip2 = b.toString();
                if (!tooltip2.isEmpty()) {
                    this.tooltipManager.add(new Tooltip(tooltip2));
                }
            }
            if ((p = PotionDuration.get(itemId)) != null) {
                PotionDuration.PotionDurationRange[] durationRanges = p.getDurationRanges();
                StringBuilder sb = new StringBuilder();
                if (durationRanges.length == 1) {
                    Duration duration = durationRanges[0].getLowestDuration();
                    sb.append("Duration: ").append(DurationFormatUtils.formatDuration((long)duration.toMillis(), (String)"m:ss"));
                } else {
                    for (PotionDuration.PotionDurationRange durationRange : durationRanges) {
                        if (sb.length() > 0) {
                            sb.append("</br>");
                        }
                        sb.append(durationRange.getPotionName()).append(": ");
                        Duration lowestDuration = durationRange.getLowestDuration();
                        sb.append(DurationFormatUtils.formatDuration((long)lowestDuration.toMillis(), (String)"m:ss"));
                        Duration highestDuration = durationRange.getHighestDuration();
                        if (lowestDuration == highestDuration) continue;
                        sb.append('~');
                        sb.append(DurationFormatUtils.formatDuration((long)highestDuration.toMillis(), (String)"m:ss"));
                    }
                }
                this.tooltipManager.add(new Tooltip(sb.toString()));
            }
        }
        if (this.config.equipmentStats() && (stats = this.itemManager.getItemStats(itemId, false)) != null && !(tooltip = this.buildStatBonusString(stats)).isEmpty()) {
            this.tooltipManager.add(new Tooltip(tooltip));
        }
        return null;
    }

    private String getChangeString(double value, boolean inverse, boolean showPercent) {
        Color plus = Positivity.getColor(this.config, Positivity.BETTER_UNCAPPED);
        Color minus = Positivity.getColor(this.config, Positivity.WORSE);
        if (value == 0.0) {
            return "";
        }
        Color color = inverse ? (value > 0.0 ? minus : plus) : (value > 0.0 ? plus : minus);
        String prefix = value > 0.0 ? "+" : "";
        String suffix = showPercent ? "%" : "";
        String valueString = QuantityFormatter.formatNumber(value);
        return ColorUtil.wrapWithColorTag(prefix + valueString + suffix, color);
    }

    private String buildStatRow(String label, double value, double diffValue, boolean inverse, boolean showPercent) {
        return this.buildStatRow(label, value, diffValue, inverse, showPercent, true);
    }

    private String buildStatRow(String label, double value, double diffValue, boolean inverse, boolean showPercent, boolean showBase) {
        StringBuilder b = new StringBuilder();
        if (value != 0.0 || diffValue != 0.0) {
            String changeStr = this.getChangeString(diffValue, inverse, showPercent);
            if (this.config.alwaysShowBaseStats() && showBase) {
                String valueStr = QuantityFormatter.formatNumber(value);
                b.append(label).append(": ").append(valueStr).append(!changeStr.isEmpty() ? " (" + changeStr + ") " : "").append("</br>");
            } else if (!changeStr.isEmpty()) {
                b.append(label).append(": ").append(changeStr).append("</br>");
            }
        }
        return b.toString();
    }

    private ItemStats getItemStatsFromContainer(ItemContainer container, int slotID) {
        Item item = container.getItem(slotID);
        return item != null ? this.itemManager.getItemStats(item.getId(), false) : null;
    }

    @VisibleForTesting
    String buildStatBonusString(ItemStats s) {
        ItemStats other = null;
        ItemStats offHand = null;
        ItemEquipmentStats currentEquipment = s.getEquipment();
        ItemContainer c = this.client.getItemContainer(InventoryID.EQUIPMENT);
        if (s.isEquipable() && currentEquipment != null && c != null) {
            ItemEquipmentStats otherEquip;
            int slot = currentEquipment.getSlot();
            other = this.getItemStatsFromContainer(c, slot);
            if (other == null && slot == EquipmentInventorySlot.SHIELD.getSlotIdx() && (other = this.getItemStatsFromContainer(c, EquipmentInventorySlot.WEAPON.getSlotIdx())) != null && (otherEquip = other.getEquipment()) != null) {
                ItemStats itemStats = other = otherEquip.isTwoHanded() ? other.subtract(UNARMED) : null;
            }
            if (slot == EquipmentInventorySlot.WEAPON.getSlotIdx()) {
                if (other == null) {
                    other = UNARMED;
                }
                if (currentEquipment.isTwoHanded()) {
                    offHand = this.getItemStatsFromContainer(c, EquipmentInventorySlot.SHIELD.getSlotIdx());
                }
            }
        }
        ItemStats subtracted = s.subtract(other).subtract(offHand);
        ItemEquipmentStats e = subtracted.getEquipment();
        StringBuilder b = new StringBuilder();
        if (this.config.showWeight()) {
            double sw = this.config.alwaysShowBaseStats() ? subtracted.getWeight() : s.getWeight();
            b.append(this.buildStatRow("Weight", s.getWeight(), sw, true, false, s.isEquipable()));
        }
        if (subtracted.isEquipable() && e != null) {
            b.append(this.buildStatRow("Prayer", currentEquipment.getPrayer(), e.getPrayer(), false, false));
            b.append(this.buildStatRow("Speed", currentEquipment.getAspeed(), e.getAspeed(), true, false));
            b.append(this.buildStatRow("Melee Str", currentEquipment.getStr(), e.getStr(), false, false));
            b.append(this.buildStatRow("Range Str", currentEquipment.getRstr(), e.getRstr(), false, false));
            b.append(this.buildStatRow("Magic Dmg", currentEquipment.getMdmg(), e.getMdmg(), false, true));
            StringBuilder abb = new StringBuilder();
            abb.append(this.buildStatRow("Stab", currentEquipment.getAstab(), e.getAstab(), false, false));
            abb.append(this.buildStatRow("Slash", currentEquipment.getAslash(), e.getAslash(), false, false));
            abb.append(this.buildStatRow("Crush", currentEquipment.getAcrush(), e.getAcrush(), false, false));
            abb.append(this.buildStatRow("Magic", currentEquipment.getAmagic(), e.getAmagic(), false, false));
            abb.append(this.buildStatRow("Range", currentEquipment.getArange(), e.getArange(), false, false));
            if (abb.length() > 0) {
                b.append(ColorUtil.wrapWithColorTag("Attack Bonus</br>", JagexColors.MENU_TARGET)).append((CharSequence)abb);
            }
            StringBuilder dbb = new StringBuilder();
            dbb.append(this.buildStatRow("Stab", currentEquipment.getDstab(), e.getDstab(), false, false));
            dbb.append(this.buildStatRow("Slash", currentEquipment.getDslash(), e.getDslash(), false, false));
            dbb.append(this.buildStatRow("Crush", currentEquipment.getDcrush(), e.getDcrush(), false, false));
            dbb.append(this.buildStatRow("Magic", currentEquipment.getDmagic(), e.getDmagic(), false, false));
            dbb.append(this.buildStatRow("Range", currentEquipment.getDrange(), e.getDrange(), false, false));
            if (dbb.length() > 0) {
                b.append(ColorUtil.wrapWithColorTag("Defence Bonus</br>", JagexColors.MENU_TARGET)).append((CharSequence)dbb);
            }
        }
        return b.toString();
    }

    private String buildStatChangeString(StatChange c) {
        StringBuilder b = new StringBuilder();
        b.append(ColorUtil.colorTag(Positivity.getColor(this.config, c.getPositivity())));
        if (this.config.relative()) {
            b.append(c.getFormattedRelative());
        }
        if (this.config.theoretical()) {
            if (this.config.relative()) {
                b.append('/');
            }
            b.append(c.getFormattedTheoretical());
        }
        if (this.config.absolute() && (this.config.relative() || this.config.theoretical())) {
            b.append(" (");
        }
        if (this.config.absolute()) {
            b.append(c.getAbsolute());
        }
        if (this.config.absolute() && (this.config.relative() || this.config.theoretical())) {
            b.append(')');
        }
        b.append(' ').append(c.getStat().getName());
        b.append("</br>");
        return b.toString();
    }
}

