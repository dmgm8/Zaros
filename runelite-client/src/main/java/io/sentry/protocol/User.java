/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

public final class User
implements Cloneable,
IUnknownPropertiesConsumer {
    @Nullable
    private String email;
    @Nullable
    private String id;
    @Nullable
    private String username;
    @Nullable
    private String ipAddress;
    @Nullable
    private Map<String, String> other;
    @Nullable
    private Map<String, Object> unknown;

    @Nullable
    public String getEmail() {
        return this.email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getId() {
        return this.id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(@Nullable String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Nullable
    public Map<String, String> getOthers() {
        return this.other;
    }

    public void setOthers(@Nullable Map<String, String> other) {
        this.other = new ConcurrentHashMap<String, String>(other);
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = new ConcurrentHashMap<String, Object>(unknown);
    }

    @TestOnly
    Map<String, Object> getUnknown() {
        return this.unknown;
    }

    @NotNull
    public User clone() throws CloneNotSupportedException {
        User clone = (User)super.clone();
        clone.other = CollectionUtils.shallowCopy(this.other);
        clone.unknown = CollectionUtils.shallowCopy(this.unknown);
        return clone;
    }
}

