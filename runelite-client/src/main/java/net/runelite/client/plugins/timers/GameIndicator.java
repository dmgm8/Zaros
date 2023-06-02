/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.timers;

import java.awt.Color;
import net.runelite.client.plugins.timers.GameTimerImageType;

enum GameIndicator {
    VENGEANCE_ACTIVE(561, GameTimerImageType.SPRITE, "Vengeance active");

    private final String description;
    private String text;
    private Color textColor;
    private final int imageId;
    private final GameTimerImageType imageType;

    private GameIndicator(int imageId, GameTimerImageType idType, String description, String text, Color textColor) {
        this.imageId = imageId;
        this.imageType = idType;
        this.description = description;
        this.text = text;
        this.textColor = textColor;
    }

    private GameIndicator(int imageId, GameTimerImageType idType, String description) {
        this(imageId, idType, description, "", null);
    }

    String getDescription() {
        return this.description;
    }

    String getText() {
        return this.text;
    }

    Color getTextColor() {
        return this.textColor;
    }

    int getImageId() {
        return this.imageId;
    }

    GameTimerImageType getImageType() {
        return this.imageType;
    }
}

