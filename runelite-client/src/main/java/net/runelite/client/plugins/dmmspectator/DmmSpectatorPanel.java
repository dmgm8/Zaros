/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.dmmspectator;

import java.awt.GridLayout;
import javax.inject.Inject;
import javax.swing.JPanel;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.dmmspectator.DmmSpectatorPlugin;
import net.runelite.client.plugins.dmmspectator.FinalsTools;
import net.runelite.client.plugins.dmmspectator.HotspotTools;
import net.runelite.client.plugins.dmmspectator.PlayerTools;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DmmSpectatorPanel
extends PluginPanel {
    private static final Logger log = LoggerFactory.getLogger(DmmSpectatorPanel.class);
    private final Client client;
    private final ClientThread clientThread;
    private final DmmSpectatorPlugin plugin;
    private final PlayerTools playerTools;
    private final FinalsTools finalsTools;
    private final HotspotTools hotspotTools;

    @Inject
    public DmmSpectatorPanel(Client client, ClientThread clientThread, DmmSpectatorPlugin plugin, PlayerTools permadeathTools, FinalsTools finalsTools, HotspotTools hotspotTools) {
        this.client = client;
        this.clientThread = clientThread;
        this.plugin = plugin;
        this.playerTools = permadeathTools;
        this.finalsTools = finalsTools;
        this.hotspotTools = hotspotTools;
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.add(this.createPanel());
    }

    private JPanel createPanel() {
        JPanel container = new JPanel();
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);
        container.setLayout(new GridLayout(0, 2, 3, 3));
        container.add(this.plugin.getPermadeathButton());
        this.plugin.getPermadeathButton().addFrame(this.playerTools);
        container.add(this.plugin.getHotspotsButton());
        this.plugin.getHotspotsButton().addFrame(this.hotspotTools);
        container.add(this.plugin.getFinalsButton());
        this.plugin.getFinalsButton().addFrame(this.finalsTools);
        container.add(this.plugin.getPlayerNamesButton());
        this.plugin.getPlayerNamesButton().addActionListener(ev -> {
            if (this.plugin.getPlayerNamesButton().isActive()) {
                this.clientThread.invokeLater(() -> this.client.runScript(new Object[]{49, "specnames false"}));
            } else {
                this.clientThread.invokeLater(() -> this.client.runScript(new Object[]{49, "specnames true"}));
            }
        });
        container.add(this.plugin.getPublicChatButton());
        this.plugin.getPublicChatButton().addActionListener(ev -> {
            if (this.plugin.getPublicChatButton().isActive()) {
                this.clientThread.invokeLater(() -> this.client.runScript(new Object[]{49, "specchat false"}));
            } else {
                this.clientThread.invokeLater(() -> this.client.runScript(new Object[]{49, "specchat true"}));
            }
        });
        return container;
    }
}

