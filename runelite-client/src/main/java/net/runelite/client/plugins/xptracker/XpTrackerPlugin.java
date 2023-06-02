/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.MoreObjects
 *  com.google.common.collect.ImmutableList
 *  com.google.inject.Binder
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.MenuAction
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Skill
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.WorldType
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.StatChanged
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.xptracker;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.WorldType;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.StatChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.NPCManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpActionType;
import net.runelite.client.plugins.xptracker.XpClient;
import net.runelite.client.plugins.xptracker.XpInfoBoxOverlay;
import net.runelite.client.plugins.xptracker.XpPanel;
import net.runelite.client.plugins.xptracker.XpPauseState;
import net.runelite.client.plugins.xptracker.XpSnapshotSingle;
import net.runelite.client.plugins.xptracker.XpState;
import net.runelite.client.plugins.xptracker.XpStateSingle;
import net.runelite.client.plugins.xptracker.XpTrackerConfig;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.plugins.xptracker.XpTrackerServiceImpl;
import net.runelite.client.plugins.xptracker.XpUpdateResult;
import net.runelite.client.plugins.xptracker.XpWorldType;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="XP Tracker", description="Enable the XP Tracker panel", tags={"experience", "levels", "panel"})
public class XpTrackerPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(XpTrackerPlugin.class);
    private static final int XP_THRESHOLD = 10000;
    private static final String MENUOP_ADD_CANVAS_TRACKER = "Add to canvas";
    private static final String MENUOP_REMOVE_CANVAS_TRACKER = "Remove from canvas";
    static final List<Skill> COMBAT = ImmutableList.of((Object)Skill.ATTACK, (Object)Skill.STRENGTH, (Object)Skill.DEFENCE, (Object)Skill.RANGED, (Object)Skill.HITPOINTS, (Object)Skill.MAGIC);
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private SkillIconManager skillIconManager;
    @Inject
    private XpTrackerConfig xpTrackerConfig;
    @Inject
    private NPCManager npcManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private XpClient xpClient;
    @Inject
    private XpState xpState;
    private NavigationButton navButton;
    @VisibleForTesting
    private XpPanel xpPanel;
    private XpWorldType lastWorldType;
    private long lastAccount;
    private long lastTickMillis = 0L;
    private boolean fetchXp;
    private long lastXp = 0L;
    private boolean initializeTracker;
    private final XpPauseState xpPauseState = new XpPauseState();

    @Provides
    XpTrackerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(XpTrackerConfig.class);
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(XpTrackerService.class).to(XpTrackerServiceImpl.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.xpPanel = new XpPanel(this, this.xpTrackerConfig, this.client, this.skillIconManager);
        BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "/skill_icons/overall.png");
        this.navButton = NavigationButton.builder().tooltip("XP Tracker").icon(icon).priority(2).panel(this.xpPanel).build();
        this.clientToolbar.addNavigation(this.navButton);
        this.fetchXp = true;
        this.initializeTracker = true;
        this.lastAccount = -1L;
        this.clientThread.invokeLater(() -> {
            if (this.client.getGameState() == GameState.LOGGED_IN) {
                this.lastAccount = this.client.getAccountHash();
                this.lastWorldType = this.worldSetToType(this.client.getWorldType());
            }
        });
    }

    @Override
    protected void shutDown() throws Exception {
        this.overlayManager.removeIf(e -> e instanceof XpInfoBoxOverlay);
        this.xpState.reset();
        this.clientToolbar.removeNavigation(this.navButton);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        GameState state = event.getGameState();
        if (state == GameState.LOGGED_IN) {
            XpWorldType type = this.worldSetToType(this.client.getWorldType());
            if (this.client.getAccountHash() != this.lastAccount || this.lastWorldType != type) {
                log.debug("World change: {} -> {}, {} -> {}", new Object[]{this.lastAccount, this.client.getAccountHash(), MoreObjects.firstNonNull((Object)((Object)this.lastWorldType), (Object)"<unknown>"), MoreObjects.firstNonNull((Object)((Object)type), (Object)"<unknown>")});
                this.lastAccount = this.client.getAccountHash();
                this.fetchXp = true;
                this.lastWorldType = type;
                this.resetState();
                assert (this.initializeTracker);
            }
        } else if (state == GameState.LOGGING_IN || state == GameState.HOPPING) {
            this.initializeTracker = true;
        } else if (state == GameState.LOGIN_SCREEN) {
            Player local = this.client.getLocalPlayer();
            if (local == null) {
                return;
            }
            String username = local.getName();
            if (username == null) {
                return;
            }
            long totalXp = this.client.getOverallExperience();
            if (Math.abs(totalXp - this.lastXp) > 10000L) {
                this.lastXp = totalXp;
            }
        }
    }

    private XpWorldType worldSetToType(EnumSet<WorldType> types) {
        XpWorldType xpType = XpWorldType.NORMAL;
        for (WorldType type : types) {
            XpWorldType t = XpWorldType.of(type);
            if (t == XpWorldType.NORMAL) continue;
            xpType = t;
        }
        return xpType;
    }

    void addOverlay(Skill skill) {
        this.removeOverlay(skill);
        this.overlayManager.add(new XpInfoBoxOverlay(this, this.xpTrackerConfig, skill, this.skillIconManager.getSkillImage(skill)));
    }

    void removeOverlay(Skill skill) {
        this.overlayManager.removeIf(e -> e instanceof XpInfoBoxOverlay && ((XpInfoBoxOverlay)e).getSkill() == skill);
    }

    boolean hasOverlay(Skill skill) {
        return this.overlayManager.anyMatch(o -> o instanceof XpInfoBoxOverlay && ((XpInfoBoxOverlay)o).getSkill() == skill);
    }

    void resetAndInitState() {
        this.resetState();
        for (Skill skill : Skill.values()) {
            long currentXp = skill == Skill.OVERALL ? this.client.getOverallExperience() : (long)this.client.getSkillExperience(skill);
            this.xpState.initializeSkill(skill, currentXp);
            this.removeOverlay(skill);
        }
    }

    private void resetState() {
        this.xpState.reset();
        this.xpPanel.resetAllInfoBoxes();
        this.xpPanel.updateTotal(new XpSnapshotSingle.XpSnapshotSingleBuilder().build());
        this.overlayManager.removeIf(e -> e instanceof XpInfoBoxOverlay);
    }

    void resetSkillState(Skill skill) {
        int currentXp = this.client.getSkillExperience(skill);
        this.xpState.resetSkill(skill, currentXp);
        this.xpPanel.resetSkill(skill);
        this.removeOverlay(skill);
    }

    void resetOtherSkillState(Skill skill) {
        for (Skill s : Skill.values()) {
            if (skill == s || s == Skill.OVERALL) continue;
            this.resetSkillState(s);
        }
    }

    void resetSkillPerHourState(Skill skill) {
        this.xpState.resetSkillPerHour(skill);
    }

    void resetAllSkillsPerHourState() {
        for (Skill skill : Skill.values()) {
            this.resetSkillPerHourState(skill);
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged statChanged) {
        XpUpdateResult updateResult;
        int endGoalXp;
        if (this.client.getVar(15029) == 1) {
            return;
        }
        Skill skill = statChanged.getSkill();
        int currentXp = statChanged.getXp();
        int currentLevel = statChanged.getLevel();
        VarPlayer startGoal = XpTrackerPlugin.startGoalVarpForSkill(skill);
        VarPlayer endGoal = XpTrackerPlugin.endGoalVarpForSkill(skill);
        int startGoalXp = startGoal != null ? this.client.getVarpValue(startGoal) : -1;
        int n = endGoalXp = endGoal != null ? this.client.getVarpValue(endGoal) : -1;
        if (this.initializeTracker) {
            return;
        }
        if (this.xpTrackerConfig.hideMaxed() && currentLevel >= 99) {
            return;
        }
        XpStateSingle state = this.xpState.getSkill(skill);
        state.setActionType(XpActionType.EXPERIENCE);
        Actor interacting = this.client.getLocalPlayer().getInteracting();
        if (interacting instanceof NPC && COMBAT.contains((Object)skill)) {
            int xpModifier = this.worldSetToType(this.client.getWorldType()).modifier(this.client);
            NPC npc = (NPC)interacting;
            this.xpState.updateNpcExperience(skill, npc, this.npcManager.getHealth(npc.getId()), xpModifier);
        }
        this.xpPanel.updateSkillExperience((updateResult = this.xpState.updateSkill(skill, currentXp, startGoalXp, endGoalXp)) == XpUpdateResult.UPDATED, this.xpPauseState.isPaused(skill), skill, this.xpState.getSkillSnapshot(skill));
        this.xpState.updateSkill(Skill.OVERALL, this.client.getOverallExperience(), -1, -1);
        this.xpPanel.updateTotal(this.xpState.getTotalSnapshot());
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        NPC npc = event.getNpc();
        if (!npc.isDead()) {
            return;
        }
        for (Skill skill : COMBAT) {
            XpUpdateResult updateResult = this.xpState.updateNpcKills(skill, npc, this.npcManager.getHealth(npc.getId()));
            boolean updated = XpUpdateResult.UPDATED.equals((Object)updateResult);
            this.xpPanel.updateSkillExperience(updated, this.xpPauseState.isPaused(skill), skill, this.xpState.getSkillSnapshot(skill));
        }
        this.xpPanel.updateTotal(this.xpState.getTotalSnapshot());
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (this.initializeTracker) {
            this.initializeTracker = false;
            for (Skill skill : Skill.values()) {
                if (skill == Skill.OVERALL || !this.xpState.isInitialized(skill)) continue;
                XpStateSingle skillState = this.xpState.getSkill(skill);
                int currentXp = this.client.getSkillExperience(skill);
                if (skillState.getCurrentXp() == (long)currentXp) continue;
                if ((long)currentXp < skillState.getCurrentXp()) {
                    log.debug("Xp is going backwards! {} {} -> {}", new Object[]{skill, skillState.getCurrentXp(), currentXp});
                    this.resetState();
                    break;
                }
                log.debug("Skill xp for {} changed when offline: {} -> {}", new Object[]{skill, skillState.getCurrentXp(), currentXp});
                long diff = (long)currentXp - skillState.getCurrentXp();
                skillState.setStartXp(skillState.getStartXp() + diff);
            }
            for (Skill skill : Skill.values()) {
                if (skill == Skill.OVERALL || this.xpState.isInitialized(skill)) continue;
                int currentXp = this.client.getSkillExperience(skill);
                XpUpdateResult xpUpdateResult = this.xpState.updateSkill(skill, currentXp, -1, -1);
                assert (xpUpdateResult == XpUpdateResult.INITIALIZED);
            }
            if (!this.xpState.isInitialized(Skill.OVERALL)) {
                long overallXp = this.client.getOverallExperience();
                log.debug("Initializing XP tracker with {} overall exp", (Object)overallXp);
                this.xpState.initializeSkill(Skill.OVERALL, overallXp);
            }
        }
        if (this.fetchXp) {
            this.lastXp = this.client.getOverallExperience();
            this.fetchXp = false;
        }
        this.rebuildSkills();
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        int widgetID = event.getActionParam1();
        if (WidgetInfo.TO_GROUP((int)widgetID) != 320 || !event.getOption().startsWith("View") || !this.xpTrackerConfig.skillTabOverlayMenuOptions()) {
            return;
        }
        String skillText = event.getOption().split(" ")[1];
        Skill skill = Skill.valueOf((String)Text.removeTags(skillText).toUpperCase());
        this.client.createMenuEntry(-1).setTarget(skillText).setOption(this.hasOverlay(skill) ? MENUOP_REMOVE_CANVAS_TRACKER : MENUOP_ADD_CANVAS_TRACKER).setType(MenuAction.RUNELITE).onClick(e -> {
            if (this.hasOverlay(skill)) {
                this.removeOverlay(skill);
            } else {
                this.addOverlay(skill);
            }
        });
    }

    XpStateSingle getSkillState(Skill skill) {
        return this.xpState.getSkill(skill);
    }

    XpSnapshotSingle getSkillSnapshot(Skill skill) {
        return this.xpState.getSkillSnapshot(skill);
    }

    private static VarPlayer startGoalVarpForSkill(Skill skill) {
        switch (skill) {
            case ATTACK: {
                return VarPlayer.ATTACK_GOAL_START;
            }
            case MINING: {
                return VarPlayer.MINING_GOAL_START;
            }
            case WOODCUTTING: {
                return VarPlayer.WOODCUTTING_GOAL_START;
            }
            case DEFENCE: {
                return VarPlayer.DEFENCE_GOAL_START;
            }
            case MAGIC: {
                return VarPlayer.MAGIC_GOAL_START;
            }
            case RANGED: {
                return VarPlayer.RANGED_GOAL_START;
            }
            case HITPOINTS: {
                return VarPlayer.HITPOINTS_GOAL_START;
            }
            case AGILITY: {
                return VarPlayer.AGILITY_GOAL_START;
            }
            case STRENGTH: {
                return VarPlayer.STRENGTH_GOAL_START;
            }
            case PRAYER: {
                return VarPlayer.PRAYER_GOAL_START;
            }
            case SLAYER: {
                return VarPlayer.SLAYER_GOAL_START;
            }
            case FISHING: {
                return VarPlayer.FISHING_GOAL_START;
            }
            case RUNECRAFT: {
                return VarPlayer.RUNECRAFT_GOAL_START;
            }
            case HERBLORE: {
                return VarPlayer.HERBLORE_GOAL_START;
            }
            case FIREMAKING: {
                return VarPlayer.FIREMAKING_GOAL_START;
            }
            case CONSTRUCTION: {
                return VarPlayer.CONSTRUCTION_GOAL_START;
            }
            case HUNTER: {
                return VarPlayer.HUNTER_GOAL_START;
            }
            case COOKING: {
                return VarPlayer.COOKING_GOAL_START;
            }
            case FARMING: {
                return VarPlayer.FARMING_GOAL_START;
            }
            case CRAFTING: {
                return VarPlayer.CRAFTING_GOAL_START;
            }
            case SMITHING: {
                return VarPlayer.SMITHING_GOAL_START;
            }
            case THIEVING: {
                return VarPlayer.THIEVING_GOAL_START;
            }
            case FLETCHING: {
                return VarPlayer.FLETCHING_GOAL_START;
            }
        }
        return null;
    }

    private static VarPlayer endGoalVarpForSkill(Skill skill) {
        switch (skill) {
            case ATTACK: {
                return VarPlayer.ATTACK_GOAL_END;
            }
            case MINING: {
                return VarPlayer.MINING_GOAL_END;
            }
            case WOODCUTTING: {
                return VarPlayer.WOODCUTTING_GOAL_END;
            }
            case DEFENCE: {
                return VarPlayer.DEFENCE_GOAL_END;
            }
            case MAGIC: {
                return VarPlayer.MAGIC_GOAL_END;
            }
            case RANGED: {
                return VarPlayer.RANGED_GOAL_END;
            }
            case HITPOINTS: {
                return VarPlayer.HITPOINTS_GOAL_END;
            }
            case AGILITY: {
                return VarPlayer.AGILITY_GOAL_END;
            }
            case STRENGTH: {
                return VarPlayer.STRENGTH_GOAL_END;
            }
            case PRAYER: {
                return VarPlayer.PRAYER_GOAL_END;
            }
            case SLAYER: {
                return VarPlayer.SLAYER_GOAL_END;
            }
            case FISHING: {
                return VarPlayer.FISHING_GOAL_END;
            }
            case RUNECRAFT: {
                return VarPlayer.RUNECRAFT_GOAL_END;
            }
            case HERBLORE: {
                return VarPlayer.HERBLORE_GOAL_END;
            }
            case FIREMAKING: {
                return VarPlayer.FIREMAKING_GOAL_END;
            }
            case CONSTRUCTION: {
                return VarPlayer.CONSTRUCTION_GOAL_END;
            }
            case HUNTER: {
                return VarPlayer.HUNTER_GOAL_END;
            }
            case COOKING: {
                return VarPlayer.COOKING_GOAL_END;
            }
            case FARMING: {
                return VarPlayer.FARMING_GOAL_END;
            }
            case CRAFTING: {
                return VarPlayer.CRAFTING_GOAL_END;
            }
            case SMITHING: {
                return VarPlayer.SMITHING_GOAL_END;
            }
            case THIEVING: {
                return VarPlayer.THIEVING_GOAL_END;
            }
            case FLETCHING: {
                return VarPlayer.FLETCHING_GOAL_END;
            }
        }
        return null;
    }

    @Schedule(period=1L, unit=ChronoUnit.SECONDS)
    public void tickSkillTimes() {
        boolean loggedIn;
        for (Skill skill : Skill.values()) {
            long skillExperience = skill == Skill.OVERALL ? this.client.getOverallExperience() : (long)this.client.getSkillExperience(skill);
            this.xpPauseState.tickXp(skill, skillExperience, this.xpTrackerConfig.pauseSkillAfter());
        }
        switch (this.client.getGameState()) {
            case LOGIN_SCREEN: 
            case LOGGING_IN: 
            case LOGIN_SCREEN_AUTHENTICATOR: {
                loggedIn = false;
                break;
            }
            default: {
                loggedIn = true;
            }
        }
        this.xpPauseState.tickLogout(this.xpTrackerConfig.pauseOnLogout(), loggedIn);
        if (this.lastTickMillis == 0L) {
            this.lastTickMillis = System.currentTimeMillis();
            return;
        }
        long nowMillis = System.currentTimeMillis();
        long tickDelta = nowMillis - this.lastTickMillis;
        this.lastTickMillis = nowMillis;
        for (Skill skill : Skill.values()) {
            if (this.xpPauseState.isPaused(skill)) continue;
            this.xpState.tick(skill, tickDelta);
        }
        this.rebuildSkills();
    }

    private void rebuildSkills() {
        for (Skill skill : Skill.values()) {
            this.xpPanel.updateSkillExperience(false, this.xpPauseState.isPaused(skill), skill, this.xpState.getSkillSnapshot(skill));
        }
        this.xpPanel.updateTotal(this.xpState.getTotalSnapshot());
    }

    void pauseSkill(Skill skill, boolean pause) {
        if (pause ? this.xpPauseState.pauseSkill(skill) : this.xpPauseState.unpauseSkill(skill)) {
            this.xpPanel.updateSkillExperience(false, this.xpPauseState.isPaused(skill), skill, this.xpState.getSkillSnapshot(skill));
        }
    }

    void pauseAllSkills(boolean pause) {
        for (Skill skill : Skill.values()) {
            this.pauseSkill(skill, pause);
        }
    }

    void setXpPanel(XpPanel xpPanel) {
        this.xpPanel = xpPanel;
    }
}

