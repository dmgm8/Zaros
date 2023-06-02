/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Inject
 *  com.google.inject.Singleton
 */
package net.runelite.client.plugins.modelcolors;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.FlatTextField;

@Singleton
public class ModelColorInputArea
extends JPanel {
    private final JTextField uiFieldItemId;
    private final JTextField uiFieldRecol1s;
    private final JTextField uiFieldRecol1d;

    @Inject
    ModelColorInputArea() {
        this.setLayout(new GridLayout(3, 1, 7, 7));
        this.uiFieldItemId = this.addComponent("Item ID");
        this.uiFieldRecol1s = this.addComponent("From Color");
        this.uiFieldRecol1d = this.addComponent("To Color");
    }

    public int getItemId() {
        return this.getInput(this.uiFieldItemId);
    }

    public int getRecolSource() {
        return this.getInput(this.uiFieldRecol1s);
    }

    public int getRecolDestination() {
        return this.getInput(this.uiFieldRecol1d);
    }

    private int getInput(JTextField field) {
        try {
            return Integer.parseInt(field.getText().replaceAll("\\D", ""));
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setInput(JTextField field, Object value) {
        field.setText(String.valueOf(value));
    }

    private JTextField addComponent(String label) {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JLabel uiLabel = new JLabel(label);
        FlatTextField uiInput = new FlatTextField();
        uiInput.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        uiInput.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
        uiInput.setBorder(new EmptyBorder(5, 7, 5, 7));
        uiLabel.setFont(FontManager.getRunescapeSmallFont());
        uiLabel.setBorder(new EmptyBorder(0, 0, 4, 0));
        uiLabel.setForeground(Color.WHITE);
        container.add((Component)uiLabel, "North");
        container.add((Component)uiInput, "Center");
        this.add(container);
        return uiInput.getTextField();
    }

    public JTextField getUiFieldItemId() {
        return this.uiFieldItemId;
    }

    public JTextField getUiFieldRecol1s() {
        return this.uiFieldRecol1s;
    }

    public JTextField getUiFieldRecol1d() {
        return this.uiFieldRecol1d;
    }
}

