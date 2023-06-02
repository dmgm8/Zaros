/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.regenmeter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.VarPlayer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.regenmeter.RegenMeterConfig;
import net.runelite.client.plugins.regenmeter.RegenMeterPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class RegenMeterOverlay
extends Overlay {
    private static final Color HITPOINTS_COLOR = RegenMeterOverlay.brighter(10159875);
    private static final Color SPECIAL_COLOR = RegenMeterOverlay.brighter(2004400);
    private static final Color OVERLAY_COLOR = new Color(255, 255, 255, 60);
    private static final double DIAMETER = 26.0;
    private static final int OFFSET = 27;
    private final Client client;
    private final RegenMeterPlugin plugin;
    private final RegenMeterConfig config;

    private static Color brighter(int color) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(color >>> 16, color >> 8 & 0xFF, color & 0xFF, hsv);
        return Color.getHSBColor(hsv[0], 1.0f, 1.0f);
    }

    @Inject
    public RegenMeterOverlay(Client client, RegenMeterPlugin plugin, RegenMeterConfig config) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        if (this.config.showHitpoints()) {
            this.renderRegen(g, WidgetInfo.MINIMAP_HEALTH_ORB, this.plugin.getHitpointsPercentage(), HITPOINTS_COLOR);
        }
        if (this.config.showSpecial()) {
            Widget widget;
            if (this.client.getVarpValue(VarPlayer.SPECIAL_ATTACK_ENABLED) == 1 && (widget = this.client.getWidget(WidgetInfo.MINIMAP_SPEC_ORB)) != null && !widget.isHidden()) {
                Rectangle bounds = widget.getBounds();
                g.setColor(OVERLAY_COLOR);
                g.fillOval(bounds.x + 27, bounds.y + (int)((double)(bounds.height / 2) - 13.0), 26, 26);
            }
            this.renderRegen(g, WidgetInfo.MINIMAP_SPEC_ORB, this.plugin.getSpecialPercentage(), SPECIAL_COLOR);
        }
        return null;
    }

    private void renderRegen(Graphics2D g, WidgetInfo widgetInfo, double percent, Color color) {
        Widget widget = this.client.getWidget(widgetInfo);
        if (widget == null || widget.isHidden()) {
            return;
        }
        Rectangle bounds = widget.getBounds();
        Arc2D.Double arc = new Arc2D.Double(bounds.x + 27, (double)bounds.y + ((double)(bounds.height / 2) - 13.0), 26.0, 26.0, 90.0, -360.0 * percent, 0);
        BasicStroke STROKE = new BasicStroke(2.0f, 0, 0);
        g.setStroke(STROKE);
        g.setColor(color);
        g.draw(arc);
    }
}

