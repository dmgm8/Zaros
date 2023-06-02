/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.pestcontrol;

import net.runelite.api.widgets.WidgetInfo;

enum Portal {
    PURPLE(WidgetInfo.PEST_CONTROL_PURPLE_SHIELD, WidgetInfo.PEST_CONTROL_PURPLE_HEALTH, WidgetInfo.PEST_CONTROL_PURPLE_ICON),
    BLUE(WidgetInfo.PEST_CONTROL_BLUE_SHIELD, WidgetInfo.PEST_CONTROL_BLUE_HEALTH, WidgetInfo.PEST_CONTROL_BLUE_ICON),
    YELLOW(WidgetInfo.PEST_CONTROL_YELLOW_SHIELD, WidgetInfo.PEST_CONTROL_YELLOW_HEALTH, WidgetInfo.PEST_CONTROL_YELLOW_ICON),
    RED(WidgetInfo.PEST_CONTROL_RED_SHIELD, WidgetInfo.PEST_CONTROL_RED_HEALTH, WidgetInfo.PEST_CONTROL_RED_ICON);

    private final WidgetInfo shield;
    private final WidgetInfo hitpoints;
    private final WidgetInfo icon;

    private Portal(WidgetInfo shield, WidgetInfo hitpoints, WidgetInfo icon) {
        this.shield = shield;
        this.hitpoints = hitpoints;
        this.icon = icon;
    }

    public WidgetInfo getShield() {
        return this.shield;
    }

    public WidgetInfo getHitpoints() {
        return this.hitpoints;
    }

    public WidgetInfo getIcon() {
        return this.icon;
    }

    public String toString() {
        return "Portal." + this.name() + "(shield=" + (Object)this.getShield() + ", hitpoints=" + (Object)this.getHitpoints() + ", icon=" + (Object)this.getIcon() + ")";
    }
}

