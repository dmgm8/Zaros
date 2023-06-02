/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.DecorativeObject
 *  net.runelite.api.GameObject
 *  net.runelite.api.GroundObject
 *  net.runelite.api.ObjectComposition
 *  net.runelite.api.TileObject
 *  net.runelite.api.WallObject
 */
package net.runelite.client.plugins.objectindicators;

import com.google.common.base.Strings;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.client.plugins.objectindicators.ColorTileObject;
import net.runelite.client.plugins.objectindicators.ObjectIndicatorsConfig;
import net.runelite.client.plugins.objectindicators.ObjectIndicatorsPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ColorUtil;

class ObjectIndicatorsOverlay
extends Overlay {
    private final Client client;
    private final ObjectIndicatorsConfig config;
    private final ObjectIndicatorsPlugin plugin;
    private final ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private ObjectIndicatorsOverlay(Client client, ObjectIndicatorsConfig config, ObjectIndicatorsPlugin plugin, ModelOutlineRenderer modelOutlineRenderer) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.LOW);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        BasicStroke stroke = new BasicStroke((float)this.config.borderWidth());
        for (ColorTileObject colorTileObject : this.plugin.getObjects()) {
            Polygon tilePoly;
            Shape clickbox;
            ObjectComposition composition;
            TileObject object = colorTileObject.getTileObject();
            Color color = colorTileObject.getColor();
            if (object.getPlane() != this.client.getPlane() || (composition = colorTileObject.getComposition()).getImpostorIds() != null && ((composition = composition.getImpostor()) == null || Strings.isNullOrEmpty((String)composition.getName()) || "null".equals(composition.getName()) || !composition.getName().equals(colorTileObject.getName()))) continue;
            if (color == null || !this.config.rememberObjectColors()) {
                color = this.config.markerColor();
            }
            if (this.config.highlightHull()) {
                this.renderConvexHull(graphics, object, color, stroke);
            }
            if (this.config.highlightOutline()) {
                this.modelOutlineRenderer.drawOutline(object, (int)this.config.borderWidth(), color, this.config.outlineFeather());
            }
            if (this.config.highlightClickbox() && (clickbox = object.getClickbox()) != null) {
                Color clickBoxColor = ColorUtil.colorWithAlpha(color, color.getAlpha() / 12);
                OverlayUtil.renderPolygon(graphics, clickbox, color, clickBoxColor, stroke);
            }
            if (!this.config.highlightTile() || (tilePoly = object.getCanvasTilePoly()) == null) continue;
            Color tileColor = ColorUtil.colorWithAlpha(color, color.getAlpha() / 12);
            OverlayUtil.renderPolygon(graphics, tilePoly, color, tileColor, stroke);
        }
        return null;
    }

    private void renderConvexHull(Graphics2D graphics, TileObject object, Color color, Stroke stroke) {
        Shape polygon;
        Shape polygon2 = null;
        if (object instanceof GameObject) {
            polygon = ((GameObject)object).getConvexHull();
        } else if (object instanceof WallObject) {
            polygon = ((WallObject)object).getConvexHull();
            polygon2 = ((WallObject)object).getConvexHull2();
        } else if (object instanceof DecorativeObject) {
            polygon = ((DecorativeObject)object).getConvexHull();
            polygon2 = ((DecorativeObject)object).getConvexHull2();
        } else {
            polygon = object instanceof GroundObject ? ((GroundObject)object).getConvexHull() : object.getCanvasTilePoly();
        }
        if (polygon != null) {
            OverlayUtil.renderPolygon(graphics, polygon, color, stroke);
        }
        if (polygon2 != null) {
            OverlayUtil.renderPolygon(graphics, polygon2, color, stroke);
        }
    }
}

