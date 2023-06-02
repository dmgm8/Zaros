/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.util;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static int size(Iterable<?> data) {
        if (data instanceof Collection) {
            return ((Collection)data).size();
        }
        int counter = 0;
        for (Object ignored : data) {
            ++counter;
        }
        return counter;
    }

    @Nullable
    public static <K, V> Map<K, V> shallowCopy(@Nullable Map<K, V> map) {
        if (map != null) {
            return new ConcurrentHashMap<K, V>(map);
        }
        return null;
    }
}

