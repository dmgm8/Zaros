/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Perspective
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.spawndumper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.spawndumper.NPCSpawn;
import net.runelite.client.plugins.spawndumper.SpawnDumperPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class SpawnDumperNPCOverlay
extends Overlay {
    private final Client client;
    private final SpawnDumperPlugin plugin;

    @Inject
    private SpawnDumperNPCOverlay(Client client, SpawnDumperPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (this.plugin.getSelectedNPCIndex() == -1) {
            return null;
        }
        NPCSpawn spawn = this.plugin.getSpawns().get(this.plugin.getSelectedNPCIndex());
        if (spawn == null) {
            return null;
        }
        Set<WorldPoint> points = spawn.getPoints();
        for (WorldPoint point : points) {
            this.renderTile(graphics, point, Color.BLUE);
        }
        int spawnX = spawn.getMinX() + (int)Math.ceil((double)(spawn.getMaxX() - spawn.getMinX()) / 2.0);
        int spawnY = spawn.getMinY() + (int)Math.ceil((double)(spawn.getMaxY() - spawn.getMinY()) / 2.0);
        this.renderTile(graphics, new WorldPoint(spawnX, spawnY, spawn.getMinZ()), Color.RED);
        return null;
    }

    private void renderTile(Graphics2D graphics, WorldPoint dest, Color color) {
        if (dest == null) {
            return;
        }
        LocalPoint localPoint = LocalPoint.fromWorld((Client)this.client, (WorldPoint)dest);
        if (localPoint == null) {
            return;
        }
        Polygon poly = Perspective.getCanvasTilePoly((Client)this.client, (LocalPoint)localPoint);
        if (poly == null) {
            return;
        }
        OverlayUtil.renderPolygon(graphics, poly, color);
    }
}

