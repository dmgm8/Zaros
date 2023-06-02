/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry.cache;

import io.sentry.DateUtils;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryItemType;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.Session;
import io.sentry.cache.CacheStrategy;
import io.sentry.cache.IEnvelopeCache;
import io.sentry.hints.SessionEnd;
import io.sentry.hints.SessionStart;
import io.sentry.util.Objects;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class EnvelopeCache
extends CacheStrategy
implements IEnvelopeCache {
    public static final String SUFFIX_ENVELOPE_FILE = ".envelope";
    public static final String PREFIX_CURRENT_SESSION_FILE = "session";
    static final String SUFFIX_CURRENT_SESSION_FILE = ".json";
    static final String CRASH_MARKER_FILE = ".sentry-native/last_crash";
    @NotNull
    private final Map<SentryEnvelope, String> fileNameMap = new WeakHashMap<SentryEnvelope, String>();

    public EnvelopeCache(@NotNull SentryOptions options) {
        super(options, options.getCacheDirPath(), options.getCacheDirSize());
    }

    @Override
    public void store(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        File envelopeFile;
        Objects.requireNonNull(envelope, "Envelope is required.");
        this.rotateCacheIfNeeded(this.allEnvelopeFiles());
        File currentSessionFile = this.getCurrentSessionFile();
        if (hint instanceof SessionEnd && !currentSessionFile.delete()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Current envelope doesn't exist.", new Object[0]);
        }
        if (hint instanceof SessionStart) {
            if (currentSessionFile.exists()) {
                this.options.getLogger().log(SentryLevel.WARNING, "Current session is not ended, we'd need to end it.", new Object[0]);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(currentSessionFile), UTF_8));){
                    Session session = this.serializer.deserializeSession(reader);
                    if (session == null) {
                        this.options.getLogger().log(SentryLevel.ERROR, "Stream from path %s resulted in a null envelope.", currentSessionFile.getAbsolutePath());
                    } else {
                        File crashMarkerFile = new File(this.options.getCacheDirPath(), CRASH_MARKER_FILE);
                        Date timestamp = null;
                        if (crashMarkerFile.exists()) {
                            this.options.getLogger().log(SentryLevel.INFO, "Crash marker file exists, last Session is gonna be Crashed.", new Object[0]);
                            timestamp = this.getTimestampFromCrashMarkerFile(crashMarkerFile);
                            if (!crashMarkerFile.delete()) {
                                this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete the crash marker file. %s.", crashMarkerFile.getAbsolutePath());
                            }
                            session.update(Session.State.Crashed, null, true);
                        }
                        session.end(timestamp);
                        SentryEnvelope fromSession = SentryEnvelope.fromSession(this.serializer, session, this.options.getSdkVersion());
                        File fileFromSession = this.getEnvelopeFile(fromSession);
                        this.writeEnvelopeToDisk(fileFromSession, fromSession);
                    }
                }
                catch (Exception e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Error processing session.", e);
                }
                if (!currentSessionFile.delete()) {
                    this.options.getLogger().log(SentryLevel.WARNING, "Failed to delete the current session file.", new Object[0]);
                }
            }
            this.updateCurrentSession(currentSessionFile, envelope);
        }
        if ((envelopeFile = this.getEnvelopeFile(envelope)).exists()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Not adding Envelope to offline storage because it already exists: %s", envelopeFile.getAbsolutePath());
            return;
        }
        this.options.getLogger().log(SentryLevel.DEBUG, "Adding Envelope to offline storage: %s", envelopeFile.getAbsolutePath());
        this.writeEnvelopeToDisk(envelopeFile, envelope);
    }

    @Nullable
    private Date getTimestampFromCrashMarkerFile(@NotNull File markerFile) {
        block8: {
            Date date;
            BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(markerFile), UTF_8));
            Throwable throwable = null;
            try {
                String timestamp = reader.readLine();
                this.options.getLogger().log(SentryLevel.DEBUG, "Crash marker file has %s timestamp.", timestamp);
                date = DateUtils.getDateTime(timestamp);
            }
            catch (Throwable throwable2) {
                try {
                    try {
                        throwable = throwable2;
                        throw throwable2;
                    }
                    catch (Throwable throwable3) {
                        EnvelopeCache.$closeResource(throwable, reader);
                        throw throwable3;
                    }
                }
                catch (IOException e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Error reading the crash marker file.", e);
                    break block8;
                }
                catch (IllegalArgumentException e) {
                    this.options.getLogger().log(SentryLevel.ERROR, e, "Error converting the crash timestamp.", new Object[0]);
                }
            }
            EnvelopeCache.$closeResource(throwable, reader);
            return date;
        }
        return null;
    }

    private void updateCurrentSession(@NotNull File currentSessionFile, @NotNull SentryEnvelope envelope) {
        Iterable<SentryEnvelopeItem> items = envelope.getItems();
        if (items.iterator().hasNext()) {
            SentryEnvelopeItem item = items.iterator().next();
            if (SentryItemType.Session.equals((Object)item.getHeader().getType())) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)new ByteArrayInputStream(item.getData()), UTF_8));){
                    Session session = this.serializer.deserializeSession(reader);
                    if (session == null) {
                        this.options.getLogger().log(SentryLevel.ERROR, "Item of type %s returned null by the parser.", new Object[]{item.getHeader().getType()});
                    } else {
                        this.writeSessionToDisk(currentSessionFile, session);
                    }
                }
                catch (Exception e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Item failed to process.", e);
                }
            } else {
                this.options.getLogger().log(SentryLevel.INFO, "Current envelope has a different envelope type %s", new Object[]{item.getHeader().getType()});
            }
        } else {
            this.options.getLogger().log(SentryLevel.INFO, "Current envelope %s is empty", currentSessionFile.getAbsolutePath());
        }
    }

    private void writeEnvelopeToDisk(@NotNull File file, @NotNull SentryEnvelope envelope) {
        if (file.exists()) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Overwriting envelope to offline storage: %s", file.getAbsolutePath());
            if (!file.delete()) {
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)outputStream, UTF_8));){
            this.serializer.serialize(envelope, (Writer)writer);
        }
        catch (Exception e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Error writing Envelope %s to offline storage", file.getAbsolutePath());
        }
    }

    private void writeSessionToDisk(@NotNull File file, @NotNull Session session) {
        if (file.exists()) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Overwriting session to offline storage: %s", session.getSessionId());
            if (!file.delete()) {
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)outputStream, UTF_8));){
            this.serializer.serialize(session, (Writer)writer);
        }
        catch (Exception e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Error writing Session to offline storage: %s", session.getSessionId());
        }
    }

    @Override
    public void discard(@NotNull SentryEnvelope envelope) {
        Objects.requireNonNull(envelope, "Envelope is required.");
        File envelopeFile = this.getEnvelopeFile(envelope);
        if (envelopeFile.exists()) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Discarding envelope from cache: %s", envelopeFile.getAbsolutePath());
            if (!envelopeFile.delete()) {
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete envelope: %s", envelopeFile.getAbsolutePath());
            }
        } else {
            this.options.getLogger().log(SentryLevel.DEBUG, "Envelope was not cached: %s", envelopeFile.getAbsolutePath());
        }
    }

    @NotNull
    private synchronized File getEnvelopeFile(@NotNull SentryEnvelope envelope) {
        String fileName;
        if (this.fileNameMap.containsKey(envelope)) {
            fileName = this.fileNameMap.get(envelope);
        } else {
            fileName = envelope.getHeader().getEventId() != null ? envelope.getHeader().getEventId().toString() : UUID.randomUUID().toString();
            fileName = fileName + SUFFIX_ENVELOPE_FILE;
            this.fileNameMap.put(envelope, fileName);
        }
        return new File(this.directory.getAbsolutePath(), fileName);
    }

    @NotNull
    private File getCurrentSessionFile() {
        return new File(this.directory.getAbsolutePath(), "session.json");
    }

    @Override
    @NotNull
    public Iterator<SentryEnvelope> iterator() {
        File[] allCachedEnvelopes = this.allEnvelopeFiles();
        ArrayList<SentryEnvelope> ret = new ArrayList<SentryEnvelope>(allCachedEnvelopes.length);
        for (File file : allCachedEnvelopes) {
            try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));){
                ret.add(this.serializer.deserializeEnvelope(is));
            }
            catch (FileNotFoundException e) {
                this.options.getLogger().log(SentryLevel.DEBUG, "Envelope file '%s' disappeared while converting all cached files to envelopes.", file.getAbsolutePath());
            }
            catch (IOException e) {
                this.options.getLogger().log(SentryLevel.ERROR, String.format("Error while reading cached envelope from file %s", file.getAbsolutePath()), e);
            }
        }
        return ret.iterator();
    }

    @NotNull
    private File[] allEnvelopeFiles() {
        File[] files;
        if (this.isDirectoryValid() && (files = this.directory.listFiles((__, fileName) -> fileName.endsWith(SUFFIX_ENVELOPE_FILE))) != null) {
            return files;
        }
        return new File[0];
    }
}

