/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigPatch {
    Map<String, String> edit = new HashMap<String, String>();
    Set<String> unset = new HashSet<String>();

    public Map<String, String> getEdit() {
        return this.edit;
    }

    public Set<String> getUnset() {
        return this.unset;
    }

    public void setEdit(Map<String, String> edit) {
        this.edit = edit;
    }

    public void setUnset(Set<String> unset) {
        this.unset = unset;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConfigPatch)) {
            return false;
        }
        ConfigPatch other = (ConfigPatch)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Map<String, String> this$edit = this.getEdit();
        Map<String, String> other$edit = other.getEdit();
        if (this$edit == null ? other$edit != null : !((Object)this$edit).equals(other$edit)) {
            return false;
        }
        Set<String> this$unset = this.getUnset();
        Set<String> other$unset = other.getUnset();
        return !(this$unset == null ? other$unset != null : !((Object)this$unset).equals(other$unset));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ConfigPatch;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Map<String, String> $edit = this.getEdit();
        result = result * 59 + ($edit == null ? 43 : ((Object)$edit).hashCode());
        Set<String> $unset = this.getUnset();
        result = result * 59 + ($unset == null ? 43 : ((Object)$unset).hashCode());
        return result;
    }

    public String toString() {
        return "ConfigPatch(edit=" + this.getEdit() + ", unset=" + this.getUnset() + ")";
    }
}

