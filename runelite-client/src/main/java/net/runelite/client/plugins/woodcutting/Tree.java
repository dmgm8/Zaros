/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 *  javax.annotation.Nullable
 */
package net.runelite.client.plugins.woodcutting;

import com.google.common.collect.ImmutableMap;
import java.time.Duration;
import java.util.Map;
import javax.annotation.Nullable;
import net.runelite.client.util.RSTimeUnit;

enum Tree {
    REGULAR_TREE(null, 1276, 1277, 1278, 1279, 1280, 40750, 40752),
    OAK_TREE(Duration.of(14L, RSTimeUnit.GAME_TICKS), 4540, 10820),
    WILLOW_TREE(Duration.of(14L, RSTimeUnit.GAME_TICKS), 10819, 10829, 10831, 10833),
    MAPLE_TREE(Duration.of(59L, RSTimeUnit.GAME_TICKS), new int[]{10832, 36681, 40754}){

        @Override
        Duration getRespawnTime(int region) {
            return region == 10044 ? Duration.of(14L, RSTimeUnit.GAME_TICKS) : ((Tree)this).respawnTime;
        }
    }
    ,
    TEAK_TREE(Duration.of(15L, RSTimeUnit.GAME_TICKS), 9036, 36686, 40758),
    MAHOGANY_TREE(Duration.of(14L, RSTimeUnit.GAME_TICKS), 9034, 36688, 40760),
    YEW_TREE(Duration.of(99L, RSTimeUnit.GAME_TICKS), 10822, 10823, 36683, 40756),
    MAGIC_TREE(Duration.of(199L, RSTimeUnit.GAME_TICKS), 10834, 10835),
    REDWOOD(Duration.of(199L, RSTimeUnit.GAME_TICKS), 29668, 29670);

    @Nullable
    private final Duration respawnTime;
    private final int[] treeIds;
    private static final int MISCELLANIA_REGION = 10044;
    private static final Map<Integer, Tree> TREES;

    private Tree(Duration respawnTime, int ... treeIds) {
        this.respawnTime = respawnTime;
        this.treeIds = treeIds;
    }

    Duration getRespawnTime(int region) {
        return this.respawnTime;
    }

    static Tree findTree(int objectId) {
        return TREES.get(objectId);
    }

    @Nullable
    public Duration getRespawnTime() {
        return this.respawnTime;
    }

    public int[] getTreeIds() {
        return this.treeIds;
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Tree tree : Tree.values()) {
            for (int treeId : tree.treeIds) {
                builder.put((Object)treeId, (Object)tree);
            }
        }
        TREES = builder.build();
    }
}

