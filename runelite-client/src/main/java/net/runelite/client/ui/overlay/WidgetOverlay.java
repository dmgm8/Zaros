/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.ui.overlay;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetOverlay
extends Overlay {
    private static final Logger log = LoggerFactory.getLogger(WidgetOverlay.class);
    protected final Client client;
    private final WidgetInfo widgetInfo;
    private final Rectangle parentBounds = new Rectangle();
    private boolean revalidate;

    public static Collection<WidgetOverlay> createOverlays(OverlayManager overlayManager, Client client) {
        return Arrays.asList(new WidgetOverlay(client, WidgetInfo.RESIZABLE_VIEWPORT_CHATBOX_PARENT, OverlayPosition.DYNAMIC), new WidgetOverlay(client, WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_PARENT, OverlayPosition.DYNAMIC), new WidgetOverlay(client, WidgetInfo.RESIZABLE_MINIMAP_STONES_WIDGET, OverlayPosition.CANVAS_TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_CHATBOX_PARENT, OverlayPosition.DYNAMIC), new WidgetOverlay(client, WidgetInfo.RESIZABLE_MINIMAP_WIDGET, OverlayPosition.CANVAS_TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_TABS1, OverlayPosition.DYNAMIC), new WidgetOverlay(client, WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_TABS2, OverlayPosition.DYNAMIC), new WidgetOverlay(client, WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_INVENTORY_PARENT, OverlayPosition.DYNAMIC), new WidgetOverlay(client, WidgetInfo.FOSSIL_ISLAND_OXYGENBAR, OverlayPosition.TOP_CENTER, OverlayPriority.HIGH), new XpTrackerWidgetOverlay(overlayManager, client, WidgetInfo.EXPERIENCE_TRACKER_WIDGET, OverlayPosition.TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.RAIDS_POINTS_INFOBOX, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.TOB_PARTY_INTERFACE, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.TOB_PARTY_STATS, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.GWD_KC, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.TITHE_FARM, OverlayPosition.TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.PEST_CONTROL_BOAT_INFO, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.PEST_CONTROL_KNIGHT_INFO_CONTAINER, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.PEST_CONTROL_ACTIVITY_SHIELD_INFO_CONTAINER, OverlayPosition.TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.ZEAH_MESS_HALL_COOKING_DISPLAY, OverlayPosition.TOP_LEFT), new PvpKDRWidgetOverlay(client, WidgetInfo.PVP_KILLDEATH_COUNTER, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.SKOTIZO_CONTAINER, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.KOUREND_FAVOUR_OVERLAY, OverlayPosition.TOP_CENTER), new WidgetOverlay(client, WidgetInfo.PYRAMID_PLUNDER_DATA, OverlayPosition.TOP_CENTER), new WidgetOverlay(client, WidgetInfo.LMS_INFO, OverlayPosition.TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.LMS_KDA, OverlayPosition.TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.GAUNTLET_TIMER_CONTAINER, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.HALLOWED_SEPULCHRE_TIMER_CONTAINER, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.HEALTH_OVERLAY_BAR, OverlayPosition.TOP_CENTER, OverlayPriority.HIGH), new WidgetOverlay(client, WidgetInfo.TOB_HEALTH_BAR, OverlayPosition.TOP_CENTER), new WidgetOverlay(client, WidgetInfo.NIGHTMARE_PILLAR_HEALTH, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.VOLCANIC_MINE_VENTS_INFOBOX_GROUP, OverlayPosition.BOTTOM_RIGHT), new WidgetOverlay(client, WidgetInfo.VOLCANIC_MINE_STABILITY_INFOBOX_GROUP, OverlayPosition.BOTTOM_LEFT), new WidgetOverlay(client, WidgetInfo.MULTICOMBAT_FIXED, OverlayPosition.BOTTOM_RIGHT), new WidgetOverlay(client, WidgetInfo.MULTICOMBAT_RESIZABLE_MODERN, OverlayPosition.CANVAS_TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.MULTICOMBAT_RESIZABLE_CLASSIC, OverlayPosition.CANVAS_TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.TEMPOROSS_STATUS_INDICATOR, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.BA_HEAL_TEAMMATES, OverlayPosition.BOTTOM_LEFT), new WidgetOverlay(client, WidgetInfo.BA_TEAM, OverlayPosition.TOP_RIGHT), new WidgetOverlay(client, WidgetInfo.PVP_WILDERNESS_SKULL_CONTAINER, OverlayPosition.DYNAMIC), new WidgetOverlay(client, WidgetInfo.TOA_PARTY_LAYER, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.TOA_RAID_LAYER, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.TOURNAMENT_OVERLAY, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.WILDERNESS_BOSS_DAMAGERS, OverlayPosition.TOP_LEFT), new WidgetOverlay(client, WidgetInfo.GWD_DAMAGERS, OverlayPosition.TOP_LEFT));
    }

    private WidgetOverlay(Client client, WidgetInfo widgetInfo, OverlayPosition overlayPosition) {
        this(client, widgetInfo, overlayPosition, OverlayPriority.HIGHEST);
    }

    private WidgetOverlay(Client client, WidgetInfo widgetInfo, OverlayPosition overlayPosition, OverlayPriority overlayPriority) {
        this.client = client;
        this.widgetInfo = widgetInfo;
        this.setPriority(overlayPriority);
        this.setLayer(OverlayLayer.UNDER_WIDGETS);
        this.setPosition(overlayPosition);
        this.setMovable(true);
        this.setSnappable(true);
    }

    @Override
    public String getName() {
        return Objects.toString((Object)this.widgetInfo);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Widget widget = this.client.getWidget(this.widgetInfo);
        Rectangle parent = this.getParentBounds(widget);
        if (parent.isEmpty()) {
            return null;
        }
        assert (widget != null);
        Rectangle bounds = this.getBounds();
        if (this.widgetInfo == WidgetInfo.GWD_DAMAGERS) {
            for (Widget child : widget.getStaticChildren()) {
                if (!child.isHidden()) continue;
                return null;
            }
        }
        if (this.getPreferredLocation() != null || this.getPreferredPosition() != null) {
            widget.setForcedPosition(bounds.x - parent.x, bounds.y - parent.y);
        } else {
            if (this.revalidate) {
                this.revalidate = false;
                log.debug("Revalidating {}", (Object)this.widgetInfo);
                widget.setForcedPosition(-1, -1);
                widget.revalidate();
            }
            Rectangle widgetBounds = widget.getBounds();
            bounds.setBounds(widgetBounds.x, widgetBounds.y, widgetBounds.width, widgetBounds.height);
        }
        return new Dimension(widget.getWidth(), widget.getHeight());
    }

    private Rectangle getParentBounds(Widget widget) {
        if (widget == null || widget.isHidden()) {
            this.parentBounds.setBounds(new Rectangle());
            return this.parentBounds;
        }
        Widget parent = widget.getParent();
        Rectangle bounds = parent == null ? new Rectangle(this.client.getRealDimensions()) : parent.getBounds();
        this.parentBounds.setBounds(bounds);
        return bounds;
    }

    @Override
    public Rectangle getParentBounds() {
        if (!this.client.isClientThread()) {
            return this.parentBounds;
        }
        Widget widget = this.client.getWidget(this.widgetInfo);
        return this.getParentBounds(widget);
    }

    @Override
    public void revalidate() {
        this.revalidate = true;
    }

    private static class PvpKDRWidgetOverlay
    extends WidgetOverlay {
        private PvpKDRWidgetOverlay(Client client, WidgetInfo widgetInfo, OverlayPosition overlayPosition) {
            super(client, widgetInfo, overlayPosition);
        }

        @Override
        public Dimension render(Graphics2D graphics) {
            if (this.client.getVarbitValue(4143) == 1) {
                return super.render(graphics);
            }
            return null;
        }
    }

    private static class XpTrackerWidgetOverlay
    extends WidgetOverlay {
        private final OverlayManager overlayManager;

        private XpTrackerWidgetOverlay(OverlayManager overlayManager, Client client, WidgetInfo widgetInfo, OverlayPosition overlayPosition) {
            super(client, widgetInfo, overlayPosition);
            this.overlayManager = overlayManager;
        }

        @Override
        public Dimension render(Graphics2D graphics) {
            if (this.client.getVarbitValue(4697) == 30 && this.client.getVarbitValue(4698) == 0) {
                return null;
            }
            return super.render(graphics);
        }

        @Override
        public OverlayPosition getPosition() {
            OverlayPosition position;
            if (!this.client.isClientThread()) {
                return super.getPosition();
            }
            switch (this.client.getVarbitValue(4692)) {
                default: {
                    position = OverlayPosition.TOP_RIGHT;
                    break;
                }
                case 1: {
                    position = OverlayPosition.TOP_CENTER;
                    break;
                }
                case 2: {
                    position = OverlayPosition.TOP_LEFT;
                }
            }
            if (position != super.getPosition()) {
                log.debug("Xp tracker moved position");
                this.setPosition(position);
                this.overlayManager.rebuildOverlayLayers();
            }
            return position;
        }
    }
}

