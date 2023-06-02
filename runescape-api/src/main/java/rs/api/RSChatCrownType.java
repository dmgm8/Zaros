/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ChatCrownType
 */
package rs.api;

import net.runelite.api.ChatCrownType;
import net.runelite.mapping.Import;

public interface RSChatCrownType
extends ChatCrownType {
    @Import(value="id")
    public int getId();

    @Import(value="id")
    public void setId(int var1);

    @Import(value="icon")
    public int getIcon();

    @Import(value="icon")
    public void setIcon(int var1);

    @Import(value="moderator")
    public boolean isModerator();

    @Import(value="moderator")
    public void setModerator(boolean var1);

    @Import(value="ignorable")
    public boolean isIgnorable();

    @Import(value="ignorable")
    public void setIgnorable(boolean var1);
}

