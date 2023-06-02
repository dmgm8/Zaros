/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 */
package net.runelite.client.plugins.specialcounter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.plugins.specialcounter.PlayerInfoDrop;
import net.runelite.client.plugins.specialcounter.SpecialCounterConfig;
import net.runelite.client.plugins.specialcounter.SpecialCounterPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

@Singleton
class PlayerInfoDropOverlay
extends Overlay {
    private final SpecialCounterPlugin plugin;
    private final SpecialCounterConfig config;
    private final Client client;

    @Inject
    private PlayerInfoDropOverlay(SpecialCounterPlugin plugin, SpecialCounterConfig config, Client client) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        List<PlayerInfoDrop> infoDrops = this.plugin.getPlayerInfoDrops();
        if (infoDrops.isEmpty()) {
            return null;
        }
        int cycle = this.client.getGameCycle();
        Iterator<PlayerInfoDrop> iterator = infoDrops.iterator();
        while (iterator.hasNext()) {
            Player player;
            PlayerInfoDrop infoDrop = iterator.next();
            if (cycle < infoDrop.getStartCycle()) continue;
            if (cycle > infoDrop.getEndCycle()) {
                iterator.remove();
                continue;
            }
            if (!this.config.specDrops() || (player = this.client.getCachedPlayers()[infoDrop.getPlayerIdx()]) == null) continue;
            int elapsed = cycle - infoDrop.getStartCycle();
            int percent = elapsed * 100 / (infoDrop.getEndCycle() - infoDrop.getStartCycle());
            int currentHeight = infoDrop.getEndHeightOffset() * percent / 100;
            String text = infoDrop.getText();
            graphics.setFont(infoDrop.getFont());
            Point textLocation = player.getCanvasTextLocation(graphics, text, player.getLogicalHeight() + infoDrop.getStartHeightOffset() + currentHeight);
            if (textLocation == null) continue;
            int alpha = 255 - 255 * percent / 100;
            BufferedImage image = infoDrop.getImage();
            if (image != null) {
                int textHeight = graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getMaxDescent();
                int textMargin = image.getWidth() / 2;
                int x = textLocation.getX() - textMargin - 1;
                int y = textLocation.getY() - textHeight / 2 - image.getHeight() / 2;
                Point imageLocation = new Point(x, y);
                textLocation = new Point(textLocation.getX() + textMargin, textLocation.getY());
                OverlayUtil.renderImageLocation(graphics, imageLocation, ImageUtil.alphaOffset((Image)image, alpha - 255));
            }
            PlayerInfoDropOverlay.drawText(graphics, textLocation, text, infoDrop.getColor(), alpha);
        }
        return null;
    }

    private static void drawText(Graphics2D g, Point point, String text, Color color, int colorAlpha) {
        g.setColor(ColorUtil.colorWithAlpha(Color.BLACK, colorAlpha));
        g.drawString(text, point.getX() + 1, point.getY() + 1);
        g.setColor(ColorUtil.colorWithAlpha(color, colorAlpha));
        g.drawString(text, point.getX(), point.getY());
    }
}

