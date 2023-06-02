/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Animation
 */
package rs.api;

import net.runelite.api.Animation;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSNode;

public interface RSAnimation
extends RSNode,
Animation {
    @Import(value="frameIDs")
    public int[] getFrameIDs();

    @Import(value="frameLengths")
    public int[] getFrameLenths();

    @Import(value="frameStep")
    public int getFrameStep();

    @Import(value="transformSpotAnimModel")
    public RSModel transformSpotAnimModel(RSModel var1, int var2);

    public void setId(int var1);

    @Import(value="hasSkeleton")
    public boolean hasSkeleton();
}

