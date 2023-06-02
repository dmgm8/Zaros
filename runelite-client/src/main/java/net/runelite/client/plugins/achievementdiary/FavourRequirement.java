/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.achievementdiary;

import net.runelite.api.Client;
import net.runelite.client.plugins.achievementdiary.Requirement;

public class FavourRequirement
implements Requirement {
    private final Favour house;
    private final int percent;

    public String toString() {
        return this.percent + "% " + this.house.getName() + " favour";
    }

    @Override
    public boolean satisfiesRequirement(Client client) {
        int realFavour = client.getVarbitValue(this.house.getVarbit());
        return realFavour / 10 >= this.percent;
    }

    public FavourRequirement(Favour house, int percent) {
        this.house = house;
        this.percent = percent;
    }

    public Favour getHouse() {
        return this.house;
    }

    public int getPercent() {
        return this.percent;
    }

    public static enum Favour {
        ARCEUUS("Arceuus", 4896),
        HOSIDIUS("Hosidius", 4895),
        LOVAKENGJ("Lovakengj", 4898),
        PISCARILIUS("Piscarilius", 4899),
        SHAYZIEN("Shayzien", 4894);

        private final String name;
        private final int varbit;

        private Favour(String name, int varbit) {
            this.name = name;
            this.varbit = varbit;
        }

        public String getName() {
            return this.name;
        }

        public int getVarbit() {
            return this.varbit;
        }
    }
}

