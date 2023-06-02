/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.DateUtils;
import io.sentry.protocol.User;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Session {
    @NotNull
    private final Date started;
    @Nullable
    private Date timestamp;
    @NotNull
    private final AtomicInteger errorCount;
    @Nullable
    private final String distinctId;
    @Nullable
    private final UUID sessionId;
    @Nullable
    private Boolean init;
    @NotNull
    private State status;
    @Nullable
    private Long sequence;
    @Nullable
    private Double duration;
    @Nullable
    private final String ipAddress;
    @Nullable
    private String userAgent;
    @Nullable
    private final String environment;
    @NotNull
    private final String release;
    @NotNull
    private final Object sessionLock = new Object();

    public Session(@NotNull State status, @NotNull Date started, @Nullable Date timestamp, int errorCount, @Nullable String distinctId, @Nullable UUID sessionId, @Nullable Boolean init, @Nullable Long sequence, @Nullable Double duration, @Nullable String ipAddress, @Nullable String userAgent, @Nullable String environment, @NotNull String release) {
        this.status = status;
        this.started = started;
        this.timestamp = timestamp;
        this.errorCount = new AtomicInteger(errorCount);
        this.distinctId = distinctId;
        this.sessionId = sessionId;
        this.init = init;
        this.sequence = sequence;
        this.duration = duration;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.environment = environment;
        this.release = release;
    }

    public Session(@Nullable String distinctId, @Nullable User user, @Nullable String environment, @NotNull String release) {
        this(State.Ok, DateUtils.getCurrentDateTimeOrNull(), DateUtils.getCurrentDateTimeOrNull(), 0, distinctId, UUID.randomUUID(), true, null, null, user != null ? user.getIpAddress() : null, null, environment, release);
    }

    @NotNull
    public Date getStarted() {
        return (Date)this.started.clone();
    }

    @Nullable
    public String getDistinctId() {
        return this.distinctId;
    }

    @Nullable
    public UUID getSessionId() {
        return this.sessionId;
    }

    @Nullable
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Nullable
    public String getUserAgent() {
        return this.userAgent;
    }

    @Nullable
    public String getEnvironment() {
        return this.environment;
    }

    @NotNull
    public String getRelease() {
        return this.release;
    }

    @Nullable
    public Boolean getInit() {
        return this.init;
    }

    @ApiStatus.Internal
    public void setInitAsTrue() {
        this.init = true;
    }

    public int errorCount() {
        return this.errorCount.get();
    }

    @NotNull
    public State getStatus() {
        return this.status;
    }

    @Nullable
    public Long getSequence() {
        return this.sequence;
    }

    @Nullable
    public Double getDuration() {
        return this.duration;
    }

    @Nullable
    public Date getTimestamp() {
        Date timestampRef = this.timestamp;
        return timestampRef != null ? (Date)timestampRef.clone() : null;
    }

    public void end() {
        this.end(DateUtils.getCurrentDateTimeOrNull());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void end(@Nullable Date timestamp) {
        Object object = this.sessionLock;
        synchronized (object) {
            this.init = null;
            if (this.status == State.Ok) {
                this.status = State.Exited;
            }
            this.timestamp = timestamp != null ? timestamp : DateUtils.getCurrentDateTimeOrNull();
            if (this.timestamp != null) {
                this.duration = this.calculateDurationTime(this.timestamp);
                this.sequence = this.getSequenceTimestamp(this.timestamp);
            }
        }
    }

    private double calculateDurationTime(@NotNull Date timestamp) {
        long diff = Math.abs(timestamp.getTime() - this.started.getTime());
        return (double)diff / 1000.0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean update(@Nullable State status, @Nullable String userAgent, boolean addErrorsCount) {
        Object object = this.sessionLock;
        synchronized (object) {
            boolean sessionHasBeenUpdated = false;
            if (status != null) {
                this.status = status;
                sessionHasBeenUpdated = true;
            }
            if (userAgent != null) {
                this.userAgent = userAgent;
                sessionHasBeenUpdated = true;
            }
            if (addErrorsCount) {
                this.errorCount.addAndGet(1);
                sessionHasBeenUpdated = true;
            }
            if (sessionHasBeenUpdated) {
                this.init = null;
                this.timestamp = DateUtils.getCurrentDateTimeOrNull();
                if (this.timestamp != null) {
                    this.sequence = this.getSequenceTimestamp(this.timestamp);
                }
            }
            return sessionHasBeenUpdated;
        }
    }

    private long getSequenceTimestamp(@NotNull Date timestamp) {
        long sequence = timestamp.getTime();
        if (sequence < 0L) {
            sequence = Math.abs(sequence);
        }
        return sequence;
    }

    @NotNull
    public Session clone() {
        return new Session(this.status, this.started, this.timestamp, this.errorCount.get(), this.distinctId, this.sessionId, this.init, this.sequence, this.duration, this.ipAddress, this.userAgent, this.environment, this.release);
    }

    public static enum State {
        Ok,
        Exited,
        Crashed,
        Abnormal;

    }
}

