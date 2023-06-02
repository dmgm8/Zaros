/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.slayer;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup(value="slayer")
public interface SlayerConfig
extends Config {
    public static final String GROUP_NAME = "slayer";
    public static final String TASK_NAME_KEY = "taskName";
    public static final String AMOUNT_KEY = "amount";
    public static final String INIT_AMOUNT_KEY = "initialAmount";
    public static final String TASK_LOC_KEY = "taskLocation";
    public static final String STREAK_KEY = "streak";
    public static final String POINTS_KEY = "points";

    @ConfigItem(position=1, keyName="infobox", name="Task InfoBox", description="Display task information in an InfoBox")
    default public boolean showInfobox() {
        return true;
    }

    @ConfigItem(position=2, keyName="itemoverlay", name="Count on Items", description="Display task count remaining on slayer items")
    default public boolean showItemOverlay() {
        return true;
    }

    @ConfigItem(position=3, keyName="superiornotification", name="Superior foe notification", description="Toggles notifications on superior foe encounters")
    default public boolean showSuperiorNotification() {
        return true;
    }

    @ConfigItem(position=4, keyName="statTimeout", name="InfoBox Expiry", description="Set the time until the InfoBox expires")
    @Units(value=" mins")
    default public int statTimeout() {
        return 5;
    }

    @ConfigItem(position=5, keyName="highlightHull", name="Highlight hull", description="Configures whether the NPC hull should be highlighted")
    default public boolean highlightHull() {
        return false;
    }

    @ConfigItem(position=6, keyName="highlightTile", name="Highlight tile", description="Configures whether the NPC tile should be highlighted")
    default public boolean highlightTile() {
        return false;
    }

    @ConfigItem(position=7, keyName="highlightOutline", name="Highlight outline", description="Configures whether or not the NPC outline should be highlighted")
    default public boolean highlightOutline() {
        return false;
    }

    @Alpha
    @ConfigItem(position=8, keyName="targetColor", name="Target color", description="Color of the highlighted targets")
    default public Color getTargetColor() {
        return Color.RED;
    }

    @ConfigItem(position=9, keyName="weaknessPrompt", name="Show Monster Weakness", description="Show an overlay on a monster when it is weak enough to finish off (Only Lizards, Gargoyles & Rockslugs)")
    default public boolean weaknessPrompt() {
        return true;
    }

    @ConfigItem(position=10, keyName="taskCommand", name="Task Command", description="Configures whether the slayer task command is enabled<br> !task")
    default public boolean taskCommand() {
        return true;
    }
}

