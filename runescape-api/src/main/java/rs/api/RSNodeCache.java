/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.NodeCache
 */
package rs.api;

import net.runelite.api.NodeCache;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCacheableNode;
import net.runelite.rs.api.RSIterableDualNodeQueue;
import net.runelite.rs.api.RSIterableHashTable;

public interface RSNodeCache
extends NodeCache {
    @Import(value="node")
    public RSCacheableNode getNode();

    @Import(value="list")
    public RSIterableDualNodeQueue getList();

    @Import(value="table")
    public void setTable(RSIterableHashTable var1);

    @Import(value="get")
    public RSCacheableNode get(long var1);

    @Import(value="put")
    public void put(RSCacheableNode var1, long var2);

    @Import(value="reset")
    public void reset();

    @Import(value="capacity")
    public int getRSCapacity();

    @Import(value="capacity")
    public void setRSCapacity(int var1);

    public int getInitialCapacity();

    public void increaseSize(int var1);

    public void resize(int var1);

    @Import(value="remainingCapacity")
    public int getRemainingCapacity();

    public float getThrashAvg();

    public void setThrashAvg(float var1);

    public boolean isThrashing();
}

