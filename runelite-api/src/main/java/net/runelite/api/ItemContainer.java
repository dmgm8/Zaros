/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 */
package net.runelite.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.Item;
import net.runelite.api.Node;

public interface ItemContainer
extends Node {
    public int getId();

    @Nonnull
    public Item[] getItems();

    @Nullable
    public Item getItem(int var1);

    public boolean contains(int var1);

    public int count(int var1);

    public int size();
}

