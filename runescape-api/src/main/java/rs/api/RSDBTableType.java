/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSDBTableType {
    @Import(value="types")
    public int[][] getTypes();

    @Import(value="defaultValues")
    public Object[][] getDefaultValues();
}

