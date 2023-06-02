/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.IndexDataBase
 */
package rs.api;

import net.runelite.api.IndexDataBase;
import net.runelite.mapping.Import;

public interface RSIndexDataBase
extends IndexDataBase {
    @Import(value="tryLoadRecordByNames")
    public boolean tryLoadRecordByNames(String var1, String var2);

    @Import(value="getFileIds")
    public int[] getFileIds(int var1);

    @Import(value="fileCount")
    public int fileCount(int var1);

    @Import(value="loadData")
    public byte[] loadData(int var1, int var2);
}

