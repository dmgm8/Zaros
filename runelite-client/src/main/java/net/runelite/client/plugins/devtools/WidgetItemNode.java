/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.widgets.WidgetItem
 */
package net.runelite.client.plugins.devtools;

import javax.swing.tree.DefaultMutableTreeNode;
import net.runelite.api.widgets.WidgetItem;

class WidgetItemNode
extends DefaultMutableTreeNode {
    private final WidgetItem widgetItem;

    public WidgetItemNode(WidgetItem widgetItem) {
        super((Object)widgetItem);
        this.widgetItem = widgetItem;
    }

    public WidgetItem getWidgetItem() {
        return this.widgetItem;
    }

    @Override
    public String toString() {
        return "I " + this.widgetItem.getIndex();
    }
}

