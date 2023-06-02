/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSCacheableNode
extends RSNode {
    @Import(value="queuePrevious")
    public RSCacheableNode getQueuePrevious();

    @Import(value="unlinkDual")
    public void unlinkDual();
}

