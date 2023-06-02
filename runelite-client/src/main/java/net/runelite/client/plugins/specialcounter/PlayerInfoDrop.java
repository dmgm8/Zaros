/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.specialcounter;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import net.runelite.client.ui.FontManager;

class PlayerInfoDrop {
    private final int startCycle;
    private final int endCycle;
    private final int playerIdx;
    private final String text;
    private final int startHeightOffset;
    private final int endHeightOffset;
    private final Font font;
    private final Color color;
    private final BufferedImage image;

    public static Builder builder(int startCycle, int endCycle, int playerIdx, String text) {
        return new Builder(startCycle, endCycle, playerIdx, text);
    }

    public int getStartCycle() {
        return this.startCycle;
    }

    public int getEndCycle() {
        return this.endCycle;
    }

    public int getPlayerIdx() {
        return this.playerIdx;
    }

    public String getText() {
        return this.text;
    }

    public int getStartHeightOffset() {
        return this.startHeightOffset;
    }

    public int getEndHeightOffset() {
        return this.endHeightOffset;
    }

    public Font getFont() {
        return this.font;
    }

    public Color getColor() {
        return this.color;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerInfoDrop)) {
            return false;
        }
        PlayerInfoDrop other = (PlayerInfoDrop)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getStartCycle() != other.getStartCycle()) {
            return false;
        }
        if (this.getEndCycle() != other.getEndCycle()) {
            return false;
        }
        if (this.getPlayerIdx() != other.getPlayerIdx()) {
            return false;
        }
        if (this.getStartHeightOffset() != other.getStartHeightOffset()) {
            return false;
        }
        if (this.getEndHeightOffset() != other.getEndHeightOffset()) {
            return false;
        }
        String this$text = this.getText();
        String other$text = other.getText();
        if (this$text == null ? other$text != null : !this$text.equals(other$text)) {
            return false;
        }
        Font this$font = this.getFont();
        Font other$font = other.getFont();
        if (this$font == null ? other$font != null : !((Object)this$font).equals(other$font)) {
            return false;
        }
        Color this$color = this.getColor();
        Color other$color = other.getColor();
        if (this$color == null ? other$color != null : !((Object)this$color).equals(other$color)) {
            return false;
        }
        BufferedImage this$image = this.getImage();
        BufferedImage other$image = other.getImage();
        return !(this$image == null ? other$image != null : !this$image.equals(other$image));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerInfoDrop;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getStartCycle();
        result = result * 59 + this.getEndCycle();
        result = result * 59 + this.getPlayerIdx();
        result = result * 59 + this.getStartHeightOffset();
        result = result * 59 + this.getEndHeightOffset();
        String $text = this.getText();
        result = result * 59 + ($text == null ? 43 : $text.hashCode());
        Font $font = this.getFont();
        result = result * 59 + ($font == null ? 43 : ((Object)$font).hashCode());
        Color $color = this.getColor();
        result = result * 59 + ($color == null ? 43 : ((Object)$color).hashCode());
        BufferedImage $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        return result;
    }

    public String toString() {
        return "PlayerInfoDrop(startCycle=" + this.getStartCycle() + ", endCycle=" + this.getEndCycle() + ", playerIdx=" + this.getPlayerIdx() + ", text=" + this.getText() + ", startHeightOffset=" + this.getStartHeightOffset() + ", endHeightOffset=" + this.getEndHeightOffset() + ", font=" + this.getFont() + ", color=" + this.getColor() + ", image=" + this.getImage() + ")";
    }

    private PlayerInfoDrop(int startCycle, int endCycle, int playerIdx, String text, int startHeightOffset, int endHeightOffset, Font font, Color color, BufferedImage image) {
        this.startCycle = startCycle;
        this.endCycle = endCycle;
        this.playerIdx = playerIdx;
        this.text = text;
        this.startHeightOffset = startHeightOffset;
        this.endHeightOffset = endHeightOffset;
        this.font = font;
        this.color = color;
        this.image = image;
    }

    static class Builder {
        private final int startCycle;
        private final int endCycle;
        private final int playerIdx;
        private final String text;
        private int startHeightOffset = 0;
        private int endHeightOffset = 200;
        private Font font = FontManager.getRunescapeBoldFont();
        private Color color = Color.WHITE;
        private BufferedImage image;

        public PlayerInfoDrop build() {
            if (this.startCycle > this.endCycle) {
                throw new IllegalArgumentException("endCycle must be after startCycle");
            }
            if (this.playerIdx < 0 || this.playerIdx > 2047) {
                throw new IllegalArgumentException("playerIdx must be between 0-2047");
            }
            return new PlayerInfoDrop(this.startCycle, this.endCycle, this.playerIdx, this.text, this.startHeightOffset, this.endHeightOffset, this.font, this.color, this.image);
        }

        public Builder(int startCycle, int endCycle, int playerIdx, String text) {
            this.startCycle = startCycle;
            this.endCycle = endCycle;
            this.playerIdx = playerIdx;
            this.text = text;
        }

        public Builder startHeightOffset(int startHeightOffset) {
            this.startHeightOffset = startHeightOffset;
            return this;
        }

        public Builder endHeightOffset(int endHeightOffset) {
            this.endHeightOffset = endHeightOffset;
            return this;
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder image(BufferedImage image) {
            this.image = image;
            return this;
        }
    }
}

