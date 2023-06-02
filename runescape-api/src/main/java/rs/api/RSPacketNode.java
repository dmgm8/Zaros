/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSPacketBuffer;

public interface RSPacketNode {
    @Import(value="packetBuffer")
    public RSPacketBuffer getPacketBuffer();
}

