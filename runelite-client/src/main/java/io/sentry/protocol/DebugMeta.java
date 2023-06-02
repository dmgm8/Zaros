/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.protocol.DebugImage;
import io.sentry.protocol.SdkInfo;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

public final class DebugMeta
implements IUnknownPropertiesConsumer {
    private SdkInfo sdkInfo;
    private List<DebugImage> images;
    private Map<String, Object> unknown;

    public List<DebugImage> getImages() {
        return this.images;
    }

    public void setImages(List<DebugImage> images) {
        this.images = images;
    }

    public SdkInfo getSdkInfo() {
        return this.sdkInfo;
    }

    public void setSdkInfo(SdkInfo sdkInfo) {
        this.sdkInfo = sdkInfo;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}

