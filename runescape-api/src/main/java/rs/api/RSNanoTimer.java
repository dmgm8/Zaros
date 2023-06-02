/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSNanoTimer {
    @Import(value="nanoTime")
    public long getNanoTime();

    @Import(value="nanoTime")
    public void setNanoTime(long var1);
}

