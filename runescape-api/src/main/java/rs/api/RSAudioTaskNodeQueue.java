/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSTaskDataNode;

public interface RSAudioTaskNodeQueue {
    @Import(value="queueAudioTaskNode")
    public void queueAudioTaskNode(RSTaskDataNode var1);
}

