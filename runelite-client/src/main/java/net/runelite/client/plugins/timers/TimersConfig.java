/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.timers;

import java.time.Instant;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="timers")
public interface TimersConfig
extends Config {
    public static final String GROUP = "timers";

    @ConfigItem(keyName="showHomeMinigameTeleports", name="Teleport cooldown timers", description="Configures whether timers for home and minigame teleport cooldowns are displayed")
    default public boolean showHomeMinigameTeleports() {
        return true;
    }

    @ConfigItem(keyName="showAntipoison", name="Antipoison/Venom timers", description="Configures whether timers for poison and venom protection are displayed")
    default public boolean showAntiPoison() {
        return true;
    }

    @ConfigItem(keyName="showAntiFire", name="Antifire timer", description="Configures whether antifire timer is displayed")
    default public boolean showAntiFire() {
        return true;
    }

    @ConfigItem(keyName="showStamina", name="Stamina timer", description="Configures whether stamina timer is displayed")
    default public boolean showStamina() {
        return true;
    }

    @ConfigItem(keyName="showOverload", name="Overload timer", description="Configures whether overload timer is displayed")
    default public boolean showOverload() {
        return true;
    }

    @ConfigItem(keyName="showLiquidAdrenaline", name="Liquid adrenaline timer", description="Configures whether liquid adrenaline timer is displayed")
    default public boolean showLiquidAdrenaline() {
        return true;
    }

    @ConfigItem(keyName="showSilkDressing", name="Silk dressing timer", description="Configures whether silk dressing timer is displayed")
    default public boolean showSilkDressing() {
        return true;
    }

    @ConfigItem(keyName="showBlessedCrystalScarab", name="Blessed crystal scarab timer", description="Configures whether blessed crystal scarab timer is displayed")
    default public boolean showBlessedCrystalScarab() {
        return true;
    }

    @ConfigItem(keyName="showPrayerEnhance", name="Prayer enhance timer", description="Configures whether prayer enhance timer is displayed")
    default public boolean showPrayerEnhance() {
        return true;
    }

    @ConfigItem(keyName="showDivine", name="Divine potion timer", description="Configures whether divine potion timer is displayed")
    default public boolean showDivine() {
        return true;
    }

    @ConfigItem(keyName="showCannon", name="Cannon timer", description="Configures whether cannon timer is displayed")
    default public boolean showCannon() {
        return true;
    }

    @ConfigItem(keyName="showMagicImbue", name="Magic imbue timer", description="Configures whether magic imbue timer is displayed")
    default public boolean showMagicImbue() {
        return true;
    }

    @ConfigItem(keyName="showCharge", name="Charge timer", description="Configures whether to show a timer for the Charge spell")
    default public boolean showCharge() {
        return true;
    }

    @ConfigItem(keyName="showImbuedHeart", name="Imbued heart timer", description="Configures whether imbued heart timer is displayed")
    default public boolean showImbuedHeart() {
        return true;
    }

    @ConfigItem(keyName="showVengeance", name="Vengeance timer", description="Configures whether vengeance and vengeance other timer is displayed")
    default public boolean showVengeance() {
        return true;
    }

    @ConfigItem(keyName="showVengeanceActive", name="Vengeance active", description="Configures whether an indicator for vengeance being active is displayed")
    default public boolean showVengeanceActive() {
        return true;
    }

    @ConfigItem(keyName="showTeleblock", name="Teleblock timer", description="Configures whether teleblock timer is displayed")
    default public boolean showTeleblock() {
        return true;
    }

    @ConfigItem(keyName="showFreezes", name="Freeze timer", description="Configures whether freeze timer is displayed")
    default public boolean showFreezes() {
        return true;
    }

    @ConfigItem(keyName="showGodWarsAltar", name="God wars altar timer", description="Configures whether god wars altar timer is displayed")
    default public boolean showGodWarsAltar() {
        return true;
    }

    @ConfigItem(keyName="showTzhaarTimers", name="Fight Caves and Inferno timers", description="Display elapsed time in the Fight Caves and Inferno")
    default public boolean showTzhaarTimers() {
        return true;
    }

    @ConfigItem(keyName="tzhaarStartTime", name="", description="", hidden=true)
    public Instant tzhaarStartTime();

    @ConfigItem(keyName="tzhaarStartTime", name="", description="")
    public void tzhaarStartTime(Instant var1);

    @ConfigItem(keyName="tzhaarLastTime", name="", description="", hidden=true)
    public Instant tzhaarLastTime();

    @ConfigItem(keyName="tzhaarLastTime", name="", description="")
    public void tzhaarLastTime(Instant var1);

    @ConfigItem(keyName="showStaffOfTheDead", name="Staff of the Dead timer", description="Configures whether staff of the dead timer is displayed")
    default public boolean showStaffOfTheDead() {
        return true;
    }

    @ConfigItem(keyName="showAbyssalSireStun", name="Abyssal Sire stun timer", description="Configures whether Abyssal Sire stun timer is displayed", hidden=true)
    default public boolean showAbyssalSireStun() {
        return false;
    }

    @ConfigItem(keyName="showDfsSpecial", name="Dragonfire Shield special timer", description="Configures whether the special attack cooldown timer for the Dragonfire Shield is displayed")
    default public boolean showDFSSpecial() {
        return true;
    }

    @ConfigItem(keyName="showArceuus", name="Arceuus spells duration", description="Whether to show Arceuus spellbook spell timers")
    default public boolean showArceuus() {
        return true;
    }

    @ConfigItem(keyName="showArceuusCooldown", name="Arceuus spells cooldown", description="Whether to show cooldown timers for Arceuus spellbook spells")
    default public boolean showArceuusCooldown() {
        return false;
    }

    @ConfigItem(keyName="showPickpocketStun", name="Pickpocket stun timer", description="Configures whether pickpocket stun timer is displayed")
    default public boolean showPickpocketStun() {
        return true;
    }

    @ConfigItem(keyName="showPlayerBoosts", name="Player boosts timer", description="Configures whether boosts such as bonus xp timers are displayed")
    default public boolean showPlayerBoosts() {
        return true;
    }
}

