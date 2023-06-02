/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.runelite.api.Actor
 *  net.runelite.api.EquipmentInventorySlot
 *  net.runelite.api.Item
 *  net.runelite.api.NPC
 *  net.runelite.api.Point
 *  net.runelite.api.TileObject
 */
package net.runelite.client.plugins.cluescrolls.clues;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.Actor;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.ClueScrollWorldOverlay;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.NamedObjectClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.NpcClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.item.AnyRequirementCollection;
import net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirement;
import net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirements;
import net.runelite.client.plugins.cluescrolls.clues.item.SingleItemRequirement;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class SkillChallengeClue
extends ClueScroll
implements NpcClueScroll,
NamedObjectClueScroll {
    private static final AnyRequirementCollection ANY_PICKAXE = ItemRequirements.any("Any Pickaxe", ItemRequirements.item(1265), ItemRequirements.item(1267), ItemRequirements.item(1269), ItemRequirements.item(12297), ItemRequirements.item(1273), ItemRequirements.item(1271), ItemRequirements.item(1275), ItemRequirements.item(11920), ItemRequirements.item(12797), ItemRequirements.item(23677), ItemRequirements.item(25376), ItemRequirements.item(13243), ItemRequirements.item(25063), ItemRequirements.item(13244), ItemRequirements.item(25369), ItemRequirements.item(23276), ItemRequirements.item(20014), ItemRequirements.item(23680), ItemRequirements.item(23682));
    private static final AnyRequirementCollection ANY_AXE = ItemRequirements.any("Any Axe", ItemRequirements.item(1351), ItemRequirements.item(1349), ItemRequirements.item(1353), ItemRequirements.item(1361), ItemRequirements.item(1355), ItemRequirements.item(1357), ItemRequirements.item(1359), ItemRequirements.item(6739), ItemRequirements.item(25378), ItemRequirements.item(13241), ItemRequirements.item(25066), ItemRequirements.item(13242), ItemRequirements.item(25371), ItemRequirements.item(23279), ItemRequirements.item(20011), ItemRequirements.item(23673), ItemRequirements.item(23675));
    private static final AnyRequirementCollection ANY_HARPOON = ItemRequirements.any("Harpoon", ItemRequirements.item(311), ItemRequirements.item(10129), ItemRequirements.item(21028), ItemRequirements.item(25373), ItemRequirements.item(21031), ItemRequirements.item(25059), ItemRequirements.item(21033), ItemRequirements.item(25367), ItemRequirements.item(23762), ItemRequirements.item(23764));
    private static final AnyRequirementCollection ANY_HAMMER = ItemRequirements.any("Hammer", ItemRequirements.item(2347), ItemRequirements.item(25644));
    private static final List<SkillChallengeClue> CLUES = ImmutableList.of((Object)new SkillChallengeClue("Cook a Pike", "i need to cook charlie a pike.", "i need to take the cooked pike to charlie.", ItemRequirements.item(351), ItemRequirements.item(349)), (Object)new SkillChallengeClue("Cook a Trout", "i need to cook charlie a trout.", "i need to take the cooked trout to charlie.", ItemRequirements.item(333), ItemRequirements.item(335)), (Object)new SkillChallengeClue("Craft a Leather Body", "i need to craft charlie a leather body.", "i need to take the leather body i crafted to charlie.", ItemRequirements.item(1129), ItemRequirements.item(1741), ItemRequirements.item(1733), ItemRequirements.item(1734)), (Object)new SkillChallengeClue("Craft some Leather Chaps", "i need to craft charlie some leather chaps.", "i need to take the leather chaps i crafted to charlie.", ItemRequirements.item(1095), ItemRequirements.item(1741), ItemRequirements.item(1733), ItemRequirements.item(1734)), (Object)new SkillChallengeClue("Fish a Herring", "i need to fish charlie a herring.", "i need to take a raw herring to charlie.", ItemRequirements.item(345), ItemRequirements.any("Fishing rod", ItemRequirements.item(307), ItemRequirements.item(22846)), ItemRequirements.item(313)), (Object)new SkillChallengeClue("Fish a Trout", "i need to fish charlie a trout.", "i need to take a raw trout to charlie.", ItemRequirements.item(335), ItemRequirements.any("Fly fishing rod", ItemRequirements.item(309), ItemRequirements.item(22844)), ItemRequirements.item(314)), (Object)new SkillChallengeClue("Mine a piece of Iron Ore", "i need to mine charlie a piece of iron ore from an iron vein.", "i need to take the iron ore to charlie.", ItemRequirements.item(440), ANY_PICKAXE), (Object)new SkillChallengeClue("Smith an Iron Dagger", "i need to smith charlie one iron dagger.", "i need to take the iron dagger i smithed to charlie.", ItemRequirements.item(1203), ItemRequirements.item(2351), ANY_HAMMER), (Object)new SkillChallengeClue("Equip a Dragon Scimitar.", true, ItemRequirements.any("Any Dragon Scimitar", ItemRequirements.item(4587), ItemRequirements.item(20000))), (Object)new SkillChallengeClue("Enchant some Dragonstone Jewellery.", "enchant a piece of dragonstone jewellery.", ItemRequirements.xOfItem(564, 1), ItemRequirements.any("Water Rune x15", ItemRequirements.xOfItem(555, 15), ItemRequirements.xOfItem(4695, 15), ItemRequirements.xOfItem(4698, 15), ItemRequirements.xOfItem(4694, 15), ItemRequirements.item(1383), ItemRequirements.item(1395), ItemRequirements.item(1403), ItemRequirements.item(6562), ItemRequirements.item(6563), ItemRequirements.item(20730), ItemRequirements.item(20733), ItemRequirements.item(11787), ItemRequirements.item(11789), ItemRequirements.item(12795), ItemRequirements.item(12796), ItemRequirements.item(21006), ItemRequirements.item(25574)), ItemRequirements.any("Earth Rune x15", ItemRequirements.xOfItem(557, 15), ItemRequirements.xOfItem(4696, 15), ItemRequirements.xOfItem(4698, 15), ItemRequirements.xOfItem(4699, 15), ItemRequirements.item(1385), ItemRequirements.item(1399), ItemRequirements.item(1407), ItemRequirements.item(6562), ItemRequirements.item(6563), ItemRequirements.item(20736), ItemRequirements.item(20739), ItemRequirements.item(3053), ItemRequirements.item(3054), ItemRequirements.item(21198), ItemRequirements.item(21200)), ItemRequirements.any("Unenchanted Dragonstone Jewellery", ItemRequirements.item(1645), ItemRequirements.item(1664), ItemRequirements.item(11115), ItemRequirements.item(1702))), (Object)new SkillChallengeClue("Craft a nature rune.", ItemRequirements.any("Pure essence or Daeyalt essence", ItemRequirements.item(7936), ItemRequirements.item(24704))), (Object)new SkillChallengeClue("Catch a mottled eel with aerial fishing in Lake Molch.", ItemRequirements.any("Fish chunks or King worms", ItemRequirements.item(22818), ItemRequirements.item(2162)), ItemRequirements.emptySlot("No Gloves", EquipmentInventorySlot.GLOVES), ItemRequirements.any("No Weapon", ItemRequirements.emptySlot("", EquipmentInventorySlot.WEAPON), ItemRequirements.item(22816), ItemRequirements.item(22817)), ItemRequirements.emptySlot("No Shield", EquipmentInventorySlot.SHIELD)), (Object[])new SkillChallengeClue[]{new SkillChallengeClue("Score a goal in skullball.", true, ItemRequirements.any("Ring of Charos", ItemRequirements.item(4202), ItemRequirements.item(6465))), new SkillChallengeClue("Complete a lap of Ape atoll agility course.", true, ItemRequirements.any("Ninja Monkey Greegree", ItemRequirements.item(4024), ItemRequirements.item(4025), ItemRequirements.item(19525))), new SkillChallengeClue("Create a super defence potion.", ItemRequirements.item(107), ItemRequirements.item(239)), new SkillChallengeClue("Steal from a chest in Ardougne Castle.", new ItemRequirement[0]), new SkillChallengeClue("Craft a green dragonhide body.", ItemRequirements.xOfItem(1745, 3), ItemRequirements.item(1733), ItemRequirements.item(1734)), new SkillChallengeClue("String a yew longbow.", ItemRequirements.item(66), ItemRequirements.item(1777)), new SkillChallengeClue("Kill a Dust Devil.", "slay a dust devil.", true, ItemRequirements.any("Facemask or Slayer Helmet", ItemRequirements.item(4164), ItemRequirements.item(11864), ItemRequirements.item(11865), ItemRequirements.item(19639), ItemRequirements.item(19641), ItemRequirements.item(21264), ItemRequirements.item(21266), ItemRequirements.item(19647), ItemRequirements.item(19649), ItemRequirements.item(19643), ItemRequirements.item(19645), ItemRequirements.item(21888), ItemRequirements.item(21890), ItemRequirements.item(23073), ItemRequirements.item(23075), ItemRequirements.item(24370), ItemRequirements.item(24444), ItemRequirements.item(25898), ItemRequirements.item(25900), ItemRequirements.item(25904), ItemRequirements.item(25906), ItemRequirements.item(25910), ItemRequirements.item(25912), ItemRequirements.item(25177), ItemRequirements.item(25179), ItemRequirements.item(25181), ItemRequirements.item(25183), ItemRequirements.item(25185), ItemRequirements.item(25187), ItemRequirements.item(25189), ItemRequirements.item(25191), ItemRequirements.item(25902), ItemRequirements.item(25908), ItemRequirements.item(25914))), new SkillChallengeClue("Catch a black warlock.", ItemRequirements.item(10012), ItemRequirements.any("Butterfly Net", ItemRequirements.item(10010), ItemRequirements.item(11259))), new SkillChallengeClue("Catch a red chinchompa.", ItemRequirements.item(10008)), new SkillChallengeClue("Mine a mithril ore.", ANY_PICKAXE), new SkillChallengeClue("Smith a mithril 2h sword.", ANY_HAMMER, ItemRequirements.xOfItem(2359, 3)), new SkillChallengeClue("Catch a raw shark.", ANY_HARPOON), new SkillChallengeClue("Cut a yew log.", ANY_AXE), new SkillChallengeClue("Fix a magical lamp in Dorgesh-Kaan.", new String[]{"Broken lamp"}, new int[]{10834, 10835}, ItemRequirements.item(10973)), new SkillChallengeClue("Burn a yew log.", ItemRequirements.item(1515), ItemRequirements.item(590)), new SkillChallengeClue("Cook a swordfish", "cook a swordfish", ItemRequirements.item(371)), new SkillChallengeClue("Craft multiple cosmic runes from a single essence.", ItemRequirements.any("Pure essence or Daeyalt essence", ItemRequirements.item(7936), ItemRequirements.item(24704))), new SkillChallengeClue("Plant a watermelon seed.", ItemRequirements.item(5341), ItemRequirements.item(5343), ItemRequirements.xOfItem(5321, 3)), new SkillChallengeClue("Activate the Chivalry prayer.", new ItemRequirement[0]), new SkillChallengeClue("Hand in a Tier 2 or higher set of Shayzien supply armour. (Requires 11 lovakite bars)", "take the lovakengj armourers a boxed set of shayzien supply armour at tier 2 or above.", ItemRequirements.any("Shayzien Supply Set (Tier 2 or higher)", ItemRequirements.item(13566), ItemRequirements.item(13567), ItemRequirements.item(13568), ItemRequirements.item(13569))), new SkillChallengeClue("Equip an abyssal whip in front of the abyssal demons of the Slayer Tower.", true, ItemRequirements.any("Abyssal Whip", ItemRequirements.item(4151), ItemRequirements.item(12774), ItemRequirements.item(12773), ItemRequirements.item(26482))), new SkillChallengeClue("Smith a runite med helm.", ANY_HAMMER, ItemRequirements.item(2363)), new SkillChallengeClue("Teleport to a spirit tree you planted yourself.", new ItemRequirement[0]), new SkillChallengeClue("Create a Barrows teleport tablet.", ItemRequirements.item(13446), ItemRequirements.xOfItem(565, 1), ItemRequirements.xOfItem(563, 2), ItemRequirements.xOfItem(566, 2)), new SkillChallengeClue("Kill a Nechryael in the Slayer Tower.", "slay a nechryael in the slayer tower.", new ItemRequirement[0]), new SkillChallengeClue("Kill a Spiritual Mage while wearing something from their god.", "kill the spiritual, magic and godly whilst representing their own god.", new ItemRequirement[0]), new SkillChallengeClue("Create an unstrung dragonstone amulet at a furnace.", ItemRequirements.item(2357), ItemRequirements.item(1615), ItemRequirements.item(1595)), new SkillChallengeClue("Burn a magic log.", ItemRequirements.item(1513), ItemRequirements.item(590)), new SkillChallengeClue("Burn a redwood log.", ItemRequirements.item(19669), ItemRequirements.item(590)), new SkillChallengeClue("Complete a lap of Rellekka's Rooftop Agility Course", "complete a lap of the rellekka rooftop agility course whilst sporting the finest amount of grace.", true, ItemRequirements.all("A full Graceful set", ItemRequirements.any("", ItemRequirements.item(11850), ItemRequirements.item(11851), ItemRequirements.item(13579), ItemRequirements.item(13580), ItemRequirements.item(13591), ItemRequirements.item(13592), ItemRequirements.item(13603), ItemRequirements.item(13604), ItemRequirements.item(13615), ItemRequirements.item(13616), ItemRequirements.item(13627), ItemRequirements.item(13628), ItemRequirements.item(13667), ItemRequirements.item(13668), ItemRequirements.item(21061), ItemRequirements.item(21063), ItemRequirements.item(24743), ItemRequirements.item(24745), ItemRequirements.item(25069), ItemRequirements.item(25071)), ItemRequirements.any("", ItemRequirements.item(11852), ItemRequirements.item(11853), ItemRequirements.item(13581), ItemRequirements.item(13582), ItemRequirements.item(13593), ItemRequirements.item(13594), ItemRequirements.item(13605), ItemRequirements.item(13606), ItemRequirements.item(13617), ItemRequirements.item(13618), ItemRequirements.item(13629), ItemRequirements.item(13630), ItemRequirements.item(13669), ItemRequirements.item(13670), ItemRequirements.item(21064), ItemRequirements.item(21066), ItemRequirements.item(24746), ItemRequirements.item(24748), ItemRequirements.item(25072), ItemRequirements.item(25074), ItemRequirements.item(9771), ItemRequirements.item(13340), ItemRequirements.item(9772), ItemRequirements.item(13341), ItemRequirements.item(13280), ItemRequirements.item(13342)), ItemRequirements.any("", ItemRequirements.item(11854), ItemRequirements.item(11855), ItemRequirements.item(13583), ItemRequirements.item(13584), ItemRequirements.item(13595), ItemRequirements.item(13596), ItemRequirements.item(13607), ItemRequirements.item(13608), ItemRequirements.item(13619), ItemRequirements.item(13620), ItemRequirements.item(13631), ItemRequirements.item(13632), ItemRequirements.item(13671), ItemRequirements.item(13672), ItemRequirements.item(21067), ItemRequirements.item(21069), ItemRequirements.item(24749), ItemRequirements.item(24751), ItemRequirements.item(25075), ItemRequirements.item(25077)), ItemRequirements.any("", ItemRequirements.item(11856), ItemRequirements.item(11857), ItemRequirements.item(13585), ItemRequirements.item(13586), ItemRequirements.item(13597), ItemRequirements.item(13598), ItemRequirements.item(13609), ItemRequirements.item(13610), ItemRequirements.item(13621), ItemRequirements.item(13622), ItemRequirements.item(13633), ItemRequirements.item(13634), ItemRequirements.item(13673), ItemRequirements.item(13674), ItemRequirements.item(21070), ItemRequirements.item(21072), ItemRequirements.item(24752), ItemRequirements.item(24754), ItemRequirements.item(25078), ItemRequirements.item(25080)), ItemRequirements.any("", ItemRequirements.item(11858), ItemRequirements.item(11859), ItemRequirements.item(13587), ItemRequirements.item(13588), ItemRequirements.item(13599), ItemRequirements.item(13600), ItemRequirements.item(13611), ItemRequirements.item(13612), ItemRequirements.item(13623), ItemRequirements.item(13624), ItemRequirements.item(13635), ItemRequirements.item(13636), ItemRequirements.item(13675), ItemRequirements.item(13676), ItemRequirements.item(21073), ItemRequirements.item(21075), ItemRequirements.item(24755), ItemRequirements.item(24757), ItemRequirements.item(25081), ItemRequirements.item(25083)), ItemRequirements.any("", ItemRequirements.item(11860), ItemRequirements.item(11861), ItemRequirements.item(13589), ItemRequirements.item(13590), ItemRequirements.item(13601), ItemRequirements.item(13602), ItemRequirements.item(13613), ItemRequirements.item(13614), ItemRequirements.item(13625), ItemRequirements.item(13626), ItemRequirements.item(13637), ItemRequirements.item(13638), ItemRequirements.item(13677), ItemRequirements.item(13678), ItemRequirements.item(21076), ItemRequirements.item(21078), ItemRequirements.item(24758), ItemRequirements.item(24760), ItemRequirements.item(25084), ItemRequirements.item(25086)))), new SkillChallengeClue("Mix an anti-venom potion.", ItemRequirements.item(5952), ItemRequirements.xOfItem(12934, 20)), new SkillChallengeClue("Mine a piece of Runite ore", "mine a piece of runite ore whilst sporting the finest mining gear.", true, ANY_PICKAXE, ItemRequirements.all("Prospector kit", ItemRequirements.any("", ItemRequirements.item(12013), ItemRequirements.item(25549)), ItemRequirements.any("", ItemRequirements.item(12014), ItemRequirements.item(13107), ItemRequirements.item(25551)), ItemRequirements.any("", ItemRequirements.item(12015), ItemRequirements.item(25553)), ItemRequirements.any("", ItemRequirements.item(12016), ItemRequirements.item(25555)))), new SkillChallengeClue("Steal a gem from the Ardougne market.", new ItemRequirement[0]), new SkillChallengeClue("Pickpocket an elf.", new ItemRequirement[0]), new SkillChallengeClue("Bind a blood rune at the blood altar.", ItemRequirements.item(7938)), new SkillChallengeClue("Create a ranging mix potion.", "mix a ranging mix potion.", ItemRequirements.item(171), ItemRequirements.item(11326)), new SkillChallengeClue("Fletch a rune dart.", ItemRequirements.item(824), ItemRequirements.item(314)), new SkillChallengeClue("Cremate a set of fiyr remains.", ItemRequirements.any("Magic or Redwood Pyre Logs", ItemRequirements.item(3448), ItemRequirements.item(19672)), ItemRequirements.item(590), ItemRequirements.item(3404)), new SkillChallengeClue("Dissect a sacred eel.", ItemRequirements.item(946), ItemRequirements.any("Fishing rod", ItemRequirements.item(307), ItemRequirements.item(22846)), ItemRequirements.item(313)), new SkillChallengeClue("Kill a lizardman shaman.", new ItemRequirement[0]), new SkillChallengeClue("Catch an Anglerfish.", "angle for an anglerfish whilst sporting the finest fishing gear.", true, ItemRequirements.any("Fishing rod", ItemRequirements.item(307), ItemRequirements.item(22846)), ItemRequirements.item(13431), ItemRequirements.all("Angler's outfit", ItemRequirements.any("", ItemRequirements.item(13258), ItemRequirements.item(25592)), ItemRequirements.any("", ItemRequirements.item(13259), ItemRequirements.item(25594)), ItemRequirements.any("", ItemRequirements.item(13260), ItemRequirements.item(25596)), ItemRequirements.any("", ItemRequirements.item(13261), ItemRequirements.item(25598)))), new SkillChallengeClue("Chop a redwood log.", "chop a redwood log whilst sporting the finest lumberjack gear.", true, ANY_AXE, ItemRequirements.all("Lumberjack outfit", ItemRequirements.item(10941), ItemRequirements.item(10939), ItemRequirements.item(10940), ItemRequirements.item(10933))), new SkillChallengeClue("Craft a light orb in the Dorgesh-Kaan bank.", ItemRequirements.item(10981), ItemRequirements.item(10980)), new SkillChallengeClue("Kill a reanimated Abyssal Demon.", "kill a reanimated abyssal.", ItemRequirements.xOfItem(566, 4), ItemRequirements.xOfItem(565, 2), ItemRequirements.any("Nature Rune x4", ItemRequirements.xOfItem(561, 4), ItemRequirements.item(22370)), ItemRequirements.range("Ensouled abyssal head", 13507, 13508)), new SkillChallengeClue("Kill a Fiyr shade inside Mort'tons shade catacombs.", ItemRequirements.any("Any Gold or Silver Shade Key", ItemRequirements.item(25424), ItemRequirements.item(25426), ItemRequirements.item(25428), ItemRequirements.item(25430), ItemRequirements.item(25432), ItemRequirements.item(3465), ItemRequirements.item(3466), ItemRequirements.item(3467), ItemRequirements.item(3468), ItemRequirements.item(3469)))});
    private final ChallengeType type;
    private final String challenge;
    private final String rawChallenge;
    private final String returnText;
    private final ItemRequirement[] itemRequirements;
    private final SingleItemRequirement returnItem;
    private final boolean requireEquip;
    private final String[] objectNames;
    private final int[] objectRegions;
    private boolean challengeCompleted;

    private SkillChallengeClue(String challenge, String rawChallenge, String returnText, SingleItemRequirement returnItem, ItemRequirement ... itemRequirements) {
        this.type = ChallengeType.CHARLIE;
        this.challenge = challenge;
        this.rawChallenge = rawChallenge;
        this.returnText = returnText;
        this.itemRequirements = itemRequirements;
        this.returnItem = returnItem;
        this.challengeCompleted = false;
        this.requireEquip = false;
        this.objectNames = new String[0];
        this.objectRegions = null;
    }

    private SkillChallengeClue(String challenge, ItemRequirement ... itemRequirements) {
        this(challenge, challenge.toLowerCase(), itemRequirements);
    }

    private SkillChallengeClue(String challenge, String[] objectNames, int[] objectRegions, ItemRequirement ... itemRequirements) {
        this(challenge, challenge.toLowerCase(), false, objectNames, objectRegions, itemRequirements);
    }

    private SkillChallengeClue(String challenge, boolean requireEquip, ItemRequirement ... itemRequirements) {
        this(challenge, challenge.toLowerCase(), requireEquip, new String[0], null, itemRequirements);
    }

    private SkillChallengeClue(String challenge, String rawChallenge, ItemRequirement ... itemRequirements) {
        this(challenge, rawChallenge, false, new String[0], null, itemRequirements);
    }

    private SkillChallengeClue(String challenge, String rawChallenge, boolean requireEquip, ItemRequirement ... itemRequirements) {
        this(challenge, rawChallenge, requireEquip, new String[0], null, itemRequirements);
    }

    private SkillChallengeClue(String challenge, String rawChallenge, boolean requireEquip, String[] objectNames, int[] objectRegions, ItemRequirement ... itemRequirements) {
        this.type = ChallengeType.SHERLOCK;
        this.challenge = challenge;
        this.rawChallenge = rawChallenge;
        this.itemRequirements = itemRequirements;
        this.challengeCompleted = false;
        this.requireEquip = requireEquip;
        this.objectNames = objectNames;
        this.objectRegions = objectRegions;
        this.returnText = "<str>" + rawChallenge + "</str>";
        this.returnItem = null;
    }

    @Override
    public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
        block4: {
            block3: {
                panelComponent.getChildren().add(TitleComponent.builder().text("Skill Challenge Clue").build());
                if (this.challengeCompleted) break block3;
                panelComponent.getChildren().add(LineComponent.builder().left("Challenge:").build());
                panelComponent.getChildren().add(LineComponent.builder().left(this.challenge).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
                if (this.itemRequirements.length <= 0) break block4;
                panelComponent.getChildren().add(LineComponent.builder().left(this.requireEquip ? "Equipment:" : "Items Required:").build());
                for (LineComponent line : SkillChallengeClue.getRequirements(plugin, this.requireEquip, this.itemRequirements)) {
                    panelComponent.getChildren().add(line);
                }
                break block4;
            }
            panelComponent.getChildren().add(LineComponent.builder().left("NPC:").build());
            panelComponent.getChildren().add(LineComponent.builder().left(this.type.getName()).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
            panelComponent.getChildren().add(LineComponent.builder().left("Location:").build());
            panelComponent.getChildren().add(LineComponent.builder().left(this.type.getLocation()).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
            if (this.returnItem != null) {
                panelComponent.getChildren().add(LineComponent.builder().left("Item:").build());
                for (LineComponent line : SkillChallengeClue.getRequirements(plugin, false, this.returnItem)) {
                    panelComponent.getChildren().add(line);
                }
            }
        }
    }

    @Override
    public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
        if (plugin.getNpcsToMark() != null) {
            for (NPC npc : plugin.getNpcsToMark()) {
                OverlayUtil.renderActorOverlayImage(graphics, (Actor)npc, plugin.getClueScrollImage(), Color.ORANGE, 30);
            }
        }
        if (!this.challengeCompleted && this.objectNames.length > 0 && plugin.getNamedObjectsToMark() != null) {
            Point mousePosition = plugin.getClient().getMouseCanvasPosition();
            for (TileObject object : plugin.getNamedObjectsToMark()) {
                if (plugin.getClient().getPlane() != object.getPlane()) continue;
                OverlayUtil.renderHoverableArea(graphics, object.getClickbox(), mousePosition, ClueScrollWorldOverlay.CLICKBOX_FILL_COLOR, ClueScrollWorldOverlay.CLICKBOX_BORDER_COLOR, ClueScrollWorldOverlay.CLICKBOX_HOVER_BORDER_COLOR);
                OverlayUtil.renderImageLocation(plugin.getClient(), graphics, object.getLocalLocation(), plugin.getClueScrollImage(), 30);
            }
        }
    }

    private static List<LineComponent> getRequirements(ClueScrollPlugin plugin, boolean requireEquipped, ItemRequirement ... requirements) {
        ArrayList<LineComponent> components = new ArrayList<LineComponent>();
        Item[] equipment = plugin.getEquippedItems();
        Item[] inventory = plugin.getInventoryItems();
        if (equipment == null) {
            equipment = new Item[]{};
        }
        if (inventory == null) {
            inventory = new Item[]{};
        }
        Item[] combined = new Item[equipment.length + inventory.length];
        System.arraycopy(equipment, 0, combined, 0, equipment.length);
        System.arraycopy(inventory, 0, combined, equipment.length, inventory.length);
        for (ItemRequirement requirement : requirements) {
            boolean equipmentFulfilled = requirement.fulfilledBy(equipment);
            boolean combinedFulfilled = requirement.fulfilledBy(combined);
            components.add(LineComponent.builder().left(requirement.getCollectiveName(plugin.getClient())).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).right(combinedFulfilled ? "\u2713" : "\u2717").rightFont(FontManager.getDefaultFont()).rightColor(equipmentFulfilled || combinedFulfilled && !requireEquipped ? Color.GREEN : (combinedFulfilled ? Color.ORANGE : Color.RED)).build());
        }
        return components;
    }

    public static SkillChallengeClue forText(String text, String rawText) {
        for (SkillChallengeClue clue : CLUES) {
            if (rawText.equalsIgnoreCase(clue.returnText)) {
                clue.setChallengeCompleted(true);
                return clue;
            }
            if (!text.equals(clue.rawChallenge)) continue;
            clue.setChallengeCompleted(false);
            return clue;
        }
        return null;
    }

    @Override
    public String[] getNpcs() {
        return new String[]{this.type.getName()};
    }

    public ChallengeType getType() {
        return this.type;
    }

    public String getChallenge() {
        return this.challenge;
    }

    public String getRawChallenge() {
        return this.rawChallenge;
    }

    public String getReturnText() {
        return this.returnText;
    }

    public ItemRequirement[] getItemRequirements() {
        return this.itemRequirements;
    }

    public SingleItemRequirement getReturnItem() {
        return this.returnItem;
    }

    public boolean isRequireEquip() {
        return this.requireEquip;
    }

    @Override
    public String[] getObjectNames() {
        return this.objectNames;
    }

    @Override
    public int[] getObjectRegions() {
        return this.objectRegions;
    }

    public boolean isChallengeCompleted() {
        return this.challengeCompleted;
    }

    public void setChallengeCompleted(boolean challengeCompleted) {
        this.challengeCompleted = challengeCompleted;
    }

    static enum ChallengeType {
        CHARLIE("Charlie the Tramp", "Southern Entrance to Varrock"),
        SHERLOCK("Sherlock", "East of the Sorcerer's Tower in Seers' Village");

        private String name;
        private String location;

        private ChallengeType(String name, String location) {
            this.name = name;
            this.location = location;
        }

        public String getName() {
            return this.name;
        }

        public String getLocation() {
            return this.location;
        }
    }
}

