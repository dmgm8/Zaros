/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.screenshot;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.util.ImageUploadStyle;

@ConfigGroup(value="screenshot")
public interface ScreenshotConfig
extends Config {
    @ConfigSection(name="What to Screenshot", description="All the options that select what to screenshot", position=99)
    public static final String whatSection = "what";

    @ConfigItem(keyName="includeFrame", name="Include Client Frame", description="Configures whether or not the client frame is included in screenshots", position=0)
    default public boolean includeFrame() {
        return true;
    }

    @ConfigItem(keyName="displayDate", name="Display Date", description="Configures whether or not the report button shows the date the screenshot was taken", position=1)
    default public boolean displayDate() {
        return true;
    }

    @ConfigItem(keyName="notifyWhenTaken", name="Notify When Taken", description="Configures whether or not you are notified when a screenshot has been taken", position=2)
    default public boolean notifyWhenTaken() {
        return true;
    }

    @ConfigItem(keyName="uploadScreenshot", name="Upload", description="Configures whether or not screenshots are uploaded to Imgur, or placed on your clipboard", position=3)
    default public ImageUploadStyle uploadScreenshot() {
        return ImageUploadStyle.NEITHER;
    }

    @ConfigItem(keyName="hotkey", name="Screenshot hotkey", description="When you press this key a screenshot will be taken", position=4)
    default public Keybind hotkey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(keyName="rewards", name="Screenshot Rewards", description="Configures whether screenshots are taken of clues, barrows, and quest completion", position=3, section="what")
    default public boolean screenshotRewards() {
        return true;
    }

    @ConfigItem(keyName="levels", name="Screenshot Levels", description="Configures whether screenshots are taken of level ups", position=4, section="what")
    default public boolean screenshotLevels() {
        return true;
    }

    @ConfigItem(keyName="kingdom", name="Screenshot Kingdom Reward", description="Configures whether screenshots are taken of Kingdom Reward", position=5, section="what", hidden=true)
    default public boolean screenshotKingdom() {
        return true;
    }

    @ConfigItem(keyName="pets", name="Screenshot Pet", description="Configures whether screenshots are taken of receiving pets", position=6, section="what")
    default public boolean screenshotPet() {
        return true;
    }

    @ConfigItem(keyName="kills", name="Screenshot PvP Kills", description="Configures whether or not screenshots are automatically taken of PvP kills", position=8, section="what")
    default public boolean screenshotKills() {
        return false;
    }

    @ConfigItem(keyName="boss", name="Screenshot Boss Kills", description="Configures whether or not screenshots are automatically taken of boss kills", position=9, section="what")
    default public boolean screenshotBossKills() {
        return false;
    }

    @ConfigItem(keyName="playerDeath", name="Screenshot Deaths", description="Configures whether or not screenshots are automatically taken when you die.", position=10, section="what")
    default public boolean screenshotPlayerDeath() {
        return false;
    }

    @ConfigItem(keyName="friendDeath", name="Screenshot Friend Deaths", description="Configures whether or not screenshots are automatically taken when friends or friends chat members die.", position=11, section="what")
    default public boolean screenshotFriendDeath() {
        return false;
    }

    @ConfigItem(keyName="clanDeath", name="Screenshot Clan Deaths", description="Configures whether or not screenshots are automatically taken when clan members die.", position=12, section="what")
    default public boolean screenshotClanDeath() {
        return false;
    }

    @ConfigItem(keyName="duels", name="Screenshot Duels", description="Configures whether or not screenshots are automatically taken of the duel end screen.", position=13, section="what")
    default public boolean screenshotDuels() {
        return false;
    }

    @ConfigItem(keyName="valuableDrop", name="Screenshot Valuable drops", description="Configures whether or not screenshots are automatically taken when you receive a valuable drop.", position=14, section="what")
    default public boolean screenshotValuableDrop() {
        return false;
    }

    @ConfigItem(keyName="valuableDropThreshold", name="Valuable Threshold", description="The minimum value to save screenshots of valuable drops.", position=15, section="what")
    default public int valuableDropThreshold() {
        return 0;
    }

    @ConfigItem(keyName="untradeableDrop", name="Screenshot Untradeable drops", description="Configures whether or not screenshots are automatically taken when you receive an untradeable drop.", position=16, section="what")
    default public boolean screenshotUntradeableDrop() {
        return false;
    }

    @ConfigItem(keyName="ccKick", name="Screenshot Kicks from FC", description="Take a screenshot when you kick a user from a friends chat.", position=17, section="what")
    default public boolean screenshotKick() {
        return false;
    }

    @ConfigItem(keyName="baHighGamble", name="Screenshot BA high gambles", description="Take a screenshot of your reward from a high gamble at Barbarian Assault.", position=18, section="what", hidden=true)
    default public boolean screenshotHighGamble() {
        return false;
    }

    @ConfigItem(keyName="collectionLogEntries", name="Screenshot collection log entries", description="Take a screenshot when completing an entry in the collection log", position=19, section="what")
    default public boolean screenshotCollectionLogEntries() {
        return true;
    }

    @ConfigItem(keyName="combatAchievements", name="Screenshot combat achievements", description="Take a screenshot when completing a combat achievement task", position=20, section="what")
    default public boolean screenshotCombatAchievements() {
        return true;
    }

    @ConfigItem(keyName="webhook", name="Webhook URL", description="The Discord Webhook URL to send messages to", position=21)
    default public String webhook() {
        return "";
    }
}

