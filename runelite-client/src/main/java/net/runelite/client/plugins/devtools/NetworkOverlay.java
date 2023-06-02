/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 */
package net.runelite.client.plugins.devtools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.text.StringCharacterIterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class NetworkOverlay
extends OverlayPanel {
    private final Client client;
    private final DevToolsPlugin plugin;

    @Inject
    NetworkOverlay(Client client, DevToolsPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Details", "Network Usage"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.plugin.getNetwork().isActive()) {
            return null;
        }
        this.panelComponent.getChildren().add(LineComponent.builder().left("In").right("Out").build());
        this.panelComponent.getChildren().add(LineComponent.builder().left(NetworkOverlay.humanReadableByteCount(this.client.getNetworkBytesReadTotal())).right(NetworkOverlay.humanReadableByteCount(this.client.getNetworkBytesWrittenTotal())).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left(NetworkOverlay.humanReadableByteCount(this.client.getNetworkBytesRead()) + "/s").right(NetworkOverlay.humanReadableByteCount(this.client.getNetworkBytesWritten()) + "/s").build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Players").right(String.valueOf(this.client.getPlayers().size())).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("NPCs").right(String.valueOf(this.client.getNpcs().size())).build());
        return super.render(graphics);
    }

    private static String humanReadableByteCount(long bytes) {
        if (-1000L < bytes && bytes < 1000L) {
            return bytes + " B";
        }
        StringCharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999950L || bytes >= 999950L) {
            bytes /= 1000L;
            ci.next();
        }
        return String.format("%.1f %cB", (double)bytes / 1000.0, Character.valueOf(ci.current()));
    }
}

