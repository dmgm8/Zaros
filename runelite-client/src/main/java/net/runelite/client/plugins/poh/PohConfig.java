/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.poh;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="poh")
public interface PohConfig
extends Config {
    @ConfigItem(keyName="showPortals", name="Show Portals", description="Configures whether to display teleport portals")
    default public boolean showPortals() {
        return true;
    }

    @ConfigItem(keyName="showAltar", name="Show Altar", description="Configures whether or not the altar is displayed")
    default public boolean showAltar() {
        return true;
    }

    @ConfigItem(keyName="showGlory", name="Show Glory mount", description="Configures whether or not the mounted glory is displayed")
    default public boolean showGlory() {
        return true;
    }

    @ConfigItem(keyName="showPools", name="Show Pools", description="Configures whether or not the pools are displayed")
    default public boolean showPools() {
        return true;
    }

    @ConfigItem(keyName="showRepairStand", name="Show Repair stand", description="Configures whether or not the repair stand is displayed")
    default public boolean showRepairStand() {
        return true;
    }

    @ConfigItem(keyName="showExitPortal", name="Show Exit portal", description="Configures whether or not the exit portal is displayed")
    default public boolean showExitPortal() {
        return true;
    }

    @ConfigItem(keyName="showBurner", name="Show Incense Burner timers", description="Configures whether or not unlit/lit burners are displayed")
    default public boolean showBurner() {
        return true;
    }

    @ConfigItem(keyName="showSpellbook", name="Show Spellbook altar", description="Configures whether or not the Spellbook altar is displayed")
    default public boolean showSpellbook() {
        return true;
    }

    @ConfigItem(keyName="showJewelleryBox", name="Show Jewellery Box", description="Configures whether or not the jewellery box is displayed")
    default public boolean showJewelleryBox() {
        return true;
    }

    @ConfigItem(keyName="showMagicTravel", name="Show Fairy/ Spirit Tree/ Obelisk", description="Configures whether or not the Fairy ring, Spirit tree or Obelisk is displayed")
    default public boolean showMagicTravel() {
        return true;
    }

    @ConfigItem(keyName="showPortalNexus", name="Show Portal Nexus", description="Configures whether or not the Portal Nexus is displayed")
    default public boolean showPortalNexus() {
        return true;
    }

    @ConfigItem(keyName="showDigsitePendant", name="Show Digsite Pendant", description="Configures whether or not the Digsite Pendant is displayed")
    default public boolean showDigsitePendant() {
        return true;
    }

    @ConfigItem(keyName="showXericsTalisman", name="Show Xeric's Talisman", description="Configures whether or not the Xeric's Talisman is displayed")
    default public boolean showXericsTalisman() {
        return true;
    }

    @ConfigItem(keyName="showMythicalCape", name="Show Mythical Cape", description="Configures whether or not the Mythical Cape is displayed")
    default public boolean showMythicalCape() {
        return true;
    }
}

