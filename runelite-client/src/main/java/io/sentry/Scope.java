/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.Breadcrumb;
import io.sentry.CircularFifoQueue;
import io.sentry.EventProcessor;
import io.sentry.IScopeObserver;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.Session;
import io.sentry.SynchronizedQueue;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Scope
implements Cloneable {
    @Nullable
    private SentryLevel level;
    @Nullable
    private String transaction;
    @Nullable
    private User user;
    @NotNull
    private List<String> fingerprint = new ArrayList<String>();
    @NotNull
    private Queue<Breadcrumb> breadcrumbs;
    @NotNull
    private Map<String, String> tags = new ConcurrentHashMap<String, String>();
    @NotNull
    private Map<String, Object> extra = new ConcurrentHashMap<String, Object>();
    @NotNull
    private List<EventProcessor> eventProcessors = new CopyOnWriteArrayList<EventProcessor>();
    @NotNull
    private final SentryOptions options;
    @Nullable
    private volatile Session session;
    @NotNull
    private final Object sessionLock = new Object();
    @NotNull
    private Contexts contexts = new Contexts();

    public Scope(@NotNull SentryOptions options) {
        this.options = options;
        this.breadcrumbs = this.createBreadcrumbsList(options.getMaxBreadcrumbs());
    }

    @Nullable
    public SentryLevel getLevel() {
        return this.level;
    }

    public void setLevel(@Nullable SentryLevel level) {
        this.level = level;
    }

    @Nullable
    public String getTransaction() {
        return this.transaction;
    }

    public void setTransaction(@Nullable String transaction) {
        this.transaction = transaction;
    }

    @Nullable
    public User getUser() {
        return this.user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.setUser(user);
            }
        }
    }

    @NotNull
    List<String> getFingerprint() {
        return this.fingerprint;
    }

    public void setFingerprint(@NotNull List<String> fingerprint) {
        this.fingerprint = fingerprint;
    }

    @NotNull
    Queue<Breadcrumb> getBreadcrumbs() {
        return this.breadcrumbs;
    }

    @Nullable
    private Breadcrumb executeBeforeBreadcrumb(@NotNull SentryOptions.BeforeBreadcrumbCallback callback, @NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        try {
            breadcrumb = callback.execute(breadcrumb, hint);
        }
        catch (Exception e) {
            this.options.getLogger().log(SentryLevel.ERROR, "The BeforeBreadcrumbCallback callback threw an exception. It will be added as breadcrumb and continue.", e);
            breadcrumb.setData("sentry:message", e.getMessage());
        }
        return breadcrumb;
    }

    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        if (breadcrumb == null) {
            return;
        }
        SentryOptions.BeforeBreadcrumbCallback callback = this.options.getBeforeBreadcrumb();
        if (callback != null) {
            breadcrumb = this.executeBeforeBreadcrumb(callback, breadcrumb, hint);
        }
        if (breadcrumb != null) {
            this.breadcrumbs.add(breadcrumb);
            if (this.options.isEnableScopeSync()) {
                for (IScopeObserver observer : this.options.getScopeObservers()) {
                    observer.addBreadcrumb(breadcrumb);
                }
            }
        } else {
            this.options.getLogger().log(SentryLevel.INFO, "Breadcrumb was dropped by beforeBreadcrumb", new Object[0]);
        }
    }

    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb) {
        this.addBreadcrumb(breadcrumb, null);
    }

    public void clearBreadcrumbs() {
        this.breadcrumbs.clear();
    }

    public void clear() {
        this.level = null;
        this.transaction = null;
        this.user = null;
        this.fingerprint.clear();
        this.breadcrumbs.clear();
        this.tags.clear();
        this.extra.clear();
        this.eventProcessors.clear();
    }

    @NotNull
    Map<String, String> getTags() {
        return this.tags;
    }

    public void setTag(@NotNull String key, @NotNull String value) {
        this.tags.put(key, value);
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.setTag(key, value);
            }
        }
    }

    public void removeTag(@NotNull String key) {
        this.tags.remove(key);
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.removeTag(key);
            }
        }
    }

    @NotNull
    Map<String, Object> getExtras() {
        return this.extra;
    }

    public void setExtra(@NotNull String key, @NotNull String value) {
        this.extra.put(key, value);
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.setExtra(key, value);
            }
        }
    }

    public void removeExtra(@NotNull String key) {
        this.extra.remove(key);
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.removeExtra(key);
            }
        }
    }

    @NotNull
    public Contexts getContexts() {
        return this.contexts;
    }

    public void setContexts(@NotNull String key, @NotNull Object value) {
        this.contexts.put(key, value);
    }

    public void setContexts(@NotNull String key, @NotNull Boolean value) {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("value", value);
        this.setContexts(key, map);
    }

    public void setContexts(@NotNull String key, @NotNull String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("value", value);
        this.setContexts(key, map);
    }

    public void setContexts(@NotNull String key, @NotNull Number value) {
        HashMap<String, Number> map = new HashMap<String, Number>();
        map.put("value", value);
        this.setContexts(key, map);
    }

    public void removeContexts(@NotNull String key) {
        this.contexts.remove(key);
    }

    @NotNull
    private Queue<Breadcrumb> createBreadcrumbsList(int maxBreadcrumb) {
        return SynchronizedQueue.synchronizedQueue(new CircularFifoQueue(maxBreadcrumb));
    }

    @NotNull
    public Scope clone() throws CloneNotSupportedException {
        Scope clone = (Scope)super.clone();
        SentryLevel levelRef = this.level;
        clone.level = levelRef != null ? SentryLevel.valueOf(levelRef.name().toUpperCase(Locale.ROOT)) : null;
        User userRef = this.user;
        clone.user = userRef != null ? userRef.clone() : null;
        clone.fingerprint = new ArrayList<String>(this.fingerprint);
        clone.eventProcessors = new CopyOnWriteArrayList<EventProcessor>(this.eventProcessors);
        Queue<Breadcrumb> breadcrumbsRef = this.breadcrumbs;
        Queue<Breadcrumb> breadcrumbsClone = this.createBreadcrumbsList(this.options.getMaxBreadcrumbs());
        for (Breadcrumb item : breadcrumbsRef) {
            Breadcrumb breadcrumbClone = item.clone();
            breadcrumbsClone.add(breadcrumbClone);
        }
        clone.breadcrumbs = breadcrumbsClone;
        Map<String, String> tagsRef = this.tags;
        ConcurrentHashMap<String, String> tagsClone = new ConcurrentHashMap<String, String>();
        for (Map.Entry entry : tagsRef.entrySet()) {
            if (entry == null) continue;
            tagsClone.put((String)entry.getKey(), (String)entry.getValue());
        }
        clone.tags = tagsClone;
        Map<String, Object> extraRef = this.extra;
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<String, Object>();
        for (Map.Entry<String, Object> item : extraRef.entrySet()) {
            if (item == null) continue;
            concurrentHashMap.put(item.getKey(), item.getValue());
        }
        clone.extra = concurrentHashMap;
        clone.contexts = this.contexts.clone();
        return clone;
    }

    @NotNull
    List<EventProcessor> getEventProcessors() {
        return this.eventProcessors;
    }

    public void addEventProcessor(@NotNull EventProcessor eventProcessor) {
        this.eventProcessors.add(eventProcessor);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    Session withSession(@NotNull IWithSession sessionCallback) {
        Session cloneSession = null;
        Object object = this.sessionLock;
        synchronized (object) {
            sessionCallback.accept(this.session);
            if (this.session != null) {
                cloneSession = this.session.clone();
            }
        }
        return cloneSession;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    SessionPair startSession() {
        SessionPair pair;
        Object object = this.sessionLock;
        synchronized (object) {
            if (this.session != null) {
                this.session.end();
            }
            Session previousSession = this.session;
            this.session = new Session(this.options.getDistinctId(), this.user, this.options.getEnvironment(), this.options.getRelease());
            Session previousClone = previousSession != null ? previousSession.clone() : null;
            pair = new SessionPair(this.session.clone(), previousClone);
        }
        return pair;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    Session endSession() {
        Session previousSession = null;
        Object object = this.sessionLock;
        synchronized (object) {
            if (this.session != null) {
                this.session.end();
                previousSession = this.session.clone();
                this.session = null;
            }
        }
        return previousSession;
    }

    static final class SessionPair {
        @Nullable
        private final Session previous;
        @NotNull
        private final Session current;

        public SessionPair(@NotNull Session current, @Nullable Session previous) {
            this.current = current;
            this.previous = previous;
        }

        @Nullable
        public Session getPrevious() {
            return this.previous;
        }

        @NotNull
        public Session getCurrent() {
            return this.current;
        }
    }

    static interface IWithSession {
        public void accept(@Nullable Session var1);
    }
}

