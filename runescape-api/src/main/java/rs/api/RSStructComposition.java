/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.IterableHashTable
 *  net.runelite.api.Node
 *  net.runelite.api.StructComposition
 */
package rs.api;

import net.runelite.api.IterableHashTable;
import net.runelite.api.Node;
import net.runelite.api.StructComposition;
import net.runelite.mapping.Import;

public interface RSStructComposition
extends StructComposition {
    public void setId(int var1);

    @Import(value="params")
    public IterableHashTable<Node> getParams();

    @Import(value="params")
    public void setParams(IterableHashTable<Node> var1);
}

