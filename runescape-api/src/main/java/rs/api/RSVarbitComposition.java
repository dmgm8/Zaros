/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.VarbitComposition
 */
package rs.api;

import net.runelite.api.VarbitComposition;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCacheableNode;

public interface RSVarbitComposition
extends RSCacheableNode,
VarbitComposition {
    @Import(value="configId")
    public int getIndex();

    @Import(value="leastSignificantBit")
    public int getLeastSignificantBit();

    @Import(value="mostSignificantBit")
    public int getMostSignificantBit();
}

