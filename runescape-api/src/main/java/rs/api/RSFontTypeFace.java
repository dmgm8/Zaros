/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.FontTypeFace
 */
package rs.api;

import net.runelite.api.FontTypeFace;
import net.runelite.mapping.Import;

public interface RSFontTypeFace
extends FontTypeFace {
    @Import(value="getTextWidth")
    public int getTextWidth(String var1);

    @Import(value="verticalSpace")
    public int getBaseline();

    @Import(value="drawTextLeftAligned")
    public void drawTextLeftAligned(String var1, int var2, int var3, int var4, int var5);
}

