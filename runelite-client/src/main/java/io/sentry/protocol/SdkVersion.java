/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.protocol.SentryPackage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class SdkVersion
implements IUnknownPropertiesConsumer {
    private String name;
    private String version;
    private List<SentryPackage> packages;
    private List<String> integrations;
    private Map<String, Object> unknown;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPackage(String name, String version) {
        SentryPackage newPackage = new SentryPackage();
        newPackage.setName(name);
        newPackage.setVersion(version);
        if (this.packages == null) {
            this.packages = new ArrayList<SentryPackage>();
        }
        this.packages.add(newPackage);
    }

    public void addIntegration(String integration) {
        if (this.integrations == null) {
            this.integrations = new ArrayList<String>();
        }
        this.integrations.add(integration);
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }

    @Nullable
    public List<SentryPackage> getPackages() {
        return this.packages;
    }

    @Nullable
    public List<String> getIntegrations() {
        return this.integrations;
    }
}

