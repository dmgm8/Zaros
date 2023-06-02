/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.grandexchange;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.grandexchange.GrandExchangeSearchMode;

@ConfigGroup(value="grandexchange")
public interface GrandExchangeConfig
extends Config {
    public static final String CONFIG_GROUP = "grandexchange";

    @ConfigItem(position=1, keyName="quickLookup", name="Hotkey lookup (Alt + Left click)", description="Configures whether to enable the hotkey lookup for GE searches")
    default public boolean quickLookup() {
        return true;
    }

    @ConfigItem(position=2, keyName="enableNotifications", name="Enable Notifications", description="Configures whether to enable notifications when an offer updates")
    default public boolean enableNotifications() {
        return true;
    }

    @ConfigItem(position=3, keyName="showActivelyTradedPrice", name="Enable actively traded prices", description="Shows the actively traded price on the GE buy interface, sourced from the RuneScape wiki", hidden=true)
    default public boolean showActivelyTradedPrice() {
        return true;
    }

    @ConfigItem(position=4, keyName="enableGeLimits", name="Enable GE Limits on GE", description="Shows the GE Limits on the GE")
    default public boolean enableGELimits() {
        return true;
    }

    @ConfigItem(position=5, keyName="enableGELimitReset", name="Enable GE Limit Reset Timer", description="Shows when GE Trade limits reset (H:MM)")
    default public boolean enableGELimitReset() {
        return true;
    }

    @ConfigItem(position=6, keyName="showTotal", name="Show GE total", description="Display the total value of all trades at the top of the GE interface")
    default public boolean showTotal() {
        return true;
    }

    @ConfigItem(position=7, keyName="showExact", name="Show exact total value", description="When enabled along with the \u2018Show GE total\u2019 option, the unabbreviated value will be displayed")
    default public boolean showExact() {
        return false;
    }

    @ConfigItem(position=8, keyName="highlightSearchMatch", name="Highlight Search Match", description="Highlights the search match with an underline")
    default public boolean highlightSearchMatch() {
        return true;
    }

    @ConfigItem(position=9, keyName="geSearchMode", name="Search Mode", description="The search mode to use for the GE<br>Default - Matches exact text only<br>Fuzzy Only - Matches inexact text such as 'sara sword'<br>Fuzzy Fallback - Uses default search, falling back to fuzzy search if no results were found")
    default public GrandExchangeSearchMode geSearchMode() {
        return GrandExchangeSearchMode.DEFAULT;
    }
}

