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
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class EnvironmentVariablePropertiesProvider
implements PropertiesProvider {
    private static final String PREFIX = "SENTRY";

    EnvironmentVariablePropertiesProvider() {
    }

    @Override
    @Nullable
    public String getProperty(@NotNull String property) {
        return StringUtils.removeSurrounding(System.getenv("SENTRY_" + property.replace(".", "_").toUpperCase(Locale.getDefault())), "\"");
    }
}

