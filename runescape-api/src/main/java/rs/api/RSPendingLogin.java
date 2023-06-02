/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.PendingLogin
 */
package rs.api;

import net.runelite.api.PendingLogin;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSName;

public interface RSPendingLogin
extends PendingLogin {
    @Import(value="name")
    public RSName getRSName();

    @Import(value="world")
    public short getWorld();
}

