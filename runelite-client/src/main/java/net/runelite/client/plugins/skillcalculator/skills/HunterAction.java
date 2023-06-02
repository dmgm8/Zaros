/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.skillcalculator.skills.NamedSkillAction;

public enum HunterAction implements NamedSkillAction
{
    CRIMSON_SWIFT("Crimson Swift", 1, 34.0f, 9965),
    POLAR_KEBBIT("Polar Kebbit", 1, 30.0f, 9953),
    COMMON_KEBBIT("Common Kebbit", 3, 36.0f, 9954),
    GOLDEN_WARBLER("Golden Warbler", 5, 47.0f, 9968),
    REGULAR_BIRD_HOUSE("Regular Bird House", 5, 280.0f, 21512),
    FELDIP_WEASEL("Feldip Weasel", 7, 48.0f, 9955),
    COPPER_LONGTAIL("Copper Longtail", 9, 61.0f, 9966),
    CERULEAN_TWITCH("Cerulean Twitch", 11, 64.5f, 9967),
    DESERT_DEVIL("Desert Devil", 13, 66.0f, 9956),
    OAK_BIRD_HOUSE("Oak Bird House", 14, 420.0f, 21515),
    RUBY_HARVEST("Ruby Harvest", 15, 24.0f, 9970),
    BABY_IMPLING("Baby Impling", 17, 18.0f, 11238),
    TROPICAL_WAGTAIL("Tropical Wagtail", 19, 95.0f, 9969),
    YOUNG_IMPLING("Young Impling", 22, 20.0f, 11240),
    WILD_KEBBIT("Wild Kebbit", 23, 128.0f, 9953),
    WILLOW_BIRD_HOUSE("Willow Bird House", 24, 560.0f, 21518),
    SAPPHIRE_GLACIALIS("Sapphire Glacialis", 25, 34.0f, 9971),
    FERRET("Ferret", 27, 115.0f, 10092),
    WHITE_RABBIT("White Rabbit", 27, 144.0f, 9975),
    GOURMET_IMPLING("Gourmet Impling", 28, 22.0f, 11242),
    SWAMP_LIZARD("Swamp Lizard", 29, 152.0f, 10149),
    SPINED_LARUPIA("Spined Larupia", 31, 180.0f, 10045),
    BARB_TAILED_KEBBIT("Barb-tailed Kebbit", 33, 168.0f, 9958),
    TEAK_BIRD_HOUSE("Teak Bird House", 34, 700.0f, 21521),
    SNOWY_KNIGHT("Snowy Knight", 35, 44.0f, 9972),
    EARTH_IMPLING("Earth Impling", 36, 25.0f, 11244),
    PRICKLY_KEBBIT("Prickly Kebbit", 37, 204.0f, 9957),
    HORNED_GRAAHK("Horned Graahk", 41, 240.0f, 10051),
    ESSENCE_IMPLING("Essence Impling", 42, 27.0f, 11246),
    SPOTTED_KEBBIT("Spotted Kebbit", 43, 104.0f, 9960),
    MAPLE_BIRD_HOUSE("Maple Bird House", 44, 820.0f, 22192),
    BLACK_WARLOCK("Black Warlock", 45, 54.0f, 9973),
    ORANGE_SALAMANDER("Orange Salamander", 47, 224.0f, 10146),
    RAZOR_BACKED_KEBBIT("Razor-backed Kebbit", 49, 348.0f, 9961),
    MAHOGANY_BIRD_HOUSE("Mahogany Bird House", 49, 960.0f, 22195),
    ECLECTIC_IMPLING("Eclectic Impling", 50, 32.0f, 11248),
    SABRE_TOOTHED_KEBBIT("Sabre-toothed Kebbit", 51, 200.0f, 9959),
    CHINCHOMPA("Chinchompa", 53, 198.4f, 9976),
    SABRE_TOOTHED_KYATT("Sabre-toothed Kyatt", 55, 300.0f, 10039),
    DARK_KEBBIT("Dark Kebbit", 57, 132.0f, 9963),
    NATURE_IMPLING("Nature Impling", 58, 34.0f, 11250),
    RED_SALAMANDER("Red Salamander", 59, 272.0f, 10147),
    YEW_BIRD_HOUSE("Yew Bird House", 59, 1020.0f, 22198),
    MANIACAL_MONKEY("Maniacal Monkey", 60, 1000.0f, 19556),
    CARNIVOROUS_CHINCHOMPA("Carnivorous Chinchompa", 63, 265.0f, 9977),
    MAGPIE_IMPLING("Magpie Impling", 65, 44.0f, 11252),
    MAGPIE_IMPLING_GIELINOR("Magpie Impling (Gielinor)", 65, 216.0f, 11252),
    BLACK_SALAMANDER("Black Salamander", 67, 319.5f, 10148),
    DASHING_KEBBIT("Dashing Kebbit", 69, 156.0f, 9964),
    BLACK_CHINCHOMPA("Black Chinchompa", 73, 315.0f, 11959),
    MAGIC_BIRD_HOUSE("Magic Bird House", 74, 1140.0f, 22201),
    NINJA_IMPLING("Ninja Impling", 74, 52.0f, 11254),
    NINJA_IMPLING_GIELINOR("Ninja Impling (Gielinor)", 74, 240.0f, 11254),
    CRYSTAL_IMPLING("Crystal Impling", 80, 280.0f, 23768),
    DRAGON_IMPLING("Dragon Impling", 83, 65.0f, 11256),
    DRAGON_IMPLING_GIELINOR("Dragon Impling (Gielinor)", 83, 300.0f, 11256),
    REDWOOD_BIRD_HOUSE("Redwood Bird House", 89, 1200.0f, 22204),
    LUCKY_IMPLING("Lucky Impling", 89, 380.0f, 19732);

    private final String name;
    private final int level;
    private final float xp;
    private final int icon;

    @Override
    public boolean isMembers(ItemManager itemManager) {
        return true;
    }

    private HunterAction(String name, int level, float xp, int icon) {
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public float getXp() {
        return this.xp;
    }

    @Override
    public int getIcon() {
        return this.icon;
    }
}

