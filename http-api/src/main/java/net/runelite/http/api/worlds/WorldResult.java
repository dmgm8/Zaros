/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.worlds;

import java.util.List;
import net.runelite.http.api.worlds.World;

public class WorldResult {
    private List<World> worlds;

    public List<World> getWorlds() {
        return this.worlds;
    }

    public void setWorlds(List<World> worlds) {
        this.worlds = worlds;
    }

    public World findWorld(int worldNum) {
        for (World world : this.worlds) {
            if (world.getId() != worldNum) continue;
            return world;
        }
        return null;
    }
}

