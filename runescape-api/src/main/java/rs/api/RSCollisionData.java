/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.CollisionData
 */
package rs.api;

import net.runelite.api.CollisionData;
import net.runelite.mapping.Import;

public interface RSCollisionData
extends CollisionData {
    @Import(value="flags")
    public int[][] getFlags();
}

