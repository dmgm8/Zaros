/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Perspective
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.grounditems;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.grounditems.GroundItem;
import net.runelite.client.plugins.grounditems.GroundItemsConfig;
import net.runelite.client.plugins.grounditems.GroundItemsPlugin;
import net.runelite.client.plugins.grounditems.LootType;
import net.runelite.client.plugins.grounditems.NamedQuantity;
import net.runelite.client.plugins.grounditems.config.DespawnTimerMode;
import net.runelite.client.plugins.grounditems.config.ItemHighlightMode;
import net.runelite.client.plugins.grounditems.config.PriceDisplayMode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.BackgroundComponent;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.util.QuantityFormatter;

public class GroundItemsOverlay
extends Overlay {
    private static final int MAX_DISTANCE = 2500;
    private static final int OFFSET_Z = 20;
    private static final int STRING_GAP = 15;
    private static final int RECTANGLE_SIZE = 8;
    private static final Color PUBLIC_TIMER_COLOR = Color.YELLOW;
    private static final Color PRIVATE_TIMER_COLOR = Color.GREEN;
    private static final int TIMER_OVERLAY_DIAMETER = 10;
    private static final Duration DESPAWN_TIME_INSTANCE = Duration.ofMinutes(30L);
    private static final Duration DESPAWN_TIME_LOOT = Duration.ofMinutes(2L);
    private static final Duration DESPAWN_TIME_DROP = Duration.ofMinutes(3L);
    private static final Duration DESPAWN_TIME_TABLE = Duration.ofMinutes(10L);
    private static final int KRAKEN_REGION = 9116;
    private static final int CLAN_HALL_REGION = 6997;
    private static final int KBD_NMZ_REGION = 9033;
    private static final int ZILYANA_REGION = 11602;
    private static final int GRAARDOR_REGION = 11347;
    private static final int KRIL_TSUTSAROTH_REGION = 11603;
    private static final int KREEARRA_REGION = 11346;
    private static final int NEX_REGION = 11601;
    private static final int NIGHTMARE_REGION = 15515;
    private static final int TEMPOROSS_REGION = 12078;
    private final Client client;
    private final GroundItemsPlugin plugin;
    private final GroundItemsConfig config;
    private final StringBuilder itemStringBuilder = new StringBuilder();
    private final BackgroundComponent backgroundComponent = new BackgroundComponent();
    private final TextComponent textComponent = new TextComponent();
    private final ProgressPieComponent progressPieComponent = new ProgressPieComponent();
    private final Map<WorldPoint, Integer> offsetMap = new HashMap<WorldPoint, Integer>();

    @Inject
    private GroundItemsOverlay(Client client, GroundItemsPlugin plugin, GroundItemsConfig config) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        boolean dontShowOverlay;
        boolean bl = dontShowOverlay = (this.config.itemHighlightMode() == ItemHighlightMode.MENU || this.config.itemHighlightMode() == ItemHighlightMode.NONE || this.plugin.isHideAll()) && !this.plugin.isHotKeyPressed();
        if (dontShowOverlay && !this.config.highlightTiles()) {
            return null;
        }
        FontMetrics fm = graphics.getFontMetrics();
        Player player = this.client.getLocalPlayer();
        if (player == null) {
            return null;
        }
        this.offsetMap.clear();
        LocalPoint localLocation = player.getLocalLocation();
        Point mousePos = this.client.getMouseCanvasPosition();
        ArrayList<GroundItem> groundItemList = this.plugin.getCollectedGroundItems().values();
        GroundItem topGroundItem = null;
        if (this.plugin.isHotKeyPressed()) {
            groundItemList = new ArrayList<GroundItem>(groundItemList);
            java.awt.Point awtMousePos = new java.awt.Point(mousePos.getX(), mousePos.getY());
            GroundItem groundItem = null;
            for (GroundItem item : groundItemList) {
                item.setOffset(this.offsetMap.compute(item.getLocation(), (k, v) -> v != null ? v + 1 : 0));
                if (groundItem != null) continue;
                if (this.plugin.getTextBoxBounds() != null && item.equals(this.plugin.getTextBoxBounds().getValue()) && this.plugin.getTextBoxBounds().getKey().contains(awtMousePos)) {
                    groundItem = item;
                    continue;
                }
                if (this.plugin.getHiddenBoxBounds() != null && item.equals(this.plugin.getHiddenBoxBounds().getValue()) && this.plugin.getHiddenBoxBounds().getKey().contains(awtMousePos)) {
                    groundItem = item;
                    continue;
                }
                if (this.plugin.getHighlightBoxBounds() == null || !item.equals(this.plugin.getHighlightBoxBounds().getValue()) || !this.plugin.getHighlightBoxBounds().getKey().contains(awtMousePos)) continue;
                groundItem = item;
            }
            if (groundItem != null) {
                groundItemList.remove(groundItem);
                groundItemList.add(groundItem);
                topGroundItem = groundItem;
            }
        }
        this.plugin.setTextBoxBounds(null);
        this.plugin.setHiddenBoxBounds(null);
        this.plugin.setHighlightBoxBounds(null);
        boolean onlyShowLoot = this.config.onlyShowLoot();
        DespawnTimerMode groundItemTimers = this.config.groundItemTimers();
        boolean outline = this.config.textOutline();
        for (GroundItem item : groundItemList) {
            Polygon poly;
            LocalPoint groundPoint = LocalPoint.fromWorld((Client)this.client, (WorldPoint)item.getLocation());
            if (groundPoint == null || localLocation.distanceTo(groundPoint) > 2500 || onlyShowLoot && !item.isMine()) continue;
            Color highlighted = this.plugin.getHighlighted(new NamedQuantity(item), item.getGePrice(), item.getHaPrice());
            Color hidden = this.plugin.getHidden(new NamedQuantity(item), item.getGePrice(), item.getHaPrice(), item.isTradeable());
            if (highlighted == null && !this.plugin.isHotKeyPressed() && (hidden != null || this.config.showHighlightedOnly())) continue;
            Color color = this.plugin.getItemColor(highlighted, hidden);
            if (this.config.highlightTiles() && (poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)groundPoint, (int)item.getHeight())) != null) {
                OverlayUtil.renderPolygon(graphics, poly, color);
            }
            if (dontShowOverlay) continue;
            this.itemStringBuilder.append(item.getName());
            if (item.getQuantity() > 1) {
                if (item.getQuantity() >= 65535) {
                    this.itemStringBuilder.append(" (Lots!)");
                } else {
                    this.itemStringBuilder.append(" (").append(QuantityFormatter.quantityToStackSize(item.getQuantity())).append(')');
                }
            }
            if (this.config.priceDisplayMode() == PriceDisplayMode.BOTH) {
                if (item.getGePrice() > 0) {
                    this.itemStringBuilder.append(" (GE: ").append(QuantityFormatter.quantityToStackSize(item.getGePrice())).append(" gp)");
                }
                if (item.getHaPrice() > 0) {
                    this.itemStringBuilder.append(" (HA: ").append(QuantityFormatter.quantityToStackSize(item.getHaPrice())).append(" gp)");
                }
            } else if (this.config.priceDisplayMode() != PriceDisplayMode.OFF) {
                int price;
                int n = price = this.config.priceDisplayMode() == PriceDisplayMode.GE ? item.getGePrice() : item.getHaPrice();
                if (price > 0) {
                    this.itemStringBuilder.append(" (").append(QuantityFormatter.quantityToStackSize(price)).append(" gp)");
                }
            }
            String itemString = this.itemStringBuilder.toString();
            this.itemStringBuilder.setLength(0);
            Point textPoint = Perspective.getCanvasTextLocation((Client)this.client, (Graphics2D)graphics, (LocalPoint)groundPoint, (String)itemString, (int)(item.getHeight() + 20));
            if (textPoint == null) continue;
            int offset = this.plugin.isHotKeyPressed() ? item.getOffset() : this.offsetMap.compute(item.getLocation(), (k, v) -> v != null ? v + 1 : 0).intValue();
            int textX = textPoint.getX();
            int textY = textPoint.getY() - 15 * offset;
            if (this.plugin.isHotKeyPressed()) {
                boolean topItem;
                int stringWidth = fm.stringWidth(itemString);
                int stringHeight = fm.getHeight();
                int x = textX - 2;
                int y = textY - stringHeight - 2;
                int width = stringWidth + 4;
                int height = stringHeight + 4;
                Rectangle itemBounds = new Rectangle(x, y, width, height);
                x += width + 2;
                y = textY - (8 + stringHeight) / 2;
                height = 8;
                width = 8;
                Rectangle itemHiddenBox = new Rectangle(x, y, width, height);
                Rectangle itemHighlightBox = new Rectangle(x += width + 2, y, width, height);
                boolean mouseInBox = itemBounds.contains(mousePos.getX(), mousePos.getY());
                boolean mouseInHiddenBox = itemHiddenBox.contains(mousePos.getX(), mousePos.getY());
                boolean mouseInHighlightBox = itemHighlightBox.contains(mousePos.getX(), mousePos.getY());
                if (mouseInBox) {
                    this.plugin.setTextBoxBounds(new AbstractMap.SimpleEntry<Rectangle, GroundItem>(itemBounds, item));
                } else if (mouseInHiddenBox) {
                    this.plugin.setHiddenBoxBounds(new AbstractMap.SimpleEntry<Rectangle, GroundItem>(itemHiddenBox, item));
                } else if (mouseInHighlightBox) {
                    this.plugin.setHighlightBoxBounds(new AbstractMap.SimpleEntry<Rectangle, GroundItem>(itemHighlightBox, item));
                }
                boolean bl2 = topItem = topGroundItem == item;
                if (topItem && (mouseInBox || mouseInHiddenBox || mouseInHighlightBox)) {
                    this.backgroundComponent.setRectangle(itemBounds);
                    this.backgroundComponent.render(graphics);
                }
                this.drawRectangle(graphics, itemHiddenBox, topItem && mouseInHiddenBox ? Color.RED : color, hidden != null, true);
                this.drawRectangle(graphics, itemHighlightBox, topItem && mouseInHighlightBox ? Color.GREEN : color, highlighted != null, false);
            }
            if (groundItemTimers == DespawnTimerMode.PIE || this.plugin.isHotKeyPressed()) {
                this.drawTimerPieOverlay(graphics, textX, textY, item);
            } else if (groundItemTimers == DespawnTimerMode.SECONDS || groundItemTimers == DespawnTimerMode.TICKS) {
                Instant despawnTime = this.calculateDespawnTime(item);
                Color timerColor = this.getItemTimerColor(item);
                if (despawnTime != null && timerColor != null) {
                    long despawnTimeMillis = despawnTime.toEpochMilli() - Instant.now().toEpochMilli();
                    String timerText = groundItemTimers == DespawnTimerMode.SECONDS ? String.format(" - %.1f", Float.valueOf((float)despawnTimeMillis / 1000.0f)) : String.format(" - %d", despawnTimeMillis / 600L);
                    this.textComponent.setText(timerText);
                    this.textComponent.setColor(timerColor);
                    this.textComponent.setOutline(outline);
                    this.textComponent.setPosition(new java.awt.Point(textX + fm.stringWidth(itemString), textY));
                    this.textComponent.render(graphics);
                }
            }
            this.textComponent.setText(itemString);
            this.textComponent.setColor(color);
            this.textComponent.setOutline(outline);
            this.textComponent.setPosition(new java.awt.Point(textX, textY));
            this.textComponent.render(graphics);
        }
        return null;
    }

    private Instant calculateDespawnTime(GroundItem groundItem) {
        Instant despawnTime;
        if (groundItem.getLootType() != LootType.PVM && groundItem.getLootType() != LootType.DROPPED && groundItem.getLootType() != LootType.TABLE) {
            return null;
        }
        Instant spawnTime = groundItem.getSpawnTime();
        if (spawnTime == null) {
            return null;
        }
        Instant now = Instant.now();
        if (this.client.isInInstancedRegion()) {
            int playerRegionID = WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)this.client.getLocalPlayer().getLocalLocation()).getRegionID();
            if (playerRegionID == 9116) {
                return null;
            }
            if (playerRegionID == 9033) {
                if (this.client.getLocalPlayer().getWorldLocation().getPlane() == 0) {
                    despawnTime = spawnTime.plus(groundItem.getLootType() == LootType.DROPPED ? DESPAWN_TIME_DROP : DESPAWN_TIME_LOOT);
                } else {
                    if (groundItem.getLootType() == LootType.DROPPED) {
                        return null;
                    }
                    despawnTime = spawnTime.plus(DESPAWN_TIME_LOOT);
                }
            } else {
                despawnTime = playerRegionID == 11602 || playerRegionID == 11347 || playerRegionID == 11603 || playerRegionID == 11346 || playerRegionID == 11601 || playerRegionID == 15515 || playerRegionID == 12078 || playerRegionID == 6997 ? spawnTime.plus(groundItem.getLootType() == LootType.DROPPED ? DESPAWN_TIME_DROP : DESPAWN_TIME_LOOT) : spawnTime.plus(DESPAWN_TIME_INSTANCE);
            }
        } else {
            switch (groundItem.getLootType()) {
                case DROPPED: {
                    despawnTime = spawnTime.plus(DESPAWN_TIME_DROP);
                    break;
                }
                case TABLE: {
                    despawnTime = spawnTime.plus(DESPAWN_TIME_TABLE);
                    break;
                }
                default: {
                    despawnTime = spawnTime.plus(DESPAWN_TIME_LOOT);
                }
            }
        }
        if (now.isBefore(spawnTime) || now.isAfter(despawnTime)) {
            return null;
        }
        return despawnTime;
    }

    private Color getItemTimerColor(GroundItem groundItem) {
        if (groundItem.getLootType() != LootType.PVM && groundItem.getLootType() != LootType.DROPPED && groundItem.getLootType() != LootType.TABLE) {
            return null;
        }
        Instant spawnTime = groundItem.getSpawnTime();
        if (spawnTime == null) {
            return null;
        }
        Instant now = Instant.now();
        if (this.client.isInInstancedRegion() || spawnTime.plus(1L, ChronoUnit.MINUTES).isAfter(now)) {
            return PRIVATE_TIMER_COLOR;
        }
        return PUBLIC_TIMER_COLOR;
    }

    private void drawTimerPieOverlay(Graphics2D graphics, int textX, int textY, GroundItem groundItem) {
        Instant now = Instant.now();
        Instant spawnTime = groundItem.getSpawnTime();
        Instant despawnTime = this.calculateDespawnTime(groundItem);
        Color fillColor = this.getItemTimerColor(groundItem);
        if (spawnTime == null || despawnTime == null || fillColor == null) {
            return;
        }
        float percent = (float)(now.toEpochMilli() - spawnTime.toEpochMilli()) / (float)(despawnTime.toEpochMilli() - spawnTime.toEpochMilli());
        this.progressPieComponent.setDiameter(10);
        int x = textX - 10;
        int y = textY - 5;
        this.progressPieComponent.setPosition(new Point(x, y));
        this.progressPieComponent.setFill(fillColor);
        this.progressPieComponent.setBorderColor(fillColor);
        this.progressPieComponent.setProgress(1.0f - percent);
        this.progressPieComponent.render(graphics);
    }

    private void drawRectangle(Graphics2D graphics, Rectangle rect, Color color, boolean inList, boolean hiddenBox) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(rect.x + 1, rect.y + 1, rect.width, rect.height);
        graphics.setColor(color);
        graphics.draw(rect);
        if (inList) {
            graphics.fill(rect);
        }
        graphics.setColor(Color.WHITE);
        graphics.drawLine(rect.x + 2, rect.y + rect.height / 2, rect.x + rect.width - 2, rect.y + rect.height / 2);
        if (!hiddenBox) {
            graphics.drawLine(rect.x + rect.width / 2, rect.y + 2, rect.x + rect.width / 2, rect.y + rect.height - 2);
        }
    }
}

