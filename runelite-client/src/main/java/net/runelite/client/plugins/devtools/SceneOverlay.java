/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.NPC
 *  net.runelite.api.Perspective
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldArea
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.devtools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.List;
import java.util.stream.Stream;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class SceneOverlay
extends Overlay {
    private static final Color MAP_SQUARE_COLOR = Color.GREEN;
    private static final Color CHUNK_BORDER_COLOR = Color.BLUE;
    private static final Color LOCAL_VALID_MOVEMENT_COLOR = new Color(141, 220, 26);
    private static final Color VALID_MOVEMENT_COLOR = new Color(73, 122, 18);
    private static final Color LINE_OF_SIGHT_COLOR = new Color(204, 42, 219);
    private static final Color INTERACTING_COLOR = Color.CYAN;
    private static final int LOCAL_TILE_SIZE = 128;
    private static final int CHUNK_SIZE = 8;
    private static final int MAP_SQUARE_SIZE = 64;
    private static final int CULL_CHUNK_BORDERS_RANGE = 16;
    private static final int STROKE_WIDTH = 4;
    private static final int CULL_LINE_OF_SIGHT_RANGE = 10;
    private static final int INTERACTING_SHIFT = -16;
    private static final Polygon ARROW_HEAD = new Polygon(new int[]{0, -3, 3}, new int[]{0, -5, -5}, 3);
    private final Client client;
    private final DevToolsPlugin plugin;

    @Inject
    public SceneOverlay(Client client, DevToolsPlugin plugin) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (this.plugin.getChunkBorders().isActive()) {
            this.renderChunkBorders(graphics);
        }
        if (this.plugin.getMapSquares().isActive()) {
            this.renderMapSquares(graphics);
        }
        if (this.plugin.getLineOfSight().isActive()) {
            this.renderLineOfSight(graphics);
        }
        if (this.plugin.getValidMovement().isActive()) {
            this.renderValidMovement(graphics);
        }
        if (this.plugin.getInteracting().isActive()) {
            this.renderInteracting(graphics);
        }
        return null;
    }

    private void renderChunkBorders(Graphics2D graphics) {
        Point p;
        boolean first;
        LocalPoint lp2;
        LocalPoint lp1;
        WorldPoint wp = this.client.getLocalPlayer().getWorldLocation();
        int startX = (wp.getX() - 16 + 8 - 1) / 8 * 8;
        int startY = (wp.getY() - 16 + 8 - 1) / 8 * 8;
        int endX = (wp.getX() + 16) / 8 * 8;
        int endY = (wp.getY() + 16) / 8 * 8;
        graphics.setStroke(new BasicStroke(4.0f));
        graphics.setColor(CHUNK_BORDER_COLOR);
        GeneralPath path = new GeneralPath();
        for (int x = startX; x <= endX; x += 8) {
            lp1 = LocalPoint.fromWorld((Client)this.client, (int)x, (int)(wp.getY() - 16));
            lp2 = LocalPoint.fromWorld((Client)this.client, (int)x, (int)(wp.getY() + 16));
            first = true;
            for (int y = lp1.getY(); y <= lp2.getY(); y += 128) {
                p = Perspective.localToCanvas((Client)this.client, (LocalPoint)new LocalPoint(lp1.getX() - 64, y - 64), (int)this.client.getPlane());
                if (p == null) continue;
                if (first) {
                    path.moveTo(p.getX(), p.getY());
                    first = false;
                    continue;
                }
                path.lineTo(p.getX(), p.getY());
            }
        }
        for (int y = startY; y <= endY; y += 8) {
            lp1 = LocalPoint.fromWorld((Client)this.client, (int)(wp.getX() - 16), (int)y);
            lp2 = LocalPoint.fromWorld((Client)this.client, (int)(wp.getX() + 16), (int)y);
            first = true;
            for (int x = lp1.getX(); x <= lp2.getX(); x += 128) {
                p = Perspective.localToCanvas((Client)this.client, (LocalPoint)new LocalPoint(x - 64, lp1.getY() - 64), (int)this.client.getPlane());
                if (p == null) continue;
                if (first) {
                    path.moveTo(p.getX(), p.getY());
                    first = false;
                    continue;
                }
                path.lineTo(p.getX(), p.getY());
            }
        }
        graphics.draw(path);
    }

    private void renderMapSquares(Graphics2D graphics) {
        Point p;
        boolean first;
        LocalPoint lp2;
        LocalPoint lp1;
        WorldPoint wp = this.client.getLocalPlayer().getWorldLocation();
        int startX = (wp.getX() - 16 + 64 - 1) / 64 * 64;
        int startY = (wp.getY() - 16 + 64 - 1) / 64 * 64;
        int endX = (wp.getX() + 16) / 64 * 64;
        int endY = (wp.getY() + 16) / 64 * 64;
        graphics.setStroke(new BasicStroke(4.0f));
        graphics.setColor(MAP_SQUARE_COLOR);
        GeneralPath path = new GeneralPath();
        for (int x = startX; x <= endX; x += 64) {
            lp1 = LocalPoint.fromWorld((Client)this.client, (int)x, (int)(wp.getY() - 16));
            lp2 = LocalPoint.fromWorld((Client)this.client, (int)x, (int)(wp.getY() + 16));
            first = true;
            for (int y = lp1.getY(); y <= lp2.getY(); y += 128) {
                p = Perspective.localToCanvas((Client)this.client, (LocalPoint)new LocalPoint(lp1.getX() - 64, y - 64), (int)this.client.getPlane());
                if (p == null) continue;
                if (first) {
                    path.moveTo(p.getX(), p.getY());
                    first = false;
                    continue;
                }
                path.lineTo(p.getX(), p.getY());
            }
        }
        for (int y = startY; y <= endY; y += 64) {
            lp1 = LocalPoint.fromWorld((Client)this.client, (int)(wp.getX() - 16), (int)y);
            lp2 = LocalPoint.fromWorld((Client)this.client, (int)(wp.getX() + 16), (int)y);
            first = true;
            for (int x = lp1.getX(); x <= lp2.getX(); x += 128) {
                p = Perspective.localToCanvas((Client)this.client, (LocalPoint)new LocalPoint(x - 64, lp1.getY() - 64), (int)this.client.getPlane());
                if (p == null) continue;
                if (first) {
                    path.moveTo(p.getX(), p.getY());
                    first = false;
                    continue;
                }
                path.lineTo(p.getX(), p.getY());
            }
        }
        graphics.draw(path);
    }

    private void renderTileIfValidForMovement(Graphics2D graphics, Actor actor, int dx, int dy) {
        WorldArea area = actor.getWorldArea();
        if (area == null) {
            return;
        }
        if (area.canTravelInDirection(this.client, dx, dy)) {
            LocalPoint lp = actor.getLocalLocation();
            if (lp == null) {
                return;
            }
            Polygon poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)(lp = new LocalPoint(lp.getX() + dx * 128 + dx * 128 * (area.getWidth() - 1) / 2, lp.getY() + dy * 128 + dy * 128 * (area.getHeight() - 1) / 2)));
            if (poly == null) {
                return;
            }
            if (actor == this.client.getLocalPlayer()) {
                OverlayUtil.renderPolygon(graphics, poly, LOCAL_VALID_MOVEMENT_COLOR);
            } else {
                OverlayUtil.renderPolygon(graphics, poly, VALID_MOVEMENT_COLOR);
            }
        }
    }

    private void renderValidMovement(Graphics2D graphics) {
        Player player = this.client.getLocalPlayer();
        List npcs = this.client.getNpcs();
        for (NPC npc : npcs) {
            if (player.getInteracting() != npc && npc.getInteracting() != player) continue;
            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    if (dx == 0 && dy == 0) continue;
                    this.renderTileIfValidForMovement(graphics, (Actor)npc, dx, dy);
                }
            }
        }
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx == 0 && dy == 0) continue;
                this.renderTileIfValidForMovement(graphics, (Actor)player, dx, dy);
            }
        }
    }

    private void renderTileIfHasLineOfSight(Graphics2D graphics, WorldArea start, int targetX, int targetY) {
        WorldPoint targetLocation = new WorldPoint(targetX, targetY, start.getPlane());
        if (start.hasLineOfSightTo(this.client, targetLocation)) {
            LocalPoint lp = LocalPoint.fromWorld((Client)this.client, (WorldPoint)targetLocation);
            if (lp == null) {
                return;
            }
            Polygon poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)lp);
            if (poly == null) {
                return;
            }
            OverlayUtil.renderPolygon(graphics, poly, LINE_OF_SIGHT_COLOR);
        }
    }

    private void renderLineOfSight(Graphics2D graphics) {
        WorldArea area = this.client.getLocalPlayer().getWorldArea();
        for (int x = area.getX() - 10; x <= area.getX() + 10; ++x) {
            for (int y = area.getY() - 10; y <= area.getY() + 10; ++y) {
                if (x == area.getX() && y == area.getY()) continue;
                this.renderTileIfHasLineOfSight(graphics, area, x, y);
            }
        }
    }

    private void renderInteracting(Graphics2D graphics) {
        Stream.concat(this.client.getPlayers().stream(), this.client.getNpcs().stream()).forEach(fa -> {
            Actor ta = fa.getInteracting();
            if (ta == null) {
                return;
            }
            LocalPoint fl = fa.getLocalLocation();
            Point fs = Perspective.localToCanvas((Client)this.client, (LocalPoint)fl, (int)this.client.getPlane(), (int)(fa.getLogicalHeight() / 2));
            if (fs == null) {
                return;
            }
            int fsx = fs.getX();
            int fsy = fs.getY() - -16;
            LocalPoint tl = ta.getLocalLocation();
            Point ts = Perspective.localToCanvas((Client)this.client, (LocalPoint)tl, (int)this.client.getPlane(), (int)(ta.getLogicalHeight() / 2));
            if (ts == null) {
                return;
            }
            int tsx = ts.getX();
            int tsy = ts.getY() - -16;
            graphics.setColor(INTERACTING_COLOR);
            graphics.drawLine(fsx, fsy, tsx, tsy);
            AffineTransform t = new AffineTransform();
            t.translate(tsx, tsy);
            t.rotate(tsx - fsx, tsy - fsy);
            t.rotate(-1.5707963267948966);
            AffineTransform ot = graphics.getTransform();
            graphics.setTransform(t);
            graphics.fill(ARROW_HEAD);
            graphics.setTransform(ot);
        });
    }
}

