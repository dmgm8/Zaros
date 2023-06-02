/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.menuentryswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.menuentryswapper.BuyMode;
import net.runelite.client.plugins.menuentryswapper.FairyRingMode;
import net.runelite.client.plugins.menuentryswapper.GEItemCollectMode;
import net.runelite.client.plugins.menuentryswapper.HouseMode;
import net.runelite.client.plugins.menuentryswapper.SellMode;
import net.runelite.client.plugins.menuentryswapper.ShiftDepositMode;
import net.runelite.client.plugins.menuentryswapper.ShiftWithdrawMode;

@ConfigGroup(value="menuentryswapper")
public interface MenuEntrySwapperConfig
extends Config {
    public static final String GROUP = "menuentryswapper";
    @ConfigSection(name="Item Swaps", description="All options that swap item menu entries", position=0, closedByDefault=true)
    public static final String itemSection = "items";
    @ConfigSection(name="NPC Swaps", description="All options that swap NPC menu entries", position=1, closedByDefault=true)
    public static final String npcSection = "npcs";
    @ConfigSection(name="Object Swaps", description="All options that swap object menu entries", position=2, closedByDefault=true)
    public static final String objectSection = "objects";
    @ConfigSection(name="UI Swaps", description="All options that swap entries in the UI (except Items)", position=3, closedByDefault=true)
    public static final String uiSection = "ui";
    @ConfigSection(name="Ground Item Swaps", description="All options that swap ground item menu entries", position=4, closedByDefault=true)
    public static final String groundItemSection = "groundItems";

    @ConfigItem(position=-3, keyName="leftClickCustomization", name="Customizable left-click", description="Allows customization of left-clicks on items", section="items")
    default public boolean leftClickCustomization() {
        return true;
    }

    @ConfigItem(position=-2, keyName="shiftClickCustomization", name="Customizable shift-click", description="Allows customization of shift-clicks on items", section="items")
    default public boolean shiftClickCustomization() {
        return true;
    }

    @ConfigItem(position=-2, keyName="objectLeftClickCustomization", name="Customizable left and shift click", description="Allows customization of left-clicks on objects", section="objects")
    default public boolean objectCustomization() {
        return true;
    }

    @ConfigItem(position=-2, keyName="objectShiftClickWalkHere", name="Shift click Walk here", description="Swaps Walk here on shift click on all objects", section="objects")
    default public boolean objectShiftClickWalkHere() {
        return false;
    }

    @ConfigItem(position=-3, keyName="npcLeftClickCustomization", name="Customizable left and shift click", description="Allows customization of left-clicks on NPCs", section="npcs")
    default public boolean npcCustomization() {
        return true;
    }

    @ConfigItem(position=-2, keyName="npcShiftClickWalkHere", name="Shift click Walk here", description="Swaps Walk here on shift click on all NPCs", section="npcs")
    default public boolean npcShiftClickWalkHere() {
        return false;
    }

    @ConfigItem(keyName="swapAdmire", name="Admire", description="Swap Admire with Teleport, Spellbook and Perks (max cape) for mounted skill capes.", section="objects")
    default public boolean swapAdmire() {
        return true;
    }

    @ConfigItem(keyName="swapAssignment", name="Assignment", description="Swap Talk-to with Assignment for Slayer Masters. This will take priority over swapping Trade.", section="npcs")
    default public boolean swapAssignment() {
        return true;
    }

    @ConfigItem(keyName="swapBanker", name="Bank", description="Swap Talk-to with Bank on Bank NPC<br>Example: Banker", section="npcs")
    default public boolean swapBank() {
        return true;
    }

    @ConfigItem(keyName="swapBirdhouseEmpty", name="Birdhouse", description="Swap Interact with Empty for birdhouses on Fossil Island", section="objects")
    default public boolean swapBirdhouseEmpty() {
        return true;
    }

    @ConfigItem(keyName="swapBones", name="Bury", description="Swap Bury with Use on Bones", section="items")
    default public boolean swapBones() {
        return false;
    }

    @ConfigItem(keyName="swapHerbs", name="Clean", description="Swap Clean with Use on Herbs", section="items")
    default public boolean swapHerbs() {
        return false;
    }

    @ConfigItem(keyName="swapChase", name="Chase", description="Allows to left click your cat to chase", section="npcs")
    default public boolean swapChase() {
        return true;
    }

    @ConfigItem(keyName="swapExchange", name="Exchange", description="Swap Talk-to with Exchange on NPC<br>Example: Grand Exchange Clerk, Tool Leprechaun, Void Knight", section="npcs")
    default public boolean swapExchange() {
        return true;
    }

    @ConfigItem(keyName="swapFairyRing", name="Fairy ring", description="Swap Zanaris with Last-destination or Configure on Fairy rings", section="objects")
    default public FairyRingMode swapFairyRing() {
        return FairyRingMode.LAST_DESTINATION;
    }

    @ConfigItem(keyName="swapHarpoon", name="Harpoon", description="Swap Cage, Big Net with Harpoon on Fishing spot", section="objects")
    default public boolean swapHarpoon() {
        return false;
    }

    @ConfigItem(keyName="swapBait", name="Bait", description="Swap Lure, Small Net with Bait on Fishing spot", section="objects")
    default public boolean swapBait() {
        return false;
    }

    @ConfigItem(keyName="swapHelp", name="Help", description="Swap Talk-to with Help on Arceuus library customers", section="npcs")
    default public boolean swapHelp() {
        return true;
    }

    @ConfigItem(keyName="swapHomePortal", name="Home", description="Swap Enter with Home or Build or Friend's house on Portal", section="objects")
    default public HouseMode swapHomePortal() {
        return HouseMode.HOME;
    }

    @ConfigItem(keyName="swapPay", name="Pay", description="Swap Talk-to with Pay on NPC<br>Example: Elstan, Heskel, Fayeth", section="npcs")
    default public boolean swapPay() {
        return true;
    }

    @ConfigItem(keyName="swapJewelleryBox", name="Jewellery Box", description="Swap Teleport Menu with previous destination on Jewellery Box", section="objects")
    default public boolean swapJewelleryBox() {
        return false;
    }

    @ConfigItem(keyName="swapPortalNexus", name="Portal Nexus", description="Swap Teleport options with Teleport Menu on the Portal Nexus", section="objects")
    default public boolean swapPortalNexus() {
        return false;
    }

    @ConfigItem(keyName="swapPrivate", name="Private", description="Swap Shared with Private on the Chambers of Xeric storage units.", section="objects")
    default public boolean swapPrivate() {
        return false;
    }

    @ConfigItem(keyName="swapPick", name="Pick", description="Swap Pick with Pick-lots of the Gourd tree in the Chambers of Xeric", section="objects")
    default public boolean swapPick() {
        return false;
    }

    @ConfigItem(keyName="swapQuick", name="Quick Pass/Open/Start/Travel", description="Swap Pass with Quick-Pass, Open with Quick-Open, Ring with Quick-Start and Talk-to with Quick-Travel", section="objects")
    default public boolean swapQuick() {
        return true;
    }

    @ConfigItem(keyName="swapBoxTrap", name="Reset", description="Swap Check with Reset on box trap", section="objects")
    default public boolean swapBoxTrap() {
        return true;
    }

    @ConfigItem(keyName="swapTeleportItem", name="Teleport item", description="Swap Wear, Wield with Rub, Teleport on teleport item<br>Example: Amulet of glory, Explorer's ring, Chronicle", section="items")
    default public boolean swapTeleportItem() {
        return false;
    }

    @ConfigItem(keyName="swapTeleToPoh", name="Tele to POH", description="Swap Wear with Tele to POH on the construction cape", section="items")
    default public boolean swapTeleToPoh() {
        return false;
    }

    @ConfigItem(keyName="swapKaramjaGloves", name="Karamja Gloves", description="Swap Wear with the Gem Mine or Duradel teleport on the Karamja Gloves 3 and 4", section="items")
    default public KaramjaGlovesMode swapKaramjaGlovesMode() {
        return KaramjaGlovesMode.WEAR;
    }

    @ConfigItem(keyName="swapArdougneCloak", name="Ardougne Cloak", description="Swap Wear with Monastery Teleport or Farm Teleport on the Ardougne cloak.", section="items")
    default public ArdougneCloakMode swapArdougneCloakMode() {
        return ArdougneCloakMode.WEAR;
    }

    @ConfigItem(keyName="swapRadasBlessing", name="Rada's Blessing", description="Swap Equip with the Woodland or Mount Karuulm teleport on Rada's Blessing.", section="items")
    default public RadasBlessingMode swapRadasBlessingMode() {
        return RadasBlessingMode.EQUIP;
    }

    @ConfigItem(keyName="swapMorytaniaLegs", name="Morytania Legs", description="Swap Wear with the Ectofunctus or Burgh de Rott teleport on the Morytania Legs.", section="items")
    default public MorytaniaLegsMode swapMorytaniaLegsMode() {
        return MorytaniaLegsMode.WEAR;
    }

    @ConfigItem(keyName="swapDesertAmulet", name="Desert Amulet", description="Swap Wear with the Nardah or Kalphite Cave teleport on Desert Amulet 4.", section="items")
    default public DesertAmuletMode swapDesertAmuletMode() {
        return DesertAmuletMode.WEAR;
    }

    @ConfigItem(keyName="swapAbyssTeleport", name="Teleport to Abyss", description="Swap Talk-to with Teleport for the Mage of Zamorak", section="npcs")
    default public boolean swapAbyssTeleport() {
        return true;
    }

    @ConfigItem(keyName="swapTrade", name="Trade", description="Swap Talk-to with Trade on NPC<br>Example: Shop keeper, Shop assistant", section="npcs")
    default public boolean swapTrade() {
        return true;
    }

    @ConfigItem(keyName="swapTravel", name="Travel", description="Swap Talk-to with Travel, Take-boat, Pay-fare, Charter on NPC<br>Example: Squire, Monk of Entrana, Customs officer, Trader Crewmember", section="npcs")
    default public boolean swapTravel() {
        return true;
    }

    @ConfigItem(keyName="swapGEItemCollect", name="GE Item Collect", description="Swap Collect-notes, Collect-items, or Bank options from GE offer", section="ui")
    default public GEItemCollectMode swapGEItemCollect() {
        return GEItemCollectMode.DEFAULT;
    }

    @ConfigItem(keyName="swapGEAbort", name="GE Abort", description="Swap abort offer on Grand Exchange offers when shift-clicking", section="ui")
    default public boolean swapGEAbort() {
        return false;
    }

    @ConfigItem(keyName="bankWithdrawShiftClick", name="Bank Withdraw Shift-Click", description="Swaps the behavior of shift-click when withdrawing from bank.", section="ui")
    default public ShiftWithdrawMode bankWithdrawShiftClick() {
        return ShiftWithdrawMode.OFF;
    }

    @ConfigItem(keyName="bankDepositShiftClick", name="Bank Deposit Shift-Click", description="Swaps the behavior of shift-click when depositing to bank.", section="ui")
    default public ShiftDepositMode bankDepositShiftClick() {
        return ShiftDepositMode.OFF;
    }

    @ConfigItem(keyName="shopBuy", name="Shop Buy Shift-Click", description="Swaps the Buy options with Value on items in shops when shift is held.", section="ui")
    default public BuyMode shopBuy() {
        return BuyMode.OFF;
    }

    @ConfigItem(keyName="shopSell", name="Shop Sell Shift-Click", description="Swaps the Sell options with Value on items in your inventory when selling to shops when shift is held.", section="ui")
    default public SellMode shopSell() {
        return SellMode.OFF;
    }

    @ConfigItem(keyName="swapEssenceMineTeleport", name="Essence Mine Teleport", description="Swaps Talk-To with Teleport for NPCs which teleport you to the essence mine", section="npcs")
    default public boolean swapEssenceMineTeleport() {
        return false;
    }

    @ConfigItem(keyName="swapTan", name="Tan", description="Swap Tan 1 with Tan All", section="ui")
    default public boolean swapTan() {
        return false;
    }

    @ConfigItem(keyName="swapDepositItems", name="Deposit Items", description="Swap Talk-to with Deposit-items", section="npcs")
    default public boolean swapDepositItems() {
        return false;
    }

    @ConfigItem(keyName="swapStairsLeftClick", name="Stairs left-click", description="Swap this option when left-clicking stairs. Also works on ladders.", section="objects")
    default public StairsMode swapStairsLeftClick() {
        return StairsMode.CLIMB;
    }

    @ConfigItem(keyName="swapStairsShiftClick", name="Stairs shift-click", description="Swap this option when shift-clicking stairs. Also works on ladders.", section="objects")
    default public StairsMode swapStairsShiftClick() {
        return StairsMode.CLIMB;
    }

    @ConfigItem(keyName="swapTemporossLeave", name="Tempoross Leave", description="Swap Talk-to with Leave after subduing Tempoross", section="npcs")
    default public boolean swapTemporossLeave() {
        return false;
    }

    @ConfigItem(keyName="removeDeadNpcMenus", name="Remove dead npc menus", description="Remove menu options such as Attack and Talk-to from dead npcs", section="npcs")
    default public boolean removeDeadNpcMenus() {
        return false;
    }

    @ConfigItem(position=-1, keyName="groundItemShiftClickWalkHere", name="Shift click Walk here", description="Swaps Walk here on shift click on all ground items", section="groundItems")
    default public boolean groundItemShiftClickWalkHere() {
        return false;
    }

    public static enum StairsMode {
        CLIMB,
        CLIMB_UP,
        CLIMB_DOWN;

    }

    public static enum DesertAmuletMode {
        WEAR,
        NARDAH,
        KALPHITE_CAVE;

    }

    public static enum RadasBlessingMode {
        EQUIP,
        KOUREND_WOODLAND,
        MOUNT_KARUULM;

    }

    public static enum MorytaniaLegsMode {
        WEAR,
        ECTOFUNTUS,
        BURGH_DE_ROTT;


        public String toString() {
            switch (this) {
                case BURGH_DE_ROTT: {
                    return "Burgh de Rott";
                }
            }
            return this.name();
        }
    }

    public static enum KaramjaGlovesMode {
        WEAR,
        GEM_MINE,
        DURADEL;

    }

    public static enum ArdougneCloakMode {
        WEAR,
        MONASTERY,
        FARM;

    }
}

