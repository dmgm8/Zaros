/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.ScriptEvent
 *  net.runelite.api.widgets.Widget
 */
package rs.api;

import net.runelite.api.ScriptEvent;
import net.runelite.api.widgets.Widget;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSNode;

public interface RSScriptEvent
extends ScriptEvent,
RSNode {
    @Import(value="params")
    public Object[] getArguments();

    @Import(value="params")
    public void setArguments(Object[] var1);

    @Import(value="source")
    public Widget getSource();

    @Import(value="source")
    public void setRSSource(Widget var1);

    @Import(value="op")
    public int getOp();

    @Import(value="opbase")
    public String getOpbase();

    @Import(value="mouseX")
    public int getMouseX();

    @Import(value="mouseY")
    public int getMouseY();

    @Import(value="keycode")
    public int getTypedKeyCode();

    @Import(value="keychar")
    public int getTypedKeyChar();
}

