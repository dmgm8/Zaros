/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 */
package net.runelite.client.plugins.poh;

import com.google.common.collect.ImmutableMap;
import java.awt.image.BufferedImage;
import java.util.Map;
import net.runelite.client.util.ImageUtil;

public enum PohIcons {
    EXITPORTAL("exitportal", 4525),
    VARROCK("varrock", 13615, 13622, 13629),
    FALADOR("falador", 13617, 13624, 13631),
    LUMBRIDGE("lumbridge", 13616, 13623, 13630),
    ARDOUGNE("ardougne", 13619, 13626, 13633),
    YANILLE("yanille", 13620, 13627, 13634),
    CAMELOT("camelot", 13618, 13625, 13632),
    LUNARISLE("lunarisle", 29339, 29347, 29355),
    WATERBIRTH("waterbirth", 29342, 29350, 29358),
    FISHINGGUILD("fishingguild", 29343, 29351, 29359),
    SENNTISTEN("senntisten", 29340, 29348, 29356),
    KHARYLL("kharyll", 29338, 29346, 29354),
    ANNAKARL("annakarl", 29341, 29349, 29357),
    KOUREND("kourend", 29345, 29353, 29361),
    MARIM("marim", 29344, 29352, 29360),
    TROLLSTRONGHOLD("trollstronghold", 33179, 33180, 33181),
    GHORROCK("ghorrock", 33433, 33436, 33439),
    CARRALLANGAR("carrallangar", 33434, 33437, 33440),
    CATHERBY("catherby", 33432, 33435, 33438),
    WEISS("weiss", 37581, 37593, 37605),
    APEATOLLDUNGEON("apeatolldungeon", 37592, 37604, 37616),
    BARROWS("barrows", 37591, 37603, 37615),
    BATTLEFRONT("battlefront", 37584, 37596, 37608),
    CEMETERY("cemetery", 37590, 37602, 37614),
    DRAYNORMANOR("draynormanor", 37583, 37595, 37607),
    FENKENSTRAINSCASTLE("fenkenstrainscastle", 37587, 37599, 37611),
    HARMONYISLAND("harmonyisland", 37589, 37601, 37613),
    ARCEUUSLIBRARY("arceuuslibrary", 41416, 41417, 41418),
    MINDALTAR("mindaltar", 37585, 37597, 37609),
    SALVEGRAVEYARD("salvegraveyard", 37586, 37598, 37610),
    WESTARDOUGNE("westardougne", 37588, 37600, 37612),
    ALTAR("altar", 13179, 13180, 13181, 13182, 13183, 13184, 13185, 13186, 13187, 13188, 13189, 13190, 13191, 13192, 13193, 13194, 13196, 13197, 13198, 13199),
    POOLS("pool", 29237, 29238, 29239, 29240, 29241, 40844, 40845, 40846, 40847, 40848),
    GLORY("glory", 13523),
    REPAIR("repair", 6802),
    SPELLBOOKALTAR("spellbook", 29147, 29148, 27979, 29150),
    JEWELLERYBOX("jewellery", 29154, 29155, 29156),
    MAGICTRAVEL("transportation", 29227, 29228, 29229, 31554, 40778, 40779),
    PORTALNEXUS("portalnexus", 27097, 33355, 33356, 33357, 33358, 33359, 33360, 33361, 33362, 33363, 33364, 33365, 33366, 33367, 33368, 33369, 33370, 33371, 33372, 33373, 33374, 33375, 33376, 33377, 33378, 33379, 33380, 33381, 33382, 33383, 33384, 33385, 33386, 33387, 33388, 33389, 33390, 33391, 33392, 33393, 33394, 33395, 33396, 33397, 33398, 33399, 33400, 33401, 33402, 33403, 33404, 33405, 33406, 33407, 33408, 33409, 33410, 33423, 33424, 33425, 33426, 33427, 33428, 33429, 33430, 33431, 37547, 37548, 37549, 37550, 37551, 37552, 37553, 37554, 37555, 37556, 37557, 37559, 37560, 37561, 37562, 37563, 37564, 37565, 37566, 37567, 37568, 37569, 37571, 37572, 37573, 37574, 37575, 37576, 37577, 37578, 37579, 37580, 41413, 41414, 41415),
    XERICSTALISMAN("xericstalisman", 33411, 33412, 33413, 33414, 33415, 33419),
    DIGSITEPENDANT("digsitependant", 33416, 33417, 33418, 33420),
    MYTHICALCAPE("mythicalcape", 31986, 31983);

    private static final Map<Integer, PohIcons> minimapIcons;
    private final String imageResource;
    private final int[] Ids;
    private BufferedImage image;

    private PohIcons(String imageResource, int ... ids) {
        this.imageResource = imageResource;
        this.Ids = ids;
    }

    public static PohIcons getIcon(int id) {
        return minimapIcons.get(id);
    }

    public BufferedImage getImage() {
        if (this.image != null) {
            return this.image;
        }
        this.image = ImageUtil.loadImageResource(((Object)((Object)this)).getClass(), this.getImageResource() + ".png");
        return this.image;
    }

    public String getImageResource() {
        return this.imageResource;
    }

    public int[] getIds() {
        return this.Ids;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (PohIcons icon : PohIcons.values()) {
            int[] arrn = icon.getIds();
            int n = arrn.length;
            for (int i = 0; i < n; ++i) {
                Integer spotId = arrn[i];
                builder.put((Object)spotId, (Object)icon);
            }
        }
        minimapIcons = builder.build();
    }
}

