/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.Perspective
 *  net.runelite.api.Point
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.woodcutting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Instant;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.woodcutting.Axe;
import net.runelite.client.plugins.woodcutting.TreeRespawn;
import net.runelite.client.plugins.woodcutting.WoodcuttingConfig;
import net.runelite.client.plugins.woodcutting.WoodcuttingPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

class WoodcuttingTreesOverlay
extends Overlay {
    private final Client client;
    private final WoodcuttingConfig config;
    private final ItemManager itemManager;
    private final WoodcuttingPlugin plugin;

    @Inject
    private WoodcuttingTreesOverlay(Client client, WoodcuttingConfig config, ItemManager itemManager, WoodcuttingPlugin plugin) {
        this.client = client;
        this.config = config;
        this.itemManager = itemManager;
        this.plugin = plugin;
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPosition(OverlayPosition.DYNAMIC);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        this.renderAxes(graphics);
        this.renderTimers(graphics);
        return null;
    }

    private void renderAxes(Graphics2D graphics) {
        if (this.plugin.getSession() == null || !this.config.showRedwoodTrees()) {
            return;
        }
        Axe axe = this.plugin.getAxe();
        if (axe == null) {
            return;
        }
        for (GameObject treeObject : this.plugin.getTreeObjects()) {
            if (treeObject.getWorldLocation().distanceTo(this.client.getLocalPlayer().getWorldLocation()) > 12) continue;
            OverlayUtil.renderImageLocation(this.client, graphics, treeObject.getLocalLocation(), this.itemManager.getImage(axe.getItemId()), 120);
        }
    }

    private void renderTimers(Graphics2D graphics) {
        List<TreeRespawn> respawns = this.plugin.getRespawns();
        if (respawns.isEmpty() || !this.config.showRespawnTimers()) {
            return;
        }
        Instant now = Instant.now();
        for (TreeRespawn treeRespawn : respawns) {
            LocalPoint minLocation = LocalPoint.fromWorld((Client)this.client, (WorldPoint)treeRespawn.getWorldLocation());
            if (minLocation == null) continue;
            LocalPoint centeredLocation = new LocalPoint(minLocation.getX() + treeRespawn.getLenX() * 64, minLocation.getY() + treeRespawn.getLenY() * 64);
            float percent = (float)(now.toEpochMilli() - treeRespawn.getStartTime().toEpochMilli()) / (float)treeRespawn.getRespawnTime();
            Point point = Perspective.localToCanvas((Client)this.client, (LocalPoint)centeredLocation, (int)this.client.getPlane());
            if (point == null || percent > 1.0f) continue;
            ProgressPieComponent ppc = new ProgressPieComponent();
            ppc.setBorderColor(Color.ORANGE);
            ppc.setFill(Color.YELLOW);
            ppc.setPosition(point);
            ppc.setProgress(percent);
            ppc.render(graphics);
        }
    }
}

