/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.MenuAction
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.external.pvpperformancetracker.FightPerformance;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerConfig;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class PvpPerformanceTrackerOverlay
extends Overlay {
    private final PanelComponent panelComponent = new PanelComponent();
    private final PvpPerformanceTrackerPlugin plugin;
    private final PvpPerformanceTrackerConfig config;
    private TitleComponent overlayTitle;
    private LineComponent simpleConfigOverlayFirstLine;
    private LineComponent simpleConfigOverlaySecondLine;
    private LineComponent overlayFirstLine;
    private LineComponent overlaySecondLine;
    private LineComponent overlayThirdLine;
    private LineComponent overlayFourthLine;
    private LineComponent overlayFifthLine;
    private LineComponent overlaySixthLine;

    @Inject
    private PvpPerformanceTrackerOverlay(PvpPerformanceTrackerPlugin plugin, PvpPerformanceTrackerConfig config) {
        super(plugin);
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.BOTTOM_RIGHT);
        this.setPriority(OverlayPriority.LOW);
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "PvP Performance Tracker"));
        this.panelComponent.setPreferredSize(new Dimension(129, 0));
        this.overlayTitle = TitleComponent.builder().text("PvP Performance").build();
        this.simpleConfigOverlayFirstLine = LineComponent.builder().build();
        this.simpleConfigOverlaySecondLine = LineComponent.builder().build();
        this.overlayFirstLine = LineComponent.builder().build();
        this.overlaySecondLine = LineComponent.builder().build();
        this.overlayThirdLine = LineComponent.builder().build();
        this.overlayFourthLine = LineComponent.builder().build();
        this.overlayFifthLine = LineComponent.builder().build();
        this.overlaySixthLine = LineComponent.builder().build();
        this.overlaySixthLine.setLeftColor(Color.WHITE);
        this.overlaySixthLine.setRight("N/A");
        this.overlaySixthLine.setRightColor(Color.WHITE);
        this.setLines();
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        FightPerformance fight = this.plugin.getCurrentFight();
        if (fight == null || !fight.fightStarted() || !this.config.showFightOverlay() || this.config.restrictToLms() && !this.plugin.isAtLMS()) {
            return null;
        }
        if (this.config.useSimpleOverlay()) {
            this.simpleConfigOverlayFirstLine.setRight(Math.round(fight.getCompetitor().calculateOffPraySuccessPercentage()) + "%");
            this.simpleConfigOverlayFirstLine.setRightColor(fight.competitorOffPraySuccessIsGreater() ? Color.GREEN : Color.WHITE);
            this.simpleConfigOverlaySecondLine.setRight(Math.round(fight.getOpponent().calculateOffPraySuccessPercentage()) + "%");
            this.simpleConfigOverlaySecondLine.setRightColor(fight.opponentOffPraySuccessIsGreater() ? Color.GREEN : Color.WHITE);
        } else {
            this.overlaySecondLine.setLeft(fight.getCompetitor().getOffPrayStats(true));
            this.overlaySecondLine.setLeftColor(fight.competitorOffPraySuccessIsGreater() ? Color.GREEN : Color.WHITE);
            this.overlaySecondLine.setRight(fight.getOpponent().getOffPrayStats(true));
            this.overlaySecondLine.setRightColor(fight.opponentOffPraySuccessIsGreater() ? Color.GREEN : Color.WHITE);
            this.overlayThirdLine.setLeft(fight.getCompetitor().getDeservedDmgString(fight.getOpponent()));
            this.overlayThirdLine.setLeftColor(fight.competitorDeservedDmgIsGreater() ? Color.GREEN : Color.WHITE);
            this.overlayThirdLine.setRight(String.valueOf((int)Math.round(fight.getOpponent().getDeservedDamage())));
            this.overlayThirdLine.setRightColor(fight.opponentDeservedDmgIsGreater() ? Color.GREEN : Color.WHITE);
            this.overlayFourthLine.setLeft(String.valueOf(fight.getCompetitor().getDmgDealtString(fight.getOpponent())));
            this.overlayFourthLine.setLeftColor(fight.competitorDmgDealtIsGreater() ? Color.GREEN : Color.WHITE);
            this.overlayFourthLine.setRight(String.valueOf(fight.getOpponent().getDamageDealt()));
            this.overlayFourthLine.setRightColor(fight.opponentDmgDealtIsGreater() ? Color.GREEN : Color.WHITE);
            this.overlayFifthLine.setLeft(String.valueOf(fight.getCompetitor().getMagicHitStats()));
            this.overlayFifthLine.setLeftColor(fight.competitorMagicHitsLuckier() ? Color.GREEN : Color.WHITE);
            this.overlayFifthLine.setRight(String.valueOf(fight.getOpponent().getMagicHitStats()));
            this.overlayFifthLine.setRightColor(fight.opponentMagicHitsLuckier() ? Color.GREEN : Color.WHITE);
            this.overlaySixthLine.setLeft(String.valueOf(fight.getCompetitor().getOffensivePrayStats(true)));
        }
        return this.panelComponent.render(graphics);
    }

    void setLines() {
        this.panelComponent.getChildren().clear();
        if (this.config.showOverlayTitle()) {
            this.panelComponent.getChildren().add(this.overlayTitle);
        }
        if (this.config.useSimpleOverlay()) {
            this.panelComponent.getChildren().add(this.simpleConfigOverlayFirstLine);
            this.panelComponent.getChildren().add(this.simpleConfigOverlaySecondLine);
        } else {
            if (this.config.showOverlayNames()) {
                this.panelComponent.getChildren().add(this.overlayFirstLine);
            }
            if (this.config.showOverlayOffPray()) {
                this.panelComponent.getChildren().add(this.overlaySecondLine);
            }
            if (this.config.showOverlayDeservedDmg()) {
                this.panelComponent.getChildren().add(this.overlayThirdLine);
            }
            if (this.config.showOverlayDmgDealt()) {
                this.panelComponent.getChildren().add(this.overlayFourthLine);
            }
            if (this.config.showOverlayMagicHits()) {
                this.panelComponent.getChildren().add(this.overlayFifthLine);
            }
            if (this.config.showOverlayOffensivePray()) {
                this.panelComponent.getChildren().add(this.overlaySixthLine);
            }
        }
    }

    void setFight(FightPerformance fight) {
        this.simpleConfigOverlayFirstLine.setLeft(fight.getCompetitor().getName());
        this.simpleConfigOverlaySecondLine.setLeft(fight.getOpponent().getName());
        String cName = fight.getCompetitor().getName();
        this.overlayFirstLine.setLeft(cName.substring(0, Math.min(6, cName.length())));
        String oName = fight.getOpponent().getName();
        this.overlayFirstLine.setRight(oName.substring(0, Math.min(6, oName.length())));
    }
}

