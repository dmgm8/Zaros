/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class LocationOverlay
extends OverlayPanel {
    private final Client client;
    private final DevToolsPlugin plugin;

    @Inject
    LocationOverlay(Client client, DevToolsPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.plugin.getLocation().isActive()) {
            return null;
        }
        WorldPoint localWorld = this.client.getLocalPlayer().getWorldLocation();
        LocalPoint localPoint = this.client.getLocalPlayer().getLocalLocation();
        int regionID = localWorld.getRegionID();
        if (this.client.isInInstancedRegion()) {
            regionID = WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)localPoint).getRegionID();
            this.panelComponent.getChildren().add(LineComponent.builder().left("Instance").build());
            int[][][] instanceTemplateChunks = this.client.getInstanceTemplateChunks();
            int z = this.client.getPlane();
            int chunkData = instanceTemplateChunks[z][localPoint.getSceneX() / 8][localPoint.getSceneY() / 8];
            int rotation = chunkData >> 1 & 3;
            int chunkY = (chunkData >> 3 & 0x7FF) * 8;
            int chunkX = (chunkData >> 14 & 0x3FF) * 8;
            this.panelComponent.getChildren().add(LineComponent.builder().left("Chunk " + localPoint.getSceneX() / 8 + "," + localPoint.getSceneY() / 8).right(rotation + " " + chunkX + " " + chunkY).build());
        }
        this.panelComponent.getChildren().add(LineComponent.builder().left("Base").right(this.client.getBaseX() + ", " + this.client.getBaseY()).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Local").right(localPoint.getX() + ", " + localPoint.getY()).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Scene").right(localPoint.getSceneX() + ", " + localPoint.getSceneY()).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Tile").right(localWorld.getX() + ", " + localWorld.getY() + ", " + this.client.getPlane()).build());
        for (int i = 0; i < this.client.getMapRegions().length; ++i) {
            int region = this.client.getMapRegions()[i];
            this.panelComponent.getChildren().add(LineComponent.builder().left(i == 0 ? "Map regions" : " ").right(String.valueOf(region)).rightColor(region == regionID ? Color.GREEN : Color.WHITE).build());
        }
        return super.render(graphics);
    }
}

