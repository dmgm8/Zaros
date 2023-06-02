/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 */
package net.runelite.client.ui;

import com.google.common.collect.ComparisonChain;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JPanel;
import net.runelite.client.ui.NavigationButton;

class ClientTitleToolbar
extends JPanel {
    private static final int TITLEBAR_SIZE = 23;
    private static final int ITEM_PADDING = 4;
    private final Map<NavigationButton, Component> componentMap = new TreeMap<NavigationButton, Component>((a, b) -> ComparisonChain.start().compare(a.getPriority(), b.getPriority()).compare((Comparable)((Object)a.getTooltip()), (Comparable)((Object)b.getTooltip())).result());

    ClientTitleToolbar() {
        this.setLayout(new LayoutManager2(){

            @Override
            public void addLayoutComponent(String name, Component comp) {
            }

            @Override
            public void addLayoutComponent(Component comp, Object constraints) {
            }

            @Override
            public void removeLayoutComponent(Component comp) {
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                int width = parent.getComponentCount() * 27;
                return new Dimension(width, 23);
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return this.preferredLayoutSize(parent);
            }

            @Override
            public Dimension maximumLayoutSize(Container parent) {
                return this.preferredLayoutSize(parent);
            }

            @Override
            public float getLayoutAlignmentX(Container target) {
                return 0.0f;
            }

            @Override
            public float getLayoutAlignmentY(Container target) {
                return 0.0f;
            }

            @Override
            public void invalidateLayout(Container target) {
            }

            @Override
            public void layoutContainer(Container parent) {
                int x = 0;
                for (Component c : parent.getComponents()) {
                    x += 4;
                    int height = c.getPreferredSize().height;
                    if (height > 23) {
                        height = 23;
                    }
                    c.setBounds(x, (23 - height) / 2, 23, height);
                    x += 23;
                }
            }
        });
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
        this.componentMap.values().forEach(this::add);
        this.repaint();
    }
}

