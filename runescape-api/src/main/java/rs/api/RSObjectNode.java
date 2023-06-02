/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSObjectNode
extends RSNode {
    @Import(value="value")
    public Object getValue();

    @Import(value="value")
    public void setValue(Object var1);
}

