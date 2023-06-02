/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Nameable
 */
package rs.api;

import net.runelite.api.Nameable;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSName;

public interface RSNameable
extends Nameable,
Comparable<Nameable> {
    @Import(value="name")
    public RSName getRsName();

    @Import(value="prevName")
    public RSName getRsPrevName();
}

