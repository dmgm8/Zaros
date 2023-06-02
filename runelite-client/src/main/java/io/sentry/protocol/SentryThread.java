/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.protocol.SentryStackTrace;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

public final class SentryThread
implements IUnknownPropertiesConsumer {
    private Long id;
    private Integer priority;
    private String name;
    private String state;
    private Boolean crashed;
    private Boolean current;
    private Boolean daemon;
    private SentryStackTrace stacktrace;
    private Map<String, Object> unknown;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isCrashed() {
        return this.crashed;
    }

    public void setCrashed(Boolean crashed) {
        this.crashed = crashed;
    }

    public Boolean isCurrent() {
        return this.current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public SentryStackTrace getStacktrace() {
        return this.stacktrace;
    }

    public void setStacktrace(SentryStackTrace stacktrace) {
        this.stacktrace = stacktrace;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean isDaemon() {
        return this.daemon;
    }

    public void setDaemon(Boolean daemon) {
        this.daemon = daemon;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}

