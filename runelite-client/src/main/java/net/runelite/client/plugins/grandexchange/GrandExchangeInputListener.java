/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuEntry
 */
package net.runelite.client.plugins.grandexchange;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.plugins.grandexchange.GrandExchangePlugin;
import net.runelite.client.util.Text;

public class GrandExchangeInputListener
extends MouseAdapter
implements KeyListener {
    private final Client client;
    private final GrandExchangePlugin plugin;

    @Inject
    private GrandExchangeInputListener(Client client, GrandExchangePlugin plugin) {
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public MouseEvent mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.isAltDown()) {
            MenuEntry[] menuEntries;
            for (MenuEntry menuEntry : menuEntries = this.client.getMenuEntries()) {
                if (!menuEntry.getOption().equals("Search Grand Exchange")) continue;
                this.search(Text.removeTags(menuEntry.getTarget()));
                e.consume();
                break;
            }
        }
        return super.mouseClicked(e);
    }

    private void search(String itemName) {
        SwingUtilities.invokeLater(() -> {
            this.plugin.getPanel().showSearch();
            if (!this.plugin.getButton().isSelected()) {
                this.plugin.getButton().getOnSelect().run();
            }
            this.plugin.getPanel().getSearchPanel().priceLookup(itemName);
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isAltDown()) {
            this.plugin.setHotKeyPressed(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!e.isAltDown()) {
            this.plugin.setHotKeyPressed(false);
        }
    }
}

