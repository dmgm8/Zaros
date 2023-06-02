/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.grounditems;

import java.time.Instant;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.grounditems.LootType;

class GroundItem {
    private int id;
    private int itemId;
    private String name;
    private int quantity;
    private WorldPoint location;
    private int height;
    private int haPrice;
    private int gePrice;
    private int offset;
    private boolean tradeable;
    @Nonnull
    private LootType lootType;
    @Nullable
    private Instant spawnTime;
    private boolean stackable;

    int getHaPrice() {
        return this.haPrice * this.quantity;
    }

    int getGePrice() {
        return this.gePrice * this.quantity;
    }

    boolean isMine() {
        return this.lootType != LootType.UNKNOWN;
    }

    GroundItem(int id, int itemId, String name, int quantity, WorldPoint location, int height, int haPrice, int gePrice, int offset, boolean tradeable, @Nonnull LootType lootType, @Nullable Instant spawnTime, boolean stackable) {
        if (lootType == null) {
            throw new NullPointerException("lootType is marked non-null but is null");
        }
        this.id = id;
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.location = location;
        this.height = height;
        this.haPrice = haPrice;
        this.gePrice = gePrice;
        this.offset = offset;
        this.tradeable = tradeable;
        this.lootType = lootType;
        this.spawnTime = spawnTime;
        this.stackable = stackable;
    }

    public static GroundItemBuilder builder() {
        return new GroundItemBuilder();
    }

    public int getId() {
        return this.id;
    }

    public int getItemId() {
        return this.itemId;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public WorldPoint getLocation() {
        return this.location;
    }

    public int getHeight() {
        return this.height;
    }

    public int getOffset() {
        return this.offset;
    }

    public boolean isTradeable() {
        return this.tradeable;
    }

    @Nonnull
    public LootType getLootType() {
        return this.lootType;
    }

    @Nullable
    public Instant getSpawnTime() {
        return this.spawnTime;
    }

    public boolean isStackable() {
        return this.stackable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLocation(WorldPoint location) {
        this.location = location;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHaPrice(int haPrice) {
        this.haPrice = haPrice;
    }

    public void setGePrice(int gePrice) {
        this.gePrice = gePrice;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTradeable(boolean tradeable) {
        this.tradeable = tradeable;
    }

    public void setLootType(@Nonnull LootType lootType) {
        if (lootType == null) {
            throw new NullPointerException("lootType is marked non-null but is null");
        }
        this.lootType = lootType;
    }

    public void setSpawnTime(@Nullable Instant spawnTime) {
        this.spawnTime = spawnTime;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GroundItem)) {
            return false;
        }
        GroundItem other = (GroundItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getItemId() != other.getItemId()) {
            return false;
        }
        if (this.getQuantity() != other.getQuantity()) {
            return false;
        }
        if (this.getHeight() != other.getHeight()) {
            return false;
        }
        if (this.getHaPrice() != other.getHaPrice()) {
            return false;
        }
        if (this.getGePrice() != other.getGePrice()) {
            return false;
        }
        if (this.getOffset() != other.getOffset()) {
            return false;
        }
        if (this.isTradeable() != other.isTradeable()) {
            return false;
        }
        if (this.isStackable() != other.isStackable()) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        WorldPoint this$location = this.getLocation();
        WorldPoint other$location = other.getLocation();
        if (this$location == null ? other$location != null : !this$location.equals((Object)other$location)) {
            return false;
        }
        LootType this$lootType = this.getLootType();
        LootType other$lootType = other.getLootType();
        if (this$lootType == null ? other$lootType != null : !((Object)((Object)this$lootType)).equals((Object)other$lootType)) {
            return false;
        }
        Instant this$spawnTime = this.getSpawnTime();
        Instant other$spawnTime = other.getSpawnTime();
        return !(this$spawnTime == null ? other$spawnTime != null : !((Object)this$spawnTime).equals(other$spawnTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof GroundItem;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getItemId();
        result = result * 59 + this.getQuantity();
        result = result * 59 + this.getHeight();
        result = result * 59 + this.getHaPrice();
        result = result * 59 + this.getGePrice();
        result = result * 59 + this.getOffset();
        result = result * 59 + (this.isTradeable() ? 79 : 97);
        result = result * 59 + (this.isStackable() ? 79 : 97);
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        WorldPoint $location = this.getLocation();
        result = result * 59 + ($location == null ? 43 : $location.hashCode());
        LootType $lootType = this.getLootType();
        result = result * 59 + ($lootType == null ? 43 : ((Object)((Object)$lootType)).hashCode());
        Instant $spawnTime = this.getSpawnTime();
        result = result * 59 + ($spawnTime == null ? 43 : ((Object)$spawnTime).hashCode());
        return result;
    }

    public String toString() {
        return "GroundItem(id=" + this.getId() + ", itemId=" + this.getItemId() + ", name=" + this.getName() + ", quantity=" + this.getQuantity() + ", location=" + (Object)this.getLocation() + ", height=" + this.getHeight() + ", haPrice=" + this.getHaPrice() + ", gePrice=" + this.getGePrice() + ", offset=" + this.getOffset() + ", tradeable=" + this.isTradeable() + ", lootType=" + (Object)((Object)this.getLootType()) + ", spawnTime=" + this.getSpawnTime() + ", stackable=" + this.isStackable() + ")";
    }

    public static class GroundItemBuilder {
        private int id;
        private int itemId;
        private String name;
        private int quantity;
        private WorldPoint location;
        private int height;
        private int haPrice;
        private int gePrice;
        private int offset;
        private boolean tradeable;
        private LootType lootType;
        private Instant spawnTime;
        private boolean stackable;

        GroundItemBuilder() {
        }

        public GroundItemBuilder id(int id) {
            this.id = id;
            return this;
        }

        public GroundItemBuilder itemId(int itemId) {
            this.itemId = itemId;
            return this;
        }

        public GroundItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public GroundItemBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public GroundItemBuilder location(WorldPoint location) {
            this.location = location;
            return this;
        }

        public GroundItemBuilder height(int height) {
            this.height = height;
            return this;
        }

        public GroundItemBuilder haPrice(int haPrice) {
            this.haPrice = haPrice;
            return this;
        }

        public GroundItemBuilder gePrice(int gePrice) {
            this.gePrice = gePrice;
            return this;
        }

        public GroundItemBuilder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public GroundItemBuilder tradeable(boolean tradeable) {
            this.tradeable = tradeable;
            return this;
        }

        public GroundItemBuilder lootType(@Nonnull LootType lootType) {
            this.lootType = lootType;
            return this;
        }

        public GroundItemBuilder spawnTime(@Nullable Instant spawnTime) {
            this.spawnTime = spawnTime;
            return this;
        }

        public GroundItemBuilder stackable(boolean stackable) {
            this.stackable = stackable;
            return this;
        }

        public GroundItem build() {
            return new GroundItem(this.id, this.itemId, this.name, this.quantity, this.location, this.height, this.haPrice, this.gePrice, this.offset, this.tradeable, this.lootType, this.spawnTime, this.stackable);
        }

        public String toString() {
            return "GroundItem.GroundItemBuilder(id=" + this.id + ", itemId=" + this.itemId + ", name=" + this.name + ", quantity=" + this.quantity + ", location=" + (Object)this.location + ", height=" + this.height + ", haPrice=" + this.haPrice + ", gePrice=" + this.gePrice + ", offset=" + this.offset + ", tradeable=" + this.tradeable + ", lootType=" + (Object)((Object)this.lootType) + ", spawnTime=" + this.spawnTime + ", stackable=" + this.stackable + ")";
        }
    }
}

