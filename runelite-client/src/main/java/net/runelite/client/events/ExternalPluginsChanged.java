/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.events;

import java.util.List;
import net.runelite.client.externalplugins.ExternalPluginManifest;

public final class ExternalPluginsChanged {
    private final List<ExternalPluginManifest> loadedManifest;

    public ExternalPluginsChanged(List<ExternalPluginManifest> loadedManifest) {
        this.loadedManifest = loadedManifest;
    }

    public List<ExternalPluginManifest> getLoadedManifest() {
        return this.loadedManifest;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExternalPluginsChanged)) {
            return false;
        }
        ExternalPluginsChanged other = (ExternalPluginsChanged)o;
        List<ExternalPluginManifest> this$loadedManifest = this.getLoadedManifest();
        List<ExternalPluginManifest> other$loadedManifest = other.getLoadedManifest();
        return !(this$loadedManifest == null ? other$loadedManifest != null : !((Object)this$loadedManifest).equals(other$loadedManifest));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<ExternalPluginManifest> $loadedManifest = this.getLoadedManifest();
        result = result * 59 + ($loadedManifest == null ? 43 : ((Object)$loadedManifest).hashCode());
        return result;
    }

    public String toString() {
        return "ExternalPluginsChanged(loadedManifest=" + this.getLoadedManifest() + ")";
    }
}

