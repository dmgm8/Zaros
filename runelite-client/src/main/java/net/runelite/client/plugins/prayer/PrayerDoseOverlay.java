/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.Point
 *  net.runelite.api.Skill
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.prayer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.prayer.PrayerConfig;
import net.runelite.client.plugins.prayer.PrayerPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;

class PrayerDoseOverlay
extends Overlay {
    private static final float PULSE_TIME = 1200.0f;
    private static final Color START_COLOR = new Color(0, 255, 255);
    private static final Color END_COLOR = new Color(0, 92, 92);
    private final Client client;
    private final PrayerPlugin plugin;
    private final PrayerConfig config;
    private final TooltipManager tooltipManager;
    private Instant startOfLastTick = Instant.now();
    private boolean trackTick = true;
    private int restoreAmount;

    @Inject
    private PrayerDoseOverlay(Client client, TooltipManager tooltipManager, PrayerPlugin plugin, PrayerConfig config) {
        this.client = client;
        this.tooltipManager = tooltipManager;
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    void onTick() {
        if (this.trackTick) {
            this.startOfLastTick = Instant.now();
            this.trackTick = false;
        } else {
            this.trackTick = true;
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Widget xpOrb = this.client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB);
        if (xpOrb == null || xpOrb.isHidden()) {
            return null;
        }
        Rectangle bounds = xpOrb.getBounds();
        if (bounds.getX() <= 0.0) {
            return null;
        }
        Point mousePosition = this.client.getMouseCanvasPosition();
        if (this.config.showPrayerStatistics() && bounds.contains(mousePosition.getX(), mousePosition.getY())) {
            StringBuilder sb = new StringBuilder();
            if (this.config.replaceOrbText()) {
                sb.append("Prayer points remaining: ").append(this.client.getBoostedSkillLevel(Skill.PRAYER));
            } else {
                sb.append("Time Remaining: ").append(this.plugin.getEstimatedTimeRemaining(false));
            }
            sb.append("</br>").append("Prayer Bonus: ").append(this.plugin.getPrayerBonus());
            this.tooltipManager.add(new Tooltip(sb.toString()));
        }
        if (!this.config.showPrayerDoseIndicator() || this.restoreAmount == 0) {
            return null;
        }
        int currentPrayer = this.client.getBoostedSkillLevel(Skill.PRAYER);
        int maxPrayer = this.client.getRealSkillLevel(Skill.PRAYER);
        int prayerPointsMissing = maxPrayer - currentPrayer;
        if (prayerPointsMissing <= 0 || prayerPointsMissing < this.restoreAmount) {
            return null;
        }
        int orbInnerSize = (int)bounds.getHeight();
        int orbInnerX = (int)(bounds.getX() + 24.0);
        int orbInnerY = (int)(bounds.getY() - 1.0);
        long timeSinceLastTick = Duration.between(this.startOfLastTick, Instant.now()).toMillis();
        float tickProgress = Math.min((float)timeSinceLastTick / 1200.0f, 1.0f);
        double t = (double)tickProgress * Math.PI;
        graphics.setColor(ColorUtil.colorLerp(START_COLOR, END_COLOR, Math.sin(t)));
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.drawOval(orbInnerX, orbInnerY, orbInnerSize, orbInnerSize);
        return null;
    }

    void setRestoreAmount(int restoreAmount) {
        this.restoreAmount = restoreAmount;
    }
}

