/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSIsaac;
import net.runelite.rs.api.RSPacketNode;

public interface RSNetWriter {
    @Import(value="isaac")
    public RSIsaac getIsaac();

    @Import(value="queue")
    public void queue(RSPacketNode var1);
}

