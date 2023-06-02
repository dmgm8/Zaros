/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Library
 *  com.sun.jna.Pointer
 */
package net.runelite.api;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface RSPSNativeLibrary
extends Library {
    public Pointer getSystemId(String var1);
}

