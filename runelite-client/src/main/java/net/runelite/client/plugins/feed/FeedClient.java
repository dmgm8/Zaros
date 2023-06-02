/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonParseException
 *  javax.inject.Inject
 *  javax.inject.Named
 *  net.runelite.http.api.feed.FeedResult
 *  okhttp3.HttpUrl
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.Response
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.feed;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.http.api.feed.FeedResult;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedClient {
    private static final Logger log = LoggerFactory.getLogger(FeedClient.class);
    private final OkHttpClient client;
    private final HttpUrl apiBase;
    private final Gson gson;

    @Inject
    private FeedClient(OkHttpClient client, @Named(value="runelite.api.base") HttpUrl apiBase, Gson gson) {
        this.client = client;
        this.apiBase = apiBase;
        this.gson = gson;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public FeedResult lookupFeed() throws IOException {
        HttpUrl url = this.apiBase.newBuilder().addPathSegment("feed.js").build();
        log.debug("Built URI: {}", (Object)url);
        Request request = new Request.Builder().url(url).build();
        try (Response response = this.client.newCall(request).execute();){
            if (!response.isSuccessful()) {
                log.debug("Error looking up feed: {}", (Object)response);
                FeedResult feedResult2 = null;
                return feedResult2;
            }
            InputStream in = response.body().byteStream();
            FeedResult feedResult = (FeedResult)this.gson.fromJson((Reader)new InputStreamReader(in, StandardCharsets.UTF_8), FeedResult.class);
            return feedResult;
        }
        catch (JsonParseException ex) {
            throw new IOException(ex);
        }
    }
}

