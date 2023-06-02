package net.runelite.client.externalplugins;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import net.runelite.client.RuneLite;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ExternalPluginsChanged;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.ui.SplashScreen;
import net.runelite.client.util.CountingInputStream;
import net.runelite.client.util.Text;
import net.runelite.client.util.VerificationException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ExternalPluginManager {
    private static final Logger log = LoggerFactory.getLogger(ExternalPluginManager.class);
    private static final String PLUGIN_LIST_KEY = "externalPlugins";
    private static Class<? extends Plugin>[] builtinExternals = null;
    @Inject
    @Named(value="safeMode")
    private boolean safeMode;
    private final ConfigManager configManager;
    private final ExternalPluginClient externalPluginClient;
    private final ScheduledExecutorService executor;
    private final PluginManager pluginManager;
    private final EventBus eventBus;
    private final OkHttpClient okHttpClient;

    @Inject
    private ExternalPluginManager(ConfigManager configManager, ExternalPluginClient externalPluginClient, ScheduledExecutorService executor, PluginManager pluginManager, EventBus eventBus, OkHttpClient okHttpClient) {
        this.configManager = configManager;
        this.externalPluginClient = externalPluginClient;
        this.executor = executor;
        this.pluginManager = pluginManager;
        this.eventBus = eventBus;
        this.okHttpClient = okHttpClient;
        executor.scheduleWithFixedDelay(() -> externalPluginClient.submitPlugins(this.getInstalledExternalPlugins()), new Random().nextInt(60), 180L, TimeUnit.MINUTES);
    }

    public void loadExternalPlugins() throws PluginInstantiationException {
        this.refreshPlugins();
        if (builtinExternals != null) {
            this.pluginManager.loadPlugins(Lists.newArrayList(builtinExternals), null);
        }
    }

    @Subscribe
    public void onSessionOpen(SessionOpen event) {
        this.executor.submit(this::refreshPlugins);
    }

    @Subscribe
    public void onSessionClose(SessionClose event) {
        this.executor.submit(this::refreshPlugins);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void refreshPlugins() {
        if (this.safeMode) {
            log.debug("External plugins are disabled in safe mode!");
            return;
        }
        HashSet<String> builtinExternalClasses = new HashSet<String>();
        if (builtinExternals != null) {
            for (Class<? extends Plugin> pluginClass : builtinExternals) {
                builtinExternalClasses.add(pluginClass.getName());
            }
        }
        HashMultimap<Object, Object> loadedExternalPlugins = HashMultimap.create();
        for (Plugin p : this.pluginManager.getPlugins()) {
            ExternalPluginManifest m = ExternalPluginManager.getExternalPluginManifest(p.getClass());
            if (m == null) continue;
            loadedExternalPlugins.put(m, p);
        }
        List<String> installedIDs = this.getInstalledExternalPlugins();
        if (installedIDs.isEmpty() && loadedExternalPlugins.isEmpty()) {
            return;
        }
        boolean startup = SplashScreen.isOpen();
        try {
            List<ExternalPluginManifest> manifestList;
            double splashLength;
            double splashStart = startup ? 0.6 : 0.0;
            double d = splashLength = startup ? 0.1 : 1.0;
            if (!startup) {
                SplashScreen.init();
            }
            Instant now = Instant.now();
            Instant keepAfter = now.minus(3L, ChronoUnit.DAYS);
            SplashScreen.stage(splashStart, null, "Downloading external plugins");
            HashSet<ExternalPluginManifest> externalPlugins = new HashSet<ExternalPluginManifest>();
            RuneLite.PLUGINS_DIR.mkdirs();
            try {
                manifestList = this.externalPluginClient.downloadManifest();
                Map<String, ExternalPluginManifest> manifests = manifestList.stream().collect(ImmutableMap.toImmutableMap(ExternalPluginManifest::getInternalName, Function.identity()));
                HashSet<ExternalPluginManifest> needsDownload = new HashSet<ExternalPluginManifest>();
                HashSet<File> keep = new HashSet<File>();
                for (String string : installedIDs) {
                    ExternalPluginManifest manifest = manifests.get(string);
                    if (manifest == null) continue;
                    if (Arrays.stream(manifest.getPlugins()).anyMatch(builtinExternalClasses::contains)) {
                        log.debug("Skipping loading [{}] from hub as a conflicting builtin external is present", manifest.getInternalName());
                        continue;
                    }
                    externalPlugins.add(manifest);
                    manifest.getJarFile().setLastModified(now.toEpochMilli());
                    if (!manifest.isValid()) {
                        needsDownload.add(manifest);
                        continue;
                    }
                    keep.add(manifest.getJarFile());
                }
                File[] files = RuneLite.PLUGINS_DIR.listFiles();
                if (files != null) {
                    for (File fi : files) {
                        if (keep.contains(fi) || fi.lastModified() >= keepAfter.toEpochMilli()) continue;
                        fi.delete();
                    }
                }
                int n = needsDownload.stream().mapToInt(ExternalPluginManifest::getSize).sum();
                int downloaded = 0;
                Iterator iterator = needsDownload.iterator();
                while (iterator.hasNext()) {
                    String[] manifest = (String[])iterator.next();
                    HttpUrl url = RuneLiteProperties.getPluginHubBase().newBuilder().addPathSegment(manifest.getInternalName()).addPathSegment(manifest.getCommit() + ".jar").build();
                    try {
                        Response res = this.okHttpClient.newCall(new Request.Builder().url(url).build()).execute();
                        try {
                            int fdownloaded = downloaded;
                            downloaded += manifest.getSize();
                            HashingInputStream his = new HashingInputStream(Hashing.sha256(), new CountingInputStream(res.body().byteStream(), arg_0 -> ExternalPluginManager.lambda$refreshPlugins$1(splashStart, splashLength, (ExternalPluginManifest)manifest, fdownloaded, n, arg_0)));
                            Files.asByteSink(manifest.getJarFile(), new FileWriteMode[0]).writeFrom(his);
                            if (his.hash().toString().equals(manifest.getHash())) continue;
                            throw new VerificationException("Plugin " + manifest.getInternalName() + " didn't match its hash");
                        }
                        finally {
                            if (res == null) continue;
                            res.close();
                        }
                    }
                    catch (IOException | VerificationException e) {
                        externalPlugins.remove(manifest);
                        log.error("Unable to download external plugin \"{}\"", (Object)manifest.getInternalName(), e);
                    }
                }
            }
            catch (IOException | VerificationException e) {
                log.error("Unable to download external plugins", e);
                if (!startup) {
                    SplashScreen.stop();
                }
                return;
            }
            SplashScreen.stage(splashStart + splashLength * 0.8, null, "Starting external plugins");
            HashSet<ExternalPluginManifest> add = new HashSet<ExternalPluginManifest>();
            for (ExternalPluginManifest ex : externalPlugins) {
                if (loadedExternalPlugins.removeAll(ex).size() > 0) continue;
                add.add(ex);
            }
            Collection<? extends Object> remove = loadedExternalPlugins.values();
            for (Plugin p : remove) {
                log.info("Stopping external plugin \"{}\"", p.getClass());
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        try {
                            this.pluginManager.stopPlugin(p);
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                catch (InterruptedException | InvocationTargetException exception) {
                    log.warn("Unable to stop external plugin \"{}\"", p.getClass().getName(), exception);
                }
                this.pluginManager.remove(p);
            }
            for (ExternalPluginManifest manifest : add) {
                if (!manifest.isValid()) {
                    log.warn("Invalid plugin for validated manifest: {}", manifest);
                    continue;
                }
                log.info("Loading external plugin \"{}\" version \"{}\" commit \"{}\"", manifest.getInternalName(), manifest.getVersion(), manifest.getCommit());
                List list = null;
                try {
                    List<Plugin> list2;
                    ExternalPluginClassLoader cl = new ExternalPluginClassLoader(manifest, new URL[]{manifest.getJarFile().toURI().toURL()});
                    ArrayList clazzes = new ArrayList();
                    for (String className : manifest.getPlugins()) {
                        clazzes.add(cl.loadClass(className));
                    }
                    List<Plugin> newPlugins2 = list2 = this.pluginManager.loadPlugins(clazzes, null);
                    if (startup) continue;
                    this.pluginManager.loadDefaultPluginConfiguration(list2);
                    SwingUtilities.invokeAndWait(() -> {
                        try {
                            for (Plugin p : newPlugins2) {
                                this.pluginManager.startPlugin(p);
                            }
                        }
                        catch (PluginInstantiationException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                catch (ThreadDeath e) {
                    throw e;
                }
                catch (Throwable e) {
                    log.warn("Unable to start or load external plugin \"{}\"", manifest.getInternalName(), e);
                    if (list == null) continue;
                    for (Plugin p : list) {
                        try {
                            SwingUtilities.invokeAndWait(() -> {
                                try {
                                    this.pluginManager.stopPlugin(p);
                                }
                                catch (Exception e2) {
                                    throw new RuntimeException(e2);
                                }
                            });
                        }
                        catch (InterruptedException | InvocationTargetException e2) {
                            log.info("Unable to fully stop plugin \"{}\"", manifest.getInternalName(), e2);
                        }
                        this.pluginManager.remove(p);
                    }
                }
            }
            if (!startup) {
                this.eventBus.post(new ExternalPluginsChanged(manifestList));
            }
        }
        finally {
            if (!startup) {
                SplashScreen.stop();
            }
        }
    }

    public List<String> getInstalledExternalPlugins() {
        String externalPluginsStr = this.configManager.getConfiguration("runelite", PLUGIN_LIST_KEY);
        return Text.fromCSV(externalPluginsStr == null ? "" : externalPluginsStr);
    }

    public void install(String key) {
        HashSet<String> plugins = new HashSet<String>(this.getInstalledExternalPlugins());
        if (plugins.add(key)) {
            this.configManager.setConfiguration("runelite", PLUGIN_LIST_KEY, Text.toCSV(plugins));
            this.executor.submit(this::refreshPlugins);
        }
    }

    public void remove(String key) {
        HashSet<String> plugins = new HashSet<String>(this.getInstalledExternalPlugins());
        if (plugins.remove(key)) {
            this.configManager.setConfiguration("runelite", PLUGIN_LIST_KEY, Text.toCSV(plugins));
            this.executor.submit(this::refreshPlugins);
        }
    }

    public void update() {
        this.executor.submit(this::refreshPlugins);
    }

    public static ExternalPluginManifest getExternalPluginManifest(Class<? extends Plugin> plugin) {
        ClassLoader cl = plugin.getClassLoader();
        if (cl instanceof ExternalPluginClassLoader) {
            ExternalPluginClassLoader ecl = (ExternalPluginClassLoader)cl;
            return ecl.getManifest();
        }
        return null;
    }

    public static void loadBuiltin(Class<? extends Plugin> ... plugins) {
        boolean assertsEnabled = false;
        if (!$assertionsDisabled) {
            assertsEnabled = true;
            if (false) {
                throw new AssertionError();
            }
        }
        if (!assertsEnabled) {
            throw new RuntimeException("Assertions are not enabled, add '-ea' to your VM options. Enabling assertions during development catches undefined behavior and incorrect API usage.");
        }
        builtinExternals = plugins;
    }

    private static void lambda$refreshPlugins$1(double splashStart, double splashLength, ExternalPluginManifest manifest, int fdownloaded, int toDownload, int i) {
        SplashScreen.stage(splashStart + splashLength * 0.2, splashStart + splashLength * 0.8, null, "Downloading " + manifest.getDisplayName(), i + fdownloaded, toDownload, true);
    }
}