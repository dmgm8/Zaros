/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.idlenotifier;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(value="idlenotifier")
public interface IdleNotifierConfig
extends Config {
    @ConfigItem(keyName="animationidle", name="Idle Animation Notifications", description="Configures if idle animation notifications are enabled", position=1)
    default public boolean animationIdle() {
        return true;
    }

    @ConfigItem(keyName="interactionidle", name="Idle Interaction Notifications", description="Configures if idle interaction notifications are enabled e.g. combat, fishing", position=2)
    default public boolean interactionIdle() {
        return true;
    }

    @ConfigItem(keyName="movementidle", name="Idle Movement Notifications", description="Configures if idle movement notifications are enabled e.g. running, walking", position=3)
    default public boolean movementIdle() {
        return false;
    }

    @ConfigItem(keyName="logoutidle", name="Idle Logout Notifications", description="Configures if the idle logout notifications are enabled", position=4, hidden=true)
    default public boolean logoutIdle() {
        return false;
    }

    @ConfigItem(keyName="timeout", name="Idle Notification Delay", description="The notification delay after the player is idle", position=5)
    @Units(value="ms")
    default public int getIdleNotificationDelay() {
        return 5000;
    }

    @ConfigItem(keyName="hitpoints", name="Hitpoints Threshold", description="The amount of hitpoints to send a notification at. A value of 0 will disable notification.", position=6)
    default public int getHitpointsThreshold() {
        return 0;
    }

    @ConfigItem(keyName="prayer", name="Prayer Threshold", description="The amount of prayer points to send a notification at. A value of 0 will disable notification.", position=7)
    default public int getPrayerThreshold() {
        return 0;
    }

    @ConfigItem(keyName="lowEnergy", name="Low Energy Threshold", description="The amount of energy points remaining to send a notification at. A value of 100 will disable notification.", position=8)
    @Units(value="%")
    @Range(max=100)
    default public int getLowEnergyThreshold() {
        return 100;
    }

    @ConfigItem(keyName="highEnergy", name="High Energy Threshold", description="The amount of energy points reached to send a notification. A value of 0 will disable notification.", position=9)
    @Units(value="%")
    @Range(max=100)
    default public int getHighEnergyThreshold() {
        return 0;
    }

    @ConfigItem(keyName="oxygen", name="Oxygen Threshold", position=10, description="The amount of remaining oxygen to send a notification at. A value of 0 will disable notification.")
    @Units(value="%")
    default public int getOxygenThreshold() {
        return 0;
    }

    @ConfigItem(keyName="spec", name="Spec Threshold", position=11, description="The amount of special attack energy reached to send a notification at. A value of 0 will disable notification.")
    @Units(value="%")
    default public int getSpecEnergyThreshold() {
        return 0;
    }
}

