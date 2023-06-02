/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.api.widgets.WidgetItem
 */
package net.runelite.client.plugins.screenmarkers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.screenmarkers.ScreenMarkerPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

class ScreenMarkerWidgetHighlightOverlay
extends Overlay {
    private final ScreenMarkerPlugin plugin;
    private final Client client;

    @Inject
    private ScreenMarkerWidgetHighlightOverlay(ScreenMarkerPlugin plugin, Client client) {
        this.plugin = plugin;
        this.client = client;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.setPriority(OverlayPriority.HIGH);
        this.setMovable(true);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        int componentId;
        if (!this.plugin.isCreatingScreenMarker() || this.plugin.isDrawingScreenMarker()) {
            return null;
        }
        MenuEntry[] menuEntries = this.client.getMenuEntries();
        if (this.client.isMenuOpen() || menuEntries.length == 0) {
            this.plugin.setSelectedWidgetBounds(null);
            return null;
        }
        MenuEntry menuEntry = menuEntries[menuEntries.length - 1];
        int childIdx = menuEntry.getParam0();
        int widgetId = menuEntry.getParam1();
        int groupId = WidgetInfo.TO_GROUP((int)widgetId);
        Widget widget = this.client.getWidget(groupId, componentId = WidgetInfo.TO_CHILD((int)widgetId));
        if (widget == null) {
            this.plugin.setSelectedWidgetBounds(null);
            return null;
        }
        Rectangle bounds = null;
        if (childIdx > -1) {
            if (widget.getType() == 2) {
                WidgetItem widgetItem = widget.getWidgetItem(childIdx);
                if (widgetItem != null) {
                    bounds = widgetItem.getCanvasBounds();
                }
            } else {
                Widget child = widget.getChild(childIdx);
                if (child != null) {
                    bounds = child.getBounds();
                }
            }
        } else {
            bounds = widget.getBounds();
        }
        if (bounds == null) {
            this.plugin.setSelectedWidgetBounds(null);
            return null;
        }
        ScreenMarkerWidgetHighlightOverlay.drawHighlight(graphics, bounds);
        this.plugin.setSelectedWidgetBounds(bounds);
        return null;
    }

    private static void drawHighlight(Graphics2D graphics, Rectangle bounds) {
        graphics.setColor(Color.GREEN);
        graphics.draw(bounds);
    }
}

