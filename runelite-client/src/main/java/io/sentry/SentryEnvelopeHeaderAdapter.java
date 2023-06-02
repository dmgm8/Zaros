/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package io.sentry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.sentry.SentryEnvelopeHeader;
import io.sentry.protocol.SdkVersion;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryPackage;
import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class SentryEnvelopeHeaderAdapter
extends TypeAdapter<SentryEnvelopeHeader> {
    public void write(JsonWriter writer, SentryEnvelopeHeader value) throws IOException {
        SdkVersion sdkVersion;
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.beginObject();
        if (value.getEventId() != null) {
            writer.name("event_id");
            writer.value(value.getEventId().toString());
        }
        if ((sdkVersion = value.getSdkVersion()) != null && this.hasValidSdkVersion(sdkVersion)) {
            List<SentryPackage> packages;
            writer.name("sdk").beginObject();
            writer.name("name").value(sdkVersion.getName());
            writer.name("version").value(sdkVersion.getVersion());
            List<String> integrations = sdkVersion.getIntegrations();
            if (integrations != null) {
                writer.name("integrations").beginArray();
                for (String integration : integrations) {
                    writer.value(integration);
                }
                writer.endArray();
            }
            if ((packages = sdkVersion.getPackages()) != null) {
                writer.name("packages").beginArray();
                for (SentryPackage item : packages) {
                    writer.beginObject();
                    writer.name("name").value(item.getName());
                    writer.name("version").value(item.getVersion());
                    writer.endObject();
                }
                writer.endArray();
            }
            writer.endObject();
        }
        writer.endObject();
    }

    private boolean hasValidSdkVersion(@NotNull SdkVersion sdkVersion) {
        return sdkVersion.getName() != null && !sdkVersion.getName().isEmpty() && sdkVersion.getVersion() != null && !sdkVersion.getVersion().isEmpty();
    }

    public SentryEnvelopeHeader read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        SentryId eventId = null;
        SdkVersion sdkVersion = null;
        reader.beginObject();
        block28: while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "event_id": {
                    eventId = new SentryId(reader.nextString());
                    continue block28;
                }
                case "sdk": {
                    reader.beginObject();
                    sdkVersion = new SdkVersion();
                    block29: while (reader.hasNext()) {
                        switch (reader.nextName()) {
                            case "name": {
                                sdkVersion.setName(reader.nextString());
                                continue block29;
                            }
                            case "version": {
                                sdkVersion.setVersion(reader.nextString());
                                continue block29;
                            }
                            case "integrations": {
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    sdkVersion.addIntegration(reader.nextString());
                                }
                                reader.endArray();
                                continue block29;
                            }
                            case "packages": {
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    reader.beginObject();
                                    String name = null;
                                    String version = null;
                                    block32: while (reader.hasNext()) {
                                        switch (reader.nextName()) {
                                            case "name": {
                                                name = reader.nextString();
                                                continue block32;
                                            }
                                            case "version": {
                                                version = reader.nextString();
                                                continue block32;
                                            }
                                        }
                                        reader.skipValue();
                                    }
                                    sdkVersion.addPackage(name, version);
                                    reader.endObject();
                                }
                                reader.endArray();
                                continue block29;
                            }
                        }
                        reader.skipValue();
                    }
                    reader.endObject();
                    continue block28;
                }
            }
            reader.skipValue();
        }
        reader.endObject();
        return new SentryEnvelopeHeader(eventId, sdkVersion);
    }
}

