/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.rs;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class TeeInputStream
extends FilterInputStream {
    private OutputStream out;

    TeeInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int thisRead = super.read(b, off, len);
        if (thisRead > 0) {
            this.out.write(b, off, thisRead);
        }
        return thisRead;
    }

    @Override
    public int read() throws IOException {
        int val = super.read();
        if (val != -1) {
            this.out.write(val);
        }
        return val;
    }

    @Override
    public long skip(long n) throws IOException {
        byte[] buf = new byte[(int)Math.min(n, 16384L)];
        long total = 0L;
        while (n > 0L) {
            int read = (int)Math.min(n, (long)buf.length);
            if ((read = this.read(buf, 0, read)) == -1) break;
            total += (long)read;
            n -= (long)read;
        }
        return total;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    public OutputStream getOut() {
        return this.out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }
}

