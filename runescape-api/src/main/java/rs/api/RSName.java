/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSName
extends Comparable {
    @Import(value="name")
    public String getName();

    @Import(value="name")
    public void setName(String var1);

    @Import(value="cleanName")
    public String getCleanName();

    @Import(value="cleanName")
    public void setCleanName(String var1);
}

