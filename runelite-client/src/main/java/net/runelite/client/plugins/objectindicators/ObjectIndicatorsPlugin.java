/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  com.google.inject.Provides
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.DecorativeObject
 *  net.runelite.api.GameObject
 *  net.runelite.api.GameState
 *  net.runelite.api.GroundObject
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.ObjectComposition
 *  net.runelite.api.Scene
 *  net.runelite.api.Tile
 *  net.runelite.api.TileObject
 *  net.runelite.api.WallObject
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.DecorativeObjectDespawned
 *  net.runelite.api.events.DecorativeObjectSpawned
 *  net.runelite.api.events.GameObjectDespawned
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GroundObjectDespawned
 *  net.runelite.api.events.GroundObjectSpawned
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.WallObjectDespawned
 *  net.runelite.api.events.WallObjectSpawned
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.objectindicators;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.GroundObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.objectindicators.ColorTileObject;
import net.runelite.client.plugins.objectindicators.ObjectIndicatorsConfig;
import net.runelite.client.plugins.objectindicators.ObjectIndicatorsOverlay;
import net.runelite.client.plugins.objectindicators.ObjectPoint;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Object Markers", description="Enable marking of objects using the Shift key", tags={"overlay", "objects", "mark", "marker"}, enabledByDefault=false)
public class ObjectIndicatorsPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(ObjectIndicatorsPlugin.class);
    private static final String CONFIG_GROUP = "objectindicators";
    private static final String MARK = "Mark object";
    private static final String UNMARK = "Unmark object";
    private final List<ColorTileObject> objects = new ArrayList<ColorTileObject>();
    private final Map<Integer, Set<ObjectPoint>> points = new HashMap<Integer, Set<ObjectPoint>>();
    @Inject
    private Client client;
    @Inject
    private ConfigManager configManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ObjectIndicatorsOverlay overlay;
    @Inject
    private ObjectIndicatorsConfig config;
    @Inject
    private Gson gson;

    @Provides
    ObjectIndicatorsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ObjectIndicatorsConfig.class);
    }

    @Override
    protected void startUp() {
        this.overlayManager.add(this.overlay);
    }

    @Override
    protected void shutDown() {
        this.overlayManager.remove(this.overlay);
        this.points.clear();
        this.objects.clear();
    }

    @Subscribe
    public void onWallObjectSpawned(WallObjectSpawned event) {
        this.checkObjectPoints((TileObject)event.getWallObject());
    }

    @Subscribe
    public void onWallObjectDespawned(WallObjectDespawned event) {
        this.objects.removeIf(o -> o.getTileObject() == event.getWallObject());
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        this.checkObjectPoints((TileObject)event.getGameObject());
    }

    @Subscribe
    public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
        this.checkObjectPoints((TileObject)event.getDecorativeObject());
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        this.objects.removeIf(o -> o.getTileObject() == event.getGameObject());
    }

    @Subscribe
    public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
        this.objects.removeIf(o -> o.getTileObject() == event.getDecorativeObject());
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        this.checkObjectPoints((TileObject)event.getGroundObject());
    }

    @Subscribe
    public void onGroundObjectDespawned(GroundObjectDespawned event) {
        this.objects.removeIf(o -> o.getTileObject() == event.getGroundObject());
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        GameState gameState = gameStateChanged.getGameState();
        if (gameState == GameState.LOADING) {
            this.points.clear();
            for (int regionId : this.client.getMapRegions()) {
                Set<ObjectPoint> regionPoints = this.loadPoints(regionId);
                if (regionPoints == null) continue;
                this.points.put(regionId, regionPoints);
            }
        }
        if (gameStateChanged.getGameState() != GameState.LOGGED_IN && gameStateChanged.getGameState() != GameState.CONNECTION_LOST) {
            this.objects.clear();
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        if (event.getType() != MenuAction.EXAMINE_OBJECT.getId() || !this.client.isKeyPressed(81)) {
            return;
        }
        Tile tile = this.client.getScene().getTiles()[this.client.getPlane()][event.getActionParam0()][event.getActionParam1()];
        TileObject tileObject = this.findTileObject(tile, event.getIdentifier());
        if (tileObject == null) {
            return;
        }
        this.client.createMenuEntry(-1).setOption(this.objects.stream().anyMatch(o -> o.getTileObject() == tileObject) ? UNMARK : MARK).setTarget(event.getTarget()).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setIdentifier(event.getIdentifier()).setType(MenuAction.RUNELITE).onClick(this::markObject);
    }

    private void markObject(MenuEntry entry) {
        Scene scene = this.client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int x = entry.getParam0();
        int y = entry.getParam1();
        int z = this.client.getPlane();
        Tile tile = tiles[z][x][y];
        TileObject object = this.findTileObject(tile, entry.getIdentifier());
        if (object == null) {
            return;
        }
        ObjectComposition objectDefinition = this.getObjectComposition(object.getId());
        String name = objectDefinition.getName();
        if (Strings.isNullOrEmpty((String)name) || name.equals("null")) {
            return;
        }
        this.markObject(objectDefinition, name, object);
    }

    private void checkObjectPoints(TileObject object) {
        String name;
        if (object.getPlane() < 0) {
            return;
        }
        WorldPoint worldPoint = WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)object.getLocalLocation(), (int)object.getPlane());
        Set<ObjectPoint> objectPoints = this.points.get(worldPoint.getRegionID());
        if (objectPoints == null) {
            return;
        }
        ObjectComposition objectComposition = this.client.getObjectDefinition(object.getId());
        if (objectComposition.getImpostorIds() == null && (Strings.isNullOrEmpty((String)(name = objectComposition.getName())) || name.equals("null"))) {
            return;
        }
        for (ObjectPoint objectPoint : objectPoints) {
            if (worldPoint.getRegionX() != objectPoint.getRegionX() || worldPoint.getRegionY() != objectPoint.getRegionY() || worldPoint.getPlane() != objectPoint.getZ() || objectPoint.getId() != object.getId()) continue;
            log.debug("Marking object {} due to matching {}", (Object)object, (Object)objectPoint);
            this.objects.add(new ColorTileObject(object, objectComposition, objectPoint.getName(), objectPoint.getColor()));
            break;
        }
    }

    private TileObject findTileObject(Tile tile, int id) {
        if (tile == null) {
            return null;
        }
        GameObject[] tileGameObjects = tile.getGameObjects();
        DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
        WallObject tileWallObject = tile.getWallObject();
        GroundObject groundObject = tile.getGroundObject();
        if (this.objectIdEquals((TileObject)tileWallObject, id)) {
            return tileWallObject;
        }
        if (this.objectIdEquals((TileObject)tileDecorativeObject, id)) {
            return tileDecorativeObject;
        }
        if (this.objectIdEquals((TileObject)groundObject, id)) {
            return groundObject;
        }
        for (GameObject object : tileGameObjects) {
            if (!this.objectIdEquals((TileObject)object, id)) continue;
            return object;
        }
        return null;
    }

    private boolean objectIdEquals(TileObject tileObject, int id) {
        if (tileObject == null) {
            return false;
        }
        if (tileObject.getId() == id) {
            return true;
        }
        ObjectComposition comp = this.client.getObjectDefinition(tileObject.getId());
        if (comp.getImpostorIds() != null) {
            for (int impostorId : comp.getImpostorIds()) {
                if (impostorId != id) continue;
                return true;
            }
        }
        return false;
    }

    private void markObject(ObjectComposition objectComposition, String name, TileObject object) {
        WorldPoint worldPoint = WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)object.getLocalLocation());
        int regionId = worldPoint.getRegionID();
        Color color = this.config.markerColor();
        ObjectPoint point = new ObjectPoint(object.getId(), name, regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), color);
        Set objectPoints = this.points.computeIfAbsent(regionId, k -> new HashSet());
        if (this.objects.removeIf(o -> o.getTileObject() == object)) {
            if (!objectPoints.removeIf(op -> (op.getId() == -1 || op.getId() == object.getId() || op.getName().equals(objectComposition.getName())) && op.getRegionX() == worldPoint.getRegionX() && op.getRegionY() == worldPoint.getRegionY() && op.getZ() == worldPoint.getPlane())) {
                log.warn("unable to find object point for unmarked object {}", (Object)object.getId());
            }
            log.debug("Unmarking object: {}", (Object)point);
        } else {
            objectPoints.add(point);
            this.objects.add(new ColorTileObject(object, this.client.getObjectDefinition(object.getId()), name, color));
            log.debug("Marking object: {}", (Object)point);
        }
        this.savePoints(regionId, objectPoints);
    }

    private void savePoints(int id, Set<ObjectPoint> points) {
        if (points.isEmpty()) {
            this.configManager.unsetConfiguration(CONFIG_GROUP, "region_" + id);
        } else {
            String json = this.gson.toJson(points);
            this.configManager.setConfiguration(CONFIG_GROUP, "region_" + id, json);
        }
    }

    private Set<ObjectPoint> loadPoints(int id) {
        String json = this.configManager.getConfiguration(CONFIG_GROUP, "region_" + id);
        if (Strings.isNullOrEmpty((String)json)) {
            return null;
        }
        Set points = (Set)this.gson.fromJson(json, new TypeToken<Set<ObjectPoint>>(){}.getType());
        return points.stream().filter(point -> !point.getName().equals("null")).collect(Collectors.toSet());
    }

    @Nullable
    private ObjectComposition getObjectComposition(int id) {
        ObjectComposition objectComposition = this.client.getObjectDefinition(id);
        return objectComposition.getImpostorIds() == null ? objectComposition : objectComposition.getImpostor();
    }

    List<ColorTileObject> getObjects() {
        return this.objects;
    }
}

