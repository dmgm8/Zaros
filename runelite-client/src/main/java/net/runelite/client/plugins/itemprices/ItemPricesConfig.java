/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.itemprices;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="itemprices")
public interface ItemPricesConfig
extends Config {
    @ConfigItem(keyName="showGEPrice", name="Show Grand Exchange Prices", description="Grand exchange prices should be shown on tooltips", position=1)
    default public boolean showGEPrice() {
        return true;
    }

    @ConfigItem(keyName="showHAValue", name="Show High Alchemy Values", description="High Alchemy values should be shown on tooltips", position=2)
    default public boolean showHAValue() {
        return true;
    }

    @ConfigItem(keyName="showEA", name="Show Price Each on Stacks", description="The price/value of each item should be shown on stacks", position=3)
    default public boolean showEA() {
        return true;
    }

    @ConfigItem(keyName="hideInventory", name="Hide Tooltips on Inventory Items", description="Tooltips should be hidden on items in the inventory", position=4)
    default public boolean hideInventory() {
        return true;
    }

    @ConfigItem(keyName="showAlchProfit", name="Show High Alchemy Profit", description="Show the profit from casting high alchemy on items", position=5)
    default public boolean showAlchProfit() {
        return false;
    }

    @ConfigItem(keyName="showWhileAlching", name="Show prices while alching", description="Show the price overlay while using High Alchemy. Takes priority over \"Hide tooltips on Inventory Items\"", position=6)
    default public boolean showWhileAlching() {
        return true;
    }
}

