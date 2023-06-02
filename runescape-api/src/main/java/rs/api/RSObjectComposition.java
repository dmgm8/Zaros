/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.IterableHashTable
 *  net.runelite.api.Node
 *  net.runelite.api.ObjectComposition
 */
package rs.api;

import net.runelite.api.IterableHashTable;
import net.runelite.api.Node;
import net.runelite.api.ObjectComposition;
import net.runelite.mapping.Import;

public interface RSObjectComposition
extends ObjectComposition {
    @Import(value="id")
    public int getId();

    @Import(value="name")
    public String getName();

    @Import(value="actions")
    public String[] getActions();

    @Import(value="mapSceneId")
    public int getMapSceneId();

    @Import(value="mapSceneId")
    public void setMapSceneId(int var1);

    @Import(value="mapIconId")
    public int getMapIconId();

    @Import(value="mapIconId")
    public void setMapIconId(int var1);

    @Import(value="impostorIds")
    public int[] getImpostorIds();

    @Import(value="getImpostor")
    public RSObjectComposition getImpostor();

    @Import(value="multivar")
    public int getVarPlayerId();

    @Import(value="multivarbit")
    public int getVarbitId();

    @Import(value="params")
    public IterableHashTable<Node> getParams();

    @Import(value="params")
    public void setParams(IterableHashTable<Node> var1);
}

