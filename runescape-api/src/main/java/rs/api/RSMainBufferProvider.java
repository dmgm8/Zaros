/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.MainBufferProvider
 */
package rs.api;

import java.awt.Component;
import java.awt.Image;
import net.runelite.api.MainBufferProvider;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSBufferProvider;

public interface RSMainBufferProvider
extends RSBufferProvider,
MainBufferProvider {
    @Import(value="image")
    public Image getImage();

    @Import(value="image")
    public void setImage(Image var1);

    @Import(value="canvas")
    public Component getCanvas();
}

