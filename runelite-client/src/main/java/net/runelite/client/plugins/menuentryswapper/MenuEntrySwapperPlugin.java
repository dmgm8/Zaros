/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Predicates
 *  com.google.common.base.Strings
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.LinkedHashMultimap
 *  com.google.common.collect.Multimap
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.ItemComposition
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.ObjectComposition
 *  net.runelite.api.events.ClientTick
 *  net.runelite.api.events.MenuOpened
 *  net.runelite.api.events.PostItemComposition
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.menuentryswapper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.ObjectComposition;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.PostItemComposition;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.NpcUtil;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.menuentryswapper.BuyMode;
import net.runelite.client.plugins.menuentryswapper.FairyRingMode;
import net.runelite.client.plugins.menuentryswapper.GEItemCollectMode;
import net.runelite.client.plugins.menuentryswapper.HouseMode;
import net.runelite.client.plugins.menuentryswapper.MenuEntrySwapperConfig;
import net.runelite.client.plugins.menuentryswapper.SellMode;
import net.runelite.client.plugins.menuentryswapper.ShiftDepositMode;
import net.runelite.client.plugins.menuentryswapper.ShiftWithdrawMode;
import net.runelite.client.plugins.menuentryswapper.Swap;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Menu Entry Swapper", description="Change the default option that is displayed when hovering over objects", tags={"npcs", "inventory", "items", "objects"}, enabledByDefault=false)
public class MenuEntrySwapperPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(MenuEntrySwapperPlugin.class);
    private static final String SHIFTCLICK_CONFIG_GROUP = "shiftclick";
    private static final String ITEM_KEY_PREFIX = "item_";
    private static final String OBJECT_KEY_PREFIX = "object_";
    private static final String OBJECT_SHIFT_KEY_PREFIX = "object_shift_";
    private static final String NPC_KEY_PREFIX = "npc_";
    private static final String NPC_SHIFT_KEY_PREFIX = "npc_shift_";
    private static final String WORN_ITEM_KEY_PREFIX = "wornitem_";
    private static final String WORN_ITEM_SHIFT_KEY_PREFIX = "wornitem_shift_";
    private static final String UI_KEY_PREFIX = "ui_";
    private static final String UI_SHIFT_KEY_PREFIX = "ui_shift_";
    private static final List<MenuAction> NPC_MENU_TYPES = ImmutableList.of((Object)MenuAction.NPC_FIRST_OPTION, (Object)MenuAction.NPC_SECOND_OPTION, (Object)MenuAction.NPC_THIRD_OPTION, (Object)MenuAction.NPC_FOURTH_OPTION, (Object)MenuAction.NPC_FIFTH_OPTION);
    private static final List<MenuAction> OBJECT_MENU_TYPES = ImmutableList.of((Object)MenuAction.GAME_OBJECT_FIRST_OPTION, (Object)MenuAction.GAME_OBJECT_SECOND_OPTION, (Object)MenuAction.GAME_OBJECT_THIRD_OPTION, (Object)MenuAction.GAME_OBJECT_FOURTH_OPTION);
    private static final Set<String> ESSENCE_MINE_NPCS = ImmutableSet.of((Object)"aubury", (Object)"archmage sedridor", (Object)"wizard distentor", (Object)"wizard cromperty", (Object)"brimstail");
    private static final Set<String> TEMPOROSS_NPCS = ImmutableSet.of((Object)"captain dudi", (Object)"captain pudi", (Object)"first mate deri", (Object)"first mate peri");
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private MenuEntrySwapperConfig config;
    @Inject
    private ConfigManager configManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private NpcUtil npcUtil;
    private final Multimap<String, Swap> swaps = LinkedHashMultimap.create();
    private final ArrayListMultimap<String, Integer> optionIndexes = ArrayListMultimap.create();

    @Provides
    MenuEntrySwapperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(MenuEntrySwapperConfig.class);
    }

    @Override
    public void startUp() {
        this.setupSwaps();
        this.removeOldSwaps();
    }

    @Override
    public void shutDown() {
        this.swaps.clear();
    }

    @VisibleForTesting
    void setupSwaps() {
        this.swap("talk-to", "mage of zamorak", "teleport", this.config::swapAbyssTeleport);
        this.swap("talk-to", "bank", this.config::swapBank);
        this.swap("talk-to", "exchange", this.config::swapExchange);
        this.swap("talk-to", "help", this.config::swapHelp);
        this.swap("talk-to", "assignment", this.config::swapAssignment);
        this.swap("talk-to", "trade", this.config::swapTrade);
        this.swap("talk-to", "trade-with", this.config::swapTrade);
        this.swap("talk-to", "shop", this.config::swapTrade);
        this.swap("talk-to", "travel", this.config::swapTravel);
        this.swap("talk-to", "pay-fare", this.config::swapTravel);
        this.swap("talk-to", "charter", this.config::swapTravel);
        this.swap("talk-to", "take-boat", this.config::swapTravel);
        this.swap("talk-to", "fly", this.config::swapTravel);
        this.swap("talk-to", "jatizso", this.config::swapTravel);
        this.swap("talk-to", "neitiznot", this.config::swapTravel);
        this.swap("talk-to", "rellekka", this.config::swapTravel);
        this.swap("talk-to", "ungael", this.config::swapTravel);
        this.swap("talk-to", "pirate's cove", this.config::swapTravel);
        this.swap("talk-to", "waterbirth island", this.config::swapTravel);
        this.swap("talk-to", "island of stone", this.config::swapTravel);
        this.swap("talk-to", "miscellania", this.config::swapTravel);
        this.swap("talk-to", "follow", this.config::swapTravel);
        this.swap("talk-to", "transport", this.config::swapTravel);
        this.swap("talk-to", "pay", this.config::swapPay);
        this.swapContains("talk-to", (Predicate<String>)Predicates.alwaysTrue(), "pay (", this.config::swapPay);
        this.swap("talk-to", "quick-travel", this.config::swapQuick);
        this.swap("talk-to", ESSENCE_MINE_NPCS::contains, "teleport", this.config::swapEssenceMineTeleport);
        this.swap("talk-to", "deposit-items", this.config::swapDepositItems);
        this.swap("talk-to", TEMPOROSS_NPCS::contains, "leave", this.config::swapTemporossLeave);
        this.swap("pass", "energy barrier", "pay-toll(2-ecto)", this.config::swapTravel);
        this.swap("open", "gate", "pay-toll(10gp)", this.config::swapTravel);
        this.swap("inspect", "trapdoor", "travel", this.config::swapTravel);
        this.swap("board", "travel cart", "pay-fare", this.config::swapTravel);
        this.swap("board", "sacrificial boat", "quick-board", this.config::swapQuick);
        this.swap("cage", "harpoon", this.config::swapHarpoon);
        this.swap("big net", "harpoon", this.config::swapHarpoon);
        this.swap("net", "harpoon", this.config::swapHarpoon);
        this.swap("lure", "bait", this.config::swapBait);
        this.swap("net", "bait", this.config::swapBait);
        this.swap("small net", "bait", this.config::swapBait);
        this.swap("enter", "portal", "home", () -> this.config.swapHomePortal() == HouseMode.HOME);
        this.swap("enter", "portal", "build mode", () -> this.config.swapHomePortal() == HouseMode.BUILD_MODE);
        this.swap("enter", "portal", "friend's house", () -> this.config.swapHomePortal() == HouseMode.FRIENDS_HOUSE);
        for (String option : new String[]{"zanaris", "tree"}) {
            this.swapContains(option, (Predicate<String>)Predicates.alwaysTrue(), "last-destination", () -> this.config.swapFairyRing() == FairyRingMode.LAST_DESTINATION);
            this.swapContains(option, (Predicate<String>)Predicates.alwaysTrue(), "configure", () -> this.config.swapFairyRing() == FairyRingMode.CONFIGURE);
        }
        this.swapContains("configure", (Predicate<String>)Predicates.alwaysTrue(), "last-destination", () -> this.config.swapFairyRing() == FairyRingMode.LAST_DESTINATION || this.config.swapFairyRing() == FairyRingMode.ZANARIS);
        this.swapContains("tree", (Predicate<String>)Predicates.alwaysTrue(), "zanaris", () -> this.config.swapFairyRing() == FairyRingMode.ZANARIS);
        this.swap("check", "reset", this.config::swapBoxTrap);
        this.swap("dismantle", "reset", this.config::swapBoxTrap);
        this.swap("take", "lay", this.config::swapBoxTrap);
        this.swap("pick-up", "chase", this.config::swapChase);
        this.swap("interact", (String target) -> target.endsWith("birdhouse"), "empty", this.config::swapBirdhouseEmpty);
        this.swap("enter", "quick-enter", this.config::swapQuick);
        this.swap("enter-crypt", "quick-enter", this.config::swapQuick);
        this.swap("ring", "quick-start", this.config::swapQuick);
        this.swap("pass", "quick-pass", this.config::swapQuick);
        this.swap("pass", "quick pass", this.config::swapQuick);
        this.swap("open", "quick-open", this.config::swapQuick);
        this.swap("climb-down", "quick-start", this.config::swapQuick);
        this.swap("climb-down", "pay", this.config::swapQuick);
        this.swap("admire", "teleport", this.config::swapAdmire);
        this.swap("admire", "spellbook", this.config::swapAdmire);
        this.swap("admire", "perks", this.config::swapAdmire);
        this.swap("teleport menu", "pvp arena", this.config::swapJewelleryBox);
        this.swap("teleport menu", "castle wars", this.config::swapJewelleryBox);
        this.swap("teleport menu", "ferox enclave", this.config::swapJewelleryBox);
        this.swap("teleport menu", "burthorpe", this.config::swapJewelleryBox);
        this.swap("teleport menu", "barbarian outpost", this.config::swapJewelleryBox);
        this.swap("teleport menu", "corporeal beast", this.config::swapJewelleryBox);
        this.swap("teleport menu", "tears of guthix", this.config::swapJewelleryBox);
        this.swap("teleport menu", "wintertodt camp", this.config::swapJewelleryBox);
        this.swap("teleport menu", "warriors' guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "champions' guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "monastery", this.config::swapJewelleryBox);
        this.swap("teleport menu", "ranging guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "fishing guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "mining guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "crafting guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "cooking guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "woodcutting guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "farming guild", this.config::swapJewelleryBox);
        this.swap("teleport menu", "miscellania", this.config::swapJewelleryBox);
        this.swap("teleport menu", "grand exchange", this.config::swapJewelleryBox);
        this.swap("teleport menu", "falador park", this.config::swapJewelleryBox);
        this.swap("teleport menu", "dondakan's rock", this.config::swapJewelleryBox);
        this.swap("teleport menu", "edgeville", this.config::swapJewelleryBox);
        this.swap("teleport menu", "karamja", this.config::swapJewelleryBox);
        this.swap("teleport menu", "draynor village", this.config::swapJewelleryBox);
        this.swap("teleport menu", "al kharid", this.config::swapJewelleryBox);
        Arrays.asList("annakarl", "ape atoll dungeon", "ardougne", "barrows", "battlefront", "camelot", "carrallangar", "catherby", "cemetery", "draynor manor", "falador", "fenkenstrain's castle", "fishing guild", "ghorrock", "grand exchange", "great kourend", "harmony island", "kharyrll", "lumbridge", "arceuus library", "lunar isle", "marim", "mind altar", "salve graveyard", "seers' village", "senntisten", "troll stronghold", "varrock", "watchtower", "waterbirth island", "weiss", "west ardougne", "yanille").forEach(location -> this.swap((String)location, "portal nexus", "teleport menu", this.config::swapPortalNexus));
        this.swap("shared", "private", this.config::swapPrivate);
        this.swap("pick", "pick-lots", this.config::swapPick);
        this.swap("view offer", "abort offer", () -> this.shiftModifier() && this.config.swapGEAbort());
        this.swap("value", "buy 1", () -> this.shiftModifier() && this.config.shopBuy() == BuyMode.BUY_1);
        this.swap("value", "buy 5", () -> this.shiftModifier() && this.config.shopBuy() == BuyMode.BUY_5);
        this.swap("value", "buy 10", () -> this.shiftModifier() && this.config.shopBuy() == BuyMode.BUY_10);
        this.swap("value", "buy 50", () -> this.shiftModifier() && this.config.shopBuy() == BuyMode.BUY_50);
        this.swap("value", "sell 1", () -> this.shiftModifier() && this.config.shopSell() == SellMode.SELL_1);
        this.swap("value", "sell 5", () -> this.shiftModifier() && this.config.shopSell() == SellMode.SELL_5);
        this.swap("value", "sell 10", () -> this.shiftModifier() && this.config.shopSell() == SellMode.SELL_10);
        this.swap("value", "sell 50", () -> this.shiftModifier() && this.config.shopSell() == SellMode.SELL_50);
        this.swap("wear", "tele to poh", this.config::swapTeleToPoh);
        this.swap("wear", "rub", this.config::swapTeleportItem);
        this.swap("wear", "teleport", this.config::swapTeleportItem);
        this.swap("wield", "teleport", this.config::swapTeleportItem);
        this.swap("wield", "invoke", this.config::swapTeleportItem);
        this.swap("wear", "teleports", this.config::swapTeleportItem);
        this.swap("wear", "farm teleport", () -> this.config.swapArdougneCloakMode() == MenuEntrySwapperConfig.ArdougneCloakMode.FARM);
        this.swap("wear", "monastery teleport", () -> this.config.swapArdougneCloakMode() == MenuEntrySwapperConfig.ArdougneCloakMode.MONASTERY);
        this.swap("wear", "gem mine", () -> this.config.swapKaramjaGlovesMode() == MenuEntrySwapperConfig.KaramjaGlovesMode.GEM_MINE);
        this.swap("wear", "duradel", () -> this.config.swapKaramjaGlovesMode() == MenuEntrySwapperConfig.KaramjaGlovesMode.DURADEL);
        this.swap("equip", "kourend woodland", () -> this.config.swapRadasBlessingMode() == MenuEntrySwapperConfig.RadasBlessingMode.KOUREND_WOODLAND);
        this.swap("equip", "mount karuulm", () -> this.config.swapRadasBlessingMode() == MenuEntrySwapperConfig.RadasBlessingMode.MOUNT_KARUULM);
        this.swap("wear", "ecto teleport", () -> this.config.swapMorytaniaLegsMode() == MenuEntrySwapperConfig.MorytaniaLegsMode.ECTOFUNTUS);
        this.swap("wear", "burgh teleport", () -> this.config.swapMorytaniaLegsMode() == MenuEntrySwapperConfig.MorytaniaLegsMode.BURGH_DE_ROTT);
        this.swap("wear", "nardah", () -> this.config.swapDesertAmuletMode() == MenuEntrySwapperConfig.DesertAmuletMode.NARDAH);
        this.swap("wear", "kalphite cave", () -> this.config.swapDesertAmuletMode() == MenuEntrySwapperConfig.DesertAmuletMode.KALPHITE_CAVE);
        this.swap("bury", "use", this.config::swapBones);
        this.swap("clean", "use", this.config::swapHerbs);
        this.swap("collect-note", "collect-item", () -> this.config.swapGEItemCollect() == GEItemCollectMode.ITEMS);
        this.swap("collect-notes", "collect-items", () -> this.config.swapGEItemCollect() == GEItemCollectMode.ITEMS);
        this.swap("collect-item", "collect-note", () -> this.config.swapGEItemCollect() == GEItemCollectMode.NOTES);
        this.swap("collect-items", "collect-notes", () -> this.config.swapGEItemCollect() == GEItemCollectMode.NOTES);
        this.swap("collect to inventory", "collect to bank", () -> this.config.swapGEItemCollect() == GEItemCollectMode.BANK);
        this.swap("collect", "bank", () -> this.config.swapGEItemCollect() == GEItemCollectMode.BANK);
        this.swap("collect-note", "bank", () -> this.config.swapGEItemCollect() == GEItemCollectMode.BANK);
        this.swap("collect-notes", "bank", () -> this.config.swapGEItemCollect() == GEItemCollectMode.BANK);
        this.swap("collect-item", "bank", () -> this.config.swapGEItemCollect() == GEItemCollectMode.BANK);
        this.swap("collect-items", "bank", () -> this.config.swapGEItemCollect() == GEItemCollectMode.BANK);
        this.swap("tan 1", "tan all", this.config::swapTan);
        this.swap("climb", "climb-up", () -> (this.shiftModifier() ? this.config.swapStairsShiftClick() : this.config.swapStairsLeftClick()) == MenuEntrySwapperConfig.StairsMode.CLIMB_UP);
        this.swap("climb", "climb-down", () -> (this.shiftModifier() ? this.config.swapStairsShiftClick() : this.config.swapStairsLeftClick()) == MenuEntrySwapperConfig.StairsMode.CLIMB_DOWN);
    }

    private void removeOldSwaps() {
        String[] keys;
        for (String key : keys = new String[]{"swapBattlestaves", "swapPrayerBook", "swapContract", "claimSlime", "swapDarkMage", "swapCaptainKhaled", "swapDecant", "swapHardWoodGrove", "swapHardWoodGroveParcel", "swapHouseAdvertisement", "swapEnchant", "swapHouseTeleportSpell", "swapTeleportSpell", "swapStartMinigame", "swapQuickleave", "swapNpcContact", "swapNets", "swapGauntlet", "swapCollectMiscellania", "swapRockCake", "swapRowboatDive"}) {
            this.configManager.unsetConfiguration("menuentryswapper", key);
        }
    }

    private void swap(String option, String swappedOption, Supplier<Boolean> enabled) {
        this.swap(option, (Predicate<String>)Predicates.alwaysTrue(), swappedOption, enabled);
    }

    private void swap(String option, String target, String swappedOption, Supplier<Boolean> enabled) {
        this.swap(option, (Predicate<String>)Predicates.equalTo((Object)target), swappedOption, enabled);
    }

    private void swap(String option, Predicate<String> targetPredicate, String swappedOption, Supplier<Boolean> enabled) {
        this.swaps.put((Object)option, (Object)new Swap((Predicate<String>)Predicates.alwaysTrue(), targetPredicate, swappedOption, enabled, true));
    }

    private void swapContains(String option, Predicate<String> targetPredicate, String swappedOption, Supplier<Boolean> enabled) {
        this.swaps.put((Object)option, (Object)new Swap((Predicate<String>)Predicates.alwaysTrue(), targetPredicate, swappedOption, enabled, false));
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("menuentryswapper") && event.getKey().equals("shiftClickCustomization")) {
            this.clientThread.invoke(this::resetItemCompositionCache);
        } else if (event.getGroup().equals(SHIFTCLICK_CONFIG_GROUP) && event.getKey().startsWith(ITEM_KEY_PREFIX)) {
            this.clientThread.invoke(this::resetItemCompositionCache);
        }
    }

    private void resetItemCompositionCache() {
        this.client.getItemCompositionCache().reset();
    }

    private Integer getItemSwapConfig(boolean shift, int itemId) {
        itemId = ItemVariationMapping.map(itemId);
        String config = this.configManager.getConfiguration(shift ? SHIFTCLICK_CONFIG_GROUP : "menuentryswapper", ITEM_KEY_PREFIX + itemId);
        if (config == null || config.isEmpty()) {
            return null;
        }
        return Integer.parseInt(config);
    }

    private void setItemSwapConfig(boolean shift, int itemId, int index) {
        itemId = ItemVariationMapping.map(itemId);
        this.configManager.setConfiguration(shift ? SHIFTCLICK_CONFIG_GROUP : "menuentryswapper", ITEM_KEY_PREFIX + itemId, index);
    }

    private void unsetItemSwapConfig(boolean shift, int itemId) {
        itemId = ItemVariationMapping.map(itemId);
        this.configManager.unsetConfiguration(shift ? SHIFTCLICK_CONFIG_GROUP : "menuentryswapper", ITEM_KEY_PREFIX + itemId);
    }

    private Integer getWornItemSwapConfig(boolean shift, int itemId) {
        itemId = ItemVariationMapping.map(itemId);
        String config = this.configManager.getConfiguration("menuentryswapper", (shift ? WORN_ITEM_SHIFT_KEY_PREFIX : WORN_ITEM_KEY_PREFIX) + itemId);
        if (config == null || config.isEmpty()) {
            return null;
        }
        return Integer.parseInt(config);
    }

    private void setWornItemSwapConfig(boolean shift, int itemId, int index) {
        itemId = ItemVariationMapping.map(itemId);
        this.configManager.setConfiguration("menuentryswapper", (shift ? WORN_ITEM_SHIFT_KEY_PREFIX : WORN_ITEM_KEY_PREFIX) + itemId, index);
    }

    private void unsetWornItemSwapConfig(boolean shift, int itemId) {
        itemId = ItemVariationMapping.map(itemId);
        this.configManager.unsetConfiguration("menuentryswapper", (shift ? WORN_ITEM_SHIFT_KEY_PREFIX : WORN_ITEM_KEY_PREFIX) + itemId);
    }

    @Subscribe
    public void onMenuOpened(MenuOpened event) {
        this.configureObjectClick(event);
        this.configureNpcClick(event);
        this.configureWornItems(event);
        this.configureItems(event);
        this.configureUiSwap(event);
    }

    private void configureObjectClick(MenuOpened event) {
        if (!this.shiftModifier() || !this.config.objectCustomization()) {
            return;
        }
        MenuEntry[] entries = event.getMenuEntries();
        for (int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            if (entry.getType() != MenuAction.EXAMINE_OBJECT) continue;
            ObjectComposition composition = this.client.getObjectDefinition(entry.getIdentifier());
            String[] actions = composition.getActions();
            Integer swapConfig = this.getObjectSwapConfig(false, composition.getId());
            MenuAction currentAction = swapConfig != null ? OBJECT_MENU_TYPES.get(swapConfig) : MenuEntrySwapperPlugin.defaultAction(composition);
            Integer shiftSwapConfig = this.getObjectSwapConfig(true, composition.getId());
            MenuAction currentShiftAction = shiftSwapConfig != null ? OBJECT_MENU_TYPES.get(shiftSwapConfig) : MenuEntrySwapperPlugin.defaultAction(composition);
            int shiftOff = 0;
            for (int actionIdx = 0; actionIdx < OBJECT_MENU_TYPES.size(); ++actionIdx) {
                if (Strings.isNullOrEmpty((String)actions[actionIdx])) continue;
                MenuAction menuAction = OBJECT_MENU_TYPES.get(actionIdx);
                if (menuAction != currentAction) {
                    this.client.createMenuEntry(idx + shiftOff).setOption("Swap left click " + actions[actionIdx]).setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(this.objectConsumer(composition, actions, actionIdx, menuAction, false));
                }
                if (menuAction == currentShiftAction || menuAction == currentAction) continue;
                this.client.createMenuEntry(idx).setOption("Swap shift click " + actions[actionIdx]).setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(this.objectConsumer(composition, actions, actionIdx, menuAction, true));
                ++shiftOff;
            }
            if (swapConfig == null && shiftSwapConfig == null) continue;
            this.client.createMenuEntry(idx).setOption("Reset swap").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(e -> {
                String message = new ChatMessageBuilder().append("The default left and shift click options for '").append(Text.removeTags(composition.getName())).append("' ").append("have been reset.").build();
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                log.debug("Unset object swap for {}", (Object)composition.getId());
                this.unsetObjectSwapConfig(true, composition.getId());
                this.unsetObjectSwapConfig(false, composition.getId());
            });
        }
    }

    private Consumer<MenuEntry> objectConsumer(ObjectComposition composition, String[] actions, int menuIdx, MenuAction menuAction, boolean shift) {
        return e -> {
            String message = new ChatMessageBuilder().append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been set to '").append(actions[menuIdx]).append("'.").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
            log.debug("Set object swap for {} to {}", (Object)composition.getId(), (Object)menuAction);
            this.setObjectSwapConfig(shift, composition.getId(), menuIdx);
        };
    }

    private Consumer<MenuEntry> walkHereConsumer(boolean shift, NPCComposition composition) {
        return e -> {
            String message = new ChatMessageBuilder().append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been set to Walk here.").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
            log.debug("Set npc {} click swap for {} to Walk here", (Object)(shift ? "shift" : "left"), (Object)composition.getId());
            this.setNpcSwapConfig(shift, composition.getId(), -1);
        };
    }

    private void configureNpcClick(MenuOpened event) {
        if (!this.shiftModifier() || !this.config.npcCustomization()) {
            return;
        }
        MenuEntry[] entries = event.getMenuEntries();
        for (int idx = entries.length - 1; idx >= 0; --idx) {
            MenuAction currentAction;
            MenuEntry entry = entries[idx];
            MenuAction type = entry.getType();
            if (type != MenuAction.EXAMINE_NPC) continue;
            NPC npc = entry.getNpc();
            assert (npc != null);
            NPCComposition composition = npc.getTransformedComposition();
            assert (composition != null);
            String[] actions = composition.getActions();
            Integer swapConfig = this.getNpcSwapConfig(false, composition.getId());
            Integer shiftSwapConfig = this.getNpcSwapConfig(true, composition.getId());
            boolean hasAttack = Arrays.stream(composition.getActions()).anyMatch("Attack"::equalsIgnoreCase);
            MenuAction menuAction = swapConfig == null ? (hasAttack ? null : MenuEntrySwapperPlugin.defaultAction(composition)) : (currentAction = swapConfig == -1 ? MenuAction.WALK : NPC_MENU_TYPES.get(swapConfig));
            MenuAction currentShiftAction = shiftSwapConfig == null ? (hasAttack ? null : MenuEntrySwapperPlugin.defaultAction(composition)) : (shiftSwapConfig == -1 ? MenuAction.WALK : NPC_MENU_TYPES.get(shiftSwapConfig));
            int shiftOff = 0;
            for (int actionIdx = 0; actionIdx < NPC_MENU_TYPES.size(); ++actionIdx) {
                if (Strings.isNullOrEmpty((String)actions[actionIdx]) || "Attack".equalsIgnoreCase(actions[actionIdx]) || "Knock-Out".equals(actions[actionIdx]) || "Lure".equals(actions[actionIdx])) continue;
                MenuAction menuAction2 = NPC_MENU_TYPES.get(actionIdx);
                if (menuAction2 != currentAction) {
                    this.client.createMenuEntry(idx + shiftOff).setOption("Swap left click " + actions[actionIdx]).setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(this.npcConsumer(composition, actions, actionIdx, menuAction2, false));
                }
                if (menuAction2 == currentShiftAction) continue;
                this.client.createMenuEntry(idx).setOption("Swap shift click " + actions[actionIdx]).setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(this.npcConsumer(composition, actions, actionIdx, menuAction2, true));
                ++shiftOff;
            }
            this.client.createMenuEntry(idx + shiftOff).setOption("Swap left click Walk here").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(this.walkHereConsumer(false, composition));
            this.client.createMenuEntry(idx).setOption("Swap shift click Walk here").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(this.walkHereConsumer(true, composition));
            ++shiftOff;
            if (this.getNpcSwapConfig(true, composition.getId()) == null && this.getNpcSwapConfig(false, composition.getId()) == null) continue;
            this.client.createMenuEntry(idx).setOption("Reset swap").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(e -> {
                String message = new ChatMessageBuilder().append("The default left and shift click options for '").append(Text.removeTags(composition.getName())).append("' ").append("have been reset.").build();
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                log.debug("Unset npc swap for {}", (Object)composition.getId());
                this.unsetNpcSwapConfig(true, composition.getId());
                this.unsetNpcSwapConfig(false, composition.getId());
            });
        }
    }

    private Consumer<MenuEntry> npcConsumer(NPCComposition composition, String[] actions, int menuIdx, MenuAction menuAction, boolean shift) {
        return e -> {
            String message = new ChatMessageBuilder().append("The default ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(composition.getName())).append("' ").append("has been set to '").append(actions[menuIdx]).append("'.").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
            log.debug("Set npc {} swap for {} to {}", new Object[]{shift ? "shift" : "left", composition.getId(), menuAction});
            this.setNpcSwapConfig(shift, composition.getId(), menuIdx);
        };
    }

    private void configureWornItems(MenuOpened event) {
        if (!this.shiftModifier()) {
            return;
        }
        MenuEntry[] entries = event.getMenuEntries();
        for (int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            Widget w = entry.getWidget();
            if (w == null || WidgetInfo.TO_GROUP((int)w.getId()) != 387 || !"Examine".equals(entry.getOption()) || entry.getIdentifier() != 10) continue;
            if ((w = w.getChild(1)) == null || w.getItemId() <= -1) break;
            ItemComposition itemComposition = this.itemManager.getItemComposition(w.getItemId());
            Integer leftClickOp = this.getWornItemSwapConfig(false, itemComposition.getId());
            Integer shiftClickOp = this.getWornItemSwapConfig(true, itemComposition.getId());
            ArrayList<MenuEntry> leftClickMenus = new ArrayList<MenuEntry>();
            ArrayList<MenuEntry> shiftClickMenus = new ArrayList<MenuEntry>();
            int paramId = 451;
            int opId = 2;
            while (paramId <= 458) {
                String opName = itemComposition.getStringValue(paramId);
                if (!Strings.isNullOrEmpty((String)opName)) {
                    if (leftClickOp == null || leftClickOp != opId) {
                        leftClickMenus.add(this.client.createMenuEntry(idx).setOption(opName).setType(MenuAction.RUNELITE).onClick(this.wornItemConsumer(itemComposition, opName, opId, false)));
                    }
                    if (shiftClickOp == null || shiftClickOp != opId) {
                        shiftClickMenus.add(this.client.createMenuEntry(idx).setOption(opName).setType(MenuAction.RUNELITE).onClick(this.wornItemConsumer(itemComposition, opName, opId, true)));
                    }
                }
                ++paramId;
                ++opId;
            }
            if (leftClickOp != null) {
                leftClickMenus.add(this.client.createMenuEntry(idx).setOption("Reset").setType(MenuAction.RUNELITE).onClick(e -> {
                    String message = new ChatMessageBuilder().append("The default worn left click option for '").append(itemComposition.getMembersName()).append("' ").append("has been reset.").build();
                    this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                    log.debug("Unset worn item left swap for {}", (Object)itemComposition.getMembersName());
                    this.unsetWornItemSwapConfig(false, itemComposition.getId());
                }));
            }
            if (shiftClickOp != null) {
                shiftClickMenus.add(this.client.createMenuEntry(idx).setOption("Reset").setType(MenuAction.RUNELITE).onClick(e -> {
                    String message = new ChatMessageBuilder().append("The default worn shift click option for '").append(itemComposition.getMembersName()).append("' ").append("has been reset.").build();
                    this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                    log.debug("Unset worn item shift swap for {}", (Object)itemComposition.getMembersName());
                    this.unsetWornItemSwapConfig(true, itemComposition.getId());
                }));
            }
            if (!leftClickMenus.isEmpty()) {
                MenuEntry sub = this.client.createMenuEntry(idx).setOption("Swap left click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE_SUBMENU);
                leftClickMenus.forEach(menu -> menu.setParent(sub));
            }
            if (shiftClickMenus.isEmpty()) break;
            MenuEntry sub = this.client.createMenuEntry(idx).setOption("Swap shift click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE_SUBMENU);
            shiftClickMenus.forEach(menu -> menu.setParent(sub));
            break;
        }
    }

    private Consumer<MenuEntry> wornItemConsumer(ItemComposition itemComposition, String opName, int opIdx, boolean shift) {
        return e -> {
            String message = new ChatMessageBuilder().append("The default worn ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(itemComposition.getMembersName())).append("' ").append("has been set to '").append(opName).append("'.").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
            log.debug("Set worn item {} swap for {} to {}", new Object[]{shift ? "shift" : "left", itemComposition.getMembersName(), opIdx});
            this.setWornItemSwapConfig(shift, itemComposition.getId(), opIdx);
        };
    }

    private void configureItems(MenuOpened event) {
        if (!this.shiftModifier()) {
            return;
        }
        MenuEntry[] entries = event.getMenuEntries();
        for (int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            Widget w = entry.getWidget();
            if (w == null || WidgetInfo.TO_GROUP((int)w.getId()) != 149 || !"Examine".equals(entry.getOption()) || entry.getIdentifier() != 10) continue;
            ItemComposition itemComposition = this.itemManager.getItemComposition(entry.getItemId());
            String[] actions = itemComposition.getInventoryActions();
            Integer leftClickOp = this.getItemSwapConfig(false, itemComposition.getId());
            Integer shiftClickOp = this.getItemSwapConfig(true, itemComposition.getId());
            int defaultLeftClickOp = this.defaultOp(itemComposition, false);
            int defaultShiftClickOp = this.defaultOp(itemComposition, true);
            ArrayList<MenuEntry> leftClickMenus = new ArrayList<MenuEntry>(actions.length + 2);
            ArrayList<MenuEntry> shiftClickMenus = new ArrayList<MenuEntry>(actions.length + 2);
            for (int actionIdx = 0; actionIdx < actions.length; ++actionIdx) {
                String opName = actions[actionIdx];
                if (!Strings.isNullOrEmpty((String)opName)) {
                    if (this.config.leftClickCustomization() && defaultLeftClickOp != actionIdx && (leftClickOp == null || leftClickOp != actionIdx)) {
                        leftClickMenus.add(this.client.createMenuEntry(idx).setOption(opName).setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, opName, actionIdx, false)));
                    }
                    if (this.config.shiftClickCustomization() && defaultShiftClickOp != actionIdx && (shiftClickOp == null || shiftClickOp != actionIdx)) {
                        shiftClickMenus.add(this.client.createMenuEntry(idx).setOption(opName).setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, opName, actionIdx, true)));
                    }
                }
                if (actionIdx != 2) continue;
                if (defaultLeftClickOp != -1 && this.config.leftClickCustomization()) {
                    leftClickMenus.add(this.client.createMenuEntry(idx).setOption("Use").setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, "Use", -1, false)));
                }
                if (defaultShiftClickOp == -1 || !this.config.shiftClickCustomization()) continue;
                shiftClickMenus.add(this.client.createMenuEntry(idx).setOption("Use").setType(MenuAction.RUNELITE).onClick(this.heldItemConsumer(itemComposition, "Use", -1, true)));
            }
            if (leftClickOp != null && this.config.leftClickCustomization()) {
                leftClickMenus.add(this.client.createMenuEntry(idx).setOption("Reset").setType(MenuAction.RUNELITE).onClick(e -> {
                    String message = new ChatMessageBuilder().append("The default held left click option for '").append(itemComposition.getMembersName()).append("' ").append("has been reset.").build();
                    this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                    log.debug("Unset held item left swap for {}", (Object)itemComposition.getMembersName());
                    this.unsetItemSwapConfig(false, itemComposition.getId());
                }));
            }
            if (shiftClickOp != null && this.config.shiftClickCustomization()) {
                shiftClickMenus.add(this.client.createMenuEntry(idx).setOption("Reset").setType(MenuAction.RUNELITE).onClick(e -> {
                    String message = new ChatMessageBuilder().append("The default held shift click option for '").append(itemComposition.getMembersName()).append("' ").append("has been reset.").build();
                    this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                    log.debug("Unset held item shift swap for {}", (Object)itemComposition.getMembersName());
                    this.unsetItemSwapConfig(true, itemComposition.getId());
                }));
            }
            if (!leftClickMenus.isEmpty()) {
                MenuEntry sub = this.client.createMenuEntry(idx).setOption("Swap left click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE_SUBMENU);
                leftClickMenus.forEach(menu -> menu.setParent(sub));
            }
            if (shiftClickMenus.isEmpty()) break;
            MenuEntry sub = this.client.createMenuEntry(idx).setOption("Swap shift click").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE_SUBMENU);
            shiftClickMenus.forEach(menu -> menu.setParent(sub));
            break;
        }
    }

    private Consumer<MenuEntry> heldItemConsumer(ItemComposition itemComposition, String opName, int opIdx, boolean shift) {
        return e -> {
            String message = new ChatMessageBuilder().append("The default held ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(itemComposition.getMembersName())).append("' ").append("has been set to '").append(opName).append("'.").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
            log.debug("Set held item {} swap for {} to {}", new Object[]{shift ? "shift" : "left", itemComposition.getMembersName(), opIdx});
            this.setItemSwapConfig(shift, itemComposition.getId(), opIdx);
        };
    }

    private void configureUiSwap(MenuOpened event) {
        if (!this.shiftModifier()) {
            return;
        }
        MenuEntry[] entries = event.getMenuEntries();
        int shiftOff = 0;
        for (int idx = entries.length - 1; idx >= 0; --idx) {
            int lowestOp;
            int interId;
            Widget w;
            MenuEntry entry = entries[idx];
            if (entry.getType() != MenuAction.CC_OP && entry.getType() != MenuAction.CC_OP_LOW_PRIORITY || (w = entry.getWidget()) == null || (interId = WidgetInfo.TO_GROUP((int)w.getId())) == 149 || interId == 387 || w.getIndex() != -1 && w.getItemId() == -1) continue;
            int componentId = w.getId();
            int itemId = w.getIndex() == -1 ? -1 : ItemVariationMapping.map(w.getItemId());
            int identifier = entry.getIdentifier();
            Integer leftClick = this.getUiSwapConfig(false, componentId, itemId);
            Integer shiftClick = this.getUiSwapConfig(true, componentId, itemId);
            for (lowestOp = 0; lowestOp < w.getActions().length && Strings.isNullOrEmpty((String)w.getActions()[lowestOp]); ++lowestOp) {
            }
            ++lowestOp;
            int highestOp = 10;
            for (int i = idx; i >= 0 && entries[i].getWidget() == w; --i) {
                highestOp = entries[i].getIdentifier();
            }
            if (identifier != lowestOp && (leftClick == null || leftClick != identifier)) {
                this.client.createMenuEntry(1 + shiftOff).setOption("Swap left click " + entry.getOption()).setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(this.uiConsumer(entry.getOption(), entry.getTarget(), false, componentId, itemId, identifier));
            }
            if (identifier != lowestOp && (shiftClick == null || shiftClick != identifier)) {
                this.client.createMenuEntry(1).setOption("Swap shift click " + entry.getOption()).setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(this.uiConsumer(entry.getOption(), entry.getTarget(), true, componentId, itemId, identifier));
                ++shiftOff;
            }
            if (identifier != highestOp || leftClick == null && shiftClick == null) continue;
            this.client.createMenuEntry(1).setOption("Reset swap").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick(menuEntry -> {
                String message = new ChatMessageBuilder().append("The default left and shift click options for '").append(Text.removeTags(menuEntry.getTarget())).append("' ").append("have been reset.").build();
                this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
                log.debug("Unset ui swap for {}/{}", (Object)componentId, (Object)menuEntry.getTarget());
                this.unsetUiSwapConfig(false, componentId, itemId);
                this.unsetUiSwapConfig(true, componentId, itemId);
            });
        }
    }

    private Consumer<MenuEntry> uiConsumer(String option, String target, boolean shift, int componentId, int itemId, int opId) {
        return e -> {
            String message = new ChatMessageBuilder().append("The default  ").append(shift ? "shift" : "left").append(" click option for '").append(Text.removeTags(target)).append("' ").append("has been set to '").append(option).append("'.").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
            log.debug("Set ui {} swap for {}/{} to {}", new Object[]{shift ? "shift" : "left", componentId, itemId, opId});
            this.setUiSwapConfig(shift, componentId, itemId, opId);
        };
    }

    private boolean swapBank(MenuEntry menuEntry, MenuAction type) {
        boolean isGroupStoragePlayerInventory;
        if (type != MenuAction.CC_OP && type != MenuAction.CC_OP_LOW_PRIORITY) {
            return false;
        }
        int widgetGroupId = WidgetInfo.TO_GROUP((int)menuEntry.getParam1());
        boolean isDepositBoxPlayerInventory = widgetGroupId == 192;
        boolean isChambersOfXericStorageUnitPlayerInventory = widgetGroupId == 551;
        boolean bl = isGroupStoragePlayerInventory = widgetGroupId == 725;
        if (this.shiftModifier() && this.config.bankDepositShiftClick() != ShiftDepositMode.OFF && type == MenuAction.CC_OP && menuEntry.getIdentifier() == (isDepositBoxPlayerInventory || isGroupStoragePlayerInventory || isChambersOfXericStorageUnitPlayerInventory ? 1 : 2) && (menuEntry.getOption().startsWith("Deposit-") || menuEntry.getOption().startsWith("Store") || menuEntry.getOption().startsWith("Donate"))) {
            ShiftDepositMode shiftDepositMode = this.config.bankDepositShiftClick();
            int opId = isDepositBoxPlayerInventory ? shiftDepositMode.getIdentifierDepositBox() : (isChambersOfXericStorageUnitPlayerInventory ? shiftDepositMode.getIdentifierChambersStorageUnit() : (isGroupStoragePlayerInventory ? shiftDepositMode.getIdentifierGroupStorage() : shiftDepositMode.getIdentifier()));
            MenuAction action = opId >= 6 ? MenuAction.CC_OP_LOW_PRIORITY : MenuAction.CC_OP;
            this.bankModeSwap(action, opId);
            return true;
        }
        if (this.shiftModifier() && this.config.bankWithdrawShiftClick() != ShiftWithdrawMode.OFF && type == MenuAction.CC_OP && menuEntry.getIdentifier() == 1 && menuEntry.getOption().startsWith("Withdraw")) {
            int opId;
            MenuAction action;
            ShiftWithdrawMode shiftWithdrawMode = this.config.bankWithdrawShiftClick();
            if (widgetGroupId == 271 || widgetGroupId == 550) {
                action = MenuAction.CC_OP;
                opId = shiftWithdrawMode.getIdentifierChambersStorageUnit();
            } else {
                action = shiftWithdrawMode.getMenuAction();
                opId = shiftWithdrawMode.getIdentifier();
            }
            this.bankModeSwap(action, opId);
            return true;
        }
        return false;
    }

    private void bankModeSwap(MenuAction entryType, int entryIdentifier) {
        MenuEntry[] menuEntries = this.client.getMenuEntries();
        for (int i = menuEntries.length - 1; i >= 0; --i) {
            MenuEntry entry = menuEntries[i];
            if (entry.getType() != entryType || entry.getIdentifier() != entryIdentifier) continue;
            entry.setType(MenuAction.CC_OP);
            menuEntries[i] = menuEntries[menuEntries.length - 1];
            menuEntries[menuEntries.length - 1] = entry;
            this.client.setMenuEntries(menuEntries);
            break;
        }
    }

    private void swapMenuEntry(MenuEntry[] menuEntries, int index, MenuEntry menuEntry) {
        Swap swap;
        MenuAction swapAction;
        Integer wornItemSwapConfig;
        Widget child;
        Integer swapIndex;
        int eventId = menuEntry.getIdentifier();
        MenuAction menuAction = menuEntry.getType();
        String option = Text.removeTags(menuEntry.getOption()).toLowerCase();
        String target = Text.removeTags(menuEntry.getTarget()).toLowerCase();
        boolean itemOp = menuEntry.isItemOp();
        if (this.shiftModifier() && itemOp) {
            Integer customOption2;
            if (this.config.shiftClickCustomization() && !option.equals("use") && (customOption2 = this.getItemSwapConfig(true, menuEntry.getItemId())) != null && customOption2 == -1) {
                this.swap(menuEntries, "use", target, index, true);
            }
            return;
        }
        if (itemOp && this.config.leftClickCustomization() && (swapIndex = this.getItemSwapConfig(false, menuEntry.getItemId())) != null) {
            int swapAction2;
            int n = swapAction2 = swapIndex >= 0 ? 1 + swapIndex : -1;
            if (swapAction2 == -1) {
                this.swap(menuEntries, "use", target, index, true);
            } else if (swapAction2 == menuEntry.getItemOp()) {
                this.swap(this.optionIndexes, menuEntries, index, menuEntries.length - 1);
            }
            return;
        }
        Widget w = menuEntry.getWidget();
        if (w != null && WidgetInfo.TO_GROUP((int)w.getId()) == 387 && (child = w.getChild(1)) != null && child.getItemId() > -1 && (wornItemSwapConfig = this.getWornItemSwapConfig(this.shiftModifier(), child.getItemId())) != null) {
            if (wornItemSwapConfig.intValue() == menuEntry.getIdentifier()) {
                this.swap(this.optionIndexes, menuEntries, index, menuEntries.length - 1);
            }
            return;
        }
        if (OBJECT_MENU_TYPES.contains((Object)menuAction)) {
            Integer n;
            int objectId = eventId;
            ObjectComposition objectComposition = this.client.getObjectDefinition(objectId);
            if (objectComposition.getImpostorIds() != null) {
                objectComposition = objectComposition.getImpostor();
                objectId = objectComposition.getId();
            }
            if ((n = this.getObjectSwapConfig(this.shiftModifier(), objectId)) != null) {
                swapAction = OBJECT_MENU_TYPES.get(n);
                if (swapAction == menuAction) {
                    this.swap(this.optionIndexes, menuEntries, index, menuEntries.length - 1);
                    return;
                }
            } else if (this.shiftModifier() && this.config.objectShiftClickWalkHere()) {
                menuEntry.setDeprioritized(true);
            }
        }
        if (NPC_MENU_TYPES.contains((Object)menuAction)) {
            NPC npc = menuEntry.getNpc();
            assert (npc != null);
            NPCComposition composition = npc.getTransformedComposition();
            assert (composition != null);
            Integer n = this.getNpcSwapConfig(this.shiftModifier(), composition.getId());
            if (n == null) {
                if (this.shiftModifier() && this.config.npcShiftClickWalkHere()) {
                    menuEntry.setDeprioritized(true);
                }
            } else if (n == -1) {
                menuEntry.setDeprioritized(true);
            } else {
                swapAction = NPC_MENU_TYPES.get(n);
                if (swapAction == menuAction) {
                    int i;
                    for (i = index; i < menuEntries.length - 1 && NPC_MENU_TYPES.contains((Object)menuEntries[i + 1].getType()); ++i) {
                    }
                    this.swap(this.optionIndexes, menuEntries, index, i);
                    return;
                }
            }
        }
        if ((menuAction == MenuAction.GROUND_ITEM_FIRST_OPTION || menuAction == MenuAction.GROUND_ITEM_SECOND_OPTION || menuAction == MenuAction.GROUND_ITEM_THIRD_OPTION || menuAction == MenuAction.GROUND_ITEM_FOURTH_OPTION || menuAction == MenuAction.GROUND_ITEM_FIFTH_OPTION) && this.shiftModifier() && this.config.groundItemShiftClickWalkHere()) {
            menuEntry.setDeprioritized(true);
        }
        if (!(menuAction != MenuAction.CC_OP && menuAction != MenuAction.CC_OP_LOW_PRIORITY || w == null || w.getIndex() != -1 && w.getItemId() == -1 || itemOp || WidgetInfo.TO_GROUP((int)w.getId()) == 387)) {
            String[] actions = w.getActions();
            int numActions = 0;
            for (String action : actions) {
                if (Strings.isNullOrEmpty((String)action)) continue;
                ++numActions;
            }
            if (numActions > 1) {
                int n = w.getId();
                int itemId = w.getIndex() == -1 ? -1 : ItemVariationMapping.map(w.getItemId());
                Integer op = this.getUiSwapConfig(this.shiftModifier(), n, itemId);
                if (op != null && op.intValue() == menuEntry.getIdentifier()) {
                    this.swap(this.optionIndexes, menuEntries, index, menuEntries.length - 1);
                    return;
                }
            }
        }
        if (this.swapBank(menuEntry, menuAction)) {
            return;
        }
        NPC hintArrowNpc = this.client.getHintArrowNpc();
        if (hintArrowNpc != null && hintArrowNpc.getIndex() == eventId && NPC_MENU_TYPES.contains((Object)menuAction)) {
            return;
        }
        Collection swaps = this.swaps.get((Object)option);
        Iterator iterator = swaps.iterator();
        while (!(!iterator.hasNext() || (swap = (Swap)iterator.next()).getTargetPredicate().test(target) && swap.getEnabled().get().booleanValue() && this.swap(menuEntries, swap.getSwappedOption(), target, index, swap.isStrict()))) {
        }
    }

    @Subscribe
    public void onClientTick(ClientTick clientTick) {
        if (this.client.getGameState() != GameState.LOGGED_IN || this.client.isMenuOpen()) {
            return;
        }
        MenuEntry[] menuEntries = this.client.getMenuEntries();
        int idx = 0;
        this.optionIndexes.clear();
        for (MenuEntry entry : menuEntries) {
            String option = Text.removeTags(entry.getOption()).toLowerCase();
            this.optionIndexes.put((Object)option, (Object)idx++);
        }
        idx = 0;
        for (MenuEntry entry : menuEntries) {
            this.swapMenuEntry(menuEntries, idx++, entry);
        }
        if (this.config.removeDeadNpcMenus()) {
            this.removeDeadNpcs();
        }
    }

    private void removeDeadNpcs() {
        MenuEntry[] newEntries;
        MenuEntry[] oldEntries = this.client.getMenuEntries();
        if (oldEntries.length != (newEntries = (MenuEntry[])Arrays.stream(oldEntries).filter(e -> {
            NPC npc = e.getNpc();
            return npc == null || !this.npcUtil.isDying(npc);
        }).toArray(MenuEntry[]::new)).length) {
            this.client.setMenuEntries(newEntries);
        }
    }

    @Subscribe
    public void onPostItemComposition(PostItemComposition event) {
        if (!this.config.shiftClickCustomization()) {
            return;
        }
        ItemComposition itemComposition = event.getItemComposition();
        Integer option = this.getItemSwapConfig(true, itemComposition.getId());
        if (option != null && option < itemComposition.getInventoryActions().length) {
            itemComposition.setShiftClickActionIndex(option.intValue());
        }
    }

    private boolean swap(MenuEntry[] menuEntries, String option, String target, int index, boolean strict) {
        int optionIdx = this.findIndex(menuEntries, index, option, target, strict);
        if (optionIdx >= 0) {
            this.swap(this.optionIndexes, menuEntries, optionIdx, index);
            return true;
        }
        return false;
    }

    private int findIndex(MenuEntry[] entries, int limit, String option, String target, boolean strict) {
        if (strict) {
            List indexes = this.optionIndexes.get((Object)option);
            for (int i = indexes.size() - 1; i >= 0; --i) {
                int idx = (Integer)indexes.get(i);
                MenuEntry entry = entries[idx];
                String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();
                if (idx >= limit || !entryTarget.equals(target)) continue;
                return idx;
            }
        } else {
            for (int i = limit - 1; i >= 0; --i) {
                MenuEntry entry = entries[i];
                String entryOption = Text.removeTags(entry.getOption()).toLowerCase();
                String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();
                if (!entryOption.contains(option.toLowerCase()) || !entryTarget.equals(target)) continue;
                return i;
            }
        }
        return -1;
    }

    private void swap(ArrayListMultimap<String, Integer> optionIndexes, MenuEntry[] entries, int index1, int index2) {
        MenuEntry entry2;
        if (index1 == index2) {
            return;
        }
        MenuEntry entry1 = entries[index1];
        entries[index1] = entry2 = entries[index2];
        entries[index2] = entry1;
        if (entry1.getType() == MenuAction.CC_OP_LOW_PRIORITY) {
            entry1.setType(MenuAction.CC_OP);
        }
        if (entry2.getType() == MenuAction.CC_OP_LOW_PRIORITY) {
            entry2.setType(MenuAction.CC_OP);
        }
        this.client.setMenuEntries(entries);
        String option1 = Text.removeTags(entry1.getOption()).toLowerCase();
        String option2 = Text.removeTags(entry2.getOption()).toLowerCase();
        List list1 = optionIndexes.get((Object)option1);
        List list2 = optionIndexes.get((Object)option2);
        list1.remove((Object)index1);
        list2.remove((Object)index2);
        MenuEntrySwapperPlugin.sortedInsert(list1, index2);
        MenuEntrySwapperPlugin.sortedInsert(list2, index1);
    }

    private static <T extends Comparable<? super T>> void sortedInsert(List<T> list, T value) {
        int idx = Collections.binarySearch(list, value);
        list.add(idx < 0 ? -idx - 1 : idx, value);
    }

    private boolean shiftModifier() {
        return this.client.isKeyPressed(81);
    }

    private Integer getObjectSwapConfig(boolean shift, int objectId) {
        String config = this.configManager.getConfiguration("menuentryswapper", (shift ? OBJECT_SHIFT_KEY_PREFIX : OBJECT_KEY_PREFIX) + objectId);
        if (config == null || config.isEmpty()) {
            return null;
        }
        return Integer.parseInt(config);
    }

    private void setObjectSwapConfig(boolean shift, int objectId, int index) {
        this.configManager.setConfiguration("menuentryswapper", (shift ? OBJECT_SHIFT_KEY_PREFIX : OBJECT_KEY_PREFIX) + objectId, index);
    }

    private void unsetObjectSwapConfig(boolean shift, int objectId) {
        this.configManager.unsetConfiguration("menuentryswapper", (shift ? OBJECT_SHIFT_KEY_PREFIX : OBJECT_KEY_PREFIX) + objectId);
    }

    private static MenuAction defaultAction(ObjectComposition objectComposition) {
        String[] actions = objectComposition.getActions();
        for (int i = 0; i < OBJECT_MENU_TYPES.size(); ++i) {
            if (Strings.isNullOrEmpty((String)actions[i])) continue;
            return OBJECT_MENU_TYPES.get(i);
        }
        return null;
    }

    private Integer getNpcSwapConfig(boolean shift, int npcId) {
        String config = this.configManager.getConfiguration("menuentryswapper", (shift ? NPC_SHIFT_KEY_PREFIX : NPC_KEY_PREFIX) + npcId);
        if (config == null || config.isEmpty()) {
            return null;
        }
        return Integer.parseInt(config);
    }

    private void setNpcSwapConfig(boolean shift, int npcId, int index) {
        this.configManager.setConfiguration("menuentryswapper", (shift ? NPC_SHIFT_KEY_PREFIX : NPC_KEY_PREFIX) + npcId, index);
    }

    private void unsetNpcSwapConfig(boolean shift, int npcId) {
        this.configManager.unsetConfiguration("menuentryswapper", (shift ? NPC_SHIFT_KEY_PREFIX : NPC_KEY_PREFIX) + npcId);
    }

    private static MenuAction defaultAction(NPCComposition composition) {
        String[] actions = composition.getActions();
        for (int i = 0; i < NPC_MENU_TYPES.size(); ++i) {
            if (Strings.isNullOrEmpty((String)actions[i]) || actions[i].equalsIgnoreCase("Attack")) continue;
            return NPC_MENU_TYPES.get(i);
        }
        return null;
    }

    private int defaultOp(ItemComposition itemComposition, boolean shift) {
        int shiftClickActionIndex;
        if (shift && (shiftClickActionIndex = itemComposition.getShiftClickActionIndex()) >= 0) {
            return shiftClickActionIndex;
        }
        String[] actions = itemComposition.getInventoryActions();
        for (int actionIdx = 0; actionIdx < 3; ++actionIdx) {
            if (Strings.isNullOrEmpty((String)actions[actionIdx])) continue;
            return actionIdx;
        }
        return -1;
    }

    private Integer getUiSwapConfig(boolean shift, int componentId, int itemId) {
        String config = this.configManager.getConfiguration("menuentryswapper", (shift ? UI_SHIFT_KEY_PREFIX : UI_KEY_PREFIX) + componentId + (itemId != -1 ? "_" + itemId : ""));
        if (config == null || config.isEmpty()) {
            return null;
        }
        return Integer.parseInt(config);
    }

    private void setUiSwapConfig(boolean shift, int componentId, int itemId, int op) {
        this.configManager.setConfiguration("menuentryswapper", (shift ? UI_SHIFT_KEY_PREFIX : UI_KEY_PREFIX) + componentId + (itemId != -1 ? "_" + itemId : ""), op);
    }

    private void unsetUiSwapConfig(boolean shift, int componentId, int itemId) {
        this.configManager.unsetConfiguration("menuentryswapper", (shift ? UI_SHIFT_KEY_PREFIX : UI_KEY_PREFIX) + componentId + (itemId != -1 ? "_" + itemId : ""));
    }
}

