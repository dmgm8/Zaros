/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.party;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup(value="party")
public interface PartyConfig
extends Config {
    public static final String GROUP = "party";
    @ConfigSection(name="Player Status Overlay", description="Player status such as health, prayer, and special attack energy drawn on player models.", position=5)
    public static final String SECTION_STATUS_OVERLAY = "statusOverlay";

    @ConfigItem(keyName="pings", name="Pings", description="Enables party pings", position=1)
    default public boolean pings() {
        return true;
    }

    @ConfigItem(keyName="sounds", name="Sound on ping", description="Enables sound notification on party ping", position=2)
    default public boolean sounds() {
        return true;
    }

    @ConfigItem(keyName="recolorNames", name="Recolor names", description="Recolor party members names based on unique color hash", position=3)
    default public boolean recolorNames() {
        return true;
    }

    @ConfigItem(keyName="pingHotkey", name="Ping hotkey", description="Key to hold to send a tile ping", position=4)
    default public Keybind pingHotkey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(section="statusOverlay", keyName="statusOverlayHealth", name="Show Health", description="Show health of party members on the player model.", position=6)
    default public boolean statusOverlayHealth() {
        return false;
    }

    @ConfigItem(section="statusOverlay", keyName="statusOverlayPrayer", name="Show Prayer", description="Show prayer of party members on the player model.", position=7)
    default public boolean statusOverlayPrayer() {
        return false;
    }

    @ConfigItem(section="statusOverlay", keyName="statusOverlayStamina", name="Show Run Energy", description="Show run energy (stamina) of party members on the player model.", position=8)
    default public boolean statusOverlayStamina() {
        return false;
    }

    @ConfigItem(section="statusOverlay", keyName="statusOverlaySpec", name="Show Spec Energy", description="Show special attack energy of party members on the player model.", position=9)
    default public boolean statusOverlaySpec() {
        return false;
    }

    @ConfigItem(section="statusOverlay", keyName="statusOverlayVeng", name="Show Vengeance", description="Show vengeance status (active/inactive) of party members on the player model.", position=10)
    default public boolean statusOverlayVeng() {
        return true;
    }

    @ConfigItem(section="statusOverlay", keyName="statusOverlayRenderSelf", name="Show On Self", description="Show above activated status overlays on your local player.", position=11)
    default public boolean statusOverlayRenderSelf() {
        return true;
    }

    @ConfigItem(keyName="previousPartyId", name="", description="", hidden=true)
    default public String previousPartyId() {
        return "";
    }

    @ConfigItem(keyName="previousPartyId", name="", description="", hidden=true)
    public void setPreviousPartyId(String var1);
}

