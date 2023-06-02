/*
 * Decompiled with CFR 0.150.
 */
package rs.api;

import net.runelite.mapping.Import;

public interface RSHostPlatformStats {
    @Import(value="javaVendorType")
    public void setJavaVendorType(int var1);

    @Import(value="cpuClockSpeed")
    public void setCpuClockSpeed(int var1);
}

