/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSParamComposition {
    @Import(value="type")
    public char getType();

    @Import(value="defaultInt")
    public int getDefaultInt();

    @Import(value="defaultString")
    public String getDefaultString();

    public boolean isString();
}

