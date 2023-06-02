/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.NPC
 */
package rs.api;

import net.runelite.api.NPC;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSActor;
import net.runelite.rs.api.RSNPCComposition;

public interface RSNPC
extends RSActor,
NPC {
    @Import(value="composition")
    public RSNPCComposition getComposition();

    public int getIndex();

    public void setIndex(int var1);
}

