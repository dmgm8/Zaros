/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.runelite.api.Item
 */
package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import javax.annotation.Nullable;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Item;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.devtools.InventoryItem;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

class InventoryDeltaPanel
extends JPanel
implements Scrollable {
    private static final DecimalFormat COMMA_FORMAT = new DecimalFormat("#,###");
    private static final Dimension ITEM_SIZE = new Dimension(40, 32);
    private final ItemManager itemManager;
    private final JPanel addedGrid = new JPanel();
    private final JPanel removedGrid = new JPanel();
    private final JPanel currentGrid = new JPanel();

    InventoryDeltaPanel(ItemManager itemManager) {
        this.itemManager = itemManager;
        this.setLayout(new BoxLayout(this, 3));
        EmptyBorder border = new EmptyBorder(2, 2, 2, 2);
        this.setBorder(border);
        this.addedGrid.setBorder(border);
        this.removedGrid.setBorder(border);
        this.currentGrid.setBorder(border);
        GridLayout layout = new GridLayout(0, 1, 1, 1);
        this.addedGrid.setLayout(layout);
        this.removedGrid.setLayout(layout);
        this.currentGrid.setLayout(layout);
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent componentEvent) {
                int cols = Math.max((InventoryDeltaPanel.this.getWidth() - 4) / (ITEM_SIZE.width + 1), 1);
                GridLayout layout = new GridLayout(0, cols, 1, 1);
                InventoryDeltaPanel.this.addedGrid.setLayout(layout);
                InventoryDeltaPanel.this.removedGrid.setLayout(layout);
                InventoryDeltaPanel.this.currentGrid.setLayout(layout);
            }
        });
    }

    void clear() {
        this.addedGrid.removeAll();
        this.removedGrid.removeAll();
        this.currentGrid.removeAll();
        this.removeAll();
        this.revalidate();
        this.repaint();
    }

    void displayItems(InventoryItem[] items, @Nullable InventoryItem[] added, @Nullable InventoryItem[] removed) {
        JLabel label;
        this.clear();
        if (added != null && added.length > 0) {
            label = new JLabel("Items Added:", 0);
            label.setAlignmentX(0.5f);
            this.add(label);
            this.add(this.addedGrid);
            for (InventoryItem item : added) {
                this.addItemToPanel(item, this.addedGrid).setBackground(new Color(0, 100, 0));
            }
        }
        if (removed != null && removed.length > 0) {
            label = new JLabel("Items Removed:", 0);
            label.setAlignmentX(0.5f);
            this.add(label);
            this.add(this.removedGrid);
            for (InventoryItem item : removed) {
                this.addItemToPanel(item, this.removedGrid).setBackground(new Color(120, 0, 0));
            }
        }
        label = new JLabel("Items in Inventory:", 0);
        label.setAlignmentX(0.5f);
        this.add(label);
        this.add(this.currentGrid);
        for (InventoryItem item : items) {
            JLabel gridItem = this.addItemToPanel(item, this.currentGrid);
            gridItem.addMouseListener(new MouseAdapter(){

                @Override
                public void mouseEntered(MouseEvent e) {
                    JLabel label = (JLabel)e.getSource();
                    label.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    JLabel label = (JLabel)e.getSource();
                    label.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                }
            });
            gridItem.setToolTipText("<html>Name: " + item.getName() + "<br/>Item ID: " + item.getItem().getId() + "<br/>Quantity: " + COMMA_FORMAT.format(item.getItem().getQuantity()) + "<br/>Slot: " + item.getSlot() + "</html>");
        }
        this.revalidate();
        this.repaint();
    }

    private JLabel addItemToPanel(InventoryItem inventoryItem, JPanel panel) {
        JLabel gridItem = new JLabel();
        gridItem.setOpaque(true);
        gridItem.setPreferredSize(ITEM_SIZE);
        gridItem.setVerticalAlignment(0);
        gridItem.setHorizontalAlignment(0);
        gridItem.setFont(FontManager.getRunescapeSmallFont());
        Item item = inventoryItem.getItem();
        if (item.getId() == -1) {
            gridItem.setText("EMPTY");
        } else {
            this.itemManager.getImage(item.getId(), item.getQuantity(), item.getQuantity() > 1).addTo(gridItem);
            gridItem.setToolTipText("<html>Name: " + inventoryItem.getName() + "<br/>Item ID: " + item.getId() + "<br/>Quantity: " + COMMA_FORMAT.format(item.getQuantity()) + "</html>");
        }
        panel.add(gridItem);
        return gridItem;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return null;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1 + (orientation == 1 ? InventoryDeltaPanel.ITEM_SIZE.height : InventoryDeltaPanel.ITEM_SIZE.width);
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 1 + (orientation == 1 ? InventoryDeltaPanel.ITEM_SIZE.height : InventoryDeltaPanel.ITEM_SIZE.width);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}

