/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.sentry.DateUtils;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.Session;
import io.sentry.util.Objects;
import io.sentry.util.StringUtils;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class SessionAdapter
extends TypeAdapter<Session> {
    @NotNull
    private final ILogger logger;

    public SessionAdapter(@NotNull ILogger logger) {
        this.logger = Objects.requireNonNull(logger, "The Logger is required.");
    }

    public void write(JsonWriter writer, Session value) throws IOException {
        int errorCount;
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.beginObject();
        if (value.getSessionId() != null) {
            writer.name("sid").value(value.getSessionId().toString());
        }
        if (value.getDistinctId() != null) {
            writer.name("did").value(value.getDistinctId());
        }
        if (value.getInit() != null) {
            writer.name("init").value(value.getInit());
        }
        writer.name("started").value(DateUtils.getTimestamp(value.getStarted()));
        writer.name("status").value(value.getStatus().name().toLowerCase(Locale.ROOT));
        if (value.getSequence() != null) {
            writer.name("seq").value((Number)value.getSequence());
        }
        if ((errorCount = value.errorCount()) > 0) {
            writer.name("errors").value((long)errorCount);
        }
        if (value.getDuration() != null) {
            writer.name("duration").value((Number)value.getDuration());
        }
        if (value.getTimestamp() != null) {
            writer.name("timestamp").value(DateUtils.getTimestamp(value.getTimestamp()));
        }
        boolean hasInitAttrs = false;
        hasInitAttrs = this.initAttrs(writer, hasInitAttrs);
        writer.name("release").value(value.getRelease());
        if (value.getEnvironment() != null) {
            hasInitAttrs = this.initAttrs(writer, hasInitAttrs);
            writer.name("environment").value(value.getEnvironment());
        }
        if (value.getIpAddress() != null) {
            hasInitAttrs = this.initAttrs(writer, hasInitAttrs);
            writer.name("ip_address").value(value.getIpAddress());
        }
        if (value.getUserAgent() != null) {
            hasInitAttrs = this.initAttrs(writer, hasInitAttrs);
            writer.name("user_agent").value(value.getUserAgent());
        }
        if (hasInitAttrs) {
            writer.endObject();
        }
        writer.endObject();
    }

    private boolean initAttrs(JsonWriter writer, boolean hasInitAtts) throws IOException {
        if (!hasInitAtts) {
            writer.name("attrs").beginObject();
        }
        return true;
    }

    public Session read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        UUID sid = null;
        String did = null;
        Boolean init = null;
        Date started = null;
        Session.State status = null;
        int errors = 0;
        Long seq = null;
        Double duration = null;
        Date timestamp = null;
        String release = null;
        String environment = null;
        String ipAddress = null;
        String userAgent = null;
        reader.beginObject();
        block36: while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "sid": {
                    sid = UUID.fromString(reader.nextString());
                    continue block36;
                }
                case "did": {
                    did = reader.nextString();
                    continue block36;
                }
                case "init": {
                    init = reader.nextBoolean();
                    continue block36;
                }
                case "started": {
                    started = this.converTimeStamp(reader.nextString(), "started");
                    continue block36;
                }
                case "status": {
                    status = Session.State.valueOf(StringUtils.capitalize(reader.nextString()));
                    continue block36;
                }
                case "errors": {
                    errors = reader.nextInt();
                    continue block36;
                }
                case "seq": {
                    seq = reader.nextLong();
                    continue block36;
                }
                case "duration": {
                    duration = reader.nextDouble();
                    continue block36;
                }
                case "timestamp": {
                    timestamp = this.converTimeStamp(reader.nextString(), "timestamp");
                    continue block36;
                }
                case "attrs": {
                    reader.beginObject();
                    block37: while (reader.hasNext()) {
                        switch (reader.nextName()) {
                            case "release": {
                                release = reader.nextString();
                                continue block37;
                            }
                            case "environment": {
                                environment = reader.nextString();
                                continue block37;
                            }
                            case "ip_address": {
                                ipAddress = reader.nextString();
                                continue block37;
                            }
                            case "user_agent": {
                                userAgent = reader.nextString();
                                continue block37;
                            }
                        }
                        reader.skipValue();
                    }
                    reader.endObject();
                    continue block36;
                }
            }
            reader.skipValue();
        }
        reader.endObject();
        if (status == null || started == null || release == null || release.isEmpty()) {
            this.logger.log(SentryLevel.ERROR, "Session is gonna be dropped due to invalid fields.", new Object[0]);
            return null;
        }
        return new Session(status, started, timestamp, errors, did, sid, init, seq, duration, ipAddress, userAgent, environment, release);
    }

    @Nullable
    private Date converTimeStamp(@NotNull String timestamp, @NotNull String field) {
        try {
            return DateUtils.getDateTime(timestamp);
        }
        catch (IllegalArgumentException e) {
            this.logger.log(SentryLevel.ERROR, e, "Error converting session (%s) field.", field);
            return null;
        }
    }
}

