/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Structure
 */
package net.runelite.client.plugins.worldhopper.ping;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class Timeval
extends Structure {
    public long tv_sec;
    public long tv_usec;

    protected List<String> getFieldOrder() {
        return Arrays.asList("tv_sec", "tv_usec");
    }
}

