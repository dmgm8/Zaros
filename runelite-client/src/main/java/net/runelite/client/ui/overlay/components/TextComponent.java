/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package net.runelite.client.ui.overlay.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.runelite.client.ui.overlay.RenderableEntity;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

public class TextComponent
implements RenderableEntity {
    private static final String COL_TAG_REGEX = "(<col=([0-9a-fA-F]){2,6}>)";
    private static final Pattern COL_TAG_PATTERN_W_LOOKAHEAD = Pattern.compile("(?=(<col=([0-9a-fA-F]){2,6}>))");
    private String text;
    private Point position = new Point();
    private Color color = Color.WHITE;
    private Color shadowColor = Color.BLACK;
    private boolean outline;
    @Nullable
    private Font font;

    @Override
    public Dimension render(Graphics2D graphics) {
        Font originalFont = null;
        if (this.font != null) {
            originalFont = graphics.getFont();
            graphics.setFont(this.font);
        }
        FontMetrics fontMetrics = graphics.getFontMetrics();
        if (COL_TAG_PATTERN_W_LOOKAHEAD.matcher(this.text).find()) {
            String[] parts = COL_TAG_PATTERN_W_LOOKAHEAD.split(this.text);
            int x = this.position.x;
            for (String textSplitOnCol : parts) {
                String textWithoutCol = Text.removeTags(textSplitOnCol);
                String colColor = textSplitOnCol.substring(textSplitOnCol.indexOf("=") + 1, textSplitOnCol.indexOf(">"));
                graphics.setColor(Color.BLACK);
                if (this.outline) {
                    graphics.drawString(textWithoutCol, x, this.position.y + 1);
                    graphics.drawString(textWithoutCol, x, this.position.y - 1);
                    graphics.drawString(textWithoutCol, x + 1, this.position.y);
                    graphics.drawString(textWithoutCol, x - 1, this.position.y);
                } else {
                    graphics.drawString(textWithoutCol, x + 1, this.position.y + 1);
                }
                graphics.setColor(Color.decode("#" + colColor));
                graphics.drawString(textWithoutCol, x, this.position.y);
                x += fontMetrics.stringWidth(textWithoutCol);
            }
        } else {
            graphics.setColor(this.shadowColor);
            if (this.outline) {
                graphics.drawString(this.text, this.position.x, this.position.y + 1);
                graphics.drawString(this.text, this.position.x, this.position.y - 1);
                graphics.drawString(this.text, this.position.x + 1, this.position.y);
                graphics.drawString(this.text, this.position.x - 1, this.position.y);
            } else {
                graphics.drawString(this.text, this.position.x + 1, this.position.y + 1);
            }
            graphics.setColor(ColorUtil.colorWithAlpha(this.color, 255));
            graphics.drawString(this.text, this.position.x, this.position.y);
        }
        int width = fontMetrics.stringWidth(this.text);
        int height = fontMetrics.getHeight();
        if (originalFont != null) {
            graphics.setFont(originalFont);
        }
        return new Dimension(width, height);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
    }

    public void setFont(@Nullable Font font) {
        this.font = font;
    }
}

