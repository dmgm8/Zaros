/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SentryValues<T> {
    private final List<T> values;

    SentryValues(@Nullable List<T> values) {
        if (values == null) {
            values = new ArrayList<T>(0);
        }
        this.values = values;
    }

    @NotNull
    public List<T> getValues() {
        return this.values;
    }
}

