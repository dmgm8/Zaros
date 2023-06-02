/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 */
package net.runelite.client.plugins.motherlode;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.motherlode.MotherlodeConfig;
import net.runelite.client.plugins.motherlode.MotherlodePlugin;
import net.runelite.client.plugins.motherlode.MotherlodeSession;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class MotherlodeOreOverlay
extends OverlayPanel {
    private final MotherlodePlugin plugin;
    private final MotherlodeSession motherlodeSession;
    private final MotherlodeConfig config;
    private final ItemManager itemManager;

    @Inject
    MotherlodeOreOverlay(MotherlodePlugin plugin, MotherlodeSession motherlodeSession, MotherlodeConfig config, ItemManager itemManager) {
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.plugin = plugin;
        this.motherlodeSession = motherlodeSession;
        this.config = config;
        this.itemManager = itemManager;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.plugin.isInMlm() || !this.config.showOresFound()) {
            return null;
        }
        MotherlodeSession session = this.motherlodeSession;
        int nuggetsFound = session.getNuggetsFound();
        int coalFound = session.getCoalFound();
        int goldFound = session.getGoldFound();
        int mithrilFound = session.getMithrilFound();
        int adamantiteFound = session.getAdamantiteFound();
        int runiteFound = session.getRuniteFound();
        if (nuggetsFound == 0 && coalFound == 0 && goldFound == 0 && mithrilFound == 0 && adamantiteFound == 0 && runiteFound == 0) {
            return null;
        }
        if (this.config.showLootIcons()) {
            this.panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
            if (nuggetsFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(12012, nuggetsFound, true)));
            }
            if (coalFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(453, coalFound, true)));
            }
            if (goldFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(444, goldFound, true)));
            }
            if (mithrilFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(447, mithrilFound, true)));
            }
            if (adamantiteFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(449, adamantiteFound, true)));
            }
            if (runiteFound > 0) {
                this.panelComponent.getChildren().add(new ImageComponent(this.itemManager.getImage(451, runiteFound, true)));
            }
        } else {
            this.panelComponent.setOrientation(ComponentOrientation.VERTICAL);
            this.panelComponent.getChildren().add(TitleComponent.builder().text("Ores found").build());
            if (nuggetsFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Nuggets:").right(Integer.toString(nuggetsFound)).build());
            }
            if (coalFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Coal:").right(Integer.toString(coalFound)).build());
            }
            if (goldFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Gold:").right(Integer.toString(goldFound)).build());
            }
            if (mithrilFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Mithril:").right(Integer.toString(mithrilFound)).build());
            }
            if (adamantiteFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Adamantite:").right(Integer.toString(adamantiteFound)).build());
            }
            if (runiteFound > 0) {
                this.panelComponent.getChildren().add(LineComponent.builder().left("Runite:").right(Integer.toString(runiteFound)).build());
            }
        }
        return super.render(graphics);
    }
}

