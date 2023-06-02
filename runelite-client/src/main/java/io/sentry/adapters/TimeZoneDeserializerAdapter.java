/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParseException
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package io.sentry.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import java.lang.reflect.Type;
import java.util.TimeZone;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class TimeZoneDeserializerAdapter
implements JsonDeserializer<TimeZone> {
    @NotNull
    private final ILogger logger;

    public TimeZoneDeserializerAdapter(@NotNull ILogger logger) {
        this.logger = logger;
    }

    public TimeZone deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json == null ? null : TimeZone.getTimeZone(json.getAsString());
        }
        catch (Exception e) {
            this.logger.log(SentryLevel.ERROR, "Error when deserializing TimeZone", e);
            return null;
        }
    }
}

