/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry;

import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface IUnknownPropertiesConsumer {
    public void acceptUnknownProperties(Map<String, Object> var1);
}

