/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Actor
 *  net.runelite.api.NPC
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.cluescrolls.clues;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.NpcClueScroll;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class MusicClue
extends ClueScroll
implements NpcClueScroll {
    private static final WorldPoint LOCATION = new WorldPoint(2990, 3384, 0);
    private static final String CECILIA = "Cecilia";
    private static final Pattern SONG_PATTERN = Pattern.compile("<col=ffffff>([A-Za-z !&',.]+)</col>");
    private final String song;

    @Override
    public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
        panelComponent.getChildren().add(TitleComponent.builder().text("Music Clue").build());
        panelComponent.getChildren().add(LineComponent.builder().left("NPC:").build());
        panelComponent.getChildren().add(LineComponent.builder().left(CECILIA).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Location:").build());
        panelComponent.getChildren().add(LineComponent.builder().left("Falador Park").leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Song:").build());
        panelComponent.getChildren().add(LineComponent.builder().left(this.song).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
    }

    @Override
    public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
        if (!LOCATION.isInScene(plugin.getClient())) {
            return;
        }
        for (NPC npc : plugin.getNpcsToMark()) {
            OverlayUtil.renderActorOverlayImage(graphics, (Actor)npc, plugin.getClueScrollImage(), Color.ORANGE, 30);
        }
    }

    @Override
    public String[] getNpcs() {
        return new String[]{CECILIA};
    }

    public static MusicClue forText(String text) {
        Matcher m = SONG_PATTERN.matcher(text);
        if (m.find()) {
            String song = m.group(1);
            return new MusicClue(song);
        }
        return null;
    }

    private MusicClue(String song) {
        this.song = song;
    }

    public String getSong() {
        return this.song;
    }
}

