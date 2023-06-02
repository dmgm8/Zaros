/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.hooks;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import net.runelite.api.MainBufferProvider;
import net.runelite.api.Renderable;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;

public interface Callbacks {
    public void post(Object var1);

    public void postDeferred(Object var1);

    public void tick();

    public void frame();

    public void updateNpcs();

    public void drawScene();

    public void drawAboveOverheads();

    public void draw(MainBufferProvider var1, Graphics var2, int var3, int var4);

    public void drawInterface(int var1, List<WidgetItem> var2);

    public void drawLayer(Widget var1, List<WidgetItem> var2);

    public MouseEvent mousePressed(MouseEvent var1);

    public MouseEvent mouseReleased(MouseEvent var1);

    public MouseEvent mouseClicked(MouseEvent var1);

    public MouseEvent mouseEntered(MouseEvent var1);

    public MouseEvent mouseExited(MouseEvent var1);

    public MouseEvent mouseDragged(MouseEvent var1);

    public MouseEvent mouseMoved(MouseEvent var1);

    public MouseWheelEvent mouseWheelMoved(MouseWheelEvent var1);

    public void keyPressed(KeyEvent var1);

    public void keyReleased(KeyEvent var1);

    public void keyTyped(KeyEvent var1);

    public boolean draw(Renderable var1, boolean var2);

    public void error(String var1, Throwable var2);
}

