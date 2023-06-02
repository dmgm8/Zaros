/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public final class Browser
implements IUnknownPropertiesConsumer,
Cloneable {
    public static final String TYPE = "browser";
    private String name;
    private String version;
    private Map<String, Object> unknown;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @TestOnly
    Map<String, Object> getUnknown() {
        return this.unknown;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = new ConcurrentHashMap<String, Object>(unknown);
    }

    @NotNull
    public Browser clone() throws CloneNotSupportedException {
        Browser clone = (Browser)super.clone();
        clone.unknown = CollectionUtils.shallowCopy(this.unknown);
        return clone;
    }
}

