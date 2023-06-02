/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Inject
 *  com.google.inject.Provides
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.Quest
 *  net.runelite.api.Skill
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.StatChanged
 *  net.runelite.api.events.WidgetLoaded
 */
package net.runelite.client.plugins.worldmap;

import com.google.inject.Inject;
import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Predicate;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.AgilityShortcut;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.worldmap.AgilityCourseLocation;
import net.runelite.client.plugins.worldmap.DungeonLocation;
import net.runelite.client.plugins.worldmap.FairyRingLocation;
import net.runelite.client.plugins.worldmap.FarmingPatchLocation;
import net.runelite.client.plugins.worldmap.FishingSpotLocation;
import net.runelite.client.plugins.worldmap.HunterAreaLocation;
import net.runelite.client.plugins.worldmap.KourendTaskLocation;
import net.runelite.client.plugins.worldmap.MapPoint;
import net.runelite.client.plugins.worldmap.MinigameLocation;
import net.runelite.client.plugins.worldmap.MiningSiteLocation;
import net.runelite.client.plugins.worldmap.QuestStartLocation;
import net.runelite.client.plugins.worldmap.RareTreeLocation;
import net.runelite.client.plugins.worldmap.RunecraftingAltarLocation;
import net.runelite.client.plugins.worldmap.TeleportLocationData;
import net.runelite.client.plugins.worldmap.TransportationPointLocation;
import net.runelite.client.plugins.worldmap.WorldMapConfig;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

@PluginDescriptor(name="World Map", description="Enhance the world map to display additional information", tags={"agility", "dungeon", "fairy", "farming", "rings", "teleports"})
public class WorldMapPlugin
extends Plugin {
    static final BufferedImage BLANK_ICON;
    private static final BufferedImage FAIRY_TRAVEL_ICON;
    private static final BufferedImage NOPE_ICON;
    private static final BufferedImage NOT_STARTED_ICON;
    private static final BufferedImage STARTED_ICON;
    private static final BufferedImage FINISHED_ICON;
    private static final BufferedImage MINING_SITE_ICON;
    private static final BufferedImage ROOFTOP_COURSE_ICON;
    static final String CONFIG_KEY = "worldmap";
    static final String CONFIG_KEY_FAIRY_RING_TOOLTIPS = "fairyRingTooltips";
    static final String CONFIG_KEY_FAIRY_RING_ICON = "fairyRingIcon";
    static final String CONFIG_KEY_AGILITY_SHORTCUT_TOOLTIPS = "agilityShortcutTooltips";
    static final String CONFIG_KEY_AGILITY_SHORTCUT_LEVEL_ICON = "agilityShortcutIcon";
    static final String CONFIG_KEY_AGILITY_COURSE_TOOLTIPS = "agilityCourseTooltips";
    static final String CONFIG_KEY_AGILITY_COURSE_ROOFTOP_ICON = "agilityCourseRooftopIcon";
    static final String CONFIG_KEY_NORMAL_TELEPORT_ICON = "standardSpellbookIcon";
    static final String CONFIG_KEY_ANCIENT_TELEPORT_ICON = "ancientSpellbookIcon";
    static final String CONFIG_KEY_LUNAR_TELEPORT_ICON = "lunarSpellbookIcon";
    static final String CONFIG_KEY_ARCEUUS_TELEPORT_ICON = "arceuusSpellbookIcon";
    static final String CONFIG_KEY_JEWELLERY_TELEPORT_ICON = "jewelleryIcon";
    static final String CONFIG_KEY_SCROLL_TELEPORT_ICON = "scrollIcon";
    static final String CONFIG_KEY_MISC_TELEPORT_ICON = "miscellaneousTeleportIcon";
    static final String CONFIG_KEY_QUEST_START_TOOLTIPS = "questStartTooltips";
    static final String CONFIG_KEY_MINIGAME_TOOLTIP = "minigameTooltip";
    static final String CONFIG_KEY_FARMING_PATCH_TOOLTIPS = "farmingpatchTooltips";
    static final String CONFIG_KEY_RARE_TREE_TOOLTIPS = "rareTreeTooltips";
    static final String CONFIG_KEY_RARE_TREE_LEVEL_ICON = "rareTreeIcon";
    static final String CONFIG_KEY_TRANSPORTATION_TELEPORT_TOOLTIPS = "transportationTooltips";
    static final String CONFIG_KEY_RUNECRAFTING_ALTAR_ICON = "runecraftingAltarIcon";
    static final String CONFIG_KEY_MINING_SITE_TOOLTIPS = "miningSiteTooltips";
    static final String CONFIG_KEY_DUNGEON_TOOLTIPS = "dungeonTooltips";
    static final String CONFIG_KEY_HUNTER_AREA_TOOLTIPS = "hunterAreaTooltips";
    static final String CONFIG_KEY_FISHING_SPOT_TOOLTIPS = "fishingSpotTooltips";
    static final String CONFIG_KEY_KOUREND_TASK_TOOLTIPS = "kourendTaskTooltips";
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private WorldMapConfig config;
    @Inject
    private WorldMapPointManager worldMapPointManager;
    private int agilityLevel = 0;
    private int woodcuttingLevel = 0;

    @Provides
    WorldMapConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(WorldMapConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.agilityLevel = this.client.getRealSkillLevel(Skill.AGILITY);
        this.woodcuttingLevel = this.client.getRealSkillLevel(Skill.WOODCUTTING);
        this.updateShownIcons();
    }

    @Override
    protected void shutDown() throws Exception {
        this.worldMapPointManager.removeIf(MapPoint.class::isInstance);
        this.agilityLevel = 0;
        this.woodcuttingLevel = 0;
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals(CONFIG_KEY)) {
            return;
        }
        this.updateShownIcons();
    }

    @Subscribe
    public void onStatChanged(StatChanged statChanged) {
        switch (statChanged.getSkill()) {
            case AGILITY: {
                int newAgilityLevel = statChanged.getBoostedLevel();
                if (newAgilityLevel == this.agilityLevel) break;
                this.agilityLevel = newAgilityLevel;
                this.updateAgilityIcons();
                break;
            }
            case WOODCUTTING: {
                int newWoodcutLevel = statChanged.getBoostedLevel();
                if (newWoodcutLevel == this.woodcuttingLevel) break;
                this.woodcuttingLevel = newWoodcutLevel;
                this.updateRareTreeIcons();
                break;
            }
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
        if (widgetLoaded.getGroupId() == 595) {
            this.updateQuestStartPointIcons();
        }
    }

    private void updateAgilityIcons() {
        block3: {
            block2: {
                this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.AGILITY_SHORTCUT));
                if (this.config.agilityShortcutLevelIcon()) break block2;
                if (!this.config.agilityShortcutTooltips()) break block3;
            }
            Arrays.stream(AgilityShortcut.values()).filter(value -> value.getWorldMapLocation() != null).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.AGILITY_SHORTCUT)).worldPoint(l.getWorldMapLocation())).image(this.agilityLevel > 0 && this.config.agilityShortcutLevelIcon() && l.getLevel() > this.agilityLevel ? NOPE_ICON : BLANK_ICON)).tooltip(this.config.agilityShortcutTooltips() ? l.getTooltip() : null)).build()).forEach(this.worldMapPointManager::add);
        }
    }

    private void updateAgilityCourseIcons() {
        block3: {
            block2: {
                this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.AGILITY_COURSE));
                if (this.config.agilityCourseTooltip()) break block2;
                if (!this.config.agilityCourseRooftop()) break block3;
            }
            Arrays.stream(AgilityCourseLocation.values()).filter(value -> value.getLocation() != null).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.AGILITY_COURSE)).worldPoint(l.getLocation())).image(this.config.agilityCourseRooftop() && l.isRooftopCourse() ? ROOFTOP_COURSE_ICON : BLANK_ICON)).tooltip(this.config.agilityCourseTooltip() ? l.getTooltip() : null)).build()).forEach(this.worldMapPointManager::add);
        }
    }

    private void updateRareTreeIcons() {
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.RARE_TREE));
        if (this.config.rareTreeLevelIcon() || this.config.rareTreeTooltips()) {
            Arrays.stream(RareTreeLocation.values()).forEach(rareTree -> Arrays.stream(rareTree.getLocations()).map(point -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.RARE_TREE)).worldPoint((WorldPoint)point)).image(this.woodcuttingLevel > 0 && this.config.rareTreeLevelIcon() && rareTree.getLevelReq() > this.woodcuttingLevel ? NOPE_ICON : BLANK_ICON)).tooltip(this.config.rareTreeTooltips() ? rareTree.getTooltip() : null)).build()).forEach(this.worldMapPointManager::add));
        }
    }

    private void updateShownIcons() {
        block13: {
            block12: {
                this.updateAgilityIcons();
                this.updateAgilityCourseIcons();
                this.updateRareTreeIcons();
                this.updateQuestStartPointIcons();
                this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.FAIRY_RING));
                if (this.config.fairyRingIcon()) break block12;
                if (!this.config.fairyRingTooltips()) break block13;
            }
            Arrays.stream(FairyRingLocation.values()).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.FAIRY_RING)).worldPoint(l.getLocation())).image(this.config.fairyRingIcon() ? FAIRY_TRAVEL_ICON : BLANK_ICON)).tooltip(this.config.fairyRingTooltips() ? "Fairy Ring - " + l.getCode() : null)).build()).forEach(this.worldMapPointManager::add);
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.MINIGAME));
        if (this.config.minigameTooltip()) {
            Arrays.stream(MinigameLocation.values()).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.MINIGAME)).worldPoint(l.getLocation())).image(BLANK_ICON)).tooltip(l.getTooltip())).build()).forEach(this.worldMapPointManager::add);
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.TRANSPORTATION));
        if (this.config.transportationTeleportTooltips()) {
            Arrays.stream(TransportationPointLocation.values()).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.TRANSPORTATION)).worldPoint(l.getLocation())).image(BLANK_ICON)).target(l.getTarget())).jumpOnClick(l.getTarget() != null)).name(Text.titleCase(l))).tooltip(l.getTooltip())).build()).forEach(this.worldMapPointManager::add);
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.FARMING_PATCH));
        if (this.config.farmingPatchTooltips()) {
            Arrays.stream(FarmingPatchLocation.values()).forEach(location -> Arrays.stream(location.getLocations()).map(point -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.FARMING_PATCH)).worldPoint((WorldPoint)point)).image(BLANK_ICON)).tooltip(location.getTooltip())).build()).forEach(this.worldMapPointManager::add));
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.TELEPORT));
        Arrays.stream(TeleportLocationData.values()).filter(data -> {
            switch (data.getType()) {
                case NORMAL_MAGIC: {
                    return this.config.normalTeleportIcon();
                }
                case ANCIENT_MAGICKS: {
                    return this.config.ancientTeleportIcon();
                }
                case LUNAR_MAGIC: {
                    return this.config.lunarTeleportIcon();
                }
                case ARCEUUS_MAGIC: {
                    return this.config.arceuusTeleportIcon();
                }
                case JEWELLERY: {
                    return this.config.jewelleryTeleportIcon();
                }
                case SCROLL: {
                    return this.config.scrollTeleportIcon();
                }
                case OTHER: {
                    return this.config.miscellaneousTeleportIcon();
                }
            }
            return false;
        }).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.TELEPORT)).worldPoint(l.getLocation())).tooltip(l.getTooltip())).image(ImageUtil.loadImageResource(WorldMapPlugin.class, l.getIconPath()))).build()).forEach(this.worldMapPointManager::add);
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.RUNECRAFT_ALTAR));
        if (this.config.runecraftingAltarIcon()) {
            Arrays.stream(RunecraftingAltarLocation.values()).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.RUNECRAFT_ALTAR)).worldPoint(l.getLocation())).image(ImageUtil.loadImageResource(WorldMapPlugin.class, l.getIconPath()))).tooltip(l.getTooltip())).build()).forEach(this.worldMapPointManager::add);
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.MINING_SITE));
        if (this.config.miningSiteTooltips()) {
            Arrays.stream(MiningSiteLocation.values()).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.MINING_SITE)).worldPoint(l.getLocation())).image(l.isIconRequired() ? MINING_SITE_ICON : BLANK_ICON)).tooltip(l.getTooltip())).build()).forEach(this.worldMapPointManager::add);
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.DUNGEON));
        if (this.config.dungeonTooltips()) {
            Arrays.stream(DungeonLocation.values()).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.DUNGEON)).worldPoint(l.getLocation())).image(BLANK_ICON)).tooltip(l.getTooltip())).build()).forEach(this.worldMapPointManager::add);
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.HUNTER));
        if (this.config.hunterAreaTooltips()) {
            Arrays.stream(HunterAreaLocation.values()).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.HUNTER)).worldPoint(l.getLocation())).image(BLANK_ICON)).tooltip(l.getTooltip())).build()).forEach(this.worldMapPointManager::add);
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.FISHING));
        if (this.config.fishingSpotTooltips()) {
            Arrays.stream(FishingSpotLocation.values()).forEach(location -> Arrays.stream(location.getLocations()).map(point -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.FISHING)).worldPoint((WorldPoint)point)).image(BLANK_ICON)).tooltip(location.getTooltip())).build()).forEach(this.worldMapPointManager::add));
        }
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.KOUREND_TASK));
        if (this.config.kourendTaskTooltips()) {
            Arrays.stream(KourendTaskLocation.values()).map(l -> ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.KOUREND_TASK)).worldPoint(l.getLocation())).image(BLANK_ICON)).tooltip(l.getTooltip())).build()).forEach(this.worldMapPointManager::add);
        }
    }

    private void updateQuestStartPointIcons() {
        this.worldMapPointManager.removeIf(WorldMapPlugin.isType(MapPoint.Type.QUEST));
        if (!this.config.questStartTooltips()) {
            return;
        }
        this.clientThread.invoke(() -> {
            if (this.client.getGameState() == GameState.LOGGED_IN) {
                Arrays.stream(QuestStartLocation.values()).map(this::createQuestStartPoint).forEach(this.worldMapPointManager::add);
            }
        });
    }

    private MapPoint createQuestStartPoint(QuestStartLocation data) {
        Quest quest = data.getQuest();
        BufferedImage icon = BLANK_ICON;
        if (quest != null) {
            switch (quest.getState(this.client)) {
                case FINISHED: {
                    icon = FINISHED_ICON;
                    break;
                }
                case IN_PROGRESS: {
                    icon = STARTED_ICON;
                    break;
                }
                case NOT_STARTED: {
                    icon = NOT_STARTED_ICON;
                }
            }
        }
        return ((MapPoint.MapPointBuilder)((MapPoint.MapPointBuilder)((WorldMapPoint.WorldMapPointBuilder)MapPoint.builder().type(MapPoint.Type.QUEST)).worldPoint(data.getLocation())).image(icon)).build();
    }

    private static Predicate<WorldMapPoint> isType(MapPoint.Type type) {
        return w -> w instanceof MapPoint && ((MapPoint)w).getType() == type;
    }

    static {
        int iconBufferSize = 17;
        int questIconBufferSize = 22;
        BLANK_ICON = new BufferedImage(17, 17, 2);
        FAIRY_TRAVEL_ICON = new BufferedImage(17, 17, 2);
        BufferedImage fairyTravelIcon = ImageUtil.loadImageResource(WorldMapPlugin.class, "fairy_ring_travel.png");
        FAIRY_TRAVEL_ICON.getGraphics().drawImage(fairyTravelIcon, 1, 1, null);
        NOPE_ICON = new BufferedImage(17, 17, 2);
        BufferedImage nopeImage = ImageUtil.loadImageResource(WorldMapPlugin.class, "nope_icon.png");
        NOPE_ICON.getGraphics().drawImage(nopeImage, 1, 1, null);
        NOT_STARTED_ICON = new BufferedImage(22, 22, 2);
        BufferedImage notStartedIcon = ImageUtil.loadImageResource(WorldMapPlugin.class, "quest_not_started_icon.png");
        NOT_STARTED_ICON.getGraphics().drawImage(notStartedIcon, 4, 4, null);
        STARTED_ICON = new BufferedImage(22, 22, 2);
        BufferedImage startedIcon = ImageUtil.loadImageResource(WorldMapPlugin.class, "quest_started_icon.png");
        STARTED_ICON.getGraphics().drawImage(startedIcon, 4, 4, null);
        FINISHED_ICON = new BufferedImage(22, 22, 2);
        BufferedImage finishedIcon = ImageUtil.loadImageResource(WorldMapPlugin.class, "quest_completed_icon.png");
        FINISHED_ICON.getGraphics().drawImage(finishedIcon, 4, 4, null);
        MINING_SITE_ICON = new BufferedImage(17, 17, 2);
        BufferedImage miningSiteIcon = ImageUtil.loadImageResource(WorldMapPlugin.class, "mining_site_icon.png");
        MINING_SITE_ICON.getGraphics().drawImage(miningSiteIcon, 1, 1, null);
        ROOFTOP_COURSE_ICON = new BufferedImage(17, 17, 2);
        BufferedImage rooftopCourseIcon = ImageUtil.loadImageResource(WorldMapPlugin.class, "rooftop_course_icon.png");
        ROOFTOP_COURSE_ICON.getGraphics().drawImage(rooftopCourseIcon, 1, 1, null);
    }
}

