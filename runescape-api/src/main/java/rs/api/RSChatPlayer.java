/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ChatPlayer
 */
package rs.api;

import net.runelite.api.ChatPlayer;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNameable;

public interface RSChatPlayer
extends ChatPlayer,
RSNameable {
    @Import(value="world")
    public int getWorld();

    @Import(value="rank")
    public int getRSRank();
}

