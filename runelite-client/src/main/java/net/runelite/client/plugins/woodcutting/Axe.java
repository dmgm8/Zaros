/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.runelite.api.Player
 */
package net.runelite.client.plugins.woodcutting;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.runelite.api.Player;

enum Axe {
    BRONZE(879, 1351),
    IRON(877, 1349),
    STEEL(875, 1353),
    BLACK(873, 1361),
    MITHRIL(871, 1355),
    ADAMANT(869, 1357),
    RUNE(867, 1359),
    GILDED(8303, 23279),
    DRAGON(2846, 6739),
    DRAGON_OR(24, 25378),
    INFERNAL(2117, 13241),
    THIRDAGE(7264, 20011),
    CRYSTAL(8324, 23673),
    TRAILBLAZER(8778, 25066);

    private final Integer animId;
    private final Integer itemId;
    private static final Map<Integer, Axe> AXE_ANIM_IDS;

    boolean matchesChoppingAnimation(Player player) {
        return player != null && this.animId.intValue() == player.getAnimation();
    }

    static Axe findAxeByAnimId(int animId) {
        return AXE_ANIM_IDS.get(animId);
    }

    private Axe(Integer animId, Integer itemId) {
        this.animId = animId;
        this.itemId = itemId;
    }

    public Integer getAnimId() {
        return this.animId;
    }

    public Integer getItemId() {
        return this.itemId;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Axe axe : Axe.values()) {
            builder.put((Object)axe.animId, (Object)axe);
        }
        AXE_ANIM_IDS = builder.build();
    }
}

