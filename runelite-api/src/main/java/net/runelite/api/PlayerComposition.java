/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.ColorTextureOverride;
import net.runelite.api.kit.KitType;

public interface PlayerComposition {
    public boolean isFemale();

    public int[] getColors();

    public int[] getEquipmentIds();

    public int getEquipmentId(KitType var1);

    public int getKitId(KitType var1);

    public void setHash();

    public void setTransformedNpcId(int var1);

    @Nullable
    public ColorTextureOverride[] getColorTextureOverrides();
}

