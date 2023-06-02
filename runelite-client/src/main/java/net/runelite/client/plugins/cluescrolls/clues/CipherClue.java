/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  javax.annotation.Nullable
 *  net.runelite.api.Actor
 *  net.runelite.api.NPC
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.cluescrolls.clues;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import javax.annotation.Nullable;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.LocationClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.NpcClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.TextClueScroll;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class CipherClue
extends ClueScroll
implements TextClueScroll,
NpcClueScroll,
LocationClueScroll {
    private static final List<CipherClue> CLUES = ImmutableList.of((Object)new CipherClue("BMJ UIF LFCBC TFMMFS", "Ali the Kebab seller", new WorldPoint(3354, 2974, 0), "Pollnivneach", "How many coins would you need to purchase 133 kebabs from me?", "399"), (Object)new CipherClue("GUHCHO", "Drezel", new WorldPoint(3440, 9895, 0), "Paterdomus", "Please solve this for x: 7x - 28=21", "7"), (Object)new CipherClue("HQNM LZM STSNQ", "Iron Man tutor", new WorldPoint(3227, 3227, 0), "Outside Lumbridge castle", "How many snakeskins are needed in order to craft 44 boots, 29 vambraces and 34 bandanas?", "666"), (Object)new CipherClue("ZHLUG ROG PDQ", "Weird Old Man", new WorldPoint(3224, 3112, 0), "Kalphite Lair entrance. Fairy ring BIQ", "SIX LEGS! All of them have 6! There are 25 of them! How many legs?", "150"), (Object)new CipherClue("ECRVCKP MJCNGF", "Captain Khaled", new WorldPoint(1845, 3754, 0), "Large eastern building in Port Piscarilius", "How many fishing cranes can you find around here?", "5"), (Object)new CipherClue("OVEXON", "Eluned", new WorldPoint(2289, 3144, 0), "Outside Lletya or in Prifddinas after Song of the Elves", "A question on elven crystal math. I have 5 and 3 crystals, large and small respectively. A large crystal is worth 10,000 coins and a small is worth but 1,000. How much are all my crystals worth?", "53,000"), (Object)new CipherClue("VTYR APCNTGLW", "King Percival", new WorldPoint(2634, 4682, 1), "Fisher Realm, first floor. Fairy ring BJR", "How many cannons are on this here castle?", "5"), (Object)new CipherClue("UZZU MUJHRKYYKJ", "Otto Godblessed", new WorldPoint(2501, 3487, 0), "Otto's Grotto", "How many pyre sites are found around this lake?", "3"), (Object)new CipherClue("XJABSE USBJCPSO", "Wizard Traiborn", new WorldPoint(3112, 3162, 0), "First floor of Wizards Tower. Fairy ring DIS", "How many air runes would I need to cast 630 wind waves?", "3150"), (Object)new CipherClue("HCKTA IQFHCVJGT", "Fairy Godfather", new WorldPoint(2446, 4428, 0), "Zanaris throne room", "There are 3 inputs and 4 letters on each ring How many total individual fairy ring codes are possible?", "64"), (Object)new CipherClue("ZSBKDO ZODO", "Pirate Pete", new WorldPoint(3680, 3537, 0), "Dock northeast of the Ectofunctus"), (Object)new CipherClue("GBJSZ RVFFO", "Fairy Queen", new WorldPoint(2347, 4435, 0), "Fairy Resistance Hideout"), (Object[])new CipherClue[]{new CipherClue("QSPGFTTPS HSBDLMFCPOF", "Professor Gracklebone", new WorldPoint(1625, 3802, 0), "Ground floor of Arceuus Library", "How many round tables can be found on this floor of the library?", "9"), new CipherClue("IWPPLQTP", "Gunnjorn", new WorldPoint(2541, 3548, 0), "Barbarian Outpost Agility course"), new CipherClue("BSOPME MZETQPS", "Arnold Lydspor", new WorldPoint(2329, 3689, 0), "Piscatoris Fishing Colony general store/bank"), new CipherClue("ESBZOPS QJH QFO", new WorldPoint(3077, 3260, 0), "Inside of Martin the Master Gardener's pig pen in Draynor Village.")});
    private final String text;
    @Nullable
    private final String npc;
    private final WorldPoint location;
    private final String area;
    @Nullable
    private final String question;
    @Nullable
    private final String answer;

    private CipherClue(String text, WorldPoint location, String area) {
        this.text = "The cipher reveals where to dig next: " + text;
        this.npc = null;
        this.location = location;
        this.area = area;
        this.question = null;
        this.answer = null;
    }

    private CipherClue(String text, String npc, WorldPoint location, String area) {
        this(text, npc, location, area, null, null);
    }

    private CipherClue(String text, String npc, WorldPoint location, String area, @Nullable String question, @Nullable String answer) {
        this.text = "The cipher reveals who to speak to next: " + text;
        this.npc = npc;
        this.location = location;
        this.area = area;
        this.question = question;
        this.answer = answer;
    }

    @Override
    public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
        panelComponent.getChildren().add(TitleComponent.builder().text("Cipher Clue").build());
        String clueNpc = this.getNpc();
        if (clueNpc != null) {
            panelComponent.getChildren().add(LineComponent.builder().left("NPC:").build());
            panelComponent.getChildren().add(LineComponent.builder().left(clueNpc).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
        }
        panelComponent.getChildren().add(LineComponent.builder().left("Location:").build());
        panelComponent.getChildren().add(LineComponent.builder().left(this.getArea()).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
        if (this.getAnswer() != null) {
            panelComponent.getChildren().add(LineComponent.builder().left("Answer:").build());
            panelComponent.getChildren().add(LineComponent.builder().left(this.getAnswer()).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
        }
    }

    @Override
    public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
        if (!this.getLocation().isInScene(plugin.getClient())) {
            return;
        }
        if (plugin.getNpcsToMark() != null) {
            for (NPC npc : plugin.getNpcsToMark()) {
                OverlayUtil.renderActorOverlayImage(graphics, (Actor)npc, plugin.getClueScrollImage(), Color.ORANGE, 30);
            }
        }
    }

    public static CipherClue forText(String text) {
        for (CipherClue clue : CLUES) {
            if (!text.equalsIgnoreCase(clue.text) && !text.equalsIgnoreCase(clue.question)) continue;
            return clue;
        }
        return null;
    }

    @Override
    public String[] getNpcs() {
        return new String[]{this.npc};
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Nullable
    public String getNpc() {
        return this.npc;
    }

    @Override
    public WorldPoint getLocation() {
        return this.location;
    }

    public String getArea() {
        return this.area;
    }

    @Nullable
    public String getQuestion() {
        return this.question;
    }

    @Nullable
    public String getAnswer() {
        return this.answer;
    }
}

