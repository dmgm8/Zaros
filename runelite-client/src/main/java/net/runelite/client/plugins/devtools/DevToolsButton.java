/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.devtools;

import java.awt.Color;
import javax.swing.JButton;
import net.runelite.client.plugins.devtools.DevToolsFrame;

public class DevToolsButton
extends JButton {
    private boolean active;

    DevToolsButton(String title) {
        super(title);
        this.addActionListener(ev -> this.setActive(!this.active));
        this.setToolTipText(title);
    }

    void setActive(boolean active) {
        this.active = active;
        if (active) {
            this.setBackground(Color.GREEN);
        } else {
            this.setBackground(null);
        }
    }

    void addFrame(DevToolsFrame frame) {
        frame.setDevToolsButton(this);
        this.addActionListener(ev -> {
            if (this.isActive()) {
                frame.close();
            } else {
                frame.open();
            }
        });
    }

    public boolean isActive() {
        return this.active;
    }
}

