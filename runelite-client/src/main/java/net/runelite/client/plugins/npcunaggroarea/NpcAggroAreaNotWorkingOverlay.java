/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Inject
 */
package net.runelite.client.plugins.npcunaggroarea;

import com.google.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.plugins.npcunaggroarea.NpcAggroAreaPlugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;

class NpcAggroAreaNotWorkingOverlay
extends OverlayPanel {
    private final NpcAggroAreaPlugin plugin;

    @Inject
    private NpcAggroAreaNotWorkingOverlay(NpcAggroAreaPlugin plugin) {
        this.plugin = plugin;
        this.panelComponent.getChildren().add(LineComponent.builder().left("Unaggressive NPC timers require calibration. Teleport far away or enter a dungeon, then run until this overlay disappears.").build());
        this.setPriority(OverlayPriority.LOW);
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.setClearChildren(false);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (this.plugin.getSafeCenters()[1] != null) {
            return null;
        }
        return super.render(graphics);
    }
}

