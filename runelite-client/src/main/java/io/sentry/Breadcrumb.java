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

import io.sentry.DateUtils;
import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.SentryLevel;
import io.sentry.util.CollectionUtils;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

public final class Breadcrumb
implements Cloneable,
IUnknownPropertiesConsumer {
    @Nullable
    private final Date timestamp;
    @Nullable
    private String message;
    @Nullable
    private String type;
    @NotNull
    private Map<String, Object> data = new ConcurrentHashMap<String, Object>();
    @Nullable
    private String category;
    @Nullable
    private SentryLevel level;
    @Nullable
    private Map<String, Object> unknown;

    Breadcrumb(@Nullable Date timestamp) {
        this.timestamp = timestamp;
    }

    @NotNull
    public static Breadcrumb http(@NotNull String url, @NotNull String method) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType("http");
        breadcrumb.setCategory("http");
        breadcrumb.setData("url", url);
        breadcrumb.setData("method", method.toUpperCase(Locale.getDefault()));
        return breadcrumb;
    }

    public Breadcrumb() {
        this(DateUtils.getCurrentDateTimeOrNull());
    }

    public Breadcrumb(@Nullable String message) {
        this();
        this.message = message;
    }

    @NotNull
    public Date getTimestamp() {
        return (Date)this.timestamp.clone();
    }

    @Nullable
    public String getMessage() {
        return this.message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public String getType() {
        return this.type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    @ApiStatus.Internal
    @NotNull
    public Map<String, Object> getData() {
        return this.data;
    }

    @Nullable
    public Object getData(@NotNull String key) {
        return this.data.get(key);
    }

    public void setData(@NotNull String key, @NotNull Object value) {
        this.data.put(key, value);
    }

    public void removeData(@NotNull String key) {
        this.data.remove(key);
    }

    @Nullable
    public String getCategory() {
        return this.category;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    @Nullable
    public SentryLevel getLevel() {
        return this.level;
    }

    public void setLevel(@Nullable SentryLevel level) {
        this.level = level;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(@Nullable Map<String, Object> unknown) {
        this.unknown = new ConcurrentHashMap<String, Object>(unknown);
    }

    @TestOnly
    @Nullable
    Map<String, Object> getUnknown() {
        return this.unknown;
    }

    @NotNull
    public Breadcrumb clone() throws CloneNotSupportedException {
        Breadcrumb clone = (Breadcrumb)super.clone();
        clone.data = CollectionUtils.shallowCopy(this.data);
        clone.unknown = CollectionUtils.shallowCopy(this.unknown);
        SentryLevel levelRef = this.level;
        clone.level = levelRef != null ? SentryLevel.valueOf(levelRef.name().toUpperCase(Locale.ROOT)) : null;
        return clone;
    }
}

