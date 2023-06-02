/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.EnumComposition
 */
package rs.api;

import net.runelite.api.EnumComposition;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCacheableNode;

public interface RSEnumComposition
extends EnumComposition,
RSCacheableNode {
    @Import(value="size")
    public int size();

    @Import(value="keys")
    public int[] getKeys();

    @Import(value="intVals")
    public int[] getIntVals();

    @Import(value="stringVals")
    public String[] getStringVals();

    @Import(value="defaultInt")
    public int getDefaultInt();

    @Import(value="defaultString")
    public String getDefaultString();
}

