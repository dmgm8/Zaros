/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Deque
 */
package rs.api;

import net.runelite.api.Deque;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSDeque<T extends RSNode>
extends Deque<T> {
    @Import(value="current")
    public T getCurrent();

    @Import(value="head")
    public T getHead();

    @Import(value="getFront")
    public T getFront();

    @Import(value="getNext")
    public T getNext();

    @Import(value="addLast")
    public void addLast(T var1);

    @Import(value="clear")
    public void clear();
}

