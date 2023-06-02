/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.cluescrolls;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.MusicClue;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class ClueScrollMusicOverlay
extends Overlay {
    private static final Rectangle PADDING = new Rectangle(2, 1, 0, 1);
    private final ClueScrollPlugin plugin;
    private final Client client;
    private boolean hasScrolled;

    @Inject
    private ClueScrollMusicOverlay(ClueScrollPlugin plugin, Client client) {
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.plugin = plugin;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        ClueScroll clue = this.plugin.getClue();
        if (!(clue instanceof MusicClue)) {
            this.hasScrolled = false;
            return null;
        }
        MusicClue musicClue = (MusicClue)clue;
        Widget musicContainer = this.client.getWidget(WidgetInfo.MUSIC_WINDOW);
        if (musicContainer == null || musicContainer.isHidden()) {
            return null;
        }
        Widget trackList = this.client.getWidget(WidgetInfo.MUSIC_TRACK_LIST);
        String trackToFind = musicClue.getSong();
        Widget found = null;
        if (trackList == null) {
            return null;
        }
        for (Widget track : trackList.getDynamicChildren()) {
            if (!track.getText().equals(trackToFind)) continue;
            found = track;
            break;
        }
        if (found == null) {
            return null;
        }
        if (!this.hasScrolled) {
            this.hasScrolled = true;
            this.plugin.scrollToWidget(WidgetInfo.MUSIC_TRACK_SCROLL_CONTAINER, WidgetInfo.MUSIC_TRACK_SCROLLBAR, found);
        }
        this.plugin.highlightWidget(graphics, found, trackList, PADDING, null);
        return null;
    }
}

