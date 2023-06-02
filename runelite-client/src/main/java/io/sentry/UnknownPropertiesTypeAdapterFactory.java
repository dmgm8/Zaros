/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.FieldNamingStrategy
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.TypeAdapter
 *  com.google.gson.TypeAdapterFactory
 *  com.google.gson.internal.Excluder
 *  com.google.gson.reflect.TypeToken
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 */
package io.sentry;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.sentry.IUnknownPropertiesConsumer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

final class UnknownPropertiesTypeAdapterFactory
implements TypeAdapterFactory {
    private static final TypeAdapterFactory instance = new UnknownPropertiesTypeAdapterFactory();

    private UnknownPropertiesTypeAdapterFactory() {
    }

    static TypeAdapterFactory get() {
        return instance;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        TypeAdapter unknownPropertiesTypeAdapter;
        if (!IUnknownPropertiesConsumer.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }
        Class rawType = typeToken.getRawType();
        TypeAdapter delegateTypeAdapter = gson.getDelegateAdapter((TypeAdapterFactory)this, typeToken);
        Excluder excluder = gson.excluder();
        FieldNamingStrategy fieldNamingStrategy = gson.fieldNamingStrategy();
        TypeAdapter castTypeAdapter = unknownPropertiesTypeAdapter = UnknownPropertiesTypeAdapter.create(rawType, delegateTypeAdapter, excluder, fieldNamingStrategy);
        return castTypeAdapter;
    }

    private static final class UnknownPropertiesTypeAdapter<T extends IUnknownPropertiesConsumer>
    extends TypeAdapter<T> {
        private final TypeAdapter<T> typeAdapter;
        private final Collection<String> propertyNames;

        private UnknownPropertiesTypeAdapter(TypeAdapter<T> typeAdapter, Collection<String> propertyNames) {
            this.typeAdapter = typeAdapter;
            this.propertyNames = propertyNames;
        }

        private static <T extends IUnknownPropertiesConsumer> TypeAdapter<T> create(Class<? super T> clazz, TypeAdapter<T> typeAdapter, Excluder excluder, FieldNamingStrategy fieldNamingStrategy) {
            Collection<String> propertyNames = UnknownPropertiesTypeAdapter.getPropertyNames(clazz, excluder, fieldNamingStrategy);
            return new UnknownPropertiesTypeAdapter<T>(typeAdapter, propertyNames);
        }

        private static Collection<String> getPropertyNames(Class<?> clazz, Excluder excluder, FieldNamingStrategy fieldNamingStrategy) {
            ArrayList<String> propertyNames = new ArrayList<String>();
            for (Class<?> i = clazz; i.getSuperclass() != null && i != Object.class; i = i.getSuperclass()) {
                for (Field declaredField : i.getDeclaredFields()) {
                    if (excluder.excludeField(declaredField, false)) continue;
                    String propertyName = fieldNamingStrategy.translateName(declaredField);
                    propertyNames.add(propertyName);
                }
            }
            return propertyNames;
        }

        public void write(JsonWriter out, T value) throws IOException {
            this.typeAdapter.write(out, value);
        }

        public T read(JsonReader in) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(in);
            if (jsonElement == null || jsonElement.isJsonNull()) {
                return null;
            }
            JsonObject jsonObjectToParse = jsonElement.getAsJsonObject();
            HashMap<String, Object> unknownProperties = new HashMap<String, Object>();
            for (Map.Entry e : jsonObjectToParse.entrySet()) {
                String propertyName = (String)e.getKey();
                if (this.propertyNames.contains(propertyName)) continue;
                unknownProperties.put(propertyName, e.getValue());
            }
            IUnknownPropertiesConsumer object = (IUnknownPropertiesConsumer)this.typeAdapter.fromJsonTree((JsonElement)jsonObjectToParse);
            if (!unknownProperties.isEmpty()) {
                object.acceptUnknownProperties(unknownProperties);
            }
            return (T)object;
        }
    }
}

