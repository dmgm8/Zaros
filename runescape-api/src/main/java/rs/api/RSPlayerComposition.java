/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.runelite.api.ObjTypeCustomisation
 *  net.runelite.api.PlayerComposition
 */
package rs.api;

import javax.annotation.Nullable;
import net.runelite.api.ObjTypeCustomisation;
import net.runelite.api.PlayerComposition;
import net.runelite.mapping.Import;

public interface RSPlayerComposition
extends PlayerComposition {
    @Import(value="isFemale")
    public boolean isFemale();

    @Import(value="colors")
    public int[] getColors();

    @Import(value="equipmentIds")
    public int[] getEquipmentIds();

    @Import(value="transformedNpcId")
    public void setTransformedNpcId(int var1);

    @Import(value="customisations")
    @Nullable
    public ObjTypeCustomisation[] getColorTextureOverrides();

    @Import(value="setHash")
    public void setHash();
}

