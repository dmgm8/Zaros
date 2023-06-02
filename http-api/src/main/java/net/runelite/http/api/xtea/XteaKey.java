/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.xtea;

import java.util.Arrays;

public class XteaKey {
    private int region;
    private int[] keys;

    public int getRegion() {
        return this.region;
    }

    public int[] getKeys() {
        return this.keys;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public void setKeys(int[] keys) {
        this.keys = keys;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof XteaKey)) {
            return false;
        }
        XteaKey other = (XteaKey)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRegion() != other.getRegion()) {
            return false;
        }
        return Arrays.equals(this.getKeys(), other.getKeys());
    }

    protected boolean canEqual(Object other) {
        return other instanceof XteaKey;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRegion();
        result = result * 59 + Arrays.hashCode(this.getKeys());
        return result;
    }

    public String toString() {
        return "XteaKey(region=" + this.getRegion() + ", keys=" + Arrays.toString(this.getKeys()) + ")";
    }
}

