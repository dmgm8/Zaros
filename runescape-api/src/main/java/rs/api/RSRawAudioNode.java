/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSResampler;

public interface RSRawAudioNode {
    @Import(value="applyResampler")
    public RSRawAudioNode applyResampler(RSResampler var1);
}

