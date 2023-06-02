/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.EnumComposition
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.Point
 *  net.runelite.api.widgets.WidgetItem
 */
package net.runelite.client.plugins.runepouch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.ItemComposition;
import net.runelite.api.Point;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.runepouch.RunepouchConfig;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ColorUtil;

class RunepouchOverlay
extends WidgetItemOverlay {
    private static final int NUM_SLOTS = 4;
    private static final int[] AMOUNT_VARBITS = new int[]{1624, 1625, 1626, 14286};
    private static final int[] RUNE_VARBITS = new int[]{29, 1622, 1623, 14285};
    private static final Dimension IMAGE_SIZE = new Dimension(11, 11);
    private final Client client;
    private final RunepouchConfig config;
    private final TooltipManager tooltipManager;
    private final ItemManager itemManager;
    private BufferedImage[] runeIcons = new BufferedImage[0];

    @Inject
    RunepouchOverlay(Client client, RunepouchConfig config, TooltipManager tooltipManager, ItemManager itemManager) {
        this.tooltipManager = tooltipManager;
        this.client = client;
        this.config = config;
        this.itemManager = itemManager;
        this.showOnInventory();
        this.showOnBank();
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem) {
        int runeId;
        if (itemId != 12791 && itemId != 24416 && itemId != 27281) {
            return;
        }
        int[] runeIds = new int[4];
        int[] amounts = new int[4];
        EnumComposition runepouchEnum = this.client.getEnum(982);
        int num = 0;
        for (int i = 0; i < 4; ++i) {
            int amount;
            int amountVarbit = AMOUNT_VARBITS[i];
            amounts[i] = amount = this.client.getVarbitValue(amountVarbit);
            int runeVarbit = RUNE_VARBITS[i];
            runeIds[i] = runeId = this.client.getVarbitValue(runeVarbit);
            if (runeId == 0 || amount <= 0) continue;
            ++num;
        }
        if (num == 0) {
            return;
        }
        RunepouchConfig.RunepouchOverlayMode overlayMode = this.config.runePouchOverlayMode();
        if (overlayMode != RunepouchConfig.RunepouchOverlayMode.MOUSE_HOVER) {
            if (num < 4) {
                this.renderList(graphics, widgetItem, runepouchEnum, runeIds, amounts);
            } else {
                this.renderGrid(graphics, widgetItem, runepouchEnum, runeIds, amounts);
            }
        }
        Point mousePos = this.client.getMouseCanvasPosition();
        if (widgetItem.getCanvasBounds().contains(mousePos.getX(), mousePos.getY()) && (overlayMode == RunepouchConfig.RunepouchOverlayMode.MOUSE_HOVER || overlayMode == RunepouchConfig.RunepouchOverlayMode.BOTH)) {
            StringBuilder tooltipBuilder = new StringBuilder();
            for (int i = 0; i < 4; ++i) {
                runeId = runeIds[i];
                int amount = amounts[i];
                if (runeId == 0 || amount <= 0) continue;
                ItemComposition rune = this.itemManager.getItemComposition(runepouchEnum.getIntValue(runeId));
                tooltipBuilder.append(amount).append(' ').append(ColorUtil.wrapWithColorTag(rune.getName(), Color.YELLOW)).append("</br>");
            }
            String tooltip = tooltipBuilder.toString();
            this.tooltipManager.add(new Tooltip(tooltip));
        }
    }

    private void renderList(Graphics2D graphics, WidgetItem widgetItem, EnumComposition runepouchEnum, int[] runeIds, int[] amounts) {
        graphics.setFont(FontManager.getRunescapeSmallFont());
        Point location = widgetItem.getCanvasLocation();
        int runeNum = -1;
        for (int i = 0; i < 4; ++i) {
            int runeId = runeIds[i];
            int amount = amounts[i];
            if (runeId == 0 || amount <= 0) continue;
            String text = RunepouchOverlay.formatNumber(amount);
            int textX = location.getX() + 11;
            int textY = location.getY() + 12 + (graphics.getFontMetrics().getHeight() - 1) * ++runeNum;
            graphics.setColor(Color.BLACK);
            graphics.drawString(text, textX + 1, textY + 1);
            graphics.setColor(this.config.fontColor());
            graphics.drawString(text, textX, textY);
            BufferedImage image = this.getRuneImage(runepouchEnum, runeId);
            if (image == null) continue;
            OverlayUtil.renderImageLocation(graphics, new Point(location.getX() - 1, location.getY() + graphics.getFontMetrics().getHeight() * runeNum - 1), image);
        }
    }

    private void renderGrid(Graphics2D graphics, WidgetItem widgetItem, EnumComposition runepouchEnum, int[] runeIds, int[] amounts) {
        Point location = widgetItem.getCanvasLocation();
        for (int i = 0; i < 4; ++i) {
            Color color;
            int height;
            int runeId = runeIds[i];
            int amount = amounts[i];
            if (runeId == -1 || amount <= 0) continue;
            int iconX = location.getX() + 2 + (i == 1 || i == 3 ? RunepouchOverlay.IMAGE_SIZE.width + 2 + 2 : 0);
            int iconY = location.getY() + 5 + (i >= 2 ? RunepouchOverlay.IMAGE_SIZE.height + 2 : 0);
            BufferedImage image = this.getRuneImage(runepouchEnum, runeId);
            if (image != null) {
                OverlayUtil.renderImageLocation(graphics, new Point(iconX, iconY), image);
            }
            if (amount < 1000) {
                height = amount / 100;
                color = Color.RED;
            } else {
                height = Math.min(10, amount / 1000);
                color = Color.GREEN;
            }
            graphics.setColor(color);
            graphics.fillRect(iconX + RunepouchOverlay.IMAGE_SIZE.width, iconY + 1 + (10 - height), 2, height);
        }
    }

    private BufferedImage getRuneImage(EnumComposition runepouchEnum, int runeId) {
        if (runeId < this.runeIcons.length && this.runeIcons[runeId] != null) {
            return this.runeIcons[runeId];
        }
        AsyncBufferedImage runeImg = this.itemManager.getImage(runepouchEnum.getIntValue(runeId));
        if (runeImg == null) {
            return null;
        }
        BufferedImage resizedImg = new BufferedImage(RunepouchOverlay.IMAGE_SIZE.width, RunepouchOverlay.IMAGE_SIZE.height, 2);
        Graphics2D g = resizedImg.createGraphics();
        g.drawImage(runeImg, 0, 0, RunepouchOverlay.IMAGE_SIZE.width, RunepouchOverlay.IMAGE_SIZE.height, null);
        g.dispose();
        if (runeId >= this.runeIcons.length) {
            this.runeIcons = Arrays.copyOf(this.runeIcons, runeId + 1);
        }
        this.runeIcons[runeId] = resizedImg;
        return resizedImg;
    }

    private static String formatNumber(int amount) {
        return amount < 1000 ? String.valueOf(amount) : amount / 1000 + "K";
    }
}

