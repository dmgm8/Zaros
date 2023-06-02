/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import java.util.EnumSet;
import net.runelite.api.WorldType;

public interface World {
    public EnumSet<WorldType> getTypes();

    public void setTypes(EnumSet<WorldType> var1);

    public int getPlayerCount();

    public void setPlayerCount(int var1);

    public int getLocation();

    public void setLocation(int var1);

    public int getIndex();

    public void setIndex(int var1);

    public int getId();

    public void setId(int var1);

    public String getActivity();

    public void setActivity(String var1);

    public String getAddress();

    public void setAddress(String var1);
}

