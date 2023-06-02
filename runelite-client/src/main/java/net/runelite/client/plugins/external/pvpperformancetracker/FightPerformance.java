/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 *  com.google.gson.annotations.SerializedName
 *  net.runelite.api.Player
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import net.runelite.api.Player;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.external.pvpperformancetracker.AnimationData;
import net.runelite.client.plugins.external.pvpperformancetracker.FightLogEntry;
import net.runelite.client.plugins.external.pvpperformancetracker.Fighter;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FightPerformance
implements Comparable<FightPerformance> {
    private static final Logger log = LoggerFactory.getLogger(FightPerformance.class);
    private static final Duration NEW_FIGHT_DELAY = Duration.ofSeconds(21L);
    private static final NumberFormat nf = NumberFormat.getInstance();
    @Expose
    @SerializedName(value="c")
    private Fighter competitor;
    @Expose
    @SerializedName(value="o")
    private Fighter opponent;
    @Expose
    @SerializedName(value="t")
    private long lastFightTime;

    static FightPerformance getTestInstance() {
        int cTotal = (int)(Math.random() * 60.0) + 8;
        int cSuccess = (int)(Math.random() * (double)(cTotal - 4)) + 4;
        double cDamage = Math.random() * (double)(cSuccess * 25);
        int oTotal = (int)(Math.random() * 60.0) + 8;
        int oSuccess = (int)(Math.random() * (double)(oTotal - 4)) + 4;
        double oDamage = Math.random() * (double)(oSuccess * 25);
        int secOffset = (int)(Math.random() * 57600.0) - 28800;
        boolean cDead = Math.random() >= 0.5;
        ArrayList<FightLogEntry> fightLogEntries = new ArrayList<FightLogEntry>();
        int[] attackerItems = new int[]{0, 0, 0};
        int[] defenderItems = new int[]{0, 0, 0};
        String attackerName = "testname";
        FightLogEntry fightLogEntry = new FightLogEntry(attackerItems, 21, 0.5, 1, 12, defenderItems, attackerName);
        FightLogEntry fightLogEntry2 = new FightLogEntry(attackerItems, 11, 0.2, 1, 41, defenderItems, attackerName);
        FightLogEntry fightLogEntry3 = new FightLogEntry(attackerItems, 12, 0.3, 1, 21, defenderItems, attackerName);
        FightLogEntry fightLogEntry4 = new FightLogEntry(attackerItems, 43, 0.1, 1, 23, defenderItems, attackerName);
        fightLogEntries.add(fightLogEntry);
        fightLogEntries.add(fightLogEntry2);
        fightLogEntries.add(fightLogEntry3);
        fightLogEntries.add(fightLogEntry4);
        return new FightPerformance("Matsyir", "TEST_DATA", cSuccess, cTotal, cDamage, oSuccess, oTotal, oDamage, cDead, secOffset, fightLogEntries);
    }

    FightPerformance(Player competitor, Player opponent, ItemManager itemManager) {
        this.competitor = new Fighter(competitor, itemManager);
        this.opponent = new Fighter(opponent, itemManager);
        this.lastFightTime = Instant.now().minusSeconds(NEW_FIGHT_DELAY.getSeconds() - 5L).toEpochMilli();
    }

    private FightPerformance(String cName, String oName, int cSuccess, int cTotal, double cDamage, int oSuccess, int oTotal, double oDamage, boolean cDead, int secondOffset, ArrayList<FightLogEntry> fightLogs) {
        this.competitor = new Fighter(cName, fightLogs);
        this.opponent = new Fighter(oName, fightLogs);
        this.competitor.addAttacks(cSuccess, cTotal, cDamage, (int)cDamage, 12, 13.0, 11);
        this.opponent.addAttacks(oSuccess, oTotal, oDamage, (int)oDamage, 14, 13.0, 11);
        if (cDead) {
            this.competitor.died();
        } else {
            this.opponent.died();
        }
        this.lastFightTime = Instant.now().minusSeconds(secondOffset).toEpochMilli();
    }

    void checkForAttackAnimations(String playerName) {
        AnimationData animationData;
        if (playerName == null) {
            return;
        }
        if (playerName.equals(this.competitor.getName()) && this.opponent.getPlayer().equals((Object)this.competitor.getPlayer().getInteracting())) {
            AnimationData animationData2 = this.competitor.getAnimationData();
            if (animationData2 != null) {
                int pray = PvpPerformanceTrackerPlugin.PLUGIN.currentlyUsedOffensivePray();
                this.competitor.addAttack(this.opponent.getPlayer().getOverheadIcon() != animationData2.attackStyle.getProtection(), this.opponent.getPlayer(), animationData2, pray);
                this.lastFightTime = Instant.now().toEpochMilli();
            }
        } else if (playerName.equals(this.opponent.getName()) && this.competitor.getPlayer().equals((Object)this.opponent.getPlayer().getInteracting()) && (animationData = this.opponent.getAnimationData()) != null) {
            this.opponent.addAttack(this.competitor.getPlayer().getOverheadIcon() != animationData.attackStyle.getProtection(), this.competitor.getPlayer(), animationData, 0);
            this.lastFightTime = Instant.now().toEpochMilli();
        }
    }

    void addDamageDealt(String playerName, int damage) {
        if (playerName == null) {
            return;
        }
        if (playerName.equals(this.competitor.getName())) {
            this.opponent.addDamageDealt(damage);
        } else if (playerName.equals(this.opponent.getName())) {
            this.competitor.addDamageDealt(damage);
        }
    }

    boolean isFightOver() {
        boolean isOver = false;
        if (this.opponent.getPlayer().getAnimation() == 836) {
            this.opponent.died();
            isOver = true;
        }
        if (this.competitor.getPlayer().getAnimation() == 836) {
            this.competitor.died();
            isOver = true;
        }
        if (Duration.between(Instant.ofEpochMilli(this.lastFightTime), Instant.now()).compareTo(NEW_FIGHT_DELAY) > 0) {
            isOver = true;
        }
        if (isOver) {
            this.lastFightTime = Instant.now().toEpochMilli();
        }
        return isOver;
    }

    ArrayList<FightLogEntry> getAllFightLogEntries() {
        if (this.competitor.getFightLogEntries() == null || this.opponent.getFightLogEntries() == null) {
            return new ArrayList<FightLogEntry>();
        }
        ArrayList<FightLogEntry> combinedList = new ArrayList<FightLogEntry>();
        combinedList.addAll(this.competitor.getFightLogEntries());
        combinedList.addAll(this.opponent.getFightLogEntries());
        combinedList.sort(FightLogEntry::compareTo);
        return combinedList;
    }

    boolean fightStarted() {
        return this.competitor.getAttackCount() > 0;
    }

    boolean competitorOffPraySuccessIsGreater() {
        return this.competitor.calculateOffPraySuccessPercentage() > this.opponent.calculateOffPraySuccessPercentage();
    }

    boolean opponentOffPraySuccessIsGreater() {
        return this.opponent.calculateOffPraySuccessPercentage() > this.competitor.calculateOffPraySuccessPercentage();
    }

    boolean competitorDeservedDmgIsGreater() {
        return this.competitor.getDeservedDamage() > this.opponent.getDeservedDamage();
    }

    boolean opponentDeservedDmgIsGreater() {
        return this.opponent.getDeservedDamage() > this.competitor.getDeservedDamage();
    }

    boolean competitorDmgDealtIsGreater() {
        return this.competitor.getDamageDealt() > this.opponent.getDamageDealt();
    }

    boolean opponentDmgDealtIsGreater() {
        return this.opponent.getDamageDealt() > this.competitor.getDamageDealt();
    }

    boolean competitorMagicHitsLuckier() {
        double competitorRate = this.competitor.getMagicHitCountDeserved() == 0.0 ? 0.0 : (double)this.competitor.getMagicHitCount() / this.competitor.getMagicHitCountDeserved();
        double opponentRate = this.opponent.getMagicHitCountDeserved() == 0.0 ? 0.0 : (double)this.opponent.getMagicHitCount() / this.opponent.getMagicHitCountDeserved();
        return competitorRate > opponentRate;
    }

    boolean opponentMagicHitsLuckier() {
        double competitorRate = this.competitor.getMagicHitCountDeserved() == 0.0 ? 0.0 : (double)this.competitor.getMagicHitCount() / this.competitor.getMagicHitCountDeserved();
        double opponentRate = this.opponent.getMagicHitCountDeserved() == 0.0 ? 0.0 : (double)this.opponent.getMagicHitCount() / this.opponent.getMagicHitCountDeserved();
        return opponentRate > competitorRate;
    }

    public double getCompetitorDeservedDmgDiff() {
        return this.competitor.getDeservedDamage() - this.opponent.getDeservedDamage();
    }

    public double getCompetitorDmgDealtDiff() {
        return this.competitor.getDamageDealt() - this.opponent.getDamageDealt();
    }

    @Override
    public int compareTo(FightPerformance o) {
        long diff = this.lastFightTime - o.lastFightTime;
        return diff == 0L ? 0 : (int)(diff / Math.abs(diff));
    }

    public Fighter getCompetitor() {
        return this.competitor;
    }

    public Fighter getOpponent() {
        return this.opponent;
    }

    public long getLastFightTime() {
        return this.lastFightTime;
    }

    static {
        nf.setMaximumFractionDigits(1);
        nf.setRoundingMode(RoundingMode.HALF_UP);
    }
}

