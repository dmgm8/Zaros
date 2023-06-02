/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.gpu;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.plugins.gpu.config.AntiAliasingMode;
import net.runelite.client.plugins.gpu.config.ColorBlindMode;
import net.runelite.client.plugins.gpu.config.UIScalingMode;

@ConfigGroup(value="gpu")
public interface GpuPluginConfig
extends Config {
    public static final String GROUP = "gpu";

    @Range(max=90)
    @ConfigItem(keyName="drawDistance", name="Draw Distance", description="Draw distance", position=1)
    default public int drawDistance() {
        return 25;
    }

    @ConfigItem(keyName="smoothBanding", name="Remove Color Banding", description="Smooths out the color banding that is present in the CPU renderer", position=2)
    default public boolean smoothBanding() {
        return false;
    }

    @ConfigItem(keyName="antiAliasingMode", name="Anti Aliasing", description="Configures the anti-aliasing mode", position=3)
    default public AntiAliasingMode antiAliasingMode() {
        return AntiAliasingMode.DISABLED;
    }

    @ConfigItem(keyName="uiScalingMode", name="UI scaling mode", description="Sampling function to use for the UI in stretched mode", position=4)
    default public UIScalingMode uiScalingMode() {
        return UIScalingMode.LINEAR;
    }

    @Range(max=100)
    @ConfigItem(keyName="fogDepth", name="Fog depth", description="Distance from the scene edge the fog starts", position=5)
    default public int fogDepth() {
        return 0;
    }

    @ConfigItem(keyName="useComputeShaders", name="Compute Shaders", description="Offloads face sorting to GPU, enabling extended draw distance. Requires plugin restart.", warning="This feature requires OpenGL 4.3 to use. Please check that your GPU supports this.\nRestart the plugin for changes to take effect.", position=6)
    default public boolean useComputeShaders() {
        return true;
    }

    @Range(min=0, max=16)
    @ConfigItem(keyName="anisotropicFilteringLevel", name="Anisotropic Filtering", description="Configures the anisotropic filtering level.", position=7)
    default public int anisotropicFilteringLevel() {
        return 0;
    }

    @ConfigItem(keyName="colorBlindMode", name="Colorblindness Correction", description="Adjusts colors to account for colorblindness", position=8)
    default public ColorBlindMode colorBlindMode() {
        return ColorBlindMode.NONE;
    }

    @ConfigItem(keyName="brightTextures", name="Bright Textures", description="Use old texture lighting method which results in brighter game textures", position=9)
    default public boolean brightTextures() {
        return false;
    }

    @ConfigItem(keyName="unlockFps", name="Unlock FPS", description="Removes the 50 FPS cap for camera movement", position=10)
    default public boolean unlockFps() {
        return false;
    }

    @ConfigItem(keyName="vsyncMode", name="Vsync Mode", description="Method to synchronize frame rate with refresh rate", position=11)
    default public SyncMode syncMode() {
        return SyncMode.ADAPTIVE;
    }

    @ConfigItem(keyName="fpsTarget", name="FPS Target", description="Target FPS when unlock FPS is enabled and Vsync mode is OFF", position=12)
    @Range(min=1, max=999)
    default public int fpsTarget() {
        return 60;
    }

    public static enum SyncMode {
        OFF,
        ON,
        ADAPTIVE;

    }
}

