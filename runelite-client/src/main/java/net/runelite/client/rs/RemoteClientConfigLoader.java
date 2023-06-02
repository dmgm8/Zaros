/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  okhttp3.CacheControl
 *  okhttp3.HttpUrl
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.Response
 */
package net.runelite.client.rs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.runelite.client.rs.ClientConfigLoader;
import net.runelite.client.rs.RSConfig;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoteClientConfigLoader
implements ClientConfigLoader {
    private final OkHttpClient okHttpClient;
    private final HttpUrl url;

    @Override
    public RSConfig fetch() throws IOException {
        Request request = new Request.Builder().url(this.url).cacheControl(CacheControl.FORCE_NETWORK).build();
        RSConfig config = new RSConfig();
        try (Response response = this.okHttpClient.newCall(request).execute();){
            if (!response.isSuccessful()) {
                throw new IOException("Unsuccessful response: " + response.message());
            }
            this.processResponse(config, new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8)));
        }
        return config;
    }

    public RemoteClientConfigLoader(OkHttpClient okHttpClient, HttpUrl url) {
        this.okHttpClient = okHttpClient;
        this.url = url;
    }
}

