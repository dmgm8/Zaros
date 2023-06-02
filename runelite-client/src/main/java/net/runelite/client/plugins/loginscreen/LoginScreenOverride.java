/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.loginscreen;

public enum LoginScreenOverride {
    OFF,
    NORMAL("normal.jpg"),
    OLD("old.jpg"),
    CHRISTMAS("christmas.jpg"),
    CHAMBERS_OF_XERIC("cox.jpg"),
    DARKMEYER("darkmeyer.jpg"),
    DRAGON_SLAYER_2("ds2.jpg"),
    FOSSIL_ISLAND("fossil_island.jpg"),
    HALLOWEEN("halloween.jpg"),
    HALLOWEEN_2019("halloween_2019.jpg"),
    INFERNO("inferno.jpg"),
    KEBOS("kebos.jpg"),
    MONKEY_MADNESS_2("mm2.jpg"),
    PRIFDDINAS("prifddinas.jpg"),
    THEATRE_OF_BLOOD("tob.jpg"),
    A_KINGDOM_DIVIDED("akd.jpg"),
    NEX("nex.jpg"),
    TOMBS_OF_AMASCUT("toa.jpg"),
    CUSTOM,
    RANDOM;

    private final String fileName;

    private LoginScreenOverride() {
        this.fileName = null;
    }

    private LoginScreenOverride(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}

