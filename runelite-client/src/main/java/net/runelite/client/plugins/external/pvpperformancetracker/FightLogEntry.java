/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 *  com.google.gson.annotations.SerializedName
 *  net.runelite.api.HeadIcon
 *  net.runelite.api.Player
 *  org.apache.commons.text.WordUtils
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Instant;
import net.runelite.api.HeadIcon;
import net.runelite.api.Player;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.plugins.external.pvpperformancetracker.AnimationData;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpDamageCalc;
import org.apache.commons.text.WordUtils;

public class FightLogEntry
implements Comparable<FightLogEntry> {
    public static final NumberFormat nf = NumberFormat.getInstance();
    public String attackerName;
    @Expose
    @SerializedName(value="t")
    private long time;
    @Expose
    @SerializedName(value="G")
    private int[] attackerGear;
    @Expose
    @SerializedName(value="O")
    private HeadIcon attackerOverhead;
    @Expose
    @SerializedName(value="m")
    private AnimationData animationData;
    @Expose
    @SerializedName(value="d")
    private double deservedDamage;
    @Expose
    @SerializedName(value="a")
    private double accuracy;
    @Expose
    @SerializedName(value="h")
    private int maxHit;
    @Expose
    @SerializedName(value="l")
    private int minHit;
    @Expose
    @SerializedName(value="s")
    private boolean splash;
    @Expose
    @SerializedName(value="g")
    private int[] defenderGear;
    @Expose
    @SerializedName(value="o")
    private HeadIcon defenderOverhead;
    @Expose
    @SerializedName(value="p")
    private int attackerOffensivePray;

    public FightLogEntry(Player attacker, Player defender, PvpDamageCalc pvpDamageCalc, int attackerOffensivePray) {
        this.attackerName = attacker.getName();
        this.attackerGear = attacker.getPlayerComposition().getEquipmentIds();
        this.attackerOverhead = attacker.getOverheadIcon();
        this.animationData = AnimationData.dataForAnimation(attacker.getAnimation());
        this.deservedDamage = pvpDamageCalc.getAverageHit();
        this.accuracy = pvpDamageCalc.getAccuracy();
        this.minHit = pvpDamageCalc.getMinHit();
        this.maxHit = pvpDamageCalc.getMaxHit();
        this.splash = this.animationData.attackStyle == AnimationData.AttackStyle.MAGIC && defender.getGraphic() == 85;
        this.time = Instant.now().toEpochMilli();
        this.defenderGear = defender.getPlayerComposition().getEquipmentIds();
        this.defenderOverhead = defender.getOverheadIcon();
        this.attackerOffensivePray = attackerOffensivePray;
    }

    public FightLogEntry(int[] attackerGear, int deservedDamage, double accuracy, int minHit, int maxHit, int[] defenderGear, String attackerName) {
        this.attackerName = attackerName;
        this.attackerGear = attackerGear;
        this.attackerOverhead = HeadIcon.MAGIC;
        this.animationData = Math.random() <= 0.5 ? AnimationData.MELEE_DAGGER_SLASH : AnimationData.MAGIC_ANCIENT_MULTI_TARGET;
        this.deservedDamage = deservedDamage;
        this.accuracy = accuracy;
        this.minHit = minHit;
        this.maxHit = maxHit;
        this.splash = Math.random() >= 0.5;
        this.time = Instant.now().toEpochMilli();
        this.defenderGear = defenderGear;
        this.defenderOverhead = HeadIcon.MAGIC;
    }

    public boolean success() {
        return this.animationData.attackStyle.getProtection() != this.defenderOverhead;
    }

    public String toChatMessage() {
        Color darkRed = new Color(127, 0, 0);
        return new ChatMessageBuilder().append(darkRed, this.attackerName + ": ").append(Color.BLACK, "Style: ").append(darkRed, WordUtils.capitalizeFully((String)this.animationData.attackStyle.toString())).append(Color.BLACK, "  Hit: ").append(darkRed, this.getHitRange()).append(Color.BLACK, "  Acc: ").append(darkRed, nf.format(this.accuracy)).append(Color.BLACK, "  AvgHit: ").append(darkRed, nf.format(this.deservedDamage)).append(Color.BLACK, " Spec?: ").append(darkRed, this.animationData.isSpecial ? "Y" : "N").append(Color.BLACK, " OffP?:").append(darkRed, this.success() ? "Y" : "N").build();
    }

    String getHitRange() {
        return this.minHit + "-" + this.maxHit;
    }

    @Override
    public int compareTo(FightLogEntry o) {
        long diff = this.time - o.time;
        return diff == 0L ? 0 : (int)(diff / Math.abs(diff));
    }

    public String getAttackerName() {
        return this.attackerName;
    }

    public long getTime() {
        return this.time;
    }

    public int[] getAttackerGear() {
        return this.attackerGear;
    }

    public HeadIcon getAttackerOverhead() {
        return this.attackerOverhead;
    }

    public AnimationData getAnimationData() {
        return this.animationData;
    }

    public double getDeservedDamage() {
        return this.deservedDamage;
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public int getMaxHit() {
        return this.maxHit;
    }

    public int getMinHit() {
        return this.minHit;
    }

    public boolean isSplash() {
        return this.splash;
    }

    public int[] getDefenderGear() {
        return this.defenderGear;
    }

    public HeadIcon getDefenderOverhead() {
        return this.defenderOverhead;
    }

    public int getAttackerOffensivePray() {
        return this.attackerOffensivePray;
    }

    static {
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setMaximumFractionDigits(2);
    }
}

