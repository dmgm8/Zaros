/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.protocol.Mechanism;
import io.sentry.protocol.SentryStackTrace;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

public final class SentryException
implements IUnknownPropertiesConsumer {
    private String type;
    private String value;
    private String module;
    private Long threadId;
    private SentryStackTrace stacktrace;
    private Mechanism mechanism;
    private Map<String, Object> unknown;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getModule() {
        return this.module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Long getThreadId() {
        return this.threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public SentryStackTrace getStacktrace() {
        return this.stacktrace;
    }

    public void setStacktrace(SentryStackTrace stacktrace) {
        this.stacktrace = stacktrace;
    }

    public Mechanism getMechanism() {
        return this.mechanism;
    }

    public void setMechanism(Mechanism mechanism) {
        this.mechanism = mechanism;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}

