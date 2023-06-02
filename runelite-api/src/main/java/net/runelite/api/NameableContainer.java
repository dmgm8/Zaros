/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.Nameable;

public interface NameableContainer<T extends Nameable> {
    public int getCount();

    public int getSize();

    public T[] getMembers();

    public T findByName(String var1);
}

