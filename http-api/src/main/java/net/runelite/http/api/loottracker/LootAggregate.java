/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.loottracker;

import java.time.Instant;
import java.util.Collection;
import net.runelite.http.api.loottracker.GameItem;
import net.runelite.http.api.loottracker.LootRecordType;

public class LootAggregate {
    private String eventId;
    private LootRecordType type;
    private Collection<GameItem> drops;
    private Instant first_time;
    private Instant last_time;
    private int amount;

    public String getEventId() {
        return this.eventId;
    }

    public LootRecordType getType() {
        return this.type;
    }

    public Collection<GameItem> getDrops() {
        return this.drops;
    }

    public Instant getFirst_time() {
        return this.first_time;
    }

    public Instant getLast_time() {
        return this.last_time;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setType(LootRecordType type) {
        this.type = type;
    }

    public void setDrops(Collection<GameItem> drops) {
        this.drops = drops;
    }

    public void setFirst_time(Instant first_time) {
        this.first_time = first_time;
    }

    public void setLast_time(Instant last_time) {
        this.last_time = last_time;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LootAggregate)) {
            return false;
        }
        LootAggregate other = (LootAggregate)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
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
        Collection<GameItem> this$drops = this.getDrops();
        Collection<GameItem> other$drops = other.getDrops();
        if (this$drops == null ? other$drops != null : !((Object)this$drops).equals(other$drops)) {
            return false;
        }
        Instant this$first_time = this.getFirst_time();
        Instant other$first_time = other.getFirst_time();
        if (this$first_time == null ? other$first_time != null : !((Object)this$first_time).equals(other$first_time)) {
            return false;
        }
        Instant this$last_time = this.getLast_time();
        Instant other$last_time = other.getLast_time();
        return !(this$last_time == null ? other$last_time != null : !((Object)this$last_time).equals(other$last_time));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LootAggregate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAmount();
        String $eventId = this.getEventId();
        result = result * 59 + ($eventId == null ? 43 : $eventId.hashCode());
        LootRecordType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        Collection<GameItem> $drops = this.getDrops();
        result = result * 59 + ($drops == null ? 43 : ((Object)$drops).hashCode());
        Instant $first_time = this.getFirst_time();
        result = result * 59 + ($first_time == null ? 43 : ((Object)$first_time).hashCode());
        Instant $last_time = this.getLast_time();
        result = result * 59 + ($last_time == null ? 43 : ((Object)$last_time).hashCode());
        return result;
    }

    public String toString() {
        return "LootAggregate(eventId=" + this.getEventId() + ", type=" + (Object)((Object)this.getType()) + ", drops=" + this.getDrops() + ", first_time=" + this.getFirst_time() + ", last_time=" + this.getLast_time() + ", amount=" + this.getAmount() + ")";
    }

    public LootAggregate(String eventId, LootRecordType type, Collection<GameItem> drops, Instant first_time, Instant last_time, int amount) {
        this.eventId = eventId;
        this.type = type;
        this.drops = drops;
        this.first_time = first_time;
        this.last_time = last_time;
        this.amount = amount;
    }

    public LootAggregate() {
    }
}

