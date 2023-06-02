/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSerializer
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.Hitsplat
 *  net.runelite.api.Player
 *  net.runelite.api.Prayer
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.HitsplatApplied
 *  net.runelite.api.events.InteractingChanged
 *  org.apache.commons.lang3.ArrayUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.external.pvpperformancetracker;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.inject.Provides;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Hitsplat;
import net.runelite.api.Player;
import net.runelite.api.Prayer;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.external.pvpperformancetracker.FightPerformance;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerConfig;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerOverlay;
import net.runelite.client.plugins.external.pvpperformancetracker.PvpPerformanceTrackerPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="PvP Performance Tracker")
public class PvpPerformanceTrackerPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(PvpPerformanceTrackerPlugin.class);
    public static final String FIGHT_HISTORY_DATA_FNAME = "FightHistoryData.json";
    public static final File FIGHT_HISTORY_DATA_DIR;
    public static Image ICON;
    public static SpriteManager SPRITE_MANAGER;
    public static PvpPerformanceTrackerConfig CONFIG;
    public static PvpPerformanceTrackerPlugin PLUGIN;
    private static final Set<Integer> LAST_MAN_STANDING_REGIONS;
    public ArrayList<FightPerformance> fightHistory;
    private NavigationButton navButton;
    private boolean navButtonShown = false;
    private PvpPerformanceTrackerPanel panel;
    @Inject
    private PvpPerformanceTrackerConfig config;
    @Inject
    private SpriteManager spriteManager;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    public Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private ConfigManager configManager;
    @Inject
    private RuneLiteConfig runeliteConfig;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private PvpPerformanceTrackerOverlay overlay;
    @Inject
    private ItemManager itemManager;
    @Inject
    private ScheduledExecutorService executor;
    private FightPerformance currentFight;
    private Gson gson;

    @Provides
    PvpPerformanceTrackerConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(PvpPerformanceTrackerConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        CONFIG = this.config;
        PLUGIN = this;
        this.panel = (PvpPerformanceTrackerPanel)this.injector.getInstance(PvpPerformanceTrackerPanel.class);
        BufferedImage icon = ImageUtil.getResourceStreamFromClass(this.getClass(), "/skull_red.png");
        ICON = new ImageIcon(icon).getImage();
        this.navButton = NavigationButton.builder().tooltip("PvP Fight History").icon(icon).priority(6).panel(this.panel).build();
        SPRITE_MANAGER = this.spriteManager;
        this.fightHistory = new ArrayList();
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Double.class, (Object)((JsonSerializer)(value, theType, context) -> value.isNaN() ? new JsonPrimitive((Number)0) : new JsonPrimitive((Number)BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP)))).create();
        this.configManager.unsetConfiguration("pvpperformancetracker", "fightHistoryData");
        this.importFightHistoryData();
        if (this.config.showFightHistoryPanel() && (!this.config.restrictToLms() || this.client.getGameState() == GameState.LOGGED_IN && this.isAtLMS())) {
            this.navButtonShown = true;
            this.clientToolbar.addNavigation(this.navButton);
        }
        this.overlayManager.add(this.overlay);
    }

    @Override
    protected void shutDown() throws Exception {
        this.updateFightHistoryData();
        this.clientToolbar.removeNavigation(this.navButton);
        this.overlayManager.remove(this.overlay);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals("pvpperformancetracker")) {
            return;
        }
        switch (event.getKey()) {
            case "showFightHistoryPanel": 
            case "restrictToLms": {
                boolean isAtLms = this.isAtLMS();
                if (!this.navButtonShown && this.config.showFightHistoryPanel() && (!this.config.restrictToLms() || isAtLms)) {
                    SwingUtilities.invokeLater(() -> this.clientToolbar.addNavigation(this.navButton));
                    this.navButtonShown = true;
                    break;
                }
                if (!this.navButtonShown || this.config.showFightHistoryPanel() && (!this.config.restrictToLms() || isAtLms)) break;
                SwingUtilities.invokeLater(() -> this.clientToolbar.removeNavigation(this.navButton));
                this.navButtonShown = false;
                break;
            }
            case "useSimpleOverlay": 
            case "showOverlayTitle": 
            case "showOverlayNames": 
            case "showOverlayOffPray": 
            case "showOverlayDeservedDmg": 
            case "showOverlayDmgDealt": 
            case "showOverlayMagicHits": 
            case "showOverlayOffensivePray": {
                this.overlay.setLines();
                break;
            }
            case "fightHistoryLimit": {
                if (this.config.fightHistoryLimit() <= 0 || this.fightHistory.size() <= this.config.fightHistoryLimit()) break;
                int numToRemove = this.fightHistory.size() - this.config.fightHistoryLimit();
                this.fightHistory.removeIf(f -> this.fightHistory.indexOf(f) < numToRemove);
                this.panel.rebuild();
            }
        }
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {
        Actor opponent;
        if (this.config.restrictToLms() && !this.isAtLMS()) {
            return;
        }
        this.stopFightIfOver();
        if (this.hasOpponent() && this.currentFight.fightStarted() || !(event.getSource() instanceof Player) || !(event.getTarget() instanceof Player)) {
            return;
        }
        if (event.getSource() == this.client.getLocalPlayer()) {
            opponent = event.getTarget();
        } else if (event.getTarget() == this.client.getLocalPlayer()) {
            opponent = event.getSource();
        } else {
            return;
        }
        if (!this.hasOpponent() || !this.currentFight.getOpponent().getName().equals(opponent.getName())) {
            this.currentFight = new FightPerformance(this.client.getLocalPlayer(), (Player)opponent, this.itemManager);
            this.overlay.setFight(this.currentFight);
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        if (this.config.restrictToLms()) {
            if (this.isAtLMS()) {
                if (!this.navButtonShown && this.config.showFightHistoryPanel()) {
                    this.clientToolbar.addNavigation(this.navButton);
                    this.navButtonShown = true;
                }
            } else if (this.navButtonShown) {
                this.clientToolbar.removeNavigation(this.navButton);
                this.navButtonShown = false;
            }
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        this.stopFightIfOver();
        this.clientThread.invokeLater(() -> {
            if (this.hasOpponent() && event.getActor() instanceof Player && event.getActor().getName() != null) {
                this.currentFight.checkForAttackAnimations(event.getActor().getName());
            }
        });
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied event) {
        Hitsplat hitsplat = event.getHitsplat();
        int hitType = hitsplat.getHitsplatType();
        if (!this.hasOpponent() || !(event.getActor() instanceof Player) || !hitsplat.isMyNormalDamage() && !hitsplat.isOthersNormalDamage() && hitType != 2 && hitType != 5) {
            return;
        }
        this.currentFight.addDamageDealt(event.getActor().getName(), hitsplat.getAmount());
    }

    @Override
    public void resetConfiguration() {
        super.resetConfiguration();
        this.resetFightHistory();
    }

    @Subscribe
    public void onClientShutdown(ClientShutdown event) {
        event.waitFor(this.executor.submit(this::updateFightHistoryData));
    }

    private boolean hasOpponent() {
        return this.currentFight != null;
    }

    private void stopFightIfOver() {
        if (this.hasOpponent() && this.currentFight.isFightOver()) {
            if (this.currentFight.fightStarted()) {
                this.addToFightHistory(this.currentFight);
            }
            this.currentFight = null;
        }
    }

    private void updateFightHistoryData() {
        try {
            File fightHistoryData = new File(FIGHT_HISTORY_DATA_DIR, FIGHT_HISTORY_DATA_FNAME);
            FileWriter writer = new FileWriter(fightHistoryData);
            this.gson.toJson(this.fightHistory, (Appendable)writer);
            ((Writer)writer).flush();
            ((Writer)writer).close();
        }
        catch (Exception e) {
            log.warn("Error ignored while updating fight history data: " + e.getMessage());
        }
    }

    void addToFightHistory(FightPerformance fight) {
        if (fight == null) {
            return;
        }
        this.fightHistory.add(fight);
        if (this.config.fightHistoryLimit() > 0 && this.fightHistory.size() > this.config.fightHistoryLimit()) {
            int numToRemove = this.fightHistory.size() - this.config.fightHistoryLimit();
            this.fightHistory.removeIf(f -> this.fightHistory.indexOf(f) < numToRemove);
            this.panel.rebuild();
        } else {
            this.panel.addFight(fight);
        }
    }

    void importFightHistoryData() {
        try {
            FIGHT_HISTORY_DATA_DIR.mkdirs();
            File fightHistoryData = new File(FIGHT_HISTORY_DATA_DIR, FIGHT_HISTORY_DATA_FNAME);
            if (!fightHistoryData.exists()) {
                FileWriter writer = new FileWriter(fightHistoryData);
                writer.write("[]");
                ((Writer)writer).close();
            }
            List<FightPerformance> savedFights = Arrays.asList((FightPerformance[])this.gson.fromJson((Reader)new FileReader(fightHistoryData), FightPerformance[].class));
            this.fightHistory.clear();
            this.importFights(savedFights);
        }
        catch (Exception e) {
            log.warn("Error while deserializing fight history data: " + e.getMessage());
            return;
        }
        this.panel.rebuild();
    }

    void importUserFightHistoryData(String data) {
        try {
            List<FightPerformance> savedFights = Arrays.asList((FightPerformance[])this.gson.fromJson(data, FightPerformance[].class));
            this.importFights(savedFights);
            this.createConfirmationModal("Data Import Successful", "PvP Performance Tracker: your fight history data was successfully imported.");
        }
        catch (Exception e) {
            log.warn("Error while importing user's fight history data: " + e.getMessage());
            this.createConfirmationModal("Data Import Failed", "PvP Performance Tracker: your fight history data was invalid, and could not be imported.");
            return;
        }
        this.panel.rebuild();
    }

    void importFights(List<FightPerformance> fights) throws NullPointerException {
        if (fights == null || fights.size() < 1) {
            return;
        }
        fights.removeIf(Objects::isNull);
        this.fightHistory.addAll(fights);
        this.fightHistory.sort(FightPerformance::compareTo);
        if (this.config.fightHistoryLimit() > 0 && this.fightHistory.size() > this.config.fightHistoryLimit()) {
            int numToRemove = this.fightHistory.size() - this.config.fightHistoryLimit();
            this.fightHistory.removeIf(f -> this.fightHistory.indexOf(f) < numToRemove);
        }
        for (FightPerformance f2 : this.fightHistory) {
            if (f2.getCompetitor() == null || f2.getOpponent() == null || f2.getCompetitor().getFightLogEntries() == null || f2.getOpponent().getFightLogEntries() == null) continue;
            f2.getCompetitor().getFightLogEntries().forEach(l -> {
                l.attackerName = f2.getCompetitor().getName();
            });
            f2.getOpponent().getFightLogEntries().forEach(l -> {
                l.attackerName = f2.getOpponent().getName();
            });
        }
    }

    void resetFightHistory() {
        this.fightHistory.clear();
        this.updateFightHistoryData();
        this.panel.rebuild();
    }

    void removeFight(FightPerformance fight) {
        this.fightHistory.remove(fight);
        this.panel.rebuild();
    }

    boolean isAtLMS() {
        int[] mapRegions = this.client.getMapRegions();
        for (int region : LAST_MAN_STANDING_REGIONS) {
            if (!ArrayUtils.contains((int[])mapRegions, (int)region)) continue;
            return true;
        }
        return false;
    }

    public void sendChatMessage(String chatMessage) {
        this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.TRADE).runeLiteFormattedMessage(chatMessage).build());
    }

    public void createConfirmationModal(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane optionPane = new JOptionPane();
            optionPane.setMessage(message);
            optionPane.setOptionType(-1);
            JDialog dialog = optionPane.createDialog(this.panel, title);
            if (dialog.isAlwaysOnTopSupported()) {
                dialog.setAlwaysOnTop(this.runeliteConfig.gameAlwaysOnTop());
            }
            dialog.setIconImage(ICON);
            dialog.setVisible(true);
        });
    }

    public void exportFightHistory() {
        String fightHistoryDataJson = this.gson.toJson((Object)this.fightHistory.toArray(new FightPerformance[0]), FightPerformance[].class);
        StringSelection contents = new StringSelection(fightHistoryDataJson);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, null);
        this.createConfirmationModal("Fight History Export Succeeded", "Fight history data was copied to the clipboard.");
    }

    public int currentlyUsedOffensivePray() {
        return this.client.isPrayerActive(Prayer.PIETY) ? 946 : (this.client.isPrayerActive(Prayer.ULTIMATE_STRENGTH) ? 125 : (this.client.isPrayerActive(Prayer.RIGOUR) ? 1420 : (this.client.isPrayerActive(Prayer.EAGLE_EYE) ? 504 : (this.client.isPrayerActive(Prayer.AUGURY) ? 1421 : (this.client.isPrayerActive(Prayer.MYSTIC_MIGHT) ? 505 : 0)))));
    }

    NavigationButton getNavButton() {
        return this.navButton;
    }

    PvpPerformanceTrackerPanel getPanel() {
        return this.panel;
    }

    public RuneLiteConfig getRuneliteConfig() {
        return this.runeliteConfig;
    }

    public FightPerformance getCurrentFight() {
        return this.currentFight;
    }

    static {
        LAST_MAN_STANDING_REGIONS = ImmutableSet.of((Object)13617, (Object)13658, (Object)13659, (Object)13660, (Object)13914, (Object)13915, (Object[])new Integer[]{13916});
        FIGHT_HISTORY_DATA_DIR = new File(RuneLite.RUNELITE_DIR, "pvp-performance-tracker");
        FIGHT_HISTORY_DATA_DIR.mkdirs();
    }
}

