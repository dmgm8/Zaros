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
import java.util.ArrayList;
import net.runelite.api.Player;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.external.pvpperformancetracker.AnimationData;
import net.runelite.client.plugins.external.pvpperformancetracker.FightLogEntry;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpDamageCalc;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Fighter {
    private static final Logger log = LoggerFactory.getLogger(Fighter.class);
    private static final NumberFormat nf = NumberFormat.getInstance();
    private Player player;
    @Expose
    @SerializedName(value="n")
    private String name;
    @Expose
    @SerializedName(value="a")
    private int attackCount;
    @Expose
    @SerializedName(value="s")
    private int offPraySuccessCount;
    @Expose
    @SerializedName(value="d")
    private double deservedDamage;
    @Expose
    @SerializedName(value="h")
    private int damageDealt;
    @Expose
    @SerializedName(value="m")
    private int magicHitCount;
    @Expose
    @SerializedName(value="M")
    private double magicHitCountDeserved;
    @Expose
    @SerializedName(value="p")
    private int offensivePraySuccessCount;
    @Expose
    @SerializedName(value="x")
    private boolean dead;
    @Expose
    @SerializedName(value="l")
    private ArrayList<FightLogEntry> fightLogEntries;
    private PvpDamageCalc pvpDamageCalc;

    Fighter(Player player, ItemManager itemManager) {
        this.player = player;
        this.name = player.getName();
        this.attackCount = 0;
        this.offPraySuccessCount = 0;
        this.deservedDamage = 0.0;
        this.damageDealt = 0;
        this.magicHitCount = 0;
        this.magicHitCountDeserved = 0.0;
        this.offensivePraySuccessCount = 0;
        this.dead = false;
        this.pvpDamageCalc = new PvpDamageCalc(itemManager);
        this.fightLogEntries = new ArrayList();
    }

    Fighter(String name, ArrayList<FightLogEntry> logs) {
        this.player = null;
        this.name = name;
        this.attackCount = 0;
        this.offPraySuccessCount = 0;
        this.deservedDamage = 0.0;
        this.damageDealt = 0;
        this.magicHitCount = 0;
        this.magicHitCountDeserved = 0.0;
        this.dead = false;
        this.fightLogEntries = logs;
    }

    Fighter(String name) {
        this.player = null;
        this.name = name;
        this.attackCount = 0;
        this.offPraySuccessCount = 0;
        this.deservedDamage = 0.0;
        this.damageDealt = 0;
        this.magicHitCount = 0;
        this.magicHitCountDeserved = 0.0;
        this.dead = false;
    }

    void addAttack(boolean successful, Player opponent, AnimationData animationData, int offensivePray) {
        ++this.attackCount;
        if (successful) {
            ++this.offPraySuccessCount;
        }
        if (animationData.attackStyle.isUsingSuccessfulOffensivePray(offensivePray)) {
            ++this.offensivePraySuccessCount;
        }
        this.pvpDamageCalc.updateDamageStats(this.player, opponent, successful, animationData);
        this.deservedDamage += this.pvpDamageCalc.getAverageHit();
        if (animationData.attackStyle == AnimationData.AttackStyle.MAGIC) {
            this.magicHitCountDeserved += this.pvpDamageCalc.getAccuracy();
            if (opponent.getGraphic() != 85) {
                ++this.magicHitCount;
            }
        }
        FightLogEntry fightLogEntry = new FightLogEntry(this.player, opponent, this.pvpDamageCalc, offensivePray);
        if (PvpPerformanceTrackerPlugin.CONFIG.fightLogInChat()) {
            PvpPerformanceTrackerPlugin.PLUGIN.sendChatMessage(fightLogEntry.toChatMessage());
        }
        this.fightLogEntries.add(fightLogEntry);
    }

    void addAttacks(int success, int total, double deservedDamage, int damageDealt, int magicHitCount, double magicHitCountDeserved, int offensivePraySuccessCount) {
        this.offPraySuccessCount += success;
        this.attackCount += total;
        this.deservedDamage += deservedDamage;
        this.damageDealt += damageDealt;
        this.magicHitCount += magicHitCount;
        this.magicHitCountDeserved += magicHitCountDeserved;
        this.offensivePraySuccessCount += offensivePraySuccessCount;
    }

    void addDamageDealt(int damage) {
        this.damageDealt += damage;
    }

    void died() {
        this.dead = true;
    }

    AnimationData getAnimationData() {
        return AnimationData.dataForAnimation(this.player.getAnimation());
    }

    String getOffPrayStats(boolean shortString) {
        nf.setMaximumFractionDigits(0);
        return shortString ? this.offPraySuccessCount + "/" + this.attackCount : nf.format(this.offPraySuccessCount) + "/" + nf.format(this.attackCount) + " (" + Math.round(this.calculateOffPraySuccessPercentage()) + "%)";
    }

    String getOffPrayStats() {
        return this.getOffPrayStats(false);
    }

    String getMagicHitStats() {
        nf.setMaximumFractionDigits(0);
        String stats = nf.format(this.magicHitCount);
        nf.setMaximumFractionDigits(2);
        stats = stats + "/" + nf.format(this.magicHitCountDeserved);
        return stats;
    }

    String getDeservedDmgString(Fighter opponent, int precision, boolean onlyDiff) {
        nf.setMaximumFractionDigits(precision);
        double difference = this.deservedDamage - opponent.deservedDamage;
        return onlyDiff ? (difference > 0.0 ? "+" : "") + nf.format(difference) : nf.format(this.deservedDamage) + " (" + (difference > 0.0 ? "+" : "") + nf.format(difference) + ")";
    }

    String getDeservedDmgString(Fighter opponent) {
        return this.getDeservedDmgString(opponent, 0, false);
    }

    String getDmgDealtString(Fighter opponent, boolean onlyDiff) {
        int difference = this.damageDealt - opponent.damageDealt;
        return onlyDiff ? (difference > 0 ? "+" : "") + difference : this.damageDealt + " (" + (difference > 0 ? "+" : "") + difference + ")";
    }

    String getDmgDealtString(Fighter opponent) {
        return this.getDmgDealtString(opponent, false);
    }

    double calculateOffPraySuccessPercentage() {
        return this.attackCount == 0 ? 0.0 : (double)this.offPraySuccessCount / (double)this.attackCount * 100.0;
    }

    double calculateOffensivePraySuccessPercentage() {
        return this.attackCount == 0 ? 0.0 : (double)this.offensivePraySuccessCount / (double)this.attackCount * 100.0;
    }

    String getOffensivePrayStats(boolean shortString) {
        nf.setMaximumFractionDigits(0);
        return shortString ? this.offensivePraySuccessCount + "/" + this.attackCount : nf.format(this.offensivePraySuccessCount) + "/" + nf.format(this.attackCount) + " (" + Math.round(this.calculateOffensivePraySuccessPercentage()) + "%)";
    }

    String getOffensivePrayStats() {
        return this.getOffensivePrayStats(false);
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getName() {
        return this.name;
    }

    public int getAttackCount() {
        return this.attackCount;
    }

    public int getOffPraySuccessCount() {
        return this.offPraySuccessCount;
    }

    public double getDeservedDamage() {
        return this.deservedDamage;
    }

    public int getDamageDealt() {
        return this.damageDealt;
    }

    public int getMagicHitCount() {
        return this.magicHitCount;
    }

    public double getMagicHitCountDeserved() {
        return this.magicHitCountDeserved;
    }

    public int getOffensivePraySuccessCount() {
        return this.offensivePraySuccessCount;
    }

    public boolean isDead() {
        return this.dead;
    }

    public ArrayList<FightLogEntry> getFightLogEntries() {
        return this.fightLogEntries;
    }

    public PvpDamageCalc getPvpDamageCalc() {
        return this.pvpDamageCalc;
    }

    static {
        nf.setMaximumFractionDigits(1);
        nf.setRoundingMode(RoundingMode.HALF_UP);
    }
}

