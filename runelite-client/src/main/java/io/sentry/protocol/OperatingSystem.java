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

public final class OperatingSystem
implements IUnknownPropertiesConsumer,
Cloneable {
    public static final String TYPE = "os";
    private String name;
    private String version;
    private String rawDescription;
    private String build;
    private String kernelVersion;
    private Boolean rooted;
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

    public String getRawDescription() {
        return this.rawDescription;
    }

    public void setRawDescription(String rawDescription) {
        this.rawDescription = rawDescription;
    }

    public String getBuild() {
        return this.build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getKernelVersion() {
        return this.kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    public Boolean isRooted() {
        return this.rooted;
    }

    public void setRooted(Boolean rooted) {
        this.rooted = rooted;
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
    public OperatingSystem clone() throws CloneNotSupportedException {
        OperatingSystem clone = (OperatingSystem)super.clone();
        clone.unknown = CollectionUtils.shallowCopy(this.unknown);
        return clone;
    }
}

