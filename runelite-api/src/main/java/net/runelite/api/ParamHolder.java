/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.IterableHashTable;
import net.runelite.api.Node;

public interface ParamHolder {
    public IterableHashTable<Node> getParams();

    public void setParams(IterableHashTable<Node> var1);

    public int getIntValue(int var1);

    public void setValue(int var1, int var2);

    public String getStringValue(int var1);

    public void setValue(int var1, String var2);
}

