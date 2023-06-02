/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.HeadIcon
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import net.runelite.api.HeadIcon;
import net.runelite.client.plugins.external.pvpperformancetracker.AnimationData;
import net.runelite.client.plugins.external.pvpperformancetracker.FightLogEntry;
import net.runelite.client.plugins.external.pvpperformancetracker.FightPerformance;
import net.runelite.client.plugins.external.pvpperformancetracker.Fighter;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

class FightPerformancePanel
extends JPanel {
    private static JFrame fightLogFrame;
    private static Image frameIcon;
    private static ImageIcon deathIcon;
    private static final SimpleDateFormat DATE_FORMAT;
    private static final NumberFormat nf;
    private static final Border normalBorder;
    private static final Border hoverBorder;
    private FightPerformance fight;

    FightPerformancePanel(FightPerformance fight) {
        if (frameIcon == null || deathIcon == null) {
            frameIcon = new ImageIcon(ImageUtil.getResourceStreamFromClass(this.getClass(), "/skull_red.png")).getImage();
            deathIcon = new ImageIcon(frameIcon.getScaledInstance(12, 12, 1));
        }
        this.fight = fight;
        Fighter competitor = fight.getCompetitor();
        Fighter opponent = fight.getOpponent();
        this.setLayout(new BorderLayout(5, 0));
        this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        String tooltipText = "Ended at " + DATE_FORMAT.format(Date.from(Instant.ofEpochMilli(fight.getLastFightTime())));
        this.setToolTipText(tooltipText);
        this.setBorder(normalBorder);
        JPanel fightPanel = new JPanel();
        fightPanel.setLayout(new BoxLayout(fightPanel, 1));
        fightPanel.setBackground(null);
        JPanel playerNamesLine = new JPanel();
        playerNamesLine.setLayout(new BorderLayout());
        playerNamesLine.setBackground(null);
        JLabel playerStatsName = new JLabel();
        if (competitor.isDead()) {
            playerStatsName.setIcon(deathIcon);
        }
        playerStatsName.setText(competitor.getName());
        playerStatsName.setForeground(Color.WHITE);
        playerNamesLine.add((Component)playerStatsName, "West");
        JLabel opponentStatsName = new JLabel();
        if (opponent.isDead()) {
            opponentStatsName.setIcon(deathIcon);
        }
        opponentStatsName.setText(opponent.getName());
        opponentStatsName.setForeground(Color.WHITE);
        playerNamesLine.add((Component)opponentStatsName, "East");
        JPanel offPrayStatsLine = new JPanel();
        offPrayStatsLine.setLayout(new BorderLayout());
        offPrayStatsLine.setBackground(null);
        JLabel playerOffPrayStats = new JLabel();
        playerOffPrayStats.setText(competitor.getOffPrayStats());
        playerOffPrayStats.setToolTipText(competitor.getOffPraySuccessCount() + " successful off-pray attacks/" + competitor.getAttackCount() + " total attacks (" + nf.format(competitor.calculateOffPraySuccessPercentage()) + "%)");
        playerOffPrayStats.setForeground(fight.competitorOffPraySuccessIsGreater() ? Color.GREEN : Color.WHITE);
        offPrayStatsLine.add((Component)playerOffPrayStats, "West");
        JLabel opponentOffPrayStats = new JLabel();
        opponentOffPrayStats.setText(opponent.getOffPrayStats());
        opponentOffPrayStats.setToolTipText(opponent.getOffPraySuccessCount() + " successful off-pray attacks/" + opponent.getAttackCount() + " total attacks (" + nf.format(opponent.calculateOffPraySuccessPercentage()) + "%)");
        opponentOffPrayStats.setForeground(fight.opponentOffPraySuccessIsGreater() ? Color.GREEN : Color.WHITE);
        offPrayStatsLine.add((Component)opponentOffPrayStats, "East");
        JPanel deservedDpsStatsLine = new JPanel();
        deservedDpsStatsLine.setLayout(new BorderLayout());
        deservedDpsStatsLine.setBackground(null);
        JLabel playerDeservedDpsStats = new JLabel();
        playerDeservedDpsStats.setText(competitor.getDeservedDmgString(opponent));
        playerDeservedDpsStats.setToolTipText(competitor.getName() + " deserved to deal " + nf.format(competitor.getDeservedDamage()) + " damage based on gear/pray (" + competitor.getDeservedDmgString(opponent, 1, true) + " vs opponent)");
        playerDeservedDpsStats.setForeground(fight.competitorDeservedDmgIsGreater() ? Color.GREEN : Color.WHITE);
        deservedDpsStatsLine.add((Component)playerDeservedDpsStats, "West");
        JLabel opponentDeservedDpsStats = new JLabel();
        opponentDeservedDpsStats.setText(opponent.getDeservedDmgString(competitor));
        opponentDeservedDpsStats.setToolTipText(opponent.getName() + " deserved to deal " + nf.format(opponent.getDeservedDamage()) + " damage based on gear/pray (" + opponent.getDeservedDmgString(competitor, 1, true) + " vs you)");
        opponentDeservedDpsStats.setForeground(fight.opponentDeservedDmgIsGreater() ? Color.GREEN : Color.WHITE);
        deservedDpsStatsLine.add((Component)opponentDeservedDpsStats, "East");
        JPanel dmgDealtStatsLine = new JPanel();
        dmgDealtStatsLine.setLayout(new BorderLayout());
        dmgDealtStatsLine.setBackground(null);
        JLabel playerDmgDealtStats = new JLabel();
        playerDmgDealtStats.setText(competitor.getDmgDealtString(opponent));
        playerDmgDealtStats.setToolTipText(competitor.getName() + " dealt " + competitor.getDamageDealt() + " damage (" + competitor.getDmgDealtString(opponent, true) + " vs opponent)");
        playerDmgDealtStats.setForeground(fight.competitorDmgDealtIsGreater() ? Color.GREEN : Color.WHITE);
        dmgDealtStatsLine.add((Component)playerDmgDealtStats, "West");
        JLabel opponentDmgDealtStats = new JLabel();
        opponentDmgDealtStats.setText(String.valueOf(opponent.getDamageDealt()));
        opponentDmgDealtStats.setToolTipText(opponent.getName() + " dealt " + opponent.getDamageDealt() + " damage (" + opponent.getDmgDealtString(competitor, true) + " vs you)");
        opponentDmgDealtStats.setForeground(fight.opponentDeservedDmgIsGreater() ? Color.GREEN : Color.WHITE);
        dmgDealtStatsLine.add((Component)opponentDmgDealtStats, "East");
        JPanel magicHitStatsLine = new JPanel();
        magicHitStatsLine.setLayout(new BorderLayout());
        magicHitStatsLine.setBackground(null);
        JLabel playerMagicHitStats = new JLabel();
        playerMagicHitStats.setText(String.valueOf(competitor.getMagicHitStats()));
        playerMagicHitStats.setToolTipText(competitor.getName() + " hit " + competitor.getMagicHitCount() + " magic attacks, but deserved to hit " + nf.format(competitor.getMagicHitCountDeserved()));
        playerMagicHitStats.setForeground(fight.competitorMagicHitsLuckier() ? Color.GREEN : Color.WHITE);
        magicHitStatsLine.add((Component)playerMagicHitStats, "West");
        JLabel opponentMagicHitStats = new JLabel();
        opponentMagicHitStats.setText(String.valueOf(opponent.getMagicHitStats()));
        opponentMagicHitStats.setToolTipText(opponent.getName() + " hit " + opponent.getMagicHitCount() + " magic attacks, but deserved to hit " + nf.format(opponent.getMagicHitCountDeserved()));
        opponentMagicHitStats.setForeground(fight.opponentMagicHitsLuckier() ? Color.GREEN : Color.WHITE);
        magicHitStatsLine.add((Component)opponentMagicHitStats, "East");
        JPanel offensivePrayStatsLine = new JPanel();
        offensivePrayStatsLine.setLayout(new BorderLayout());
        offensivePrayStatsLine.setBackground(null);
        JLabel playerOffensivePrayStats = new JLabel();
        playerOffensivePrayStats.setText(String.valueOf(competitor.getOffensivePrayStats()));
        playerOffensivePrayStats.setToolTipText(competitor.getOffensivePrayStats() + " successful offensive prayers/" + competitor.getAttackCount() + " total attacks (" + nf.format(competitor.calculateOffensivePraySuccessPercentage()) + "%)");
        playerOffensivePrayStats.setForeground(Color.WHITE);
        offensivePrayStatsLine.add((Component)playerOffensivePrayStats, "West");
        JLabel opponentOffensivePrayStats = new JLabel();
        opponentOffensivePrayStats.setText("N/A");
        opponentOffensivePrayStats.setToolTipText("No data is available for the opponent's offensive prayers.");
        opponentOffensivePrayStats.setForeground(Color.WHITE);
        offensivePrayStatsLine.add((Component)opponentOffensivePrayStats, "East");
        fightPanel.add(playerNamesLine);
        fightPanel.add(offPrayStatsLine);
        fightPanel.add(deservedDpsStatsLine);
        fightPanel.add(dmgDealtStatsLine);
        fightPanel.add(magicHitStatsLine);
        fightPanel.add(offensivePrayStatsLine);
        this.add((Component)fightPanel, "North");
        MouseAdapter fightPerformanceMouseListener = new MouseAdapter(){

            @Override
            public void mouseEntered(MouseEvent e) {
                FightPerformancePanel.this.setFullBackgroundColor(ColorScheme.DARK_GRAY_COLOR);
                FightPerformancePanel.this.setOutline(true);
                FightPerformancePanel.this.setCursor(new Cursor(12));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                FightPerformancePanel.this.setFullBackgroundColor(ColorScheme.DARKER_GRAY_COLOR);
                FightPerformancePanel.this.setOutline(false);
                FightPerformancePanel.this.setCursor(new Cursor(0));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    return;
                }
                FightPerformancePanel.this.createFightLogFrame();
            }
        };
        this.addMouseListener(fightPerformanceMouseListener);
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeFight = new JMenuItem("Remove Fight");
        removeFight.addActionListener(e -> PvpPerformanceTrackerPlugin.PLUGIN.removeFight(fight));
        popupMenu.add(removeFight);
        this.setComponentPopupMenu(popupMenu);
    }

    private void setFullBackgroundColor(Color color) {
        this.setBackground(color);
        for (Component c : this.getComponents()) {
            c.setBackground(color);
        }
    }

    private void setOutline(boolean visible) {
        this.setBorder(visible ? hoverBorder : normalBorder);
    }

    private void createFightLogFrame() {
        ArrayList<FightLogEntry> fightLogEntries;
        if (fightLogFrame != null) {
            fightLogFrame.dispose();
        }
        if ((fightLogEntries = this.fight.getAllFightLogEntries()) == null || fightLogEntries.size() < 1) {
            PvpPerformanceTrackerPlugin.PLUGIN.createConfirmationModal("Info", "There are no fight log entries available for this fight.");
            return;
        }
        String title = this.fight.getCompetitor().getName() + " vs " + this.fight.getOpponent().getName();
        fightLogFrame = new JFrame(title);
        if (fightLogFrame.isAlwaysOnTopSupported()) {
            fightLogFrame.setAlwaysOnTop(PvpPerformanceTrackerPlugin.PLUGIN.getRuneliteConfig().gameAlwaysOnTop());
        }
        fightLogFrame.setIconImage(frameIcon);
        fightLogFrame.setSize(765, 503);
        fightLogFrame.setLocation(this.getRootPane().getLocationOnScreen());
        JPanel mainPanel = new JPanel(new BorderLayout(4, 4));
        Object[][] stats = new Object[fightLogEntries.size()][11];
        int i = 0;
        long initialTime = 0L;
        for (FightLogEntry fightEntry : fightLogEntries) {
            if (i == 0) {
                initialTime = fightEntry.getTime();
            }
            int styleIcon = fightEntry.getAnimationData().attackStyle == AnimationData.AttackStyle.RANGED ? 200 : (fightEntry.getAnimationData().attackStyle == AnimationData.AttackStyle.MAGIC ? 202 : 197);
            int prayIcon = 0;
            boolean noOverhead = false;
            if (fightEntry.getDefenderOverhead() == HeadIcon.RANGED) {
                prayIcon = 128;
            } else if (fightEntry.getDefenderOverhead() == HeadIcon.MAGIC) {
                prayIcon = 127;
            } else if (fightEntry.getDefenderOverhead() == HeadIcon.MELEE) {
                prayIcon = 129;
            } else {
                noOverhead = true;
            }
            BufferedImage styleIconRendered = PvpPerformanceTrackerPlugin.SPRITE_MANAGER.getSprite(styleIcon, 0);
            stats[i][0] = fightEntry.getAttackerName();
            stats[i][1] = styleIconRendered;
            stats[i][2] = fightEntry.getHitRange();
            stats[i][3] = nf.format(fightEntry.getAccuracy() * 100.0) + '%';
            stats[i][4] = nf.format(fightEntry.getDeservedDamage());
            stats[i][5] = fightEntry.getAnimationData().isSpecial ? "\u2714" : "";
            stats[i][6] = fightEntry.success() ? "\u2714" : "";
            Object object = stats[i][7] = noOverhead ? "" : PvpPerformanceTrackerPlugin.SPRITE_MANAGER.getSprite(prayIcon, 0);
            if (fightEntry.getAnimationData().attackStyle == AnimationData.AttackStyle.MAGIC) {
                int freezeIcon = fightEntry.isSplash() ? 378 : 328;
                BufferedImage freezeIconRendered = PvpPerformanceTrackerPlugin.SPRITE_MANAGER.getSprite(freezeIcon, 0);
                stats[i][8] = freezeIconRendered;
            } else {
                stats[i][8] = "";
            }
            stats[i][9] = fightEntry.getAttackerOffensivePray() <= 0 ? "" : PvpPerformanceTrackerPlugin.SPRITE_MANAGER.getSprite(fightEntry.getAttackerOffensivePray(), 0);
            long durationLong = fightEntry.getTime() - initialTime;
            Duration duration = Duration.ofMillis(durationLong);
            String time = String.format("%02d:%02d.%01d", duration.toMinutes(), duration.getSeconds() % 60L, durationLong % 1000L / 100L);
            stats[i][10] = time;
            ++i;
        }
        Object[] header = new String[]{"Attacker", "Style", "Hit Range", "Accuracy", "Avg Hit", "Special?", "Off-Pray?", "Def Prayer", "Splash", "Offensive Pray", "Time"};
        JTable table = new JTable(stats, header);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getColumnModel().getColumn(1).setCellRenderer(new BufferedImageCellRenderer());
        table.getColumnModel().getColumn(7).setCellRenderer(new BufferedImageCellRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(new BufferedImageCellRenderer());
        table.getColumnModel().getColumn(9).setCellRenderer(new BufferedImageCellRenderer());
        mainPanel.add((Component)new JScrollPane(table), "Center");
        fightLogFrame.add(mainPanel);
        fightLogFrame.setVisible(true);
    }

    static {
        DATE_FORMAT = new SimpleDateFormat("HH:mm:ss 'on' yyyy/MM/dd");
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        normalBorder = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, ColorScheme.DARK_GRAY_COLOR), new EmptyBorder(4, 6, 4, 6));
        hoverBorder = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, ColorScheme.DARK_GRAY_COLOR), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_HOVER_COLOR), new EmptyBorder(3, 5, 3, 5)));
    }

    static class BufferedImageCellRenderer
    extends DefaultTableCellRenderer {
        BufferedImageCellRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            this.setText("");
            this.setIcon(value instanceof BufferedImage ? new ImageIcon((BufferedImage)value) : null);
            return this;
        }
    }
}

