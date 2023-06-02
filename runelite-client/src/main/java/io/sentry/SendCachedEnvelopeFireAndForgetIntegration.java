/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.DirectoryProcessor;
import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.util.Objects;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SendCachedEnvelopeFireAndForgetIntegration
implements Integration {
    private final SendFireAndForgetFactory factory;

    public SendCachedEnvelopeFireAndForgetIntegration(@NotNull SendFireAndForgetFactory factory) {
        this.factory = Objects.requireNonNull(factory, "SendFireAndForgetFactory is required");
    }

    @Override
    public final void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        Objects.requireNonNull(options, "SentryOptions is required");
        String cachedDir = options.getCacheDirPath();
        if (!this.factory.hasValidPath(cachedDir, options.getLogger())) {
            options.getLogger().log(SentryLevel.ERROR, "No cache dir path is defined in options.", new Object[0]);
            return;
        }
        SendFireAndForget sender = this.factory.create(hub, options);
        if (sender == null) {
            options.getLogger().log(SentryLevel.ERROR, "SendFireAndForget factory is null.", new Object[0]);
            return;
        }
        try {
            options.getExecutorService().submit(() -> {
                try {
                    sender.send();
                }
                catch (Exception e) {
                    options.getLogger().log(SentryLevel.ERROR, "Failed trying to send cached events.", e);
                }
            });
            options.getLogger().log(SentryLevel.DEBUG, "SendCachedEventFireAndForgetIntegration installed.", new Object[0]);
        }
        catch (Exception e) {
            options.getLogger().log(SentryLevel.ERROR, "Failed to call the executor. Cached events will not be sent", e);
        }
    }

    public static interface SendFireAndForgetFactory {
        @Nullable
        public SendFireAndForget create(IHub var1, SentryOptions var2);

        default public boolean hasValidPath(@Nullable String dirPath, @NotNull ILogger logger) {
            if (dirPath == null || dirPath.isEmpty()) {
                logger.log(SentryLevel.INFO, "No cached dir path is defined in options.", new Object[0]);
                return false;
            }
            return true;
        }

        @NotNull
        default public SendFireAndForget processDir(@NotNull DirectoryProcessor directoryProcessor, @NotNull String dirPath, @NotNull ILogger logger) {
            File dirFile = new File(dirPath);
            return () -> {
                logger.log(SentryLevel.DEBUG, "Started processing cached files from %s", dirPath);
                directoryProcessor.processDirectory(dirFile);
                logger.log(SentryLevel.DEBUG, "Finished processing cached files from %s", dirPath);
            };
        }
    }

    public static interface SendFireAndForgetDirPath {
        @Nullable
        public String getDirPath();
    }

    public static interface SendFireAndForget {
        public void send();
    }
}

