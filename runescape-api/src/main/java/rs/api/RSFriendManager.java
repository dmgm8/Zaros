/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.FriendContainer
 *  net.runelite.api.Ignore
 *  net.runelite.api.NameableContainer
 */
package rs.api;

import net.runelite.api.FriendContainer;
import net.runelite.api.Ignore;
import net.runelite.api.NameableContainer;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSName;

public interface RSFriendManager {
    @Import(value="friendContainer")
    public FriendContainer getFriendContainer();

    @Import(value="ignoreContainer")
    public NameableContainer<Ignore> getIgnoreContainer();

    @Import(value="isFriended")
    public boolean isFriended(RSName var1, boolean var2);

    @Import(value="isIgnored")
    public boolean isIgnored(RSName var1);
}

