/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Inject
 *  com.google.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.events.ScriptCallbackEvent
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.dmmspectator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.dmmspectator.DmmSpectatorFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HotspotTools
extends DmmSpectatorFrame {
    private static final Logger log = LoggerFactory.getLogger(HotspotTools.class);
    private final Client client;
    private final ClientThread clientThread;
    private JScrollPane scrollPane;
    private JPanel panel;
    private JPanel hotspotDataPanel;
    private String[] hotspots;

    @Inject
    public HotspotTools(Client client, ClientThread clientThread, EventBus eventBus) {
        this.client = client;
        this.clientThread = clientThread;
        eventBus.register(this);
        this.setTitle("Hotspots");
        this.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(300, 400));
        mainPanel.add(this.createScrollPane());
        this.add(mainPanel);
        this.refresh();
        this.pack();
    }

    private JPanel createScrollPane() {
        this.panel = new JPanel();
        this.hotspotDataPanel = new JPanel();
        this.panel.setLayout(new BoxLayout(this.panel, 1));
        this.hotspotDataPanel.setLayout(new GridLayout(0, 2, 3, 3));
        JLabel label = new JLabel("Hotspots");
        label.setAlignmentX(0.5f);
        this.scrollPane = new JScrollPane(this.hotspotDataPanel);
        this.scrollPane.setPreferredSize(new Dimension(300, 346));
        this.panel.add(label);
        this.panel.add(this.scrollPane);
        return this.panel;
    }

    @Override
    public void show() {
        super.show();
        this.refresh();
    }

    private void refresh() {
        this.clientThread.invokeLater(() -> {
            if (this.hotspots != null) {
                SwingUtilities.invokeLater(() -> {
                    this.hotspotDataPanel.removeAll();
                    for (int i = 0; i < this.hotspots.length; i += 2) {
                        String hotspot = this.hotspots[i];
                        String location = this.hotspots[i + 1];
                        JButton playerButton = new JButton(hotspot);
                        playerButton.addActionListener(l -> this.clientThread.invokeLater(() -> this.client.runScript(new Object[]{49, "spectele " + location})));
                        this.hotspotDataPanel.add(playerButton);
                    }
                    this.pack();
                });
            }
        });
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent event) {
        String eventName = event.getEventName();
        int[] intStack = this.client.getIntStack();
        String[] stringStack = this.client.getStringStack();
        int intStackSize = this.client.getIntStackSize();
        int stringStackSize = this.client.getStringStackSize();
        switch (eventName) {
            case "deadman_spectator_transmit_hotspots": {
                String hotspots = stringStack[stringStackSize - 1];
                System.out.println("Received deadman_spectator_transmit_hotspots(" + hotspots + ")");
                this.hotspots = hotspots.split("\\|");
                this.refresh();
                break;
            }
        }
    }
}

