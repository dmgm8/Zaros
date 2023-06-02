/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.ISerializer;
import io.sentry.SentryEnvelopeItemHeader;
import io.sentry.SentryEvent;
import io.sentry.SentryItemType;
import io.sentry.Session;
import io.sentry.util.Objects;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class SentryEnvelopeItem {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final SentryEnvelopeItemHeader header;
    @Nullable
    private final Callable<byte[]> dataFactory;
    @Nullable
    private byte[] data;

    SentryEnvelopeItem(@NotNull SentryEnvelopeItemHeader header, byte[] data) {
        this.header = Objects.requireNonNull(header, "SentryEnvelopeItemHeader is required.");
        this.data = data;
        this.dataFactory = null;
    }

    SentryEnvelopeItem(@NotNull SentryEnvelopeItemHeader header, @Nullable Callable<byte[]> dataFactory) {
        this.header = Objects.requireNonNull(header, "SentryEnvelopeItemHeader is required.");
        this.dataFactory = Objects.requireNonNull(dataFactory, "DataFactory is required.");
        this.data = null;
    }

    @NotNull
    public byte[] getData() throws Exception {
        if (this.data == null && this.dataFactory != null) {
            this.data = this.dataFactory.call();
        }
        return this.data;
    }

    @NotNull
    public SentryEnvelopeItemHeader getHeader() {
        return this.header;
    }

    @NotNull
    public static SentryEnvelopeItem fromSession(@NotNull ISerializer serializer, @NotNull Session session) throws IOException {
        Objects.requireNonNull(serializer, "ISerializer is required.");
        Objects.requireNonNull(session, "Session is required.");
        CachedItem cachedItem = new CachedItem(() -> {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Throwable throwable = null;
            try {
                byte[] arrby;
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)stream, UTF_8));
                Throwable throwable2 = null;
                try {
                    serializer.serialize(session, (Writer)writer);
                    arrby = stream.toByteArray();
                }
                catch (Throwable throwable3) {
                    try {
                        try {
                            throwable2 = throwable3;
                            throw throwable3;
                        }
                        catch (Throwable throwable4) {
                            SentryEnvelopeItem.$closeResource(throwable2, writer);
                            throw throwable4;
                        }
                    }
                    catch (Throwable throwable5) {
                        throwable = throwable5;
                        throw throwable5;
                    }
                }
                SentryEnvelopeItem.$closeResource(throwable2, writer);
                return arrby;
            }
            finally {
                SentryEnvelopeItem.$closeResource(throwable, stream);
            }
        });
        SentryEnvelopeItemHeader itemHeader = new SentryEnvelopeItemHeader(SentryItemType.Session, () -> cachedItem.getBytes().length, "application/json", null);
        return new SentryEnvelopeItem(itemHeader, () -> cachedItem.getBytes());
    }

    @Nullable
    public SentryEvent getEvent(@NotNull ISerializer serializer) throws Exception {
        if (this.header == null || this.header.getType() != SentryItemType.Event) {
            return null;
        }
        try (BufferedReader eventReader = new BufferedReader(new InputStreamReader((InputStream)new ByteArrayInputStream(this.getData()), UTF_8));){
            SentryEvent sentryEvent = serializer.deserializeEvent(eventReader);
            return sentryEvent;
        }
    }

    @NotNull
    public static SentryEnvelopeItem fromEvent(@NotNull ISerializer serializer, @NotNull SentryEvent event) throws IOException {
        Objects.requireNonNull(serializer, "ISerializer is required.");
        Objects.requireNonNull(event, "SentryEvent is required.");
        CachedItem cachedItem = new CachedItem(() -> {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Throwable throwable = null;
            try {
                byte[] arrby;
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)stream, UTF_8));
                Throwable throwable2 = null;
                try {
                    serializer.serialize(event, (Writer)writer);
                    arrby = stream.toByteArray();
                }
                catch (Throwable throwable3) {
                    try {
                        try {
                            throwable2 = throwable3;
                            throw throwable3;
                        }
                        catch (Throwable throwable4) {
                            SentryEnvelopeItem.$closeResource(throwable2, writer);
                            throw throwable4;
                        }
                    }
                    catch (Throwable throwable5) {
                        throwable = throwable5;
                        throw throwable5;
                    }
                }
                SentryEnvelopeItem.$closeResource(throwable2, writer);
                return arrby;
            }
            finally {
                SentryEnvelopeItem.$closeResource(throwable, stream);
            }
        });
        SentryEnvelopeItemHeader itemHeader = new SentryEnvelopeItemHeader(SentryItemType.Event, () -> cachedItem.getBytes().length, "application/json", null);
        return new SentryEnvelopeItem(itemHeader, () -> cachedItem.getBytes());
    }

    private static class CachedItem {
        @Nullable
        private byte[] bytes;
        @Nullable
        private final Callable<byte[]> dataFactory;

        public CachedItem(@Nullable Callable<byte[]> dataFactory) {
            this.dataFactory = dataFactory;
        }

        @Nullable
        public byte[] getBytes() throws Exception {
            if (this.bytes == null) {
                this.bytes = this.dataFactory.call();
            }
            return this.bytes;
        }
    }
}

