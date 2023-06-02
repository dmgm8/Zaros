/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  org.apache.commons.lang3.math.NumberUtils
 */
package net.runelite.client.plugins.banktags.tabs;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.banktags.tabs.TagTab;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.math.NumberUtils;

@Singleton
class TabManager {
    private final List<TagTab> tabs = new ArrayList<TagTab>();
    private final ConfigManager configManager;

    @Inject
    private TabManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    void add(TagTab tagTab) {
        if (!this.contains(tagTab.getTag())) {
            this.tabs.add(tagTab);
        }
    }

    void clear() {
        this.tabs.forEach(t -> t.setHidden(true));
        this.tabs.clear();
    }

    TagTab find(String tag) {
        Optional<TagTab> first = this.tabs.stream().filter(t -> t.getTag().equals(Text.standardize(tag))).findAny();
        return first.orElse(null);
    }

    List<String> getAllTabs() {
        return Text.fromCSV((String)MoreObjects.firstNonNull((Object)this.configManager.getConfiguration("banktags", "tagtabs"), (Object)""));
    }

    TagTab load(String tag) {
        TagTab tagTab = this.find(tag);
        if (tagTab == null) {
            tag = Text.standardize(tag);
            String item = this.configManager.getConfiguration("banktags", "icon_" + tag);
            int itemid = NumberUtils.toInt((String)item, (int)952);
            tagTab = new TagTab(itemid, tag);
        }
        return tagTab;
    }

    void swap(String tagToMove, String tagDestination) {
        tagToMove = Text.standardize(tagToMove);
        tagDestination = Text.standardize(tagDestination);
        if (this.contains(tagToMove) && this.contains(tagDestination)) {
            Collections.swap(this.tabs, this.indexOf(tagToMove), this.indexOf(tagDestination));
        }
    }

    void insert(String tagToMove, String tagDestination) {
        tagToMove = Text.standardize(tagToMove);
        tagDestination = Text.standardize(tagDestination);
        if (this.contains(tagToMove) && this.contains(tagDestination)) {
            this.tabs.add(this.indexOf(tagDestination), this.tabs.remove(this.indexOf(tagToMove)));
        }
    }

    void remove(String tag) {
        TagTab tagTab = this.find(tag);
        if (tagTab != null) {
            tagTab.setHidden(true);
            this.tabs.remove(tagTab);
            this.removeIcon(tag);
        }
    }

    void save() {
        String tags = Text.toCSV(this.tabs.stream().map(TagTab::getTag).collect(Collectors.toList()));
        this.configManager.setConfiguration("banktags", "tagtabs", tags);
    }

    void removeIcon(String tag) {
        this.configManager.unsetConfiguration("banktags", "icon_" + Text.standardize(tag));
    }

    void setIcon(String tag, String icon) {
        this.configManager.setConfiguration("banktags", "icon_" + Text.standardize(tag), icon);
    }

    int size() {
        return this.tabs.size();
    }

    private boolean contains(String tag) {
        return this.tabs.stream().anyMatch(t -> t.getTag().equals(tag));
    }

    private int indexOf(TagTab tagTab) {
        return this.tabs.indexOf(tagTab);
    }

    private int indexOf(String tag) {
        return this.indexOf(this.find(tag));
    }

    public List<TagTab> getTabs() {
        return this.tabs;
    }
}

