/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSerializationContext
 *  com.google.gson.JsonSerializer
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package io.sentry.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.protocol.Contexts;
import java.lang.reflect.Type;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class ContextsSerializerAdapter
implements JsonSerializer<Contexts> {
    @NotNull
    private final ILogger logger;

    public ContextsSerializerAdapter(@NotNull ILogger logger) {
        this.logger = logger;
    }

    public JsonElement serialize(Contexts src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        JsonObject object = new JsonObject();
        for (Map.Entry entry : src.entrySet()) {
            try {
                JsonElement element = context.serialize(entry.getValue(), Object.class);
                if (element == null) continue;
                object.add((String)entry.getKey(), element);
            }
            catch (JsonParseException e) {
                this.logger.log(SentryLevel.ERROR, "%s context key isn't serializable.", new Object[0]);
            }
        }
        return object;
    }
}

