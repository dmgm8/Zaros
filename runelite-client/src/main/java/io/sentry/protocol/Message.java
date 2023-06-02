/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

public final class Message
implements IUnknownPropertiesConsumer {
    private String formatted;
    private String message;
    private List<String> params;
    private Map<String, Object> unknown;

    public String getFormatted() {
        return this.formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getParams() {
        return this.params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}

