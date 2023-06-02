/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.plugins.external.pvpperformancetracker.RangeAmmoData;
import net.runelite.client.plugins.external.pvpperformancetracker.RingData;

@ConfigGroup(value="pvpperformancetracker")
public interface PvpPerformanceTrackerConfig
extends Config {
    public static final int LEVEL_MIN = 1;
    public static final int LEVEL_MAX = 120;
    @ConfigSection(name="Overlay", description="Contains overlay settings (MAX 5 lines, if you use the detailed one)", position=2, closedByDefault=true)
    public static final String overlay = "overlay";
    @ConfigSection(name="Gear/Ammo", description="Contains gear/ammo settings", position=11, closedByDefault=true)
    public static final String gearAmmo = "gearAmmo";
    @ConfigSection(name="Levels", description="Contains level settings for the deserved damage statistic", position=15, closedByDefault=true)
    public static final String levels = "levels";

    @ConfigItem(keyName="restrictToLms", name="Restrict to LMS", description="Restricts use within the LMS area. WARNING: can be inaccurate outside LMS, as every attack animation's combat style must be manually mapped.", position=0)
    default public boolean restrictToLms() {
        return false;
    }

    @ConfigItem(keyName="showFightHistoryPanel", name="Show Fight History Panel", description="Enables the side-panel which displays previous fight statistics.", position=1)
    default public boolean showFightHistoryPanel() {
        return true;
    }

    @ConfigItem(keyName="showFightOverlay", name="Show Fight Overlay", description="Display an overlay of statistics while fighting.", position=2, section="overlay")
    default public boolean showFightOverlay() {
        return true;
    }

    @ConfigItem(keyName="useSimpleOverlay", name="Use Simple Overlay", description="The overlay will only display off-pray percentage as stats rather than various selected stats.", position=3, section="overlay")
    default public boolean useSimpleOverlay() {
        return false;
    }

    @ConfigItem(keyName="showOverlayTitle", name="Overlay: Show Title", description="The overlay will have a title to display that it is PvP Performance.", position=4, section="overlay")
    default public boolean showOverlayTitle() {
        return false;
    }

    @ConfigItem(keyName="showOverlayNames", name="Overlay: Show Names", description="The overlay will display names. Does not apply to the simple overlay.", position=5, section="overlay")
    default public boolean showOverlayNames() {
        return true;
    }

    @ConfigItem(keyName="showOverlayOffPray", name="Overlay: Show Off-Pray", description="The overlay will display off-pray stats as a fraction & percentage. Does not apply to the simple overlay.", position=6, section="overlay")
    default public boolean showOverlayOffPray() {
        return true;
    }

    @ConfigItem(keyName="showOverlayDeservedDmg", name="Overlay: Show Deserved Dmg", description="The overlay will display deserved damage & difference. Does not apply to the simple overlay.", position=7, section="overlay")
    default public boolean showOverlayDeservedDmg() {
        return true;
    }

    @ConfigItem(keyName="showOverlayDmgDealt", name="Overlay: Show Dmg Dealt", description="The overlay will display damage dealt. Does not apply to the simple overlay.", position=8, section="overlay")
    default public boolean showOverlayDmgDealt() {
        return true;
    }

    @ConfigItem(keyName="showOverlayMagicHits", name="Overlay: Show Magic Hits", description="The overlay will display successful magic hits & deserved magic hits. Does not apply to the simple overlay.", position=9, section="overlay")
    default public boolean showOverlayMagicHits() {
        return true;
    }

    @ConfigItem(keyName="showOverlayOffensivePray", name="Overlay: Show Offensive Pray", description="The overlay will display offensive pray stats. Does not apply to the simple overlay.", position=10, section="overlay")
    default public boolean showOverlayOffensivePray() {
        return false;
    }

    @ConfigItem(keyName="ringChoice", name="Ring Used", description="Rings used for the deserved damage calculation.", position=11, section="gearAmmo")
    default public RingData ringChoice() {
        return RingData.BERSERKER_RING;
    }

    @ConfigItem(keyName="boltChoice", name="RCB Ammo", description="Bolts used for rune crossbow's deserved damage calculation. LMS uses diamond (e). Dragonfire protection not accounted for.", position=12, section="gearAmmo")
    default public RangeAmmoData.BoltAmmo boltChoice() {
        return RangeAmmoData.BoltAmmo.DIAMOND_BOLTS_E;
    }

    @ConfigItem(keyName="strongBoltChoice", name="ACB/DCB/DHCB Ammo", description="Bolts used for ACB/DCB/DHCB's deserved damage calculation. LMS uses regular diamond (e). Dragonfire protection not accounted for.", position=13, section="gearAmmo")
    default public RangeAmmoData.StrongBoltAmmo strongBoltChoice() {
        return RangeAmmoData.StrongBoltAmmo.DIAMOND_BOLTS_E;
    }

    @ConfigItem(keyName="bpDartChoice", name="Blowpipe Ammo", description="Darts used for blowpipe deserved damage calculation.", position=14, section="gearAmmo")
    default public RangeAmmoData.DartAmmo bpDartChoice() {
        return RangeAmmoData.DartAmmo.DRAGON_DARTS;
    }

    @Range(min=1, max=120)
    @ConfigItem(keyName="attackLevel", name="Attack Level", description="Attack level used for the deserved damage calculation (includes potion boost).", position=15, section="levels")
    default public int attackLevel() {
        return 118;
    }

    @Range(min=1, max=120)
    @ConfigItem(keyName="strengthLevel", name="Strength Level", description="Strength level used for the deserved damage calculation (includes potion boost).", position=16, section="levels")
    default public int strengthLevel() {
        return 118;
    }

    @Range(min=1, max=120)
    @ConfigItem(keyName="defenceLevel", name="Defence Level", description="Defence level used for the deserved damage calculation (includes potion boost).", position=17, section="levels")
    default public int defenceLevel() {
        return 75;
    }

    @Range(min=1, max=120)
    @ConfigItem(keyName="rangedLevel", name="Ranged Level", description="Ranged level used for the deserved damage calculation (includes potion boost).", position=18, section="levels")
    default public int rangedLevel() {
        return 112;
    }

    @Range(min=1, max=120)
    @ConfigItem(keyName="magicLevel", name="Magic Level", description="Magic level used for the deserved damage calculation (includes potion boost).", position=19, section="levels")
    default public int magicLevel() {
        return 99;
    }

    @Range(max=10000)
    @ConfigItem(keyName="fightHistoryLimit", name="Fight History Limit", description="Maximum number of previous fights to save and display in the panel. 0 means unlimited. Can cause lag spikes at very high numbers", position=20)
    default public int fightHistoryLimit() {
        return 1000;
    }

    @ConfigItem(keyName="fightLogInChat", name="Fight Log In Chat", description="Display basic fight logs in trade chat during a fight. This is very excessive, mostly for testing/verification.", position=21)
    default public boolean fightLogInChat() {
        return false;
    }
}

