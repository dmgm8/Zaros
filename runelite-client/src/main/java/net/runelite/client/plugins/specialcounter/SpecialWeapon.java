/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.specialcounter;

import java.util.function.Function;
import net.runelite.client.plugins.specialcounter.SpecialCounterConfig;

public enum SpecialWeapon {
    DRAGON_WARHAMMER("Dragon Warhammer", new int[]{13576}, false, SpecialCounterConfig::dragonWarhammerThreshold),
    ARCLIGHT("Arclight", new int[]{19675}, false, SpecialCounterConfig::arclightThreshold),
    DARKLIGHT("Darklight", new int[]{6746}, false, SpecialCounterConfig::darklightThreshold),
    BANDOS_GODSWORD("Bandos Godsword", new int[]{11804, 20370}, true, SpecialCounterConfig::bandosGodswordThreshold),
    BARRELCHEST_ANCHOR("Barrelchest Anchor", new int[]{10887}, true, c -> 0),
    BONE_DAGGER("Bone Dagger", new int[]{8872, 8874, 8876, 8878}, true, c -> 0),
    DORGESHUUN_CROSSBOW("Dorgeshuun Crossbow", new int[]{8880}, true, c -> 0),
    BULWARK("Dinh's Bulwark", new int[]{21015}, false, SpecialCounterConfig::bulwarkThreshold);

    private final String name;
    private final int[] itemID;
    private final boolean damage;
    private final Function<SpecialCounterConfig, Integer> threshold;

    private SpecialWeapon(String name, int[] itemID, boolean damage, Function<SpecialCounterConfig, Integer> threshold) {
        this.name = name;
        this.itemID = itemID;
        this.damage = damage;
        this.threshold = threshold;
    }

    public String getName() {
        return this.name;
    }

    public int[] getItemID() {
        return this.itemID;
    }

    public boolean isDamage() {
        return this.damage;
    }

    public Function<SpecialCounterConfig, Integer> getThreshold() {
        return this.threshold;
    }
}

