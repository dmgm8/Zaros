/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
 */
package net.runelite.http.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;

public class InstantTypeAdapter
extends TypeAdapter<Instant> {
    public void write(JsonWriter out, Instant value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.toEpochMilli());
    }

    public Instant read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        if (in.peek() == JsonToken.NUMBER) {
            long jsTime = in.nextLong();
            return Instant.ofEpochMilli(jsTime);
        }
        long seconds = 0L;
        int nanos = 0;
        in.beginObject();
        while (in.peek() != JsonToken.END_OBJECT) {
            switch (in.nextName()) {
                case "nanos": {
                    nanos = in.nextInt();
                    break;
                }
                case "seconds": {
                    seconds = in.nextLong();
                }
            }
        }
        in.endObject();
        return Instant.ofEpochSecond(seconds, nanos);
    }
}

