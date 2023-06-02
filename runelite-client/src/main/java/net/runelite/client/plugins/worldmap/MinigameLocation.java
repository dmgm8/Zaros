/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.worldmap;

import net.runelite.api.coords.WorldPoint;

enum MinigameLocation {
    BARBARIAN_ASSAULT("Barbarian Assault", new WorldPoint(2531, 3569, 0)),
    BURGH_DE_ROTT_RAMBLE("Burgh de Rott Ramble", new WorldPoint(3434, 3487, 0)),
    CASTLE_WARS("Castle Wars", new WorldPoint(2439, 3092, 0)),
    CASTLE_WARS_PORTAL("Castle Wars Portal", new WorldPoint(3140, 3626, 0)),
    PVP_ARENA("PvP Arena", new WorldPoint(3313, 3238, 0)),
    MAGE_ARENA("Mage Arena", new WorldPoint(3095, 3957, 0)),
    NIGHTMARE_ZONE("Nightmare Zone", new WorldPoint(2606, 3115, 0)),
    PEST_CONTROL_NOVICE("Pest Control Novice", new WorldPoint(2660, 2637, 0)),
    PEST_CONTROL_INTERMEDIATE("Pest Control Intermediate", new WorldPoint(2638, 2641, 0)),
    PEST_CONTROL_VETERAN("Pest Control Veteran", new WorldPoint(2632, 2648, 0)),
    TEMPLE_TREKKING("Temple Trekking", new WorldPoint(3479, 3240, 0)),
    TZHAAR_FIGHT_CAVE("TzHaar Fight Cave", new WorldPoint(2437, 5168, 0)),
    TZHAAR_FIGHT_PIT("TzHaar Fight Pit", new WorldPoint(2398, 5177, 0)),
    LAST_MAN_STANDING("Last Man Standing", new WorldPoint(3138, 3635, 0)),
    INFERNO("Inferno", new WorldPoint(2495, 5118, 0)),
    BRIMHAVEN_AGILITY_ARENA("Brimhaven Agility Arena", new WorldPoint(2809, 3191, 0)),
    FISHING_TRAWLER("Fishing Trawler", new WorldPoint(2667, 3163, 0)),
    GNOME_BALL("Gnome Ball", new WorldPoint(2381, 3488, 0)),
    GNOME_RESTAURANT("Gnome Restaurant", new WorldPoint(2436, 3502, 0)),
    IMPETUOUS_IMPULSES("Impetuous Impulses", new WorldPoint(2425, 4445, 0)),
    MAGE_TRAINING_ARENA("Mage Training Arena", new WorldPoint(3362, 3318, 0)),
    PYRAMID_PLUNDER("Pyramid Plunder", new WorldPoint(3288, 2787, 0)),
    RANGING_GUILD("Ranging Guild", new WorldPoint(2671, 3419, 0)),
    ROGUES_DEN("Rogues' Den", new WorldPoint(2905, 3537, 0)),
    SHADES_OF_MORTTON("Shades of Mort'ton", new WorldPoint(3505, 3315, 0)),
    SORCERESSS_GARDEN("Sorceress's Garden", new WorldPoint(3285, 3180, 0)),
    TROUBLE_BREWING("Trouble Brewing", new WorldPoint(3811, 3021, 0)),
    VOLCANIC_MINE("Volcanic Mine", new WorldPoint(3812, 3810, 0)),
    TAI_BWO_WANNAI_CLEANUP("Tai Bwo Wannai Cleanup", new WorldPoint(2795, 3066, 0)),
    BURTHORPE_GAMES_ROOM("Burthorpe Games Room", new WorldPoint(2900, 3565, 0)),
    RAT_PITS_PORT_SARIM("Rat Pits", new WorldPoint(3015, 3232, 0)),
    RAT_PITS_VARROCK("Rat Pits", new WorldPoint(3266, 3400, 0)),
    RAT_PITS_ARDOUGNE("Rat Pits", new WorldPoint(2561, 3318, 0)),
    RAT_PITS_KELDAGRIM("Rat Pits", new WorldPoint(2913, 10188, 0)),
    TEARS_OF_GUTHIX("Tears of Guthix", new WorldPoint(3257, 9517, 0)),
    CLAN_WARS("Clan Wars", new WorldPoint(3133, 3621, 0)),
    ANIMATION_ROOM("Animation Room", new WorldPoint(2853, 3537, 0)),
    DUMMY_ROOM("Dummy Room", new WorldPoint(2857, 3551, 0)),
    CATAPULT_ROOM("Catapult Room", new WorldPoint(2842, 3545, 0)),
    SHOT_PUT_ROOM("Shot Put Room", new WorldPoint(2863, 3550, 0)),
    HALLOWED_SEPULCHRE("Hallowed Sepulchre", new WorldPoint(3653, 3386, 1)),
    THE_GAUNTLET("The Gauntlet", new WorldPoint(3223, 12505, 1)),
    MAHOGANY_HOMES_ARDOUGNE("Mahogany Homes", new WorldPoint(2634, 3295, 0)),
    MAHOGANY_HOMES_FALADOR("Mahogany Homes", new WorldPoint(2989, 3363, 0)),
    MAHOGANY_HOMES_HOSIDIUS("Mahogany Homes", new WorldPoint(1780, 3623, 0)),
    MAHOGANY_HOMES_VARROCK("Mahogany Homes", new WorldPoint(3240, 3471, 0)),
    SOUL_WARS("Soul Wars", new WorldPoint(2209, 2855, 0)),
    SOUL_WARS_EDGEVILLE_PORTAL("Soul Wars", new WorldPoint(3082, 3474, 0)),
    TZHAAR_KET_RAKS_CHALLENGES("TzHaar-Ket-Rak's Challenges", new WorldPoint(2544, 5111, 0));

    private final String tooltip;
    private final WorldPoint location;

    private MinigameLocation(String tooltip, WorldPoint location) {
        this.tooltip = tooltip;
        this.location = location;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public WorldPoint getLocation() {
        return this.location;
    }
}

