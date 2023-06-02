/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ItemContainer
 */
package rs.api;

import net.runelite.api.ItemContainer;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSItemContainer
extends RSNode,
ItemContainer {
    @Import(value="itemIds")
    public int[] getItemIds();

    @Import(value="stackSizes")
    public int[] getStackSizes();
}

