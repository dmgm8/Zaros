/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.party.data;

import java.awt.Color;
import net.runelite.api.coords.WorldPoint;

public class PartyTilePingData {
    private final WorldPoint point;
    private final Color color;
    private int alpha = 255;

    public PartyTilePingData(WorldPoint point, Color color) {
        this.point = point;
        this.color = color;
    }

    public WorldPoint getPoint() {
        return this.point;
    }

    public Color getColor() {
        return this.color;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}

