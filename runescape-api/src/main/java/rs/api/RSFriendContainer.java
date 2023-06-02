/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Deque
 *  net.runelite.api.FriendContainer
 *  net.runelite.api.PendingLogin
 */
package rs.api;

import net.runelite.api.Deque;
import net.runelite.api.FriendContainer;
import net.runelite.api.PendingLogin;
import net.runelite.mapping.Import;

public interface RSFriendContainer
extends FriendContainer {
    @Import(value="pendingLogins")
    public Deque<PendingLogin> getPendingLogins();
}

