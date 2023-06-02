/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.runecraft;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(value="runecraft")
public interface RunecraftConfig
extends Config {
    public static final String GROUP = "runecraft";
    @ConfigSection(name="Rift Settings", description="Abyss rift overlay settings", position=99)
    public static final String riftSection = "rifts";

    @ConfigItem(keyName="showRifts", name="Show Rifts in Abyss", description="Configures whether the rifts in the abyss will be displayed", position=2, section="rifts")
    default public boolean showRifts() {
        return true;
    }

    @ConfigItem(keyName="showClickBox", name="Show Rift click box", description="Configures whether to display the click box of the rift", position=3, section="rifts")
    default public boolean showClickBox() {
        return true;
    }

    @ConfigItem(keyName="showAir", name="Show Air rift", description="Configures whether to display the air rift", position=4, section="rifts")
    default public boolean showAir() {
        return true;
    }

    @ConfigItem(keyName="showBlood", name="Show Blood rift", description="Configures whether to display the Blood rift", position=5, section="rifts")
    default public boolean showBlood() {
        return true;
    }

    @ConfigItem(keyName="showBody", name="Show Body rift", description="Configures whether to display the Body rift", position=6, section="rifts")
    default public boolean showBody() {
        return true;
    }

    @ConfigItem(keyName="showChaos", name="Show Chaos rift", description="Configures whether to display the Chaos rift", position=7, section="rifts")
    default public boolean showChaos() {
        return true;
    }

    @ConfigItem(keyName="showCosmic", name="Show Cosmic rift", description="Configures whether to display the Cosmic rift", position=8, section="rifts")
    default public boolean showCosmic() {
        return true;
    }

    @ConfigItem(keyName="showDeath", name="Show Death rift", description="Configures whether to display the Death rift", position=9, section="rifts")
    default public boolean showDeath() {
        return true;
    }

    @ConfigItem(keyName="showEarth", name="Show Earth rift", description="Configures whether to display the Earth rift", position=10, section="rifts")
    default public boolean showEarth() {
        return true;
    }

    @ConfigItem(keyName="showFire", name="Show Fire rift", description="Configures whether to display the Fire rift", position=11, section="rifts")
    default public boolean showFire() {
        return true;
    }

    @ConfigItem(keyName="showLaw", name="Show Law rift", description="Configures whether to display the Law rift", position=12, section="rifts")
    default public boolean showLaw() {
        return true;
    }

    @ConfigItem(keyName="showMind", name="Show Mind rift", description="Configures whether to display the Mind rift", position=13, section="rifts")
    default public boolean showMind() {
        return true;
    }

    @ConfigItem(keyName="showNature", name="Show Nature rift", description="Configures whether to display the Nature rift", position=14, section="rifts")
    default public boolean showNature() {
        return true;
    }

    @ConfigItem(keyName="showSoul", name="Show Soul rift", description="Configures whether to display the Soul rift", position=15, section="rifts")
    default public boolean showSoul() {
        return true;
    }

    @ConfigItem(keyName="showWater", name="Show Water rift", description="Configures whether to display the Water rift", position=16, section="rifts")
    default public boolean showWater() {
        return true;
    }

    @ConfigItem(keyName="hightlightDarkMage", name="Highlight Dark Mage NPC", description="Configures whether to highlight the Dark Mage when pouches are degraded", position=18)
    default public boolean hightlightDarkMage() {
        return true;
    }

    @ConfigItem(keyName="degradingNotification", name="Notify when pouch degrades", description="Send a notification when a pouch degrades", position=19)
    default public boolean degradingNotification() {
        return true;
    }
}

