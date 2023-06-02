/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.runelite.api.Player
 */
package net.runelite.client.plugins.mining;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.runelite.api.Player;

enum Pickaxe {
    BRONZE(1265, 625, 6753),
    IRON(1267, 626, 6754),
    STEEL(1269, 627, 6755),
    BLACK(12297, 3873, 3866),
    MITHRIL(1273, 629, 6757),
    ADAMANT(1271, 628, 6756),
    RUNE(1275, 624, 6752),
    GILDED(23276, 8313, 8312),
    DRAGON(11920, 7139, 6758),
    DRAGON_OR(23677, 8346, 8344),
    DRAGON_OR_TRAILBLAZER(25376, 8887, 8886),
    DRAGON_UPGRADED(12797, 642, 335),
    INFERNAL(13243, 4482, 4481),
    THIRDAGE(20014, 7283, 7282),
    CRYSTAL(23680, 8347, 8345),
    TRAILBLAZER(25063, 8787, 8788, 8789, 8786);

    private final int itemId;
    private final int[] animIds;
    private static final Map<Integer, Pickaxe> PICKAXE_ANIM_IDS;

    private Pickaxe(int itemId, int ... animIds) {
        this.itemId = itemId;
        this.animIds = animIds;
    }

    boolean matchesMiningAnimation(Player player) {
        return player != null && Pickaxe.fromAnimation(player.getAnimation()) == this;
    }

    static Pickaxe fromAnimation(int animId) {
        return PICKAXE_ANIM_IDS.get(animId);
    }

    public int getItemId() {
        return this.itemId;
    }

    public int[] getAnimIds() {
        return this.animIds;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Pickaxe pickaxe : Pickaxe.values()) {
            for (int animId : pickaxe.animIds) {
                builder.put((Object)animId, (Object)pickaxe);
            }
        }
        PICKAXE_ANIM_IDS = builder.build();
    }
}

