/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry;

import io.sentry.SentryOptions;
import io.sentry.SentryStackTraceFactory;
import io.sentry.protocol.SentryStackFrame;
import io.sentry.protocol.SentryStackTrace;
import io.sentry.protocol.SentryThread;
import io.sentry.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

final class SentryThreadFactory {
    @NotNull
    private final SentryStackTraceFactory sentryStackTraceFactory;
    @NotNull
    private final SentryOptions options;

    public SentryThreadFactory(@NotNull SentryStackTraceFactory sentryStackTraceFactory, @NotNull SentryOptions options) {
        this.sentryStackTraceFactory = Objects.requireNonNull(sentryStackTraceFactory, "The SentryStackTraceFactory is required.");
        this.options = Objects.requireNonNull(options, "The SentryOptions is required");
    }

    @Nullable
    List<SentryThread> getCurrentThread(@Nullable List<Long> mechanismThreadIds) {
        HashMap<Thread, StackTraceElement[]> threads = new HashMap<Thread, StackTraceElement[]>();
        Thread currentThread = Thread.currentThread();
        threads.put(currentThread, currentThread.getStackTrace());
        return this.getCurrentThreads(threads, mechanismThreadIds);
    }

    @Nullable
    List<SentryThread> getCurrentThreads(@Nullable List<Long> mechanismThreadIds) {
        return this.getCurrentThreads(Thread.getAllStackTraces(), mechanismThreadIds);
    }

    @TestOnly
    @Nullable
    List<SentryThread> getCurrentThreads(@NotNull Map<Thread, StackTraceElement[]> threads, @Nullable List<Long> mechanismThreadIds) {
        ArrayList<SentryThread> result = null;
        Thread currentThread = Thread.currentThread();
        if (threads.size() > 0) {
            result = new ArrayList<SentryThread>();
            if (!threads.containsKey(currentThread)) {
                threads.put(currentThread, currentThread.getStackTrace());
            }
            for (Map.Entry<Thread, StackTraceElement[]> item : threads.entrySet()) {
                Thread thread = item.getKey();
                boolean crashed = thread == currentThread || mechanismThreadIds != null && mechanismThreadIds.contains(thread.getId());
                result.add(this.getSentryThread(crashed, item.getValue(), item.getKey()));
            }
        }
        return result;
    }

    @NotNull
    private SentryThread getSentryThread(boolean crashed, @NotNull StackTraceElement[] stackFramesElements, @NotNull Thread thread) {
        SentryThread sentryThread = new SentryThread();
        sentryThread.setName(thread.getName());
        sentryThread.setPriority(thread.getPriority());
        sentryThread.setId(thread.getId());
        sentryThread.setDaemon(thread.isDaemon());
        sentryThread.setState(thread.getState().name());
        sentryThread.setCrashed(crashed);
        List<SentryStackFrame> frames = this.sentryStackTraceFactory.getStackFrames(stackFramesElements);
        if (this.options.isAttachStacktrace() && frames != null && frames.size() > 0) {
            sentryThread.setStacktrace(new SentryStackTrace(frames));
        }
        return sentryThread;
    }
}

