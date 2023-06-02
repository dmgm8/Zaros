/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.config;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.config.PropertiesLoader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class FilesystemPropertiesLoader
implements PropertiesLoader {
    @NotNull
    private final String filePath;
    @NotNull
    private final ILogger logger;

    public FilesystemPropertiesLoader(@NotNull String filePath, @NotNull ILogger logger) {
        this.filePath = filePath;
        this.logger = logger;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    @Nullable
    public Properties load() {
        try {
            File f = new File(this.filePath);
            if (!f.isFile()) return null;
            if (!f.canRead()) return null;
            try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));){
                Properties properties2 = new Properties();
                properties2.load(is);
                Properties properties = properties2;
                return properties;
            }
        }
        catch (IOException e) {
            this.logger.log(SentryLevel.ERROR, e, "Failed to load Sentry configuration from file: %s", this.filePath);
            return null;
        }
    }
}

