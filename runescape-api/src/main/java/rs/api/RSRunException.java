/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSRunException {
    @Import(value="parent")
    public Throwable getParent();
}

