/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.inject.Binder
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  javax.inject.Named
 *  joptsimple.internal.Strings
 *  net.runelite.api.Actor
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.Hitsplat
 *  net.runelite.api.MessageNode
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.Skill
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.ActorDeath
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.CommandExecuted
 *  net.runelite.api.events.FakeXpDrop
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.HitsplatApplied
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.NpcSpawned
 *  net.runelite.api.events.StatChanged
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.http.api.chat.Task
 *  org.apache.commons.lang3.ArrayUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.slayer;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Binder;
import com.google.inject.Provides;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Named;
import joptsimple.internal.Strings;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Hitsplat;
import net.runelite.api.MessageNode;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.FakeXpDrop;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.StatChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatClient;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ChatInput;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.slayer.SlayerConfig;
import net.runelite.client.plugins.slayer.SlayerOverlay;
import net.runelite.client.plugins.slayer.SlayerPluginService;
import net.runelite.client.plugins.slayer.SlayerPluginServiceImpl;
import net.runelite.client.plugins.slayer.SlayerUnlock;
import net.runelite.client.plugins.slayer.TargetWeaknessOverlay;
import net.runelite.client.plugins.slayer.Task;
import net.runelite.client.plugins.slayer.TaskCounter;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Slayer", description="Show additional slayer task related information", tags={"combat", "notifications", "overlay", "tasks"})
public class SlayerPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SlayerPlugin.class);
    private static final Pattern CHAT_GEM_PROGRESS_MESSAGE = Pattern.compile("^(?:You're assigned to kill|You have received a new Slayer assignment from .*:) (?:[Tt]he )?(?<name>.+?)(?: (?:in|on|south of) (?:the )?(?<location>[^;]+))?(?:; only | \\()(?<amount>\\d+)(?: more to go\\.|\\))$");
    private static final String CHAT_GEM_COMPLETE_MESSAGE = "You need something new to hunt.";
    private static final Pattern CHAT_COMPLETE_MESSAGE = Pattern.compile("You've completed (?:at least )?(?<tasks>[\\d,]+) (?:Wilderness )?tasks?(?: and received \\d+ points, giving you a total of (?<points>[\\d,]+)| and reached the maximum amount of Slayer points \\((?<points2>[\\d,]+)\\))?");
    private static final String CHAT_CANCEL_MESSAGE = "Your task has been cancelled.";
    private static final String CHAT_CANCEL_MESSAGE_JAD = "You no longer have a slayer task as you left the fight cave.";
    private static final String CHAT_CANCEL_MESSAGE_ZUK = "You no longer have a slayer task as you left the Inferno.";
    private static final String CHAT_SUPERIOR_MESSAGE = "A superior foe has appeared...";
    private static final String CHAT_BRACELET_SLAUGHTER = "Your bracelet of slaughter prevents your slayer";
    private static final String CHAT_BRACELET_EXPEDITIOUS = "Your expeditious bracelet helps you progress your";
    private static final Pattern COMBAT_BRACELET_TASK_UPDATE_MESSAGE = Pattern.compile("^You still need to kill (\\d+) monsters to complete your current Slayer assignment");
    private static final Pattern NPC_ASSIGN_MESSAGE = Pattern.compile(".*(?:Your new task is to kill|You are to bring balance to)\\s*(?<amount>\\d+) (?<name>.+?)(?: (?:in|on|south of) (?:the )?(?<location>.+))?\\.");
    private static final Pattern NPC_ASSIGN_BOSS_MESSAGE = Pattern.compile("^(?:Excellent\\. )?You're now assigned to (?:kill|bring balance to) (?:the )?(.*) (\\d+) times.*Your reward point tally is (.*)\\.$");
    private static final Pattern NPC_ASSIGN_FIRST_MESSAGE = Pattern.compile("^We'll start you off (?:hunting|bringing balance to) (.*), you'll need to kill (\\d*) of them\\.$");
    private static final Pattern NPC_CURRENT_MESSAGE = Pattern.compile("^You're (?:still(?: meant to be)?|currently assigned to) (?:hunting|bringing balance to|kill|bring balance to|slaying) (?<name>.+?)(?: (?:in|on|south of) (?:the )?(?<location>.+))?(?:, with|; (?:you have|only)) (?<amount>\\d+)(?: more)? to go\\..*");
    private static final Pattern REWARD_POINTS = Pattern.compile("Reward points: ((?:\\d+,)*\\d+)");
    private static final int GROTESQUE_GUARDIANS_REGION = 6727;
    private static final String TASK_COMMAND_STRING = "!task";
    private static final Pattern TASK_STRING_VALIDATION = Pattern.compile("[^a-zA-Z0-9' -]");
    private static final int TASK_STRING_MAX_LENGTH = 50;
    @Inject
    private Client client;
    @Inject
    private SlayerConfig config;
    @Inject
    private ConfigManager configManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SlayerOverlay overlay;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private Notifier notifier;
    @Inject
    private ClientThread clientThread;
    @Inject
    private TargetWeaknessOverlay targetWeaknessOverlay;
    @Inject
    private ChatCommandManager chatCommandManager;
    @Inject
    private ScheduledExecutorService executor;
    @Inject
    private ChatClient chatClient;
    @Inject
    private NpcOverlayService npcOverlayService;
    private final List<NPC> targets = new ArrayList<NPC>();
    @Inject
    @Named(value="developerMode")
    boolean developerMode;
    private final Set<NPC> taggedNpcs = new HashSet<NPC>();
    private int taggedNpcsDiedPrevTick;
    private int taggedNpcsDiedThisTick;
    private int amount;
    private int initialAmount;
    private String taskLocation;
    private String taskName;
    private TaskCounter counter;
    private int cachedXp = -1;
    private Instant infoTimer;
    private boolean loginFlag;
    private final List<Pattern> targetNames = new ArrayList<Pattern>();
    public final Function<NPC, HighlightedNpc> isTarget = n -> {
        if ((this.config.highlightHull() || this.config.highlightTile() || this.config.highlightOutline()) && this.targets.contains(n)) {
            Color color = this.config.getTargetColor();
            return HighlightedNpc.builder().npc((NPC)n).highlightColor(color).fillColor(ColorUtil.colorWithAlpha(color, color.getAlpha() / 12)).hull(this.config.highlightHull()).tile(this.config.highlightTile()).outline(this.config.highlightOutline()).build();
        }
        return null;
    };

    @Override
    public void configure(Binder binder) {
        binder.bind(SlayerPluginService.class).to(SlayerPluginServiceImpl.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.chatCommandManager.registerCommandAsync(TASK_COMMAND_STRING, (arg_0, arg_1) -> this.taskLookup(arg_0, arg_1), (arg_0, arg_1) -> this.taskSubmit(arg_0, arg_1));
        this.npcOverlayService.registerHighlighter(this.isTarget);
        this.overlayManager.add(this.overlay);
        this.overlayManager.add(this.targetWeaknessOverlay);
        if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.cachedXp = this.client.getSkillExperience(Skill.SLAYER);
            this.migrateConfig();
            if (this.getIntProfileConfig("amount") != -1 && !this.getStringProfileConfig("taskName").isEmpty()) {
                this.clientThread.invoke(() -> this.setTask(this.getStringProfileConfig("taskName"), this.getIntProfileConfig("amount"), this.getIntProfileConfig("initialAmount"), this.getStringProfileConfig("taskLocation"), false));
            }
        }
    }

    @Override
    protected void shutDown() throws Exception {
        this.chatCommandManager.unregisterCommand(TASK_COMMAND_STRING);
        this.npcOverlayService.unregisterHighlighter(this.isTarget);
        this.overlayManager.remove(this.overlay);
        this.overlayManager.remove(this.targetWeaknessOverlay);
        this.removeCounter();
        this.targets.clear();
        this.taggedNpcs.clear();
        this.cachedXp = -1;
    }

    @Provides
    SlayerConfig provideSlayerConfig(ConfigManager configManager) {
        return configManager.getConfig(SlayerConfig.class);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        switch (event.getGameState()) {
            case HOPPING: 
            case LOGGING_IN: {
                this.cachedXp = -1;
                this.taskName = "";
                this.amount = 0;
                this.loginFlag = true;
                this.targets.clear();
                this.taggedNpcs.clear();
                break;
            }
            case LOGGED_IN: {
                this.migrateConfig();
                if (this.getIntProfileConfig("amount") == -1 || this.getStringProfileConfig("taskName").isEmpty() || !this.loginFlag) break;
                this.setTask(this.getStringProfileConfig("taskName"), this.getIntProfileConfig("amount"), this.getIntProfileConfig("initialAmount"), this.getStringProfileConfig("taskLocation"), false);
                this.loginFlag = false;
            }
        }
    }

    @Subscribe
    public void onCommandExecuted(CommandExecuted commandExecuted) {
        if (this.developerMode && commandExecuted.getCommand().equals("task")) {
            this.setTask(commandExecuted.getArguments()[0], 42, 42);
            log.debug("Set task to {}", (Object)commandExecuted.getArguments()[0]);
        }
    }

    @VisibleForTesting
    int getIntProfileConfig(String key) {
        Integer value = (Integer)this.configManager.getRSProfileConfiguration("slayer", key, Integer.TYPE);
        return value == null ? -1 : value;
    }

    @VisibleForTesting
    String getStringProfileConfig(String key) {
        String value = (String)this.configManager.getRSProfileConfiguration("slayer", key, (Type)((Object)String.class));
        return value == null ? "" : value;
    }

    private void setProfileConfig(String key, Object value) {
        if (value != null) {
            this.configManager.setRSProfileConfiguration("slayer", key, value);
        } else {
            this.configManager.unsetRSProfileConfiguration("slayer", key);
        }
    }

    private void save() {
        this.setProfileConfig("amount", this.amount);
        this.setProfileConfig("initialAmount", this.initialAmount);
        this.setProfileConfig("taskName", this.taskName);
        this.setProfileConfig("taskLocation", this.taskLocation);
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
        NPC npc = npcSpawned.getNpc();
        if (this.isTarget(npc)) {
            this.targets.add(npc);
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        this.taggedNpcs.remove((Object)npc);
        this.targets.remove((Object)npc);
    }

    @Subscribe
    public void onGameTick(GameTick tick) {
        Duration statTimeout;
        Duration timeSinceInfobox;
        Widget rewardsBarWidget;
        Widget npcDialog = this.client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
        if (npcDialog != null) {
            String location;
            int amount;
            String name;
            String npcText = Text.sanitizeMultilineText(npcDialog.getText());
            Widget[] mAssign = NPC_ASSIGN_MESSAGE.matcher(npcText);
            Matcher mAssignFirst = NPC_ASSIGN_FIRST_MESSAGE.matcher(npcText);
            Matcher mAssignBoss = NPC_ASSIGN_BOSS_MESSAGE.matcher(npcText);
            Matcher mCurrent = NPC_CURRENT_MESSAGE.matcher(npcText);
            if (mAssign.find()) {
                name = mAssign.group("name");
                amount = Integer.parseInt(mAssign.group("amount"));
                location = mAssign.group("location");
                this.setTask(name, amount, amount, location);
            } else if (mAssignFirst.find()) {
                int amount2 = Integer.parseInt(mAssignFirst.group(2));
                this.setTask(mAssignFirst.group(1), amount2, amount2);
            } else if (mAssignBoss.find()) {
                int amount3 = Integer.parseInt(mAssignBoss.group(2));
                this.setTask(mAssignBoss.group(1), amount3, amount3);
                int points = Integer.parseInt(mAssignBoss.group(3).replaceAll(",", ""));
                this.setProfileConfig("points", points);
            } else if (mCurrent.find()) {
                name = mCurrent.group("name");
                amount = Integer.parseInt(mCurrent.group("amount"));
                location = mCurrent.group("location");
                this.setTask(name, amount, this.initialAmount, location);
            }
        }
        if ((rewardsBarWidget = this.client.getWidget(WidgetInfo.SLAYER_REWARDS_TOPBAR)) != null) {
            for (Widget w : rewardsBarWidget.getDynamicChildren()) {
                int points;
                Matcher mPoints = REWARD_POINTS.matcher(w.getText());
                if (!mPoints.find()) continue;
                int prevPoints = this.getIntProfileConfig("points");
                if (prevPoints == (points = Integer.parseInt(mPoints.group(1).replaceAll(",", "")))) break;
                this.setProfileConfig("points", points);
                this.removeCounter();
                this.addCounter();
                break;
            }
        }
        if (this.infoTimer != null && this.config.statTimeout() != 0 && (timeSinceInfobox = Duration.between(this.infoTimer, Instant.now())).compareTo(statTimeout = Duration.ofMinutes(this.config.statTimeout())) >= 0) {
            this.removeCounter();
        }
        this.taggedNpcsDiedPrevTick = this.taggedNpcsDiedThisTick;
        this.taggedNpcsDiedThisTick = 0;
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM) {
            return;
        }
        String chatMsg = Text.removeTags(event.getMessage());
        if (chatMsg.startsWith(CHAT_BRACELET_SLAUGHTER)) {
            ++this.amount;
        } else if (chatMsg.startsWith(CHAT_BRACELET_EXPEDITIOUS)) {
            --this.amount;
        }
        if (chatMsg.startsWith("You've completed") && (chatMsg.contains("Slayer master") || chatMsg.contains("Slayer Master"))) {
            Matcher mComplete = CHAT_COMPLETE_MESSAGE.matcher(chatMsg);
            if (mComplete.find()) {
                String mTasks = mComplete.group("tasks");
                String mPoints = mComplete.group("points");
                if (mPoints == null) {
                    mPoints = mComplete.group("points2");
                }
                if (mTasks != null) {
                    int streak = Integer.parseInt(mTasks.replace(",", ""));
                    this.setProfileConfig("streak", streak);
                }
                if (mPoints != null) {
                    int points = Integer.parseInt(mPoints.replace(",", ""));
                    this.setProfileConfig("points", points);
                }
            }
            this.setTask("", 0, 0);
            return;
        }
        if (chatMsg.equals(CHAT_GEM_COMPLETE_MESSAGE) || chatMsg.equals(CHAT_CANCEL_MESSAGE) || chatMsg.equals(CHAT_CANCEL_MESSAGE_JAD) || chatMsg.equals(CHAT_CANCEL_MESSAGE_ZUK)) {
            this.setTask("", 0, 0);
            return;
        }
        if (this.config.showSuperiorNotification() && chatMsg.equals(CHAT_SUPERIOR_MESSAGE)) {
            this.notifier.notify(CHAT_SUPERIOR_MESSAGE);
            return;
        }
        Matcher mProgress = CHAT_GEM_PROGRESS_MESSAGE.matcher(chatMsg);
        if (mProgress.find()) {
            String name = mProgress.group("name");
            int gemAmount = Integer.parseInt(mProgress.group("amount"));
            String location = mProgress.group("location");
            this.setTask(name, gemAmount, this.initialAmount, location);
            return;
        }
        Matcher bracerProgress = COMBAT_BRACELET_TASK_UPDATE_MESSAGE.matcher(chatMsg);
        if (bracerProgress.find()) {
            int taskAmount = Integer.parseInt(bracerProgress.group(1));
            this.setTask(this.taskName, taskAmount, this.initialAmount);
            ++this.amount;
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged statChanged) {
        if (statChanged.getSkill() != Skill.SLAYER) {
            return;
        }
        int slayerExp = statChanged.getXp();
        if (slayerExp <= this.cachedXp) {
            return;
        }
        if (this.cachedXp == -1) {
            this.cachedXp = slayerExp;
            return;
        }
        int delta = slayerExp - this.cachedXp;
        this.cachedXp = slayerExp;
        this.xpChanged(delta);
    }

    @Subscribe
    public void onFakeXpDrop(FakeXpDrop fakeXpDrop) {
        if (fakeXpDrop.getSkill() == Skill.SLAYER) {
            int delta = fakeXpDrop.getXp();
            this.xpChanged(delta);
        }
    }

    private void xpChanged(int delta) {
        log.debug("Slayer xp change delta: {}, killed npcs: {}", (Object)delta, (Object)this.taggedNpcsDiedPrevTick);
        Task task = Task.getTask(this.taskName);
        if (task != null && task.getXpMatcher() != null) {
            if (task.getXpMatcher().test(delta)) {
                this.killed(Integer.max(this.taggedNpcsDiedPrevTick, 1));
            }
        } else {
            this.killed(Integer.max(this.taggedNpcsDiedPrevTick, 1));
        }
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        Actor actor = hitsplatApplied.getActor();
        Hitsplat hitsplat = hitsplatApplied.getHitsplat();
        if (hitsplat.isMyNormalDamage() && this.targets.contains((Object)actor)) {
            this.taggedNpcs.add((NPC)actor);
        }
    }

    @Subscribe
    public void onActorDeath(ActorDeath actorDeath) {
        Actor actor = actorDeath.getActor();
        if (this.taggedNpcs.contains((Object)actor)) {
            log.debug("Tagged NPC {} has died", (Object)actor.getName());
            ++this.taggedNpcsDiedThisTick;
        }
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals("slayer")) {
            return;
        }
        if (event.getKey().equals("infobox")) {
            if (this.config.showInfobox()) {
                this.clientThread.invoke(this::addCounter);
            } else {
                this.removeCounter();
            }
        } else {
            this.npcOverlayService.rebuild();
        }
    }

    @VisibleForTesting
    void killed(int amt) {
        if (this.amount == 0) {
            return;
        }
        this.amount -= amt;
        if (this.doubleTroubleExtraKill()) {
            assert (amt == 1);
            --this.amount;
        }
        this.setProfileConfig("amount", this.amount);
        if (!this.config.showInfobox()) {
            return;
        }
        this.addCounter();
        this.counter.setCount(this.amount);
        this.infoTimer = Instant.now();
    }

    private boolean doubleTroubleExtraKill() {
        return WorldPoint.fromLocalInstance((Client)this.client, (LocalPoint)this.client.getLocalPlayer().getLocalLocation()).getRegionID() == 6727 && SlayerUnlock.GROTESQUE_GUARDIAN_DOUBLE_COUNT.isEnabled(this.client);
    }

    @VisibleForTesting
    boolean isTarget(NPC npc) {
        if (this.targetNames.isEmpty()) {
            return false;
        }
        NPCComposition composition = npc.getTransformedComposition();
        if (composition == null) {
            return false;
        }
        String name = composition.getName().replace('\u00a0', ' ').toLowerCase();
        for (Pattern target : this.targetNames) {
            Matcher targetMatcher = target.matcher(name);
            if (!targetMatcher.find() || !ArrayUtils.contains((Object[])composition.getActions(), (Object)"Attack") && !ArrayUtils.contains((Object[])composition.getActions(), (Object)"Pick")) continue;
            return true;
        }
        return false;
    }

    private void rebuildTargetNames(Task task) {
        this.targetNames.clear();
        if (task != null) {
            Arrays.stream(task.getTargetNames()).map(SlayerPlugin::targetNamePattern).forEach(this.targetNames::add);
            this.targetNames.add(SlayerPlugin.targetNamePattern(this.taskName.replaceAll("s$", "")));
        }
    }

    private static Pattern targetNamePattern(String targetName) {
        return Pattern.compile("(?:\\s|^)" + targetName + "(?:\\s|$)", 2);
    }

    private void rebuildTargetList() {
        this.targets.clear();
        for (NPC npc : this.client.getNpcs()) {
            if (!this.isTarget(npc)) continue;
            this.targets.add(npc);
        }
    }

    @VisibleForTesting
    void setTask(String name, int amt, int initAmt) {
        this.setTask(name, amt, initAmt, null);
    }

    private void setTask(String name, int amt, int initAmt, String location) {
        this.setTask(name, amt, initAmt, location, true);
    }

    private void setTask(String name, int amt, int initAmt, String location, boolean addCounter) {
        this.taskName = name;
        this.amount = amt;
        this.initialAmount = Math.max(amt, initAmt);
        this.taskLocation = location;
        this.save();
        this.removeCounter();
        if (addCounter) {
            this.infoTimer = Instant.now();
            this.addCounter();
        }
        Task task = Task.getTask(name);
        this.rebuildTargetNames(task);
        this.rebuildTargetList();
        this.npcOverlayService.rebuild();
    }

    private void addCounter() {
        if (!this.config.showInfobox() || this.counter != null || Strings.isNullOrEmpty((String)this.taskName)) {
            return;
        }
        Task task = Task.getTask(this.taskName);
        int itemSpriteId = 4155;
        if (task != null) {
            itemSpriteId = task.getItemSpriteId();
        }
        AsyncBufferedImage taskImg = this.itemManager.getImage(itemSpriteId);
        String taskTooltip = ColorUtil.wrapWithColorTag("%s", new Color(255, 119, 0)) + "</br>";
        if (this.taskLocation != null && !this.taskLocation.isEmpty()) {
            taskTooltip = taskTooltip + this.taskLocation + "</br>";
        }
        taskTooltip = taskTooltip + ColorUtil.wrapWithColorTag("Pts:", Color.YELLOW) + " %s</br>" + ColorUtil.wrapWithColorTag("Streak:", Color.YELLOW) + " %s";
        if (this.initialAmount > 0) {
            taskTooltip = taskTooltip + "</br>" + ColorUtil.wrapWithColorTag("Start:", Color.YELLOW) + " " + this.initialAmount;
        }
        this.counter = new TaskCounter(taskImg, this, this.amount);
        this.counter.setTooltip(String.format(taskTooltip, this.capsString(this.taskName), this.getIntProfileConfig("points"), this.getIntProfileConfig("streak")));
        this.infoBoxManager.addInfoBox(this.counter);
    }

    private void removeCounter() {
        if (this.counter == null) {
            return;
        }
        this.infoBoxManager.removeInfoBox(this.counter);
        this.counter = null;
    }

    void taskLookup(ChatMessage chatMessage, String message) {
        net.runelite.http.api.chat.Task task;
        if (!this.config.taskCommand()) {
            return;
        }
        ChatMessageType type = chatMessage.getType();
        String player = type.equals((Object)ChatMessageType.PRIVATECHATOUT) ? this.client.getLocalPlayer().getName() : Text.removeTags(chatMessage.getName()).replace('\u00a0', ' ');
        try {
            task = this.chatClient.getTask(player);
        }
        catch (IOException ex) {
            log.debug("unable to lookup slayer task", (Throwable)ex);
            return;
        }
        if (TASK_STRING_VALIDATION.matcher(task.getTask()).find() || task.getTask().length() > 50 || TASK_STRING_VALIDATION.matcher(task.getLocation()).find() || task.getLocation().length() > 50 || Task.getTask(task.getTask()) == null || !Task.LOCATIONS.contains(task.getLocation())) {
            log.debug("Validation failed for task name or location: {}", (Object)task);
            return;
        }
        int killed = task.getInitialAmount() - task.getAmount();
        StringBuilder sb = new StringBuilder();
        sb.append(task.getTask());
        if (!Strings.isNullOrEmpty((String)task.getLocation())) {
            sb.append(" (").append(task.getLocation()).append(')');
        }
        sb.append(": ");
        if (killed < 0) {
            sb.append(task.getAmount()).append(" left");
        } else {
            sb.append(killed).append('/').append(task.getInitialAmount()).append(" killed");
        }
        String response = new ChatMessageBuilder().append(ChatColorType.NORMAL).append("Slayer Task: ").append(ChatColorType.HIGHLIGHT).append(sb.toString()).build();
        MessageNode messageNode = chatMessage.getMessageNode();
        messageNode.setRuneLiteFormatMessage(response);
        this.client.refreshChat();
    }

    private boolean taskSubmit(ChatInput chatInput, String value) {
        if (Strings.isNullOrEmpty((String)this.taskName)) {
            return false;
        }
        String playerName = this.client.getLocalPlayer().getName();
        this.executor.execute(() -> {
            try {
                this.chatClient.submitTask(playerName, this.capsString(this.taskName), this.amount, this.initialAmount, this.taskLocation);
            }
            catch (Exception ex) {
                log.warn("unable to submit slayer task", (Throwable)ex);
            }
            finally {
                chatInput.resume();
            }
        });
        return true;
    }

    private String capsString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void migrateConfig() {
        this.migrateConfigKey("taskName");
        this.migrateConfigKey("amount");
        this.migrateConfigKey("initialAmount");
        this.migrateConfigKey("taskLocation");
        this.migrateConfigKey("streak");
        this.migrateConfigKey("points");
        this.configManager.unsetConfiguration("slayer", "expeditious");
        this.configManager.unsetConfiguration("slayer", "slaughter");
        this.configManager.unsetRSProfileConfiguration("slayer", "expeditious");
        this.configManager.unsetRSProfileConfiguration("slayer", "slaughter");
    }

    private void migrateConfigKey(String key) {
        String value = this.configManager.getConfiguration("slayer", key);
        if (value != null) {
            this.configManager.unsetConfiguration("slayer", key);
            this.configManager.setRSProfileConfiguration("slayer", key, value);
        }
    }

    List<NPC> getTargets() {
        return this.targets;
    }

    int getAmount() {
        return this.amount;
    }

    void setAmount(int amount) {
        this.amount = amount;
    }

    int getInitialAmount() {
        return this.initialAmount;
    }

    void setInitialAmount(int initialAmount) {
        this.initialAmount = initialAmount;
    }

    String getTaskLocation() {
        return this.taskLocation;
    }

    void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
    }

    String getTaskName() {
        return this.taskName;
    }

    void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}

