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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.api.Client;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.dmmspectator.DmmSpectatorFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PlayerTools
extends DmmSpectatorFrame {
    private static final Logger log = LoggerFactory.getLogger(PlayerTools.class);
    private final Client client;
    private final ClientThread clientThread;
    private JScrollPane scrollPane;
    private JPanel panel;
    private JPanel playerDataPanel;
    private JTextField playerSearchField;
    private String[] playerNames;
    private String searchString;

    @Inject
    public PlayerTools(Client client, ClientThread clientThread, EventBus eventBus) {
        this.client = client;
        this.clientThread = clientThread;
        eventBus.register(this);
        this.setTitle("Player Tools");
        this.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(600, 400));
        mainPanel.add(this.createScrollPane());
        this.add(mainPanel);
        this.refresh();
        this.pack();
    }

    private JPanel createScrollPane() {
        this.panel = new JPanel();
        this.playerDataPanel = new JPanel();
        this.playerSearchField = new JTextField();
        this.playerSearchField.setPreferredSize(new Dimension(500, 24));
        this.playerSearchField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                PlayerTools.this.searchString = PlayerTools.this.playerSearchField.getText();
                PlayerTools.this.refresh();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                PlayerTools.this.searchString = PlayerTools.this.playerSearchField.getText();
                PlayerTools.this.refresh();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                PlayerTools.this.searchString = PlayerTools.this.playerSearchField.getText();
                PlayerTools.this.refresh();
            }
        });
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new FlowLayout());
        textFieldPanel.setPreferredSize(new Dimension(500, 32));
        textFieldPanel.add(this.playerSearchField);
        this.panel.setLayout(new BoxLayout(this.panel, 1));
        this.playerDataPanel.setLayout(new GridLayout(0, 2, 3, 3));
        JLabel label = new JLabel("Search:");
        label.setAlignmentX(0.5f);
        this.scrollPane = new JScrollPane(this.playerDataPanel);
        this.scrollPane.setPreferredSize(new Dimension(500, 346));
        this.panel.add(label);
        this.panel.add(textFieldPanel);
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
            if (this.playerNames != null) {
                SwingUtilities.invokeLater(() -> {
                    this.playerDataPanel.removeAll();
                    for (String player : this.playerNames) {
                        if (this.searchString != null && !this.searchString.isEmpty() && !player.toLowerCase().contains(this.searchString.toLowerCase())) continue;
                        JButton playerButton = new JButton(player);
                        playerButton.addActionListener(l -> this.clientThread.invokeLater(() -> this.client.runScript(new Object[]{49, "specteleto " + player})));
                        this.playerDataPanel.add(playerButton);
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
            case "deadman_spectator_transmit_names": {
                String names = stringStack[stringStackSize - 1];
                System.out.println("Received deadman_spectator_transmit_names(" + names + ")");
                this.playerNames = names.split("\\|");
                this.refresh();
                break;
            }
        }
    }
}

