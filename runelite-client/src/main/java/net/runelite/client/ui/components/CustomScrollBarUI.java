/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import net.runelite.client.ui.ColorScheme;

public class CustomScrollBarUI
extends BasicScrollBarUI {
    private Color thumbColor = ColorScheme.MEDIUM_GRAY_COLOR;
    private Color trackColor = ColorScheme.SCROLL_TRACK_COLOR;

    @Override
    protected void paintTrack(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        graphics.setColor(this.trackColor);
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    protected void paintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        graphics.setColor(this.thumbColor);
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    protected JButton createEmptyButton() {
        JButton button = new JButton();
        Dimension zeroDim = new Dimension(0, 0);
        button.setPreferredSize(zeroDim);
        button.setMinimumSize(zeroDim);
        button.setMaximumSize(zeroDim);
        return button;
    }

    public static ComponentUI createUI(JComponent c) {
        JScrollBar bar = (JScrollBar)c;
        bar.setUnitIncrement(16);
        bar.setPreferredSize(new Dimension(7, 7));
        return new CustomScrollBarUI();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return this.createEmptyButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return this.createEmptyButton();
    }

    public void setThumbColor(Color thumbColor) {
        this.thumbColor = thumbColor;
    }

    public void setTrackColor(Color trackColor) {
        this.trackColor = trackColor;
    }
}

