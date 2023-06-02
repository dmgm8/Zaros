/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.timers;

import java.awt.Color;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.timers.GameIndicator;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;

public class IndicatorIndicator
extends InfoBox {
    private final GameIndicator indicator;

    IndicatorIndicator(GameIndicator indicator, Plugin plugin) {
        super(null, plugin);
        this.indicator = indicator;
        this.setPriority(InfoBoxPriority.MED);
    }

    @Override
    public String getText() {
        return this.indicator.getText();
    }

    @Override
    public Color getTextColor() {
        return this.indicator.getTextColor();
    }

    public GameIndicator getIndicator() {
        return this.indicator;
    }
}

