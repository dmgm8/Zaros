/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.WidgetNode
 */
package rs.api;

import net.runelite.api.WidgetNode;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSWidgetNode
extends RSNode,
WidgetNode {
    @Import(value="id")
    public int getId();

    @Import(value="type")
    public int getModalMode();
}

