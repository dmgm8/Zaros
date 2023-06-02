/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.GrandExchangeOffer
 */
package rs.api;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.mapping.Import;

public interface RSGrandExchangeOffer
extends GrandExchangeOffer {
    @Import(value="quantitySold")
    public int getQuantitySold();

    @Import(value="itemId")
    public int getItemId();

    @Import(value="itemId")
    public void setItemId(int var1);

    @Import(value="totalQuantity")
    public int getTotalQuantity();

    @Import(value="price")
    public int getPrice();

    @Import(value="spent")
    public int getSpent();

    @Import(value="spent")
    public void setSpent(int var1);

    @Import(value="state")
    public byte getRSState();
}

