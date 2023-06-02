/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.events.MenuOpened
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.inventorytags;

import com.google.gson.Gson;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.inventorytags.InventoryTagsConfig;
import net.runelite.client.plugins.inventorytags.InventoryTagsOverlay;
import net.runelite.client.plugins.inventorytags.Tag;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Inventory Tags", description="Add the ability to tag items in your inventory", tags={"highlight", "items", "overlay", "tagging"}, enabledByDefault=false)
public class InventoryTagsPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(InventoryTagsPlugin.class);
    private static final String ITEM_KEY_PREFIX = "item_";
    private static final String TAG_KEY_PREFIX = "tag_";
    private static final String SETNAME_GROUP_1 = "Group 1";
    private static final String SETNAME_GROUP_2 = "Group 2";
    private static final String SETNAME_GROUP_3 = "Group 3";
    private static final String SETNAME_GROUP_4 = "Group 4";
    private static final String SETNAME_GROUP_5 = "Group 5";
    private static final String SETNAME_GROUP_6 = "Group 6";
    @Inject
    private Client client;
    @Inject
    private ConfigManager configManager;
    @Inject
    private InventoryTagsConfig config;
    @Inject
    private InventoryTagsOverlay overlay;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private Gson gson;
    @Inject
    private ColorPickerManager colorPickerManager;

    @Provides
    InventoryTagsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(InventoryTagsConfig.class);
    }

    @Override
    protected void startUp() {
        this.overlayManager.add(this.overlay);
        this.convertConfig();
    }

    @Override
    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
    }

    Tag getTag(int itemId) {
        String tag = this.configManager.getConfiguration("inventorytags", TAG_KEY_PREFIX + itemId);
        if (tag == null || tag.isEmpty()) {
            return null;
        }
        return (Tag)this.gson.fromJson(tag, Tag.class);
    }

    void setTag(int itemId, Tag tag) {
        String json = this.gson.toJson((Object)tag);
        this.configManager.setConfiguration("inventorytags", TAG_KEY_PREFIX + itemId, json);
    }

    void unsetTag(int itemId) {
        this.configManager.unsetConfiguration("inventorytags", TAG_KEY_PREFIX + itemId);
    }

    private void convertConfig() {
        String migrated = this.configManager.getConfiguration("inventorytags", "migrated");
        if (migrated != null) {
            return;
        }
        List<String> keys = this.configManager.getConfigurationKeys("inventorytags.item_");
        for (String key : keys) {
            Color color;
            String[] str = key.split("\\.", 2);
            if (str.length != 2) continue;
            int itemId = Integer.parseInt(str[1].substring(ITEM_KEY_PREFIX.length()));
            String tag = this.configManager.getConfiguration(str[0], str[1]);
            if (tag == null || (color = this.getGroupNameColor(tag)) == null) continue;
            Tag t = new Tag();
            t.color = color;
            this.setTag(itemId, t);
            log.debug("Converted tag {} {} -> {}", new Object[]{itemId, tag, t});
        }
        this.configManager.setConfiguration("inventorytags", "migrated", "1");
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (configChanged.getGroup().equals("inventorytags")) {
            this.overlay.invalidateCache();
        }
    }

    @Subscribe
    public void onMenuOpened(MenuOpened event) {
        if (!this.client.isKeyPressed(81)) {
            return;
        }
        MenuEntry[] entries = event.getMenuEntries();
        for (int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            Widget w = entry.getWidget();
            if (w == null || WidgetInfo.TO_GROUP((int)w.getId()) != 149 || !"Examine".equals(entry.getOption()) || entry.getIdentifier() != 10) continue;
            int itemId = w.getItemId();
            Tag tag = this.getTag(itemId);
            this.client.createMenuEntry(idx).setOption("Pick inventory tag").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(e -> {
                Color color = tag == null ? Color.WHITE : tag.color;
                SwingUtilities.invokeLater(() -> {
                    RuneliteColorPicker colorPicker = this.colorPickerManager.create(null, color, "Inventory Tag", true);
                    colorPicker.setOnClose(c -> {
                        Tag t = new Tag();
                        t.color = c;
                        this.setTag(itemId, t);
                    });
                    colorPicker.setVisible(true);
                });
            });
            if (tag != null) {
                this.client.createMenuEntry(idx).setOption("Clear inventory tag").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(e -> this.unsetTag(itemId));
            }
            for (Color color : this.invColors()) {
                this.client.createMenuEntry(idx).setOption(ColorUtil.prependColorTag("Inventory tag", color)).setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(e -> {
                    Tag t = new Tag();
                    t.color = color;
                    this.setTag(itemId, t);
                });
            }
        }
    }

    Color getGroupNameColor(String name) {
        switch (name) {
            case "Group 1": {
                return this.config.getGroup1Color();
            }
            case "Group 2": {
                return this.config.getGroup2Color();
            }
            case "Group 3": {
                return this.config.getGroup3Color();
            }
            case "Group 4": {
                return this.config.getGroup4Color();
            }
            case "Group 5": {
                return this.config.getGroup5Color();
            }
            case "Group 6": {
                return this.config.getGroup6Color();
            }
        }
        return null;
    }

    private List<Color> invColors() {
        ArrayList<Color> colors = new ArrayList<Color>();
        ItemContainer container = this.client.getItemContainer(InventoryID.INVENTORY);
        for (Item item : container.getItems()) {
            Tag tag = this.getTag(item.getId());
            if (tag == null || tag.color == null || colors.contains(tag.color)) continue;
            colors.add(tag.color);
        }
        return colors;
    }
}

