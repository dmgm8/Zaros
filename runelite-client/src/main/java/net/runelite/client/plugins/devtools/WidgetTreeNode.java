/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.widgets.Widget
 */
package net.runelite.client.plugins.devtools;

import javax.swing.tree.DefaultMutableTreeNode;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.devtools.WidgetInspector;

class WidgetTreeNode
extends DefaultMutableTreeNode {
    private final String type;

    public WidgetTreeNode(String type, Widget widget) {
        super((Object)widget);
        this.type = type;
    }

    public Widget getWidget() {
        return (Widget)this.getUserObject();
    }

    @Override
    public String toString() {
        return this.type + " " + WidgetInspector.getWidgetIdentifier(this.getWidget());
    }
}

