/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import java.util.List;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSTradingPostResult;

public interface RSTradingPostResults {
    @Import(value="events")
    public List<RSTradingPostResult> getEvents();
}

