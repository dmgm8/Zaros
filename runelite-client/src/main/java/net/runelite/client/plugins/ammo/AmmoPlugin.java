/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.EquipmentInventorySlot
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.events.ItemContainerChanged
 */
package net.runelite.client.plugins.ammo;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.ammo.AmmoCounter;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.AsyncBufferedImage;

@PluginDescriptor(name="Ammo", description="Shows the current ammo the player has equipped", tags={"bolts", "darts", "chinchompa", "equipment"})
public class AmmoPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private ItemManager itemManager;
    private AmmoCounter counterBox;

    @Override
    protected void startUp() throws Exception {
        this.clientThread.invokeLater(() -> {
            ItemContainer container = this.client.getItemContainer(InventoryID.EQUIPMENT);
            if (container != null) {
                this.checkInventory(container.getItems());
            }
        });
    }

    @Override
    protected void shutDown() throws Exception {
        this.infoBoxManager.removeInfoBox(this.counterBox);
        this.counterBox = null;
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getItemContainer() != this.client.getItemContainer(InventoryID.EQUIPMENT)) {
            return;
        }
        this.checkInventory(event.getItemContainer().getItems());
    }

    private void checkInventory(Item[] items) {
        Item weapon;
        ItemComposition weaponComp;
        if (items.length > EquipmentInventorySlot.WEAPON.getSlotIdx() && (weaponComp = this.itemManager.getItemComposition((weapon = items[EquipmentInventorySlot.WEAPON.getSlotIdx()]).getId())).isStackable()) {
            this.updateInfobox(weapon, weaponComp);
            return;
        }
        if (items.length <= EquipmentInventorySlot.AMMO.getSlotIdx()) {
            this.removeInfobox();
            return;
        }
        Item ammo = items[EquipmentInventorySlot.AMMO.getSlotIdx()];
        ItemComposition comp = this.itemManager.getItemComposition(ammo.getId());
        if (!comp.isStackable()) {
            this.removeInfobox();
            return;
        }
        this.updateInfobox(ammo, comp);
    }

    private void updateInfobox(Item item, ItemComposition comp) {
        if (this.counterBox != null && this.counterBox.getItemID() == item.getId()) {
            this.counterBox.setCount(item.getQuantity());
            return;
        }
        this.removeInfobox();
        AsyncBufferedImage image = this.itemManager.getImage(item.getId(), 5, false);
        this.counterBox = new AmmoCounter(this, item.getId(), item.getQuantity(), comp.getName(), image);
        this.infoBoxManager.addInfoBox(this.counterBox);
    }

    private void removeInfobox() {
        this.infoBoxManager.removeInfoBox(this.counterBox);
        this.counterBox = null;
    }
}

