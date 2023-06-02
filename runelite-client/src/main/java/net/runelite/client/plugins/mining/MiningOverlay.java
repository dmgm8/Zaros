/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Skill
 */
package net.runelite.client.plugins.mining;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.client.plugins.mining.MiningConfig;
import net.runelite.client.plugins.mining.MiningPlugin;
import net.runelite.client.plugins.mining.MiningSession;
import net.runelite.client.plugins.mining.Pickaxe;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class MiningOverlay
extends OverlayPanel {
    static final String MINING_RESET = "Reset";
    private final Client client;
    private final MiningPlugin plugin;
    private final MiningConfig config;
    private final XpTrackerService xpTrackerService;

    @Inject
    private MiningOverlay(Client client, MiningPlugin plugin, MiningConfig config, XpTrackerService xpTrackerService) {
        super(plugin);
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.xpTrackerService = xpTrackerService;
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Mining overlay"));
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, MINING_RESET, "Mining overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        MiningSession session = this.plugin.getSession();
        if (session == null || session.getLastMined() == null || !this.config.showMiningStats()) {
            return null;
        }
        Pickaxe pickaxe = this.plugin.getPickaxe();
        if (pickaxe != null && (pickaxe.matchesMiningAnimation(this.client.getLocalPlayer()) || this.client.getLocalPlayer().getAnimation() == 7201)) {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("Mining").color(Color.GREEN).build());
        } else {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("NOT mining").color(Color.RED).build());
        }
        int actions = this.xpTrackerService.getActions(Skill.MINING);
        if (actions > 0) {
            this.panelComponent.getChildren().add(LineComponent.builder().left("Total mined:").right(Integer.toString(actions)).build());
            if (actions > 2) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Mined/hr:").right(Integer.toString(this.xpTrackerService.getActionsHr(Skill.MINING))).build());
            }
        }
        return super.render(graphics);
    }
}

