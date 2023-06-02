/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.classic.Level
 *  ch.qos.logback.classic.Logger
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.MoreObjects
 *  com.google.inject.Guice
 *  com.google.inject.Inject
 *  com.google.inject.Injector
 *  com.google.inject.Module
 *  io.sentry.Sentry
 *  io.sentry.protocol.User
 *  javax.annotation.Nullable
 *  javax.inject.Provider
 *  javax.inject.Singleton
 *  joptsimple.ArgumentAcceptingOptionSpec
 *  joptsimple.OptionParser
 *  joptsimple.OptionSet
 *  joptsimple.OptionSpec
 *  joptsimple.ValueConversionException
 *  joptsimple.ValueConverter
 *  joptsimple.util.EnumConverter
 *  net.runelite.api.Client
 *  net.runelite.api.Constants
 *  net.runelite.http.api.RuneLiteAPI
 *  okhttp3.Cache
 *  okhttp3.OkHttpClient
 *  okhttp3.OkHttpClient$Builder
 *  okhttp3.Request
 *  okhttp3.Response
 *  org.apache.commons.lang3.SystemUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client;

import ch.qos.logback.classic.Level;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.sentry.Sentry;
import io.sentry.protocol.User;
import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.SwingUtilities;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import joptsimple.util.EnumConverter;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.client.ClassPreloader;
import net.runelite.client.ClientSessionManager;
import net.runelite.client.RuneLiteModule;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.RuntimeConfigLoader;
import net.runelite.client.TelemetryClient;
import net.runelite.client.account.SessionManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.discord.DiscordService;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.rs.ClientLoader;
import net.runelite.client.rs.ClientUpdateCheckMode;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.ui.SplashScreen;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.WidgetOverlay;
import net.runelite.client.ui.overlay.tooltip.TooltipOverlay;
import net.runelite.client.ui.overlay.worldmap.WorldMapOverlay;
import net.runelite.client.util.ReflectUtil;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class RuneLite {
    private static final Logger log = LoggerFactory.getLogger(RuneLite.class);
    public static final File RUNELITE_DIR = new File(System.getProperty("user.home"), ".zaros");
    public static final File CACHE_DIR = new File(RUNELITE_DIR, "cache");
    public static final File PLUGINS_DIR = new File(RUNELITE_DIR, "plugins");
    public static final File PROFILES_DIR = new File(RUNELITE_DIR, "profiles");
    public static final File SCREENSHOT_DIR = new File(RUNELITE_DIR, "screenshots");
    public static final File LOGS_DIR = new File(RUNELITE_DIR, "logs");
    public static final File DEFAULT_SESSION_FILE = new File(RUNELITE_DIR, "session");
    public static final File DEFAULT_CONFIG_FILE = new File(RUNELITE_DIR, "settings.properties");
    private static final int MAX_OKHTTP_CACHE_SIZE = 0x1400000;
    public static String USER_AGENT = "RuneLite/" + RuneLiteProperties.getVersion() + "-" + RuneLiteProperties.getCommit() + (RuneLiteProperties.isDirty() ? "+" : "");
    private static Injector injector;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private ExternalPluginManager externalPluginManager;
    @Inject
    private EventBus eventBus;
    @Inject
    private ConfigManager configManager;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private DiscordService discordService;
    @Inject
    private ClientSessionManager clientSessionManager;
    @Inject
    private ClientUI clientUI;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private Provider<TooltipOverlay> tooltipOverlay;
    @Inject
    private Provider<WorldMapOverlay> worldMapOverlay;
    @Inject
    @Nullable
    private Applet applet;
    @Inject
    @Nullable
    private Client client;
    @Inject
    @Nullable
    private RuntimeConfig runtimeConfig;
    @Inject
    @Nullable
    private TelemetryClient telemetryClient;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void main(String[] args) throws Exception {
        OkHttpClient okHttpClient;
        Locale.setDefault(Locale.ENGLISH);
        OptionParser parser = new OptionParser(false);
        parser.accepts("developer-mode", "Enable developer tools");
        parser.accepts("debug", "Show extra debugging output");
        parser.accepts("safe-mode", "Disables external plugins and the GPU plugin");
        parser.accepts("insecure-skip-tls-verification", "Disables TLS verification");
        parser.accepts("vanilla", "Disables all plugins and custom UI");
        parser.accepts("local-config", "Enables loading the applet params from resources");
        ArgumentAcceptingOptionSpec sessionfile = parser.accepts("sessionfile", "Use a specified session file").withRequiredArg().withValuesConvertedBy((ValueConverter)new ConfigFileConverter()).defaultsTo((Object)DEFAULT_SESSION_FILE, (Object[])new File[0]);
        ArgumentAcceptingOptionSpec configfile = parser.accepts("config", "Use a specified config file").withRequiredArg().withValuesConvertedBy((ValueConverter)new ConfigFileConverter()).defaultsTo((Object)DEFAULT_CONFIG_FILE, (Object[])new File[0]);
        ArgumentAcceptingOptionSpec updateMode = parser.accepts("rs", "Select client type").withRequiredArg().ofType(ClientUpdateCheckMode.class).defaultsTo((Object)ClientUpdateCheckMode.RUNELITE, (Object[])new ClientUpdateCheckMode[0]).withValuesConvertedBy((ValueConverter)new EnumConverter<ClientUpdateCheckMode>(ClientUpdateCheckMode.class){

            public ClientUpdateCheckMode convert(String v) {
                return (ClientUpdateCheckMode)super.convert(v.toUpperCase());
            }
        });
        parser.accepts("help", "Show this text").forHelp();
        OptionSet options = parser.parse(args);
        if (options.has("help")) {
            parser.printHelpOn((OutputStream)System.out);
            System.exit(0);
        }
        if (options.has("debug")) {
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger((String)"ROOT");
            logger.setLevel(Level.DEBUG);
        }
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            log.error("Uncaught exception:", throwable);
            if (throwable instanceof AbstractMethodError) {
                log.error("Classes are out of date; Build with maven again.");
            }
        });
        boolean localAppletConfig = options.has("local-config");
        RuneLiteAPI.CLIENT = okHttpClient = RuneLite.buildHttpClient(options.has("insecure-skip-tls-verification"));
        Sentry.init(sentryOptions -> sentryOptions.setDsn("https://18a94ce3f2fe42eb9000315fe2f72142@o480827.ingest.sentry.io/5528487"), (boolean)true);
        try {
            Sentry.setExtra((String)"os_arch", (String)SystemUtils.OS_ARCH);
            Sentry.setExtra((String)"java_vendor", (String)System.getProperty("java.vendor"));
            Sentry.setExtra((String)"java_version", (String)System.getProperty("java.version"));
        }
        catch (Exception e) {
            Sentry.setExtra((String)"os_arch", (String)"Unknown");
            Sentry.setExtra((String)"java_vendor", (String)"Unknown");
            Sentry.setExtra((String)"java_version", (String)"1.6");
        }
        User defaultUser = new User();
        defaultUser.setIpAddress("{{auto}}");
        Sentry.setUser((User)defaultUser);
        SplashScreen.init();
        SplashScreen.stage(0.0, "Retrieving client", "");
        try {
            boolean developerMode;
            RuntimeConfigLoader runtimeConfigLoader = new RuntimeConfigLoader(okHttpClient);
            ClientLoader clientLoader = new ClientLoader(okHttpClient, (ClientUpdateCheckMode)((Object)options.valueOf((OptionSpec)updateMode)), localAppletConfig);
            new Thread(() -> {
                clientLoader.get();
                ClassPreloader.preload();
            }, "Preloader").start();
            boolean bl = developerMode = options.has("developer-mode") && RuneLiteProperties.getLauncherVersion() == null;
            if (developerMode) {
                boolean assertions = false;
                if (!$assertionsDisabled) {
                    assertions = true;
                    if (!true) {
                        throw new AssertionError();
                    }
                }
                if (!assertions) {
                    SwingUtilities.invokeLater(() -> new FatalErrorDialog("Developers should enable assertions; Add `-ea` to your JVM arguments`").addHelpButtons().addBuildingGuide().open());
                    return;
                }
            }
            PROFILES_DIR.mkdirs();
            log.info("RuneLite {} (launcher version {}) starting up, args: {}", new Object[]{RuneLiteProperties.getVersion(), MoreObjects.firstNonNull((Object)RuneLiteProperties.getLauncherVersion(), (Object)"unknown"), args.length == 0 ? "none" : String.join((CharSequence)" ", args)});
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            log.info("Java VM arguments: {}", (Object)String.join((CharSequence)" ", runtime.getInputArguments()));
            long start = System.currentTimeMillis();
            injector = Guice.createInjector((Module[])new Module[]{new RuneLiteModule(okHttpClient, clientLoader, runtimeConfigLoader, developerMode, options.has("safe-mode"), true, options.has("vanilla"), (File)options.valueOf((OptionSpec)sessionfile), (File)options.valueOf((OptionSpec)configfile))});
            RuneLite runelite = (RuneLite)injector.getInstance(RuneLite.class);
            if (!options.has("vanilla")) {
                runelite.start();
            } else {
                runelite.startVanilla();
            }
            long end = System.currentTimeMillis();
            long uptime = runtime.getUptime();
            log.info("Client initialization took {}ms. Uptime: {}ms", (Object)(end - start), (Object)uptime);
        }
        catch (Exception e) {
            log.error("Failure during startup", (Throwable)e);
            SwingUtilities.invokeLater(() -> new FatalErrorDialog("RuneLite has encountered an unexpected error during startup.").addHelpButtons().open());
        }
        finally {
            SplashScreen.stop();
        }
    }

    public void start() throws Exception {
        boolean isOutdated;
        boolean bl = isOutdated = this.client == null;
        if (!isOutdated) {
            injector.injectMembers((Object)this.client);
        }
        this.setupSystemProps();
        if (this.applet != null) {
            RuneLite.copyJagexCache();
            this.applet.setSize(Constants.GAME_FIXED_SIZE);
            System.setProperty("jagex.disableBouncyCastle", "true");
            String oldHome = System.setProperty("user.home", RUNELITE_DIR.getAbsolutePath());
            try {
                this.applet.init();
            }
            finally {
                System.setProperty("user.home", oldHome);
            }
            this.applet.start();
        }
        SplashScreen.stage(0.57, null, "Loading configuration");
        this.configManager.load();
        this.sessionManager.loadSession();
        this.pluginManager.setOutdated(isOutdated);
        this.pluginManager.loadCorePlugins();
        this.pluginManager.loadSideLoadPlugins();
        this.externalPluginManager.loadExternalPlugins();
        SplashScreen.stage(0.7, null, "Finalizing configuration");
        this.pluginManager.loadDefaultPluginConfiguration(null);
        this.clientSessionManager.start();
        this.eventBus.register(this.clientSessionManager);
        SplashScreen.stage(0.75, null, "Starting core interface");
        this.clientUI.init();
        this.discordService.init();
        this.eventBus.register(this.clientUI);
        this.eventBus.register(this.pluginManager);
        this.eventBus.register(this.externalPluginManager);
        this.eventBus.register(this.overlayManager);
        this.eventBus.register(this.configManager);
        this.eventBus.register(this.discordService);
        if (!isOutdated) {
            WidgetOverlay.createOverlays(this.overlayManager, this.client).forEach(this.overlayManager::add);
            this.overlayManager.add((Overlay)this.worldMapOverlay.get());
            this.overlayManager.add((Overlay)this.tooltipOverlay.get());
        }
        this.pluginManager.startPlugins();
        SplashScreen.stop();
        this.clientUI.show();
        if (this.telemetryClient != null) {
            this.telemetryClient.submitTelemetry();
        }
        ReflectUtil.queueInjectorAnnotationCacheInvalidation(injector);
        ReflectUtil.invalidateAnnotationCaches();
    }

    private void startVanilla() throws Exception {
        injector.injectMembers((Object)this.client);
        SplashScreen.stage(0.75, null, "Starting core interface");
        this.clientUI.init();
        SplashScreen.stop();
        this.clientUI.show();
    }

    @VisibleForTesting
    public static void setInjector(Injector injector) {
        RuneLite.injector = injector;
    }

    @VisibleForTesting
    static OkHttpClient buildHttpClient(boolean insecureSkipTlsVerification) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().pingInterval(30L, TimeUnit.SECONDS).addInterceptor(chain -> {
            Request request = chain.request();
            if (request.header("User-Agent") != null) {
                return chain.proceed(request);
            }
            Request userAgentRequest = request.newBuilder().header("User-Agent", USER_AGENT).build();
            return chain.proceed(userAgentRequest);
        }).cache(new Cache(new File(CACHE_DIR, "okhttp"), 0x1400000L)).addNetworkInterceptor(chain -> {
            Response res = chain.proceed(chain.request());
            if (res.code() >= 400 && "GET".equals(res.request().method())) {
                res = res.newBuilder().header("Cache-Control", "no-store").build();
            }
            return res;
        });
        if (insecureSkipTlsVerification || RuneLiteProperties.isInsecureSkipTlsVerification()) {
            RuneLite.setupInsecureTrustManager(builder);
        }
        return builder.build();
    }

    private static void setupInsecureTrustManager(OkHttpClient.Builder okHttpClientBuilder) {
        try {
            X509TrustManager trustManager = new X509TrustManager(){

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            okHttpClientBuilder.sslSocketFactory(sc.getSocketFactory(), trustManager);
        }
        catch (KeyManagementException | NoSuchAlgorithmException ex) {
            log.warn("unable to setup insecure trust manager", (Throwable)ex);
        }
    }

    private static void copyJagexCache() {
        Path from = Paths.get(System.getProperty("user.home"), "jagexcache");
        Path to = Paths.get(System.getProperty("user.home"), ".zaros", "jagexcache");
        if (Files.exists(to, new LinkOption[0]) || !Files.exists(from, new LinkOption[0])) {
            return;
        }
        log.info("Copying jagexcache from {} to {}", (Object)from, (Object)to);
        try (Stream<Path> stream = Files.walk(from, new FileVisitOption[0]);){
            stream.forEach(source -> {
                try {
                    Files.copy(source, to.resolve(from.relativize((Path)source)), StandardCopyOption.COPY_ATTRIBUTES);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (Exception e) {
            log.warn("unable to copy jagexcache", (Throwable)e);
        }
    }

    private void setupSystemProps() {
        if (this.runtimeConfig == null || this.runtimeConfig.getSysProps() == null) {
            return;
        }
        for (Map.Entry<String, String> entry : this.runtimeConfig.getSysProps().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            log.debug("Setting property {}={}", (Object)key, (Object)value);
            System.setProperty(key, value);
        }
    }

    public static Injector getInjector() {
        return injector;
    }

    private static class ConfigFileConverter
    implements ValueConverter<File> {
        private ConfigFileConverter() {
        }

        public File convert(String fileName) {
            File file = Paths.get(fileName, new String[0]).isAbsolute() || fileName.startsWith("./") || fileName.startsWith(".\\") ? new File(fileName) : new File(RUNELITE_DIR, fileName);
            if (!(!file.exists() || file.isFile() && file.canWrite())) {
                throw new ValueConversionException(String.format("File %s is not accessible", file.getAbsolutePath()));
            }
            return file;
        }

        public Class<? extends File> valueType() {
            return File.class;
        }

        public String valuePattern() {
            return null;
        }
    }
}

