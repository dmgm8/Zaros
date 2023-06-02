/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSRawAudioNode;

public interface RSSoundEffect {
    @Import(value="toRawAudioNode")
    public RSRawAudioNode toRawAudioNode();
}

