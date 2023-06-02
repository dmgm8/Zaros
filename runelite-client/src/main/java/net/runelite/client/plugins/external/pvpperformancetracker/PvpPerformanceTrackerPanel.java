/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.client.plugins.external.pvpperformancetracker.FightPerformance;
import net.runelite.client.plugins.external.pvpperformancetracker.FightPerformancePanel;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerConfig;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;
import net.runelite.client.plugins.external.pvpperformancetracker.TotalStatsPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

class PvpPerformanceTrackerPanel
extends PluginPanel {
    private final JPanel fightHistoryContainer = new JPanel();
    private final TotalStatsPanel totalStatsPanel = new TotalStatsPanel();
    private final PvpPerformanceTrackerPlugin plugin;
    private final PvpPerformanceTrackerConfig config;

    @Inject
    private PvpPerformanceTrackerPanel(PvpPerformanceTrackerPlugin plugin, PvpPerformanceTrackerConfig config) {
        super(false);
        this.plugin = plugin;
        this.config = config;
        this.setLayout(new BorderLayout(0, 4));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));
        JPanel mainContent = new JPanel(new BorderLayout());
        this.fightHistoryContainer.setSize(this.getSize());
        this.fightHistoryContainer.setLayout(new BoxLayout(this.fightHistoryContainer, 1));
        this.add(this.totalStatsPanel, "North", 0);
        JScrollPane scrollableContainer = new JScrollPane(mainContent);
        scrollableContainer.setBackground(ColorScheme.DARK_GRAY_COLOR);
        scrollableContainer.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        mainContent.add((Component)this.fightHistoryContainer, "North");
        this.add((Component)scrollableContainer, "Center");
    }

    public void addFight(FightPerformance fight) {
        this.totalStatsPanel.addFight(fight);
        SwingUtilities.invokeLater(() -> {
            this.fightHistoryContainer.add((Component)new FightPerformancePanel(fight), 0);
            this.updateUI();
        });
    }

    public void addFights(ArrayList<FightPerformance> fights) {
        this.totalStatsPanel.addFights(fights);
        SwingUtilities.invokeLater(() -> {
            fights.forEach(f -> this.fightHistoryContainer.add((Component)new FightPerformancePanel((FightPerformance)f), 0));
            this.updateUI();
        });
    }

    public void rebuild() {
        this.totalStatsPanel.reset();
        this.fightHistoryContainer.removeAll();
        if (this.plugin.fightHistory.size() > 0) {
            this.addFights(this.plugin.fightHistory);
        }
        SwingUtilities.invokeLater(this::updateUI);
    }
}

