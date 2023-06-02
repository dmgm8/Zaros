/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Nameable
 *  net.runelite.api.NameableContainer
 */
package rs.api;

import net.runelite.api.Nameable;
import net.runelite.api.NameableContainer;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSName;
import net.runelite.rs.api.RSNameable;

public interface RSNameableContainer<T extends Nameable>
extends NameableContainer<T> {
    @Import(value="nameables")
    public Nameable[] getNameables();

    @Import(value="count")
    public int getCount();

    @Import(value="nameables")
    public void setNameables(RSNameable[] var1);

    @Import(value="isMember")
    public boolean isMember(RSName var1);

    @Import(value="size")
    public int getSize();

    @Import(value="size")
    public void setSize(int var1);

    @Import(value="findByName")
    public T findByName(RSName var1);

    @Import(value="createArray")
    public RSNameable[] createArray(int var1);

    public void rl$add(RSName var1, RSName var2);

    public void rl$remove(RSNameable var1);
}

