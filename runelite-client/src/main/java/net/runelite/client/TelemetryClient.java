/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  net.runelite.http.api.RuneLiteAPI
 *  net.runelite.http.api.telemetry.Telemetry
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
package net.runelite.client;

import com.google.gson.Gson;
import com.sun.management.OperatingSystemMXBean;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import net.runelite.http.api.RuneLiteAPI;
import net.runelite.http.api.telemetry.Telemetry;
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

public class TelemetryClient {
    private static final Logger log = LoggerFactory.getLogger(TelemetryClient.class);
    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final HttpUrl apiBase;

    void submitTelemetry() {
        HttpUrl url = this.apiBase.newBuilder().addPathSegment("telemetry").build();
        Request request = new Request.Builder().url(url).post(RequestBody.create(RuneLiteAPI.JSON, this.gson.toJson(TelemetryClient.buildTelemetry()))).build();
        this.okHttpClient.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                log.debug("Error submitting telemetry", e);
            }

            public void onResponse(Call call, Response response) {
                log.debug("Submitted telemetry");
                response.close();
            }
        });
    }

    public void submitError(String type, String error) {
        HttpUrl url = this.apiBase.newBuilder().addPathSegment("telemetry").addPathSegment("error").addQueryParameter("type", type).addQueryParameter("error", error).build();
        Request request = new Request.Builder().url(url).post(RequestBody.create(null, new byte[0])).build();
        this.okHttpClient.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException e) {
                log.debug("Error submitting error", e);
            }

            public void onResponse(Call call, Response response) {
                log.debug("Submitted error");
                response.close();
            }
        });
    }

    private static Telemetry buildTelemetry() {
        Telemetry telemetry = new Telemetry();
        telemetry.setJavaVendor(System.getProperty("java.vendor"));
        telemetry.setJavaVersion(System.getProperty("java.version"));
        telemetry.setOsName(System.getProperty("os.name"));
        telemetry.setOsVersion(System.getProperty("os.version"));
        telemetry.setOsArch(System.getProperty("os.arch"));
        telemetry.setLauncherVersion(System.getProperty("runelite.launcher.version"));
        java.lang.management.OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        if (operatingSystemMXBean instanceof OperatingSystemMXBean) {
            long totalPhysicalMemorySize = ((OperatingSystemMXBean)operatingSystemMXBean).getTotalPhysicalMemorySize();
            telemetry.setTotalMemory(totalPhysicalMemorySize);
        }
        return telemetry;
    }

    public TelemetryClient(OkHttpClient okHttpClient, Gson gson, HttpUrl apiBase) {
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.apiBase = apiBase;
    }
}

