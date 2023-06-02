/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.util;

import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class StringUtils {
    private StringUtils() {
    }

    @Nullable
    public static String getStringAfterDot(@Nullable String str) {
        if (str != null) {
            int lastDotIndex = str.lastIndexOf(".");
            if (lastDotIndex >= 0 && str.length() > lastDotIndex + 1) {
                return str.substring(lastDotIndex + 1);
            }
            return str;
        }
        return null;
    }

    @Nullable
    public static String capitalize(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1).toLowerCase(Locale.ROOT);
    }

    @Nullable
    public static String removeSurrounding(@Nullable String str, @Nullable String delimiter) {
        if (str != null && delimiter != null && str.startsWith(delimiter) && str.endsWith(delimiter)) {
            return str.substring(delimiter.length(), str.length() - delimiter.length());
        }
        return str;
    }
}

