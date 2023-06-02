/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class Mechanism
implements IUnknownPropertiesConsumer {
    private String type;
    private String description;
    private String helpLink;
    private Boolean handled;
    private Map<String, Object> meta;
    private Map<String, Object> data;
    private final transient Thread thread;
    private Boolean synthetic;
    private Map<String, Object> unknown;

    public Mechanism() {
        this(null);
    }

    public Mechanism(@Nullable Thread thread) {
        this.thread = thread;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelpLink() {
        return this.helpLink;
    }

    public void setHelpLink(String helpLink) {
        this.helpLink = helpLink;
    }

    public Boolean isHandled() {
        return this.handled;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }

    public Map<String, Object> getMeta() {
        return this.meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    Thread getThread() {
        return this.thread;
    }

    public Boolean getSynthetic() {
        return this.synthetic;
    }

    public void setSynthetic(Boolean synthetic) {
        this.synthetic = synthetic;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}

