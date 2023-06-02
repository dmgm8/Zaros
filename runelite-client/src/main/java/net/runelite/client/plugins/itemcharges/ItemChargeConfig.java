/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.itemcharges;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(value="itemCharge")
public interface ItemChargeConfig
extends Config {
    public static final String GROUP = "itemCharge";
    public static final String KEY_AMULET_OF_BOUNTY = "amuletOfBounty";
    public static final String KEY_AMULET_OF_CHEMISTRY = "amuletOfChemistry";
    public static final String KEY_BINDING_NECKLACE = "bindingNecklace";
    public static final String KEY_BRACELET_OF_SLAUGHTER = "braceletOfSlaughter";
    public static final String KEY_CHRONICLE = "chronicle";
    public static final String KEY_DODGY_NECKLACE = "dodgyNecklace";
    public static final String KEY_EXPEDITIOUS_BRACELET = "expeditiousBracelet";
    public static final String KEY_EXPLORERS_RING = "explorerRing";
    public static final String KEY_RING_OF_FORGING = "ringOfForging";
    public static final String KEY_BLOOD_ESSENCE = "bloodEssence";
    public static final String KEY_BRACELET_OF_CLAY = "braceletOfClay";
    @ConfigSection(name="Charge Settings", description="Configuration for which charges should be displayed", position=98)
    public static final String chargesSection = "charges";
    @ConfigSection(name="Notification Settings", description="Configuration for notifications", position=99)
    public static final String notificationSection = "notifications";

    @ConfigItem(keyName="veryLowWarningColor", name="Very Low Warning", description="The color of the overlay when charges are very low", position=1)
    default public Color veryLowWarningColor() {
        return Color.RED;
    }

    @ConfigItem(keyName="lowWarningColor", name="Low Warning", description="The color of the overlay when charges are low", position=2)
    default public Color lowWarningolor() {
        return Color.YELLOW;
    }

    @ConfigItem(keyName="veryLowWarning", name="Very Low Warning", description="The charge count for the very low warning color", position=3)
    default public int veryLowWarning() {
        return 1;
    }

    @ConfigItem(keyName="lowWarning", name="Low Warning", description="The charge count for the low warning color", position=4)
    default public int lowWarning() {
        return 2;
    }

    @ConfigItem(keyName="showTeleportCharges", name="Teleport Charges", description="Show teleport item charge counts", position=5, section="charges")
    default public boolean showTeleportCharges() {
        return true;
    }

    @ConfigItem(keyName="showDodgyCount", name="Dodgy Necklace Count", description="Show Dodgy necklace charges", position=6, section="charges")
    default public boolean showDodgyCount() {
        return true;
    }

    @ConfigItem(keyName="dodgyNotification", name="Dodgy Necklace Notification", description="Send a notification when a Dodgy necklace breaks", position=7, section="notifications")
    default public boolean dodgyNotification() {
        return true;
    }

    @ConfigItem(keyName="showImpCharges", name="Imp-in-a-box charges", description="Show Imp-in-a-box item charges", position=8, section="charges")
    default public boolean showImpCharges() {
        return true;
    }

    @ConfigItem(keyName="showFungicideCharges", name="Fungicide Charges", description="Show Fungicide item charges", position=9, section="charges")
    default public boolean showFungicideCharges() {
        return true;
    }

    @ConfigItem(keyName="showWateringCanCharges", name="Watering Can Charges", description="Show Watering can item charges", position=10, section="charges")
    default public boolean showWateringCanCharges() {
        return true;
    }

    @ConfigItem(keyName="showWaterskinCharges", name="Waterskin Charges", description="Show Waterskin dose counts", position=11, section="charges")
    default public boolean showWaterskinCharges() {
        return true;
    }

    @ConfigItem(keyName="showBellowCharges", name="Bellows Charges", description="Show Ogre bellows item charges", position=12, section="charges")
    default public boolean showBellowCharges() {
        return true;
    }

    @ConfigItem(keyName="showBasketCharges", name="Basket Charges", description="Show Fruit basket item counts", position=13, section="charges")
    default public boolean showBasketCharges() {
        return true;
    }

    @ConfigItem(keyName="showSackCharges", name="Sack Charges", description="Show Sack item counts", position=14, section="charges")
    default public boolean showSackCharges() {
        return true;
    }

    @ConfigItem(keyName="showAbyssalBraceletCharges", name="Abyssal Bracelet Charges", description="Show Abyssal bracelet item charges", position=15, section="charges")
    default public boolean showAbyssalBraceletCharges() {
        return true;
    }

    @ConfigItem(keyName="showAmuletOfChemistryCharges", name="Amulet of Chemistry Charges", description="Show Amulet of chemistry item charges", position=16, section="charges")
    default public boolean showAmuletOfChemistryCharges() {
        return true;
    }

    @ConfigItem(keyName="showAmuletOfBountyCharges", name="Amulet of Bounty Charges", description="Show Amulet of bounty item charges", position=17, section="charges")
    default public boolean showAmuletOfBountyCharges() {
        return true;
    }

    @ConfigItem(keyName="recoilNotification", name="Ring of Recoil Notification", description="Send a notification when a Ring of recoil breaks", position=18, section="notifications")
    default public boolean recoilNotification() {
        return false;
    }

    @ConfigItem(keyName="showBindingNecklaceCharges", name="Binding Necklace Charges", description="Show Binding necklace item charges", position=19, section="charges")
    default public boolean showBindingNecklaceCharges() {
        return true;
    }

    @ConfigItem(keyName="bindingNotification", name="Binding Necklace Notification", description="Send a notification when a Binding necklace breaks", position=20, section="notifications")
    default public boolean bindingNotification() {
        return true;
    }

    @ConfigItem(keyName="showExplorerRingCharges", name="Explorer's Ring Alch Charges", description="Show Explorer's ring alchemy charges", position=21, section="charges")
    default public boolean showExplorerRingCharges() {
        return true;
    }

    @ConfigItem(keyName="showRingOfForgingCount", name="Ring of Forging Charges", description="Show Ring of forging item charges", position=22, section="charges")
    default public boolean showRingOfForgingCount() {
        return true;
    }

    @ConfigItem(keyName="ringOfForgingNotification", name="Ring of Forging Notification", description="Send a notification when a Ring of forging breaks", position=23, section="notifications")
    default public boolean ringOfForgingNotification() {
        return true;
    }

    @ConfigItem(keyName="showInfoboxes", name="Infoboxes", description="Show an infobox with remaining charges for equipped items", position=24)
    default public boolean showInfoboxes() {
        return false;
    }

    @ConfigItem(keyName="showPotionDoseCount", name="Potion Doses", description="Show remaining potion doses", position=25, section="charges")
    default public boolean showPotionDoseCount() {
        return false;
    }

    @ConfigItem(keyName="showBraceletOfSlaughterCharges", name="Bracelet of Slaughter Charges", description="Show Bracelet of Slaughter item charges", position=26, section="charges")
    default public boolean showBraceletOfSlaughterCharges() {
        return true;
    }

    @ConfigItem(keyName="slaughterNotification", name="Bracelet of Slaughter Notification", description="Send a notification when a Bracelet of Slaughter breaks", position=27, section="notifications")
    default public boolean slaughterNotification() {
        return true;
    }

    @ConfigItem(keyName="showExpeditiousBraceletCharges", name="Expeditious Bracelet Charges", description="Show Expeditious Bracelet item charges", position=28, section="charges")
    default public boolean showExpeditiousBraceletCharges() {
        return true;
    }

    @ConfigItem(keyName="expeditiousNotification", name="Expeditious Bracelet Notification", description="Send a notification when an Expeditious Bracelet breaks", position=29, section="notifications")
    default public boolean expeditiousNotification() {
        return true;
    }

    @ConfigItem(keyName="showGuthixRestDoses", name="Guthix Rest Doses", description="Show Guthix Rest doses", position=29, section="charges")
    default public boolean showGuthixRestDoses() {
        return true;
    }

    @ConfigItem(keyName="showBloodEssenceCharges", name="Blood Essence Charges", description="Show Blood Essence charges", position=30, section="charges")
    default public boolean showBloodEssenceCharges() {
        return true;
    }

    @ConfigItem(keyName="showBraceletOfClayCharges", name="Bracelet of Clay Charges", description="Show Bracelet of Clay item charges", position=31, section="charges")
    default public boolean showBraceletOfClayCharges() {
        return true;
    }

    @ConfigItem(keyName="braceletOfClayNotification", name="Bracelet of Clay Notification", description="Send a notification when a Bracelet of Clay breaks", position=32, section="notifications")
    default public boolean braceletOfClayNotification() {
        return true;
    }
}

