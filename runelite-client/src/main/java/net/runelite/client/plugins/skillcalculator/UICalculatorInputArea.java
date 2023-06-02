/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Inject
 *  com.google.inject.Singleton
 */
package net.runelite.client.plugins.skillcalculator;

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
class UICalculatorInputArea
extends JPanel {
    private final JTextField uiFieldCurrentLevel;
    private final JTextField uiFieldCurrentXP;
    private final JTextField uiFieldTargetLevel;
    private final JTextField uiFieldTargetXP;

    @Inject
    UICalculatorInputArea() {
        this.setLayout(new GridLayout(2, 2, 7, 7));
        this.uiFieldCurrentLevel = this.addComponent("Current Level");
        this.uiFieldCurrentXP = this.addComponent("Current Experience");
        this.uiFieldTargetLevel = this.addComponent("Target Level");
        this.uiFieldTargetXP = this.addComponent("Target Experience");
    }

    int getCurrentLevelInput() {
        return this.getInput(this.uiFieldCurrentLevel);
    }

    void setCurrentLevelInput(int value) {
        this.setInput(this.uiFieldCurrentLevel, value);
    }

    int getCurrentXPInput() {
        return this.getInput(this.uiFieldCurrentXP);
    }

    void setCurrentXPInput(Object value) {
        this.setInput(this.uiFieldCurrentXP, value);
    }

    int getTargetLevelInput() {
        return this.getInput(this.uiFieldTargetLevel);
    }

    void setTargetLevelInput(Object value) {
        this.setInput(this.uiFieldTargetLevel, value);
    }

    int getTargetXPInput() {
        return this.getInput(this.uiFieldTargetXP);
    }

    void setTargetXPInput(Object value) {
        this.setInput(this.uiFieldTargetXP, value);
    }

    void setNeededXP(Object value) {
        this.uiFieldTargetXP.setToolTipText((String)value);
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

    public JTextField getUiFieldCurrentLevel() {
        return this.uiFieldCurrentLevel;
    }

    public JTextField getUiFieldCurrentXP() {
        return this.uiFieldCurrentXP;
    }

    public JTextField getUiFieldTargetLevel() {
        return this.uiFieldTargetLevel;
    }

    public JTextField getUiFieldTargetXP() {
        return this.uiFieldTargetXP;
    }
}

