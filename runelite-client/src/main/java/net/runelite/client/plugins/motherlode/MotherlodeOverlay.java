/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 */
package net.runelite.client.plugins.motherlode;

import com.google.common.collect.ImmutableSet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.motherlode.MotherlodeConfig;
import net.runelite.client.plugins.motherlode.MotherlodePlugin;
import net.runelite.client.plugins.motherlode.MotherlodeSession;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class MotherlodeOverlay
extends OverlayPanel {
    private static final Set<Integer> MINING_ANIMATION_IDS = ImmutableSet.of((Object)6753, (Object)6754, (Object)6755, (Object)3866, (Object)6757, (Object)6756, (Object[])new Integer[]{6752, 8312, 6758, 335, 8344, 8886, 4481, 7282, 8345, 8786});
    static final String MINING_RESET = "Reset";
    private final Client client;
    private final MotherlodePlugin plugin;
    private final MotherlodeSession motherlodeSession;
    private final MotherlodeConfig config;

    @Inject
    MotherlodeOverlay(Client client, MotherlodePlugin plugin, MotherlodeSession motherlodeSession, MotherlodeConfig config) {
        super(plugin);
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.plugin = plugin;
        this.motherlodeSession = motherlodeSession;
        this.config = config;
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY, MINING_RESET, "Motherlode mine overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.plugin.isInMlm() || !this.config.showMiningStats()) {
            return null;
        }
        MotherlodeSession session = this.motherlodeSession;
        if (session.getLastPayDirtMined() == null) {
            return null;
        }
        Duration statTimeout = Duration.ofMinutes(this.config.statTimeout());
        Duration sinceLastPayDirt = Duration.between(session.getLastPayDirtMined(), Instant.now());
        if (sinceLastPayDirt.compareTo(statTimeout) >= 0) {
            return null;
        }
        if (this.config.showMiningState()) {
            if (MINING_ANIMATION_IDS.contains(this.client.getLocalPlayer().getAnimation())) {
                this.panelComponent.getChildren().add(TitleComponent.builder().text("Mining").color(Color.GREEN).build());
            } else {
                this.panelComponent.getChildren().add(TitleComponent.builder().text("NOT mining").color(Color.RED).build());
            }
        }
        this.panelComponent.getChildren().add(LineComponent.builder().left("Pay-dirt mined:").right(Integer.toString(session.getTotalMined())).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Pay-dirt/hr:").right(session.getRecentMined() > 2 ? Integer.toString(session.getPerHour()) : "").build());
        return super.render(graphics);
    }
}

