/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 */
package net.runelite.client.plugins.animsmoothing;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.animsmoothing.AnimationSmoothingConfig;

@PluginDescriptor(name="Animation Smoothing", description="Show smoother player, NPC, and object animations", tags={"npcs", "objects", "players"}, enabledByDefault=false)
public class AnimationSmoothingPlugin
extends Plugin {
    static final String CONFIG_GROUP = "animationSmoothing";
    @Inject
    private Client client;
    @Inject
    private AnimationSmoothingConfig config;

    @Provides
    AnimationSmoothingConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(AnimationSmoothingConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.update();
    }

    @Override
    protected void shutDown() throws Exception {
        this.client.setInterpolatePlayerAnimations(false);
        this.client.setInterpolateNpcAnimations(false);
        this.client.setInterpolateObjectAnimations(false);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals(CONFIG_GROUP)) {
            this.update();
        }
    }

    private void update() {
        this.client.setInterpolatePlayerAnimations(this.config.smoothPlayerAnimations());
        this.client.setInterpolateNpcAnimations(this.config.smoothNpcAnimations());
        this.client.setInterpolateObjectAnimations(this.config.smoothObjectAnimations());
    }
}

