/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.interfacestyles;

import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.interfacestyles.Skin;

enum WidgetOverride {
    FIXED_CORNER_TOP_LEFT_2005(Skin.AROUND_2005, "1026", WidgetInfo.FIXED_VIEWPORT_COMBAT_TAB),
    FIXED_CORNER_TOP_RIGHT_2005(Skin.AROUND_2005, "1027", WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB),
    FIXED_CORNER_BOTTOM_LEFT_2005(Skin.AROUND_2005, "1028", WidgetInfo.FIXED_VIEWPORT_FRIENDS_CHAT_TAB),
    FIXED_CORNER_BOTTOM_RIGHT_2005(Skin.AROUND_2005, "1029", WidgetInfo.FIXED_VIEWPORT_MUSIC_TAB),
    FIXED_TOP_LEFT_2005(Skin.AROUND_2005, "1030_top_left", WidgetInfo.FIXED_VIEWPORT_STATS_TAB, WidgetInfo.FIXED_VIEWPORT_QUESTS_TAB),
    FIXED_TOP_RIGHT_2005(Skin.AROUND_2005, "1030_top_right", WidgetInfo.FIXED_VIEWPORT_EQUIPMENT_TAB, WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB),
    FIXED_TOP_MIDDLE_2005(Skin.AROUND_2005, "1030_top_middle", WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB),
    FIXED_BOTTOM_LEFT_2005(Skin.AROUND_2005, "1030_bottom_left", WidgetInfo.FIXED_VIEWPORT_FRIENDS_TAB, WidgetInfo.FIXED_VIEWPORT_IGNORES_TAB),
    FIXED_BOTTOM_RIGHT_2005(Skin.AROUND_2005, "1030_bottom_middle", WidgetInfo.FIXED_VIEWPORT_LOGOUT_TAB),
    FIXED_BOTTOM_MIDDLE_2005(Skin.AROUND_2005, "1030_bottom_right", WidgetInfo.FIXED_VIEWPORT_OPTIONS_TAB, WidgetInfo.FIXED_VIEWPORT_EMOTES_TAB);

    private Skin skin;
    private String name;
    private WidgetInfo[] widgetInfo;

    private WidgetOverride(Skin skin, String name, WidgetInfo ... widgetInfo) {
        this.skin = skin;
        this.name = name;
        this.widgetInfo = widgetInfo;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public String getName() {
        return this.name;
    }

    public WidgetInfo[] getWidgetInfo() {
        return this.widgetInfo;
    }
}

