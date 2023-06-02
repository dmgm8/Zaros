/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.camera;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.plugins.camera.ControlFunction;

@ConfigGroup(value="zoom")
public interface CameraConfig
extends Config {
    public static final int OUTER_LIMIT_MIN = -400;
    public static final int OUTER_LIMIT_MAX = 400;
    public static final int INNER_ZOOM_LIMIT = 1004;

    @ConfigItem(keyName="inner", name="Expand inner zoom limit", description="Configures whether or not the inner zoom limit is reduced", position=1)
    default public boolean innerLimit() {
        return false;
    }

    @Range(min=-400, max=400)
    @ConfigItem(keyName="outerLimit2", name="Expand outer zoom limit", description="Configures how much the outer zoom limit is adjusted", position=2)
    default public int outerLimit() {
        return 0;
    }

    @ConfigItem(keyName="relaxCameraPitch", name="Vertical camera", description="Relax the camera's upper pitch limit", position=3)
    default public boolean relaxCameraPitch() {
        return false;
    }

    @ConfigItem(keyName="controlFunction", name="Control Function", description="Configures the zoom function when control is pressed", position=4)
    default public ControlFunction controlFunction() {
        return ControlFunction.NONE;
    }

    @ConfigItem(keyName="ctrlZoomValue", name="Reset zoom position", description="Position of zoom when it is reset", position=5)
    @Range(min=-400, max=1004)
    default public int ctrlZoomValue() {
        return 512;
    }

    @ConfigItem(keyName="zoomIncrement", name="Zoom Speed", description="Speed of zoom", position=6)
    default public int zoomIncrement() {
        return 25;
    }

    @ConfigItem(keyName="rightClickMovesCamera", name="Right click moves camera", description="Remaps right click to middle mouse click if there are no menu options", position=7)
    default public boolean rightClickMovesCamera() {
        return false;
    }

    @ConfigItem(keyName="ignoreExamine", name="Ignore Examine", description="Ignore the Examine menu entry", position=8)
    default public boolean ignoreExamine() {
        return false;
    }

    @ConfigItem(keyName="middleClickMenu", name="Middle-button opens menu", description="Middle-mouse button always opens the menu", position=9)
    default public boolean middleClickMenu() {
        return false;
    }

    @ConfigItem(keyName="compassLookPreservePitch", name="Preserve pitch on compass look", description="Preserves the current pitch value (vertical angle) when using the compass look options.", position=11)
    default public boolean compassLookPreservePitch() {
        return false;
    }

    @ConfigItem(keyName="invertYaw", name="Invert Yaw", description="Makes moving the camera horizontally with the mouse backwards", position=12)
    default public boolean invertYaw() {
        return false;
    }

    @ConfigItem(keyName="invertPitch", name="Invert Pitch", description="Makes moving the camera vertically with the mouse backwards", position=13)
    default public boolean invertPitch() {
        return false;
    }

    @ConfigItem(keyName="preserveYaw", name="Preserve yaw on world hop", description="Preserves the camera yaw (left/right) when world hopping.", position=14)
    default public boolean preserveYaw() {
        return false;
    }
}

