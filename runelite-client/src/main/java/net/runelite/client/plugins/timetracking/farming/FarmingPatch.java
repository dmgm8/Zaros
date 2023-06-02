/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.timetracking.farming;

import net.runelite.client.plugins.timetracking.farming.FarmingRegion;
import net.runelite.client.plugins.timetracking.farming.PatchImplementation;

class FarmingPatch {
    private FarmingRegion region;
    private final String name;
    private final int varbit;
    private final PatchImplementation implementation;
    private int farmer = -1;

    FarmingPatch(String name, int varbit, PatchImplementation implementation, int farmer) {
        this(name, varbit, implementation);
        this.farmer = farmer;
    }

    String configKey() {
        return this.region.getRegionID() + "." + this.varbit;
    }

    String notifyConfigKey() {
        return "notify." + this.region.getRegionID() + "." + this.varbit;
    }

    FarmingPatch(String name, int varbit, PatchImplementation implementation) {
        this.name = name;
        this.varbit = varbit;
        this.implementation = implementation;
    }

    public FarmingRegion getRegion() {
        return this.region;
    }

    public String getName() {
        return this.name;
    }

    public PatchImplementation getImplementation() {
        return this.implementation;
    }

    public int getFarmer() {
        return this.farmer;
    }

    public String toString() {
        return "FarmingPatch(region=" + this.getRegion() + ", name=" + this.getName() + ", implementation=" + (Object)((Object)this.getImplementation()) + ")";
    }

    void setRegion(FarmingRegion region) {
        this.region = region;
    }

    public int getVarbit() {
        return this.varbit;
    }
}

