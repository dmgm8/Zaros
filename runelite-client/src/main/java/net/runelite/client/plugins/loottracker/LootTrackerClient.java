/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonParseException
 *  com.google.gson.reflect.TypeToken
 *  javax.inject.Inject
 *  javax.inject.Named
 *  net.runelite.http.api.RuneLiteAPI
 *  net.runelite.http.api.loottracker.LootAggregate
 *  net.runelite.http.api.loottracker.LootRecord
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.HttpUrl
 *  okhttp3.HttpUrl$Builder
 *  okhttp3.MediaType
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.loottracker;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.http.api.RuneLiteAPI;
import net.runelite.http.api.loottracker.LootAggregate;
import net.runelite.http.api.loottracker.LootRecord;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LootTrackerClient {
    private static final Logger log = LoggerFactory.getLogger(LootTrackerClient.class);
    private final OkHttpClient client;
    private final HttpUrl apiBase;
    private final Gson gson;
    private UUID uuid;

    @Inject
    private LootTrackerClient(OkHttpClient client, @Named(value="runelite.api.base") HttpUrl apiBase, Gson gson) {
        this.client = client;
        this.apiBase = apiBase;
        this.gson = gson;
    }

    public CompletableFuture<Void> submit(Collection<LootRecord> lootRecords) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        HttpUrl url = this.apiBase.newBuilder().addPathSegment("loottracker").build();
        Request.Builder requestBuilder = new Request.Builder();
        if (this.uuid != null) {
            requestBuilder.header("RUNELITE-AUTH", this.uuid.toString());
        }
        requestBuilder.post(RequestBody.create((MediaType)RuneLiteAPI.JSON, (String)this.gson.toJson(lootRecords))).url(url).build();
        this.client.newCall(requestBuilder.build()).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                log.warn("unable to submit loot", (Throwable)e);
                future.completeExceptionally(e);
            }

            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    log.debug("Submitted loot");
                } else {
                    log.warn("Error submitting loot: {} - {}", (Object)response.code(), (Object)response.message());
                }
                response.close();
                future.complete(null);
            }
        });
        return future;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Collection<LootAggregate> get() throws IOException {
        HttpUrl url = this.apiBase.newBuilder().addPathSegment("loottracker").build();
        Request request = new Request.Builder().header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();
        try (Response response = this.client.newCall(request).execute();){
            if (!response.isSuccessful()) {
                log.debug("Error looking up loot: {}", (Object)response);
                Collection<LootAggregate> collection2 = null;
                return collection2;
            }
            InputStream in = response.body().byteStream();
            Collection collection = (Collection)this.gson.fromJson((Reader)new InputStreamReader(in, StandardCharsets.UTF_8), new TypeToken<List<LootAggregate>>(){}.getType());
            return collection;
        }
        catch (JsonParseException ex) {
            throw new IOException(ex);
        }
    }

    public void delete(String eventId) {
        HttpUrl.Builder builder = this.apiBase.newBuilder().addPathSegment("loottracker");
        if (eventId != null) {
            builder.addQueryParameter("eventId", eventId);
        }
        Request request = new Request.Builder().header("RUNELITE-AUTH", this.uuid.toString()).delete().url(builder.build()).build();
        this.client.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                log.warn("unable to delete loot", (Throwable)e);
            }

            public void onResponse(Call call, Response response) {
                log.debug("Deleted loot");
                response.close();
            }
        });
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}

