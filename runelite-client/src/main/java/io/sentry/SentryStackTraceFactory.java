/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry;

import io.sentry.protocol.SentryStackFrame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

final class SentryStackTraceFactory {
    @Nullable
    private final List<String> inAppExcludes;
    @Nullable
    private final List<String> inAppIncludes;

    public SentryStackTraceFactory(@Nullable List<String> inAppExcludes, @Nullable List<String> inAppIncludes) {
        this.inAppExcludes = inAppExcludes;
        this.inAppIncludes = inAppIncludes;
    }

    @Nullable
    List<SentryStackFrame> getStackFrames(@Nullable StackTraceElement[] elements) {
        ArrayList<SentryStackFrame> sentryStackFrames = null;
        if (elements != null && elements.length > 0) {
            sentryStackFrames = new ArrayList<SentryStackFrame>();
            for (StackTraceElement item : elements) {
                String className;
                if (item == null || (className = item.getClassName()).startsWith("io.sentry.")) continue;
                SentryStackFrame sentryStackFrame = new SentryStackFrame();
                sentryStackFrame.setInApp(this.isInApp(className));
                sentryStackFrame.setModule(className);
                sentryStackFrame.setFunction(item.getMethodName());
                sentryStackFrame.setFilename(item.getFileName());
                if (item.getLineNumber() >= 0) {
                    sentryStackFrame.setLineno(item.getLineNumber());
                }
                sentryStackFrame.setNative(item.isNativeMethod());
                sentryStackFrames.add(sentryStackFrame);
            }
            Collections.reverse(sentryStackFrames);
        }
        return sentryStackFrames;
    }

    @TestOnly
    boolean isInApp(@Nullable String className) {
        if (className == null || className.isEmpty()) {
            return true;
        }
        if (this.inAppIncludes != null) {
            for (String include : this.inAppIncludes) {
                if (!className.startsWith(include)) continue;
                return true;
            }
        }
        if (this.inAppExcludes != null) {
            for (String exclude : this.inAppExcludes) {
                if (!className.startsWith(exclude)) continue;
                return false;
            }
        }
        return false;
    }
}

