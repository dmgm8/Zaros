/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 *  net.runelite.api.EquipmentInventorySlot
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemContainer
 */
package net.runelite.client.plugins.itemstats.potions;

import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.client.plugins.itemstats.StatBoost;
import net.runelite.client.plugins.itemstats.stats.Stats;

public class PrayerPotion
extends StatBoost {
    private static final double BASE_PERC = 0.25;
    private final int delta;
    private final double perc;
    private static final int RING_SLOT = EquipmentInventorySlot.RING.getSlotIdx();
    private static final int CAPE_SLOT = EquipmentInventorySlot.CAPE.getSlotIdx();

    public PrayerPotion(int delta) {
        this(delta, 0.25);
    }

    PrayerPotion(int delta, double perc) {
        super(Stats.PRAYER, false);
        this.delta = delta;
        this.perc = perc;
    }

    @Override
    public int heals(Client client) {
        ItemContainer invContainer;
        boolean hasHolyWrench = false;
        ItemContainer equipContainer = client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipContainer != null) {
            Item cape = equipContainer.getItem(CAPE_SLOT);
            Item ring = equipContainer.getItem(RING_SLOT);
            boolean bl = hasHolyWrench = ring != null && ring.getId() == 13202;
            if (cape != null) {
                int capeId = cape.getId();
                hasHolyWrench |= capeId == 9759;
                hasHolyWrench |= capeId == 9760;
                hasHolyWrench |= capeId == 13280;
                hasHolyWrench |= capeId == 13342;
            }
        }
        if (!hasHolyWrench && (invContainer = client.getItemContainer(InventoryID.INVENTORY)) != null) {
            for (Item itemStack : invContainer.getItems()) {
                int item = itemStack.getId();
                hasHolyWrench = item == 6714;
                hasHolyWrench |= item == 9759;
                hasHolyWrench |= item == 9760;
                hasHolyWrench |= item == 13280;
                if (hasHolyWrench |= item == 13342) break;
            }
        }
        double percent = hasHolyWrench ? this.perc + 0.02 : this.perc;
        int max = this.getStat().getMaximum(client);
        return (int)((double)max * percent) * (this.delta >= 0 ? 1 : -1) + this.delta;
    }
}

