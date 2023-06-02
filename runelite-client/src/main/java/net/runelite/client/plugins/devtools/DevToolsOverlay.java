/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Actor
 *  net.runelite.api.Animation
 *  net.runelite.api.Client
 *  net.runelite.api.DecorativeObject
 *  net.runelite.api.DynamicObject
 *  net.runelite.api.GameObject
 *  net.runelite.api.GraphicsObject
 *  net.runelite.api.ItemLayer
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.Perspective
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 *  net.runelite.api.Projectile
 *  net.runelite.api.Renderable
 *  net.runelite.api.Scene
 *  net.runelite.api.Tile
 *  net.runelite.api.TileItem
 *  net.runelite.api.TileObject
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Actor;
import net.runelite.api.Animation;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.GraphicsObject;
import net.runelite.api.ItemLayer;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.plugins.devtools.MovementFlag;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

@Singleton
class DevToolsOverlay
extends Overlay {
    private static final Font FONT = FontManager.getRunescapeFont().deriveFont(1, 16.0f);
    private static final Color RED = new Color(221, 44, 0);
    private static final Color GREEN = new Color(0, 200, 83);
    private static final Color ORANGE = new Color(255, 109, 0);
    private static final Color YELLOW = new Color(255, 214, 0);
    private static final Color CYAN = new Color(0, 184, 212);
    private static final Color BLUE = new Color(41, 98, 255);
    private static final Color DEEP_PURPLE = new Color(98, 0, 234);
    private static final Color PURPLE = new Color(170, 0, 255);
    private static final Color GRAY = new Color(158, 158, 158);
    private static final int MAX_DISTANCE = 2400;
    private final Client client;
    private final DevToolsPlugin plugin;
    private final TooltipManager toolTipManager;

    @Inject
    private DevToolsOverlay(Client client, DevToolsPlugin plugin, TooltipManager toolTipManager) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.setPriority(OverlayPriority.HIGHEST);
        this.client = client;
        this.plugin = plugin;
        this.toolTipManager = toolTipManager;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        graphics.setFont(FONT);
        if (this.plugin.getPlayers().isActive()) {
            this.renderPlayers(graphics);
        }
        if (this.plugin.getNpcs().isActive()) {
            this.renderNpcs(graphics);
        }
        if (this.plugin.getGroundItems().isActive() || this.plugin.getGroundObjects().isActive() || this.plugin.getGameObjects().isActive() || this.plugin.getWalls().isActive() || this.plugin.getDecorations().isActive() || this.plugin.getTileLocation().isActive() || this.plugin.getMovementFlags().isActive()) {
            this.renderTileObjects(graphics);
        }
        if (this.plugin.getProjectiles().isActive()) {
            this.renderProjectiles(graphics);
        }
        if (this.plugin.getGraphicsObjects().isActive()) {
            this.renderGraphicsObjects(graphics);
        }
        if (this.plugin.getRoofs().isActive()) {
            this.renderRoofs(graphics);
        }
        return null;
    }

    private void renderRoofs(Graphics2D graphics) {
        Scene scene = this.client.getScene();
        Tile[][][] tiles = scene.getTiles();
        byte[][][] settings = this.client.getTileSettings();
        int z = this.client.getPlane();
        String text = "R";
        for (int x = 0; x < 104; ++x) {
            for (int y = 0; y < 104; ++y) {
                Point loc;
                byte flag;
                Tile tile = tiles[z][x][y];
                if (tile == null || ((flag = settings[z][x][y]) & 4) == 0 || (loc = Perspective.getCanvasTextLocation((Client)this.client, (Graphics2D)graphics, (LocalPoint)tile.getLocalLocation(), (String)text, (int)z)) == null) continue;
                OverlayUtil.renderTextLocation(graphics, loc, text, Color.RED);
            }
        }
    }

    private void renderPlayers(Graphics2D graphics) {
        List players = this.client.getPlayers();
        Player local = this.client.getLocalPlayer();
        for (Player p : players) {
            if (p == local) continue;
            String text = p.getName() + " (A: " + p.getAnimation() + ") (P: " + p.getPoseAnimation() + ") (G: " + p.getGraphic() + ")";
            OverlayUtil.renderActorOverlay(graphics, (Actor)p, text, BLUE);
        }
        String text = local.getName() + " (A: " + local.getAnimation() + ") (P: " + local.getPoseAnimation() + ") (G: " + local.getGraphic() + ")";
        OverlayUtil.renderActorOverlay(graphics, (Actor)local, text, CYAN);
    }

    private void renderNpcs(Graphics2D graphics) {
        List npcs = this.client.getNpcs();
        for (NPC npc : npcs) {
            Color color;
            NPCComposition composition = npc.getComposition();
            Color color2 = color = composition.getCombatLevel() > 1 ? YELLOW : ORANGE;
            if (composition.getConfigs() != null) {
                NPCComposition transformedComposition = composition.transform();
                if (transformedComposition == null) {
                    color = GRAY;
                } else {
                    composition = transformedComposition;
                }
            }
            String text = composition.getName() + " (ID:" + composition.getId() + ") (A: " + npc.getAnimation() + ") (P: " + npc.getPoseAnimation() + ") (G: " + npc.getGraphic() + ")";
            OverlayUtil.renderActorOverlay(graphics, (Actor)npc, text, color);
        }
    }

    private void renderTileObjects(Graphics2D graphics) {
        Scene scene = this.client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = this.client.getPlane();
        for (int x = 0; x < 104; ++x) {
            for (int y = 0; y < 104; ++y) {
                Player player;
                Tile tile = tiles[z][x][y];
                if (tile == null || (player = this.client.getLocalPlayer()) == null) continue;
                if (this.plugin.getGroundItems().isActive()) {
                    this.renderGroundItems(graphics, tile, player);
                }
                if (this.plugin.getGroundObjects().isActive()) {
                    this.renderTileObject(graphics, (TileObject)tile.getGroundObject(), player, PURPLE);
                }
                if (this.plugin.getGameObjects().isActive()) {
                    this.renderGameObjects(graphics, tile, player);
                }
                if (this.plugin.getWalls().isActive()) {
                    this.renderTileObject(graphics, (TileObject)tile.getWallObject(), player, GRAY);
                }
                if (this.plugin.getDecorations().isActive()) {
                    this.renderDecorObject(graphics, tile, player);
                }
                if (this.plugin.getTileLocation().isActive()) {
                    this.renderTileTooltip(graphics, tile);
                }
                if (!this.plugin.getMovementFlags().isActive()) continue;
                this.renderMovementInfo(graphics, tile);
            }
        }
    }

    private void renderTileTooltip(Graphics2D graphics, Tile tile) {
        LocalPoint tileLocalLocation = tile.getLocalLocation();
        Polygon poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)tileLocalLocation);
        if (poly != null && poly.contains(this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY())) {
            WorldPoint worldLocation = tile.getWorldLocation();
            String tooltip = String.format("World location: %d, %d, %d</br>Region ID: %d location: %d, %d", worldLocation.getX(), worldLocation.getY(), worldLocation.getPlane(), this.client.isInInstancedRegion() ? WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)tileLocalLocation).getRegionID() : worldLocation.getRegionID(), worldLocation.getRegionX(), worldLocation.getRegionY());
            this.toolTipManager.add(new Tooltip(tooltip));
            OverlayUtil.renderPolygon(graphics, poly, GREEN);
        }
    }

    private void renderMovementInfo(Graphics2D graphics, Tile tile) {
        Polygon poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)tile.getLocalLocation());
        if (poly == null || !poly.contains(this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY())) {
            return;
        }
        if (this.client.getCollisionMaps() != null) {
            int[][] flags = this.client.getCollisionMaps()[this.client.getPlane()].getFlags();
            int data = flags[tile.getSceneLocation().getX()][tile.getSceneLocation().getY()];
            Set<MovementFlag> movementFlags = MovementFlag.getSetFlags(data);
            if (movementFlags.isEmpty()) {
                this.toolTipManager.add(new Tooltip("No movement flags"));
            } else {
                movementFlags.forEach(flag -> this.toolTipManager.add(new Tooltip(flag.toString())));
            }
            OverlayUtil.renderPolygon(graphics, poly, GREEN);
        }
    }

    private void renderGroundItems(Graphics2D graphics, Tile tile, Player player) {
        ItemLayer itemLayer = tile.getItemLayer();
        if (itemLayer != null && player.getLocalLocation().distanceTo(itemLayer.getLocalLocation()) <= 2400) {
            Renderable current = itemLayer.getBottom();
            while (current instanceof TileItem) {
                TileItem item = (TileItem)current;
                OverlayUtil.renderTileOverlay(graphics, (TileObject)itemLayer, "ID: " + item.getId() + " Qty:" + item.getQuantity(), RED);
                current = current.getNext();
            }
        }
    }

    private void renderGameObjects(Graphics2D graphics, Tile tile, Player player) {
        GameObject[] gameObjects = tile.getGameObjects();
        if (gameObjects != null) {
            for (GameObject gameObject : gameObjects) {
                Animation animation;
                if (gameObject == null || !gameObject.getSceneMinLocation().equals((Object)tile.getSceneLocation()) || player.getLocalLocation().distanceTo(gameObject.getLocalLocation()) > 2400) continue;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("ID: ").append(gameObject.getId());
                if (gameObject.getRenderable() instanceof DynamicObject && (animation = ((DynamicObject)gameObject.getRenderable()).getAnimation()) != null) {
                    stringBuilder.append(" A: ").append(animation.getId());
                }
                OverlayUtil.renderTileOverlay(graphics, (TileObject)gameObject, stringBuilder.toString(), GREEN);
            }
        }
    }

    private void renderTileObject(Graphics2D graphics, TileObject tileObject, Player player, Color color) {
        if (tileObject != null && player.getLocalLocation().distanceTo(tileObject.getLocalLocation()) <= 2400) {
            OverlayUtil.renderTileOverlay(graphics, tileObject, "ID: " + tileObject.getId(), color);
        }
    }

    private void renderDecorObject(Graphics2D graphics, Tile tile, Player player) {
        DecorativeObject decorObject = tile.getDecorativeObject();
        if (decorObject != null) {
            Shape p;
            if (player.getLocalLocation().distanceTo(decorObject.getLocalLocation()) <= 2400) {
                OverlayUtil.renderTileOverlay(graphics, (TileObject)decorObject, "ID: " + decorObject.getId(), DEEP_PURPLE);
            }
            if ((p = decorObject.getConvexHull()) != null) {
                graphics.draw(p);
            }
            if ((p = decorObject.getConvexHull2()) != null) {
                graphics.draw(p);
            }
        }
    }

    private void renderProjectiles(Graphics2D graphics) {
        for (Projectile projectile : this.client.getProjectiles()) {
            int y;
            int projectileId = projectile.getId();
            String text = "(ID: " + projectileId + ")";
            int x = (int)projectile.getX();
            LocalPoint projectilePoint = new LocalPoint(x, y = (int)projectile.getY());
            Point textLocation = Perspective.getCanvasTextLocation((Client)this.client, (Graphics2D)graphics, (LocalPoint)projectilePoint, (String)text, (int)0);
            if (textLocation == null) continue;
            OverlayUtil.renderTextLocation(graphics, textLocation, text, Color.RED);
        }
    }

    private void renderGraphicsObjects(Graphics2D graphics) {
        for (GraphicsObject graphicsObject : this.client.getGraphicsObjects()) {
            String infoString;
            Point textLocation;
            LocalPoint lp = graphicsObject.getLocation();
            Polygon poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)lp);
            if (poly != null) {
                OverlayUtil.renderPolygon(graphics, poly, Color.MAGENTA);
            }
            if ((textLocation = Perspective.getCanvasTextLocation((Client)this.client, (Graphics2D)graphics, (LocalPoint)lp, (String)(infoString = "(ID: " + graphicsObject.getId() + ")"), (int)0)) == null) continue;
            OverlayUtil.renderTextLocation(graphics, textLocation, infoString, Color.WHITE);
        }
    }
}

