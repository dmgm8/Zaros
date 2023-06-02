/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.randomevents;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(value="randomevents")
public interface RandomEventConfig
extends Config {
    @ConfigSection(name="Notification Settings", description="Choose which random events will trigger notifications when spawned", position=99)
    public static final String notificationSection = "section";

    @ConfigItem(keyName="removeMenuOptions", name="Remove others' menu options", description="Remove menu options from random events for other players.", position=-3)
    default public boolean removeMenuOptions() {
        return true;
    }

    @ConfigItem(keyName="notifyAll", name="Notify for all events", description="", position=-2, section="section")
    default public boolean notifyAllEvents() {
        return false;
    }

    @ConfigItem(keyName="notifyDunce", name="Notify on Surprise Exam", description="", section="section")
    default public boolean notifyDunce() {
        return false;
    }

    @ConfigItem(keyName="notifyGenie", name="Notify on Genie", description="", section="section")
    default public boolean notifyGenie() {
        return false;
    }

    @ConfigItem(keyName="notifyDemon", name="Notify on Drill Demon", description="", section="section")
    default public boolean notifyDemon() {
        return false;
    }

    @ConfigItem(keyName="notifyForester", name="Notify on Freaky Forester", description="", section="section")
    default public boolean notifyForester() {
        return false;
    }

    @ConfigItem(keyName="notifyFrog", name="Notify on Kiss the Frog", description="", section="section")
    default public boolean notifyFrog() {
        return false;
    }

    @ConfigItem(keyName="notifyGravedigger", name="Notify on Gravedigger", description="", section="section")
    default public boolean notifyGravedigger() {
        return false;
    }

    @ConfigItem(keyName="notifyMoM", name="Notify on Mysterious Old Man", description="", section="section")
    default public boolean notifyMoM() {
        return false;
    }

    @ConfigItem(keyName="notifyBob", name="Notify on Evil Bob", description="", section="section")
    default public boolean notifyBob() {
        return false;
    }

    @ConfigItem(keyName="notifyQuiz", name="Notify on Quiz Master", description="", section="section")
    default public boolean notifyQuiz() {
        return false;
    }

    @ConfigItem(keyName="notifyJekyll", name="Notify on Jekyll & Hyde", description="", section="section")
    default public boolean notifyJekyll() {
        return false;
    }

    @ConfigItem(keyName="notifyBeekeeper", name="Notify on Beekeeper", description="", section="section")
    default public boolean notifyBeekeeper() {
        return false;
    }

    @ConfigItem(keyName="notifySandwich", name="Notify on Sandwich Lady", description="", section="section")
    default public boolean notifySandwich() {
        return false;
    }
}

