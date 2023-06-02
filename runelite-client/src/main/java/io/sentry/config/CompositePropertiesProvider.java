/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.config;

import io.sentry.config.PropertiesProvider;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class CompositePropertiesProvider
implements PropertiesProvider {
    @NotNull
    private final List<PropertiesProvider> providers;

    public CompositePropertiesProvider(@NotNull List<PropertiesProvider> providers) {
        this.providers = providers;
    }

    @Override
    @Nullable
    public String getProperty(@NotNull String property) {
        for (PropertiesProvider provider : this.providers) {
            String result = provider.getProperty(property);
            if (result == null) continue;
            return result;
        }
        return null;
    }
}

