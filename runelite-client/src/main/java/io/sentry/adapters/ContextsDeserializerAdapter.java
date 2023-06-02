/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonDeserializer
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.protocol.App;
import io.sentry.protocol.Browser;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.Device;
import io.sentry.protocol.Gpu;
import io.sentry.protocol.OperatingSystem;
import io.sentry.protocol.SentryRuntime;
import java.lang.reflect.Type;
import java.util.Iterator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class ContextsDeserializerAdapter
implements JsonDeserializer<Contexts> {
    @NotNull
    private final ILogger logger;

    public ContextsDeserializerAdapter(@NotNull ILogger logger) {
        this.logger = logger;
    }

    public Contexts deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json != null && !json.isJsonNull()) {
                Contexts contexts = new Contexts();
                JsonObject jsonObject = json.getAsJsonObject();
                if (jsonObject != null && !jsonObject.isJsonNull()) {
                    Iterator iterator = jsonObject.keySet().iterator();
                    block20: while (iterator.hasNext()) {
                        String key;
                        switch (key = (String)iterator.next()) {
                            case "app": {
                                App app = this.parseObject(context, jsonObject, key, App.class);
                                if (app == null) continue block20;
                                contexts.setApp(app);
                                continue block20;
                            }
                            case "browser": {
                                Browser browser = this.parseObject(context, jsonObject, key, Browser.class);
                                if (browser == null) continue block20;
                                contexts.setBrowser(browser);
                                continue block20;
                            }
                            case "device": {
                                Device device = this.parseObject(context, jsonObject, key, Device.class);
                                if (device == null) continue block20;
                                contexts.setDevice(device);
                                continue block20;
                            }
                            case "os": {
                                OperatingSystem os = this.parseObject(context, jsonObject, key, OperatingSystem.class);
                                if (os == null) continue block20;
                                contexts.setOperatingSystem(os);
                                continue block20;
                            }
                            case "runtime": {
                                SentryRuntime runtime = this.parseObject(context, jsonObject, key, SentryRuntime.class);
                                if (runtime == null) continue block20;
                                contexts.setRuntime(runtime);
                                continue block20;
                            }
                            case "gpu": {
                                Gpu gpu = this.parseObject(context, jsonObject, key, Gpu.class);
                                if (gpu == null) continue block20;
                                contexts.setGpu(gpu);
                                continue block20;
                            }
                        }
                        JsonElement element = jsonObject.get(key);
                        if (element == null || element.isJsonNull()) continue;
                        try {
                            Object object = context.deserialize(element, Object.class);
                            contexts.put(key, object);
                        }
                        catch (JsonParseException e) {
                            this.logger.log(SentryLevel.ERROR, e, "Error when deserializing the %s key.", key);
                        }
                    }
                }
                return contexts;
            }
        }
        catch (Exception e) {
            this.logger.log(SentryLevel.ERROR, "Error when deserializing Contexts", e);
        }
        return null;
    }

    @Nullable
    private <T> T parseObject(@NotNull JsonDeserializationContext context, @NotNull JsonObject jsonObject, @NotNull String key, @NotNull Class<T> clazz) throws JsonParseException {
        JsonObject object = jsonObject.getAsJsonObject(key);
        if (object != null && !object.isJsonNull()) {
            return (T)context.deserialize((JsonElement)object, clazz);
        }
        return null;
    }
}

