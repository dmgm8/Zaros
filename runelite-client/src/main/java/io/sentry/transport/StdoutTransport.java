/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package io.sentry.transport;

import io.sentry.ISerializer;
import io.sentry.SentryEnvelope;
import io.sentry.transport.ITransport;
import io.sentry.transport.TransportResult;
import io.sentry.util.Objects;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.jetbrains.annotations.NotNull;

public final class StdoutTransport
implements ITransport {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @NotNull
    private final ISerializer serializer;

    public StdoutTransport(@NotNull ISerializer serializer) {
        this.serializer = Objects.requireNonNull(serializer, "Serializer is required");
    }

    @Override
    public boolean isRetryAfter(String type) {
        return false;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    @Override
    public TransportResult send(@NotNull SentryEnvelope envelope) throws IOException {
        Objects.requireNonNull(envelope, "SentryEnvelope is required");
        try {
            Throwable throwable = null;
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)System.out, UTF_8));){
                TransportResult transportResult;
                PrintWriter printWriter = new PrintWriter(writer);
                Throwable throwable2 = null;
                try {
                    this.serializer.serialize(envelope, (Writer)printWriter);
                    transportResult = TransportResult.success();
                }
                catch (Throwable throwable3) {
                    try {
                        try {
                            throwable2 = throwable3;
                            throw throwable3;
                        }
                        catch (Throwable throwable4) {
                            StdoutTransport.$closeResource(throwable2, printWriter);
                            throw throwable4;
                        }
                    }
                    catch (Throwable throwable5) {
                        throwable = throwable5;
                        throw throwable5;
                    }
                }
                StdoutTransport.$closeResource(throwable2, printWriter);
                return transportResult;
            }
        }
        catch (Exception e) {
            return TransportResult.error();
        }
    }

    @Override
    public void close() {
    }
}

