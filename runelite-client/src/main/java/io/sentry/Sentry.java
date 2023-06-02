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
import io.sentry.Dsn;
import io.sentry.GsonSerializer;
import io.sentry.Hub;
import io.sentry.HubAdapter;
import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.ISentryClient;
import io.sentry.Integration;
import io.sentry.NoOpHub;
import io.sentry.NoOpLogger;
import io.sentry.NoOpSerializer;
import io.sentry.OptionsContainer;
import io.sentry.ScopeCallback;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.SystemOutLogger;
import io.sentry.cache.EnvelopeCache;
import io.sentry.config.PropertiesProviderFactory;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Sentry {
    @NotNull
    private static final ThreadLocal<IHub> currentHub = new ThreadLocal();
    @NotNull
    private static volatile IHub mainHub = NoOpHub.getInstance();
    private static final boolean GLOBAL_HUB_DEFAULT_MODE = false;
    private static volatile boolean globalHubMode = false;

    private Sentry() {
    }

    @NotNull
    static IHub getCurrentHub() {
        if (globalHubMode) {
            return mainHub;
        }
        IHub hub = currentHub.get();
        if (hub == null) {
            hub = mainHub.clone();
            currentHub.set(hub);
        }
        return hub;
    }

    public static boolean isEnabled() {
        return Sentry.getCurrentHub().isEnabled();
    }

    public static void init() {
        Sentry.init((SentryOptions options) -> options.setEnableExternalConfiguration(true), false);
    }

    public static <T extends SentryOptions> void init(@NotNull OptionsContainer<T> clazz, @NotNull OptionsConfiguration<T> optionsConfiguration) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Sentry.init(clazz, optionsConfiguration, false);
    }

    public static <T extends SentryOptions> void init(@NotNull OptionsContainer<T> clazz, @NotNull OptionsConfiguration<T> optionsConfiguration, boolean globalHubMode) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        SentryOptions options = (SentryOptions)clazz.createInstance();
        optionsConfiguration.configure(options);
        Sentry.init(options, globalHubMode);
    }

    public static void init(@NotNull OptionsConfiguration<SentryOptions> optionsConfiguration) {
        Sentry.init(optionsConfiguration, false);
    }

    public static void init(@NotNull OptionsConfiguration<SentryOptions> optionsConfiguration, boolean globalHubMode) {
        SentryOptions options = new SentryOptions();
        optionsConfiguration.configure(options);
        Sentry.init(options, globalHubMode);
    }

    @ApiStatus.Internal
    public static void init(@NotNull SentryOptions options) {
        Sentry.init(options, false);
    }

    private static synchronized void init(@NotNull SentryOptions options, boolean globalHubMode) {
        if (Sentry.isEnabled()) {
            options.getLogger().log(SentryLevel.WARNING, "Sentry has been already initialized. Previous configuration will be overwritten.", new Object[0]);
        }
        if (!Sentry.initConfigurations(options)) {
            return;
        }
        options.getLogger().log(SentryLevel.INFO, "GlobalHubMode: '%s'", String.valueOf(globalHubMode));
        Sentry.globalHubMode = globalHubMode;
        IHub hub = Sentry.getCurrentHub();
        mainHub = new Hub(options);
        currentHub.set(mainHub);
        hub.close();
        for (Integration integration : options.getIntegrations()) {
            integration.register(HubAdapter.getInstance(), options);
        }
    }

    private static boolean initConfigurations(@NotNull SentryOptions options) {
        String dsn;
        if (options.isEnableExternalConfiguration()) {
            options.merge(SentryOptions.from(PropertiesProviderFactory.create()));
        }
        if ((dsn = options.getDsn()) == null) {
            throw new IllegalArgumentException("DSN is required. Use empty string to disable SDK.");
        }
        if (dsn.isEmpty()) {
            Sentry.close();
            return false;
        }
        Dsn parsedDsn = new Dsn(dsn);
        ILogger logger = options.getLogger();
        if (options.isDebug() && logger instanceof NoOpLogger) {
            options.setLogger(new SystemOutLogger());
            logger = options.getLogger();
        }
        logger.log(SentryLevel.INFO, "Initializing SDK with DSN: '%s'", options.getDsn());
        if (options.getSerializer() instanceof NoOpSerializer) {
            options.setSerializer(new GsonSerializer(logger, options.getEnvelopeReader()));
        }
        if (options.getCacheDirPath() != null && !options.getCacheDirPath().isEmpty()) {
            File cacheDir = new File(options.getCacheDirPath());
            cacheDir.mkdirs();
            File outboxDir = new File(options.getOutboxPath());
            outboxDir.mkdirs();
            options.setEnvelopeDiskCache(new EnvelopeCache(options));
        } else {
            logger.log(SentryLevel.INFO, "No outbox dir path is defined in options.", new Object[0]);
        }
        return true;
    }

    public static synchronized void close() {
        IHub hub = Sentry.getCurrentHub();
        mainHub = NoOpHub.getInstance();
        hub.close();
    }

    @NotNull
    public static SentryId captureEvent(@NotNull SentryEvent event) {
        return Sentry.getCurrentHub().captureEvent(event);
    }

    @NotNull
    public static SentryId captureEvent(@NotNull SentryEvent event, @Nullable Object hint) {
        return Sentry.getCurrentHub().captureEvent(event, hint);
    }

    @NotNull
    public static SentryId captureMessage(@NotNull String message) {
        return Sentry.getCurrentHub().captureMessage(message);
    }

    @NotNull
    public static SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level) {
        return Sentry.getCurrentHub().captureMessage(message, level);
    }

    @NotNull
    public static SentryId captureException(@NotNull Throwable throwable) {
        return Sentry.getCurrentHub().captureException(throwable);
    }

    @NotNull
    public static SentryId captureException(@NotNull Throwable throwable, @Nullable Object hint) {
        return Sentry.getCurrentHub().captureException(throwable, hint);
    }

    public static void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        Sentry.getCurrentHub().addBreadcrumb(breadcrumb, hint);
    }

    public static void addBreadcrumb(@NotNull Breadcrumb breadcrumb) {
        Sentry.getCurrentHub().addBreadcrumb(breadcrumb);
    }

    public static void addBreadcrumb(@NotNull String message) {
        Sentry.getCurrentHub().addBreadcrumb(message);
    }

    public static void addBreadcrumb(@NotNull String message, @NotNull String category) {
        Sentry.getCurrentHub().addBreadcrumb(message, category);
    }

    public static void setLevel(@Nullable SentryLevel level) {
        Sentry.getCurrentHub().setLevel(level);
    }

    public static void setTransaction(@Nullable String transaction) {
        Sentry.getCurrentHub().setTransaction(transaction);
    }

    public static void setUser(@Nullable User user) {
        Sentry.getCurrentHub().setUser(user);
    }

    public static void setFingerprint(@NotNull List<String> fingerprint) {
        Sentry.getCurrentHub().setFingerprint(fingerprint);
    }

    public static void clearBreadcrumbs() {
        Sentry.getCurrentHub().clearBreadcrumbs();
    }

    public static void setTag(@NotNull String key, @NotNull String value) {
        Sentry.getCurrentHub().setTag(key, value);
    }

    public static void removeTag(@NotNull String key) {
        Sentry.getCurrentHub().removeTag(key);
    }

    public static void setExtra(@NotNull String key, @NotNull String value) {
        Sentry.getCurrentHub().setExtra(key, value);
    }

    public static void removeExtra(@NotNull String key) {
        Sentry.getCurrentHub().removeExtra(key);
    }

    @NotNull
    public static SentryId getLastEventId() {
        return Sentry.getCurrentHub().getLastEventId();
    }

    public static void pushScope() {
        if (!globalHubMode) {
            Sentry.getCurrentHub().pushScope();
        }
    }

    public static void popScope() {
        if (!globalHubMode) {
            Sentry.getCurrentHub().popScope();
        }
    }

    public static void withScope(@NotNull ScopeCallback callback) {
        Sentry.getCurrentHub().withScope(callback);
    }

    public static void configureScope(@NotNull ScopeCallback callback) {
        Sentry.getCurrentHub().configureScope(callback);
    }

    public static void bindClient(@NotNull ISentryClient client) {
        Sentry.getCurrentHub().bindClient(client);
    }

    public static void flush(long timeoutMillis) {
        Sentry.getCurrentHub().flush(timeoutMillis);
    }

    public static void startSession() {
        Sentry.getCurrentHub().startSession();
    }

    public static void endSession() {
        Sentry.getCurrentHub().endSession();
    }

    public static interface OptionsConfiguration<T extends SentryOptions> {
        public void configure(@NotNull T var1);
    }
}

