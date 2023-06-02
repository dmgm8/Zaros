/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSMiniMenuEntry {
    @Import(value="type")
    public int getType();

    @Import(value="type")
    public void setType(int var1);

    @Import(value="arg1")
    public int getArg1();

    @Import(value="arg1")
    public void setArg1(int var1);

    @Import(value="arg2")
    public int getArg2();

    @Import(value="arg2")
    public void setArg2(int var1);

    @Import(value="identifier")
    public int getIdentifier();

    @Import(value="identifier")
    public void setIdentifier(int var1);

    @Import(value="object")
    public int getObject();

    @Import(value="object")
    public void setObject(int var1);

    @Import(value="op")
    public String getOp();

    @Import(value="op")
    public void setOp(String var1);

    @Import(value="target")
    public String getTarget();

    @Import(value="target")
    public void setTarget(String var1);
}

