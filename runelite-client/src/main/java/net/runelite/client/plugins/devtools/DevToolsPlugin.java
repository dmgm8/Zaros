/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.classic.Level
 *  ch.qos.logback.classic.Logger
 *  com.google.common.collect.ImmutableList
 *  com.google.common.primitives.Ints
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.Experience
 *  net.runelite.api.GameState
 *  net.runelite.api.IndexedSprite
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Skill
 *  net.runelite.api.SpritePixels
 *  net.runelite.api.VarbitComposition
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.CommandExecuted
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.ScriptCallbackEvent
 *  net.runelite.api.events.StatChanged
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.kit.KitType
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.devtools;

import ch.qos.logback.classic.Level;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.google.inject.Provides;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.GameState;
import net.runelite.api.IndexedSprite;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.SpritePixels;
import net.runelite.api.VarbitComposition;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.devtools.CameraOverlay;
import net.runelite.client.plugins.devtools.DevToolsButton;
import net.runelite.client.plugins.devtools.DevToolsConfig;
import net.runelite.client.plugins.devtools.DevToolsOverlay;
import net.runelite.client.plugins.devtools.DevToolsPanel;
import net.runelite.client.plugins.devtools.LocationOverlay;
import net.runelite.client.plugins.devtools.NetworkOverlay;
import net.runelite.client.plugins.devtools.SceneOverlay;
import net.runelite.client.plugins.devtools.SoundEffectOverlay;
import net.runelite.client.plugins.devtools.WorldMapLocationOverlay;
import net.runelite.client.plugins.devtools.WorldMapRegionOverlay;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.OSType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Developer Tools", tags={"panel"}, developerPlugin=true)
public class DevToolsPlugin
extends Plugin
implements KeyListener {
    private static final Logger log = LoggerFactory.getLogger(DevToolsPlugin.class);
    private static final List<MenuAction> EXAMINE_MENU_ACTIONS = ImmutableList.of((Object)MenuAction.EXAMINE_ITEM, (Object)MenuAction.EXAMINE_ITEM_GROUND, (Object)MenuAction.EXAMINE_NPC, (Object)MenuAction.EXAMINE_OBJECT);
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private DevToolsOverlay overlay;
    @Inject
    private LocationOverlay locationOverlay;
    @Inject
    private SceneOverlay sceneOverlay;
    @Inject
    private CameraOverlay cameraOverlay;
    @Inject
    private WorldMapLocationOverlay worldMapLocationOverlay;
    @Inject
    private WorldMapRegionOverlay mapRegionOverlay;
    @Inject
    private SoundEffectOverlay soundEffectOverlay;
    @Inject
    private NetworkOverlay networkOverlay;
    @Inject
    private EventBus eventBus;
    @Inject
    private ConfigManager configManager;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private KeyManager keyManager;
    private DevToolsButton players;
    private DevToolsButton npcs;
    private DevToolsButton groundItems;
    private DevToolsButton groundObjects;
    private DevToolsButton gameObjects;
    private DevToolsButton graphicsObjects;
    private DevToolsButton walls;
    private DevToolsButton decorations;
    private DevToolsButton projectiles;
    private DevToolsButton location;
    private DevToolsButton chunkBorders;
    private DevToolsButton mapSquares;
    private DevToolsButton validMovement;
    private DevToolsButton movementFlags;
    private DevToolsButton lineOfSight;
    private DevToolsButton cameraPosition;
    private DevToolsButton worldMapLocation;
    private DevToolsButton tileLocation;
    private DevToolsButton interacting;
    private DevToolsButton examine;
    private DevToolsButton detachedCamera;
    private DevToolsButton widgetInspector;
    private DevToolsButton varInspector;
    private DevToolsButton soundEffects;
    private DevToolsButton scriptInspector;
    private DevToolsButton inventoryInspector;
    private DevToolsButton roofs;
    private DevToolsButton shell;
    private DevToolsButton interfacePath;
    private DevToolsButton scriptPath;
    private DevToolsButton network;
    private NavigationButton navButton;

    @Provides
    DevToolsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(DevToolsConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.players = new DevToolsButton("Players");
        this.npcs = new DevToolsButton("NPCs");
        this.groundItems = new DevToolsButton("Ground Items");
        this.groundObjects = new DevToolsButton("Ground Objects");
        this.gameObjects = new DevToolsButton("Game Objects");
        this.graphicsObjects = new DevToolsButton("Graphics Objects");
        this.walls = new DevToolsButton("Walls");
        this.decorations = new DevToolsButton("Decorations");
        this.projectiles = new DevToolsButton("Projectiles");
        this.location = new DevToolsButton("Location");
        this.worldMapLocation = new DevToolsButton("World Map Location");
        this.tileLocation = new DevToolsButton("Tile Location");
        this.cameraPosition = new DevToolsButton("Camera Position");
        this.chunkBorders = new DevToolsButton("Chunk Borders");
        this.mapSquares = new DevToolsButton("Map Squares");
        this.lineOfSight = new DevToolsButton("Line Of Sight");
        this.validMovement = new DevToolsButton("Valid Movement");
        this.movementFlags = new DevToolsButton("Movement Flags");
        this.interacting = new DevToolsButton("Interacting");
        this.examine = new DevToolsButton("Examine");
        this.detachedCamera = new DevToolsButton("Detached Camera");
        this.widgetInspector = new DevToolsButton("Widget Inspector");
        this.varInspector = new DevToolsButton("Var Inspector");
        this.soundEffects = new DevToolsButton("Sound Effects");
        this.scriptInspector = new DevToolsButton("Script Inspector");
        this.inventoryInspector = new DevToolsButton("Inventory Inspector");
        this.roofs = new DevToolsButton("Roofs");
        this.shell = new DevToolsButton("Shell");
        this.interfacePath = new DevToolsButton("Interface Path");
        this.scriptPath = new DevToolsButton("Script Path");
        this.network = new DevToolsButton("Network Usage");
        this.overlayManager.add(this.overlay);
        this.overlayManager.add(this.locationOverlay);
        this.overlayManager.add(this.sceneOverlay);
        this.overlayManager.add(this.cameraOverlay);
        this.overlayManager.add(this.worldMapLocationOverlay);
        this.overlayManager.add(this.mapRegionOverlay);
        this.overlayManager.add(this.soundEffectOverlay);
        this.overlayManager.add(this.networkOverlay);
        DevToolsPanel panel = (DevToolsPanel)this.injector.getInstance(DevToolsPanel.class);
        BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "devtools_icon.png");
        this.navButton = NavigationButton.builder().tooltip("Developer Tools").icon(icon).priority(1).panel(panel).build();
        this.clientToolbar.addNavigation(this.navButton);
        this.eventBus.register(this.soundEffectOverlay);
        this.keyManager.registerKeyListener(this);
    }

    @Override
    protected void shutDown() throws Exception {
        this.eventBus.unregister(this.soundEffectOverlay);
        this.keyManager.unregisterKeyListener(this);
        this.overlayManager.remove(this.overlay);
        this.overlayManager.remove(this.locationOverlay);
        this.overlayManager.remove(this.sceneOverlay);
        this.overlayManager.remove(this.cameraOverlay);
        this.overlayManager.remove(this.worldMapLocationOverlay);
        this.overlayManager.remove(this.mapRegionOverlay);
        this.overlayManager.remove(this.soundEffectOverlay);
        this.overlayManager.remove(this.networkOverlay);
        this.clientToolbar.removeNavigation(this.navButton);
    }

    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked overlayMenuClicked) {
        if (overlayMenuClicked.getOverlay() == this.networkOverlay) {
            String message = "In: " + this.client.getNetworkBytesReadTotal() + "B Out: " + this.client.getNetworkBytesWrittenTotal() + "B";
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, "");
        }
    }

    @Subscribe
    public void onCommandExecuted(CommandExecuted commandExecuted) {
        CharSequence[] args = commandExecuted.getArguments();
        switch (commandExecuted.getCommand()) {
            case "logger": {
                String message;
                ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger((String)"ROOT");
                Level currentLoggerLevel = logger.getLevel();
                if (args.length < 1) {
                    message = "Logger level is currently set to " + (Object)currentLoggerLevel;
                } else {
                    Level newLoggerLevel = Level.toLevel((String)args[0], (Level)currentLoggerLevel);
                    logger.setLevel(newLoggerLevel);
                    message = "Logger level has been set to " + (Object)newLoggerLevel;
                }
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
                break;
            }
            case "getvarp": {
                int varp = Integer.parseInt(args[0]);
                int[] varps = this.client.getVarps();
                int value = varps[varp];
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "VarPlayer " + varp + ": " + value, null);
                break;
            }
            case "setvarp": {
                int varp = Integer.parseInt(args[0]);
                int value = Integer.parseInt((String)args[1]);
                int[] varps = this.client.getVarps();
                varps[varp] = value;
                this.client.queueChangedVarp(varp);
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Set VarPlayer " + varp + " to " + value, null);
                VarbitChanged varbitChanged = new VarbitChanged();
                varbitChanged.setVarpId(varp);
                this.eventBus.post((Object)varbitChanged);
                break;
            }
            case "getvarb": {
                int varbit = Integer.parseInt(args[0]);
                int value = this.client.getVarbitValue(this.client.getVarps(), varbit);
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Varbit " + varbit + ": " + value, null);
                break;
            }
            case "setvarb": {
                int varbit = Integer.parseInt(args[0]);
                int value = Integer.parseInt((String)args[1]);
                this.client.setVarbitValue(this.client.getVarps(), varbit, value);
                VarbitComposition varbitComposition = this.client.getVarbit(varbit);
                this.client.queueChangedVarp(varbitComposition.getIndex());
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Set varbit " + varbit + " to " + value, null);
                this.eventBus.post((Object)new VarbitChanged());
                break;
            }
            case "addxp": {
                int level;
                Skill skill = Skill.valueOf((String)args[0].toUpperCase());
                int xp = Integer.parseInt((String)args[1]);
                int totalXp = this.client.getSkillExperience(skill) + xp;
                this.client.getBoostedSkillLevels()[skill.ordinal()] = level = Math.min(Experience.getLevelForXp((int)totalXp), 99);
                this.client.getRealSkillLevels()[skill.ordinal()] = level;
                this.client.getSkillExperiences()[skill.ordinal()] = totalXp;
                this.client.queueChangedSkill(skill);
                StatChanged statChanged = new StatChanged(skill, totalXp, level, level);
                this.eventBus.post((Object)statChanged);
                break;
            }
            case "setstat": {
                Skill skill = Skill.valueOf((String)args[0].toUpperCase());
                int level = Integer.parseInt((String)args[1]);
                level = Ints.constrainToRange((int)level, (int)1, (int)99);
                int xp = Experience.getXpForLevel((int)level);
                this.client.getBoostedSkillLevels()[skill.ordinal()] = level;
                this.client.getRealSkillLevels()[skill.ordinal()] = level;
                this.client.getSkillExperiences()[skill.ordinal()] = xp;
                this.client.queueChangedSkill(skill);
                StatChanged statChanged = new StatChanged(skill, xp, level, level);
                this.eventBus.post((Object)statChanged);
                break;
            }
            case "anim": {
                int id = Integer.parseInt(args[0]);
                Player localPlayer = this.client.getLocalPlayer();
                localPlayer.setAnimation(id);
                localPlayer.setAnimationFrame(0);
                break;
            }
            case "gfx": {
                int id = Integer.parseInt(args[0]);
                Player localPlayer = this.client.getLocalPlayer();
                localPlayer.setGraphic(id);
                localPlayer.setSpotAnimFrame(0);
                break;
            }
            case "transform": {
                int id = Integer.parseInt(args[0]);
                Player player = this.client.getLocalPlayer();
                player.getPlayerComposition().setTransformedNpcId(id);
                player.setIdlePoseAnimation(-1);
                player.setPoseAnimation(-1);
                break;
            }
            case "cape": {
                int id = Integer.parseInt(args[0]);
                Player player = this.client.getLocalPlayer();
                player.getPlayerComposition().getEquipmentIds()[KitType.CAPE.getIndex()] = id + 512;
                player.getPlayerComposition().setHash();
                break;
            }
            case "sound": {
                int id = Integer.parseInt(args[0]);
                this.client.playSoundEffect(id);
                break;
            }
            case "msg": {
                this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", String.join((CharSequence)" ", args), "");
                break;
            }
            case "setconf": {
                String message;
                String group = args[0];
                CharSequence key = args[1];
                String value = "";
                for (int i = 2; i < args.length; ++i) {
                    if (((String)args[i]).equals("=")) {
                        value = String.join((CharSequence)" ", Arrays.copyOfRange(args, i + 1, args.length));
                        break;
                    }
                    key = (String)key + " " + (String)args[i];
                }
                String current = this.configManager.getConfiguration(group, (String)key);
                if (value.isEmpty()) {
                    this.configManager.unsetConfiguration(group, (String)key);
                    message = String.format("Unset configuration %s.%s (was: %s)", group, key, current);
                } else {
                    this.configManager.setConfiguration(group, (String)key, value);
                    message = String.format("Set configuration %s.%s to %s (was: %s)", group, key, value, current);
                }
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.GAMEMESSAGE).runeLiteFormattedMessage(new ChatMessageBuilder().append(message).build()).build());
                break;
            }
            case "getconf": {
                String group = args[0];
                String key = String.join((CharSequence)" ", Arrays.copyOfRange(args, 1, args.length));
                String value = this.configManager.getConfiguration(group, key);
                String message = String.format("%s.%s = %s", group, key, value);
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.GAMEMESSAGE).runeLiteFormattedMessage(new ChatMessageBuilder().append(message).build()).build());
                break;
            }
            case "modicons": {
                ChatMessageBuilder builder = new ChatMessageBuilder();
                IndexedSprite[] modIcons = this.client.getModIcons();
                for (int i = 0; i < modIcons.length; ++i) {
                    builder.append(i + "=").img(i);
                    if (i == modIcons.length - 1) continue;
                    builder.append(", ");
                }
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.GAMEMESSAGE).runeLiteFormattedMessage(builder.build()).build());
                break;
            }
            case "item-images": {
                File itemImagesDir = new File("./item-images");
                if (!itemImagesDir.exists()) {
                    itemImagesDir.mkdirs();
                }
                try {
                    int[] ids;
                    for (int id : ids = this.client.getIndexConfig().getFileIds(10)) {
                        SpritePixels sprite = this.client.createItemSprite(id, 0, 1, 0x302020, 0, false, 512);
                        ImageIO.write((RenderedImage)sprite.toBufferedImage(), "png", new File(itemImagesDir, id + ".png"));
                    }
                    break;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        if (!this.examine.isActive()) {
            return;
        }
        MenuAction action = MenuAction.of((int)event.getType());
        if (EXAMINE_MENU_ACTIONS.contains((Object)action)) {
            MenuEntry entry = event.getMenuEntry();
            int identifier = event.getIdentifier();
            String info = "ID: ";
            if (action == MenuAction.EXAMINE_NPC) {
                NPC npc = entry.getNpc();
                assert (npc != null);
                info = info + npc.getId();
            } else {
                info = info + identifier;
                if (action == MenuAction.EXAMINE_OBJECT) {
                    WorldPoint point = WorldPoint.fromScene((Client)this.client, (int)entry.getParam0(), (int)entry.getParam1(), (int)this.client.getPlane());
                    info = info + " X: " + point.getX() + " Y: " + point.getY();
                }
            }
            entry.setTarget(entry.getTarget() + " " + ColorUtil.prependColorTag("(" + info + ")", JagexColors.MENU_TARGET));
        }
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent ev) {
        if ("devtoolsEnabled".equals(ev.getEventName())) {
            this.client.getIntStack()[this.client.getIntStackSize() - 1] = 1;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        boolean isModifierDown;
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        boolean bl = isModifierDown = OSType.getOSType() == OSType.MacOS ? e.isMetaDown() : e.isControlDown();
        if (e.getKeyCode() == 86 && isModifierDown) {
            try {
                String data = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString().trim();
                String appended = this.client.getVarcStrValue(335) + data;
                if (appended.length() > 127) {
                    appended = appended.substring(0, 127);
                }
                this.client.setVarcStrValue(335, appended);
                this.clientThread.invokeLater(() -> this.client.runScript(new Object[]{223}));
            }
            catch (UnsupportedFlavorException | IOException ex) {
                log.warn("failed to fetch clipboard data", (Throwable)ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public Client getClient() {
        return this.client;
    }

    public ClientThread getClientThread() {
        return this.clientThread;
    }

    public ClientToolbar getClientToolbar() {
        return this.clientToolbar;
    }

    public OverlayManager getOverlayManager() {
        return this.overlayManager;
    }

    public DevToolsOverlay getOverlay() {
        return this.overlay;
    }

    public LocationOverlay getLocationOverlay() {
        return this.locationOverlay;
    }

    public SceneOverlay getSceneOverlay() {
        return this.sceneOverlay;
    }

    public CameraOverlay getCameraOverlay() {
        return this.cameraOverlay;
    }

    public WorldMapLocationOverlay getWorldMapLocationOverlay() {
        return this.worldMapLocationOverlay;
    }

    public WorldMapRegionOverlay getMapRegionOverlay() {
        return this.mapRegionOverlay;
    }

    public SoundEffectOverlay getSoundEffectOverlay() {
        return this.soundEffectOverlay;
    }

    public NetworkOverlay getNetworkOverlay() {
        return this.networkOverlay;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public ChatMessageManager getChatMessageManager() {
        return this.chatMessageManager;
    }

    public KeyManager getKeyManager() {
        return this.keyManager;
    }

    public DevToolsButton getPlayers() {
        return this.players;
    }

    public DevToolsButton getNpcs() {
        return this.npcs;
    }

    public DevToolsButton getGroundItems() {
        return this.groundItems;
    }

    public DevToolsButton getGroundObjects() {
        return this.groundObjects;
    }

    public DevToolsButton getGameObjects() {
        return this.gameObjects;
    }

    public DevToolsButton getGraphicsObjects() {
        return this.graphicsObjects;
    }

    public DevToolsButton getWalls() {
        return this.walls;
    }

    public DevToolsButton getDecorations() {
        return this.decorations;
    }

    public DevToolsButton getProjectiles() {
        return this.projectiles;
    }

    public DevToolsButton getLocation() {
        return this.location;
    }

    public DevToolsButton getChunkBorders() {
        return this.chunkBorders;
    }

    public DevToolsButton getMapSquares() {
        return this.mapSquares;
    }

    public DevToolsButton getValidMovement() {
        return this.validMovement;
    }

    public DevToolsButton getMovementFlags() {
        return this.movementFlags;
    }

    public DevToolsButton getLineOfSight() {
        return this.lineOfSight;
    }

    public DevToolsButton getCameraPosition() {
        return this.cameraPosition;
    }

    public DevToolsButton getWorldMapLocation() {
        return this.worldMapLocation;
    }

    public DevToolsButton getTileLocation() {
        return this.tileLocation;
    }

    public DevToolsButton getInteracting() {
        return this.interacting;
    }

    public DevToolsButton getExamine() {
        return this.examine;
    }

    public DevToolsButton getDetachedCamera() {
        return this.detachedCamera;
    }

    public DevToolsButton getWidgetInspector() {
        return this.widgetInspector;
    }

    public DevToolsButton getVarInspector() {
        return this.varInspector;
    }

    public DevToolsButton getSoundEffects() {
        return this.soundEffects;
    }

    public DevToolsButton getScriptInspector() {
        return this.scriptInspector;
    }

    public DevToolsButton getInventoryInspector() {
        return this.inventoryInspector;
    }

    public DevToolsButton getRoofs() {
        return this.roofs;
    }

    public DevToolsButton getShell() {
        return this.shell;
    }

    public DevToolsButton getInterfacePath() {
        return this.interfacePath;
    }

    public DevToolsButton getScriptPath() {
        return this.scriptPath;
    }

    public DevToolsButton getNetwork() {
        return this.network;
    }

    public NavigationButton getNavButton() {
        return this.navButton;
    }
}

