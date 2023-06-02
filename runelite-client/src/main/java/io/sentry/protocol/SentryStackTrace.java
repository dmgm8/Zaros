/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.protocol.SentryStackFrame;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

public final class SentryStackTrace
implements IUnknownPropertiesConsumer {
    private List<SentryStackFrame> frames;
    private Map<String, String> registers;
    private Map<String, Object> unknown;

    public SentryStackTrace() {
    }

    public SentryStackTrace(List<SentryStackFrame> frames) {
        this.frames = frames;
    }

    public List<SentryStackFrame> getFrames() {
        return this.frames;
    }

    public void setFrames(List<SentryStackFrame> frames) {
        this.frames = frames;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }

    public Map<String, String> getRegisters() {
        return this.registers;
    }

    public void setRegisters(Map<String, String> registers) {
        this.registers = registers;
    }
}

