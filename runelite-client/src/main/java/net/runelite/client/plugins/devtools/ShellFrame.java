/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.jshell.ShellPanel
 */
package net.runelite.client.plugins.devtools;

import java.awt.Container;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.devtools.DevToolsFrame;
import net.runelite.jshell.ShellPanel;

@Singleton
class ShellFrame
extends DevToolsFrame {
    private final ShellPanel shellPanel;

    @Inject
    ShellFrame(final ClientThread clientThread, ScheduledExecutorService executor) {
        this.shellPanel = new ShellPanel(executor){

            protected void invokeOnClientThread(Runnable r) {
                clientThread.invoke(r);
            }
        };
        this.setContentPane((Container)this.shellPanel);
        this.setTitle("RuneLite Shell");
        this.pack();
    }

    @Override
    public void open() {
        this.shellPanel.switchContext(RuneLite.getInjector());
        super.open();
    }

    @Override
    public void close() {
        super.close();
        this.shellPanel.freeContext();
    }
}

