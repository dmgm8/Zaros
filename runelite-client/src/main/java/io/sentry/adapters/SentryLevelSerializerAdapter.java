/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package io.sentry.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import java.lang.reflect.Type;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class SentryLevelSerializerAdapter
implements JsonSerializer<SentryLevel> {
    @NotNull
    private final ILogger logger;

    public SentryLevelSerializerAdapter(@NotNull ILogger logger) {
        this.logger = logger;
    }

    public JsonElement serialize(SentryLevel src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            return src == null ? null : new JsonPrimitive(src.name().toLowerCase(Locale.ROOT));
        }
        catch (Exception e) {
            this.logger.log(SentryLevel.ERROR, "Error when serializing SentryLevel", e);
            return null;
        }
    }
}

