/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Node
 */
package rs.api;

import net.runelite.api.Node;
import net.runelite.mapping.Import;

public interface RSNode
extends Node {
    @Import(value="next")
    public RSNode getNext();

    @Import(value="hash")
    public long getHash();

    @Import(value="previous")
    public RSNode getPrevious();

    @Import(value="unlink")
    public void unlink();

    public void onUnlink();
}

