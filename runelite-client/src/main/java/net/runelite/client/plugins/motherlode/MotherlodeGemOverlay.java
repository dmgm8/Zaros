/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.MenuAction
 */
package net.runelite.client.plugins.motherlode;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.api.MenuAction;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.motherlode.MotherlodeConfig;
import net.runelite.client.plugins.motherlode.MotherlodePlugin;
import net.runelite.client.plugins.motherlode.MotherlodeSession;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class MotherlodeGemOverlay
extends OverlayPanel {
    private final MotherlodePlugin plugin;
    private final MotherlodeSession motherlodeSession;
    private final MotherlodeConfig config;
    private final ItemManager itemManager;

    @Inject
    MotherlodeGemOverlay(MotherlodePlugin plugin, MotherlodeSession motherlodeSession, MotherlodeConfig config, ItemManager itemManager) {
        super(plugin);
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.plugin = plugin;
        this.motherlodeSession = motherlodeSession;
        this.config = config;
        this.itemManager = itemManager;
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Gem overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        MotherlodeSession session = this.motherlodeSession;
        if (session.getLastGemFound() == null || !this.plugin.isInMlm() || !this.config.showGemsFound()) {
            return null;
        }
        Duration statTimeout = Duration.ofMinutes(this.config.statTimeout());
        Duration sinceLastGem = Duration.between(session.getLastGemFound(), Instant.now());
        if (sinceLastGem.compareTo(statTimeout) >= 0) {
            return null;
        }
        int diamondsFound = session.getDiamondsFound();
        int rubiesFound = session.getRubiesFound();
        int emeraldsFound = session.getEmeraldsFound();
        int sapphiresFound = session.getSapphiresFound();
        if (this.config.showLootIcons()) {
            this.panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
            if (diamondsFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(1617, diamondsFound, true)));
            }
            if (rubiesFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(1619, rubiesFound, true)));
            }
            if (emeraldsFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(1621, emeraldsFound, true)));
            }
            if (sapphiresFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(1623, sapphiresFound, true)));
            }
        } else {
            this.panelComponent.setOrientation(ComponentOrientation.VERTICAL);
            this.panelComponent.getChildren().add(TitleComponent.builder().text("Gems found").build());
            if (diamondsFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Diamonds:").right(Integer.toString(diamondsFound)).build());
            }
            if (rubiesFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Rubies:").right(Integer.toString(rubiesFound)).build());
            }
            if (emeraldsFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Emeralds:").right(Integer.toString(emeraldsFound)).build());
            }
            if (sapphiresFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Sapphires:").right(Integer.toString(sapphiresFound)).build());
            }
        }
        return super.render(graphics);
    }
}

