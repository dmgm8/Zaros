/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.config;

import io.sentry.config.PropertiesProvider;
import io.sentry.util.StringUtils;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SimplePropertiesProvider
implements PropertiesProvider {
    @NotNull
    private final Properties properties;

    public SimplePropertiesProvider(@NotNull Properties properties) {
        this.properties = properties;
    }

    @Override
    @Nullable
    public String getProperty(@NotNull String property) {
        return StringUtils.removeSurrounding(this.properties.getProperty(property), "\"");
    }
}

