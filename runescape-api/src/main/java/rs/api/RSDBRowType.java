/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSDBRowType {
    @Import(value="table")
    public int getTable();

    @Import(value="getColumn")
    public Object[] getColumn(int var1);
}

