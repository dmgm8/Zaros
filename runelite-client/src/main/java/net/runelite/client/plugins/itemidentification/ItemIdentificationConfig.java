/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.itemidentification;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.itemidentification.ItemIdentificationMode;

@ConfigGroup(value="itemidentification")
public interface ItemIdentificationConfig
extends Config {
    @ConfigSection(name="Categories", description="The categories of items to identify", position=99)
    public static final String identificationSection = "identification";

    @ConfigItem(keyName="identificationType", name="Identification Type", position=-4, description="How much to show of the item name")
    default public ItemIdentificationMode identificationType() {
        return ItemIdentificationMode.SHORT;
    }

    @ConfigItem(keyName="textColor", name="Color", position=-3, description="The colour of the identification text")
    default public Color textColor() {
        return Color.WHITE;
    }

    @ConfigItem(keyName="showHerbSeeds", name="Seeds (Herb)", description="Show identification on Herb Seeds", section="identification")
    default public boolean showHerbSeeds() {
        return true;
    }

    @ConfigItem(keyName="showAllotmentSeeds", name="Seeds (Allotment)", description="Show identification on Allotment Seeds", section="identification")
    default public boolean showAllotmentSeeds() {
        return false;
    }

    @ConfigItem(keyName="showFlowerSeeds", name="Seeds (Flower)", description="Show identification on Flower Seeds", section="identification")
    default public boolean showFlowerSeeds() {
        return false;
    }

    @ConfigItem(keyName="showFruitTreeSeeds", name="Seeds (Fruit Tree)", description="Show identification on Fruit Tree Seeds", section="identification")
    default public boolean showFruitTreeSeeds() {
        return false;
    }

    @ConfigItem(keyName="showTreeSeeds", name="Seeds (Tree)", description="Show identification on Tree Seeds", section="identification")
    default public boolean showTreeSeeds() {
        return false;
    }

    @ConfigItem(keyName="showSpecialSeeds", name="Seeds (Special)", description="Show identification on Special Seeds", section="identification")
    default public boolean showSpecialSeeds() {
        return false;
    }

    @ConfigItem(keyName="showBerrySeeds", name="Seeds (Berry)", description="Show identification on Berry Seeds", section="identification")
    default public boolean showBerrySeeds() {
        return false;
    }

    @ConfigItem(keyName="showHopSeeds", name="Seeds (Hops)", description="Show identification on Hops Seeds", section="identification")
    default public boolean showHopsSeeds() {
        return false;
    }

    @ConfigItem(keyName="showSacks", name="Sacks", description="Show identification on Sacks", section="identification")
    default public boolean showSacks() {
        return false;
    }

    @ConfigItem(keyName="showHerbs", name="Herbs", description="Show identification on Herbs", section="identification")
    default public boolean showHerbs() {
        return false;
    }

    @ConfigItem(keyName="showLogs", name="Logs", description="Show identification on Logs", section="identification")
    default public boolean showLogs() {
        return false;
    }

    @ConfigItem(keyName="showPyreLogs", name="Logs (Pyre)", description="Show identification on Pyre Logs", section="identification")
    default public boolean showPyreLogs() {
        return false;
    }

    @ConfigItem(keyName="showPlanks", name="Planks", description="Show identification on Planks", section="identification")
    default public boolean showPlanks() {
        return false;
    }

    @ConfigItem(keyName="showSaplings", name="Saplings", description="Show identification on Saplings and Seedlings", section="identification")
    default public boolean showSaplings() {
        return true;
    }

    @ConfigItem(keyName="showComposts", name="Composts", description="Show identification on Composts", section="identification")
    default public boolean showComposts() {
        return false;
    }

    @ConfigItem(keyName="showOres", name="Ores", description="Show identification on Ores", section="identification")
    default public boolean showOres() {
        return false;
    }

    @ConfigItem(keyName="showBars", name="Bars", description="Show identification on Bars", section="identification")
    default public boolean showBars() {
        return false;
    }

    @ConfigItem(keyName="showGems", name="Gems", description="Show identification on Gems", section="identification")
    default public boolean showGems() {
        return false;
    }

    @ConfigItem(keyName="showPotions", name="Potions", description="Show identification on Potions", section="identification")
    default public boolean showPotions() {
        return false;
    }

    @ConfigItem(keyName="showImplingJars", name="Impling jars", description="Show identification on Impling jars", section="identification")
    default public boolean showImplingJars() {
        return false;
    }

    @ConfigItem(keyName="showTablets", name="Tablets", description="Show identification on Tablets", section="identification")
    default public boolean showTablets() {
        return false;
    }

    @ConfigItem(keyName="showTeleportScrolls", name="Teleport Scrolls", description="Show identification on teleport scrolls", section="identification")
    default public boolean showTeleportScrolls() {
        return false;
    }

    @ConfigItem(keyName="showJewellery", name="Jewellery (Unenchanted)", description="Show identification on unenchanted jewellery", section="identification")
    default public boolean showJewellery() {
        return false;
    }

    @ConfigItem(keyName="showEnchantedJewellery", name="Jewellery (Enchanted)", description="Show identification on enchanted jewellery", section="identification")
    default public boolean showEnchantedJewellery() {
        return false;
    }
}

