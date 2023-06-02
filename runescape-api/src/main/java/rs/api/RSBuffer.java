/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Buffer
 */
package rs.api;

import net.runelite.api.Buffer;
import net.runelite.mapping.Import;

public interface RSBuffer
extends Buffer {
    @Import(value="payload")
    public byte[] getPayload();

    @Import(value="offset")
    public int getOffset();

    @Import(value="offset")
    public void setOffset(int var1);

    @Import(value="g2")
    public int g2();

    @Import(value="g4")
    public int g4();

    @Import(value="p1")
    public void p1(int var1);

    @Import(value="p2")
    public void p2(int var1);

    @Import(value="pjstr")
    public void pjstr(String var1);
}

