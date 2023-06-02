/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.config;

import java.util.ArrayList;
import java.util.List;
import net.runelite.http.api.config.ConfigEntry;

public class Configuration {
    private List<ConfigEntry> config = new ArrayList<ConfigEntry>();

    public List<ConfigEntry> getConfig() {
        return this.config;
    }

    public void setConfig(List<ConfigEntry> config) {
        this.config = config;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Configuration)) {
            return false;
        }
        Configuration other = (Configuration)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<ConfigEntry> this$config = this.getConfig();
        List<ConfigEntry> other$config = other.getConfig();
        return !(this$config == null ? other$config != null : !((Object)this$config).equals(other$config));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Configuration;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<ConfigEntry> $config = this.getConfig();
        result = result * 59 + ($config == null ? 43 : ((Object)$config).hashCode());
        return result;
    }

    public String toString() {
        return "Configuration(config=" + this.getConfig() + ")";
    }

    public Configuration(List<ConfigEntry> config) {
        this.config = config;
    }
}

