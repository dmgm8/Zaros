/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.math.DoubleMath
 *  com.google.gson.Gson
 *  com.google.inject.AbstractModule
 *  com.google.inject.Provides
 *  com.google.inject.binder.ConstantBindingBuilder
 *  com.google.inject.name.Names
 *  javax.annotation.Nullable
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.hooks.Callbacks
 *  net.runelite.http.api.RuneLiteAPI
 *  okhttp3.HttpUrl
 *  okhttp3.OkHttpClient
 */
package net.runelite.client;

import com.google.common.base.Strings;
import com.google.common.math.DoubleMath;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.name.Names;
import java.applet.Applet;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.hooks.Callbacks;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.TelemetryClient;
import net.runelite.client.account.SessionManager;
import net.runelite.client.callback.Hooks;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ChatColorConfig;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.ItemManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.task.Scheduler;
import net.runelite.client.util.DeferredEventBus;
import net.runelite.client.util.ExecutorServiceExceptionLogger;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class RuneLiteModule
extends AbstractModule {
    private final OkHttpClient okHttpClient;
    private final Supplier<Applet> clientLoader;
    private final Supplier<RuntimeConfig> configSupplier;
    private final boolean developerMode;
    private final boolean safeMode;
    private final boolean disableTelemetry;
    private final boolean vanilla;
    private final File sessionfile;
    private final File config;

    protected void configure() {
        Properties properties = RuneLiteProperties.getProperties();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            this.bindConstant().annotatedWith(Names.named(key)).to(value);
        }
        RuntimeConfig runtimeConfig = this.configSupplier.get();
        if (runtimeConfig != null && runtimeConfig.getProps() != null) {
            for (Map.Entry<String, ?> entry : runtimeConfig.getProps().entrySet()) {
                ConstantBindingBuilder binder;
                if (entry.getValue() instanceof String) {
                    binder = this.bindConstant().annotatedWith(Names.named(entry.getKey()));
                    binder.to((String)entry.getValue());
                    continue;
                }
                if (!(entry.getValue() instanceof Double)) continue;
                binder = this.bindConstant().annotatedWith(Names.named(entry.getKey()));
                if (DoubleMath.isMathematicalInteger((Double)entry.getValue())) {
                    binder.to((int)((Double)entry.getValue()).doubleValue());
                    continue;
                }
                binder.to((Double) entry.getValue());
            }
        }
        this.bindConstant().annotatedWith(Names.named("developerMode")).to(this.developerMode);
        this.bindConstant().annotatedWith(Names.named("safeMode")).to(this.safeMode);
        this.bindConstant().annotatedWith(Names.named("disableTelemetry")).to(this.disableTelemetry);
        this.bindConstant().annotatedWith(Names.named("vanilla")).to(this.vanilla);
        this.bind(File.class).annotatedWith(Names.named("sessionfile")).toInstance(this.sessionfile);
        this.bind(File.class).annotatedWith(Names.named("config")).toInstance(this.config);
        this.bind(ScheduledExecutorService.class).toInstance(new ExecutorServiceExceptionLogger(Executors.newSingleThreadScheduledExecutor()));
        this.bind(OkHttpClient.class).toInstance(this.okHttpClient);
        this.bind(MenuManager.class);
        this.bind(ChatMessageManager.class);
        this.bind(ItemManager.class);
        this.bind(Scheduler.class);
        this.bind(PluginManager.class);
        this.bind(SessionManager.class);
        this.bind(Gson.class).toInstance(RuneLiteAPI.GSON);
        this.bind(Callbacks.class).to(Hooks.class);
        this.bind(EventBus.class).toInstance(new EventBus());
        this.bind(EventBus.class).annotatedWith(Names.named("Deferred EventBus")).to(DeferredEventBus.class);
    }

    @Provides
    @Singleton
    Applet provideApplet() {
        return this.clientLoader.get();
    }

    @Provides
    @Singleton
    Client provideClient(@Nullable Applet applet) {
        return applet instanceof Client ? (Client)applet : null;
    }

    @Provides
    @Singleton
    RuntimeConfig provideRuntimeConfig() {
        return this.configSupplier.get();
    }

    @Provides
    @Singleton
    RuneLiteConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(RuneLiteConfig.class);
    }

    @Provides
    @Singleton
    ChatColorConfig provideChatColorConfig(ConfigManager configManager) {
        return configManager.getConfig(ChatColorConfig.class);
    }

    @Provides
    @Named(value="runelite.api.base")
    HttpUrl provideApiBase(@Named(value="runelite.api.base") String s) {
        String prop = System.getProperty("runelite.http-service.url");
        return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
    }

    @Provides
    @Named(value="runelite.session")
    HttpUrl provideSession(@Named(value="runelite.session") String s) {
        String prop = System.getProperty("runelite.session.url");
        return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
    }

    @Provides
    @Named(value="runelite.static.base")
    HttpUrl provideStaticBase(@Named(value="runelite.static.base") String s) {
        String prop = System.getProperty("runelite.static.url");
        return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
    }

    @Provides
    @Named(value="runelite.ws")
    HttpUrl provideWs(@Named(value="runelite.ws") String s) {
        String prop = System.getProperty("runelite.ws.url");
        return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
    }

    @Provides
    @Singleton
    TelemetryClient provideTelemetry(OkHttpClient okHttpClient, Gson gson, @Named(value="runelite.api.base") HttpUrl apiBase) {
        return this.disableTelemetry ? null : new TelemetryClient(okHttpClient, gson, apiBase);
    }

    public RuneLiteModule(OkHttpClient okHttpClient, Supplier<Applet> clientLoader, Supplier<RuntimeConfig> configSupplier, boolean developerMode, boolean safeMode, boolean disableTelemetry, boolean vanilla, File sessionfile, File config) {
        this.okHttpClient = okHttpClient;
        this.clientLoader = clientLoader;
        this.configSupplier = configSupplier;
        this.developerMode = developerMode;
        this.safeMode = safeMode;
        this.disableTelemetry = disableTelemetry;
        this.vanilla = vanilla;
        this.sessionfile = sessionfile;
        this.config = config;
    }
}

