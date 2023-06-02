/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.FieldNamingPolicy
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sentry.IEnvelopeReader;
import io.sentry.ILogger;
import io.sentry.ISerializer;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeHeader;
import io.sentry.SentryEnvelopeHeaderAdapter;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryEnvelopeItemHeader;
import io.sentry.SentryEnvelopeItemHeaderAdapter;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.Session;
import io.sentry.SessionAdapter;
import io.sentry.UnknownPropertiesTypeAdapterFactory;
import io.sentry.adapters.ContextsDeserializerAdapter;
import io.sentry.adapters.ContextsSerializerAdapter;
import io.sentry.adapters.DateDeserializerAdapter;
import io.sentry.adapters.DateSerializerAdapter;
import io.sentry.adapters.OrientationDeserializerAdapter;
import io.sentry.adapters.OrientationSerializerAdapter;
import io.sentry.adapters.SentryIdDeserializerAdapter;
import io.sentry.adapters.SentryIdSerializerAdapter;
import io.sentry.adapters.SentryLevelDeserializerAdapter;
import io.sentry.adapters.SentryLevelSerializerAdapter;
import io.sentry.adapters.TimeZoneDeserializerAdapter;
import io.sentry.adapters.TimeZoneSerializerAdapter;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.Device;
import io.sentry.protocol.SentryId;
import io.sentry.util.Objects;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GsonSerializer
implements ISerializer {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @NotNull
    private final ILogger logger;
    @NotNull
    private final Gson gson;
    @NotNull
    private final IEnvelopeReader envelopeReader;

    public GsonSerializer(@NotNull ILogger logger, @NotNull IEnvelopeReader envelopeReader) {
        this.logger = Objects.requireNonNull(logger, "The ILogger object is required.");
        this.envelopeReader = Objects.requireNonNull(envelopeReader, "The IEnvelopeReader object is required.");
        this.gson = this.provideGson();
    }

    @NotNull
    private Gson provideGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(SentryId.class, (Object)new SentryIdSerializerAdapter(this.logger)).registerTypeAdapter(SentryId.class, (Object)new SentryIdDeserializerAdapter(this.logger)).registerTypeAdapter(Date.class, (Object)new DateSerializerAdapter(this.logger)).registerTypeAdapter(Date.class, (Object)new DateDeserializerAdapter(this.logger)).registerTypeAdapter(TimeZone.class, (Object)new TimeZoneSerializerAdapter(this.logger)).registerTypeAdapter(TimeZone.class, (Object)new TimeZoneDeserializerAdapter(this.logger)).registerTypeAdapter(Device.DeviceOrientation.class, (Object)new OrientationSerializerAdapter(this.logger)).registerTypeAdapter(Device.DeviceOrientation.class, (Object)new OrientationDeserializerAdapter(this.logger)).registerTypeAdapter(SentryLevel.class, (Object)new SentryLevelSerializerAdapter(this.logger)).registerTypeAdapter(SentryLevel.class, (Object)new SentryLevelDeserializerAdapter(this.logger)).registerTypeAdapter(Contexts.class, (Object)new ContextsDeserializerAdapter(this.logger)).registerTypeAdapter(Contexts.class, (Object)new ContextsSerializerAdapter(this.logger)).registerTypeAdapterFactory(UnknownPropertiesTypeAdapterFactory.get()).registerTypeAdapter(SentryEnvelopeHeader.class, (Object)new SentryEnvelopeHeaderAdapter()).registerTypeAdapter(SentryEnvelopeItemHeader.class, (Object)new SentryEnvelopeItemHeaderAdapter()).registerTypeAdapter(Session.class, (Object)new SessionAdapter(this.logger)).create();
    }

    @Override
    @Nullable
    public SentryEvent deserializeEvent(@NotNull Reader reader) {
        Objects.requireNonNull(reader, "The Reader object is required.");
        return (SentryEvent)this.gson.fromJson(reader, SentryEvent.class);
    }

    @Override
    @Nullable
    public Session deserializeSession(@NotNull Reader reader) {
        Objects.requireNonNull(reader, "The Reader object is required.");
        return (Session)this.gson.fromJson(reader, Session.class);
    }

    @Override
    @Nullable
    public SentryEnvelope deserializeEnvelope(@NotNull InputStream inputStream) {
        Objects.requireNonNull(inputStream, "The InputStream object is required.");
        try {
            return this.envelopeReader.read(inputStream);
        }
        catch (IOException e) {
            this.logger.log(SentryLevel.ERROR, "Error deserializing envelope.", e);
            return null;
        }
    }

    @Override
    public void serialize(@NotNull SentryEvent event, @NotNull Writer writer) throws IOException {
        Objects.requireNonNull(event, "The SentryEvent object is required.");
        Objects.requireNonNull(writer, "The Writer object is required.");
        this.gson.toJson((Object)event, SentryEvent.class, (Appendable)writer);
        writer.flush();
    }

    @Override
    public void serialize(@NotNull Session session, @NotNull Writer writer) throws IOException {
        Objects.requireNonNull(session, "The Session object is required.");
        Objects.requireNonNull(writer, "The Writer object is required.");
        this.gson.toJson((Object)session, Session.class, (Appendable)writer);
        writer.flush();
    }

    @Override
    public void serialize(@NotNull SentryEnvelope envelope, @NotNull Writer writer) throws Exception {
        Objects.requireNonNull(envelope, "The SentryEnvelope object is required.");
        Objects.requireNonNull(writer, "The Writer object is required.");
        this.gson.toJson((Object)envelope.getHeader(), SentryEnvelopeHeader.class, (Appendable)writer);
        writer.write("\n");
        for (SentryEnvelopeItem item : envelope.getItems()) {
            this.gson.toJson((Object)item.getHeader(), SentryEnvelopeItemHeader.class, (Appendable)writer);
            writer.write("\n");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)new ByteArrayInputStream(item.getData()), UTF_8));){
                int charsRead;
                char[] buffer = new char[1024];
                while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
                    writer.write(buffer, 0, charsRead);
                }
            }
            writer.write("\n");
        }
        writer.flush();
    }

    @Override
    public String serialize(@NotNull Map<String, Object> data) throws Exception {
        Objects.requireNonNull(data, "The SentryEnvelope object is required.");
        return this.gson.toJson(data);
    }
}

