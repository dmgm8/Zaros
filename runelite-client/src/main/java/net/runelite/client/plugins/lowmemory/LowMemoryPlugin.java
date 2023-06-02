/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.events.BeforeRender
 */
package net.runelite.client.plugins.lowmemory;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.BeforeRender;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.lowmemory.LowMemoryConfig;

@PluginDescriptor(name="Low Detail", description="Turn off ground decorations and certain textures, reducing memory usage", tags={"memory", "usage", "ground", "decorations"}, enabledByDefault=false)
public class LowMemoryPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private LowMemoryConfig config;

    @Override
    protected void startUp() {
        this.clientThread.invoke(() -> {
            if (this.client.getGameState().getState() >= GameState.LOGIN_SCREEN.getState()) {
                this.client.changeMemoryMode(this.config.lowDetail());
                return true;
            }
            return false;
        });
    }

    @Override
    protected void shutDown() {
        this.clientThread.invoke(() -> this.client.changeMemoryMode(false));
    }

    @Provides
    LowMemoryConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(LowMemoryConfig.class);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (configChanged.getGroup().equals("lowmemory")) {
            this.clientThread.invoke(() -> {
                if (this.client.getGameState().getState() >= GameState.LOGIN_SCREEN.getState()) {
                    this.client.changeMemoryMode(this.config.lowDetail());
                }
            });
        }
    }

    @Subscribe
    public void onBeforeRender(BeforeRender beforeRender) {
        this.client.getScene().setMinLevel(this.config.hideLowerPlanes() ? this.client.getPlane() : 0);
    }
}

