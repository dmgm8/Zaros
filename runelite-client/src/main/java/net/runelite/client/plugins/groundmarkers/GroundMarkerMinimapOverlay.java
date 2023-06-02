/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Perspective
 *  net.runelite.api.Point
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.groundmarkers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.groundmarkers.ColorTileMarker;
import net.runelite.client.plugins.groundmarkers.GroundMarkerConfig;
import net.runelite.client.plugins.groundmarkers.GroundMarkerPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

class GroundMarkerMinimapOverlay
extends Overlay {
    private static final int MAX_DRAW_DISTANCE = 16;
    private static final int TILE_WIDTH = 4;
    private static final int TILE_HEIGHT = 4;
    private final Client client;
    private final GroundMarkerConfig config;
    private final GroundMarkerPlugin plugin;

    @Inject
    private GroundMarkerMinimapOverlay(Client client, GroundMarkerConfig config, GroundMarkerPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.LOW);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.config.drawTileOnMinimmap()) {
            return null;
        }
        List<ColorTileMarker> points = this.plugin.getPoints();
        for (ColorTileMarker point : points) {
            WorldPoint worldPoint = point.getWorldPoint();
            if (worldPoint.getPlane() != this.client.getPlane()) continue;
            Color tileColor = point.getColor();
            if (tileColor == null || !this.config.rememberTileColors()) {
                tileColor = this.config.markerColor();
            }
            this.drawOnMinimap(graphics, worldPoint, tileColor);
        }
        return null;
    }

    private void drawOnMinimap(Graphics2D graphics, WorldPoint point, Color color) {
        WorldPoint playerLocation = this.client.getLocalPlayer().getWorldLocation();
        if (point.distanceTo(playerLocation) >= 16) {
            return;
        }
        LocalPoint lp = LocalPoint.fromWorld((Client)this.client, (WorldPoint)point);
        if (lp == null) {
            return;
        }
        Point posOnMinimap = Perspective.localToMinimap((Client)this.client, (LocalPoint)lp);
        if (posOnMinimap == null) {
            return;
        }
        OverlayUtil.renderMinimapRect(this.client, graphics, posOnMinimap, 4, 4, color);
    }
}

