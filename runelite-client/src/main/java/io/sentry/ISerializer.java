/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

import io.sentry.SentryEnvelope;
import io.sentry.SentryEvent;
import io.sentry.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public interface ISerializer {
    public SentryEvent deserializeEvent(Reader var1);

    public Session deserializeSession(Reader var1);

    public SentryEnvelope deserializeEnvelope(InputStream var1);

    public void serialize(SentryEvent var1, Writer var2) throws IOException;

    public void serialize(Session var1, Writer var2) throws IOException;

    public void serialize(SentryEnvelope var1, Writer var2) throws Exception;

    public String serialize(Map<String, Object> var1) throws Exception;
}

