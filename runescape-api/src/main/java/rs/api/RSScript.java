/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Script
 */
package rs.api;

import net.runelite.api.Script;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCacheableNode;

public interface RSScript
extends Script,
RSCacheableNode {
    @Import(value="intOperands")
    public int[] getIntOperands();

    @Import(value="instructions")
    public int[] getInstructions();

    @Import(value="intStackCount")
    public int getIntStackCount();

    @Import(value="stringStackCount")
    public int getStringStackCount();
}

