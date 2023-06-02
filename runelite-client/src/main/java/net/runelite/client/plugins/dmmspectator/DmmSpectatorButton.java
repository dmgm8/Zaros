/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.dmmspectator;

import java.awt.Color;
import javax.swing.JButton;
import net.runelite.client.plugins.dmmspectator.DmmSpectatorFrame;

public class DmmSpectatorButton
extends JButton {
    private boolean active;

    DmmSpectatorButton(String title) {
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

    void addFrame(DmmSpectatorFrame frame) {
        if (frame == null) {
            return;
        }
        frame.setDmmSpectatorButton(this);
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

