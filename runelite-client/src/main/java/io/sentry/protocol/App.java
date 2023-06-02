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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public final class App
implements IUnknownPropertiesConsumer,
Cloneable {
    public static final String TYPE = "app";
    private String appIdentifier;
    private Date appStartTime;
    private String deviceAppHash;
    private String buildType;
    private String appName;
    private String appVersion;
    private String appBuild;
    private Map<String, Object> unknown;

    public String getAppIdentifier() {
        return this.appIdentifier;
    }

    public void setAppIdentifier(String appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

    public Date getAppStartTime() {
        Date appStartTimeRef = this.appStartTime;
        return appStartTimeRef != null ? (Date)appStartTimeRef.clone() : null;
    }

    public void setAppStartTime(Date appStartTime) {
        this.appStartTime = appStartTime;
    }

    public String getDeviceAppHash() {
        return this.deviceAppHash;
    }

    public void setDeviceAppHash(String deviceAppHash) {
        this.deviceAppHash = deviceAppHash;
    }

    public String getBuildType() {
        return this.buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppBuild() {
        return this.appBuild;
    }

    public void setAppBuild(String appBuild) {
        this.appBuild = appBuild;
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
    public App clone() throws CloneNotSupportedException {
        App clone = (App)super.clone();
        clone.unknown = CollectionUtils.shallowCopy(this.unknown);
        return clone;
    }
}

