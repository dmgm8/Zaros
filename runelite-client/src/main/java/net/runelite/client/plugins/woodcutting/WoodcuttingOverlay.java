/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Skill
 */
package net.runelite.client.plugins.woodcutting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.client.plugins.woodcutting.Axe;
import net.runelite.client.plugins.woodcutting.WoodcuttingConfig;
import net.runelite.client.plugins.woodcutting.WoodcuttingPlugin;
import net.runelite.client.plugins.woodcutting.WoodcuttingSession;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class WoodcuttingOverlay
extends OverlayPanel {
    static final String WOODCUTTING_RESET = "Reset";
    private final Client client;
    private final WoodcuttingPlugin plugin;
    private final WoodcuttingConfig config;
    private final XpTrackerService xpTrackerService;

    @Inject
    private WoodcuttingOverlay(Client client, WoodcuttingPlugin plugin, WoodcuttingConfig config, XpTrackerService xpTrackerService) {
        super(plugin);
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.xpTrackerService = xpTrackerService;
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Woodcutting overlay"));
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, WOODCUTTING_RESET, "Woodcutting overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.config.showWoodcuttingStats()) {
            return null;
        }
        WoodcuttingSession session = this.plugin.getSession();
        if (session == null) {
            return null;
        }
        Axe axe = this.plugin.getAxe();
        if (axe != null && axe.matchesChoppingAnimation(this.client.getLocalPlayer())) {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("Woodcutting").color(Color.GREEN).build());
        } else {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("NOT woodcutting").color(Color.RED).build());
        }
        int actions = this.xpTrackerService.getActions(Skill.WOODCUTTING);
        if (actions > 0) {
            this.panelComponent.getChildren().add(LineComponent.builder().left("Logs cut:").right(Integer.toString(actions)).build());
            if (actions > 2) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Logs/hr:").right(Integer.toString(this.xpTrackerService.getActionsHr(Skill.WOODCUTTING))).build());
            }
        }
        return super.render(graphics);
    }
}

