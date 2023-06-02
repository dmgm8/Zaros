/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;
import net.runelite.rs.api.RSGrandExchangeOffer;

public interface RSTradingPostResult {
    @Import(value="offer")
    public RSGrandExchangeOffer getOffer();

    @Import(value="age")
    public long getAge();
}

