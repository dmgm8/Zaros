/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.loottracker;

import java.time.Instant;
import java.util.Collection;
import net.runelite.http.api.loottracker.GameItem;
import net.runelite.http.api.loottracker.LootRecordType;

public class LootRecord {
    private String eventId;
    private LootRecordType type;
    private Object metadata;
    private Collection<GameItem> drops;
    private Instant time;
    private Integer world;

    public String getEventId() {
        return this.eventId;
    }

    public LootRecordType getType() {
        return this.type;
    }

    public Object getMetadata() {
        return this.metadata;
    }

    public Collection<GameItem> getDrops() {
        return this.drops;
    }

    public Instant getTime() {
        return this.time;
    }

    public Integer getWorld() {
        return this.world;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setType(LootRecordType type) {
        this.type = type;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public void setDrops(Collection<GameItem> drops) {
        this.drops = drops;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public void setWorld(Integer world) {
        this.world = world;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LootRecord)) {
            return false;
        }
        LootRecord other = (LootRecord)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$world = this.getWorld();
        Integer other$world = other.getWorld();
        if (this$world == null ? other$world != null : !((Object)this$world).equals(other$world)) {
            return false;
        }
        String this$eventId = this.getEventId();
        String other$eventId = other.getEventId();
        if (this$eventId == null ? other$eventId != null : !this$eventId.equals(other$eventId)) {
            return false;
        }
        LootRecordType this$type = this.getType();
        LootRecordType other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type)) {
            return false;
        }
        Object this$metadata = this.getMetadata();
        Object other$metadata = other.getMetadata();
        if (this$metadata == null ? other$metadata != null : !this$metadata.equals(other$metadata)) {
            return false;
        }
        Collection<GameItem> this$drops = this.getDrops();
        Collection<GameItem> other$drops = other.getDrops();
        if (this$drops == null ? other$drops != null : !((Object)this$drops).equals(other$drops)) {
            return false;
        }
        Instant this$time = this.getTime();
        Instant other$time = other.getTime();
        return !(this$time == null ? other$time != null : !((Object)this$time).equals(other$time));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LootRecord;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $world = this.getWorld();
        result = result * 59 + ($world == null ? 43 : ((Object)$world).hashCode());
        String $eventId = this.getEventId();
        result = result * 59 + ($eventId == null ? 43 : $eventId.hashCode());
        LootRecordType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        Object $metadata = this.getMetadata();
        result = result * 59 + ($metadata == null ? 43 : $metadata.hashCode());
        Collection<GameItem> $drops = this.getDrops();
        result = result * 59 + ($drops == null ? 43 : ((Object)$drops).hashCode());
        Instant $time = this.getTime();
        result = result * 59 + ($time == null ? 43 : ((Object)$time).hashCode());
        return result;
    }

    public String toString() {
        return "LootRecord(eventId=" + this.getEventId() + ", type=" + (Object)((Object)this.getType()) + ", metadata=" + this.getMetadata() + ", drops=" + this.getDrops() + ", time=" + this.getTime() + ", world=" + this.getWorld() + ")";
    }

    public LootRecord() {
    }

    public LootRecord(String eventId, LootRecordType type, Object metadata, Collection<GameItem> drops, Instant time, Integer world) {
        this.eventId = eventId;
        this.type = type;
        this.metadata = metadata;
        this.drops = drops;
        this.time = time;
        this.world = world;
    }
}

