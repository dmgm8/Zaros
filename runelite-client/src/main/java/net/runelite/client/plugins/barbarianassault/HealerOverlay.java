/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.barbarianassault;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.barbarianassault.BarbarianAssaultConfig;
import net.runelite.client.plugins.barbarianassault.BarbarianAssaultPlugin;
import net.runelite.client.plugins.barbarianassault.Role;
import net.runelite.client.plugins.barbarianassault.Round;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class HealerOverlay
extends Overlay {
    private static final Color HP_HIGH = new Color(10, 146, 5, 125);
    private static final Color HP_MID = new Color(146, 146, 0, 230);
    private static final Color HP_LOW = new Color(225, 35, 0, 125);
    private final Client client;
    private final BarbarianAssaultPlugin plugin;
    private final BarbarianAssaultConfig config;

    @Inject
    private HealerOverlay(Client client, BarbarianAssaultPlugin plugin, BarbarianAssaultConfig config) {
        super(plugin);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.UNDER_WIDGETS);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Round round = this.plugin.getCurrentRound();
        if (round == null) {
            return null;
        }
        Role role = round.getRoundRole();
        if (this.config.showHealerBars() && role == Role.HEALER) {
            for (HealerTeam teammate : HealerTeam.values()) {
                Widget widget = this.client.getWidget(teammate.getTeammate());
                if (widget == null) continue;
                String[] teammateHealth = widget.getText().split(" / ");
                int curHealth = Integer.parseInt(teammateHealth[0]);
                int maxHealth = Integer.parseInt(teammateHealth[1]);
                int width = teammate.getWidth();
                double hpRatio = (double)curHealth / (double)maxHealth;
                int filledWidth = this.getBarWidth(hpRatio, width);
                Color barColor = this.getBarColor(hpRatio);
                int offsetX = teammate.getOffsetX();
                int offsetY = teammate.getOffsetY();
                int x = widget.getCanvasLocation().getX() - offsetX;
                int y = widget.getCanvasLocation().getY() - offsetY;
                graphics.setColor(barColor);
                graphics.fillRect(x, y, filledWidth, 20);
            }
        }
        return null;
    }

    private int getBarWidth(double ratio, int size) {
        if (ratio >= 1.0) {
            return size;
        }
        return (int)Math.round(ratio * (double)size);
    }

    private Color getBarColor(double ratio) {
        if (ratio <= 0.33) {
            return HP_LOW;
        }
        if (ratio <= 0.66) {
            return HP_MID;
        }
        return HP_HIGH;
    }

    private static enum HealerTeam {
        TEAMMATE1(WidgetInfo.BA_HEAL_TEAMMATE1, 28, 2, 115),
        TEAMMATE2(WidgetInfo.BA_HEAL_TEAMMATE2, 26, 2, 115),
        TEAMMATE3(WidgetInfo.BA_HEAL_TEAMMATE3, 26, 2, 115),
        TEAMMATE4(WidgetInfo.BA_HEAL_TEAMMATE4, 25, 2, 115);

        private WidgetInfo teammate;
        private int offsetX;
        private int offsetY;
        private int width;

        public WidgetInfo getTeammate() {
            return this.teammate;
        }

        public int getOffsetX() {
            return this.offsetX;
        }

        public int getOffsetY() {
            return this.offsetY;
        }

        public int getWidth() {
            return this.width;
        }

        private HealerTeam(WidgetInfo teammate, int offsetX, int offsetY, int width) {
            this.teammate = teammate;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.width = width;
        }
    }
}

