/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.runelite.api.VarPlayer
 */
package net.runelite.client.chat;

import java.awt.Color;
import javax.annotation.Nullable;
import net.runelite.api.VarPlayer;
import net.runelite.client.chat.ChatColorType;

class ChatColor {
    private ChatColorType type;
    private Color color;
    private boolean transparent;
    private boolean isDefault;
    @Nullable
    private VarPlayer setting;

    public ChatColor(ChatColorType type, Color color, boolean transparent) {
        this(type, color, transparent, false, null);
    }

    public ChatColorType getType() {
        return this.type;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isTransparent() {
        return this.transparent;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    @Nullable
    public VarPlayer getSetting() {
        return this.setting;
    }

    public void setType(ChatColorType type) {
        this.type = type;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setSetting(@Nullable VarPlayer setting) {
        this.setting = setting;
    }

    public String toString() {
        return "ChatColor(type=" + (Object)((Object)this.getType()) + ", color=" + this.getColor() + ", transparent=" + this.isTransparent() + ", isDefault=" + this.isDefault() + ", setting=" + (Object)this.getSetting() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChatColor)) {
            return false;
        }
        ChatColor other = (ChatColor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isTransparent() != other.isTransparent()) {
            return false;
        }
        ChatColorType this$type = this.getType();
        ChatColorType other$type = other.getType();
        return !(this$type == null ? other$type != null : !((Object)((Object)this$type)).equals((Object)other$type));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChatColor;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isTransparent() ? 79 : 97);
        ChatColorType $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)((Object)$type)).hashCode());
        return result;
    }

    public ChatColor(ChatColorType type, Color color, boolean transparent, boolean isDefault, @Nullable VarPlayer setting) {
        this.type = type;
        this.color = color;
        this.transparent = transparent;
        this.isDefault = isDefault;
        this.setting = setting;
    }
}

