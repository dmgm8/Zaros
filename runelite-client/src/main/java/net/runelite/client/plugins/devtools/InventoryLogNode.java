/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.devtools;

import javax.swing.tree.DefaultMutableTreeNode;
import net.runelite.client.plugins.devtools.InventoryLog;

class InventoryLogNode
extends DefaultMutableTreeNode {
    private final InventoryLog log;

    InventoryLogNode(InventoryLog log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "Tick: " + this.log.getTick();
    }

    public InventoryLog getLog() {
        return this.log;
    }
}

