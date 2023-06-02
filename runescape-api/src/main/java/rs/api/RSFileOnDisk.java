/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import java.io.RandomAccessFile;
import net.runelite.mapping.Import;

public interface RSFileOnDisk {
    @Import(value="file")
    public RandomAccessFile getFile();

    @Import(value="position")
    public long getPosition();

    @Import(value="length")
    public long getLength();
}

