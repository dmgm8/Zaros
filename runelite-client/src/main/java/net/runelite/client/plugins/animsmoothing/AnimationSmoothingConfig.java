/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.animsmoothing;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value="animationSmoothing")
public interface AnimationSmoothingConfig
extends Config {
    @ConfigItem(keyName="smoothPlayerAnimations", name="Smooth Player Animations", description="Configures whether the player animations are smooth or not", position=1)
    default public boolean smoothPlayerAnimations() {
        return true;
    }

    @ConfigItem(keyName="smoothNpcAnimations", name="Smooth NPC Animations", description="Configures whether the npc animations are smooth or not", position=2)
    default public boolean smoothNpcAnimations() {
        return true;
    }

    @ConfigItem(keyName="smoothObjectAnimations", name="Smooth Object Animations", description="Configures whether the object animations are smooth or not", position=3)
    default public boolean smoothObjectAnimations() {
        return true;
    }
}

