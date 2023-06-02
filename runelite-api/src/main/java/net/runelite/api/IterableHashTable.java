/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Node;

public interface IterableHashTable<T extends Node>
extends Iterable<T> {
    public T get(long var1);

    public void put(T var1, long var2);
}

