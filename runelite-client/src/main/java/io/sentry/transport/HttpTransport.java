/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.ApiStatus$NonExtendable
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.transport;

import io.sentry.ILogger;
import io.sentry.ISerializer;
import io.sentry.SentryEnvelope;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.transport.CurrentDateProvider;
import io.sentry.transport.IConnectionConfigurator;
import io.sentry.transport.ICurrentDateProvider;
import io.sentry.transport.ITransport;
import io.sentry.transport.TransportResult;
import io.sentry.util.Objects;
import io.sentry.util.StringUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
@ApiStatus.Internal
public class HttpTransport
implements ITransport {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @Nullable
    private final Proxy proxy;
    @NotNull
    private final IConnectionConfigurator connectionConfigurator;
    @NotNull
    private final ISerializer serializer;
    private final int connectionTimeout;
    private final int readTimeout;
    @NotNull
    private final URL envelopeUrl;
    @Nullable
    private final SSLSocketFactory sslSocketFactory;
    @Nullable
    private final HostnameVerifier hostnameVerifier;
    @NotNull
    private final SentryOptions options;
    @NotNull
    private final Map<DataCategory, Date> sentryRetryAfterLimit = new ConcurrentHashMap<DataCategory, Date>();
    private static final int HTTP_RETRY_AFTER_DEFAULT_DELAY_MILLIS = 60000;
    @NotNull
    private final ICurrentDateProvider currentDateProvider;
    @NotNull
    private final ILogger logger;

    public HttpTransport(@NotNull SentryOptions options, @NotNull IConnectionConfigurator connectionConfigurator, int connectionTimeoutMillis, int readTimeoutMillis, @Nullable SSLSocketFactory sslSocketFactory, @Nullable HostnameVerifier hostnameVerifier, @NotNull URL sentryUrl) {
        this(options, connectionConfigurator, connectionTimeoutMillis, readTimeoutMillis, sslSocketFactory, hostnameVerifier, sentryUrl, CurrentDateProvider.getInstance());
    }

    HttpTransport(@NotNull SentryOptions options, @NotNull IConnectionConfigurator connectionConfigurator, int connectionTimeoutMillis, int readTimeoutMillis, @Nullable SSLSocketFactory sslSocketFactory, @Nullable HostnameVerifier hostnameVerifier, @NotNull URL sentryUrl, @NotNull ICurrentDateProvider currentDateProvider) {
        this.proxy = options.getProxy();
        this.connectionConfigurator = connectionConfigurator;
        this.serializer = options.getSerializer();
        this.connectionTimeout = connectionTimeoutMillis;
        this.readTimeout = readTimeoutMillis;
        this.options = options;
        this.sslSocketFactory = sslSocketFactory;
        this.hostnameVerifier = hostnameVerifier;
        this.currentDateProvider = Objects.requireNonNull(currentDateProvider, "CurrentDateProvider is required.");
        this.logger = Objects.requireNonNull(options.getLogger(), "Logger is required.");
        try {
            URI uri = sentryUrl.toURI();
            this.envelopeUrl = uri.resolve(uri.getPath() + "/envelope/").toURL();
        }
        catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Failed to compose the Sentry's server URL.", e);
        }
    }

    @NotNull
    protected HttpURLConnection open() throws IOException {
        return (HttpURLConnection)(this.proxy == null ? this.envelopeUrl.openConnection() : this.envelopeUrl.openConnection(this.proxy));
    }

    @Override
    public boolean isRetryAfter(@NotNull String itemType) {
        DataCategory dataCategory = this.getCategoryFromItemType(itemType);
        Date currentDate = new Date(this.currentDateProvider.getCurrentTimeMillis());
        Date dateAllCategories = this.sentryRetryAfterLimit.get((Object)DataCategory.All);
        if (dateAllCategories != null && !currentDate.after(dateAllCategories)) {
            return true;
        }
        if (DataCategory.Unknown.equals((Object)dataCategory)) {
            return false;
        }
        Date dateCategory = this.sentryRetryAfterLimit.get((Object)dataCategory);
        if (dateCategory != null) {
            return !currentDate.after(dateCategory);
        }
        return false;
    }

    @NotNull
    private DataCategory getCategoryFromItemType(@NotNull String itemType) {
        switch (itemType) {
            case "event": {
                return DataCategory.Error;
            }
            case "session": {
                return DataCategory.Session;
            }
            case "attachment": {
                return DataCategory.Attachment;
            }
            case "transaction": {
                return DataCategory.Transaction;
            }
        }
        return DataCategory.Unknown;
    }

    @NotNull
    private HttpURLConnection createConnection() throws IOException {
        HttpURLConnection connection = this.open();
        this.connectionConfigurator.configure(connection);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Encoding", "gzip");
        connection.setRequestProperty("Content-Type", "application/x-sentry-envelope");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Connection", "close");
        connection.setConnectTimeout(this.connectionTimeout);
        connection.setReadTimeout(this.readTimeout);
        if (connection instanceof HttpsURLConnection && this.hostnameVerifier != null) {
            ((HttpsURLConnection)connection).setHostnameVerifier(this.hostnameVerifier);
        }
        if (connection instanceof HttpsURLConnection && this.sslSocketFactory != null) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(this.sslSocketFactory);
        }
        connection.connect();
        return connection;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @NotNull
    public TransportResult send(@NotNull SentryEnvelope envelope) throws IOException {
        TransportResult result;
        HttpURLConnection connection = this.createConnection();
        try (OutputStream outputStream = connection.getOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)gzip, UTF_8));){
            this.serializer.serialize(envelope, (Writer)writer);
        }
        catch (Exception e) {
            this.logger.log(SentryLevel.ERROR, e, "An exception occurred while submitting the envelope to the Sentry server.", new Object[0]);
        }
        finally {
            result = this.readAndLog(connection);
        }
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    private TransportResult readAndLog(@NotNull HttpURLConnection connection) {
        try {
            int responseCode = connection.getResponseCode();
            this.updateRetryAfterLimits(connection, responseCode);
            if (!this.isSuccessfulResponseCode(responseCode)) {
                this.logger.log(SentryLevel.ERROR, "Request failed, API returned %s", responseCode);
                if (this.options.isDebug()) {
                    String errorMessage = this.getErrorMessageFromStream(connection);
                    this.logger.log(SentryLevel.ERROR, errorMessage, new Object[0]);
                }
                TransportResult transportResult = TransportResult.error(responseCode);
                return transportResult;
            }
            this.logger.log(SentryLevel.DEBUG, "Envelope sent successfully.", new Object[0]);
            TransportResult transportResult = TransportResult.success();
            return transportResult;
        }
        catch (IOException e) {
            this.logger.log(SentryLevel.ERROR, e, "Error reading and logging the response stream", new Object[0]);
        }
        finally {
            this.closeAndDisconnect(connection);
        }
        return TransportResult.error();
    }

    private void closeAndDisconnect(@NotNull HttpURLConnection connection) {
        try {
            connection.getInputStream().close();
        }
        catch (IOException iOException) {
        }
        finally {
            connection.disconnect();
        }
    }

    private void updateRetryAfterLimits(@NotNull HttpURLConnection connection, int responseCode) {
        String retryAfterHeader = connection.getHeaderField("Retry-After");
        String sentryRateLimitHeader = connection.getHeaderField("X-Sentry-Rate-Limits");
        this.updateRetryAfterLimits(sentryRateLimitHeader, retryAfterHeader, responseCode);
    }

    private void updateRetryAfterLimits(@Nullable String sentryRateLimitHeader, @Nullable String retryAfterHeader, int errorCode) {
        if (sentryRateLimitHeader != null) {
            for (String limit : sentryRateLimitHeader.split(",", -1)) {
                String[] retryAfterAndCategories = (limit = limit.replace(" ", "")).split(":", -1);
                if (retryAfterAndCategories.length <= 0) continue;
                String retryAfter = retryAfterAndCategories[0];
                long retryAfterMillis = this.parseRetryAfterOrDefault(retryAfter);
                if (retryAfterAndCategories.length <= 1) continue;
                String allCategories = retryAfterAndCategories[1];
                Date date = new Date(this.currentDateProvider.getCurrentTimeMillis() + retryAfterMillis);
                if (allCategories != null && !allCategories.isEmpty()) {
                    String[] categories;
                    for (String catItem : categories = allCategories.split(";", -1)) {
                        DataCategory dataCategory = DataCategory.Unknown;
                        try {
                            dataCategory = DataCategory.valueOf(StringUtils.capitalize(catItem));
                        }
                        catch (IllegalArgumentException e) {
                            this.logger.log(SentryLevel.INFO, e, "Unknown category: %s", catItem);
                        }
                        if (DataCategory.Unknown.equals((Object)dataCategory)) continue;
                        this.applyRetryAfterOnlyIfLonger(dataCategory, date);
                    }
                    continue;
                }
                this.applyRetryAfterOnlyIfLonger(DataCategory.All, date);
            }
        } else if (errorCode == 429) {
            long retryAfterMillis = this.parseRetryAfterOrDefault(retryAfterHeader);
            Date date = new Date(this.currentDateProvider.getCurrentTimeMillis() + retryAfterMillis);
            this.applyRetryAfterOnlyIfLonger(DataCategory.All, date);
        }
    }

    private void applyRetryAfterOnlyIfLonger(@NotNull DataCategory dataCategory, @NotNull Date date) {
        Date oldDate = this.sentryRetryAfterLimit.get((Object)dataCategory);
        if (oldDate == null || date.after(oldDate)) {
            this.sentryRetryAfterLimit.put(dataCategory, date);
        }
    }

    private long parseRetryAfterOrDefault(@Nullable String retryAfterHeader) {
        long retryAfterMillis = 60000L;
        if (retryAfterHeader != null) {
            try {
                retryAfterMillis = (long)(Double.parseDouble(retryAfterHeader) * 1000.0);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return retryAfterMillis;
    }

    /*
     * Enabled aggressive exception aggregation
     */
    @NotNull
    private String getErrorMessageFromStream(@NotNull HttpURLConnection connection) {
        try {
            Throwable throwable = null;
            try (InputStream errorStream = connection.getErrorStream();){
                String string;
                BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, UTF_8));
                Throwable throwable2 = null;
                try {
                    String line;
                    StringBuilder sb = new StringBuilder();
                    boolean first = true;
                    while ((line = reader.readLine()) != null) {
                        if (!first) {
                            sb.append("\n");
                        }
                        sb.append(line);
                        first = false;
                    }
                    string = sb.toString();
                }
                catch (Throwable throwable3) {
                    try {
                        try {
                            throwable2 = throwable3;
                            throw throwable3;
                        }
                        catch (Throwable throwable4) {
                            HttpTransport.$closeResource(throwable2, reader);
                            throw throwable4;
                        }
                    }
                    catch (Throwable throwable5) {
                        throwable = throwable5;
                        throw throwable5;
                    }
                }
                HttpTransport.$closeResource(throwable2, reader);
                return string;
            }
        }
        catch (IOException e) {
            return "Failed to obtain error message while analyzing send failure.";
        }
    }

    private boolean isSuccessfulResponseCode(int responseCode) {
        return responseCode == 200;
    }

    @Override
    public void close() throws IOException {
    }

    private static enum DataCategory {
        All("__all__"),
        Default("default"),
        Error("error"),
        Session("session"),
        Attachment("attachment"),
        Transaction("transaction"),
        Security("security"),
        Unknown("unknown");

        private final String category;

        private DataCategory(String category) {
            this.category = category;
        }

        public String getCategory() {
            return this.category;
        }
    }
}

