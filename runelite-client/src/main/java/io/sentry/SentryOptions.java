/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.Breadcrumb;
import io.sentry.DiagnosticLogger;
import io.sentry.DuplicateEventDetectionEventProcessor;
import io.sentry.EnvelopeReader;
import io.sentry.EventProcessor;
import io.sentry.IEnvelopeReader;
import io.sentry.ILogger;
import io.sentry.IScopeObserver;
import io.sentry.ISentryExecutorService;
import io.sentry.ISerializer;
import io.sentry.Integration;
import io.sentry.MainEventProcessor;
import io.sentry.NoOpEnvelopeReader;
import io.sentry.NoOpLogger;
import io.sentry.NoOpSerializer;
import io.sentry.SentryEvent;
import io.sentry.SentryExecutorService;
import io.sentry.SentryLevel;
import io.sentry.ShutdownHookIntegration;
import io.sentry.UncaughtExceptionHandlerIntegration;
import io.sentry.cache.IEnvelopeCache;
import io.sentry.config.PropertiesProvider;
import io.sentry.protocol.SdkVersion;
import io.sentry.transport.ITransport;
import io.sentry.transport.ITransportGate;
import io.sentry.transport.NoOpEnvelopeCache;
import io.sentry.transport.NoOpTransport;
import io.sentry.transport.NoOpTransportGate;
import java.io.File;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SentryOptions {
    static final SentryLevel DEFAULT_DIAGNOSTIC_LEVEL = SentryLevel.DEBUG;
    @NotNull
    private final List<EventProcessor> eventProcessors = new CopyOnWriteArrayList<EventProcessor>();
    @NotNull
    private final List<Integration> integrations = new CopyOnWriteArrayList<Integration>();
    @Nullable
    private String dsn;
    private long shutdownTimeout = 2000L;
    private long flushTimeoutMillis = 15000L;
    private boolean debug;
    private boolean enableNdk = true;
    @NotNull
    private ILogger logger = NoOpLogger.getInstance();
    @NotNull
    private SentryLevel diagnosticLevel = DEFAULT_DIAGNOSTIC_LEVEL;
    @NotNull
    private ISerializer serializer = NoOpSerializer.getInstance();
    @NotNull
    private IEnvelopeReader envelopeReader = new EnvelopeReader();
    @Nullable
    private String sentryClientName;
    @Nullable
    private BeforeSendCallback beforeSend;
    @Nullable
    private BeforeBreadcrumbCallback beforeBreadcrumb;
    @Nullable
    private String cacheDirPath;
    private int cacheDirSize;
    private int maxQueueSize = this.cacheDirSize = 10;
    private int maxBreadcrumbs = 100;
    @Nullable
    private String release;
    @Nullable
    private String environment;
    @Nullable
    private Proxy proxy;
    @Nullable
    private Double sampleRate;
    @NotNull
    private final List<String> inAppExcludes = new CopyOnWriteArrayList<String>();
    @NotNull
    private final List<String> inAppIncludes = new CopyOnWriteArrayList<String>();
    @NotNull
    private ITransport transport = NoOpTransport.getInstance();
    @NotNull
    private ITransportGate transportGate = NoOpTransportGate.getInstance();
    @Nullable
    private String dist;
    private boolean attachThreads;
    private boolean attachStacktrace = true;
    private boolean enableSessionTracking = true;
    private long sessionTrackingIntervalMillis = 30000L;
    private String distinctId;
    private String serverName;
    private boolean enableUncaughtExceptionHandler = true;
    @NotNull
    private ISentryExecutorService executorService;
    private int connectionTimeoutMillis = 5000;
    private int readTimeoutMillis = 5000;
    @NotNull
    private IEnvelopeCache envelopeDiskCache = NoOpEnvelopeCache.getInstance();
    @Nullable
    private SdkVersion sdkVersion;
    private boolean sendDefaultPii = false;
    @Nullable
    private HostnameVerifier hostnameVerifier;
    @Nullable
    private SSLSocketFactory sslSocketFactory;
    @NotNull
    private final List<IScopeObserver> observers = new ArrayList<IScopeObserver>();
    private boolean enableScopeSync;
    private boolean enableExternalConfiguration;

    @NotNull
    public static SentryOptions from(@NotNull PropertiesProvider propertiesProvider) {
        SentryOptions options = new SentryOptions();
        options.setDsn(propertiesProvider.getProperty("dsn"));
        options.setEnvironment(propertiesProvider.getProperty("environment"));
        options.setRelease(propertiesProvider.getProperty("release"));
        options.setDist(propertiesProvider.getProperty("dist"));
        options.setServerName(propertiesProvider.getProperty("servername"));
        return options;
    }

    public void addEventProcessor(@NotNull EventProcessor eventProcessor) {
        this.eventProcessors.add(eventProcessor);
    }

    @NotNull
    public List<EventProcessor> getEventProcessors() {
        return this.eventProcessors;
    }

    public void addIntegration(@NotNull Integration integration) {
        this.integrations.add(integration);
    }

    @NotNull
    public List<Integration> getIntegrations() {
        return this.integrations;
    }

    @Nullable
    public String getDsn() {
        return this.dsn;
    }

    public void setDsn(@Nullable String dsn) {
        this.dsn = dsn;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @NotNull
    public ILogger getLogger() {
        return this.logger;
    }

    public void setLogger(@Nullable ILogger logger) {
        this.logger = logger == null ? NoOpLogger.getInstance() : new DiagnosticLogger(this, logger);
    }

    @NotNull
    public SentryLevel getDiagnosticLevel() {
        return this.diagnosticLevel;
    }

    public void setDiagnosticLevel(@Nullable SentryLevel diagnosticLevel) {
        this.diagnosticLevel = diagnosticLevel != null ? diagnosticLevel : DEFAULT_DIAGNOSTIC_LEVEL;
    }

    @NotNull
    public ISerializer getSerializer() {
        return this.serializer;
    }

    public void setSerializer(@Nullable ISerializer serializer) {
        this.serializer = serializer != null ? serializer : NoOpSerializer.getInstance();
    }

    @NotNull
    public IEnvelopeReader getEnvelopeReader() {
        return this.envelopeReader;
    }

    public void setEnvelopeReader(@Nullable IEnvelopeReader envelopeReader) {
        this.envelopeReader = envelopeReader != null ? envelopeReader : NoOpEnvelopeReader.getInstance();
    }

    public boolean isEnableNdk() {
        return this.enableNdk;
    }

    public void setEnableNdk(boolean enableNdk) {
        this.enableNdk = enableNdk;
    }

    public long getShutdownTimeout() {
        return this.shutdownTimeout;
    }

    public void setShutdownTimeout(long shutdownTimeoutMillis) {
        this.shutdownTimeout = shutdownTimeoutMillis;
    }

    @Nullable
    public String getSentryClientName() {
        return this.sentryClientName;
    }

    public void setSentryClientName(@Nullable String sentryClientName) {
        this.sentryClientName = sentryClientName;
    }

    @Nullable
    public BeforeSendCallback getBeforeSend() {
        return this.beforeSend;
    }

    public void setBeforeSend(@Nullable BeforeSendCallback beforeSend) {
        this.beforeSend = beforeSend;
    }

    @Nullable
    public BeforeBreadcrumbCallback getBeforeBreadcrumb() {
        return this.beforeBreadcrumb;
    }

    public void setBeforeBreadcrumb(@Nullable BeforeBreadcrumbCallback beforeBreadcrumb) {
        this.beforeBreadcrumb = beforeBreadcrumb;
    }

    @Nullable
    public String getCacheDirPath() {
        return this.cacheDirPath;
    }

    @Nullable
    public String getOutboxPath() {
        if (this.cacheDirPath == null || this.cacheDirPath.isEmpty()) {
            return null;
        }
        return this.cacheDirPath + File.separator + "outbox";
    }

    public void setCacheDirPath(@Nullable String cacheDirPath) {
        this.cacheDirPath = cacheDirPath;
    }

    public int getCacheDirSize() {
        return this.cacheDirSize;
    }

    public void setCacheDirSize(int cacheDirSize) {
        this.cacheDirSize = cacheDirSize;
    }

    public int getMaxBreadcrumbs() {
        return this.maxBreadcrumbs;
    }

    public void setMaxBreadcrumbs(int maxBreadcrumbs) {
        this.maxBreadcrumbs = maxBreadcrumbs;
    }

    @Nullable
    public String getRelease() {
        return this.release;
    }

    public void setRelease(@Nullable String release) {
        this.release = release;
    }

    @Nullable
    public String getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(@Nullable String environment) {
        this.environment = environment;
    }

    @Nullable
    public Proxy getProxy() {
        return this.proxy;
    }

    public void setProxy(@Nullable Proxy proxy) {
        this.proxy = proxy;
    }

    @Nullable
    public Double getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(Double sampleRate) {
        if (sampleRate != null && (sampleRate > 1.0 || sampleRate <= 0.0)) {
            throw new IllegalArgumentException("The value " + sampleRate + " is not valid. Use null to disable or values between 0.01 (inclusive) and 1.0 (exclusive).");
        }
        this.sampleRate = sampleRate;
    }

    @NotNull
    public List<String> getInAppExcludes() {
        return this.inAppExcludes;
    }

    public void addInAppExclude(@NotNull String exclude) {
        this.inAppExcludes.add(exclude);
    }

    @NotNull
    public List<String> getInAppIncludes() {
        return this.inAppIncludes;
    }

    public void addInAppInclude(@NotNull String include) {
        this.inAppIncludes.add(include);
    }

    @NotNull
    public ITransport getTransport() {
        return this.transport;
    }

    public void setTransport(@Nullable ITransport transport) {
        this.transport = transport != null ? transport : NoOpTransport.getInstance();
    }

    @Nullable
    public String getDist() {
        return this.dist;
    }

    public void setDist(@Nullable String dist) {
        this.dist = dist;
    }

    @NotNull
    public ITransportGate getTransportGate() {
        return this.transportGate;
    }

    public void setTransportGate(@Nullable ITransportGate transportGate) {
        this.transportGate = transportGate != null ? transportGate : NoOpTransportGate.getInstance();
    }

    public boolean isAttachStacktrace() {
        return this.attachStacktrace;
    }

    public void setAttachStacktrace(boolean attachStacktrace) {
        this.attachStacktrace = attachStacktrace;
    }

    public boolean isAttachThreads() {
        return this.attachThreads;
    }

    public void setAttachThreads(boolean attachThreads) {
        this.attachThreads = attachThreads;
    }

    public boolean isEnableSessionTracking() {
        return this.enableSessionTracking;
    }

    public void setEnableSessionTracking(boolean enableSessionTracking) {
        this.enableSessionTracking = enableSessionTracking;
    }

    @Nullable
    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(@Nullable String serverName) {
        this.serverName = serverName;
    }

    public long getSessionTrackingIntervalMillis() {
        return this.sessionTrackingIntervalMillis;
    }

    public void setSessionTrackingIntervalMillis(long sessionTrackingIntervalMillis) {
        this.sessionTrackingIntervalMillis = sessionTrackingIntervalMillis;
    }

    @ApiStatus.Internal
    public String getDistinctId() {
        return this.distinctId;
    }

    @ApiStatus.Internal
    public void setDistinctId(String distinctId) {
        this.distinctId = distinctId;
    }

    public long getFlushTimeoutMillis() {
        return this.flushTimeoutMillis;
    }

    public void setFlushTimeoutMillis(long flushTimeoutMillis) {
        this.flushTimeoutMillis = flushTimeoutMillis;
    }

    public boolean isEnableUncaughtExceptionHandler() {
        return this.enableUncaughtExceptionHandler;
    }

    public void setEnableUncaughtExceptionHandler(boolean enableUncaughtExceptionHandler) {
        this.enableUncaughtExceptionHandler = enableUncaughtExceptionHandler;
    }

    @NotNull
    ISentryExecutorService getExecutorService() {
        return this.executorService;
    }

    void setExecutorService(@NotNull ISentryExecutorService executorService) {
        if (executorService != null) {
            this.executorService = executorService;
        }
    }

    public int getConnectionTimeoutMillis() {
        return this.connectionTimeoutMillis;
    }

    public void setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return this.readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    @NotNull
    public IEnvelopeCache getEnvelopeDiskCache() {
        return this.envelopeDiskCache;
    }

    public void setEnvelopeDiskCache(@Nullable IEnvelopeCache envelopeDiskCache) {
        this.envelopeDiskCache = envelopeDiskCache != null ? envelopeDiskCache : NoOpEnvelopeCache.getInstance();
    }

    public int getMaxQueueSize() {
        return this.maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        if (maxQueueSize > 0) {
            this.maxQueueSize = maxQueueSize;
        }
    }

    @Nullable
    public SdkVersion getSdkVersion() {
        return this.sdkVersion;
    }

    @Nullable
    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public void setSslSocketFactory(@Nullable SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    @Nullable
    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public void setHostnameVerifier(@Nullable HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    @ApiStatus.Internal
    public void setSdkVersion(@Nullable SdkVersion sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public boolean isSendDefaultPii() {
        return this.sendDefaultPii;
    }

    public void setSendDefaultPii(boolean sendDefaultPii) {
        this.sendDefaultPii = sendDefaultPii;
    }

    public void addScopeObserver(@NotNull IScopeObserver observer) {
        this.observers.add(observer);
    }

    @NotNull
    List<IScopeObserver> getScopeObservers() {
        return this.observers;
    }

    public boolean isEnableScopeSync() {
        return this.enableScopeSync;
    }

    public void setEnableScopeSync(boolean enableScopeSync) {
        this.enableScopeSync = enableScopeSync;
    }

    public boolean isEnableExternalConfiguration() {
        return this.enableExternalConfiguration;
    }

    public void setEnableExternalConfiguration(boolean enableExternalConfiguration) {
        this.enableExternalConfiguration = enableExternalConfiguration;
    }

    public SentryOptions() {
        this.executorService = new SentryExecutorService();
        this.integrations.add(new UncaughtExceptionHandlerIntegration());
        this.integrations.add(new ShutdownHookIntegration());
        this.eventProcessors.add(new MainEventProcessor(this));
        this.eventProcessors.add(new DuplicateEventDetectionEventProcessor(this));
        this.setSentryClientName("sentry.java/3.1.0");
        this.setSdkVersion(this.createSdkVersion());
    }

    void merge(@NotNull SentryOptions options) {
        if (options.getDsn() != null) {
            this.setDsn(options.getDsn());
        }
        if (options.getEnvironment() != null) {
            this.setEnvironment(options.getEnvironment());
        }
        if (options.getRelease() != null) {
            this.setRelease(options.getRelease());
        }
        if (options.getDist() != null) {
            this.setDist(options.getDist());
        }
        if (options.getServerName() != null) {
            this.setServerName(options.getServerName());
        }
    }

    @NotNull
    private SdkVersion createSdkVersion() {
        SdkVersion sdkVersion = new SdkVersion();
        sdkVersion.setName("sentry.java");
        String version = "3.1.0";
        sdkVersion.setVersion(version);
        sdkVersion.addPackage("maven:sentry", version);
        return sdkVersion;
    }

    public static interface BeforeBreadcrumbCallback {
        @Nullable
        public Breadcrumb execute(@NotNull Breadcrumb var1, @Nullable Object var2);
    }

    public static interface BeforeSendCallback {
        @Nullable
        public SentryEvent execute(@NotNull SentryEvent var1, @Nullable Object var2);
    }
}

