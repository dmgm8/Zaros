/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.runelite.api.Animation
 *  net.runelite.api.Client
 *  net.runelite.api.Deque
 *  net.runelite.api.GraphicsObject
 *  net.runelite.api.IndexDataBase
 *  net.runelite.api.NodeCache
 *  net.runelite.api.Projectile
 *  net.runelite.api.SpritePixels
 *  net.runelite.api.StructComposition
 *  net.runelite.api.World
 *  net.runelite.api.clan.ClanChannel
 *  net.runelite.api.clan.ClanRank
 *  net.runelite.api.clan.ClanSettings
 *  net.runelite.api.widgets.Widget
 */
package rs.api;

import java.util.Map;
import javax.annotation.Nullable;
import net.runelite.api.Animation;
import net.runelite.api.Client;
import net.runelite.api.Deque;
import net.runelite.api.GraphicsObject;
import net.runelite.api.IndexDataBase;
import net.runelite.api.NodeCache;
import net.runelite.api.Projectile;
import net.runelite.api.SpritePixels;
import net.runelite.api.StructComposition;
import net.runelite.api.World;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.widgets.Widget;
import net.runelite.mapping.Construct;
import net.runelite.mapping.Import;
import net.runelite.rs.api.RSAudioTaskNode;
import net.runelite.rs.api.RSAudioTaskNodeQueue;
import net.runelite.rs.api.RSBuffer;
import net.runelite.rs.api.RSBufferProvider;
import net.runelite.rs.api.RSChatCrownType;
import net.runelite.rs.api.RSClanChannel;
import net.runelite.rs.api.RSClanSettings;
import net.runelite.rs.api.RSClientProt;
import net.runelite.rs.api.RSCollisionData;
import net.runelite.rs.api.RSDBRowType;
import net.runelite.rs.api.RSDBTableType;
import net.runelite.rs.api.RSDeque;
import net.runelite.rs.api.RSEnumComposition;
import net.runelite.rs.api.RSFont;
import net.runelite.rs.api.RSFrames;
import net.runelite.rs.api.RSFriendManager;
import net.runelite.rs.api.RSFriendsChatManager;
import net.runelite.rs.api.RSGameEngine;
import net.runelite.rs.api.RSGrandExchangeOffer;
import net.runelite.rs.api.RSHashTable;
import net.runelite.rs.api.RSIndexData;
import net.runelite.rs.api.RSIndexDataBase;
import net.runelite.rs.api.RSIndexedSprite;
import net.runelite.rs.api.RSIntegerNode;
import net.runelite.rs.api.RSIsaac;
import net.runelite.rs.api.RSItemComposition;
import net.runelite.rs.api.RSIterableHashTable;
import net.runelite.rs.api.RSJagexLoginType;
import net.runelite.rs.api.RSMapElementConfig;
import net.runelite.rs.api.RSMiniMenuEntry;
import net.runelite.rs.api.RSModelData;
import net.runelite.rs.api.RSMusicPlayer;
import net.runelite.rs.api.RSNPC;
import net.runelite.rs.api.RSNPCComposition;
import net.runelite.rs.api.RSName;
import net.runelite.rs.api.RSNodeCache;
import net.runelite.rs.api.RSObjectComposition;
import net.runelite.rs.api.RSObjectNode;
import net.runelite.rs.api.RSPacketNode;
import net.runelite.rs.api.RSParamComposition;
import net.runelite.rs.api.RSPlayer;
import net.runelite.rs.api.RSPreferences;
import net.runelite.rs.api.RSProjectile;
import net.runelite.rs.api.RSRawAudioNode;
import net.runelite.rs.api.RSRenderOverview;
import net.runelite.rs.api.RSResampler;
import net.runelite.rs.api.RSScene;
import net.runelite.rs.api.RSScript;
import net.runelite.rs.api.RSScriptEvent;
import net.runelite.rs.api.RSSoundEffect;
import net.runelite.rs.api.RSSpritePixels;
import net.runelite.rs.api.RSTextureProvider;
import net.runelite.rs.api.RSTileItem;
import net.runelite.rs.api.RSTradingPostResults;
import net.runelite.rs.api.RSVarcs;
import net.runelite.rs.api.RSWidget;
import net.runelite.rs.api.RSWorld;

public interface RSClient
extends RSGameEngine,
Client {
    @Import(value="cameraX")
    public int getCameraX();

    @Import(value="cameraY")
    public int getCameraY();

    @Import(value="cameraZ")
    public int getCameraZ();

    @Import(value="cameraX2")
    public int getCameraX2();

    @Import(value="cameraY2")
    public int getCameraY2();

    @Import(value="cameraZ2")
    public int getCameraZ2();

    @Import(value="clientPlane")
    public int getPlane();

    @Import(value="Scene_plane")
    public int getSceneMaxPlane();

    @Import(value="cameraPitch")
    public int getCameraPitch();

    @Import(value="cameraPitch")
    public void setCameraPitch(int var1);

    @Import(value="cameraYaw")
    public int getCameraYaw();

    @Import(value="world")
    public int getWorld();

    @Import(value="FPS")
    public int getFPS();

    @Import(value="mapAngle")
    public int getMapAngle();

    @Import(value="mapAngle")
    public void setCameraYawTarget(int var1);

    @Import(value="tileHeights")
    public int[][][] getTileHeights();

    @Import(value="tileSettings")
    public byte[][][] getTileSettings();

    @Import(value="clientVarps")
    public int[] getVarps();

    @Import(value="serverVarps")
    public int[] getServerVarps();

    @Import(value="varcs")
    public RSVarcs getVarcs();

    @Import(value="energy")
    public int getEnergy();

    @Import(value="weight")
    public int getWeight();

    @Import(value="baseX")
    public int getBaseX();

    @Import(value="baseY")
    public int getBaseY();

    @Import(value="boostedSkillLevels")
    public int[] getBoostedSkillLevels();

    @Import(value="realSkillLevels")
    public int[] getRealSkillLevels();

    @Import(value="skillExperiences")
    public int[] getSkillExperiences();

    @Import(value="recentStatTransmitList")
    public int[] getRecentStatTransmitList();

    @Import(value="changedSkillsCount")
    public int getChangedSkillsCount();

    @Import(value="changedSkillsCount")
    public void setChangedSkillsCount(int var1);

    @Import(value="recentVarTransmitList")
    public int[] getRecentVarTransmitList();

    @Import(value="recentVarTransmitCount")
    public int getRecentVarTransmitCount();

    @Import(value="recentVarTransmitCount")
    public void setRecentVarTransmitCount(int var1);

    @Import(value="gameState")
    public int getRSGameState();

    @Import(value="setGameState")
    public void setRSGameState(int var1);

    @Import(value="checkClick")
    public void setCheckClick(boolean var1);

    @Import(value="mouseX2")
    public void setMouseCanvasHoverPositionX(int var1);

    @Import(value="mouseY2")
    public void setMouseCanvasHoverPositionY(int var1);

    @Import(value="mouseCurrentButton")
    public int getMouseCurrentButton();

    @Import(value="selectedSceneTileX")
    public int getSelectedSceneTileX();

    @Import(value="selectedSceneTileX")
    public void setSelectedSceneTileX(int var1);

    @Import(value="selectedSceneTileY")
    public int getSelectedSceneTileY();

    @Import(value="selectedSceneTileY")
    public void setSelectedSceneTileY(int var1);

    @Import(value="draggingWidget")
    public boolean isDraggingWidget();

    @Import(value="draggedWidget")
    public RSWidget getDraggedWidget();

    @Import(value="draggedOnWidget")
    public RSWidget getDraggedOnWidget();

    @Import(value="draggedOnWidget")
    public void setDraggedOnWidget(Widget var1);

    @Import(value="dragTime")
    public int getDragTime();

    @Import(value="toplevelInterface")
    public int getTopLevelInterfaceId();

    @Import(value="widgets")
    public RSWidget[][] getWidgets();

    public RSWidget[] getGroup(int var1);

    @Import(value="scene")
    public RSScene getScene();

    @Import(value="localPlayer")
    public RSPlayer getLocalPlayer();

    @Import(value="followerIndex")
    public int getFollowerIndex();

    @Import(value="npcIndexesCount")
    public int getNpcIndexesCount();

    @Import(value="npcIndices")
    public int[] getNpcIndices();

    @Import(value="cachedNPCs")
    public RSNPC[] getCachedNPCs();

    @Import(value="collisionMaps")
    public RSCollisionData[] getCollisionMaps();

    @Import(value="playerIndexesCount")
    public int getPlayerIndexesCount();

    @Import(value="playerIndices")
    public int[] getPlayerIndices();

    @Import(value="cachedPlayers")
    public RSPlayer[] getCachedPlayers();

    @Import(value="groundItemDeque")
    public RSDeque[][][] getGroundItemDeque();

    @Import(value="username")
    public String getUsername();

    @Import(value="username")
    public void setUsername(String var1);

    @Import(value="password")
    public void setPassword(String var1);

    @Import(value="otp")
    public void setOtp(String var1);

    @Import(value="currentLoginField")
    public int getCurrentLoginField();

    @Import(value="loginIndex")
    public int getLoginIndex();

    @Import(value="playerOptions")
    public String[] getPlayerOptions();

    @Import(value="playerOptionsPriorities")
    public boolean[] getPlayerOptionsPriorities();

    @Import(value="playerMenuTypes")
    public int[] getPlayerMenuTypes();

    @Import(value="mouseX")
    public int getMouseX();

    @Import(value="mouseY")
    public int getMouseY();

    @Import(value="mouseX2")
    public int getMouseX2();

    @Import(value="mouseY2")
    public int getMouseY2();

    @Import(value="containsBounds")
    public boolean containsBounds(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

    @Import(value="checkClick")
    public boolean isCheckClick();

    @Import(value="activeMiniMenuEntry")
    @Nullable
    public RSMiniMenuEntry getActiveMiniMenuEntry();

    @Import(value="menuOptionCount")
    public int getMenuOptionCount();

    @Import(value="menuOptionCount")
    public void setMenuOptionCount(int var1);

    @Import(value="menuOptions")
    public String[] getMenuOptions();

    @Import(value="menuTargets")
    public String[] getMenuTargets();

    @Import(value="menuIdentifiers")
    public int[] getMenuIdentifiers();

    @Import(value="menuTypes")
    public int[] getMenuTypes();

    @Import(value="menuItems")
    public int[] getMenuItems();

    @Import(value="menuActionParams0")
    public int[] getMenuActionParams0();

    @Import(value="menuActionParams1")
    public int[] getMenuActionParams1();

    @Import(value="menuForceLeftClick")
    public boolean[] getMenuForceLeftClick();

    @Import(value="worldList")
    public RSWorld[] getWorldList();

    @Import(value="addChatMessage")
    public void addChatMessage(int var1, String var2, String var3, String var4);

    @Import(value="getObjectDefinition")
    public RSObjectComposition getObjectDefinition(int var1);

    @Import(value="getNpcDefinition")
    public RSNPCComposition getNpcDefinition(int var1);

    @Import(value="getStructComposition")
    public StructComposition getStructComposition(int var1);

    @Import(value="structConfigCache")
    public NodeCache getStructCompositionCache();

    @Import(value="getParamComposition")
    public RSParamComposition getParamComposition(int var1);

    @Import(value="scale")
    public int getScale();

    @Import(value="canvasHeight")
    public int getCanvasHeight();

    @Import(value="canvasWidth")
    public int getCanvasWidth();

    @Import(value="viewportHeight")
    public int getViewportHeight();

    @Import(value="viewportWidth")
    public int getViewportWidth();

    @Import(value="Viewport_xOffset")
    public int getViewportXOffset();

    @Import(value="Viewport_yOffset")
    public int getViewportYOffset();

    @Import(value="isResized")
    public boolean isResized();

    @Import(value="widgetPositionX")
    public int[] getWidgetPositionsX();

    @Import(value="widgetPositionY")
    public int[] getWidgetPositionsY();

    @Import(value="itemContainers")
    public RSHashTable getItemContainers();

    @Import(value="getItemDefinition")
    public RSItemComposition getItemDefinition(int var1);

    @Import(value="createSprite")
    public RSSpritePixels createItemSprite(int var1, int var2, int var3, int var4, int var5, boolean var6);

    @Import(value="getDBRowType")
    public RSDBRowType getDBRowType(int var1);

    @Import(value="getDBTableType")
    public RSDBTableType getDBTableType(int var1);

    @Import(value="decodeSprite")
    public void decodeSprite(byte[] var1);

    @Import(value="indexedSpriteCount")
    public int getIndexedSpriteCount();

    @Import(value="indexedSpriteWidth")
    public int getIndexedSpriteWidth();

    @Import(value="indexedSpriteHeight")
    public int getIndexedSpriteHeight();

    @Import(value="indexedSpriteOffsetXs")
    public int[] getIndexedSpriteOffsetXs();

    @Import(value="indexedSpriteOffsetXs")
    public void setIndexedSpriteOffsetXs(int[] var1);

    @Import(value="indexedSpriteOffsetYs")
    public int[] getIndexedSpriteOffsetYs();

    @Import(value="indexedSpriteOffsetYs")
    public void setIndexedSpriteOffsetYs(int[] var1);

    @Import(value="indexSpriteWidths")
    public int[] getIndexSpriteWidths();

    @Import(value="indexSpriteWidths")
    public void setIndexSpriteWidths(int[] var1);

    @Import(value="indexedSpriteHeights")
    public int[] getIndexedSpriteHeights();

    @Import(value="indexedSpriteHeights")
    public void setIndexedSpriteHeights(int[] var1);

    @Import(value="spritePixels")
    public byte[][] getSpritePixels();

    @Import(value="spritePixels")
    public void setSpritePixels(byte[][] var1);

    @Import(value="indexedSpritePalette")
    public int[] getIndexedSpritePalette();

    @Import(value="indexedSpritePalette")
    public void setIndexSpritePalette(int[] var1);

    @Import(value="itemModelCache")
    public NodeCache getItemModelCache();

    @Import(value="itemSpriteCache")
    public NodeCache getItemSpriteCache();

    @Import(value="archiveSprites")
    public RSIndexDataBase getIndexSprites();

    @Import(value="archiveClientScripts")
    public RSIndexDataBase getIndexScripts();

    @Import(value="archiveConfig")
    public RSIndexDataBase getIndexConfig();

    @Import(value="widgetFlags")
    public RSHashTable getWidgetFlags();

    @Import(value="componentTable")
    public RSHashTable getComponentTable();

    @Import(value="grandExchangeOffers")
    public RSGrandExchangeOffer[] getGrandExchangeOffers();

    @Import(value="isMenuOpen")
    public boolean isMenuOpen();

    @Import(value="gameCycle")
    public int getGameCycle();

    @Import(value="chatLineMap")
    public Map getChatLineMap();

    @Import(value="messages")
    public RSIterableHashTable getMessages();

    @Import(value="revision")
    public int getRevision();

    @Import(value="mapRegions")
    public int[] getMapRegions();

    @Import(value="instanceTemplateChunks")
    public int[][][] getInstanceTemplateChunks();

    @Import(value="xteaKeys")
    public int[][] getXteaKeys();

    @Import(value="gameDrawingMode")
    public int getGameDrawingMode();

    @Import(value="gameDrawingMode")
    public void setGameDrawingMode(int var1);

    @Import(value="cycleCntr")
    public int getCycleCntr();

    @Import(value="chatCycle")
    public void setChatCycle(int var1);

    @Import(value="mapElementConfigs")
    public RSMapElementConfig[] getMapElementConfigs();

    @Import(value="mapscene")
    public RSIndexedSprite[] getMapScene();

    @Import(value="mapIcons")
    public RSSpritePixels[] getMapIcons();

    @Import(value="mapDots")
    public RSSpritePixels[] getMapDots();

    @Import(value="modIcons")
    public RSIndexedSprite[] getModIcons();

    @Import(value="modIcons")
    public void setRSModIcons(RSIndexedSprite[] var1);

    @Construct
    public RSIndexedSprite createIndexedSprite();

    @Construct
    public RSSpritePixels createSpritePixels(int[] var1, int var2, int var3);

    @Construct
    public RSProjectile rs$createProjectile(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11);

    @Import(value="projectiles")
    public Deque<Projectile> getProjectiles();

    @Import(value="graphicsObjectDeque")
    public Deque<GraphicsObject> getGraphicsObjects();

    @Import(value="destinationX")
    public int getDestinationX();

    @Import(value="destinationY")
    public int getDestinationY();

    @Import(value="audioEffects")
    public RSSoundEffect[] getAudioEffects();

    @Import(value="queuedSoundEffectIDs")
    public int[] getQueuedSoundEffectIDs();

    @Import(value="soundLocations")
    public int[] getSoundLocations();

    @Import(value="queuedSoundEffectLoops")
    public int[] getQueuedSoundEffectLoops();

    @Import(value="queuedSoundEffectDelays")
    public int[] getQueuedSoundEffectDelays();

    @Import(value="queuedSoundEffectCount")
    public int getQueuedSoundEffectCount();

    @Import(value="queuedSoundEffectCount")
    public void setQueuedSoundEffectCount(int var1);

    @Import(value="rasterProvider")
    public RSBufferProvider getBufferProvider();

    @Import(value="mouseIdleTicks")
    public int getMouseIdleTicks();

    @Import(value="mouseLastPressedTimeMillis")
    public long getMouseLastPressedMillis();

    @Import(value="keyboardIdleTicks")
    public int getKeyboardIdleTicks();

    @Import(value="lowMemory")
    public void setLowMemory(boolean var1);

    @Import(value="sceneLowMemory")
    public void setSceneLowMemory(boolean var1);

    @Import(value="audioHighMemory")
    public void setAudioHighMemory(boolean var1);

    @Import(value="objectCompositionLowDetail")
    public void setObjectCompositionLowDetail(boolean var1);

    @Import(value="intStackSize")
    public int getIntStackSize();

    @Import(value="intStackSize")
    public void setIntStackSize(int var1);

    @Import(value="intStack")
    public int[] getIntStack();

    @Import(value="scriptStringStackSize")
    public int getStringStackSize();

    @Import(value="scriptStringStackSize")
    public void setStringStackSize(int var1);

    @Import(value="scriptStringStack")
    public String[] getStringStack();

    @Import(value="primaryActiveComponent")
    public Widget getScriptActiveWidget();

    @Import(value="secondaryActiveComponent")
    public Widget getScriptDotWidget();

    @Import(value="friendManager")
    public RSFriendManager getFriendManager();

    @Import(value="friendsChatManager")
    public RSFriendsChatManager getFriendsChatManager();

    @Import(value="loginType")
    public RSJagexLoginType getLoginType();

    @Construct
    public RSName createName(String var1, RSJagexLoginType var2);

    @Import(value="getVarbit")
    public int getRSVarbit(int var1);

    @Import(value="varbits")
    public RSNodeCache getVarbitCache();

    @Import(value="preferences")
    public RSPreferences getPreferences();

    @Import(value="cameraPitchTarget")
    public int getCameraPitchTarget();

    @Import(value="cameraPitchTarget")
    public void setCameraPitchTarget(int var1);

    @Import(value="pitchSin")
    public void setPitchSin(int var1);

    @Import(value="pitchCos")
    public void setPitchCos(int var1);

    @Import(value="yawSin")
    public void setYawSin(int var1);

    @Import(value="yawCos")
    public void setYawCos(int var1);

    @Import(value="Rasterizer3D_zoom")
    public int get3dZoom();

    @Import(value="Rasterizer3D_zoom")
    public void set3dZoom(int var1);

    @Import(value="Rasterizer3D_clipMidX2")
    public int getRasterizer3D_clipMidX2();

    @Import(value="Rasterizer3D_clipNegativeMidX")
    public int getRasterizer3D_clipNegativeMidX();

    @Import(value="Rasterizer3D_clipNegativeMidY")
    public int getRasterizer3D_clipNegativeMidY();

    @Import(value="Rasterizer3D_clipMidY2")
    public int getRasterizer3D_clipMidY2();

    @Import(value="centerX")
    public int getCenterX();

    @Import(value="centerY")
    public int getCenterY();

    @Import(value="renderOverview")
    public RSRenderOverview getRenderOverview();

    @Import(value="changeWorld")
    public void changeWorld(World var1);

    @Construct
    public RSWorld createWorld();

    @Import(value="animOffsetX")
    public void setAnimOffsetX(int var1);

    @Import(value="animOffsetY")
    public void setAnimOffsetY(int var1);

    @Import(value="animOffsetZ")
    public void setAnimOffsetZ(int var1);

    @Import(value="getFrames")
    public RSFrames getFrames(int var1);

    @Import(value="minimapSprite")
    public RSSpritePixels getMinimapSprite();

    @Import(value="minimapSprite")
    public void setMinimapSprite(SpritePixels var1);

    @Import(value="drawObject")
    public void drawObject(int var1, int var2, int var3, int var4, int var5);

    @Construct
    public RSScriptEvent rs$createScriptEvent();

    @Import(value="runScript")
    public void runScript(RSScriptEvent var1, int var2, int var3);

    @Import(value="hintArrowTargetType")
    public void setHintArrowTargetType(int var1);

    @Import(value="hintArrowTargetType")
    public int getHintArrowTargetType();

    @Import(value="hintArrowX")
    public void setHintArrowX(int var1);

    @Import(value="hintArrowX")
    public int getHintArrowX();

    @Import(value="hintArrowY")
    public void setHintArrowY(int var1);

    @Import(value="hintArrowY")
    public int getHintArrowY();

    @Import(value="hintArrowOffsetX")
    public void setHintArrowOffsetX(int var1);

    @Import(value="hintArrowOffsetY")
    public void setHintArrowOffsetY(int var1);

    @Import(value="hintArrowNpcTargetIdx")
    public void setHintArrowNpcTargetIdx(int var1);

    @Import(value="hintArrowNpcTargetIdx")
    public int getHintArrowNpcTargetIdx();

    @Import(value="hintArrowPlayerTargetIdx")
    public void setHintArrowPlayerTargetIdx(int var1);

    @Import(value="hintArrowPlayerTargetIdx")
    public int getHintArrowPlayerTargetIdx();

    @Import(value="hintArrowHeight")
    public void setHintArrowHeight(int var1);

    @Import(value="isDynamicRegion")
    public boolean isInInstancedRegion();

    @Import(value="itemPressedDuration")
    public int getItemPressedDuration();

    @Import(value="itemPressedDuration")
    public void setItemPressedDuration(int var1);

    @Import(value="flags")
    public int getFlags();

    @Import(value="compass")
    public void setCompass(SpritePixels var1);

    @Import(value="widgetSpriteCache")
    public RSNodeCache getWidgetSpriteCache();

    @Import(value="items")
    public RSNodeCache getItemCompositionCache();

    @Import(value="objects")
    public NodeCache getObjectCompositionCache();

    @Import(value="oculusOrbState")
    public int getOculusOrbState();

    @Import(value="oculusOrbState")
    public void setOculusOrbState(int var1);

    @Import(value="oculusOrbNormalSpeed")
    public void setOculusOrbNormalSpeed(int var1);

    @Import(value="lookingAtX")
    public int getOculusOrbFocalPointX();

    @Import(value="lookingAtY")
    public int getOculusOrbFocalPointY();

    public RSTileItem getLastItemDespawn();

    public void setLastItemDespawn(RSTileItem var1);

    @Construct
    public RSWidget createWidget();

    @Import(value="revalidateWidget")
    public void revalidateWidget(Widget var1);

    @Import(value="revalidateWidgetScroll")
    public void revalidateWidgetScroll(Widget[] var1, Widget var2, boolean var3);

    @Import(value="menuAction")
    public void menuAction(int var1, int var2, int var3, int var4, int var5, String var6, String var7, int var8, int var9);

    @Import(value="Viewport_entityCountAtMouse")
    public int getEntitiesAtMouseCount();

    @Import(value="Viewport_entityCountAtMouse")
    public void setEntitiesAtMouseCount(int var1);

    @Import(value="Viewport_entitiesAtMouse")
    public long[] getEntitiesAtMouse();

    @Import(value="Viewport_mouseX")
    public int getViewportMouseX();

    @Import(value="Viewport_mouseY")
    public int getViewportMouseY();

    @Import(value="textureProvider")
    public RSTextureProvider getTextureProvider();

    @Import(value="occupiedTilesTick")
    public int[][] getOccupiedTilesTick();

    @Import(value="cycle")
    public int getCycle();

    @Import(value="cycle")
    public void setCycle(int var1);

    @Import(value="visibilityMaps")
    public boolean[][][][] getVisibilityMaps();

    @Import(value="renderArea")
    public void setRenderArea(boolean[][] var1);

    @Import(value="cameraX2")
    public void setCameraX2(int var1);

    @Import(value="cameraY2")
    public void setCameraY2(int var1);

    @Import(value="cameraZ2")
    public void setCameraZ2(int var1);

    @Import(value="screenCenterX")
    public void setScreenCenterX(int var1);

    @Import(value="screenCenterZ")
    public void setScreenCenterZ(int var1);

    @Import(value="Scene_plane")
    public void setScenePlane(int var1);

    @Import(value="minTileX")
    public void setMinTileX(int var1);

    @Import(value="minTileZ")
    public void setMinTileZ(int var1);

    @Import(value="maxTileX")
    public void setMaxTileX(int var1);

    @Import(value="maxTileZ")
    public void setMaxTileZ(int var1);

    @Import(value="tileUpdateCount")
    public int getTileUpdateCount();

    @Import(value="tileUpdateCount")
    public void setTileUpdateCount(int var1);

    @Import(value="Viewport_containsMouse")
    public boolean getViewportContainsMouse();

    @Import(value="graphicsPixels")
    public int[] getGraphicsPixels();

    @Import(value="graphicsPixelsWidth")
    public int getGraphicsPixelsWidth();

    @Import(value="graphicsPixelsHeight")
    public int getGraphicsPixelsHeight();

    @Import(value="fillRectangle")
    public void RasterizerFillRectangle(int var1, int var2, int var3, int var4, int var5);

    @Import(value="startX")
    public int getStartX();

    @Import(value="startY")
    public int getStartY();

    @Import(value="endX")
    public int getEndX();

    @Import(value="endY")
    public int getEndY();

    @Import(value="if1DraggedWidget")
    public RSWidget getIf1DraggedWidget();

    @Import(value="if1DraggedItemIndex")
    public int getIf1DraggedItemIndex();

    @Import(value="if1DraggedItemX")
    public int getIf1DraggedItemX();

    @Import(value="if1DraggedItemY")
    public int getIf1DraggedItemY();

    @Import(value="spellSelected")
    public boolean getSpellSelected();

    @Import(value="spellSelected")
    public void setSpellSelected(boolean var1);

    @Import(value="selectedComponent")
    public int getSelectedComponent();

    @Import(value="selectedComponentSub")
    public int getSelectedComponentSub();

    @Import(value="itemSelectionState")
    public int getSelectedItem();

    @Import(value="selectedItemIndex")
    public int getSelectedItemIndex();

    @Import(value="getEnumType")
    public RSEnumComposition getEnumType(int var1);

    @Import(value="menuX")
    public int getMenuX();

    @Import(value="menuY")
    public int getMenuY();

    @Import(value="menuHeight")
    public int getMenuHeight();

    @Import(value="menuWidth")
    public int getMenuWidth();

    @Import(value="fontBold12")
    public RSFont getFontBold12();

    @Import(value="drawHorizontalLine")
    public void RasterizerDrawHorizontalLine(int var1, int var2, int var3, int var4);

    @Import(value="drawVerticalLine")
    public void RasterizerDrawVerticalLine(int var1, int var2, int var3, int var4);

    @Import(value="drawGradient")
    public void RasterizerDrawGradient(int var1, int var2, int var3, int var4, int var5, int var6);

    @Import(value="fillRectangleAlpha")
    public void RasterizerFillRectangleAlpha(int var1, int var2, int var3, int var4, int var5, int var6);

    @Import(value="drawRectangle")
    public void RasterizerDrawRectangle(int var1, int var2, int var3, int var4, int var5);

    @Import(value="drawCircle")
    public void RasterizerDrawCircle(int var1, int var2, int var3, int var4);

    @Import(value="headbarConfigCache")
    public RSNodeCache getHealthBarCache();

    @Import(value="headbarSpriteCache")
    public RSNodeCache getHealthBarSpriteCache();

    @Import(value="getTrack")
    public RSSoundEffect getTrack(RSIndexData var1, int var2, int var3);

    @Import(value="createSoundEffectAudioTaskNode")
    public RSAudioTaskNode createSoundEffectAudioTaskNode(RSRawAudioNode var1, int var2, int var3);

    @Import(value="soundEffectAudioQueue")
    public RSAudioTaskNodeQueue getSoundEffectAudioQueue();

    @Import(value="soundEffectResampler")
    public RSResampler getSoundEffectResampler();

    @Import(value="playingJingle")
    public boolean isPlayingJingle();

    @Import(value="musicType")
    public void setMusicType(int var1);

    @Import(value="musicIndex")
    public void setMusicIndex(IndexDataBase var1);

    @Import(value="musicCurrentTrackId")
    public int getMusicCurrentTrackId();

    @Import(value="musicCurrentTrackId")
    public void setMusicCurrentTrackId(int var1);

    @Import(value="musicFileId")
    public void setMusicFileId(int var1);

    @Import(value="musicInt")
    public void setMusicInt(int var1);

    @Import(value="musicVolume2")
    public void setRSMusicVolume2(int var1);

    @Import(value="musicPlayer")
    @Nullable
    public RSMusicPlayer getMusicPlayer();

    @Import(value="currentSong")
    public int getCurrentSong();

    @Import(value="viewportWalking")
    public void setViewportWalking(boolean var1);

    @Import(value="crossSprites")
    public RSSpritePixels[] getCrossSprites();

    @Import(value="recentInvTransmitList")
    public int[] getRecentInvTransmitList();

    @Import(value="itemCount")
    public int getItemCount();

    @Import(value="scriptCache")
    public RSNodeCache getScriptCache();

    @Import(value="archiveMusic")
    public IndexDataBase getIndexTrack1();

    @Import(value="ocFindResultCount")
    public void setGeSearchResultCount(int var1);

    @Import(value="ocFindResults")
    public void setGeSearchResultIds(short[] var1);

    @Import(value="ocFindResultPointer")
    public void setGeSearchResultIndex(int var1);

    @Import(value="killTime")
    public void setKillTime(long var1);

    @Import(value="mousecam")
    public boolean getMouseCam();

    @Import(value="cameraAccelerationX")
    public int getCameraAccelerationX();

    @Import(value="cameraAccelerationX")
    public void setCameraAccelerationX(int var1);

    @Import(value="cameraAccelerationY")
    public int getCameraAccelerationY();

    @Import(value="cameraAccelerationY")
    public void setCameraAccelerationY(int var1);

    @Import(value="loginBackground")
    public void setRSLoginBackground(SpritePixels var1);

    @Import(value="loginBackgroundMirrored")
    public void setRSLoginBackgroundMirrored(SpritePixels var1);

    @Import(value="loginScreenDrawn")
    public void setLoginScreenDrawn(boolean var1);

    @Import(value="keysPressed")
    public boolean[] getKeysPressed();

    @Construct
    public RSIterableHashTable createIterableHashTable(int var1);

    @Construct
    public RSIntegerNode createIntegerNode(int var1);

    @Construct
    public RSObjectNode createObjectNode(Object var1);

    public void runScript(RSScriptEvent var1);

    @Import(value="crossWorldMessageIds")
    public long[] getCrossWorldMessageIds();

    @Import(value="crossWorldMessageIdsIndex")
    public int getCrossWorldMessageIdsIndex();

    public ClanRank getClanRank(int var1);

    @Import(value="affinedClanSettings")
    public RSClanSettings[] getAffinedClanSettings();

    @Import(value="affinedClanChannels")
    public RSClanChannel[] getAffinedClanChannels();

    @Import(value="listenedClanSettings")
    @Nullable
    public ClanSettings getGuestClanSettings();

    @Import(value="listenedClanChannel")
    @Nullable
    public ClanChannel getGuestClanChannel();

    @Import(value="hookRequestsHigh")
    public RSDeque<RSScriptEvent> getHookRequestsHigh();

    @Import(value="hookRequestsNormal")
    public RSDeque<RSScriptEvent> getHookRequestsNormal();

    @Import(value="hookRequestsLow")
    public RSDeque<RSScriptEvent> getHookRequestsLow();

    @Construct
    public RSModelData createModelData(RSModelData[] var1, int var2);

    @Import(value="getSeqType")
    public Animation loadAnimation(int var1);

    @Import(value="randomHue")
    public int getRandomHue();

    @Import(value="Tiles_underlays")
    public byte[][][] getTileUnderlays();

    @Import(value="Tiles_overlays")
    public byte[][][] getTileOverlays();

    @Import(value="Tiles_shapes")
    public byte[][][] getTileShapes();

    @Import(value="ambientSounds")
    public RSDeque getAmbientSoundEffects();

    @Import(value="isCameraLocked")
    public boolean isCameraLocked();

    public void posToCameraAngle(int var1, int var2);

    @Import(value="loadModel")
    public RSModelData loadModel(RSIndexDataBase var1, int var2, int var3);

    @Construct
    public RSNodeCache createNodeCache(int var1);

    @Construct
    public RSChatCrownType createChatCrownType(int var1, int var2, boolean var3, boolean var4, boolean var5);

    @Construct
    public RSClientProt createClientProt(int var1, int var2);

    @Import(value="createMessage")
    public RSPacketNode createMessage(RSClientProt var1, RSIsaac var2);

    @Import(value="host")
    public String getHost();

    @Import(value="host")
    public void setHost(String var1);

    @Import(value="myWorldPort")
    public int getPort();

    @Import(value="myWorldPort")
    public void setPort(int var1);

    @Import(value="loginMessage1")
    public void setLoginMessage1(String var1);

    @Import(value="loginMessage2")
    public void setLoginMessage2(String var1);

    @Import(value="loginMessage3")
    public void setLoginMessage3(String var1);

    @Construct
    public RSBuffer createBuffer(byte[] var1);

    @Import(value="validInterfaces")
    public boolean[] getValidInterfaces();

    @Import(value="decodeScript")
    public RSScript decodeScript(byte[] var1);

    @Import(value="loginBackgroundMirrored")
    public SpritePixels getLoginBackgroundMirrored();

    @Import(value="tradingPostResults")
    public RSTradingPostResults getTradingPostResults();

    @Import(value="tradingPostTimeOffset")
    public long getTradingPostTimeOffset();

    @Import(value="currentTimeMillisSafe")
    public long currentTimeMillisSafe();

    @Import(value="loadingStage")
    public int getLoadingStage();

    @Import(value="loadingStage")
    public void setLoadingStage(int var1);

    @Import(value="loadingBarPercentage")
    public int getLoadingBarPercentage();

    @Import(value="loadingBarPercentage")
    public void setLoadingBarPercentage(int var1);

    @Import(value="loadingText")
    public String getLoadingText();

    @Import(value="loadingText")
    public void setLoadingText(String var1);

    @Import(value="setupLoginScreen")
    public void setupLoginScreen(RSIndexDataBase var1, RSIndexDataBase var2, boolean var3, int var4);

    public boolean getDisableMinimenu();

    public void setDisableMinimenu(boolean var1);
}

