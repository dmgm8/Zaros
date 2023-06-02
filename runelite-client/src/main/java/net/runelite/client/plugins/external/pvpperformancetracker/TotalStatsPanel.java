/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.client.plugins.external.pvpperformancetracker.FightPerformance;
import net.runelite.client.plugins.external.pvpperformancetracker.Fighter;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;
import net.runelite.client.ui.ColorScheme;

public class TotalStatsPanel
extends JPanel {
    private static final NumberFormat nf = NumberFormat.getInstance();
    private static final NumberFormat nf1;
    private static final NumberFormat nf2;
    private JLabel killsLabel;
    private JLabel deathsLabel;
    private JLabel offPrayStatsLabel;
    private JLabel deservedDmgStatsLabel;
    private JLabel dmgDealtStatsLabel;
    private JLabel magicHitCountStatsLabel;
    private JLabel offensivePrayCountStatsLabel;
    private Fighter totalStats = new Fighter("Player");
    private int numKills = 0;
    private int numDeaths = 0;
    private int numFights = 0;
    private double totalDeservedDmg = 0.0;
    private double totalDeservedDmgDiff = 0.0;
    private double avgDeservedDmg = 0.0;
    private double avgDeservedDmgDiff = 0.0;
    private double killTotalDeservedDmg = 0.0;
    private double killTotalDeservedDmgDiff = 0.0;
    private double killAvgDeservedDmg = 0.0;
    private double killAvgDeservedDmgDiff = 0.0;
    private double deathTotalDeservedDmg = 0.0;
    private double deathTotalDeservedDmgDiff = 0.0;
    private double deathAvgDeservedDmg = 0.0;
    private double deathAvgDeservedDmgDiff = 0.0;
    private double totalDmgDealt = 0.0;
    private double totalDmgDealtDiff = 0.0;
    private double avgDmgDealt = 0.0;
    private double avgDmgDealtDiff = 0.0;
    private double killTotalDmgDealt = 0.0;
    private double killTotalDmgDealtDiff = 0.0;
    private double killAvgDmgDealt = 0.0;
    private double killAvgDmgDealtDiff = 0.0;
    private double deathTotalDmgDealt = 0.0;
    private double deathTotalDmgDealtDiff = 0.0;
    private double deathAvgDmgDealt = 0.0;
    private double deathAvgDmgDealtDiff = 0.0;

    TotalStatsPanel() {
        this.setLayout(new GridLayout(7, 1));
        this.setBorder(new EmptyBorder(8, 8, 8, 8));
        this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        JLabel titleLabel = new JLabel();
        titleLabel.setText("PvP Performance Tracker");
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setForeground(Color.WHITE);
        this.add(titleLabel);
        JPanel killDeathPanel = new JPanel(new BorderLayout());
        this.killsLabel = new JLabel();
        this.killsLabel.setText(this.numKills + " Kills");
        this.killsLabel.setForeground(Color.WHITE);
        killDeathPanel.add((Component)this.killsLabel, "West");
        this.deathsLabel = new JLabel();
        this.deathsLabel.setText(this.numDeaths + " Deaths");
        this.deathsLabel.setForeground(Color.WHITE);
        killDeathPanel.add((Component)this.deathsLabel, "East");
        killDeathPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.add(killDeathPanel);
        JPanel offPrayStatsPanel = new JPanel(new BorderLayout());
        JLabel leftLabel = new JLabel();
        leftLabel.setText("Total Off-Pray:");
        leftLabel.setForeground(Color.WHITE);
        offPrayStatsPanel.add((Component)leftLabel, "West");
        this.offPrayStatsLabel = new JLabel();
        this.offPrayStatsLabel.setForeground(Color.WHITE);
        offPrayStatsPanel.add((Component)this.offPrayStatsLabel, "East");
        offPrayStatsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.add(offPrayStatsPanel);
        JPanel deservedDmgStatsPanel = new JPanel(new BorderLayout());
        JLabel deservedDmgStatsLeftLabel = new JLabel();
        deservedDmgStatsLeftLabel.setText("Avg Deserved Dmg:");
        deservedDmgStatsLeftLabel.setForeground(Color.WHITE);
        deservedDmgStatsPanel.add((Component)deservedDmgStatsLeftLabel, "West");
        this.deservedDmgStatsLabel = new JLabel();
        this.deservedDmgStatsLabel.setForeground(Color.WHITE);
        deservedDmgStatsPanel.add((Component)this.deservedDmgStatsLabel, "East");
        deservedDmgStatsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.add(deservedDmgStatsPanel);
        JPanel dmgDealtStatsPanel = new JPanel(new BorderLayout());
        JLabel dmgDealtStatsLeftLabel = new JLabel();
        dmgDealtStatsLeftLabel.setText("Avg Damage Dealt:");
        dmgDealtStatsLeftLabel.setForeground(Color.WHITE);
        dmgDealtStatsPanel.add((Component)dmgDealtStatsLeftLabel, "West");
        this.dmgDealtStatsLabel = new JLabel();
        this.dmgDealtStatsLabel.setForeground(Color.WHITE);
        dmgDealtStatsPanel.add((Component)this.dmgDealtStatsLabel, "East");
        dmgDealtStatsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.add(dmgDealtStatsPanel);
        JPanel magicHitStatsPanel = new JPanel(new BorderLayout());
        JLabel magicHitStatsLeftLabel = new JLabel();
        magicHitStatsLeftLabel.setText("Magic Luck:");
        magicHitStatsLeftLabel.setForeground(Color.WHITE);
        magicHitStatsPanel.add((Component)magicHitStatsLeftLabel, "West");
        this.magicHitCountStatsLabel = new JLabel();
        this.magicHitCountStatsLabel.setForeground(Color.WHITE);
        magicHitStatsPanel.add((Component)this.magicHitCountStatsLabel, "East");
        magicHitStatsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.add(magicHitStatsPanel);
        JPanel offensivePrayStatsPanel = new JPanel(new BorderLayout());
        JLabel offensivePrayStatsLeftLabel = new JLabel();
        offensivePrayStatsLeftLabel.setText("Offensive Pray:");
        offensivePrayStatsLeftLabel.setForeground(Color.WHITE);
        offensivePrayStatsPanel.add((Component)offensivePrayStatsLeftLabel, "West");
        this.offensivePrayCountStatsLabel = new JLabel();
        this.offensivePrayCountStatsLabel.setForeground(Color.WHITE);
        offensivePrayStatsPanel.add((Component)this.offensivePrayCountStatsLabel, "East");
        offensivePrayStatsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.add(offensivePrayStatsPanel);
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem resetAllFights = new JMenuItem("Reset All");
        resetAllFights.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset all fight history data? This cannot be undone.", "Warning", 0);
            if (dialogResult == 0) {
                PvpPerformanceTrackerPlugin.PLUGIN.resetFightHistory();
            }
        });
        JMenuItem exportFightHistory = new JMenuItem("Export Fight History");
        exportFightHistory.addActionListener(e -> PvpPerformanceTrackerPlugin.PLUGIN.exportFightHistory());
        JMenuItem importFightHistory = new JMenuItem("Import Fight History");
        importFightHistory.addActionListener(e -> {
            String fightHistoryData = JOptionPane.showInputDialog(this, "Enter the fight history data you wish to import:", "Import Fight History", 1);
            if (fightHistoryData == null || fightHistoryData.length() < 2) {
                return;
            }
            PvpPerformanceTrackerPlugin.PLUGIN.importUserFightHistoryData(fightHistoryData);
        });
        popupMenu.add(resetAllFights);
        popupMenu.add(exportFightHistory);
        popupMenu.add(importFightHistory);
        this.setComponentPopupMenu(popupMenu);
        this.setLabels();
    }

    private void setLabels() {
        String avgDeservedDmgDiffOneDecimal = nf1.format(this.avgDeservedDmgDiff);
        String avgDmgDealtDiffOneDecimal = nf1.format(this.avgDmgDealtDiff);
        this.killsLabel.setText(nf.format(this.numKills) + " Kill" + (this.numKills != 1 ? "s" : ""));
        this.deathsLabel.setText(nf.format(this.numDeaths) + " Death" + (this.numDeaths != 1 ? "s" : ""));
        if (this.totalStats.getAttackCount() >= 10000) {
            this.offPrayStatsLabel.setText(nf1.format((double)this.totalStats.getOffPraySuccessCount() / 1000.0) + "K/" + nf1.format((double)this.totalStats.getAttackCount() / 1000.0) + "K (" + Math.round(this.totalStats.calculateOffPraySuccessPercentage()) + "%)");
        } else {
            this.offPrayStatsLabel.setText(this.totalStats.getOffPrayStats());
        }
        this.offPrayStatsLabel.setToolTipText(nf.format(this.totalStats.getOffPraySuccessCount()) + " successful off-pray attacks/" + nf.format(this.totalStats.getAttackCount()) + " total attacks (" + nf2.format(this.totalStats.calculateOffPraySuccessPercentage()) + "%)");
        this.deservedDmgStatsLabel.setText(nf.format(this.avgDeservedDmg) + " (" + (this.avgDeservedDmgDiff > 0.0 ? "+" : "") + avgDeservedDmgDiffOneDecimal + ")");
        this.deservedDmgStatsLabel.setToolTipText("Avg of " + nf1.format(this.avgDeservedDmg) + " deserved damage per fight with avg diff of " + (this.avgDeservedDmgDiff > 0.0 ? "+" : "") + avgDeservedDmgDiffOneDecimal + ". On kills: " + nf1.format(this.killAvgDeservedDmg) + " (" + (this.killAvgDeservedDmgDiff > 0.0 ? "+" : "") + nf1.format(this.killAvgDeservedDmgDiff) + "), on deaths: " + nf1.format(this.deathAvgDeservedDmg) + " (" + (this.deathAvgDeservedDmgDiff > 0.0 ? "+" : "") + nf1.format(this.deathAvgDeservedDmgDiff) + ")");
        this.dmgDealtStatsLabel.setText(nf.format(this.avgDmgDealt) + " (" + (this.avgDmgDealtDiff > 0.0 ? "+" : "") + avgDmgDealtDiffOneDecimal + ")");
        this.dmgDealtStatsLabel.setToolTipText("Avg of " + nf1.format(this.avgDmgDealt) + " damage per fight with avg diff of " + (this.avgDmgDealtDiff > 0.0 ? "+" : "") + avgDmgDealtDiffOneDecimal + ". On kills: " + nf1.format(this.killAvgDmgDealt) + " (" + (this.killAvgDmgDealtDiff > 0.0 ? "+" : "") + nf1.format(this.killAvgDmgDealtDiff) + "), on deaths: " + nf1.format(this.deathAvgDmgDealt) + " (" + (this.deathAvgDmgDealtDiff > 0.0 ? "+" : "") + nf1.format(this.deathAvgDmgDealtDiff) + ")");
        if (this.totalStats.getMagicHitCountDeserved() >= 10000.0) {
            this.magicHitCountStatsLabel.setText(nf1.format((double)this.totalStats.getMagicHitCount() / 1000.0) + "K/" + nf1.format(this.totalStats.getMagicHitCountDeserved() / 1000.0) + "K");
        } else {
            this.magicHitCountStatsLabel.setText(this.totalStats.getMagicHitStats());
        }
        this.magicHitCountStatsLabel.setToolTipText("You hit " + nf1.format(this.totalStats.getMagicHitCount()) + " magic attacks, but deserved to hit " + nf1.format(this.totalStats.getMagicHitCountDeserved()));
        if (this.totalStats.getAttackCount() >= 10000) {
            this.offensivePrayCountStatsLabel.setText(nf1.format((double)this.totalStats.getOffensivePraySuccessCount() / 1000.0) + "K/" + nf1.format((double)this.totalStats.getAttackCount() / 1000.0) + "K (" + Math.round(this.totalStats.calculateOffensivePraySuccessPercentage()) + "%)");
        } else {
            this.offensivePrayCountStatsLabel.setText(this.totalStats.getOffensivePrayStats());
        }
        this.offensivePrayCountStatsLabel.setToolTipText(nf.format(this.totalStats.getOffensivePraySuccessCount()) + " successful offensive prayers/" + nf.format(this.totalStats.getAttackCount()) + " total attacks (" + nf2.format(this.totalStats.calculateOffensivePraySuccessPercentage()) + "%)");
    }

    public void addFight(FightPerformance fight) {
        ++this.numFights;
        this.totalDeservedDmg += fight.getCompetitor().getDeservedDamage();
        this.totalDeservedDmgDiff += fight.getCompetitorDeservedDmgDiff();
        this.totalDmgDealt += (double)fight.getCompetitor().getDamageDealt();
        this.totalDmgDealtDiff += fight.getCompetitorDmgDealtDiff();
        this.avgDeservedDmg = this.totalDeservedDmg / (double)this.numFights;
        this.avgDeservedDmgDiff = this.totalDeservedDmgDiff / (double)this.numFights;
        this.avgDmgDealt = this.totalDmgDealt / (double)this.numFights;
        this.avgDmgDealtDiff = this.totalDmgDealtDiff / (double)this.numFights;
        if (fight.getCompetitor().isDead()) {
            ++this.numDeaths;
            this.deathTotalDeservedDmg += fight.getCompetitor().getDeservedDamage();
            this.deathTotalDeservedDmgDiff += fight.getCompetitorDeservedDmgDiff();
            this.deathTotalDmgDealt += (double)fight.getCompetitor().getDamageDealt();
            this.deathTotalDmgDealtDiff += fight.getCompetitorDmgDealtDiff();
            this.deathAvgDeservedDmg = this.deathTotalDeservedDmg / (double)this.numDeaths;
            this.deathAvgDeservedDmgDiff = this.deathTotalDeservedDmgDiff / (double)this.numDeaths;
            this.deathAvgDmgDealt = this.deathTotalDmgDealt / (double)this.numDeaths;
            this.deathAvgDmgDealtDiff = this.deathTotalDmgDealtDiff / (double)this.numDeaths;
        }
        if (fight.getOpponent().isDead()) {
            ++this.numKills;
            this.killTotalDeservedDmg += fight.getCompetitor().getDeservedDamage();
            this.killTotalDeservedDmgDiff += fight.getCompetitorDeservedDmgDiff();
            this.killTotalDmgDealt += (double)fight.getCompetitor().getDamageDealt();
            this.killTotalDmgDealtDiff += fight.getCompetitorDmgDealtDiff();
            this.killAvgDeservedDmg = this.killTotalDeservedDmg / (double)this.numKills;
            this.killAvgDeservedDmgDiff = this.killTotalDeservedDmgDiff / (double)this.numKills;
            this.killAvgDmgDealt = this.killTotalDmgDealt / (double)this.numKills;
            this.killAvgDmgDealtDiff = this.killTotalDmgDealtDiff / (double)this.numKills;
        }
        this.totalStats.addAttacks(fight.getCompetitor().getOffPraySuccessCount(), fight.getCompetitor().getAttackCount(), fight.getCompetitor().getDeservedDamage(), fight.getCompetitor().getDamageDealt(), fight.getCompetitor().getMagicHitCount(), fight.getCompetitor().getMagicHitCountDeserved(), fight.getCompetitor().getOffensivePraySuccessCount());
        SwingUtilities.invokeLater(this::setLabels);
    }

    public void addFights(ArrayList<FightPerformance> fights) {
        this.numFights += fights.size();
        for (FightPerformance fight : fights) {
            this.totalDeservedDmg += fight.getCompetitor().getDeservedDamage();
            this.totalDeservedDmgDiff += fight.getCompetitorDeservedDmgDiff();
            this.totalDmgDealt += (double)fight.getCompetitor().getDamageDealt();
            this.totalDmgDealtDiff += fight.getCompetitorDmgDealtDiff();
            if (fight.getCompetitor().isDead()) {
                ++this.numDeaths;
                this.deathTotalDeservedDmg += fight.getCompetitor().getDeservedDamage();
                this.deathTotalDeservedDmgDiff += fight.getCompetitorDeservedDmgDiff();
                this.deathTotalDmgDealt += (double)fight.getCompetitor().getDamageDealt();
                this.deathTotalDmgDealtDiff += fight.getCompetitorDmgDealtDiff();
            }
            if (fight.getOpponent().isDead()) {
                ++this.numKills;
                this.killTotalDeservedDmg += fight.getCompetitor().getDeservedDamage();
                this.killTotalDeservedDmgDiff += fight.getCompetitorDeservedDmgDiff();
                this.killTotalDmgDealt += (double)fight.getCompetitor().getDamageDealt();
                this.killTotalDmgDealtDiff += fight.getCompetitorDmgDealtDiff();
            }
            this.totalStats.addAttacks(fight.getCompetitor().getOffPraySuccessCount(), fight.getCompetitor().getAttackCount(), fight.getCompetitor().getDeservedDamage(), fight.getCompetitor().getDamageDealt(), fight.getCompetitor().getMagicHitCount(), fight.getCompetitor().getMagicHitCountDeserved(), fight.getCompetitor().getOffensivePraySuccessCount());
        }
        this.avgDeservedDmg = this.totalDeservedDmg / (double)this.numFights;
        this.avgDeservedDmgDiff = this.totalDeservedDmgDiff / (double)this.numFights;
        this.avgDmgDealt = this.totalDmgDealt / (double)this.numFights;
        this.avgDmgDealtDiff = this.totalDmgDealtDiff / (double)this.numFights;
        this.killAvgDeservedDmg = this.killTotalDeservedDmg / (double)this.numKills;
        this.killAvgDeservedDmgDiff = this.killTotalDeservedDmgDiff / (double)this.numKills;
        this.deathAvgDeservedDmg = this.deathTotalDeservedDmg / (double)this.numDeaths;
        this.deathAvgDeservedDmgDiff = this.deathTotalDeservedDmgDiff / (double)this.numDeaths;
        this.killAvgDmgDealt = this.killTotalDmgDealt / (double)this.numKills;
        this.killAvgDmgDealtDiff = this.killTotalDmgDealtDiff / (double)this.numKills;
        this.deathAvgDmgDealt = this.deathTotalDmgDealt / (double)this.numDeaths;
        this.deathAvgDmgDealtDiff = this.deathTotalDmgDealtDiff / (double)this.numDeaths;
        SwingUtilities.invokeLater(this::setLabels);
    }

    public void reset() {
        this.numFights = 0;
        this.numDeaths = 0;
        this.numKills = 0;
        this.totalDeservedDmg = 0.0;
        this.totalDeservedDmgDiff = 0.0;
        this.killTotalDeservedDmg = 0.0;
        this.killTotalDeservedDmgDiff = 0.0;
        this.deathTotalDeservedDmg = 0.0;
        this.deathTotalDeservedDmgDiff = 0.0;
        this.totalDmgDealt = 0.0;
        this.totalDmgDealtDiff = 0.0;
        this.killTotalDmgDealt = 0.0;
        this.killTotalDmgDealtDiff = 0.0;
        this.deathTotalDmgDealt = 0.0;
        this.deathTotalDmgDealtDiff = 0.0;
        this.avgDeservedDmg = 0.0;
        this.avgDeservedDmgDiff = 0.0;
        this.killAvgDeservedDmg = 0.0;
        this.killAvgDeservedDmgDiff = 0.0;
        this.deathAvgDeservedDmg = 0.0;
        this.deathAvgDeservedDmgDiff = 0.0;
        this.avgDmgDealt = 0.0;
        this.avgDmgDealtDiff = 0.0;
        this.killAvgDmgDealt = 0.0;
        this.killAvgDmgDealtDiff = 0.0;
        this.deathAvgDmgDealt = 0.0;
        this.deathAvgDmgDealtDiff = 0.0;
        this.totalStats = new Fighter("Player");
        SwingUtilities.invokeLater(this::setLabels);
    }

    static {
        nf.setMaximumFractionDigits(1);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf1 = NumberFormat.getInstance();
        nf1.setMaximumFractionDigits(1);
        nf1.setRoundingMode(RoundingMode.HALF_UP);
        nf2 = NumberFormat.getInstance();
        nf2.setMaximumFractionDigits(2);
        nf2.setRoundingMode(RoundingMode.HALF_UP);
    }
}

