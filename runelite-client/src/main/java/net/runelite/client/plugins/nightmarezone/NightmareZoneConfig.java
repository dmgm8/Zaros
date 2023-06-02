/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.nightmarezone;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(value="nightmareZone")
public interface NightmareZoneConfig
extends Config {
    @ConfigItem(keyName="moveoverlay", name="Override NMZ overlay", description="Overrides the overlay so it doesn't conflict with other RuneLite plugins", position=1)
    default public boolean moveOverlay() {
        return true;
    }

    @ConfigItem(keyName="powersurgenotification", name="Power surge notification", description="Toggles notifications when a power surge power-up appears", position=2)
    default public boolean powerSurgeNotification() {
        return false;
    }

    @ConfigItem(keyName="recurrentdamagenotification", name="Recurrent damage notification", description="Toggles notifications when a recurrent damage power-up appears", position=3)
    default public boolean recurrentDamageNotification() {
        return false;
    }

    @ConfigItem(keyName="zappernotification", name="Zapper notification", description="Toggles notifications when a zapper power-up appears", position=4)
    default public boolean zapperNotification() {
        return false;
    }

    @ConfigItem(keyName="ultimateforcenotification", name="Ultimate Force notification", description="Toggles notifications when an ultimate force power-up appears", position=5)
    default public boolean ultimateForceNotification() {
        return false;
    }

    @ConfigItem(keyName="overloadnotification", name="Overload notification", description="Toggles notifications when your overload runs out", position=6)
    default public boolean overloadNotification() {
        return true;
    }

    @Range(min=0, max=300)
    @ConfigItem(keyName="overloadearlywarningseconds", name="Overload early warning", description="You will be notified this many seconds before your overload potion expires", position=7)
    @Units(value="s")
    default public int overloadEarlyWarningSeconds() {
        return 10;
    }

    @ConfigItem(keyName="absorptionnotification", name="Absorption notification", description="Toggles notifications when your absorption points gets below your threshold", position=8)
    default public boolean absorptionNotification() {
        return true;
    }

    @ConfigItem(keyName="absorptionthreshold", name="Absorption Threshold", description="The amount of absorption points to send a notification at", position=9)
    default public int absorptionThreshold() {
        return 50;
    }

    @ConfigItem(keyName="absorptioncoloroverthreshold", name="Color above threshold", description="Configures the color for the absorption widget when above the threshold", position=10)
    default public Color absorptionColorAboveThreshold() {
        return Color.YELLOW;
    }

    @ConfigItem(keyName="absorptioncolorbelowthreshold", name="Color below threshold", description="Configures the color for the absorption widget when below the threshold", position=11)
    default public Color absorptionColorBelowThreshold() {
        return Color.RED;
    }
}

