/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.runelite.api.Actor;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.widgets.Widget;

public interface MenuEntry {
    public String getOption();

    public MenuEntry setOption(String var1);

    public String getTarget();

    public MenuEntry setTarget(String var1);

    public int getIdentifier();

    public MenuEntry setIdentifier(int var1);

    public MenuAction getType();

    public MenuEntry setType(MenuAction var1);

    public int getParam0();

    public MenuEntry setParam0(int var1);

    public int getParam1();

    public MenuEntry setParam1(int var1);

    public boolean isForceLeftClick();

    public MenuEntry setForceLeftClick(boolean var1);

    public boolean isDeprioritized();

    public MenuEntry setDeprioritized(boolean var1);

    public MenuEntry onClick(Consumer<MenuEntry> var1);

    public MenuEntry setParent(MenuEntry var1);

    @Nullable
    public MenuEntry getParent();

    public boolean isItemOp();

    public int getItemOp();

    public int getItemId();

    @Nullable
    public Widget getWidget();

    @Nullable
    public NPC getNpc();

    @Nullable
    public Player getPlayer();

    @Nullable
    public Actor getActor();
}

