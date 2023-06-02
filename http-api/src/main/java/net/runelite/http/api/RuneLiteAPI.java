/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.ExclusionStrategy
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  okhttp3.MediaType
 *  okhttp3.OkHttpClient
 */
package net.runelite.http.api;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.time.Instant;
import net.runelite.http.api.gson.ColorTypeAdapter;
import net.runelite.http.api.gson.IllegalReflectionExclusion;
import net.runelite.http.api.gson.InstantTypeAdapter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class RuneLiteAPI {
    public static final String RUNELITE_AUTH = "RUNELITE-AUTH";
    public static final String RUNELITE_MACHINEID = "RUNELITE-MACHINEID";
    @Deprecated
    public static OkHttpClient CLIENT;
    public static final Gson GSON;
    public static final MediaType JSON;

    static {
        JSON = MediaType.parse("application/json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantTypeAdapter()).registerTypeAdapter(Color.class, new ColorTypeAdapter());
        boolean assertionsEnabled = false;
        if (!$assertionsDisabled) {
            assertionsEnabled = true;
            if (false) {
                throw new AssertionError();
            }
        }
        if (assertionsEnabled) {
            IllegalReflectionExclusion jbe = new IllegalReflectionExclusion();
            gsonBuilder.addSerializationExclusionStrategy(jbe);
            gsonBuilder.addDeserializationExclusionStrategy(jbe);
        }
        GSON = gsonBuilder.create();
    }
}

