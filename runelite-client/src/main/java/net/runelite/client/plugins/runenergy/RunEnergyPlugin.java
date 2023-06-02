/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.EquipmentInventorySlot
 *  net.runelite.api.InventoryID
 *  net.runelite.api.Item
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.Skill
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.ScriptCallbackEvent
 *  net.runelite.api.events.ScriptPostFired
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.apache.commons.lang3.StringUtils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.runenergy;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.runenergy.RunEnergyConfig;
import net.runelite.client.plugins.runenergy.RunEnergyOverlay;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Run Energy", description="Show various information related to run energy", tags={"overlay", "stamina"})
public class RunEnergyPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(RunEnergyPlugin.class);
    private static final ImmutableSet<Integer> ALL_GRACEFUL_HOODS = ImmutableSet.of((Object)11851, (Object)13579, (Object)13580, (Object)13591, (Object)13592, (Object)13603, (Object[])new Integer[]{13604, 13615, 13616, 13627, 13628, 13667, 13668, 21061, 21063, 24743, 24745, 25069, 25071});
    private static final ImmutableSet<Integer> ALL_GRACEFUL_TOPS = ImmutableSet.of((Object)11855, (Object)13583, (Object)13584, (Object)13595, (Object)13596, (Object)13607, (Object[])new Integer[]{13608, 13619, 13620, 13631, 13632, 13671, 13672, 21067, 21069, 24749, 24751, 25075, 25077});
    private static final ImmutableSet<Integer> ALL_GRACEFUL_LEGS = ImmutableSet.of((Object)11857, (Object)13585, (Object)13586, (Object)13597, (Object)13598, (Object)13609, (Object[])new Integer[]{13610, 13621, 13622, 13633, 13634, 13673, 13674, 21070, 21072, 24752, 24754, 25078, 25080});
    private static final ImmutableSet<Integer> ALL_GRACEFUL_GLOVES = ImmutableSet.of((Object)11859, (Object)13587, (Object)13588, (Object)13599, (Object)13600, (Object)13611, (Object[])new Integer[]{13612, 13623, 13624, 13635, 13636, 13675, 13676, 21073, 21075, 24755, 24757, 25081, 25083});
    private static final ImmutableSet<Integer> ALL_GRACEFUL_BOOTS = ImmutableSet.of((Object)11861, (Object)13589, (Object)13590, (Object)13601, (Object)13602, (Object)13613, (Object[])new Integer[]{13614, 13625, 13626, 13637, 13638, 13677, 13678, 21076, 21078, 24758, 24760, 25084, 25086});
    private static final ImmutableSet<Integer> ALL_GRACEFUL_CAPES = ImmutableSet.of((Object)11853, (Object)13581, (Object)13582, (Object)13593, (Object)13594, (Object)13605, (Object[])new Integer[]{13606, 13617, 13618, 13629, 13630, 13669, 13670, 21064, 21066, 24746, 24748, 25072, 25074, 9771, 9772, 13280});
    private static final int GRACEFUL_FULL_SET_BOOST_BONUS = 10;
    private static final int RING_OF_ENDURANCE_PASSIVE_EFFECT = 500;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private RunEnergyOverlay energyOverlay;
    @Inject
    private RunEnergyConfig energyConfig;
    @Inject
    private ConfigManager configManager;
    private int lastCheckTick;
    private boolean roeWarningSent;
    private boolean localPlayerRunningToDestination;
    private WorldPoint prevLocalPlayerLocation;

    @Provides
    RunEnergyConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(RunEnergyConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.energyOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.energyOverlay);
        this.localPlayerRunningToDestination = false;
        this.prevLocalPlayerLocation = null;
        this.lastCheckTick = -1;
        this.roeWarningSent = false;
        this.resetRunOrbText();
    }

    Integer getRingOfEnduranceCharges() {
        return (Integer)this.configManager.getRSProfileConfiguration("runenergy", "ringOfEnduranceCharges", (Type)((Object)Integer.class));
    }

    void setRingOfEnduranceCharges(int charges) {
        this.configManager.setRSProfileConfiguration("runenergy", "ringOfEnduranceCharges", charges);
    }

    boolean isRingOfEnduranceEquipped() {
        ItemContainer equipment = this.client.getItemContainer(InventoryID.EQUIPMENT);
        return equipment != null && equipment.count(24736) == 1;
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        this.localPlayerRunningToDestination = this.prevLocalPlayerLocation != null && this.client.getLocalDestinationLocation() != null && this.prevLocalPlayerLocation.distanceTo(this.client.getLocalPlayer().getWorldLocation()) > 1;
        this.prevLocalPlayerLocation = this.client.getLocalPlayer().getWorldLocation();
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired scriptPostFired) {
        if (scriptPostFired.getScriptId() == 447 && this.energyConfig.replaceOrbText()) {
            this.setRunOrbText(this.getEstimatedRunTimeRemaining(true));
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("runenergy") && !this.energyConfig.replaceOrbText()) {
            this.resetRunOrbText();
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM) {
            return;
        }
        String message = event.getMessage();
        if (message.equals("Your Ring of endurance doubles the duration of your stamina potion's effect.")) {
            Integer charges = this.getRingOfEnduranceCharges();
            if (charges == null) {
                log.debug("Ring of endurance charge with no known charges");
                return;
            }
            Integer n = charges;
            Integer n2 = charges = Integer.valueOf(charges - 1);
            this.setRingOfEnduranceCharges(charges);
            if (!this.roeWarningSent && charges < 500 && this.energyConfig.ringOfEnduranceChargeMessage()) {
                String chatMessage = new ChatMessageBuilder().append(ChatColorType.HIGHLIGHT).append("Your Ring of endurance now has less than 500 charges. Add more charges to regain its passive stamina effect.").build();
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
                this.roeWarningSent = true;
            }
        } else if (message.startsWith("Your Ring of endurance is charged with") || message.startsWith("You load your Ring of endurance with")) {
            Matcher matcher = Pattern.compile("([0-9]+)").matcher(message);
            int charges = -1;
            while (matcher.find()) {
                charges = Integer.parseInt(matcher.group(1));
            }
            this.setRingOfEnduranceCharges(charges);
            if (charges >= 500) {
                this.roeWarningSent = false;
            }
        }
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent event) {
        if (!"destroyOnOpKey".equals(event.getEventName())) {
            return;
        }
        int yesOption = this.client.getIntStack()[this.client.getIntStackSize() - 1];
        if (yesOption == 1) {
            this.checkDestroyWidget();
        }
    }

    private void setRunOrbText(String text) {
        Widget runOrbText = this.client.getWidget(WidgetInfo.MINIMAP_RUN_ORB_TEXT);
        if (runOrbText != null) {
            runOrbText.setText(text);
        }
    }

    private void resetRunOrbText() {
        this.setRunOrbText(Integer.toString(this.client.getEnergy()));
    }

    String getEstimatedRunTimeRemaining(boolean inSeconds) {
        int effectiveWeight = Math.min(Math.max(this.client.getWeight(), 0), 64);
        int energyUnitsLost = effectiveWeight * 67 / 64 + 67;
        if (this.client.getVarbitValue(25) != 0) {
            energyUnitsLost = (int)((double)energyUnitsLost * 0.3);
        } else if (this.isRingOfEnduranceEquipped()) {
            Integer charges = this.getRingOfEnduranceCharges();
            if (charges == null) {
                return "?";
            }
            if (charges >= 500) {
                energyUnitsLost = (int)((double)energyUnitsLost * 0.85);
            }
        }
        double ticksLeft = Math.ceil((double)this.client.getEnergy() / ((double)energyUnitsLost / 100.0));
        double secondsLeft = ticksLeft * 600.0 / 1000.0;
        if (inSeconds) {
            return (int)Math.floor(secondsLeft) + "s";
        }
        int minutes = (int)Math.floor(secondsLeft / 60.0);
        int seconds = (int)Math.floor(secondsLeft - (double)minutes * 60.0);
        return minutes + ":" + StringUtils.leftPad((String)Integer.toString(seconds), (int)2, (String)"0");
    }

    private int getGracefulRecoveryBoost() {
        ItemContainer equipment = this.client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipment == null) {
            return 0;
        }
        Item[] items = equipment.getItems();
        int boost = 0;
        for (GracefulEquipmentSlot slot : GracefulEquipmentSlot.values()) {
            Item wornItem;
            if (items.length <= slot.getIndex() || (wornItem = items[slot.getIndex()]) == null || !slot.getItems().contains((Object)wornItem.getId())) continue;
            boost += slot.getBoost();
        }
        if (boost == GracefulEquipmentSlot.TOTAL_BOOSTS) {
            boost += 10;
        }
        return boost;
    }

    int getEstimatedRecoverTimeRemaining() {
        if (this.localPlayerRunningToDestination) {
            return -1;
        }
        double recoverRate = (double)(48 + this.client.getBoostedSkillLevel(Skill.AGILITY)) / 360.0;
        double secondsLeft = (double)(100 - this.client.getEnergy()) / (recoverRate *= 1.0 + (double)this.getGracefulRecoveryBoost() / 100.0);
        return (int)secondsLeft;
    }

    private void checkDestroyWidget() {
        int currentTick = this.client.getTickCount();
        if (this.lastCheckTick == currentTick) {
            return;
        }
        this.lastCheckTick = currentTick;
        Widget widgetDestroyItemName = this.client.getWidget(WidgetInfo.DESTROY_ITEM_NAME);
        if (widgetDestroyItemName == null) {
            return;
        }
        if (widgetDestroyItemName.getText().equals("Ring of endurance")) {
            this.setRingOfEnduranceCharges(0);
        }
    }

    static /* synthetic */ ImmutableSet access$000() {
        return ALL_GRACEFUL_HOODS;
    }

    static /* synthetic */ ImmutableSet access$100() {
        return ALL_GRACEFUL_TOPS;
    }

    static /* synthetic */ ImmutableSet access$200() {
        return ALL_GRACEFUL_LEGS;
    }

    static /* synthetic */ ImmutableSet access$300() {
        return ALL_GRACEFUL_GLOVES;
    }

    static /* synthetic */ ImmutableSet access$400() {
        return ALL_GRACEFUL_BOOTS;
    }

    static /* synthetic */ ImmutableSet access$500() {
        return ALL_GRACEFUL_CAPES;
    }

    private static enum GracefulEquipmentSlot {
        HEAD(EquipmentInventorySlot.HEAD.getSlotIdx(), (ImmutableSet<Integer>)RunEnergyPlugin.access$000(), 3),
        BODY(EquipmentInventorySlot.BODY.getSlotIdx(), (ImmutableSet<Integer>)RunEnergyPlugin.access$100(), 4),
        LEGS(EquipmentInventorySlot.LEGS.getSlotIdx(), (ImmutableSet<Integer>)RunEnergyPlugin.access$200(), 4),
        GLOVES(EquipmentInventorySlot.GLOVES.getSlotIdx(), (ImmutableSet<Integer>)RunEnergyPlugin.access$300(), 3),
        BOOTS(EquipmentInventorySlot.BOOTS.getSlotIdx(), (ImmutableSet<Integer>)RunEnergyPlugin.access$400(), 3),
        CAPE(EquipmentInventorySlot.CAPE.getSlotIdx(), (ImmutableSet<Integer>)RunEnergyPlugin.access$500(), 3);

        private final int index;
        private final ImmutableSet<Integer> items;
        private final int boost;
        private static final int TOTAL_BOOSTS;

        private GracefulEquipmentSlot(int index, ImmutableSet<Integer> items, int boost) {
            this.index = index;
            this.items = items;
            this.boost = boost;
        }

        public int getIndex() {
            return this.index;
        }

        public ImmutableSet<Integer> getItems() {
            return this.items;
        }

        public int getBoost() {
            return this.boost;
        }

        static {
            TOTAL_BOOSTS = Arrays.stream(GracefulEquipmentSlot.values()).mapToInt(GracefulEquipmentSlot::getBoost).sum();
        }
    }
}

