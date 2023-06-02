/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.runelite.api.Client
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.cluescrolls.clues;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.LocationClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.TextClueScroll;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class FairyRingClue
extends ClueScroll
implements TextClueScroll,
LocationClueScroll {
    private static final List<FairyRingClue> CLUES = ImmutableList.of((Object)new FairyRingClue("A I R 2 3 3 1", new WorldPoint(2702, 3246, 0)), (Object)new FairyRingClue("A I Q 0 4 4 0", new WorldPoint(3000, 3110, 0)), (Object)new FairyRingClue("A L P 1 1 4 0", new WorldPoint(2504, 3633, 0)), (Object)new FairyRingClue("B L P 6 2 0 0", new WorldPoint(2439, 5132, 0)), (Object)new FairyRingClue("B J R 1 1 2 3", new WorldPoint(2648, 4729, 0)), (Object)new FairyRingClue("B I P 7 0 1 3", new WorldPoint(3407, 3330, 0)), (Object)new FairyRingClue("C I S 0 0 0 9", new WorldPoint(1630, 3868, 0)), (Object)new FairyRingClue("C K P 0 2 2 4", new WorldPoint(2073, 4846, 0)), (Object)new FairyRingClue("D I P 8 5 1 1", new WorldPoint(3041, 4770, 0)), (Object)new FairyRingClue("D K S 2 3 1 0", new WorldPoint(2747, 3720, 0)));
    private String text;
    private WorldPoint location;

    private FairyRingClue(String text, WorldPoint location) {
        this.text = text;
        this.location = location;
        this.setRequiresSpade(true);
    }

    @Override
    public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
        panelComponent.getChildren().add(TitleComponent.builder().text("Fairy Ring Clue").build());
        panelComponent.getChildren().add(LineComponent.builder().left("Code:").build());
        panelComponent.getChildren().add(LineComponent.builder().left(this.getText().substring(0, 5)).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
        panelComponent.getChildren().add(LineComponent.builder().left("Travel to the fairy ring to see where to dig.").build());
    }

    @Override
    public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
        LocalPoint localLocation = LocalPoint.fromWorld((Client)plugin.getClient(), (WorldPoint)this.getLocation());
        if (localLocation == null) {
            return;
        }
        OverlayUtil.renderTileOverlay(plugin.getClient(), graphics, localLocation, plugin.getSpadeImage(), Color.ORANGE);
    }

    public static FairyRingClue forText(String text) {
        for (FairyRingClue clue : CLUES) {
            if (!clue.text.equalsIgnoreCase(text)) continue;
            return clue;
        }
        return null;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public WorldPoint getLocation() {
        return this.location;
    }
}

