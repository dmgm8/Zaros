/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.CharMatcher
 *  com.google.common.base.Splitter
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.HttpUrl
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.Response
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.hiscore;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.hiscore.HiscoreEndpoint;
import net.runelite.client.hiscore.HiscoreResult;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.hiscore.Skill;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HiscoreClient {
    private static final Logger log = LoggerFactory.getLogger(HiscoreClient.class);
    private static final Splitter NEWLINE_SPLITTER = Splitter.on((CharMatcher)CharMatcher.anyOf((CharSequence)"\r\n")).omitEmptyStrings();
    private static final Splitter COMMA_SPLITTER = Splitter.on((char)',').trimResults();
    private final OkHttpClient client;
    private final Map<HiscoreEndpoint, List<HiscoreSkill>> mappings;

    @Inject
    private HiscoreClient(OkHttpClient client, Gson gson, @Nullable RuntimeConfig runtimeConfig) {
        this.client = client;
        Map<String, List<String>> strMapping = HiscoreClient.loadDiskMappings(gson);
        if (runtimeConfig != null && runtimeConfig.getHiscoreMapping() != null) {
            strMapping.putAll(runtimeConfig.getHiscoreMapping());
        }
        this.mappings = HiscoreClient.convertMappings(strMapping, false);
    }

    @VisibleForTesting
    static Map<String, List<String>> loadDiskMappings(Gson gson) {
        Map map;
        block8: {
            InputStream is = HiscoreClient.class.getResourceAsStream("mappings.json");
            try {
                map = (Map)gson.fromJson((Reader)new InputStreamReader(is, StandardCharsets.UTF_8), new TypeToken<Map<String, List<String>>>(){}.getType());
                if (is == null) break block8;
            }
            catch (Throwable throwable) {
                try {
                    if (is != null) {
                        try {
                            is.close();
                        }
                        catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            is.close();
        }
        return map;
    }

    @VisibleForTesting
    static Map<HiscoreEndpoint, List<HiscoreSkill>> convertMappings(Map<String, List<String>> strMapping, boolean strict) {
        HashMap entries = new HashMap();
        for (Map.Entry<String, List<String>> e : strMapping.entrySet()) {
            entries.put(e.getKey(), Collections.unmodifiableList(e.getValue().stream().map(name -> {
                try {
                    return HiscoreSkill.valueOf(name);
                }
                catch (IllegalArgumentException ex) {
                    log.warn("invalid skill {}", name, (Object)ex);
                    if (strict) {
                        throw ex;
                    }
                    return null;
                }
            }).collect(Collectors.toList())));
        }
        Map out = (Map)Arrays.stream(HiscoreEndpoint.values()).collect(ImmutableMap.toImmutableMap(Function.identity(), endpoint -> {
            List map = (List)entries.remove(endpoint.name());
            if (map == null) {
                map = (List)entries.get("default");
            }
            return map;
        }));
        entries.remove("default");
        if (strict && !entries.isEmpty()) {
            log.warn("invalid endpoint {}", entries.keySet());
            throw new IllegalArgumentException("invalid entrypoints");
        }
        return out;
    }

    public HiscoreResult lookup(String username) throws IOException {
        return this.lookup(username, HiscoreEndpoint.NORMAL);
    }

    public HiscoreResult lookup(String username, HiscoreEndpoint endpoint) throws IOException {
        return this.lookup(username, endpoint, endpoint.getHiscoreURL());
    }

    @VisibleForTesting
    HiscoreResult lookup(String username, HiscoreEndpoint endpoint, HttpUrl url) throws IOException {
        try (Response response = this.client.newCall(HiscoreClient.buildRequest(username, url)).execute();){
            HiscoreResult hiscoreResult = this.processResponse(username, endpoint, response);
            return hiscoreResult;
        }
    }

    public CompletableFuture<HiscoreResult> lookupAsync(final String username, final HiscoreEndpoint endpoint) {
        final CompletableFuture<HiscoreResult> future = new CompletableFuture<HiscoreResult>();
        this.client.newCall(HiscoreClient.buildRequest(username, endpoint.getHiscoreURL())).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    future.complete(HiscoreClient.this.processResponse(username, endpoint, response));
                }
                finally {
                    response.close();
                }
            }
        });
        return future;
    }

    private static Request buildRequest(String username, HttpUrl hiscoreUrl) {
        HttpUrl url = hiscoreUrl.newBuilder().addQueryParameter("player", username).build();
        log.debug("Built URL {}", (Object)url);
        return new Request.Builder().url(url).build();
    }

    private HiscoreResult processResponse(String username, HiscoreEndpoint endpoint, Response response) throws IOException {
        if (!response.isSuccessful()) {
            if (response.code() == 404) {
                return null;
            }
            throw new IOException("Error retrieving data from Jagex Hiscores: " + (Object)response);
        }
        String responseStr = response.body().string();
        int accountType = -1;
        ImmutableMap.Builder skills = ImmutableMap.builder();
        Iterator<HiscoreSkill> map = this.mappings.get((Object)endpoint).iterator();
        for (String line : NEWLINE_SPLITTER.split((CharSequence)responseStr)) {
            if (!map.hasNext()) {
                log.warn("{} returned extra data", (Object)endpoint);
                break;
            }
            List record = COMMA_SPLITTER.splitToList((CharSequence)line);
            if (accountType == -1) {
                accountType = Integer.parseInt((String)record.get(0));
                continue;
            }
            HiscoreSkill skill = map.next();
            if (skill == null) continue;
            try {
                int rank = Integer.parseInt((String)record.get(0));
                int level = Integer.parseInt((String)record.get(1));
                long experience = -1L;
                if (record.size() == 3) {
                    experience = Long.parseLong((String)record.get(2));
                }
                skills.put((Object)skill, (Object)new Skill(rank, level, experience));
            }
            catch (Exception e) {
                log.warn("invalid hiscore line \"{}\"", (Object)line, (Object)e);
            }
        }
        if (map.hasNext()) {
            log.warn("{} returned less data than expected ({} expected next)", (Object)endpoint, (Object)map.next());
        }
        return new HiscoreResult(accountType, username, (Map<HiscoreSkill, Skill>)skills.build());
    }
}

