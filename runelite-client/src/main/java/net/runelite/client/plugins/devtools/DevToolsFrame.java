/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.devtools;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import net.runelite.client.plugins.devtools.DevToolsButton;
import net.runelite.client.ui.ClientUI;

public class DevToolsFrame
extends JFrame {
    protected DevToolsButton devToolsButton;

    public DevToolsFrame() {
        this.setIconImage(ClientUI.ICON);
        this.setDefaultCloseOperation(0);
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                DevToolsFrame.this.close();
                DevToolsFrame.this.devToolsButton.setActive(false);
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

    void setDevToolsButton(DevToolsButton devToolsButton) {
        this.devToolsButton = devToolsButton;
    }
}

