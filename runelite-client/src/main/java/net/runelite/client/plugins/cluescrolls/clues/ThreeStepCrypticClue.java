/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.InventoryID
 *  net.runelite.api.ItemContainer
 *  net.runelite.api.coords.WorldPoint
 */
package net.runelite.client.plugins.cluescrolls.clues;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.CrypticClue;
import net.runelite.client.plugins.cluescrolls.clues.LocationsClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.NpcClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.ObjectClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.TextClueScroll;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.Text;

public class ThreeStepCrypticClue
extends ClueScroll
implements TextClueScroll,
ObjectClueScroll,
NpcClueScroll,
LocationsClueScroll {
    private final List<Map.Entry<CrypticClue, Boolean>> clueSteps;
    private final String text;

    public static ThreeStepCrypticClue forText(String plainText, String text) {
        String[] split = text.split("<br>\\s*<br>");
        ArrayList<Map.Entry<CrypticClue, Boolean>> steps = new ArrayList<Map.Entry<CrypticClue, Boolean>>(split.length);
        block0: for (String part : split) {
            boolean isDone = part.contains("<str>");
            String rawText = Text.sanitizeMultilineText(part);
            for (CrypticClue clue : CrypticClue.CLUES) {
                if (!rawText.equalsIgnoreCase(clue.getText())) continue;
                steps.add(new AbstractMap.SimpleEntry<CrypticClue, Boolean>(clue, isDone));
                continue block0;
            }
        }
        if (steps.isEmpty() || steps.size() < 3) {
            return null;
        }
        return new ThreeStepCrypticClue(steps, plainText);
    }

    @Override
    public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
        panelComponent.setPreferredSize(new Dimension(200, 0));
        for (int i = 0; i < this.clueSteps.size(); ++i) {
            Map.Entry<CrypticClue, Boolean> e = this.clueSteps.get(i);
            if (e.getValue().booleanValue()) continue;
            CrypticClue c = e.getKey();
            panelComponent.getChildren().add(TitleComponent.builder().text("Cryptic Clue #" + (i + 1)).build());
            panelComponent.getChildren().add(LineComponent.builder().left("Solution:").build());
            panelComponent.getChildren().add(LineComponent.builder().left(c.getSolution()).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
        }
    }

    @Override
    public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
        for (Map.Entry<CrypticClue, Boolean> e : this.clueSteps) {
            if (e.getValue().booleanValue()) continue;
            e.getKey().makeWorldOverlayHint(graphics, plugin);
        }
    }

    public boolean update(int containerId, ItemContainer itemContainer) {
        if (containerId == InventoryID.INVENTORY.getId()) {
            return this.checkForPart(itemContainer, 19837, 0) || this.checkForPart(itemContainer, 19838, 1) || this.checkForPart(itemContainer, 19839, 2);
        }
        return false;
    }

    private boolean checkForPart(ItemContainer itemContainer, int clueScrollPart, int index) {
        Map.Entry<CrypticClue, Boolean> entry;
        if (itemContainer.contains(clueScrollPart) && !(entry = this.clueSteps.get(index)).getValue().booleanValue()) {
            entry.setValue(true);
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        for (Map.Entry<CrypticClue, Boolean> clueStep : this.clueSteps) {
            clueStep.setValue(false);
        }
    }

    @Override
    public WorldPoint getLocation() {
        return null;
    }

    @Override
    public WorldPoint[] getLocations() {
        return (WorldPoint[])this.clueSteps.stream().filter(s -> (Boolean)s.getValue() == false).map(s -> ((CrypticClue)s.getKey()).getLocation()).filter(Objects::nonNull).toArray(WorldPoint[]::new);
    }

    @Override
    public String[] getNpcs() {
        return (String[])this.clueSteps.stream().filter(s -> (Boolean)s.getValue() == false).map(s -> ((CrypticClue)s.getKey()).getNpc()).toArray(String[]::new);
    }

    @Override
    public int[] getObjectIds() {
        return this.clueSteps.stream().filter(s -> (Boolean)s.getValue() == false).mapToInt(s -> ((CrypticClue)s.getKey()).getObjectId()).toArray();
    }

    public List<Map.Entry<CrypticClue, Boolean>> getClueSteps() {
        return this.clueSteps;
    }

    @Override
    public String getText() {
        return this.text;
    }

    public ThreeStepCrypticClue(List<Map.Entry<CrypticClue, Boolean>> clueSteps, String text) {
        this.clueSteps = clueSteps;
        this.text = text;
    }
}

