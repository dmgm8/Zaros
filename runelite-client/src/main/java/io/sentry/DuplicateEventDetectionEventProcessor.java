/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.EventProcessor;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.exception.ExceptionMechanismException;
import io.sentry.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DuplicateEventDetectionEventProcessor
implements EventProcessor {
    private final WeakHashMap<Throwable, Object> capturedObjects = new WeakHashMap();
    private final SentryOptions options;

    public DuplicateEventDetectionEventProcessor(@NotNull SentryOptions options) {
        this.options = Objects.requireNonNull(options, "options are required");
    }

    @Override
    public SentryEvent process(@NotNull SentryEvent event, @Nullable Object hint) {
        Throwable throwable = event.getThrowable();
        if (throwable != null) {
            if (throwable instanceof ExceptionMechanismException) {
                ExceptionMechanismException ex = (ExceptionMechanismException)throwable;
                if (this.capturedObjects.containsKey(ex.getThrowable())) {
                    this.options.getLogger().log(SentryLevel.DEBUG, "Duplicate Exception detected. Event %s will be discarded.", event.getEventId());
                    return null;
                }
                this.capturedObjects.put(ex.getThrowable(), null);
            } else {
                if (this.capturedObjects.containsKey(throwable) || DuplicateEventDetectionEventProcessor.containsAnyKey(this.capturedObjects, DuplicateEventDetectionEventProcessor.allCauses(throwable))) {
                    this.options.getLogger().log(SentryLevel.DEBUG, "Duplicate Exception detected. Event %s will be discarded.", event.getEventId());
                    return null;
                }
                this.capturedObjects.put(throwable, null);
            }
        }
        return event;
    }

    private static <T> boolean containsAnyKey(@NotNull Map<T, Object> map, @NotNull List<T> list) {
        for (T entry : list) {
            if (!map.containsKey(entry)) continue;
            return true;
        }
        return false;
    }

    @NotNull
    private static List<Throwable> allCauses(@NotNull Throwable throwable) {
        ArrayList<Throwable> causes = new ArrayList<Throwable>();
        Throwable ex = throwable;
        while (ex.getCause() != null) {
            causes.add(ex.getCause());
            ex = ex.getCause();
        }
        return causes;
    }
}

