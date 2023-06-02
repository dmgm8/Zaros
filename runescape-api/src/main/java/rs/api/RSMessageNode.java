/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.MessageNode
 */
package rs.api;

import net.runelite.api.MessageNode;
import net.runelite.mapping.Import;

public interface RSMessageNode
extends MessageNode {
    @Import(value="id")
    public int getId();

    @Import(value="type")
    public int getRSType();

    @Import(value="name")
    public String getName();

    @Import(value="name")
    public void setName(String var1);

    @Import(value="sender")
    public String getSender();

    @Import(value="sender")
    public void setSender(String var1);

    @Import(value="value")
    public String getRSValue();

    @Import(value="value")
    public void setRSValue(String var1);
}

