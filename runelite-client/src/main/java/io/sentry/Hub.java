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
import io.sentry.IHub;
import io.sentry.ISentryClient;
import io.sentry.Integration;
import io.sentry.NoOpSentryClient;
import io.sentry.Scope;
import io.sentry.ScopeCallback;
import io.sentry.SentryClient;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.Session;
import io.sentry.hints.SessionEndHint;
import io.sentry.hints.SessionStartHint;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Hub
implements IHub {
    @NotNull
    private volatile SentryId lastEventId;
    @NotNull
    private final SentryOptions options;
    private volatile boolean isEnabled;
    @NotNull
    private final Deque<StackItem> stack = new LinkedBlockingDeque<StackItem>();

    public Hub(@NotNull SentryOptions options) {
        this(options, Hub.createRootStackItem(options));
    }

    private Hub(@NotNull SentryOptions options, @Nullable StackItem rootStackItem) {
        Hub.validateOptions(options);
        this.options = options;
        if (rootStackItem != null) {
            this.stack.push(rootStackItem);
        }
        this.lastEventId = SentryId.EMPTY_ID;
        this.isEnabled = true;
    }

    private static void validateOptions(@NotNull SentryOptions options) {
        Objects.requireNonNull(options, "SentryOptions is required.");
        if (options.getDsn() == null || options.getDsn().isEmpty()) {
            throw new IllegalArgumentException("Hub requires a DSN to be instantiated. Considering using the NoOpHub is no DSN is available.");
        }
    }

    private static StackItem createRootStackItem(@NotNull SentryOptions options) {
        Hub.validateOptions(options);
        Scope scope = new Scope(options);
        SentryClient client = new SentryClient(options);
        return new StackItem(client, scope);
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    @NotNull
    public SentryId captureEvent(@NotNull SentryEvent event, @Nullable Object hint) {
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureEvent' call is a no-op.", new Object[0]);
        } else if (event == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "captureEvent called with null parameter.", new Object[0]);
        } else {
            try {
                StackItem item = this.stack.peek();
                if (item != null) {
                    sentryId = item.client.captureEvent(event, item.scope, hint);
                } else {
                    this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when captureEvent", new Object[0]);
                }
            }
            catch (Exception e) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error while capturing event with id: " + event.getEventId(), e);
            }
        }
        this.lastEventId = sentryId;
        return sentryId;
    }

    @Override
    @NotNull
    public SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level) {
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureMessage' call is a no-op.", new Object[0]);
        } else if (message == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "captureMessage called with null parameter.", new Object[0]);
        } else {
            try {
                StackItem item = this.stack.peek();
                if (item != null) {
                    sentryId = item.client.captureMessage(message, level, item.scope);
                } else {
                    this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when captureMessage", new Object[0]);
                }
            }
            catch (Exception e) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error while capturing message: " + message, e);
            }
        }
        this.lastEventId = sentryId;
        return sentryId;
    }

    @Override
    @ApiStatus.Internal
    public SentryId captureEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        Objects.requireNonNull(envelope, "SentryEnvelope is required.");
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureEnvelope' call is a no-op.", new Object[0]);
        } else {
            try {
                StackItem item = this.stack.peek();
                if (item != null) {
                    sentryId = item.client.captureEnvelope(envelope, hint);
                } else {
                    this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when captureEnvelope", new Object[0]);
                }
            }
            catch (Exception e) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error while capturing envelope.", e);
            }
        }
        this.lastEventId = sentryId;
        return sentryId;
    }

    @Override
    @NotNull
    public SentryId captureException(@NotNull Throwable throwable, @Nullable Object hint) {
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureException' call is a no-op.", new Object[0]);
        } else if (throwable == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "captureException called with null parameter.", new Object[0]);
        } else {
            try {
                StackItem item = this.stack.peek();
                if (item != null) {
                    sentryId = item.client.captureException(throwable, item.scope, hint);
                } else {
                    this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when captureException", new Object[0]);
                }
            }
            catch (Exception e) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error while capturing exception: " + throwable.getMessage(), e);
            }
        }
        this.lastEventId = sentryId;
        return sentryId;
    }

    @Override
    public void startSession() {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'startSession' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                Scope.SessionPair pair = item.scope.startSession();
                if (pair.getPrevious() != null) {
                    item.client.captureSession(pair.getPrevious(), new SessionEndHint());
                }
                item.client.captureSession(pair.getCurrent(), new SessionStartHint());
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when startSession", new Object[0]);
            }
        }
    }

    @Override
    public void endSession() {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'endSession' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                Session previousSession = item.scope.endSession();
                if (previousSession != null) {
                    item.client.captureSession(previousSession, new SessionEndHint());
                }
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when endSession", new Object[0]);
            }
        }
    }

    @Override
    public void close() {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'close' call is a no-op.", new Object[0]);
        } else {
            try {
                for (Integration integration : this.options.getIntegrations()) {
                    if (!(integration instanceof Closeable)) continue;
                    ((Closeable)((Object)integration)).close();
                }
                this.options.getExecutorService().close(this.options.getShutdownTimeout());
                StackItem item = this.stack.peek();
                if (item != null) {
                    item.client.close();
                } else {
                    this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was NULL when closing Hub", new Object[0]);
                }
            }
            catch (Exception e) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error while closing the Hub.", e);
            }
            this.isEnabled = false;
        }
    }

    @Override
    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'addBreadcrumb' call is a no-op.", new Object[0]);
        } else if (breadcrumb == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "addBreadcrumb called with null parameter.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.addBreadcrumb(breadcrumb, hint);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when addBreadcrumb", new Object[0]);
            }
        }
    }

    @Override
    public void setLevel(@Nullable SentryLevel level) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setLevel' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.setLevel(level);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when setLevel", new Object[0]);
            }
        }
    }

    @Override
    public void setTransaction(@Nullable String transaction) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setTransaction' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.setTransaction(transaction);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when setTransaction", new Object[0]);
            }
        }
    }

    @Override
    public void setUser(@Nullable User user) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setUser' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.setUser(user);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when setUser", new Object[0]);
            }
        }
    }

    @Override
    public void setFingerprint(@NotNull List<String> fingerprint) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setFingerprint' call is a no-op.", new Object[0]);
        } else if (fingerprint == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "setFingerprint called with null parameter.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.setFingerprint(fingerprint);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when setFingerprint", new Object[0]);
            }
        }
    }

    @Override
    public void clearBreadcrumbs() {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'clearBreadcrumbs' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.clearBreadcrumbs();
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when clearBreadcrumbs", new Object[0]);
            }
        }
    }

    @Override
    public void setTag(@NotNull String key, @NotNull String value) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setTag' call is a no-op.", new Object[0]);
        } else if (key == null || value == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "setTag called with null parameter.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.setTag(key, value);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when setTag", new Object[0]);
            }
        }
    }

    @Override
    public void removeTag(@NotNull String key) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'removeTag' call is a no-op.", new Object[0]);
        } else if (key == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "removeTag called with null parameter.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.removeTag(key);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when removeTag", new Object[0]);
            }
        }
    }

    @Override
    public void setExtra(@NotNull String key, @NotNull String value) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setExtra' call is a no-op.", new Object[0]);
        } else if (key == null || value == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "setExtra called with null parameter.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.setExtra(key, value);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when setExtra", new Object[0]);
            }
        }
    }

    @Override
    public void removeExtra(@NotNull String key) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'removeExtra' call is a no-op.", new Object[0]);
        } else if (key == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "removeExtra called with null parameter.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                item.scope.removeExtra(key);
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when removeExtra", new Object[0]);
            }
        }
    }

    @Override
    @NotNull
    public SentryId getLastEventId() {
        return this.lastEventId;
    }

    @Override
    public void pushScope() {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'addBreadcrumb' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                Scope clone = null;
                try {
                    clone = item.scope.clone();
                }
                catch (CloneNotSupportedException e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "An error has occurred when cloning a Scope", e);
                }
                if (clone != null) {
                    StackItem newItem = new StackItem(item.client, clone);
                    this.stack.push(newItem);
                }
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was NULL when pushScope", new Object[0]);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void popScope() {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'popScope' call is a no-op.", new Object[0]);
        } else {
            Deque<StackItem> deque = this.stack;
            synchronized (deque) {
                if (this.stack.size() != 1) {
                    this.stack.pop();
                } else {
                    this.options.getLogger().log(SentryLevel.WARNING, "Attempt to pop the root scope.", new Object[0]);
                }
            }
        }
    }

    @Override
    public void withScope(@NotNull ScopeCallback callback) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'withScope' call is a no-op.", new Object[0]);
        } else {
            this.pushScope();
            StackItem item = this.stack.peek();
            if (item != null) {
                try {
                    callback.run(item.scope);
                }
                catch (Exception e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Error in the 'withScope' callback.", e);
                }
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when withScope", new Object[0]);
            }
            this.popScope();
        }
    }

    @Override
    public void configureScope(@NotNull ScopeCallback callback) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'withScope' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                try {
                    callback.run(item.scope);
                }
                catch (Exception e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Error in the 'configureScope' callback.", e);
                }
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when configureScope", new Object[0]);
            }
        }
    }

    @Override
    public void bindClient(@NotNull ISentryClient client) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'bindClient' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                if (client != null) {
                    this.options.getLogger().log(SentryLevel.DEBUG, "New client bound to scope.", new Object[0]);
                    item.client = client;
                } else {
                    this.options.getLogger().log(SentryLevel.DEBUG, "NoOp client bound to scope.", new Object[0]);
                    item.client = NoOpSentryClient.getInstance();
                }
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when bindClient", new Object[0]);
            }
        }
    }

    @Override
    public void flush(long timeoutMillis) {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'flush' call is a no-op.", new Object[0]);
        } else {
            StackItem item = this.stack.peek();
            if (item != null) {
                try {
                    item.client.flush(timeoutMillis);
                }
                catch (Exception e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Error in the 'client.flush'.", e);
                }
            } else {
                this.options.getLogger().log(SentryLevel.FATAL, "Stack peek was null when flush", new Object[0]);
            }
        }
    }

    @Override
    @NotNull
    public IHub clone() {
        if (!this.isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Disabled Hub cloned.", new Object[0]);
        }
        Hub clone = new Hub(this.options, null);
        for (StackItem item : this.stack) {
            Scope clonedScope;
            try {
                clonedScope = item.scope.clone();
            }
            catch (CloneNotSupportedException e) {
                this.options.getLogger().log(SentryLevel.ERROR, "Clone not supported", new Object[0]);
                clonedScope = new Scope(this.options);
            }
            StackItem cloneItem = new StackItem(item.client, clonedScope);
            clone.stack.push(cloneItem);
        }
        return clone;
    }

    private static final class StackItem {
        @NotNull
        private volatile ISentryClient client;
        @NotNull
        private volatile Scope scope;

        StackItem(@NotNull ISentryClient client, @NotNull Scope scope) {
            this.client = Objects.requireNonNull(client, "ISentryClient is required.");
            this.scope = Objects.requireNonNull(scope, "Scope is required.");
        }
    }
}

