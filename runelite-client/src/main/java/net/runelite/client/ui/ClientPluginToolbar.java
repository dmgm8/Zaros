/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 */
package net.runelite.client.ui;

import com.google.common.collect.ComparisonChain;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.Box;
import javax.swing.JToolBar;
import net.runelite.client.ui.NavigationButton;

public class ClientPluginToolbar
extends JToolBar {
    private static final int TOOLBAR_WIDTH = 36;
    private static final int TOOLBAR_HEIGHT = 503;
    private final Map<NavigationButton, Component> componentMap = new TreeMap<NavigationButton, Component>((a, b) -> ComparisonChain.start().compareTrueFirst(a.isTab(), b.isTab()).compare(a.getPriority(), b.getPriority()).compare((Comparable)((Object)a.getTooltip()), (Comparable)((Object)b.getTooltip())).result());

    ClientPluginToolbar() {
        super(1);
        this.setFloatable(false);
        this.setSize(new Dimension(36, 503));
        this.setMinimumSize(new Dimension(36, 503));
        this.setPreferredSize(new Dimension(36, 503));
        this.setMaximumSize(new Dimension(36, Integer.MAX_VALUE));
    }

    void addComponent(NavigationButton button, Component c) {
        if (this.componentMap.put(button, c) == null) {
            this.update();
        }
    }

    void removeComponent(NavigationButton button) {
        if (this.componentMap.remove(button) != null) {
            this.update();
        }
    }

    private void update() {
        this.removeAll();
        boolean isDelimited = false;
        for (Map.Entry<NavigationButton, Component> entry : this.componentMap.entrySet()) {
            if (!entry.getKey().isTab() && !isDelimited) {
                isDelimited = true;
                this.add(Box.createVerticalGlue());
                this.addSeparator();
            }
            this.add(entry.getValue());
        }
        this.repaint();
    }
}

