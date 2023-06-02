/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.profiles;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="profiles")
public interface ProfilesConfig
extends Config {
    @ConfigItem(keyName="profilesData", name="", description="", hidden=true)
    default public String profilesData() {
        return "";
    }

    @ConfigItem(keyName="profilesData", name="", description="")
    public void profilesData(String var1);

    @ConfigItem(keyName="salt", name="", description="", hidden=true)
    default public String salt() {
        return "";
    }

    @ConfigItem(keyName="salt", name="", description="")
    public void salt(String var1);

    @ConfigItem(keyName="rememberPassword", name="Remember Password", description="Remembers passwords for accounts", position=0)
    default public boolean rememberPassword() {
        return true;
    }

    @ConfigItem(keyName="displayEmailAddress", name="Display email field", description="Displays the email address field", position=1)
    default public boolean displayEmailAddress() {
        return false;
    }

    @ConfigItem(keyName="streamerMode", name="Hide email addresses", description="Hides your account emails", position=2, hidden=true)
    default public boolean streamerMode() {
        return false;
    }

    @ConfigItem(keyName="switchPanel", name="Auto-open Panel", description="Automatically switch to the account switcher panel on the login screen", position=3)
    default public boolean switchPanel() {
        return false;
    }
}

