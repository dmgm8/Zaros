/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.sentry.SentryEnvelopeItemHeader;
import io.sentry.SentryItemType;
import io.sentry.util.StringUtils;
import java.io.IOException;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class SentryEnvelopeItemHeaderAdapter
extends TypeAdapter<SentryEnvelopeItemHeader> {
    public void write(JsonWriter writer, SentryEnvelopeItemHeader value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.beginObject();
        if (value.getContentType() != null) {
            writer.name("content_type");
            writer.value(value.getContentType());
        }
        if (value.getFileName() != null) {
            writer.name("filename");
            writer.value(value.getFileName());
        }
        if (!SentryItemType.Unknown.equals((Object)value.getType())) {
            writer.name("type");
            writer.value(value.getType().name().toLowerCase(Locale.ROOT));
        }
        writer.name("length");
        writer.value((long)value.getLength());
        writer.endObject();
    }

    public SentryEnvelopeItemHeader read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String contentType = null;
        String fileName = null;
        SentryItemType type = SentryItemType.Unknown;
        int length = 0;
        reader.beginObject();
        block14: while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "content_type": {
                    contentType = reader.nextString();
                    continue block14;
                }
                case "filename": {
                    fileName = reader.nextString();
                    continue block14;
                }
                case "type": {
                    try {
                        type = SentryItemType.valueOf(StringUtils.capitalize(reader.nextString()));
                    }
                    catch (IllegalArgumentException illegalArgumentException) {}
                    continue block14;
                }
                case "length": {
                    length = reader.nextInt();
                    continue block14;
                }
            }
            reader.skipValue();
        }
        reader.endObject();
        return new SentryEnvelopeItemHeader(type, length, contentType, fileName);
    }
}

