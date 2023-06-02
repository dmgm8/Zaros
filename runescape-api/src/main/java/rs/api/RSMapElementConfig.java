/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.MapElementConfig
 */
package rs.api;

import net.runelite.api.MapElementConfig;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCacheableNode;
import net.runelite.rs.api.RSSpritePixels;

public interface RSMapElementConfig
extends RSCacheableNode,
MapElementConfig {
    @Import(value="getMapIcon")
    public RSSpritePixels getMapIcon(boolean var1);
}

