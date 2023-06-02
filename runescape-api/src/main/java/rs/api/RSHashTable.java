/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.HashTable
 */
package rs.api;

import net.runelite.api.HashTable;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSHashTable<T extends RSNode>
extends HashTable<T> {
    @Import(value="get")
    public T get(long var1);

    @Import(value="size")
    public int getSize();

    @Import(value="buckets")
    public T[] getBuckets();
}

