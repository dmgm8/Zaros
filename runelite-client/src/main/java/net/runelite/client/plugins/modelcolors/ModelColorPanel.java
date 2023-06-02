/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Singleton
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.PlayerComposition
 *  net.runelite.api.kit.KitType
 */
package net.runelite.client.plugins.modelcolors;

import com.google.inject.Singleton;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.PlayerComposition;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.modelcolors.ModelColorInputArea;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

@Singleton
public class ModelColorPanel
extends PluginPanel {
    private final ModelColorInputArea uiInput;
    private final Client client;
    private final ItemManager itemManager;
    private int index = 0;
    private final Map<Integer, short[]> originalReplace = new HashMap<Integer, short[]>();
    private final Map<Integer, short[]> originalReplaceWith = new HashMap<Integer, short[]>();

    @Inject
    ModelColorPanel(Client client, ClientThread clientThread, ModelColorInputArea uiInput, ItemManager itemManager) {
        this.client = client;
        this.uiInput = uiInput;
        this.itemManager = itemManager;
        this.getScrollPane().setVerticalScrollBarPolicy(22);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 2;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        uiInput.setBorder(new EmptyBorder(15, 0, 15, 0));
        uiInput.setBackground(ColorScheme.DARK_GRAY_COLOR);
        uiInput.getUiFieldItemId().addActionListener(e -> this.onUpdate());
        uiInput.getUiFieldRecol1s().addActionListener(e -> this.onUpdate());
        uiInput.getUiFieldRecol1d().addActionListener(e -> this.onUpdate());
        this.add((Component)uiInput, c);
        ++c.gridy;
    }

    private void onUpdate() {
        int from = this.uiInput.getRecolSource();
        int to = this.uiInput.getRecolDestination();
        int itemId = this.uiInput.getItemId();
        if (itemId > 0) {
            ItemComposition composition = this.itemManager.getItemComposition(itemId);
            short[] toReplace = (short[])Objects.requireNonNull(this.originalReplace.computeIfAbsent(itemId, k -> composition.getColorToReplace())).clone();
            short[] toReplaceWith = (short[])Objects.requireNonNull(this.originalReplaceWith.computeIfAbsent(itemId, k -> composition.getColorToReplaceWith())).clone();
            boolean replaced = false;
            for (int i = 0; i < toReplace.length; ++i) {
                if (toReplace[i] != from) continue;
                toReplaceWith[i] = (short)to;
                replaced = true;
                break;
            }
            if (!replaced) {
                short[] newToReplace = new short[toReplace.length + 1];
                short[] newToReplaceWith = new short[toReplaceWith.length + 1];
                System.arraycopy(toReplace, 0, newToReplace, 0, toReplace.length);
                System.arraycopy(toReplaceWith, 0, newToReplaceWith, 0, toReplaceWith.length);
                toReplace = newToReplace;
                toReplaceWith = newToReplaceWith;
                toReplace[toReplace.length - 1] = (short)from;
                toReplaceWith[toReplaceWith.length - 1] = (short)to;
            }
            composition.setColorToReplace(toReplace);
            composition.setColorToReplaceWith(toReplaceWith);
            PlayerComposition pComp = this.client.getLocalPlayer().getPlayerComposition();
            int[] equipmentIds = pComp.getEquipmentIds();
            equipmentIds[KitType.AMULET.getIndex()] = ++this.index + 512;
            pComp.setHash();
        }
    }
}

