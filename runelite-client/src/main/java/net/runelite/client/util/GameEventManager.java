/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Renderable
 *  net.runelite.api.Scene
 *  net.runelite.api.Tile
 *  net.runelite.api.TileItem
 *  net.runelite.api.events.DecorativeObjectSpawned
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GroundObjectSpawned
 *  net.runelite.api.events.ItemContainerChanged
 *  net.runelite.api.events.ItemSpawned
 *  net.runelite.api.events.NpcSpawned
 *  net.runelite.api.events.PlayerSpawned
 *  net.runelite.api.events.WallObjectSpawned
 */
package net.runelite.client.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;

@Singleton
public class GameEventManager {
    private final EventBus eventBus = new EventBus();
    private final Client client;
    private final ClientThread clientThread;

    @Inject
    private GameEventManager(Client client, ClientThread clientThread) {
        this.client = client;
        this.clientThread = clientThread;
    }

    private void forEachTile(Consumer<Tile> consumer) {
        Scene scene = this.client.getScene();
        Tile[][][] tiles = scene.getTiles();
        for (int z = 0; z < 4; ++z) {
            for (int x = 0; x < 104; ++x) {
                for (int y = 0; y < 104; ++y) {
                    Tile tile = tiles[z][x][y];
                    if (tile == null) continue;
                    consumer.accept(tile);
                }
            }
        }
    }

    public void simulateGameEvents(Object subscriber) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        this.clientThread.invoke(() -> {
            this.eventBus.register(subscriber);
            for (ItemContainer itemContainer : this.client.getItemContainers()) {
                this.eventBus.post(new ItemContainerChanged(itemContainer.getId(), itemContainer));
            }
            for (NPC nPC : this.client.getCachedNPCs()) {
                if (nPC == null) continue;
                NpcSpawned npcSpawned = new NpcSpawned(nPC);
                this.eventBus.post(npcSpawned);
            }
            for (NPC nPC : this.client.getCachedPlayers()) {
                if (nPC == null) continue;
                PlayerSpawned playerSpawned = new PlayerSpawned((Player)nPC);
                this.eventBus.post(playerSpawned);
            }
            this.forEachTile(tile -> {
                Optional.ofNullable(tile.getWallObject()).ifPresent(object -> {
                    WallObjectSpawned objectSpawned = new WallObjectSpawned();
                    objectSpawned.setTile(tile);
                    objectSpawned.setWallObject(object);
                    this.eventBus.post(objectSpawned);
                });
                Optional.ofNullable(tile.getDecorativeObject()).ifPresent(object -> {
                    DecorativeObjectSpawned objectSpawned = new DecorativeObjectSpawned();
                    objectSpawned.setTile(tile);
                    objectSpawned.setDecorativeObject(object);
                    this.eventBus.post(objectSpawned);
                });
                Optional.ofNullable(tile.getGroundObject()).ifPresent(object -> {
                    GroundObjectSpawned objectSpawned = new GroundObjectSpawned();
                    objectSpawned.setTile(tile);
                    objectSpawned.setGroundObject(object);
                    this.eventBus.post(objectSpawned);
                });
                Arrays.stream(tile.getGameObjects()).filter(Objects::nonNull).filter(object -> object.getSceneMinLocation().equals(tile.getSceneLocation())).forEach(object -> {
                    GameObjectSpawned objectSpawned = new GameObjectSpawned();
                    objectSpawned.setTile(tile);
                    objectSpawned.setGameObject(object);
                    this.eventBus.post(objectSpawned);
                });
                Optional.ofNullable(tile.getItemLayer()).ifPresent(itemLayer -> {
                    Renderable current = itemLayer.getBottom();
                    while (current instanceof TileItem) {
                        TileItem item = (TileItem)current;
                        current = (Renderable) current.getNext();
                        ItemSpawned itemSpawned = new ItemSpawned(tile, item);
                        this.eventBus.post(itemSpawned);
                    }
                });
            });
            this.eventBus.unregister(subscriber);
        });
    }
}

