/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package io.sentry;

import io.sentry.CredentialsSettingConfigurator;
import io.sentry.Dsn;
import io.sentry.SentryOptions;
import io.sentry.transport.HttpTransport;
import io.sentry.transport.ITransport;
import java.net.MalformedURLException;
import java.net.URL;
import org.jetbrains.annotations.NotNull;

final class HttpTransportFactory {
    private HttpTransportFactory() {
    }

    static ITransport create(@NotNull SentryOptions options) {
        URL sentryUrl;
        Dsn parsedDsn = new Dsn(options.getDsn());
        CredentialsSettingConfigurator credentials = new CredentialsSettingConfigurator(parsedDsn, options.getSentryClientName());
        try {
            sentryUrl = parsedDsn.getSentryUri().toURL();
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to compose the Sentry's server URL.", e);
        }
        return new HttpTransport(options, credentials, options.getConnectionTimeoutMillis(), options.getReadTimeoutMillis(), options.getSslSocketFactory(), options.getHostnameVerifier(), sentryUrl);
    }
}

