/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 */
package net.runelite.client.ui.components.colorpicker;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.WindowEvent;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;

@Singleton
public class ColorPickerManager {
    private final ConfigManager configManager;
    private RuneliteColorPicker currentPicker;

    @Inject
    private ColorPickerManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public RuneliteColorPicker create(Window owner, Color previousColor, String title, boolean alphaHidden) {
        if (this.currentPicker != null) {
            this.currentPicker.dispatchEvent(new WindowEvent(this.currentPicker, 201));
        }
        this.currentPicker = new RuneliteColorPicker(owner, previousColor, title, alphaHidden, this.configManager, this);
        return this.currentPicker;
    }

    void setCurrentPicker(RuneliteColorPicker currentPicker) {
        this.currentPicker = currentPicker;
    }

    RuneliteColorPicker getCurrentPicker() {
        return this.currentPicker;
    }
}

