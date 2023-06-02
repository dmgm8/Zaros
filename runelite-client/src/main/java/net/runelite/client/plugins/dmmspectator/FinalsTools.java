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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import net.runelite.client.plugins.dmmspectator.FightButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class FinalsTools
extends DmmSpectatorFrame {
    private static final Logger log = LoggerFactory.getLogger(FinalsTools.class);
    private final Client client;
    private final ClientThread clientThread;
    private JPanel panel;
    private JPanel playerDataPanel;
    private JTextField playerSearchField;
    private JTabbedPane tabs;
    private JPanel[] tabPanels = new JPanel[8];
    private String[][] playerNames = new String[8][];
    private String searchString;

    @Inject
    public FinalsTools(Client client, ClientThread clientThread, EventBus eventBus) {
        this.client = client;
        this.clientThread = clientThread;
        eventBus.register(this);
        this.setTitle("Finals Tools");
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
        this.playerSearchField.setPreferredSize(new Dimension(550, 24));
        this.playerSearchField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                FinalsTools.this.searchString = FinalsTools.this.playerSearchField.getText();
                FinalsTools.this.updateSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                FinalsTools.this.searchString = FinalsTools.this.playerSearchField.getText();
                FinalsTools.this.updateSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                FinalsTools.this.searchString = FinalsTools.this.playerSearchField.getText();
                FinalsTools.this.updateSearch();
            }
        });
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new FlowLayout());
        textFieldPanel.setPreferredSize(new Dimension(550, 32));
        textFieldPanel.add(this.playerSearchField);
        this.panel.setLayout(new BoxLayout(this.panel, 1));
        this.playerDataPanel.setLayout(new GridLayout(0, 2, 3, 3));
        JScrollPane playerDataScrollPane = new JScrollPane(this.playerDataPanel);
        playerDataScrollPane.setPreferredSize(new Dimension(550, 346));
        this.tabs = new JTabbedPane();
        for (int i = 0; i < 8; ++i) {
            JPanel tab = this.tabPanels[i] = new JPanel();
            tab.setLayout(new GridLayout(0, 2, 3, 3));
            JScrollPane scrollPane = new JScrollPane(tab);
            scrollPane.setPreferredSize(new Dimension(550, 300));
            this.tabs.addTab("Round " + (i + 1), scrollPane);
        }
        JLabel label = new JLabel("Search:");
        label.setAlignmentX(0.5f);
        this.panel.add(label);
        this.panel.add(textFieldPanel);
        this.panel.add(this.tabs);
        this.panel.add(playerDataScrollPane);
        return this.panel;
    }

    private void updateSearch() {
        if (this.searchString == null || this.searchString.isEmpty()) {
            this.tabs.setVisible(true);
            this.playerDataPanel.setVisible(false);
            this.refresh();
            return;
        }
        this.tabs.setVisible(false);
        this.playerDataPanel.setVisible(true);
        this.playerDataPanel.removeAll();
        for (int round = 0; round < this.playerNames.length; ++round) {
            String[] names = this.playerNames[round];
            if (names == null) continue;
            for (int i = 0; i < names.length; i += 2) {
                FightButton fightButton;
                String player1 = names[i];
                String player2 = null;
                if (i < names.length - 1) {
                    player2 = names[i + 1];
                }
                if (player1 != null && player1.isEmpty()) {
                    player1 = null;
                }
                if (player2 != null && player2.isEmpty()) {
                    player2 = null;
                }
                if (!(fightButton = new FightButton(player1, player2)).getText().toLowerCase().contains(this.searchString.toLowerCase())) continue;
                this.playerDataPanel.add(fightButton);
                fightButton.addAction(this.client, this.clientThread);
            }
        }
        this.pack();
    }

    @Override
    public void show() {
        super.show();
        this.refresh();
    }

    private void refresh() {
        this.clientThread.invokeLater(() -> {
            for (int round = 0; round < this.tabPanels.length; ++round) {
                String[] names = this.playerNames[round];
                JPanel panel = this.tabPanels[round];
                if (names == null) {
                    panel.removeAll();
                    continue;
                }
                SwingUtilities.invokeLater(() -> {
                    panel.removeAll();
                    for (int i = 0; i < names.length; i += 2) {
                        String player1 = names[i];
                        String player2 = null;
                        if (i < names.length - 1) {
                            player2 = names[i + 1];
                        }
                        if (player1 != null && player1.isEmpty()) {
                            player1 = null;
                        }
                        if (player2 != null && player2.isEmpty()) {
                            player2 = null;
                        }
                        if (player1 == null && player2 == null) continue;
                        FightButton fightButton = new FightButton(player1, player2);
                        fightButton.addAction(this.client, this.clientThread);
                        panel.add(fightButton);
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
            case "deadman_spectator_transmit_fights": {
                int round = intStack[intStackSize - 1];
                String names = stringStack[stringStackSize - 1];
                System.out.println("Received deadman_spectator_transmit_fights(" + round + ", " + names + ")");
                this.playerNames[round] = names.split("\\|");
                this.refresh();
                break;
            }
        }
    }
}

