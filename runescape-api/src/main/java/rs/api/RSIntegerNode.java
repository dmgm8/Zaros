/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.IntegerNode
 */
package rs.api;

import net.runelite.api.IntegerNode;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSIntegerNode
extends RSNode,
IntegerNode {
    @Import(value="value")
    public int getValue();

    @Import(value="value")
    public void setValue(int var1);
}

