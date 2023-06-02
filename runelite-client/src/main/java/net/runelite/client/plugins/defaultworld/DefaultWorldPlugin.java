/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.World
 *  net.runelite.api.events.WorldChanged
 *  net.runelite.http.api.worlds.World
 *  net.runelite.http.api.worlds.WorldResult
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.defaultworld;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.WorldChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.defaultworld.DefaultWorldConfig;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Default World", description="Enable a default world to be selected when launching the client", tags={"home"}, forceDisabled=true)
public class DefaultWorldPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(DefaultWorldPlugin.class);
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private DefaultWorldConfig config;
    @Inject
    private WorldService worldService;

    @Override
    protected void startUp() {
        this.clientThread.invokeLater(() -> {
            if (this.client.getGameState().getState() < GameState.LOGIN_SCREEN.getState()) {
                return false;
            }
            this.applyWorld();
            return true;
        });
    }

    @Override
    protected void shutDown() {
    }

    @Provides
    DefaultWorldConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(DefaultWorldConfig.class);
    }

    @Subscribe
    public void onSessionOpen(SessionOpen event) {
        this.clientThread.invokeLater(this::applyWorld);
    }

    @Subscribe
    public void onWorldChanged(WorldChanged worldChanged) {
        int world = this.client.getWorld();
        this.config.lastWorld(world);
        log.debug("Saving last world {}", (Object)world);
    }

    private void applyWorld() {
        int correctedWorld;
        if (this.client.getGameState() != GameState.LOGIN_SCREEN) {
            return;
        }
        int newWorld = this.config.useLastWorld() ? this.config.lastWorld() : this.config.getWorld();
        int n = correctedWorld = newWorld < 300 ? newWorld + 300 : newWorld;
        if (correctedWorld <= 300 || this.client.getWorld() == correctedWorld) {
            return;
        }
        WorldResult worldResult = this.worldService.getWorlds();
        if (worldResult == null) {
            log.warn("Failed to lookup worlds.");
            return;
        }
        World world = worldResult.findWorld(correctedWorld);
        if (world == null) {
            log.warn("World {} not found.", (Object)correctedWorld);
            return;
        }
        net.runelite.api.World rsWorld = this.client.createWorld();
        rsWorld.setActivity(world.getActivity());
        rsWorld.setAddress(world.getAddress());
        rsWorld.setId(world.getId());
        rsWorld.setPlayerCount(world.getPlayers());
        rsWorld.setLocation(world.getLocation());
        rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));
        this.client.changeWorld(rsWorld);
        log.debug("Applied new world {}", (Object)correctedWorld);
    }
}

