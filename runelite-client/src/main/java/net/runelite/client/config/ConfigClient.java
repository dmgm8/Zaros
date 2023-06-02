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
 *  net.runelite.http.api.config.ConfigPatch
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.HttpUrl
 *  okhttp3.MediaType
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.config;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.http.api.RuneLiteAPI;
import net.runelite.http.api.config.ConfigPatch;
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

public class ConfigClient {
    private static final Logger log = LoggerFactory.getLogger(ConfigClient.class);
    private final OkHttpClient client;
    private final HttpUrl apiBase;
    private final Gson gson;
    private UUID uuid;

    @Inject
    private ConfigClient(OkHttpClient client, @Named(value="runelite.api.base") HttpUrl apiBase, Gson gson) {
        this.client = client;
        this.apiBase = apiBase;
        this.gson = gson;
    }

    public Map<String, String> get() throws IOException {
        Map map;
        block8: {
            HttpUrl url = this.apiBase.newBuilder().addPathSegment("config").addPathSegment("v2").build();
            log.debug("Built URI: {}", (Object)url);
            Request request = new Request.Builder().header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();
            Response response = this.client.newCall(request).execute();
            try {
                InputStream in = response.body().byteStream();
                Type type = new TypeToken<Map<String, String>>(){}.getType();
                map = (Map)this.gson.fromJson((Reader)new InputStreamReader(in, StandardCharsets.UTF_8), type);
                if (response == null) break block8;
            }
            catch (Throwable throwable) {
                try {
                    if (response != null) {
                        try {
                            response.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (JsonParseException ex) {
                    throw new IOException(ex);
                }
            }
            response.close();
        }
        return map;
    }

    public CompletableFuture<Void> patch(final ConfigPatch patch) {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        HttpUrl url = this.apiBase.newBuilder().addPathSegment("config").addPathSegment("v2").build();
        log.debug("Built URI: {}", (Object)url);
        Request request = new Request.Builder().patch(RequestBody.create((MediaType)RuneLiteAPI.JSON, (String)this.gson.toJson((Object)patch))).header("RUNELITE-AUTH", this.uuid.toString()).url(url).build();
        this.client.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                log.warn("Unable to synchronize configuration item", (Throwable)e);
                future.completeExceptionally(e);
            }

            public void onResponse(Call call, Response response) {
                if (response.code() != 200) {
                    String body = "bad response";
                    try {
                        body = response.body().string();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                    log.warn("failed to synchronize some of {}/{} configuration values: {}", new Object[]{patch.getEdit().size(), patch.getUnset().size(), body});
                } else {
                    log.debug("Synchronized {}/{} configuration values", (Object)patch.getEdit().size(), (Object)patch.getUnset().size());
                }
                response.close();
                future.complete(null);
            }
        });
        return future;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}

