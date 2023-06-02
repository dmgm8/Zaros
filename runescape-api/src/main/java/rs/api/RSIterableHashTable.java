/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.IterableHashTable
 *  net.runelite.api.Node
 */
package rs.api;

import net.runelite.api.IterableHashTable;
import net.runelite.api.Node;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSIterableHashTable
extends IterableHashTable {
    @Import(value="get")
    public RSNode get(long var1);

    @Import(value="put")
    public void put(Node var1, long var2);
}

