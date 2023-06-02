/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.devtools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class CameraOverlay
extends OverlayPanel {
    private final Client client;
    private final DevToolsPlugin plugin;

    @Inject
    CameraOverlay(Client client, DevToolsPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.plugin.getCameraPosition().isActive()) {
            return null;
        }
        this.panelComponent.getChildren().add(TitleComponent.builder().text("Camera").build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("X").right("" + this.client.getCameraX()).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Y").right("" + this.client.getCameraY()).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Z").right("" + this.client.getCameraZ()).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Pitch").right("" + this.client.getCameraPitch()).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Yaw").right("" + this.client.getCameraYaw()).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Scale").right("" + this.client.getScale()).build());
        return super.render(graphics);
    }
}

