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
import java.awt.Color;
import java.io.IOException;

public class ColorTypeAdapter
extends TypeAdapter<Color> {
    public void write(JsonWriter out, Color value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        int rgba = value.getRGB();
        out.value(String.format("#%08X", rgba));
    }

    public Color read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case NULL: {
                in.nextNull();
                return null;
            }
            case STRING: {
                String value = in.nextString();
                if (value.charAt(0) == '#') {
                    value = value.substring(1);
                }
                int intValue = Integer.parseUnsignedInt(value, 16);
                return new Color(intValue, true);
            }
            case BEGIN_OBJECT: {
                in.beginObject();
                double value = 0.0;
                block11: while (in.peek() != JsonToken.END_OBJECT) {
                    switch (in.nextName()) {
                        case "value": {
                            value = in.nextDouble();
                            continue block11;
                        }
                    }
                    in.skipValue();
                }
                in.endObject();
                return new Color((int)value, true);
            }
        }
        return null;
    }
}

