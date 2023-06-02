/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 */
package net.runelite.api;

import com.jagex.oldscape.pub.OAuthApi;
import java.awt.Canvas;
import java.awt.Dimension;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.Actor;
import net.runelite.api.AmbientSoundEffect;
import net.runelite.api.Animation;
import net.runelite.api.BufferProvider;
import net.runelite.api.ChatLineBuffer;
import net.runelite.api.ChatMessageType;
import net.runelite.api.CollisionData;
import net.runelite.api.Deque;
import net.runelite.api.EnumComposition;
import net.runelite.api.FriendContainer;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.GameEngine;
import net.runelite.api.GameState;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GraphicsObject;
import net.runelite.api.HashTable;
import net.runelite.api.Ignore;
import net.runelite.api.IndexDataBase;
import net.runelite.api.IndexedSprite;
import net.runelite.api.IntegerNode;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.IterableHashTable;
import net.runelite.api.MapElementConfig;
import net.runelite.api.MenuEntry;
import net.runelite.api.MessageNode;
import net.runelite.api.Model;
import net.runelite.api.ModelData;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.NameableContainer;
import net.runelite.api.NodeCache;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Preferences;
import net.runelite.api.Projectile;
import net.runelite.api.RenderOverview;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.Scene;
import net.runelite.api.ScriptEvent;
import net.runelite.api.Skill;
import net.runelite.api.SpritePixels;
import net.runelite.api.StructComposition;
import net.runelite.api.TextureProvider;
import net.runelite.api.Tile;
import net.runelite.api.VarPlayer;
import net.runelite.api.VarbitComposition;
import net.runelite.api.WidgetNode;
import net.runelite.api.World;
import net.runelite.api.WorldType;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public interface Client
extends OAuthApi,
GameEngine {
    public Callbacks getCallbacks();

    public DrawCallbacks getDrawCallbacks();

    public void setDrawCallbacks(DrawCallbacks var1);

    public List<Player> getPlayers();

    public List<NPC> getNpcs();

    public NPC[] getCachedNPCs();

    public Player[] getCachedPlayers();

    public int getBoostedSkillLevel(Skill var1);

    public int getRealSkillLevel(Skill var1);

    public int getTotalLevel();

    public MessageNode addChatMessage(ChatMessageType var1, String var2, String var3, String var4);

    public MessageNode addChatMessage(ChatMessageType var1, String var2, String var3, String var4, boolean var5);

    public GameState getGameState();

    public void setGameState(GameState var1);

    public void stopNow();

    @Deprecated
    public String getUsername();

    public void setUsername(String var1);

    public void setPassword(String var1);

    public void setOtp(String var1);

    public int getCurrentLoginField();

    public int getLoginIndex();

    public AccountType getAccountType();

    @Override
    public Canvas getCanvas();

    public int getFPS();

    public int getCameraX();

    public int getCameraY();

    public int getCameraZ();

    public int getCameraPitch();

    public int getCameraYaw();

    public int getWorld();

    public int getCanvasHeight();

    public int getCanvasWidth();

    public int getViewportHeight();

    public int getViewportWidth();

    public int getViewportXOffset();

    public int getViewportYOffset();

    public int getScale();

    public Point getMouseCanvasPosition();

    public int[][][] getTileHeights();

    public byte[][][] getTileSettings();

    public int getPlane();

    public int getSceneMaxPlane();

    public Scene getScene();

    public Player getLocalPlayer();

    @Nullable
    public NPC getFollower();

    @Nonnull
    public ItemComposition getItemDefinition(int var1);

    @Nullable
    public SpritePixels createItemSprite(int var1, int var2, int var3, int var4, int var5, boolean var6, int var7);

    public NodeCache getItemModelCache();

    public NodeCache getItemSpriteCache();

    @Nullable
    public SpritePixels[] getSprites(IndexDataBase var1, int var2, int var3);

    public IndexDataBase getIndexSprites();

    public IndexDataBase getIndexScripts();

    public IndexDataBase getIndexConfig();

    public IndexDataBase getIndex(int var1);

    public int getBaseX();

    public int getBaseY();

    public int getMouseCurrentButton();

    @Nullable
    public Tile getSelectedSceneTile();

    public boolean isDraggingWidget();

    @Nullable
    public Widget getDraggedWidget();

    @Nullable
    public Widget getDraggedOnWidget();

    public void setDraggedOnWidget(Widget var1);

    public int getDragTime();

    public int getTopLevelInterfaceId();

    public Widget[] getWidgetRoots();

    @Nullable
    public Widget getWidget(WidgetInfo var1);

    @Nullable
    public Widget getWidget(int var1, int var2);

    @Nullable
    public Widget getWidget(int var1);

    public int[] getWidgetPositionsX();

    public int[] getWidgetPositionsY();

    public int getEnergy();

    public int getWeight();

    public String[] getPlayerOptions();

    public boolean[] getPlayerOptionsPriorities();

    public int[] getPlayerMenuTypes();

    public World[] getWorldList();

    public MenuEntry createMenuEntry(int var1);

    public MenuEntry[] getMenuEntries();

    public void setMenuEntries(MenuEntry[] var1);

    public boolean isMenuOpen();

    public int getMenuX();

    public int getMenuY();

    public int getMenuHeight();

    public int getMenuWidth();

    public int getMapAngle();

    public void setCameraYawTarget(int var1);

    public boolean isResized();

    public int getRevision();

    public int[] getMapRegions();

    public int[][][] getInstanceTemplateChunks();

    public int[][] getXteaKeys();

    public int[] getVarps();

    public int[] getServerVarps();

    public Map<Integer, Object> getVarcMap();

    @Deprecated
    public int getVar(VarPlayer var1);

    public int getVarpValue(VarPlayer var1);

    @Deprecated
    public int getVar(int var1);

    public int getVarbitValue(int var1);

    public int getServerVarbitValue(int var1);

    public int getVarpValue(int var1);

    public int getServerVarpValue(int var1);

    public int getVarcIntValue(int var1);

    public String getVarcStrValue(int var1);

    public void setVarcStrValue(int var1, String var2);

    public void setVarcIntValue(int var1, int var2);

    public void setVarbit(int var1, int var2);

    @Nullable
    public VarbitComposition getVarbit(int var1);

    public int getVarbitValue(int[] var1, int var2);

    public void setVarbitValue(int[] var1, int var2, int var3);

    public void queueChangedVarp(int var1);

    public HashTable<IntegerNode> getWidgetFlags();

    public HashTable<WidgetNode> getComponentTable();

    public GrandExchangeOffer[] getGrandExchangeOffers();

    public boolean isPrayerActive(Prayer var1);

    public int getSkillExperience(Skill var1);

    public long getOverallExperience();

    public void refreshChat();

    public Map<Integer, ChatLineBuffer> getChatLineMap();

    public IterableHashTable<MessageNode> getMessages();

    public ObjectComposition getObjectDefinition(int var1);

    public NPCComposition getNpcDefinition(int var1);

    public StructComposition getStructComposition(int var1);

    public NodeCache getStructCompositionCache();

    public Object getDBTableField(int var1, int var2, int var3, int var4);

    public MapElementConfig[] getMapElementConfigs();

    public IndexedSprite[] getMapScene();

    public SpritePixels[] getMapDots();

    public int getGameCycle();

    public SpritePixels[] getMapIcons();

    public IndexedSprite[] getModIcons();

    public void setModIcons(IndexedSprite[] var1);

    public IndexedSprite createIndexedSprite();

    public SpritePixels createSpritePixels(int[] var1, int var2, int var3);

    @Nullable
    public LocalPoint getLocalDestinationLocation();

    public Projectile createProjectile(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, @Nullable Actor var11, int var12, int var13);

    public Deque<Projectile> getProjectiles();

    public Deque<GraphicsObject> getGraphicsObjects();

    public RuneLiteObject createRuneLiteObject();

    @Nullable
    public ModelData loadModelData(int var1);

    public ModelData mergeModels(ModelData[] var1, int var2);

    public ModelData mergeModels(ModelData ... var1);

    @Nullable
    public Model loadModel(int var1);

    @Nullable
    public Model loadModel(int var1, short[] var2, short[] var3);

    public Animation loadAnimation(int var1);

    public int getMusicVolume();

    public void setMusicVolume(int var1);

    public boolean isPlayingJingle();

    public int getMusicCurrentTrackId();

    public void playSoundEffect(int var1);

    public void playSoundEffect(int var1, int var2, int var3, int var4);

    public void playSoundEffect(int var1, int var2, int var3, int var4, int var5);

    public void playSoundEffect(int var1, int var2);

    public BufferProvider getBufferProvider();

    public int getMouseIdleTicks();

    public long getMouseLastPressedMillis();

    public int getKeyboardIdleTicks();

    public void changeMemoryMode(boolean var1);

    @Nullable
    public ItemContainer getItemContainer(InventoryID var1);

    @Nullable
    public ItemContainer getItemContainer(int var1);

    public HashTable<ItemContainer> getItemContainers();

    public int getIntStackSize();

    public void setIntStackSize(int var1);

    public int[] getIntStack();

    public int getStringStackSize();

    public void setStringStackSize(int var1);

    public String[] getStringStack();

    public Widget getScriptActiveWidget();

    public Widget getScriptDotWidget();

    public boolean isFriended(String var1, boolean var2);

    @Nullable
    public FriendsChatManager getFriendsChatManager();

    public FriendContainer getFriendContainer();

    public NameableContainer<Ignore> getIgnoreContainer();

    public Preferences getPreferences();

    public void setCameraPitchRelaxerEnabled(boolean var1);

    public void setInvertYaw(boolean var1);

    public void setInvertPitch(boolean var1);

    public RenderOverview getRenderOverview();

    public boolean isStretchedEnabled();

    public void setStretchedEnabled(boolean var1);

    public boolean isStretchedFast();

    public void setStretchedFast(boolean var1);

    public void setStretchedIntegerScaling(boolean var1);

    public void setStretchedKeepAspectRatio(boolean var1);

    public void setScalingFactor(int var1);

    public void invalidateStretching(boolean var1);

    public Dimension getStretchedDimensions();

    public Dimension getRealDimensions();

    public void changeWorld(World var1);

    public World createWorld();

    public SpritePixels drawInstanceMap(int var1);

    public void runScript(Object ... var1);

    public ScriptEvent createScriptEvent(Object ... var1);

    public boolean hasHintArrow();

    public int getHintArrowType();

    public void clearHintArrow();

    public void setHintArrow(WorldPoint var1);

    public void setHintArrow(LocalPoint var1);

    public void setHintArrow(Player var1);

    public void setHintArrow(NPC var1);

    public WorldPoint getHintArrowPoint();

    public Player getHintArrowPlayer();

    public NPC getHintArrowNpc();

    public boolean isInterpolatePlayerAnimations();

    public void setInterpolatePlayerAnimations(boolean var1);

    public boolean isInterpolateNpcAnimations();

    public void setInterpolateNpcAnimations(boolean var1);

    public boolean isInterpolateObjectAnimations();

    public void setInterpolateObjectAnimations(boolean var1);

    public boolean isInInstancedRegion();

    public int getItemPressedDuration();

    @Nullable
    public CollisionData[] getCollisionMaps();

    public int[] getBoostedSkillLevels();

    public int[] getRealSkillLevels();

    public int[] getSkillExperiences();

    public void queueChangedSkill(Skill var1);

    public Map<Integer, SpritePixels> getSpriteOverrides();

    public Map<Integer, SpritePixels> getWidgetSpriteOverrides();

    public void setCompass(SpritePixels var1);

    public NodeCache getWidgetSpriteCache();

    public int getTickCount();

    public void setTickCount(int var1);

    @Deprecated
    public void setInventoryDragDelay(int var1);

    public EnumSet<WorldType> getWorldType();

    public int getOculusOrbState();

    public void setOculusOrbState(int var1);

    public void setOculusOrbNormalSpeed(int var1);

    public int getOculusOrbFocalPointX();

    public int getOculusOrbFocalPointY();

    public void openWorldHopper();

    public void hopToWorld(World var1);

    public void setSkyboxColor(int var1);

    public int getSkyboxColor();

    public boolean isGpu();

    public void setGpu(boolean var1);

    public int get3dZoom();

    public int getCenterX();

    public int getCenterY();

    public int getCameraX2();

    public int getCameraY2();

    public int getCameraZ2();

    public TextureProvider getTextureProvider();

    public void setRenderArea(boolean[][] var1);

    public int getRasterizer3D_clipMidX2();

    public int getRasterizer3D_clipNegativeMidX();

    public int getRasterizer3D_clipNegativeMidY();

    public int getRasterizer3D_clipMidY2();

    public void checkClickbox(Model var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10);

    @Deprecated
    public Widget getIf1DraggedWidget();

    @Deprecated
    public int getIf1DraggedItemIndex();

    public boolean getSpellSelected();

    public void setSpellSelected(boolean var1);

    public int getSelectedItem();

    public int getSelectedItemIndex();

    @Nullable
    public Widget getSelectedWidget();

    public NodeCache getItemCompositionCache();

    public NodeCache getObjectCompositionCache();

    public SpritePixels[] getCrossSprites();

    public EnumComposition getEnum(int var1);

    public void draw2010Menu(int var1);

    public void drawOriginalMenu(int var1);

    public void resetHealthBarCaches();

    public int getItemCount();

    public void setAllWidgetsAreOpTargetable(boolean var1);

    public void setGeSearchResultCount(int var1);

    public void setGeSearchResultIds(short[] var1);

    public void setGeSearchResultIndex(int var1);

    public void setLoginScreen(SpritePixels var1);

    public void setShouldRenderLoginScreenFire(boolean var1);

    public boolean isKeyPressed(int var1);

    public long[] getCrossWorldMessageIds();

    public int getCrossWorldMessageIdsIndex();

    @Nullable
    public ClanChannel getClanChannel();

    @Nullable
    public ClanChannel getGuestClanChannel();

    @Nullable
    public ClanSettings getClanSettings();

    @Nullable
    public ClanSettings getGuestClanSettings();

    @Nullable
    public ClanChannel getClanChannel(int var1);

    @Nullable
    public ClanSettings getClanSettings(int var1);

    public void setUnlockedFps(boolean var1);

    public void setUnlockedFpsTarget(int var1);

    public Deque<AmbientSoundEffect> getAmbientSoundEffects();

    public void setIdleTimeout(int var1);

    public int getIdleTimeout();

    public void setDevelopmentInterfaceLocation(String var1);

    public void setDevelopmentScriptLocation(String var1);

    public void setPrioritizeTeamAndClan(boolean var1);

    public int getNetworkBytesRead();

    public int getNetworkBytesWritten();

    public int getNetworkBytesReadTotal();

    public int getNetworkBytesWrittenTotal();

    public boolean getDisableMinimenu();

    public void setDisableMinimenu(boolean var1);
}

