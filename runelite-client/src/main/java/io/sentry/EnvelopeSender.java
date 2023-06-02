/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.DirectoryProcessor;
import io.sentry.IEnvelopeSender;
import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.ISerializer;
import io.sentry.SentryLevel;
import io.sentry.util.Objects;
import java.io.File;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class EnvelopeSender
extends DirectoryProcessor
implements IEnvelopeSender {
    @NotNull
    private final IHub hub;
    @NotNull
    private final ISerializer serializer;
    @NotNull
    private final ILogger logger;

    public EnvelopeSender(@NotNull IHub hub, @NotNull ISerializer serializer, @NotNull ILogger logger, long flushTimeoutMillis) {
        super(logger, flushTimeoutMillis);
        this.hub = Objects.requireNonNull(hub, "Hub is required.");
        this.serializer = Objects.requireNonNull(serializer, "Serializer is required.");
        this.logger = Objects.requireNonNull(logger, "Logger is required.");
    }

    /*
     * Exception decompiling
     */
    @Override
    protected void processFile(@NotNull File file, @Nullable Object hint) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:406)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:481)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
    }

    @Override
    protected boolean isRelevantFileName(String fileName) {
        return fileName.endsWith(".envelope");
    }

    @Override
    public void processEnvelopeFile(@NotNull String path, @Nullable Object hint) {
        Objects.requireNonNull(path, "Path is required.");
        this.processFile(new File(path), hint);
    }

    private void safeDelete(File file, String errorMessageSuffix) {
        try {
            if (!file.delete()) {
                this.logger.log(SentryLevel.ERROR, "Failed to delete '%s' %s", file.getAbsolutePath(), errorMessageSuffix);
            }
        }
        catch (Exception e) {
            this.logger.log(SentryLevel.ERROR, e, "Failed to delete '%s' %s", file.getAbsolutePath(), errorMessageSuffix);
        }
    }
}

