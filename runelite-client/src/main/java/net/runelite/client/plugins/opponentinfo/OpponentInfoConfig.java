/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.opponentinfo;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.opponentinfo.HitpointsDisplayStyle;

@ConfigGroup(value="opponentinfo")
public interface OpponentInfoConfig
extends Config {
    @ConfigItem(keyName="lookupOnInteraction", name="Lookup players on interaction", description="Display a combat stat comparison panel on player interaction. (follow, trade, challenge, attack, etc.)", position=0)
    default public boolean lookupOnInteraction() {
        return false;
    }

    @ConfigItem(keyName="hitpointsDisplayStyle", name="Display style", description="Show opponent's hitpoints as a value (if known), percentage, or both", position=1)
    default public HitpointsDisplayStyle hitpointsDisplayStyle() {
        return HitpointsDisplayStyle.HITPOINTS;
    }

    @ConfigItem(keyName="showOpponentsInMenu", name="Show opponents in menu", description="Marks opponents names in the menu which you are attacking or are attacking you (NPC only)", position=3)
    default public boolean showOpponentsInMenu() {
        return false;
    }
}

