/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSFrameMap
extends RSNode {
    @Import(value="count")
    public int getCount();

    @Import(value="types")
    public int[] getTypes();

    @Import(value="list")
    public int[][] getList();
}

