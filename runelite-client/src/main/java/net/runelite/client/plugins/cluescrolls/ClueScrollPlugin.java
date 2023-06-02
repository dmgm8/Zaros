/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.MoreObjects
 *  com.google.inject.Binder
 *  com.google.inject.Provides
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  joptsimple.internal.Strings
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.EnumComposition
 *  net.runelite.api.GameObject
 *  net.runelite.api.GameState
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.MenuAction
 *  net.runelite.api.NPC
 *  net.runelite.api.ObjectComposition
 *  net.runelite.api.Point
 *  net.runelite.api.Scene
 *  net.runelite.api.Tile
 *  net.runelite.api.TileObject
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.CommandExecuted
 *  net.runelite.api.events.DecorativeObjectDespawned
 *  net.runelite.api.events.DecorativeObjectSpawned
 *  net.runelite.api.events.GameObjectDespawned
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.GroundObjectDespawned
 *  net.runelite.api.events.GroundObjectSpawned
 *  net.runelite.api.events.ItemContainerChanged
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.NpcSpawned
 *  net.runelite.api.events.WallObjectDespawned
 *  net.runelite.api.events.WallObjectSpawned
 *  net.runelite.api.events.WidgetLoaded
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.apache.commons.lang3.ArrayUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.cluescrolls;

import com.google.common.base.MoreObjects;
import com.google.inject.Binder;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import joptsimple.internal.Strings;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Point;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.banktags.BankTagsPlugin;
import net.runelite.client.plugins.banktags.TagManager;
import net.runelite.client.plugins.cluescrolls.ClueScrollConfig;
import net.runelite.client.plugins.cluescrolls.ClueScrollEmoteOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollMusicOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollService;
import net.runelite.client.plugins.cluescrolls.ClueScrollServiceImpl;
import net.runelite.client.plugins.cluescrolls.ClueScrollWorldMapPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollWorldOverlay;
import net.runelite.client.plugins.cluescrolls.clues.AnagramClue;
import net.runelite.client.plugins.cluescrolls.clues.BeginnerMapClue;
import net.runelite.client.plugins.cluescrolls.clues.CipherClue;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.CoordinateClue;
import net.runelite.client.plugins.cluescrolls.clues.CrypticClue;
import net.runelite.client.plugins.cluescrolls.clues.EmoteClue;
import net.runelite.client.plugins.cluescrolls.clues.FairyRingClue;
import net.runelite.client.plugins.cluescrolls.clues.FaloTheBardClue;
import net.runelite.client.plugins.cluescrolls.clues.HotColdClue;
import net.runelite.client.plugins.cluescrolls.clues.LocationClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.LocationsClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.MapClue;
import net.runelite.client.plugins.cluescrolls.clues.MonsterClue;
import net.runelite.client.plugins.cluescrolls.clues.MusicClue;
import net.runelite.client.plugins.cluescrolls.clues.NamedObjectClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.NpcClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.ObjectClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.SkillChallengeClue;
import net.runelite.client.plugins.cluescrolls.clues.TextClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.ThreeStepCrypticClue;
import net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirement;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Clue Scroll", description="Show answers to clue scroll riddles, anagrams, ciphers, and cryptic clues", tags={"arrow", "hints", "world", "map", "coordinates", "emotes"})
@PluginDependency(value=BankTagsPlugin.class)
public class ClueScrollPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(ClueScrollPlugin.class);
    private static final Color HIGHLIGHT_BORDER_COLOR = Color.ORANGE;
    private static final Color HIGHLIGHT_HOVER_BORDER_COLOR = HIGHLIGHT_BORDER_COLOR.darker();
    private static final Color HIGHLIGHT_FILL_COLOR = new Color(0, 255, 0, 20);
    private static final String CLUE_TAG_NAME = "clue";
    private static final int[] RUNEPOUCH_AMOUNT_VARBITS = new int[]{1624, 1625, 1626, 14286};
    private static final int[] RUNEPOUCH_RUNE_VARBITS = new int[]{29, 1622, 1623, 14285};
    private ClueScroll clue;
    private final List<NPC> npcsToMark = new ArrayList<NPC>();
    private final List<TileObject> objectsToMark = new ArrayList<TileObject>();
    private final Set<TileObject> namedObjectsToMark = new HashSet<TileObject>();
    private Item[] equippedItems;
    private Item[] inventoryItems;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ItemManager itemManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ClueScrollOverlay clueScrollOverlay;
    @Inject
    private ClueScrollEmoteOverlay clueScrollEmoteOverlay;
    @Inject
    private ClueScrollMusicOverlay clueScrollMusicOverlay;
    @Inject
    private ClueScrollWorldOverlay clueScrollWorldOverlay;
    @Inject
    private ClueScrollConfig config;
    @Inject
    private WorldMapPointManager worldMapPointManager;
    @Inject
    private TagManager tagManager;
    @Inject
    @Named(value="developerMode")
    boolean developerMode;
    private BufferedImage emoteImage;
    private BufferedImage mapArrow;
    private Integer clueItemId;
    private boolean worldMapPointsSet = false;
    private int currentPlane = -1;
    private boolean namedObjectCheckThisTick;
    private final TextComponent textComponent = new TextComponent();
    private EmoteClue activeSTASHClue;
    private EmoteClue clickedSTASHClue;

    @Provides
    ClueScrollConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(ClueScrollConfig.class);
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(ClueScrollService.class).to(ClueScrollServiceImpl.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.clueScrollOverlay);
        this.overlayManager.add(this.clueScrollEmoteOverlay);
        this.overlayManager.add(this.clueScrollWorldOverlay);
        this.overlayManager.add(this.clueScrollMusicOverlay);
        this.tagManager.registerTag(CLUE_TAG_NAME, this::testClueTag);
    }

    @Override
    protected void shutDown() throws Exception {
        this.tagManager.unregisterTag(CLUE_TAG_NAME);
        this.overlayManager.remove(this.clueScrollOverlay);
        this.overlayManager.remove(this.clueScrollEmoteOverlay);
        this.overlayManager.remove(this.clueScrollWorldOverlay);
        this.overlayManager.remove(this.clueScrollMusicOverlay);
        this.npcsToMark.clear();
        this.namedObjectsToMark.clear();
        this.inventoryItems = null;
        this.equippedItems = null;
        this.currentPlane = -1;
        this.namedObjectCheckThisTick = false;
        this.resetClue(true);
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String text;
        if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM) {
            return;
        }
        String message = event.getMessage();
        if (this.clue instanceof HotColdClue && ((HotColdClue)this.clue).update(message, this)) {
            this.worldMapPointsSet = false;
        }
        if (this.clue instanceof SkillChallengeClue && ((text = Text.removeTags(message)).equals("Skill challenge completed.") || text.equals("You have completed your master level challenge!") || text.startsWith("You have completed Charlie's task,") || text.equals("You have completed this challenge scroll."))) {
            ((SkillChallengeClue)this.clue).setChallengeCompleted(true);
        }
        if (message.endsWith(" the STASH unit.")) {
            if (this.clue instanceof EmoteClue && this.clickedSTASHClue != null && message.equals("You withdraw your items from the STASH unit.")) {
                this.activeSTASHClue = this.clickedSTASHClue;
            } else if (message.equals("You deposit your items into the STASH unit.")) {
                this.activeSTASHClue = null;
            }
            this.clickedSTASHClue = null;
        }
    }

    @Subscribe
    public void onOverlayMenuClicked(OverlayMenuClicked overlayMenuClicked) {
        OverlayMenuEntry overlayMenuEntry = overlayMenuClicked.getEntry();
        if (overlayMenuEntry.getMenuAction() == MenuAction.RUNELITE_OVERLAY && overlayMenuClicked.getEntry().getOption().equals("Reset") && overlayMenuClicked.getOverlay() == this.clueScrollOverlay) {
            this.resetClue(true);
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        EmoteClue emoteClue;
        boolean isXMarksTheSpotOrb;
        if (event.getMenuOption() == null) {
            return;
        }
        boolean bl = isXMarksTheSpotOrb = event.getItemId() == 23069;
        if (isXMarksTheSpotOrb || event.getMenuOption().equals("Read")) {
            ItemComposition itemComposition = this.itemManager.getItemComposition(event.getItemId());
            if (isXMarksTheSpotOrb || itemComposition.getName().startsWith("Clue scroll") || itemComposition.getName().startsWith("Challenge scroll") || itemComposition.getName().startsWith("Treasure scroll")) {
                this.clueItemId = itemComposition.getId();
                this.updateClue(MapClue.forItemId(this.clueItemId));
            }
        } else if (event.getMenuOption().equals("Search") && this.clue instanceof EmoteClue && (emoteClue = (EmoteClue)this.clue).getStashUnit() != null && emoteClue.getStashUnit().getObjectId() == event.getId()) {
            this.clickedSTASHClue = emoteClue;
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        List<Item> runePouchContents;
        ItemContainer itemContainer = event.getItemContainer();
        if (event.getContainerId() == InventoryID.EQUIPMENT.getId()) {
            this.equippedItems = itemContainer.getItems();
            return;
        }
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) {
            return;
        }
        this.inventoryItems = itemContainer.getItems();
        if ((itemContainer.contains(12791) || itemContainer.contains(24416) || itemContainer.contains(27281)) && !(runePouchContents = this.getRunepouchContents()).isEmpty()) {
            block0: for (int i = 0; i < this.inventoryItems.length; ++i) {
                Item invItem = this.inventoryItems[i];
                for (Item rune : runePouchContents) {
                    if (invItem.getId() != rune.getId()) continue;
                    this.inventoryItems[i] = new Item(invItem.getId(), rune.getQuantity() + invItem.getQuantity());
                    runePouchContents.remove((Object)rune);
                    continue block0;
                }
            }
            this.inventoryItems = (Item[])ArrayUtils.addAll((Object[])this.inventoryItems, (Object[])runePouchContents.toArray((T[])new Item[0]));
        }
        if (this.clue != null && this.clueItemId != null && !itemContainer.contains(this.clueItemId.intValue())) {
            this.resetClue(true);
        }
        if (this.clue instanceof ThreeStepCrypticClue && ((ThreeStepCrypticClue)this.clue).update(event.getContainerId(), itemContainer)) {
            this.worldMapPointsSet = false;
            this.npcsToMark.clear();
            if (this.config.displayHintArrows()) {
                this.client.clearHintArrow();
            }
            this.checkClueNPCs(this.clue, this.client.getCachedNPCs());
        }
    }

    private List<Item> getRunepouchContents() {
        EnumComposition runepouchEnum = this.client.getEnum(982);
        ArrayList<Item> items = new ArrayList<Item>(RUNEPOUCH_AMOUNT_VARBITS.length);
        for (int i = 0; i < RUNEPOUCH_AMOUNT_VARBITS.length; ++i) {
            int runeId;
            int amount = this.client.getVarbitValue(RUNEPOUCH_AMOUNT_VARBITS[i]);
            if (amount <= 0 || (runeId = this.client.getVarbitValue(RUNEPOUCH_RUNE_VARBITS[i])) == 0) continue;
            int itemId = runepouchEnum.getIntValue(runeId);
            Item item = new Item(itemId, amount);
            items.add(item);
        }
        return items;
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        NPC npc = event.getNpc();
        this.checkClueNPCs(this.clue, npc);
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        boolean removed = this.npcsToMark.remove((Object)event.getNpc());
        if (removed) {
            if (this.npcsToMark.isEmpty()) {
                this.client.clearHintArrow();
            } else {
                this.client.setHintArrow(this.npcsToMark.get(0));
            }
        }
    }

    @Subscribe
    public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
        this.tileObjectDespawnedHandler((TileObject)event.getDecorativeObject());
    }

    @Subscribe
    public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
        this.tileObjectSpawnedHandler((TileObject)event.getDecorativeObject());
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        this.tileObjectDespawnedHandler((TileObject)event.getGameObject());
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        this.tileObjectSpawnedHandler((TileObject)event.getGameObject());
    }

    @Subscribe
    public void onGroundObjectDespawned(GroundObjectDespawned event) {
        this.tileObjectDespawnedHandler((TileObject)event.getGroundObject());
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        this.tileObjectSpawnedHandler((TileObject)event.getGroundObject());
    }

    @Subscribe
    public void onWallObjectDespawned(WallObjectDespawned event) {
        this.tileObjectDespawnedHandler((TileObject)event.getWallObject());
    }

    @Subscribe
    public void onWallObjectSpawned(WallObjectSpawned event) {
        this.tileObjectSpawnedHandler((TileObject)event.getWallObject());
    }

    private void tileObjectDespawnedHandler(TileObject despawned) {
        this.namedObjectsToMark.remove((Object)despawned);
    }

    private void tileObjectSpawnedHandler(TileObject spawned) {
        this.checkClueNamedObject(this.clue, spawned);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("cluescroll") && !this.config.displayHintArrows()) {
            this.client.clearHintArrow();
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        GameState state = event.getGameState();
        if (state != GameState.LOGGED_IN) {
            this.namedObjectsToMark.clear();
        }
        if (state == GameState.LOGIN_SCREEN) {
            this.resetClue(true);
        } else if (state == GameState.HOPPING) {
            this.namedObjectCheckThisTick = true;
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        WorldPoint[] locations;
        this.objectsToMark.clear();
        if (this.clue instanceof LocationsClueScroll) {
            int[] objectIds;
            locations = ((LocationsClueScroll)((Object)this.clue)).getLocations();
            if (locations.length > 0) {
                this.addMapPoints(locations);
            }
            if (this.clue instanceof ObjectClueScroll && (objectIds = ((ObjectClueScroll)((Object)this.clue)).getObjectIds()).length > 0) {
                for (WorldPoint location : locations) {
                    if (location == null) continue;
                    this.highlightObjectsForLocation(location, objectIds);
                }
            }
        }
        if (this.clue instanceof LocationClueScroll) {
            boolean npcHintArrowMarked;
            locations = ((LocationClueScroll)((Object)this.clue)).getLocations();
            boolean bl = npcHintArrowMarked = this.client.getHintArrowNpc() != null && this.npcsToMark.contains((Object)this.client.getHintArrowNpc());
            if (!npcHintArrowMarked) {
                this.client.clearHintArrow();
            }
            for (WorldPoint location : locations) {
                int[] objectIds;
                if (location.isInScene(this.client) && this.config.displayHintArrows() && !npcHintArrowMarked) {
                    this.client.setHintArrow(location);
                }
                this.addMapPoints(location);
                if (!(this.clue instanceof ObjectClueScroll) || (objectIds = ((ObjectClueScroll)((Object)this.clue)).getObjectIds()).length <= 0) continue;
                this.highlightObjectsForLocation(location, objectIds);
            }
        }
        if (this.currentPlane != this.client.getPlane()) {
            this.namedObjectsToMark.clear();
            this.currentPlane = this.client.getPlane();
            this.namedObjectCheckThisTick = true;
        } else if (this.namedObjectCheckThisTick) {
            this.namedObjectCheckThisTick = false;
            this.checkClueNamedObjects(this.clue);
        }
        Widget chatDialogClueItem = this.client.getWidget(WidgetInfo.DIALOG_SPRITE_SPRITE);
        if (chatDialogClueItem != null && (chatDialogClueItem.getItemId() == 23182 || chatDialogClueItem.getItemId() == 19835)) {
            this.resetClue(false);
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        if (event.getGroupId() >= 346 && event.getGroupId() <= 356) {
            this.updateClue(BeginnerMapClue.forWidgetID(event.getGroupId()));
        } else if (event.getGroupId() == 203) {
            this.clientThread.invokeLater(() -> {
                Widget clueScrollText = this.client.getWidget(WidgetInfo.CLUE_SCROLL_TEXT);
                if (clueScrollText != null) {
                    ClueScroll clueScroll = this.findClueScroll(clueScrollText.getText());
                    if (clueScroll != null) {
                        this.updateClue(clueScroll);
                    } else {
                        log.info("Unknown clue text: {}", (Object)clueScrollText.getText());
                        this.resetClue(true);
                    }
                }
            });
        }
    }

    @Subscribe
    public void onCommandExecuted(CommandExecuted commandExecuted) {
        if (this.developerMode && commandExecuted.getCommand().equals(CLUE_TAG_NAME)) {
            String text = Strings.join((String[])commandExecuted.getArguments(), (String)" ");
            if (text.isEmpty()) {
                this.resetClue(true);
            } else {
                ClueScroll clueScroll = this.findClueScroll(text);
                log.debug("Found clue scroll for '{}': {}", (Object)text, (Object)clueScroll);
                this.updateClue(clueScroll);
            }
        }
    }

    public BufferedImage getClueScrollImage() {
        return this.itemManager.getImage(19835);
    }

    public BufferedImage getEmoteImage() {
        if (this.emoteImage != null) {
            return this.emoteImage;
        }
        this.emoteImage = ImageUtil.loadImageResource(this.getClass(), "emote.png");
        return this.emoteImage;
    }

    public BufferedImage getSpadeImage() {
        return this.itemManager.getImage(952);
    }

    BufferedImage getMapArrow() {
        if (this.mapArrow != null) {
            return this.mapArrow;
        }
        this.mapArrow = ImageUtil.loadImageResource(this.getClass(), "/util/clue_arrow.png");
        return this.mapArrow;
    }

    private void resetClue(boolean withItemId) {
        if (this.clue instanceof LocationsClueScroll) {
            ((LocationsClueScroll)((Object)this.clue)).reset();
        }
        if (withItemId) {
            this.clueItemId = null;
        }
        this.clue = null;
        this.worldMapPointManager.removeIf(ClueScrollWorldMapPoint.class::isInstance);
        this.worldMapPointsSet = false;
        this.npcsToMark.clear();
        this.namedObjectsToMark.clear();
        if (this.config.displayHintArrows()) {
            this.client.clearHintArrow();
        }
    }

    private ClueScroll findClueScroll(String rawText) {
        String text = Text.sanitizeMultilineText(rawText).toLowerCase();
        if (this.clue instanceof TextClueScroll && ((TextClueScroll)((Object)this.clue)).getText().equalsIgnoreCase(text)) {
            return this.clue;
        }
        if (text.startsWith("i'd like to hear some music.")) {
            return MusicClue.forText(rawText);
        }
        if (text.contains("degrees") && text.contains("minutes")) {
            return this.coordinatesToWorldPoint(text);
        }
        AnagramClue anagramClue = AnagramClue.forText(text);
        if (anagramClue != null) {
            return anagramClue;
        }
        CipherClue cipherClue = CipherClue.forText(text);
        if (cipherClue != null) {
            return cipherClue;
        }
        CrypticClue crypticClue = CrypticClue.forText(text);
        if (crypticClue != null) {
            return crypticClue;
        }
        EmoteClue emoteClue = EmoteClue.forText(text);
        if (emoteClue != null) {
            return emoteClue;
        }
        FairyRingClue fairyRingClue = FairyRingClue.forText(text);
        if (fairyRingClue != null) {
            return fairyRingClue;
        }
        FaloTheBardClue faloTheBardClue = FaloTheBardClue.forText(text);
        if (faloTheBardClue != null) {
            return faloTheBardClue;
        }
        HotColdClue hotColdClue = HotColdClue.forText(text);
        if (hotColdClue != null) {
            return hotColdClue;
        }
        SkillChallengeClue skillChallengeClue = SkillChallengeClue.forText(text, rawText);
        if (skillChallengeClue != null) {
            return skillChallengeClue;
        }
        ThreeStepCrypticClue threeStepCrypticClue = ThreeStepCrypticClue.forText(text, rawText);
        if (threeStepCrypticClue != null) {
            return threeStepCrypticClue;
        }
        MonsterClue monsterClue = MonsterClue.forText(text);
        if (monsterClue != null) {
            return monsterClue;
        }
        return null;
    }

    private CoordinateClue coordinatesToWorldPoint(String text) {
        WorldPoint mirrorPoint;
        WorldPoint coordinate;
        String[] splitText = text.split(" ");
        if (splitText.length != 10) {
            log.warn("Splitting \"" + text + "\" did not result in an array of 10 cells");
            return null;
        }
        if (!splitText[1].startsWith("degree") || !splitText[3].startsWith("minute")) {
            log.warn("\"" + text + "\" is not a well formed coordinate string");
            return null;
        }
        int degY = Integer.parseInt(splitText[0]);
        int minY = Integer.parseInt(splitText[2]);
        if (splitText[4].equals("south")) {
            degY *= -1;
            minY *= -1;
        }
        int degX = Integer.parseInt(splitText[5]);
        int minX = Integer.parseInt(splitText[7]);
        if (splitText[9].equals("west")) {
            degX *= -1;
            minX *= -1;
        }
        return new CoordinateClue(text, coordinate, (coordinate = this.coordinatesToWorldPoint(degX, minX, degY, minY)) == (mirrorPoint = WorldPoint.getMirrorPoint((WorldPoint)coordinate, (boolean)false)) ? null : mirrorPoint);
    }

    private WorldPoint coordinatesToWorldPoint(int degX, int minX, int degY, int minY) {
        int x2 = 2440;
        int y2 = 3161;
        x2 = (int)((long)x2 + ((long)(degX * 32) + Math.round((double)minX / 1.875)));
        y2 = (int)((long)y2 + ((long)(degY * 32) + Math.round((double)minY / 1.875)));
        return new WorldPoint(x2, y2, 0);
    }

    private void addMapPoints(WorldPoint ... points) {
        if (this.worldMapPointsSet) {
            return;
        }
        this.worldMapPointsSet = true;
        this.worldMapPointManager.removeIf(ClueScrollWorldMapPoint.class::isInstance);
        for (WorldPoint point : points) {
            this.worldMapPointManager.add(new ClueScrollWorldMapPoint(point, this));
        }
    }

    private void highlightObjectsForLocation(WorldPoint location, int ... objectIds) {
        LocalPoint localLocation = LocalPoint.fromWorld((Client)this.client, (WorldPoint)location);
        if (localLocation == null) {
            return;
        }
        Scene scene = this.client.getScene();
        Tile[][][] tiles = scene.getTiles();
        Tile tile = tiles[this.client.getPlane()][localLocation.getSceneX()][localLocation.getSceneY()];
        for (GameObject object : tile.getGameObjects()) {
            if (object == null) continue;
            for (int id : objectIds) {
                ObjectComposition impostor;
                if (object.getId() == id) {
                    this.objectsToMark.add((TileObject)object);
                    continue;
                }
                ObjectComposition comp = this.client.getObjectDefinition(object.getId());
                ObjectComposition objectComposition = impostor = comp.getImpostorIds() != null ? comp.getImpostor() : comp;
                if (impostor == null || impostor.getId() != id) continue;
                this.objectsToMark.add((TileObject)object);
            }
        }
    }

    private void checkClueNPCs(ClueScroll clue, NPC ... npcs) {
        if (!(clue instanceof NpcClueScroll)) {
            return;
        }
        NpcClueScroll npcClueScroll = (NpcClueScroll)((Object)clue);
        if (npcClueScroll.getNpcs() == null || npcClueScroll.getNpcs().length == 0) {
            return;
        }
        for (NPC npc : npcs) {
            if (npc == null || npc.getName() == null) continue;
            for (String npcName : npcClueScroll.getNpcs()) {
                if (!Objects.equals(npc.getName(), npcName)) continue;
                this.npcsToMark.add(npc);
            }
        }
        if (!this.npcsToMark.isEmpty() && this.config.displayHintArrows()) {
            this.client.setHintArrow(this.npcsToMark.get(0));
        }
    }

    private void checkClueNamedObjects(@Nullable ClueScroll clue) {
        if (!(clue instanceof NamedObjectClueScroll)) {
            return;
        }
        Tile[][] arrtile = this.client.getScene().getTiles()[this.client.getPlane()];
        int n = arrtile.length;
        for (int i = 0; i < n; ++i) {
            Tile[] tiles;
            for (Tile tile : tiles = arrtile[i]) {
                if (tile == null) continue;
                for (GameObject object : tile.getGameObjects()) {
                    if (object == null) continue;
                    this.checkClueNamedObject(clue, (TileObject)object);
                }
            }
        }
    }

    private void checkClueNamedObject(@Nullable ClueScroll clue, @Nonnull TileObject object) {
        if (!(clue instanceof NamedObjectClueScroll)) {
            return;
        }
        NamedObjectClueScroll namedObjectClue = (NamedObjectClueScroll)((Object)clue);
        String[] objectNames = namedObjectClue.getObjectNames();
        int[] regionIds = namedObjectClue.getObjectRegions();
        if (objectNames == null || objectNames.length == 0 || regionIds != null && !ArrayUtils.contains((int[])regionIds, (int)object.getWorldLocation().getRegionID())) {
            return;
        }
        ObjectComposition comp = this.client.getObjectDefinition(object.getId());
        ObjectComposition impostor = comp.getImpostorIds() != null ? comp.getImpostor() : comp;
        for (String name : objectNames) {
            if (!comp.getName().equals(name) && !impostor.getName().equals(name)) continue;
            this.namedObjectsToMark.add(object);
        }
    }

    private void updateClue(ClueScroll clue) {
        if (clue == null || clue == this.clue) {
            return;
        }
        this.resetClue(false);
        this.checkClueNPCs(clue, this.client.getCachedNPCs());
        this.checkClueNamedObjects(clue);
        this.clue = clue;
    }

    void highlightWidget(Graphics2D graphics, Widget toHighlight, Widget container, Rectangle padding, String text) {
        padding = (Rectangle)MoreObjects.firstNonNull((Object)padding, (Object)new Rectangle());
        Point canvasLocation = toHighlight.getCanvasLocation();
        if (canvasLocation == null) {
            return;
        }
        Point windowLocation = container.getCanvasLocation();
        if (windowLocation.getY() > canvasLocation.getY() + toHighlight.getHeight() || windowLocation.getY() + container.getHeight() < canvasLocation.getY()) {
            return;
        }
        Area widgetArea = new Area(new Rectangle(canvasLocation.getX() - padding.x, Math.max(canvasLocation.getY(), windowLocation.getY()) - padding.y, toHighlight.getWidth() + padding.x + padding.width, Math.min(Math.min(windowLocation.getY() + container.getHeight() - canvasLocation.getY(), toHighlight.getHeight()), Math.min(canvasLocation.getY() + toHighlight.getHeight() - windowLocation.getY(), toHighlight.getHeight())) + padding.y + padding.height));
        OverlayUtil.renderHoverableArea(graphics, widgetArea, this.client.getMouseCanvasPosition(), HIGHLIGHT_FILL_COLOR, HIGHLIGHT_BORDER_COLOR, HIGHLIGHT_HOVER_BORDER_COLOR);
        if (text == null) {
            return;
        }
        FontMetrics fontMetrics = graphics.getFontMetrics();
        this.textComponent.setPosition(new java.awt.Point(canvasLocation.getX() + toHighlight.getWidth() / 2 - fontMetrics.stringWidth(text) / 2, canvasLocation.getY() + fontMetrics.getHeight()));
        this.textComponent.setText(text);
        this.textComponent.render(graphics);
    }

    void scrollToWidget(WidgetInfo list, WidgetInfo scrollbar, Widget ... toHighlight) {
        Widget parent = this.client.getWidget(list);
        int averageCentralY = 0;
        int nonnullCount = 0;
        for (Widget widget : toHighlight) {
            if (widget == null) continue;
            averageCentralY += widget.getRelativeY() + widget.getHeight() / 2;
            ++nonnullCount;
        }
        if (nonnullCount == 0) {
            return;
        }
        int newScroll = Math.max(0, Math.min(parent.getScrollHeight(), (averageCentralY /= nonnullCount) - parent.getHeight() / 2));
        this.client.runScript(new Object[]{72, scrollbar.getId(), list.getId(), newScroll});
    }

    private boolean testClueTag(int itemId) {
        block6: {
            ClueScroll c;
            block5: {
                c = this.clue;
                if (c == null) {
                    return false;
                }
                if (!(c instanceof EmoteClue)) break block5;
                EmoteClue emote = (EmoteClue)c;
                for (ItemRequirement ir : emote.getItemRequirements()) {
                    if (!ir.fulfilledBy(itemId)) continue;
                    return true;
                }
                break block6;
            }
            if (c instanceof CoordinateClue || c instanceof HotColdClue || c instanceof FairyRingClue) {
                return itemId == 952;
            }
            if (c instanceof MapClue) {
                MapClue mapClue = (MapClue)c;
                return mapClue.getObjectId() == -1 && itemId == 952;
            }
            if (!(c instanceof SkillChallengeClue)) break block6;
            SkillChallengeClue challengeClue = (SkillChallengeClue)c;
            for (ItemRequirement ir : challengeClue.getItemRequirements()) {
                if (!ir.fulfilledBy(itemId)) continue;
                return true;
            }
        }
        return false;
    }

    public ClueScroll getClue() {
        return this.clue;
    }

    public List<NPC> getNpcsToMark() {
        return this.npcsToMark;
    }

    public List<TileObject> getObjectsToMark() {
        return this.objectsToMark;
    }

    public Set<TileObject> getNamedObjectsToMark() {
        return this.namedObjectsToMark;
    }

    public Item[] getEquippedItems() {
        return this.equippedItems;
    }

    public Item[] getInventoryItems() {
        return this.inventoryItems;
    }

    public Client getClient() {
        return this.client;
    }

    public EmoteClue getActiveSTASHClue() {
        return this.activeSTASHClue;
    }
}

