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
package net.runelite.client.plugins.motherlode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.motherlode.MotherlodeConfig;
import net.runelite.client.plugins.motherlode.MotherlodePlugin;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;

class MotherlodeSackOverlay
extends OverlayPanel {
    private static final Color DANGER = new Color(150, 0, 0, 150);
    private final Client client;
    private final MotherlodeConfig config;
    private final MotherlodePlugin plugin;

    @Inject
    MotherlodeSackOverlay(Client client, MotherlodeConfig config, MotherlodePlugin plugin) {
        super(plugin);
        this.setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Sack overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!this.plugin.isInMlm()) {
            return null;
        }
        Widget sack = this.client.getWidget(WidgetInfo.MOTHERLODE_MINE);
        this.panelComponent.setBackgroundColor(ComponentConstants.STANDARD_BACKGROUND_COLOR);
        if (sack != null) {
            sack.setHidden(true);
            if (this.config.showSack()) {
                if (this.plugin.getCurSackSize() >= this.plugin.getMaxSackSize()) {
                    this.panelComponent.setBackgroundColor(DANGER);
                }
                this.panelComponent.getChildren().add(LineComponent.builder().left("Pay-dirt in sack:").right(String.valueOf(this.client.getVarbitValue(5558))).build());
            }
            if (this.config.showDepositsLeft()) {
                Integer depositsLeft = this.plugin.getDepositsLeft();
                Color color = Color.WHITE;
                if (depositsLeft != null) {
                    if (depositsLeft == 0) {
                        this.panelComponent.setBackgroundColor(DANGER);
                    } else if (depositsLeft == 1) {
                        color = Color.RED;
                    }
                }
                this.panelComponent.getChildren().add(LineComponent.builder().left("Deposits left:").leftColor(color).right(depositsLeft == null ? "N/A" : String.valueOf(depositsLeft)).rightColor(color).build());
            }
        }
        return super.render(graphics);
    }
}

