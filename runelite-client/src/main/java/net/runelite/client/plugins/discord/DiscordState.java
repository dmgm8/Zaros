/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  com.google.common.collect.ComparisonChain
 *  javax.inject.Inject
 *  javax.inject.Named
 */
package net.runelite.client.plugins.discord;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.client.discord.DiscordPresence;
import net.runelite.client.discord.DiscordService;
import net.runelite.client.plugins.discord.DiscordConfig;
import net.runelite.client.plugins.discord.DiscordGameEventType;

class DiscordState {
    private final List<EventWithTime> events = new ArrayList<EventWithTime>();
    private final DiscordService discordService;
    private final DiscordConfig config;
    private final String runeliteTitle;
    private final String runeliteVersion;
    private DiscordPresence lastPresence;

    @Inject
    private DiscordState(DiscordService discordService, DiscordConfig config, @Named(value="runelite.title") String runeliteTitle, @Named(value="runelite.version") String runeliteVersion) {
        this.discordService = discordService;
        this.config = config;
        this.runeliteTitle = runeliteTitle;
        this.runeliteVersion = runeliteVersion;
    }

    void reset() {
        this.discordService.clearPresence();
        this.events.clear();
        this.lastPresence = null;
    }

    void triggerEvent(DiscordGameEventType eventType) {
        EventWithTime event;
        Optional<EventWithTime> foundEvent = this.events.stream().filter(e -> ((EventWithTime)e).type == eventType).findFirst();
        if (foundEvent.isPresent()) {
            event = foundEvent.get();
        } else {
            event = new EventWithTime(eventType);
            event.setStart(Instant.now());
            this.events.add(event);
        }
        event.setUpdated(Instant.now());
        if (event.getType().isShouldClear()) {
            this.events.removeIf(e -> e.getType() != eventType && e.getType().isShouldBeCleared());
        }
        if (event.getType().isShouldRestart()) {
            event.setStart(Instant.now());
        }
        this.events.sort((a, b) -> ComparisonChain.start().compare(b.getType().getPriority(), a.getType().getPriority()).compare((Comparable)b.getUpdated(), (Comparable)a.getUpdated()).result());
        this.updatePresenceWithLatestEvent();
    }

    private void updatePresenceWithLatestEvent() {
        Instant startTime;
        if (this.events.isEmpty()) {
            this.reset();
            return;
        }
        EventWithTime event = this.events.get(0);
        String imageKey = null;
        String state = null;
        String details = null;
        for (EventWithTime eventWithTime : this.events) {
            if (imageKey == null) {
                imageKey = eventWithTime.getType().getImageKey();
            }
            if (details == null) {
                details = eventWithTime.getType().getDetails();
            }
            if (state == null) {
                state = eventWithTime.getType().getState();
            }
            if (imageKey == null || details == null || state == null) continue;
            break;
        }
        String versionShortHand = this.runeliteVersion.replace("-SNAPSHOT", "+");
        DiscordPresence.DiscordPresenceBuilder presenceBuilder = DiscordPresence.builder().state((String)MoreObjects.firstNonNull(state, (Object)"")).details((String)MoreObjects.firstNonNull((Object)details, (Object)"")).largeImageText(this.runeliteTitle + " v" + versionShortHand).smallImageKey(imageKey);
        switch (this.config.elapsedTimeType()) {
            case HIDDEN: {
                startTime = null;
                break;
            }
            case TOTAL: {
                startTime = this.events.stream().filter(e -> e.getType().isRoot()).sorted((a, b) -> b.getUpdated().compareTo(a.getUpdated())).map(EventWithTime::getStart).findFirst().orElse(event.getStart());
                break;
            }
            default: {
                startTime = event.getStart();
            }
        }
        presenceBuilder.startTimestamp(startTime);
        DiscordPresence presence = presenceBuilder.build();
        if (!presence.equals(this.lastPresence)) {
            this.lastPresence = presence;
            this.discordService.updatePresence(presence);
        }
    }

    void checkForTimeout() {
        if (this.events.isEmpty()) {
            return;
        }
        Duration actionTimeout = Duration.ofMinutes(this.config.actionTimeout());
        Instant now = Instant.now();
        boolean removedAny = this.events.removeAll(this.events.stream().filter(event -> event.getType().isShouldBeCleared()).filter(event -> event.getType().isShouldTimeout() && now.isAfter(event.getUpdated().plus(actionTimeout))).collect(Collectors.toList()));
        if (removedAny) {
            this.updatePresenceWithLatestEvent();
        }
    }

    private static class EventWithTime {
        private final DiscordGameEventType type;
        private Instant start;
        private Instant updated;

        public EventWithTime(DiscordGameEventType type) {
            this.type = type;
        }

        public DiscordGameEventType getType() {
            return this.type;
        }

        public Instant getStart() {
            return this.start;
        }

        public Instant getUpdated() {
            return this.updated;
        }

        public void setStart(Instant start) {
            this.start = start;
        }

        public void setUpdated(Instant updated) {
            this.updated = updated;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof EventWithTime)) {
                return false;
            }
            EventWithTime other = (EventWithTime)o;
            if (!other.canEqual(this)) {
                return false;
            }
            DiscordGameEventType this$type = this.getType();
            DiscordGameEventType other$type = other.getType();
            if (this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type)) {
                return false;
            }
            Instant this$start = this.getStart();
            Instant other$start = other.getStart();
            if (this$start == null ? other$start != null : !((Object)this$start).equals(other$start)) {
                return false;
            }
            Instant this$updated = this.getUpdated();
            Instant other$updated = other.getUpdated();
            return !(this$updated == null ? other$updated != null : !((Object)this$updated).equals(other$updated));
        }

        protected boolean canEqual(Object other) {
            return other instanceof EventWithTime;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            DiscordGameEventType $type = this.getType();
            result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
            Instant $start = this.getStart();
            result = result * 59 + ($start == null ? 43 : ((Object)$start).hashCode());
            Instant $updated = this.getUpdated();
            result = result * 59 + ($updated == null ? 43 : ((Object)$updated).hashCode());
            return result;
        }

        public String toString() {
            return "DiscordState.EventWithTime(type=" + (Object)((Object)this.getType()) + ", start=" + this.getStart() + ", updated=" + this.getUpdated() + ")";
        }
    }
}

