/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCacheableNode;
import net.runelite.rs.api.RSFrame;

public interface RSFrames
extends RSCacheableNode {
    @Import(value="skeletons")
    public RSFrame[] getFrames();
}

