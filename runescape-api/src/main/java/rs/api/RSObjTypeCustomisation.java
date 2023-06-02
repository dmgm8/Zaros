/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ObjTypeCustomisation
 */
package rs.api;

import net.runelite.api.ObjTypeCustomisation;
import net.runelite.mapping.Import;

public interface RSObjTypeCustomisation
extends ObjTypeCustomisation {
    @Import(value="recol")
    public short[] getColorToReplaceWith();

    @Import(value="retex")
    public short[] getTextureToReplaceWith();
}

