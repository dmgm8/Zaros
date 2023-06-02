/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry;

import io.sentry.Breadcrumb;
import io.sentry.DateUtils;
import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.SentryLevel;
import io.sentry.SentryValues;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.DebugMeta;
import io.sentry.protocol.Message;
import io.sentry.protocol.Request;
import io.sentry.protocol.SdkVersion;
import io.sentry.protocol.SentryException;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryThread;
import io.sentry.protocol.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

public final class SentryEvent
implements IUnknownPropertiesConsumer {
    private SentryId eventId;
    private final Date timestamp;
    @Nullable
    private transient Throwable throwable;
    private Message message;
    private String serverName;
    private String platform;
    private String release;
    private String dist;
    private String logger;
    private SentryValues<SentryThread> threads;
    private SentryValues<SentryException> exception;
    private SentryLevel level;
    private String transaction;
    private String environment;
    private User user;
    private Request request;
    private SdkVersion sdk;
    private Contexts contexts = new Contexts();
    private List<String> fingerprint;
    private List<Breadcrumb> breadcrumbs;
    private Map<String, String> tags;
    private Map<String, Object> extra;
    private Map<String, Object> unknown;
    private Map<String, String> modules;
    private DebugMeta debugMeta;

    SentryEvent(SentryId eventId, Date timestamp) {
        this.eventId = eventId;
        this.timestamp = timestamp;
    }

    public SentryEvent(@Nullable Throwable throwable) {
        this();
        this.throwable = throwable;
    }

    public SentryEvent() {
        this(new SentryId(), DateUtils.getCurrentDateTimeOrNull());
    }

    @TestOnly
    public SentryEvent(Date timestamp) {
        this(new SentryId(), timestamp);
    }

    public SentryId getEventId() {
        return this.eventId;
    }

    public Date getTimestamp() {
        return (Date)this.timestamp.clone();
    }

    @Nullable
    public Throwable getThrowable() {
        return this.throwable;
    }

    public Message getMessage() {
        return this.message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRelease() {
        return this.release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getDist() {
        return this.dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getLogger() {
        return this.logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public List<SentryThread> getThreads() {
        if (this.threads != null) {
            return this.threads.getValues();
        }
        return null;
    }

    public void setThreads(List<SentryThread> threads) {
        this.threads = new SentryValues<SentryThread>(threads);
    }

    public List<SentryException> getExceptions() {
        return this.exception == null ? null : this.exception.getValues();
    }

    public void setExceptions(List<SentryException> exception) {
        this.exception = new SentryValues<SentryException>(exception);
    }

    public void setEventId(SentryId eventId) {
        this.eventId = eventId;
    }

    public void setThrowable(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }

    public SentryLevel getLevel() {
        return this.level;
    }

    public void setLevel(SentryLevel level) {
        this.level = level;
    }

    public String getTransaction() {
        return this.transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public SdkVersion getSdk() {
        return this.sdk;
    }

    public void setSdk(SdkVersion sdk) {
        this.sdk = sdk;
    }

    public List<String> getFingerprints() {
        return this.fingerprint;
    }

    public void setFingerprints(List<String> fingerprint) {
        this.fingerprint = fingerprint;
    }

    public List<Breadcrumb> getBreadcrumbs() {
        return this.breadcrumbs;
    }

    public void setBreadcrumbs(List<Breadcrumb> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public void addBreadcrumb(Breadcrumb breadcrumb) {
        if (this.breadcrumbs == null) {
            this.breadcrumbs = new ArrayList<Breadcrumb>();
        }
        this.breadcrumbs.add(breadcrumb);
    }

    public void addBreadcrumb(@Nullable String message) {
        this.addBreadcrumb(new Breadcrumb(message));
    }

    Map<String, String> getTags() {
        return this.tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public void removeTag(@NotNull String key) {
        if (this.tags != null) {
            this.tags.remove(key);
        }
    }

    @Nullable
    public String getTag(@NotNull String key) {
        if (this.tags != null) {
            return this.tags.get(key);
        }
        return null;
    }

    public void setTag(String key, String value) {
        if (this.tags == null) {
            this.tags = new HashMap<String, String>();
        }
        this.tags.put(key, value);
    }

    Map<String, Object> getExtras() {
        return this.extra;
    }

    public void setExtras(Map<String, Object> extra) {
        this.extra = extra;
    }

    public void setExtra(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<String, Object>();
        }
        this.extra.put(key, value);
    }

    public void removeExtra(@NotNull String key) {
        if (this.extra != null) {
            this.extra.remove(key);
        }
    }

    @Nullable
    public Object getExtra(@NotNull String key) {
        if (this.extra != null) {
            return this.extra.get(key);
        }
        return null;
    }

    public Contexts getContexts() {
        return this.contexts;
    }

    public void setContexts(Contexts contexts) {
        this.contexts = contexts;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }

    @TestOnly
    public Map<String, Object> getUnknown() {
        return this.unknown;
    }

    Map<String, String> getModules() {
        return this.modules;
    }

    public void setModules(Map<String, String> modules) {
        this.modules = modules;
    }

    public void setModule(String key, String value) {
        if (this.modules == null) {
            this.modules = new HashMap<String, String>();
        }
        this.modules.put(key, value);
    }

    public void removeModule(@NotNull String key) {
        if (this.modules != null) {
            this.modules.remove(key);
        }
    }

    @Nullable
    public String getModule(@NotNull String key) {
        if (this.modules != null) {
            return this.modules.get(key);
        }
        return null;
    }

    public DebugMeta getDebugMeta() {
        return this.debugMeta;
    }

    public void setDebugMeta(DebugMeta debugMeta) {
        this.debugMeta = debugMeta;
    }

    public boolean isCrashed() {
        if (this.exception != null) {
            for (SentryException e : this.exception.getValues()) {
                if (e.getMechanism() == null || e.getMechanism().isHandled() == null || e.getMechanism().isHandled().booleanValue()) continue;
                return true;
            }
        }
        return false;
    }

    public boolean isErrored() {
        return this.exception != null && !this.exception.getValues().isEmpty();
    }
}

