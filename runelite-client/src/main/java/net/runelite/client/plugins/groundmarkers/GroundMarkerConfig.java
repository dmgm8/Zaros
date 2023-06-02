/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.groundmarkers;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="groundMarker")
public interface GroundMarkerConfig
extends Config {
    public static final String GROUND_MARKER_CONFIG_GROUP = "groundMarker";
    public static final String SHOW_IMPORT_EXPORT_KEY_NAME = "showImportExport";
    public static final String SHOW_CLEAR_KEY_NAME = "showClear";

    @Alpha
    @ConfigItem(keyName="markerColor", name="Tile color", description="Configures the color of marked tile")
    default public Color markerColor() {
        return Color.YELLOW;
    }

    @ConfigItem(keyName="rememberTileColors", name="Remember color per tile", description="Color tiles using the color from time of placement")
    default public boolean rememberTileColors() {
        return false;
    }

    @ConfigItem(keyName="drawOnMinimap", name="Draw tiles on minimap", description="Configures whether marked tiles should be drawn on minimap")
    default public boolean drawTileOnMinimmap() {
        return false;
    }

    @ConfigItem(keyName="showImportExport", name="Show Import/Export options", description="Show the Import/Export options on the minimap right-click menu")
    default public boolean showImportExport() {
        return true;
    }

    @ConfigItem(keyName="showClear", name="Show Clear option", description="Show the Clear option on the minimap right-click menu, which deletes all currently loaded markers")
    default public boolean showClear() {
        return false;
    }

    @ConfigItem(keyName="borderWidth", name="Border Width", description="Width of the marked tile border")
    default public double borderWidth() {
        return 2.0;
    }

    @ConfigItem(keyName="fillOpacity", name="Fill Opacity", description="Opacity of the tile fill color")
    default public int fillOpacity() {
        return 50;
    }
}

