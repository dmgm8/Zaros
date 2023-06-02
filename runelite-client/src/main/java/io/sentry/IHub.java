/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.Breadcrumb;
import io.sentry.ISentryClient;
import io.sentry.ScopeCallback;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IHub {
    public boolean isEnabled();

    public SentryId captureEvent(SentryEvent var1, @Nullable Object var2);

    default public SentryId captureEvent(SentryEvent event) {
        return this.captureEvent(event, null);
    }

    default public SentryId captureMessage(String message) {
        return this.captureMessage(message, SentryLevel.INFO);
    }

    public SentryId captureMessage(String var1, SentryLevel var2);

    public SentryId captureEnvelope(SentryEnvelope var1, @Nullable Object var2);

    default public SentryId captureEnvelope(SentryEnvelope envelope) {
        return this.captureEnvelope(envelope, null);
    }

    public SentryId captureException(Throwable var1, @Nullable Object var2);

    default public SentryId captureException(Throwable throwable) {
        return this.captureException(throwable, null);
    }

    public void startSession();

    public void endSession();

    public void close();

    public void addBreadcrumb(Breadcrumb var1, @Nullable Object var2);

    default public void addBreadcrumb(Breadcrumb breadcrumb) {
        this.addBreadcrumb(breadcrumb, null);
    }

    default public void addBreadcrumb(@NotNull String message) {
        this.addBreadcrumb(new Breadcrumb(message));
    }

    default public void addBreadcrumb(@NotNull String message, @NotNull String category) {
        Breadcrumb breadcrumb = new Breadcrumb(message);
        breadcrumb.setCategory(category);
        this.addBreadcrumb(breadcrumb);
    }

    public void setLevel(SentryLevel var1);

    public void setTransaction(String var1);

    public void setUser(User var1);

    public void setFingerprint(List<String> var1);

    public void clearBreadcrumbs();

    public void setTag(String var1, String var2);

    public void removeTag(String var1);

    public void setExtra(String var1, String var2);

    public void removeExtra(String var1);

    public SentryId getLastEventId();

    public void pushScope();

    public void popScope();

    public void withScope(ScopeCallback var1);

    public void configureScope(ScopeCallback var1);

    public void bindClient(ISentryClient var1);

    public void flush(long var1);

    public IHub clone();
}

