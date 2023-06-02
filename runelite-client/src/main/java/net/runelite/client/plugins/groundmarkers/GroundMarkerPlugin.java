/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Tile
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.MenuEntryAdded
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.groundmarkers;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.groundmarkers.ColorTileMarker;
import net.runelite.client.plugins.groundmarkers.GroundMarkerConfig;
import net.runelite.client.plugins.groundmarkers.GroundMarkerMinimapOverlay;
import net.runelite.client.plugins.groundmarkers.GroundMarkerOverlay;
import net.runelite.client.plugins.groundmarkers.GroundMarkerPoint;
import net.runelite.client.plugins.groundmarkers.GroundMarkerSharingManager;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Ground Markers", description="Enable marking of tiles using the Shift key", tags={"overlay", "tiles"})
public class GroundMarkerPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(GroundMarkerPlugin.class);
    private static final String CONFIG_GROUP = "groundMarker";
    private static final String MARK = "Mark tile";
    private static final String UNMARK = "Unmark tile";
    private static final String LABEL = "Label tile";
    private static final String WALK_HERE = "Walk here";
    private static final String REGION_PREFIX = "region_";
    private final List<ColorTileMarker> points = new ArrayList<ColorTileMarker>();
    @Inject
    private Client client;
    @Inject
    private GroundMarkerConfig config;
    @Inject
    private ConfigManager configManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private GroundMarkerOverlay overlay;
    @Inject
    private GroundMarkerMinimapOverlay minimapOverlay;
    @Inject
    private ChatboxPanelManager chatboxPanelManager;
    @Inject
    private EventBus eventBus;
    @Inject
    private GroundMarkerSharingManager sharingManager;
    @Inject
    private Gson gson;

    void savePoints(int regionId, Collection<GroundMarkerPoint> points) {
        if (points == null || points.isEmpty()) {
            this.configManager.unsetConfiguration(CONFIG_GROUP, REGION_PREFIX + regionId);
            return;
        }
        String json = this.gson.toJson(points);
        this.configManager.setConfiguration(CONFIG_GROUP, REGION_PREFIX + regionId, json);
    }

    Collection<GroundMarkerPoint> getPoints(int regionId) {
        String json = this.configManager.getConfiguration(CONFIG_GROUP, REGION_PREFIX + regionId);
        if (Strings.isNullOrEmpty((String)json)) {
            return Collections.emptyList();
        }
        return (Collection)this.gson.fromJson(json, new TypeToken<List<GroundMarkerPoint>>(){}.getType());
    }

    @Provides
    GroundMarkerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(GroundMarkerConfig.class);
    }

    void loadPoints() {
        this.points.clear();
        int[] regions = this.client.getMapRegions();
        if (regions == null) {
            return;
        }
        for (int regionId : regions) {
            log.debug("Loading points for region {}", (Object)regionId);
            Collection<GroundMarkerPoint> regionPoints = this.getPoints(regionId);
            Collection<ColorTileMarker> colorTileMarkers = this.translateToColorTileMarker(regionPoints);
            this.points.addAll(colorTileMarkers);
        }
    }

    private Collection<ColorTileMarker> translateToColorTileMarker(Collection<GroundMarkerPoint> points) {
        if (points.isEmpty()) {
            return Collections.emptyList();
        }
        return points.stream().map(point -> new ColorTileMarker(WorldPoint.fromRegion((int)point.getRegionId(), (int)point.getRegionX(), (int)point.getRegionY(), (int)point.getZ()), point.getColor(), point.getLabel())).flatMap(colorTile -> {
            Collection localWorldPoints = WorldPoint.toLocalInstance((Client)this.client, (WorldPoint)colorTile.getWorldPoint());
            return localWorldPoints.stream().map(wp -> new ColorTileMarker((WorldPoint)wp, colorTile.getColor(), colorTile.getLabel()));
        }).collect(Collectors.toList());
    }

    @Override
    public void startUp() {
        this.overlayManager.add(this.overlay);
        this.overlayManager.add(this.minimapOverlay);
        if (this.config.showImportExport()) {
            this.sharingManager.addImportExportMenuOptions();
        }
        if (this.config.showClear()) {
            this.sharingManager.addClearMenuOption();
        }
        this.loadPoints();
        this.eventBus.register(this.sharingManager);
    }

    @Override
    public void shutDown() {
        this.eventBus.unregister(this.sharingManager);
        this.overlayManager.remove(this.overlay);
        this.overlayManager.remove(this.minimapOverlay);
        this.sharingManager.removeMenuOptions();
        this.points.clear();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        this.loadPoints();
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        boolean hotKeyPressed = this.client.isKeyPressed(81);
        if (hotKeyPressed && event.getOption().equals(WALK_HERE)) {
            Tile selectedSceneTile = this.client.getSelectedSceneTile();
            if (selectedSceneTile == null) {
                return;
            }
            WorldPoint worldPoint = WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)selectedSceneTile.getLocalLocation());
            int regionId = worldPoint.getRegionID();
            GroundMarkerPoint point = new GroundMarkerPoint(regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), null, null);
            boolean exists = this.getPoints(regionId).contains(point);
            this.client.createMenuEntry(-1).setOption(exists ? UNMARK : MARK).setTarget(event.getTarget()).setType(MenuAction.RUNELITE).onClick(e -> {
                Tile target = this.client.getSelectedSceneTile();
                if (target != null) {
                    this.markTile(target.getLocalLocation());
                }
            });
            if (exists) {
                this.client.createMenuEntry(-2).setOption(LABEL).setTarget(event.getTarget()).setType(MenuAction.RUNELITE).onClick(e -> {
                    Tile target = this.client.getSelectedSceneTile();
                    if (target != null) {
                        this.labelTile(target);
                    }
                });
            }
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals(CONFIG_GROUP) && (event.getKey().equals("showImportExport") || event.getKey().equals("showClear"))) {
            this.sharingManager.removeMenuOptions();
            if (this.config.showImportExport()) {
                this.sharingManager.addImportExportMenuOptions();
            }
            if (this.config.showClear()) {
                this.sharingManager.addClearMenuOption();
            }
        }
    }

    private void markTile(LocalPoint localPoint) {
        if (localPoint == null) {
            return;
        }
        WorldPoint worldPoint = WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)localPoint);
        int regionId = worldPoint.getRegionID();
        GroundMarkerPoint point = new GroundMarkerPoint(regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), this.config.markerColor(), null);
        log.debug("Updating point: {} - {}", (Object)point, (Object)worldPoint);
        ArrayList<GroundMarkerPoint> groundMarkerPoints = new ArrayList<GroundMarkerPoint>(this.getPoints(regionId));
        if (groundMarkerPoints.contains(point)) {
            groundMarkerPoints.remove(point);
        } else {
            groundMarkerPoints.add(point);
        }
        this.savePoints(regionId, groundMarkerPoints);
        this.loadPoints();
    }

    private void labelTile(Tile tile) {
        LocalPoint localPoint = tile.getLocalLocation();
        WorldPoint worldPoint = WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)localPoint);
        int regionId = worldPoint.getRegionID();
        GroundMarkerPoint searchPoint = new GroundMarkerPoint(regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), null, null);
        Collection<GroundMarkerPoint> points = this.getPoints(regionId);
        GroundMarkerPoint existing = points.stream().filter(p -> p.equals(searchPoint)).findFirst().orElse(null);
        if (existing == null) {
            return;
        }
        this.chatboxPanelManager.openTextInput("Tile label").value(Optional.ofNullable(existing.getLabel()).orElse("")).onDone(input -> {
            input = Strings.emptyToNull((String)input);
            GroundMarkerPoint newPoint = new GroundMarkerPoint(regionId, worldPoint.getRegionX(), worldPoint.getRegionY(), worldPoint.getPlane(), existing.getColor(), (String)input);
            points.remove(searchPoint);
            points.add(newPoint);
            this.savePoints(regionId, points);
            this.loadPoints();
        }).build();
    }

    List<ColorTileMarker> getPoints() {
        return this.points;
    }
}

