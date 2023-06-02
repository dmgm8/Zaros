/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package io.sentry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sentry.IEnvelopeReader;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeHeader;
import io.sentry.SentryEnvelopeHeaderAdapter;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryEnvelopeItemHeader;
import io.sentry.SentryEnvelopeItemHeaderAdapter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class EnvelopeReader
implements IEnvelopeReader {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson gson = new GsonBuilder().registerTypeAdapter(SentryEnvelopeHeader.class, (Object)new SentryEnvelopeHeaderAdapter()).registerTypeAdapter(SentryEnvelopeItemHeader.class, (Object)new SentryEnvelopeItemHeaderAdapter()).create();

    @Override
    @Nullable
    public SentryEnvelope read(@NotNull InputStream stream) throws IOException {
        byte[] buffer = new byte[1024];
        int streamOffset = 0;
        int envelopeEndHeaderOffset = -1;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();){
            int currentLength;
            while ((currentLength = stream.read(buffer)) > 0) {
                for (int i = 0; envelopeEndHeaderOffset == -1 && i < currentLength; ++i) {
                    if (buffer[i] != 10) continue;
                    envelopeEndHeaderOffset = streamOffset + i;
                    break;
                }
                outputStream.write(buffer, 0, currentLength);
                streamOffset += currentLength;
            }
            byte[] envelopeBytes = outputStream.toByteArray();
            if (envelopeBytes.length == 0) {
                throw new IllegalArgumentException("Empty stream.");
            }
            if (envelopeEndHeaderOffset == -1) {
                throw new IllegalArgumentException("Envelope contains no header.");
            }
            SentryEnvelopeHeader header = this.deserializeEnvelopeHeader(envelopeBytes, 0, envelopeEndHeaderOffset);
            if (header == null) {
                throw new IllegalArgumentException("Envelope header is null.");
            }
            int itemHeaderStartOffset = envelopeEndHeaderOffset + 1;
            ArrayList<SentryEnvelopeItem> items = new ArrayList<SentryEnvelopeItem>();
            while (true) {
                int lineBreakIndex = -1;
                for (int i = itemHeaderStartOffset; i < envelopeBytes.length; ++i) {
                    if (envelopeBytes[i] != 10) continue;
                    lineBreakIndex = i;
                    break;
                }
                if (lineBreakIndex == -1) {
                    throw new IllegalArgumentException("Invalid envelope. Item at index '" + items.size() + "'. has no header delimiter.");
                }
                SentryEnvelopeItemHeader itemHeader = this.deserializeEnvelopeItemHeader(envelopeBytes, itemHeaderStartOffset, lineBreakIndex - itemHeaderStartOffset);
                if (itemHeader.getLength() <= 0) {
                    throw new IllegalArgumentException("Item header at index '" + items.size() + "' has an invalid value: '" + itemHeader.getLength() + "'.");
                }
                int payloadEndOffsetExclusive = lineBreakIndex + itemHeader.getLength() + 1;
                if (payloadEndOffsetExclusive > envelopeBytes.length) {
                    throw new IllegalArgumentException("Invalid length for item at index '" + items.size() + "'. Item is '" + payloadEndOffsetExclusive + "' bytes. There are '" + envelopeBytes.length + "' in the buffer.");
                }
                byte[] envelopeItemBytes = Arrays.copyOfRange(envelopeBytes, lineBreakIndex + 1, payloadEndOffsetExclusive);
                SentryEnvelopeItem item = new SentryEnvelopeItem(itemHeader, envelopeItemBytes);
                items.add(item);
                if (payloadEndOffsetExclusive == envelopeBytes.length) break;
                if (payloadEndOffsetExclusive + 1 == envelopeBytes.length) {
                    if (envelopeBytes[payloadEndOffsetExclusive] == 10) break;
                    throw new IllegalArgumentException("Envelope has invalid data following an item.");
                }
                itemHeaderStartOffset = payloadEndOffsetExclusive + 1;
            }
            SentryEnvelope sentryEnvelope = new SentryEnvelope(header, items);
            return sentryEnvelope;
        }
    }

    private SentryEnvelopeHeader deserializeEnvelopeHeader(byte[] buffer, int offset, int length) {
        String json = new String(buffer, offset, length, UTF_8);
        return (SentryEnvelopeHeader)this.gson.fromJson(json, SentryEnvelopeHeader.class);
    }

    private SentryEnvelopeItemHeader deserializeEnvelopeItemHeader(byte[] buffer, int offset, int length) {
        String json = new String(buffer, offset, length, UTF_8);
        return (SentryEnvelopeItemHeader)this.gson.fromJson(json, SentryEnvelopeItemHeader.class);
    }
}

