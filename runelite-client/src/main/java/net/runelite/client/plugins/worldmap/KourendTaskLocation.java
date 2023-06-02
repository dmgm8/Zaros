/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.worldmap;

import net.runelite.api.coords.WorldPoint;

enum KourendTaskLocation {
    ARCEUUS_LIBRARY_SOUTH("Arceuus Task (0%) - Library Assistance", new WorldPoint(1622, 3801, 0)),
    ARCEUUS_LIBRARY_NORTH("Arceuus Task (0%) - Library Assistance", new WorldPoint(1638, 3817, 0)),
    ARCEUUS_DENSE_ESSENCE("Arceuus Task (25%) - Dense Essence Mining", new WorldPoint(1757, 3852, 0)),
    ARCEUUS_REANIMATE_CREATURES("Arceuus Task (60%) - Creature Reanimation", new WorldPoint(1714, 3883, 0)),
    ARCEUUS_BLOOD_ALTAR("Arceuus Unlock (100%) - Blood Rune Crafting", new WorldPoint(1716, 3829, 0)),
    ARCEUUS_SOUL_ALTAR("Arceuus Unlock (100%) - Soul Rune Crafting", new WorldPoint(1814, 3854, 0)),
    HOSIDIUS_PLOUGH_NORTH("Hosidius Task (0%) - Ploughs", new WorldPoint(1770, 3550, 0)),
    HOSIDIUS_PLOUGH_SOUTH("Hosidius Task (0%) - Ploughs", new WorldPoint(1769, 3528, 0)),
    HOSIDUIS_FERTILISER("Hosidius Task (5%) - Making Fertilizer", new WorldPoint(1701, 3527, 0)),
    HOSIDIUS_SPIRIT_TREE("Hosidius Unlock (35%) - Spirit Tree Patch", new WorldPoint(1695, 3545, 0)),
    HOSIDIUS_MESS("Hosidius Task (45%) - Mess", new WorldPoint(1639, 3629, 0)),
    HOSIDIUS_FARMING_PATCH("Hosidius Unlock (50%) - Disease Protection", new WorldPoint(1740, 3552, 0)),
    HOSIDIUS_VINERY("Hosidius Task (65%) - Grape Planting", new WorldPoint(1804, 3564, 0)),
    HOSIDIUS_TITHE_FARM("Hosidius Unlock (100%) - Tithe Farm", new WorldPoint(1802, 3499, 0)),
    HOSIDIUS_KITCHEN("Hosidius Unlock (100%) - Kitchen", new WorldPoint(1678, 3618, 0)),
    LOVAKENGJ_DYNAMITE("Lovakengj Task (0%) - Making Juniper Charcoal", new WorldPoint(1718, 3472, 0)),
    LOVAKENGJ_SULFUR("Lovakengj Task (0%) - Mining Volcanic Sulphur", new WorldPoint(1437, 3861, 0)),
    LOVAKENGJ_ARMOURER_T1("Lovakengj Task (30%) - Armourer (Tier 1)", new WorldPoint(1453, 3750, 0)),
    LOVAKENGJ_ARMOURER_T2("Lovakengj Task (40%) - Armourer (Tier 2)", new WorldPoint(1436, 3784, 0)),
    LOVAKENGJ_ARMOURER_T3("Lovakengj Task (50%) - Armourer (Tier 3)", new WorldPoint(1430, 3797, 0)),
    LOVAKENGJ_ARMOURER_T4("Lovakengj Task (60%) - Armourer (Tier 4)", new WorldPoint(1460, 3763, 0)),
    LOVAKENGJ_ARMOURER_T5("Lovakengj Task (70%) - Armourer (Tier 5)", new WorldPoint(1524, 3755, 0)),
    LOVAKENGJ_BLAST_MINE("Lovakengj Unlock (100%) - Blast Mine", new WorldPoint(1488, 3865, 0)),
    PISCARILIUS_CRANE_REPAIR("Piscarilius Task (0%) - Fishing Crane Repair", new WorldPoint(1820, 3742, 0)),
    PISCARILIUS_FRESH_FISH("Piscarilius Task (15%) - Fish Delivery", new WorldPoint(1831, 3716, 0)),
    PISCARILIUS_SANDWORMS("Piscarilius Task (30%) - Hunting Sandworms", new WorldPoint(1841, 3789, 0)),
    PISCARILIUS_STEALING_ARTEFACTS("Piscarilius Task (75%) - Stealing Artefacts", new WorldPoint(1849, 3753, 0)),
    SHAYZIEN_HEALING_SOLDIERS("Shayzien Task (0%) - Healing Wounded Soldiers", new WorldPoint(1528, 3627, 0)),
    SHAYZIEN_LIZARDMEN_EAST("Shayzien Task (5%) - Killing Lizardmen", new WorldPoint(1570, 3668, 0)),
    SHAYZIEN_LIZARDMEN_WEST("Shayzien Task (5%) - Killing Lizardmen", new WorldPoint(1471, 3686, 0)),
    SHAYZIEN_ORGANIZED_CRIME("Shayzien Task (40%) - Organized Crime", new WorldPoint(1504, 3635, 0)),
    SHAYZIEN_COMBAT_RING("Shayzien Task (60%) - Combat Ring", new WorldPoint(1541, 3628, 0)),
    SHAYZIEN_LIZARDMAN_SHAMANS("Shayzien Unlock (100%) - Lizardman Shamans", new WorldPoint(1455, 3693, 0));

    private final String tooltip;
    private final WorldPoint location;

    private KourendTaskLocation(String tooltip, WorldPoint location) {
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

