/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.HealthBar
 */
package rs.api;

import net.runelite.api.HealthBar;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSCacheableNode;
import net.runelite.rs.api.RSSpritePixels;

public interface RSHealthBar
extends RSCacheableNode,
HealthBar {
    @Import(value="healthScale")
    public int getHealthScale();

    @Import(value="healthBarFrontSpriteId")
    public int getHealthBarFrontSpriteId();

    @Import(value="getHealthBarFrontSprite")
    public RSSpritePixels getHealthBarFrontSprite();

    @Import(value="getHealthBarBackSprite")
    public RSSpritePixels getHealthBarBackSprite();

    @Import(value="healthBarPadding")
    public void setPadding(int var1);
}

