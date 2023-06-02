/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.fairyring;

public enum FairyRings {
    AIQ("Mudskipper Point"),
    AIR("(Island) South-east of Ardougne"),
    AJQ("Cave south of Dorgesh-Kaan"),
    AJR("Slayer cave"),
    AJS("Penguins near Miscellania"),
    AKP("Necropolis"),
    AKQ("Piscatoris Hunter area"),
    AKS("Feldip Hunter area"),
    ALP("(Island) Lighthouse"),
    ALQ("Haunted Woods east of Canifis"),
    ALR("Abyssal Area"),
    ALS("McGrubor's Wood"),
    BIP("(Island) South-west of Mort Myre"),
    BIQ("Kalphite Hive"),
    BIS("Ardougne Zoo - Unicorns"),
    BJP("(Island) Isle of Souls"),
    BJR("Realm of the Fisher King"),
    BJS("(Island) Near Zul-Andra", "zulrah"),
    BKP("South of Castle Wars"),
    BKQ("Enchanted Valley"),
    BKR("Mort Myre Swamp, south of Canifis"),
    BKS("Zanaris"),
    BLP("TzHaar area"),
    BLQ("Yu'biusk"),
    BLR("Legends' Guild"),
    CIP("(Island) Miscellania"),
    CIQ("North-west of Yanille"),
    CIS("North of the Arceuus Library"),
    CIR("North-east of the Farming Guild", "mount karuulm konar"),
    CJR("Sinclair Mansion", "falo bard"),
    CKP("Cosmic entity's plane"),
    CKR("South of Tai Bwo Wannai Village"),
    CKS("Canifis"),
    CLP("(Island) South of Draynor Village"),
    CLR("(Island) Ape Atoll"),
    CLS("(Island) Hazelmere's home"),
    DIP("(Sire Boss) Abyssal Nexus"),
    DIR("Gorak's Plane"),
    DIQ("Player-owned house", "poh home"),
    DIS("Wizards' Tower"),
    DJP("Tower of Life"),
    DJR("Chasm of Fire"),
    DKP("South of Musa Point"),
    DKR("Edgeville, Grand Exchange"),
    DKS("Polar Hunter area"),
    DLQ("North of Nardah"),
    DLR("(Island) Poison Waste south of Isafdar"),
    DLS("Myreque hideout under The Hollows");

    private final String destination;
    private final String tags;

    private FairyRings(String destination) {
        this(destination, "");
    }

    private FairyRings(String destination, String tags) {
        this.destination = destination;
        this.tags = tags.toLowerCase() + " " + destination.toLowerCase();
    }

    public String getDestination() {
        return this.destination;
    }

    public String getTags() {
        return this.tags;
    }
}

