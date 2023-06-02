/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.xtea;

import java.util.ArrayList;
import java.util.List;
import net.runelite.http.api.xtea.XteaKey;

public class XteaRequest {
    private int revision;
    private List<XteaKey> keys = new ArrayList<XteaKey>();

    public void addKey(XteaKey key) {
        this.keys.add(key);
    }

    public int getRevision() {
        return this.revision;
    }

    public List<XteaKey> getKeys() {
        return this.keys;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public void setKeys(List<XteaKey> keys) {
        this.keys = keys;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof XteaRequest)) {
            return false;
        }
        XteaRequest other = (XteaRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRevision() != other.getRevision()) {
            return false;
        }
        List<XteaKey> this$keys = this.getKeys();
        List<XteaKey> other$keys = other.getKeys();
        return !(this$keys == null ? other$keys != null : !((Object)this$keys).equals(other$keys));
    }

    protected boolean canEqual(Object other) {
        return other instanceof XteaRequest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRevision();
        List<XteaKey> $keys = this.getKeys();
        result = result * 59 + ($keys == null ? 43 : ((Object)$keys).hashCode());
        return result;
    }

    public String toString() {
        return "XteaRequest(revision=" + this.getRevision() + ", keys=" + this.getKeys() + ")";
    }
}

