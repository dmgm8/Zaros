/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Player
 *  net.runelite.api.Point
 */
package net.runelite.client.plugins.playerindicators;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.plugins.playerindicators.PlayerIndicatorsConfig;
import net.runelite.client.plugins.playerindicators.PlayerIndicatorsService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class PlayerIndicatorsMinimapOverlay
extends Overlay {
    private final PlayerIndicatorsService playerIndicatorsService;
    private final PlayerIndicatorsConfig config;

    @Inject
    private PlayerIndicatorsMinimapOverlay(PlayerIndicatorsConfig config, PlayerIndicatorsService playerIndicatorsService) {
        this.config = config;
        this.playerIndicatorsService = playerIndicatorsService;
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        this.playerIndicatorsService.forEachPlayer((player, color) -> this.renderPlayerOverlay(graphics, (Player)player, (Color)color));
        return null;
    }

    private void renderPlayerOverlay(Graphics2D graphics, Player actor, Color color) {
        Point minimapLocation;
        String name = actor.getName().replace('\u00a0', ' ');
        if (this.config.drawMinimapNames() && (minimapLocation = actor.getMinimapLocation()) != null) {
            OverlayUtil.renderTextLocation(graphics, minimapLocation, name, color);
        }
    }
}

