/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSFrameMap;

public interface RSFrame {
    @Import(value="skin")
    public RSFrameMap getSkin();

    @Import(value="transformCount")
    public int getTransformCount();

    @Import(value="transformTypes")
    public int[] getTransformTypes();

    @Import(value="translator_x")
    public int[] getTranslatorX();

    @Import(value="translator_y")
    public int[] getTranslatorY();

    @Import(value="translator_z")
    public int[] getTranslatorZ();

    @Import(value="showing")
    public boolean isShowing();
}

