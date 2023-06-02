/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.config;

import java.util.List;

public interface SearchablePlugin {
    public String getSearchableName();

    public List<String> getKeywords();

    default public boolean isPinned() {
        return false;
    }
}

