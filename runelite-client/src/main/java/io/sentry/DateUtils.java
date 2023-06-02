/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class DateUtils {
    private static final String UTC = "UTC";
    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISO_FORMAT_WITH_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private DateUtils() {
    }

    @NotNull
    public static String getTimestampIsoFormat(@NotNull Date date) {
        TimeZone tz = TimeZone.getTimeZone(UTC);
        SimpleDateFormat df = new SimpleDateFormat(ISO_FORMAT_WITH_MILLIS, Locale.ROOT);
        df.setTimeZone(tz);
        return df.format(date);
    }

    @NotNull
    public static Date getCurrentDateTime() throws IllegalArgumentException {
        String timestampIsoFormat = DateUtils.getTimestampIsoFormat(new Date());
        return DateUtils.getDateTime(timestampIsoFormat);
    }

    @Nullable
    public static Date getCurrentDateTimeOrNull() throws IllegalArgumentException {
        try {
            return DateUtils.getCurrentDateTime();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    @NotNull
    public static Date getDateTime(@NotNull String timestamp) throws IllegalArgumentException {
        try {
            return new SimpleDateFormat(ISO_FORMAT_WITH_MILLIS, Locale.ROOT).parse(timestamp);
        }
        catch (ParseException e) {
            try {
                return new SimpleDateFormat(ISO_FORMAT, Locale.ROOT).parse(timestamp);
            }
            catch (ParseException parseException) {
                throw new IllegalArgumentException("timestamp is not ISO format " + timestamp);
            }
        }
    }

    @NotNull
    public static Date getDateTimeWithMillisPrecision(@NotNull String timestamp) throws IllegalArgumentException {
        try {
            String[] times = timestamp.split("\\.", -1);
            long seconds = Long.parseLong(times[0]);
            long millis = times.length > 1 ? Long.parseLong(times[1]) : 0L;
            return DateUtils.getDateTime(new Date(seconds * 1000L + millis));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("timestamp is not millis format " + timestamp);
        }
    }

    @NotNull
    public static String getTimestamp(@NotNull Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO_FORMAT_WITH_MILLIS, Locale.ROOT);
        return df.format(date);
    }

    @NotNull
    public static Date getDateTime(@NotNull Date date) throws IllegalArgumentException {
        String timestampIsoFormat = DateUtils.getTimestampIsoFormat(date);
        return DateUtils.getDateTime(timestampIsoFormat);
    }
}

