/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import io.sentry.InvalidDsnException;
import java.net.URI;
import org.jetbrains.annotations.Nullable;

final class Dsn {
    private final String projectId;
    private final String path;
    private final String secretKey;
    private final String publicKey;
    private final URI sentryUri;

    public String getProjectId() {
        return this.projectId;
    }

    public String getPath() {
        return this.path;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    URI getSentryUri() {
        return this.sentryUri;
    }

    Dsn(@Nullable String dsn) throws InvalidDsnException {
        try {
            int projectIdStart;
            String path;
            URI uri = new URI(dsn);
            String userInfo = uri.getUserInfo();
            if (userInfo == null || userInfo.isEmpty()) {
                throw new IllegalArgumentException("Invalid DSN: No public key provided.");
            }
            String[] keys = userInfo.split(":", -1);
            this.publicKey = keys[0];
            if (this.publicKey == null || this.publicKey.isEmpty()) {
                throw new IllegalArgumentException("Invalid DSN: No public key provided.");
            }
            this.secretKey = keys.length > 1 ? keys[1] : null;
            String uriPath = uri.getPath();
            if (uriPath.endsWith("/")) {
                uriPath = uriPath.substring(0, uriPath.length() - 1);
            }
            if (!(path = uriPath.substring(0, projectIdStart = uriPath.lastIndexOf("/") + 1)).endsWith("/")) {
                path = path + "/";
            }
            this.path = path;
            this.projectId = uriPath.substring(projectIdStart);
            if (this.projectId.isEmpty()) {
                throw new IllegalArgumentException("Invalid DSN: A Project Id is required.");
            }
            this.sentryUri = new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), path + "api/" + this.projectId, null, null);
        }
        catch (Exception e) {
            throw new InvalidDsnException(dsn, e);
        }
    }
}

