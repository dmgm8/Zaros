/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.VarPlayer
 */
package net.runelite.client.plugins.timetracking.hunter;

import net.runelite.api.VarPlayer;

enum BirdHouseSpace {
    MEADOW_NORTH("Mushroom Meadow (North)", VarPlayer.BIRD_HOUSE_MEADOW_NORTH),
    MEADOW_SOUTH("Mushroom Meadow (South)", VarPlayer.BIRD_HOUSE_MEADOW_SOUTH),
    VALLEY_NORTH("Verdant Valley (Northeast)", VarPlayer.BIRD_HOUSE_VALLEY_NORTH),
    VALLEY_SOUTH("Verdant Valley (Southwest)", VarPlayer.BIRD_HOUSE_VALLEY_SOUTH);

    private final String name;
    private final VarPlayer varp;

    private BirdHouseSpace(String name, VarPlayer varp) {
        this.name = name;
        this.varp = varp;
    }

    public String getName() {
        return this.name;
    }

    public VarPlayer getVarp() {
        return this.varp;
    }
}

