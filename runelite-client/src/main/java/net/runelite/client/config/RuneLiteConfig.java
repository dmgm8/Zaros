/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Constants
 */
package net.runelite.client.config;

import java.awt.Color;
import java.awt.Dimension;
import net.runelite.api.Constants;
import net.runelite.client.Notifier;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.ExpandResizeType;
import net.runelite.client.config.FlashNotification;
import net.runelite.client.config.FontType;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;
import net.runelite.client.config.RequestFocusType;
import net.runelite.client.config.TooltipPositionType;
import net.runelite.client.config.Units;
import net.runelite.client.config.WarningOnExit;
import net.runelite.client.ui.ContainableFrame;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.util.OSType;

@ConfigGroup(value="runelite")
public interface RuneLiteConfig
extends Config {
    public static final String GROUP_NAME = "runelite";
    @ConfigSection(name="Window Settings", description="Settings relating to the client's window and frame", position=0)
    public static final String windowSettings = "windowSettings";
    @ConfigSection(name="Notification Settings", description="Settings relating to notifications", position=1)
    public static final String notificationSettings = "notificationSettings";
    @ConfigSection(name="Overlay Settings", description="Settings relating to fonts", position=2)
    public static final String overlaySettings = "overlaySettings";

    @ConfigItem(keyName="gameSize", name="Game size", description="The game will resize to this resolution upon starting the client", position=10, section="windowSettings")
    default public Dimension gameSize() {
        return Constants.GAME_FIXED_SIZE;
    }

    @ConfigItem(keyName="automaticResizeType", name="Resize type", description="Choose how the window should resize when opening and closing panels", position=11, section="windowSettings")
    default public ExpandResizeType automaticResizeType() {
        return ExpandResizeType.KEEP_GAME_SIZE;
    }

    @ConfigItem(keyName="lockWindowSize", name="Lock window size", description="Determines if the window resizing is allowed or not", position=12, section="windowSettings")
    default public boolean lockWindowSize() {
        return false;
    }

    @ConfigItem(keyName="containInScreen2", name="Contain in screen", description="Makes the client stay contained in the screen when attempted to move out of it.<br>Note: 'Always' only works if custom chrome is enabled.", position=13, section="windowSettings")
    default public ContainableFrame.Mode containInScreen() {
        return ContainableFrame.Mode.RESIZING;
    }

    @ConfigItem(keyName="rememberScreenBounds", name="Remember client position", description="Save the position and size of the client after exiting", position=14, section="windowSettings")
    default public boolean rememberScreenBounds() {
        return true;
    }

    @ConfigItem(keyName="uiEnableCustomChrome", name="Enable custom window chrome", description="Use RuneLite's custom window title and borders.", warning="Please restart your client after changing this setting", position=15, section="windowSettings")
    default public boolean enableCustomChrome() {
        return OSType.getOSType() == OSType.Windows;
    }

    @Range(min=10, max=100)
    @ConfigItem(keyName="uiWindowOpacity", name="Window opacity", description="Set the windows opacity. Requires \"Enable custom window chrome\" to be enabled.", position=16, section="windowSettings")
    default public int windowOpacity() {
        return 100;
    }

    @ConfigItem(keyName="gameAlwaysOnTop", name="Always on top", description="The game will always be on the top of the screen", position=17, section="windowSettings")
    default public boolean gameAlwaysOnTop() {
        return false;
    }

    @ConfigItem(keyName="warningOnExit", name="Exit warning", description="Shows a warning popup when trying to exit the client", position=18, section="windowSettings")
    default public WarningOnExit warningOnExit() {
        return WarningOnExit.LOGGED_IN;
    }

    @ConfigItem(keyName="usernameInTitle", name="Show display name in title", description="Toggles displaying of local player's display name in client title", position=19, section="windowSettings")
    default public boolean usernameInTitle() {
        return true;
    }

    @ConfigItem(keyName="trayIcon", name="Enable tray icon", description="Enables icon in system tray", warning="Disabling this may limit your ability to receive tray notifications.\nPlease restart your client after changing this setting.", position=20, section="notificationSettings")
    default public boolean enableTrayIcon() {
        return true;
    }

    @ConfigItem(keyName="notificationTray", name="Enable tray notifications", description="Enables tray notifications", position=21, section="notificationSettings")
    default public boolean enableTrayNotifications() {
        return true;
    }

    @ConfigItem(keyName="notificationRequestFocus", name="Request focus", description="Configures the window focus request type on notification", position=22, section="notificationSettings")
    default public RequestFocusType notificationRequestFocus() {
        return RequestFocusType.OFF;
    }

    @ConfigItem(keyName="notificationSound", name="Notification sound", description="Enables the playing of a beep sound when notifications are displayed", position=23, section="notificationSettings")
    default public Notifier.NativeCustomOff notificationSound() {
        return Notifier.NativeCustomOff.NATIVE;
    }

    @ConfigItem(keyName="notificationTimeout", name="Notification timeout", description="How long notification will be shown in milliseconds. A value of 0 will make it use the system configuration. (Linux only)", position=24, section="notificationSettings")
    @Units(value="ms")
    default public int notificationTimeout() {
        return 10000;
    }

    @ConfigItem(keyName="notificationGameMessage", name="Game message notifications", description="Adds a notification message to the chatbox", position=25, section="notificationSettings")
    default public boolean enableGameMessageNotification() {
        return false;
    }

    @ConfigItem(keyName="flashNotification", name="Flash", description="Flashes the game frame as a notification", position=26, section="notificationSettings")
    default public FlashNotification flashNotification() {
        return FlashNotification.DISABLED;
    }

    @ConfigItem(keyName="notificationFocused", name="Send notifications when focused", description="Toggles all notifications for when the client is focused", position=27, section="notificationSettings")
    default public boolean sendNotificationsWhenFocused() {
        return false;
    }

    @Alpha
    @ConfigItem(keyName="notificationFlashColor", name="Notification Flash", description="Sets the color of the notification flashes.", position=28, section="notificationSettings")
    default public Color notificationFlashColor() {
        return new Color(255, 0, 0, 70);
    }

    @ConfigItem(keyName="fontType", name="Dynamic Overlay Font", description="Configures what font type is used for in-game overlays such as player name, ground items, etc.", position=30, section="overlaySettings")
    default public FontType fontType() {
        return FontType.SMALL;
    }

    @ConfigItem(keyName="tooltipFontType", name="Tooltip Font", description="Configures what font type is used for in-game tooltips such as food stats, NPC names, etc.", position=31, section="overlaySettings")
    default public FontType tooltipFontType() {
        return FontType.SMALL;
    }

    @ConfigItem(keyName="interfaceFontType", name="Interface Font", description="Configures what font type is used for in-game interface overlays such as panels, opponent info, clue scrolls etc.", position=32, section="overlaySettings")
    default public FontType interfaceFontType() {
        return FontType.REGULAR;
    }

    @ConfigItem(keyName="infoboxFontType", name="Infobox Font", description="Configures what font type is used for infoboxes.", position=33, section="overlaySettings")
    default public FontType infoboxFontType() {
        return FontType.REGULAR;
    }

    @ConfigItem(keyName="menuEntryShift", name="Require Shift for overlay menu", description="Overlay right-click menu will require shift to be added", position=34, section="overlaySettings")
    default public boolean menuEntryShift() {
        return true;
    }

    @ConfigItem(keyName="tooltipPosition", name="Tooltip Position", description="Configures whether to show the tooltip above or under the cursor", position=35, section="overlaySettings")
    default public TooltipPositionType tooltipPosition() {
        return TooltipPositionType.UNDER_CURSOR;
    }

    @ConfigItem(keyName="infoBoxVertical", name="Display infoboxes vertically", description="Toggles the infoboxes to display vertically", position=40, section="overlaySettings", hidden=true)
    default public boolean infoBoxVertical() {
        return false;
    }

    @ConfigItem(keyName="infoBoxSize", name="Infobox size", description="Configures the size of each infobox in pixels", position=42, section="overlaySettings")
    @Units(value="px")
    default public int infoBoxSize() {
        return 35;
    }

    @ConfigItem(keyName="infoBoxTextOutline", name="Outline infobox text", description="Draw a full outline instead of a simple shadow for infobox text", position=43, section="overlaySettings")
    default public boolean infoBoxTextOutline() {
        return false;
    }

    @Alpha
    @ConfigItem(keyName="overlayBackgroundColor", name="Overlay Color", description="Configures the background color of infoboxes and overlays", position=44, section="overlaySettings")
    default public Color overlayBackgroundColor() {
        return ComponentConstants.STANDARD_BACKGROUND_COLOR;
    }

    @ConfigItem(keyName="sidebarToggleKey", name="Sidebar Toggle Key", description="The key that will toggle the sidebar (accepts modifiers)", position=45, section="windowSettings")
    default public Keybind sidebarToggleKey() {
        return new Keybind(122, 128);
    }

    @ConfigItem(keyName="panelToggleKey", name="Plugin Panel Toggle Key", description="The key that will toggle the current or last opened plugin panel (accepts modifiers)", position=46, section="windowSettings")
    default public Keybind panelToggleKey() {
        return new Keybind(123, 128);
    }

    @ConfigItem(keyName="blockExtraMouseButtons", name="Block extra mouse buttons", description="Blocks extra mouse buttons (4 and above)", position=50)
    default public boolean blockExtraMouseButtons() {
        return true;
    }

    @ConfigItem(keyName="useWikiItemPrices", name="Use actively traded price", description="Use actively traded prices, sourced from the RuneScape wiki, for item prices", position=51)
    default public boolean useWikiItemPrices() {
        return true;
    }

    @ConfigItem(keyName="dragHotkey", name="Drag Hotkey", description="Configures the hotkey used to drag UI elements around", position=52)
    default public Keybind dragHotkey() {
        return Keybind.ALT;
    }
}

