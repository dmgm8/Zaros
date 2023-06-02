/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Point
 *  net.runelite.api.RenderOverview
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.RenderOverview;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

class WorldMapRegionOverlay
extends Overlay {
    private static final Color WHITE_TRANSLUCENT = new Color(255, 255, 255, 127);
    private static final int LABEL_PADDING = 4;
    private static final int REGION_SIZE = 64;
    private static final int REGION_TRUNCATE = -64;
    private final Client client;
    private final DevToolsPlugin plugin;

    @Inject
    private WorldMapRegionOverlay(Client client, DevToolsPlugin plugin) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
        this.setLayer(OverlayLayer.MANUAL);
        this.drawAfterInterface(595);
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.plugin.getWorldMapLocation().isActive()) {
            return null;
        }
        this.drawRegionOverlay(graphics);
        return null;
    }

    private void drawRegionOverlay(Graphics2D graphics) {
        RenderOverview ro = this.client.getRenderOverview();
        Widget map = this.client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
        float pixelsPerTile = ro.getWorldMapZoom();
        if (map == null) {
            return;
        }
        Rectangle worldMapRect = map.getBounds();
        graphics.setClip(worldMapRect);
        int widthInTiles = (int)Math.ceil(worldMapRect.getWidth() / (double)pixelsPerTile);
        int heightInTiles = (int)Math.ceil(worldMapRect.getHeight() / (double)pixelsPerTile);
        Point worldMapPosition = ro.getWorldMapPosition();
        int yTileMin = worldMapPosition.getY() - heightInTiles / 2;
        int xRegionMin = worldMapPosition.getX() - widthInTiles / 2 & 0xFFFFFFC0;
        int xRegionMax = (worldMapPosition.getX() + widthInTiles / 2 & 0xFFFFFFC0) + 64;
        int yRegionMin = yTileMin & 0xFFFFFFC0;
        int yRegionMax = (worldMapPosition.getY() + heightInTiles / 2 & 0xFFFFFFC0) + 64;
        int regionPixelSize = (int)Math.ceil(64.0f * pixelsPerTile);
        for (int x = xRegionMin; x < xRegionMax; x += 64) {
            for (int y = yRegionMin; y < yRegionMax; y += 64) {
                graphics.setColor(WHITE_TRANSLUCENT);
                int yTileOffset = -(yTileMin - y);
                int xTileOffset = x + widthInTiles / 2 - worldMapPosition.getX();
                int xPos = (int)((float)xTileOffset * pixelsPerTile) + (int)worldMapRect.getX();
                int yPos = worldMapRect.height - (int)((float)yTileOffset * pixelsPerTile) + (int)worldMapRect.getY();
                graphics.drawRect(xPos, yPos -= regionPixelSize, regionPixelSize, regionPixelSize);
                int regionId = x >> 6 << 8 | y >> 6;
                String regionText = String.valueOf(regionId);
                FontMetrics fm = graphics.getFontMetrics();
                Rectangle2D textBounds = fm.getStringBounds(regionText, graphics);
                int labelWidth = (int)textBounds.getWidth() + 8;
                int labelHeight = (int)textBounds.getHeight() + 8;
                graphics.fillRect(xPos, yPos, labelWidth, labelHeight);
                graphics.setColor(Color.BLACK);
                graphics.drawString(regionText, xPos + 4, yPos + (int)textBounds.getHeight() + 4);
            }
        }
    }
}

