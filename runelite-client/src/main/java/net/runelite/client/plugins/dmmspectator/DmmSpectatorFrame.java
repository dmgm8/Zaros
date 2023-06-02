/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.dmmspectator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import net.runelite.client.plugins.dmmspectator.DmmSpectatorButton;
import net.runelite.client.ui.ClientUI;

public class DmmSpectatorFrame
extends JFrame {
    protected DmmSpectatorButton dmmSpectatorButton;

    public DmmSpectatorFrame() {
        this.setIconImage(ClientUI.ICON);
        this.setDefaultCloseOperation(0);
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                DmmSpectatorFrame.this.close();
                DmmSpectatorFrame.this.dmmSpectatorButton.setActive(false);
            }
        });
    }

    public void open() {
        this.setVisible(true);
        this.toFront();
        this.repaint();
    }

    public void close() {
        this.setVisible(false);
    }

    void setDmmSpectatorButton(DmmSpectatorButton dmmSpectatorButton) {
        this.dmmSpectatorButton = dmmSpectatorButton;
    }
}

