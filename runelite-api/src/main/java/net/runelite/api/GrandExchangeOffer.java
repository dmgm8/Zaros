/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api;

import net.runelite.api.GrandExchangeOfferState;

public interface GrandExchangeOffer {
    public int getQuantitySold();

    public int getItemId();

    public int getTotalQuantity();

    public int getPrice();

    public int getSpent();

    public GrandExchangeOfferState getState();
}

