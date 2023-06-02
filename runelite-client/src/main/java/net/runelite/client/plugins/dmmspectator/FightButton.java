/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.dmmspectator;

import javax.swing.JButton;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;

public class FightButton
extends JButton {
    private final String player1;
    private final String player2;

    public FightButton(String player1, String player2) {
        if (player1 != null && player2 != null) {
            this.setText(player1 + " vs. " + player2);
        } else if (player1 != null) {
            this.setText(player1);
        } else if (player2 != null) {
            this.setText(player2);
        }
        this.player1 = player1;
        this.player2 = player2;
    }

    public void addAction(Client client, ClientThread clientThread) {
        String player;
        String string = player = this.player1 != null ? this.player1 : this.player2;
        if (player == null) {
            return;
        }
        this.addActionListener(l -> clientThread.invokeLater(() -> client.runScript(new Object[]{49, "specteleto " + player})));
    }
}

