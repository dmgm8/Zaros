/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.worldmap;

public enum TeleportType {
    NORMAL_MAGIC(""),
    ANCIENT_MAGICKS("Ancient - "),
    LUNAR_MAGIC("Lunar - "),
    ARCEUUS_MAGIC("Arceuus - "),
    JEWELLERY("Jewellery - "),
    SCROLL(""),
    OTHER("");

    private String prefix;

    private TeleportType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }
}

