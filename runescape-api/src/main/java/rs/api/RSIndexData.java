/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSIndexDataBase;

public interface RSIndexData
extends RSIndexDataBase {
    @Import(value="index")
    public int getIndex();
}

