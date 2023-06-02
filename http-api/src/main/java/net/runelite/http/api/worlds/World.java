/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.worlds;

import java.util.EnumSet;
import net.runelite.http.api.worlds.WorldRegion;
import net.runelite.http.api.worlds.WorldType;

public final class World {
    private final int id;
    private final EnumSet<WorldType> types;
    private final String address;
    private final String activity;
    private final int location;
    private final int players;

    public WorldRegion getRegion() {
        return WorldRegion.valueOf(this.location);
    }

    World(int id, EnumSet<WorldType> types, String address, String activity, int location, int players) {
        this.id = id;
        this.types = types;
        this.address = address;
        this.activity = activity;
        this.location = location;
        this.players = players;
    }

    public static WorldBuilder builder() {
        return new WorldBuilder();
    }

    public int getId() {
        return this.id;
    }

    public EnumSet<WorldType> getTypes() {
        return this.types;
    }

    public String getAddress() {
        return this.address;
    }

    public String getActivity() {
        return this.activity;
    }

    public int getLocation() {
        return this.location;
    }

    public int getPlayers() {
        return this.players;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof World)) {
            return false;
        }
        World other = (World)o;
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getLocation() != other.getLocation()) {
            return false;
        }
        if (this.getPlayers() != other.getPlayers()) {
            return false;
        }
        EnumSet<WorldType> this$types = this.getTypes();
        EnumSet<WorldType> other$types = other.getTypes();
        if (this$types == null ? other$types != null : !((Object)this$types).equals(other$types)) {
            return false;
        }
        String this$address = this.getAddress();
        String other$address = other.getAddress();
        if (this$address == null ? other$address != null : !this$address.equals(other$address)) {
            return false;
        }
        String this$activity = this.getActivity();
        String other$activity = other.getActivity();
        return !(this$activity == null ? other$activity != null : !this$activity.equals(other$activity));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getLocation();
        result = result * 59 + this.getPlayers();
        EnumSet<WorldType> $types = this.getTypes();
        result = result * 59 + ($types == null ? 43 : ((Object)$types).hashCode());
        String $address = this.getAddress();
        result = result * 59 + ($address == null ? 43 : $address.hashCode());
        String $activity = this.getActivity();
        result = result * 59 + ($activity == null ? 43 : $activity.hashCode());
        return result;
    }

    public String toString() {
        return "World(id=" + this.getId() + ", types=" + this.getTypes() + ", address=" + this.getAddress() + ", activity=" + this.getActivity() + ", location=" + this.getLocation() + ", players=" + this.getPlayers() + ")";
    }

    public static class WorldBuilder {
        private int id;
        private EnumSet<WorldType> types;
        private String address;
        private String activity;
        private int location;
        private int players;

        WorldBuilder() {
        }

        public WorldBuilder id(int id) {
            this.id = id;
            return this;
        }

        public WorldBuilder types(EnumSet<WorldType> types) {
            this.types = types;
            return this;
        }

        public WorldBuilder address(String address) {
            this.address = address;
            return this;
        }

        public WorldBuilder activity(String activity) {
            this.activity = activity;
            return this;
        }

        public WorldBuilder location(int location) {
            this.location = location;
            return this;
        }

        public WorldBuilder players(int players) {
            this.players = players;
            return this;
        }

        public World build() {
            return new World(this.id, this.types, this.address, this.activity, this.location, this.players);
        }

        public String toString() {
            return "World.WorldBuilder(id=" + this.id + ", types=" + this.types + ", address=" + this.address + ", activity=" + this.activity + ", location=" + this.location + ", players=" + this.players + ")";
        }
    }
}

