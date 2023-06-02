/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package io.sentry;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.jetbrains.annotations.NotNull;

public final class SystemOutLogger
implements ILogger {
    @Override
    public void log(SentryLevel level, String message, Object ... args) {
        System.out.println(String.format("%s: %s", new Object[]{level, String.format(message, args)}));
    }

    @Override
    public void log(SentryLevel level, String message, Throwable throwable) {
        if (throwable == null) {
            this.log(level, message, new Object[0]);
        } else {
            System.out.println(String.format("%s: %s\n%s", new Object[]{level, String.format(message, throwable.toString()), this.captureStackTrace(throwable)}));
        }
    }

    @Override
    public void log(SentryLevel level, Throwable throwable, String message, Object ... args) {
        if (throwable == null) {
            this.log(level, message, args);
        } else {
            System.out.println(String.format("%s: %s \n %s\n%s", new Object[]{level, String.format(message, args), throwable.toString(), this.captureStackTrace(throwable)}));
        }
    }

    @NotNull
    private String captureStackTrace(@NotNull Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}

