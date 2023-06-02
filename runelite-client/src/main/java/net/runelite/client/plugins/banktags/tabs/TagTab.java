/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.widgets.Widget
 */
package net.runelite.client.plugins.banktags.tabs;

import net.runelite.api.widgets.Widget;

public class TagTab {
    private String tag;
    private int iconItemId;
    private Widget background;
    private Widget icon;
    private Widget menu;

    TagTab(int iconItemId, String tag) {
        this.iconItemId = iconItemId;
        this.tag = tag;
    }

    void setHidden(boolean hide) {
        if (this.background != null) {
            this.background.setHidden(hide);
        }
        if (this.icon != null) {
            this.icon.setHidden(hide);
        }
        if (this.menu != null) {
            this.menu.setHidden(hide);
        }
    }

    public String getTag() {
        return this.tag;
    }

    public int getIconItemId() {
        return this.iconItemId;
    }

    public Widget getBackground() {
        return this.background;
    }

    public Widget getIcon() {
        return this.icon;
    }

    public Widget getMenu() {
        return this.menu;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setIconItemId(int iconItemId) {
        this.iconItemId = iconItemId;
    }

    public void setBackground(Widget background) {
        this.background = background;
    }

    public void setIcon(Widget icon) {
        this.icon = icon;
    }

    public void setMenu(Widget menu) {
        this.menu = menu;
    }

    public String toString() {
        return "TagTab(tag=" + this.getTag() + ", iconItemId=" + this.getIconItemId() + ", background=" + (Object)this.getBackground() + ", icon=" + (Object)this.getIcon() + ", menu=" + (Object)this.getMenu() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TagTab)) {
            return false;
        }
        TagTab other = (TagTab)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$tag = this.getTag();
        String other$tag = other.getTag();
        return !(this$tag == null ? other$tag != null : !this$tag.equals(other$tag));
    }

    protected boolean canEqual(Object other) {
        return other instanceof TagTab;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $tag = this.getTag();
        result = result * 59 + ($tag == null ? 43 : $tag.hashCode());
        return result;
    }
}

