/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

import io.sentry.ISerializer;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEvent;
import io.sentry.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

final class NoOpSerializer
implements ISerializer {
    private static final NoOpSerializer instance = new NoOpSerializer();

    public static NoOpSerializer getInstance() {
        return instance;
    }

    private NoOpSerializer() {
    }

    @Override
    public SentryEvent deserializeEvent(Reader reader) {
        return null;
    }

    @Override
    public Session deserializeSession(Reader reader) {
        return null;
    }

    @Override
    public SentryEnvelope deserializeEnvelope(InputStream inputStream) {
        return null;
    }

    @Override
    public void serialize(SentryEvent event, Writer writer) {
    }

    @Override
    public void serialize(Session session, Writer writer) throws IOException {
    }

    @Override
    public void serialize(SentryEnvelope envelope, Writer outputStream) throws Exception {
    }

    @Override
    public String serialize(Map<String, Object> data) throws Exception {
        return null;
    }
}

