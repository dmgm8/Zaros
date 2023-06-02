/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Deque
 */
package rs.api;

import net.runelite.api.Deque;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSLinkable;

public interface RSLinkableDeque<T extends RSLinkable>
extends Deque<T> {
    @Import(value="addLast")
    public void addLast(T var1);

    @Import(value="sentinel")
    public RSLinkable getSentinel();

    @Import(value="current")
    public void setCurrent(RSLinkable var1);
}

