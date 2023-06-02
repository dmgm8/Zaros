/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.blastmine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.blastmine.BlastMinePlugin;
import net.runelite.client.plugins.blastmine.BlastMinePluginConfig;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;

class BlastMineOreCountOverlay
extends OverlayPanel {
    private final Client client;
    private final BlastMinePluginConfig config;
    private final ItemManager itemManager;

    @Inject
    private BlastMineOreCountOverlay(BlastMinePlugin plugin, Client client, BlastMinePluginConfig config, ItemManager itemManager) {
        super(plugin);
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.config = config;
        this.itemManager = itemManager;
        this.panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Blast mine overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Widget blastMineWidget = this.client.getWidget(WidgetInfo.BLAST_MINE);
        if (blastMineWidget == null) {
            return null;
        }
        if (this.config.showOreOverlay()) {
            blastMineWidget.setHidden(true);
            this.panelComponent.getChildren().add(new ImageComponent(this.getImage(453, this.client.getVarbitValue(4924))));
            this.panelComponent.getChildren().add(new ImageComponent(this.getImage(444, this.client.getVarbitValue(4925))));
            this.panelComponent.getChildren().add(new ImageComponent(this.getImage(447, this.client.getVarbitValue(4926))));
            this.panelComponent.getChildren().add(new ImageComponent(this.getImage(449, this.client.getVarbitValue(4921))));
            this.panelComponent.getChildren().add(new ImageComponent(this.getImage(451, this.client.getVarbitValue(4922))));
        } else {
            blastMineWidget.setHidden(false);
        }
        return super.render(graphics);
    }

    private BufferedImage getImage(int itemID, int amount) {
        return this.itemManager.getImage(itemID, amount, true);
    }
}

