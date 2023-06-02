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
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import joptsimple.util.EnumConverter;
import net.runelite.api.Client;
import net.runelite.api.Constants;
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
        ArgumentAcceptingOptionSpec<File> sessionfile = parser.accepts("sessionfile", "Use a specified session file").withRequiredArg().withValuesConvertedBy(new ConfigFileConverter()).defaultsTo(DEFAULT_SESSION_FILE);
        ArgumentAcceptingOptionSpec<File> configfile = parser.accepts("config", "Use a specified config file").withRequiredArg().withValuesConvertedBy(new ConfigFileConverter()).defaultsTo(DEFAULT_CONFIG_FILE);
        ArgumentAcceptingOptionSpec<ClientUpdateCheckMode> updateMode = parser.accepts("rs", "Select client type").withRequiredArg().ofType(ClientUpdateCheckMode.class).defaultsTo(ClientUpdateCheckMode.RUNELITE).withValuesConvertedBy(new EnumConverter<>(ClientUpdateCheckMode.class) {
            public ClientUpdateCheckMode convert(String v) {
                return super.convert(v.toUpperCase());
            }
        });
        parser.accepts("help", "Show this text").forHelp();
        OptionSet options = parser.parse(args);
        if (options.has("help")) {
            parser.printHelpOn(System.out);
            System.exit(0);
        }
        if (options.has("debug")) {
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("ROOT");
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
        Sentry.init(sentryOptions -> sentryOptions.setDsn("https://18a94ce3f2fe42eb9000315fe2f72142@o480827.ingest.sentry.io/5528487"), true);
        try {
            Sentry.setExtra("os_arch", SystemUtils.OS_ARCH);
            Sentry.setExtra("java_vendor", System.getProperty("java.vendor"));
            Sentry.setExtra("java_version", System.getProperty("java.version"));
        }
        catch (Exception e) {
            Sentry.setExtra("os_arch", "Unknown");
            Sentry.setExtra("java_vendor", "Unknown");
            Sentry.setExtra("java_version", "1.6");
        }
        User defaultUser = new User();
        defaultUser.setIpAddress("{{auto}}");
        Sentry.setUser(defaultUser);
        SplashScreen.init();
        SplashScreen.stage(0.0, "Retrieving client", "");
        try {
            boolean developerMode;
            RuntimeConfigLoader runtimeConfigLoader = new RuntimeConfigLoader(okHttpClient);
            ClientLoader clientLoader = new ClientLoader(okHttpClient, options.valueOf(updateMode), localAppletConfig);
            new Thread(() -> {
                clientLoader.get();
                ClassPreloader.preload();
            }, "Preloader").start();
            boolean bl = developerMode = options.has("developer-mode") && RuneLiteProperties.getLauncherVersion() == null;
            if (developerMode) {
                    return;
                }
            PROFILES_DIR.mkdirs();
            log.info("RuneLite {} (launcher version {}) starting up, args: {}", RuneLiteProperties.getVersion(), MoreObjects.firstNonNull(RuneLiteProperties.getLauncherVersion(), "unknown"), args.length == 0 ? "none" : String.join(" ", args));
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            log.info("Java VM arguments: {}", String.join(" ", runtime.getInputArguments()));
            long start = System.currentTimeMillis();
            injector = Guice.createInjector(new RuneLiteModule(okHttpClient, clientLoader, runtimeConfigLoader, developerMode, options.has("safe-mode"), true, options.has("vanilla"), options.valueOf(sessionfile), options.valueOf(configfile)));
            RuneLite runelite = injector.getInstance(RuneLite.class);
            if (options.has("vanilla")) {
                runelite.startVanilla();
            } else {
                runelite.start();
            }
            long end = System.currentTimeMillis();
            long uptime = runtime.getUptime();
            log.info("Client initialization took {}ms. Uptime: {}ms", end - start, uptime);
        }
        catch (Exception e) {
            log.error("Failure during startup", e);
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
            injector.injectMembers(this.client);
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
            this.overlayManager.add(this.worldMapOverlay.get());
            this.overlayManager.add(this.tooltipOverlay.get());
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
        injector.injectMembers(this.client);
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
            log.warn("unable to setup insecure trust manager", ex);
        }
    }

    private static void copyJagexCache() {
        Path from = Paths.get(System.getProperty("user.home"), "jagexcache");
        Path to = Paths.get(System.getProperty("user.home"), ".zaros", "jagexcache");
        if (Files.exists(to) || !Files.exists(from)) {
            return;
        }
        log.info("Copying jagexcache from {} to {}", from, to);
        try (Stream<Path> stream = Files.walk(from)){
            stream.forEach(source -> {
                try {
                    Files.copy(source, to.resolve(from.relativize(source)), StandardCopyOption.COPY_ATTRIBUTES);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (Exception e) {
            log.warn("unable to copy jagexcache", e);
        }
    }

    private void setupSystemProps() {
        if (this.runtimeConfig == null || this.runtimeConfig.getSysProps() == null) {
            return;
        }
        for (Map.Entry<String, String> entry : this.runtimeConfig.getSysProps().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            log.debug("Setting property {}={}", key, value);
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
            File file = Paths.get(fileName).isAbsolute() || fileName.startsWith("./") || fileName.startsWith(".\\") ? new File(fileName) : new File(RUNELITE_DIR, fileName);
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
