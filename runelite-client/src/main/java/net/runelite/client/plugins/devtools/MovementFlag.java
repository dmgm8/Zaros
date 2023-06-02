/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.devtools;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

enum MovementFlag {
    BLOCK_MOVEMENT_NORTH_WEST(1),
    BLOCK_MOVEMENT_NORTH(2),
    BLOCK_MOVEMENT_NORTH_EAST(4),
    BLOCK_MOVEMENT_EAST(8),
    BLOCK_MOVEMENT_SOUTH_EAST(16),
    BLOCK_MOVEMENT_SOUTH(32),
    BLOCK_MOVEMENT_SOUTH_WEST(64),
    BLOCK_MOVEMENT_WEST(128),
    BLOCK_MOVEMENT_OBJECT(256),
    BLOCK_MOVEMENT_FLOOR_DECORATION(262144),
    BLOCK_MOVEMENT_FLOOR(0x200000),
    BLOCK_MOVEMENT_FULL(2359552);

    private int flag;

    public static Set<MovementFlag> getSetFlags(int collisionData) {
        return Arrays.stream(MovementFlag.values()).filter(movementFlag -> (movementFlag.flag & collisionData) != 0).collect(Collectors.toSet());
    }

    private MovementFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return this.flag;
    }
}

