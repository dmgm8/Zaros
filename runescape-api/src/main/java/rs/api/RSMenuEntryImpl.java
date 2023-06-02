/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.MenuEntry
 */
package rs.api;

import java.util.function.Consumer;
import net.runelite.api.MenuEntry;

public interface RSMenuEntryImpl
extends MenuEntry {
    public int getIdx();

    public void setIdx(int var1);

    public Consumer getConsumer();

    public void setConsumer(Consumer var1);

    public void setItemId(int var1);
}

