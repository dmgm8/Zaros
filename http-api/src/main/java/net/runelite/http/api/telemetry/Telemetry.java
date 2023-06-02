/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.telemetry;

public class Telemetry {
    String javaVendor;
    String javaVersion;
    String osName;
    String osVersion;
    String osArch;
    String launcherVersion;
    long totalMemory;

    public String getJavaVendor() {
        return this.javaVendor;
    }

    public String getJavaVersion() {
        return this.javaVersion;
    }

    public String getOsName() {
        return this.osName;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public String getOsArch() {
        return this.osArch;
    }

    public String getLauncherVersion() {
        return this.launcherVersion;
    }

    public long getTotalMemory() {
        return this.totalMemory;
    }

    public void setJavaVendor(String javaVendor) {
        this.javaVendor = javaVendor;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public void setLauncherVersion(String launcherVersion) {
        this.launcherVersion = launcherVersion;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Telemetry)) {
            return false;
        }
        Telemetry other = (Telemetry)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTotalMemory() != other.getTotalMemory()) {
            return false;
        }
        String this$javaVendor = this.getJavaVendor();
        String other$javaVendor = other.getJavaVendor();
        if (this$javaVendor == null ? other$javaVendor != null : !this$javaVendor.equals(other$javaVendor)) {
            return false;
        }
        String this$javaVersion = this.getJavaVersion();
        String other$javaVersion = other.getJavaVersion();
        if (this$javaVersion == null ? other$javaVersion != null : !this$javaVersion.equals(other$javaVersion)) {
            return false;
        }
        String this$osName = this.getOsName();
        String other$osName = other.getOsName();
        if (this$osName == null ? other$osName != null : !this$osName.equals(other$osName)) {
            return false;
        }
        String this$osVersion = this.getOsVersion();
        String other$osVersion = other.getOsVersion();
        if (this$osVersion == null ? other$osVersion != null : !this$osVersion.equals(other$osVersion)) {
            return false;
        }
        String this$osArch = this.getOsArch();
        String other$osArch = other.getOsArch();
        if (this$osArch == null ? other$osArch != null : !this$osArch.equals(other$osArch)) {
            return false;
        }
        String this$launcherVersion = this.getLauncherVersion();
        String other$launcherVersion = other.getLauncherVersion();
        return !(this$launcherVersion == null ? other$launcherVersion != null : !this$launcherVersion.equals(other$launcherVersion));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Telemetry;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $totalMemory = this.getTotalMemory();
        result = result * 59 + (int)($totalMemory >>> 32 ^ $totalMemory);
        String $javaVendor = this.getJavaVendor();
        result = result * 59 + ($javaVendor == null ? 43 : $javaVendor.hashCode());
        String $javaVersion = this.getJavaVersion();
        result = result * 59 + ($javaVersion == null ? 43 : $javaVersion.hashCode());
        String $osName = this.getOsName();
        result = result * 59 + ($osName == null ? 43 : $osName.hashCode());
        String $osVersion = this.getOsVersion();
        result = result * 59 + ($osVersion == null ? 43 : $osVersion.hashCode());
        String $osArch = this.getOsArch();
        result = result * 59 + ($osArch == null ? 43 : $osArch.hashCode());
        String $launcherVersion = this.getLauncherVersion();
        result = result * 59 + ($launcherVersion == null ? 43 : $launcherVersion.hashCode());
        return result;
    }

    public String toString() {
        return "Telemetry(javaVendor=" + this.getJavaVendor() + ", javaVersion=" + this.getJavaVersion() + ", osName=" + this.getOsName() + ", osVersion=" + this.getOsVersion() + ", osArch=" + this.getOsArch() + ", launcherVersion=" + this.getLauncherVersion() + ", totalMemory=" + this.getTotalMemory() + ")";
    }
}

