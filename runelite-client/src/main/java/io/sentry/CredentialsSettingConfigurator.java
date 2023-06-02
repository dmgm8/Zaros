/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

import io.sentry.Dsn;
import io.sentry.transport.IConnectionConfigurator;
import java.net.HttpURLConnection;

final class CredentialsSettingConfigurator
implements IConnectionConfigurator {
    private static final String USER_AGENT = "User-Agent";
    private static final String SENTRY_AUTH = "X-Sentry-Auth";
    private final String authHeader;
    private final String userAgent;

    CredentialsSettingConfigurator(Dsn dsn, String clientName) {
        String publicKey = dsn.getPublicKey();
        String secretKey = dsn.getSecretKey();
        this.authHeader = "Sentry sentry_version=7,sentry_client=" + clientName + ",sentry_key=" + publicKey + (secretKey != null && secretKey.length() > 0 ? ",sentry_secret=" + secretKey : "");
        this.userAgent = clientName;
    }

    @Override
    public void configure(HttpURLConnection connection) {
        connection.setRequestProperty(USER_AGENT, this.userAgent);
        connection.setRequestProperty(SENTRY_AUTH, this.authHeader);
    }
}

