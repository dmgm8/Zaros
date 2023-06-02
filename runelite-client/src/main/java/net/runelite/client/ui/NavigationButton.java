/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.ui;

import java.awt.image.BufferedImage;
import java.util.Map;
import net.runelite.client.ui.PluginPanel;

public class NavigationButton {
    private final BufferedImage icon;
    private boolean tab;
    private final String tooltip;
    private Runnable onReady;
    private boolean selected;
    private Runnable onClick;
    private Runnable onSelect;
    private PluginPanel panel;
    private int priority;
    private Map<String, Runnable> popup;

    private static boolean $default$tab() {
        return true;
    }

    private static String $default$tooltip() {
        return "";
    }

    NavigationButton(BufferedImage icon, boolean tab, String tooltip, Runnable onReady, boolean selected, Runnable onClick, Runnable onSelect, PluginPanel panel, int priority, Map<String, Runnable> popup) {
        this.icon = icon;
        this.tab = tab;
        this.tooltip = tooltip;
        this.onReady = onReady;
        this.selected = selected;
        this.onClick = onClick;
        this.onSelect = onSelect;
        this.panel = panel;
        this.priority = priority;
        this.popup = popup;
    }

    public static NavigationButtonBuilder builder() {
        return new NavigationButtonBuilder();
    }

    public BufferedImage getIcon() {
        return this.icon;
    }

    public boolean isTab() {
        return this.tab;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public Runnable getOnReady() {
        return this.onReady;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public Runnable getOnClick() {
        return this.onClick;
    }

    public Runnable getOnSelect() {
        return this.onSelect;
    }

    public PluginPanel getPanel() {
        return this.panel;
    }

    public int getPriority() {
        return this.priority;
    }

    public Map<String, Runnable> getPopup() {
        return this.popup;
    }

    public void setTab(boolean tab) {
        this.tab = tab;
    }

    public void setOnReady(Runnable onReady) {
        this.onReady = onReady;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void setOnSelect(Runnable onSelect) {
        this.onSelect = onSelect;
    }

    public void setPanel(PluginPanel panel) {
        this.panel = panel;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setPopup(Map<String, Runnable> popup) {
        this.popup = popup;
    }

    public String toString() {
        return "NavigationButton(icon=" + this.getIcon() + ", tab=" + this.isTab() + ", tooltip=" + this.getTooltip() + ", onReady=" + this.getOnReady() + ", selected=" + this.isSelected() + ", onClick=" + this.getOnClick() + ", onSelect=" + this.getOnSelect() + ", panel=" + this.getPanel() + ", priority=" + this.getPriority() + ", popup=" + this.getPopup() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NavigationButton)) {
            return false;
        }
        NavigationButton other = (NavigationButton)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$tooltip = this.getTooltip();
        String other$tooltip = other.getTooltip();
        return !(this$tooltip == null ? other$tooltip != null : !this$tooltip.equals(other$tooltip));
    }

    protected boolean canEqual(Object other) {
        return other instanceof NavigationButton;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $tooltip = this.getTooltip();
        result = result * 59 + ($tooltip == null ? 43 : $tooltip.hashCode());
        return result;
    }

    public static class NavigationButtonBuilder {
        private BufferedImage icon;
        private boolean tab$set;
        private boolean tab$value;
        private boolean tooltip$set;
        private String tooltip$value;
        private Runnable onReady;
        private boolean selected;
        private Runnable onClick;
        private Runnable onSelect;
        private PluginPanel panel;
        private int priority;
        private Map<String, Runnable> popup;

        NavigationButtonBuilder() {
        }

        public NavigationButtonBuilder icon(BufferedImage icon) {
            this.icon = icon;
            return this;
        }

        public NavigationButtonBuilder tab(boolean tab) {
            this.tab$value = tab;
            this.tab$set = true;
            return this;
        }

        public NavigationButtonBuilder tooltip(String tooltip) {
            this.tooltip$value = tooltip;
            this.tooltip$set = true;
            return this;
        }

        public NavigationButtonBuilder onReady(Runnable onReady) {
            this.onReady = onReady;
            return this;
        }

        public NavigationButtonBuilder selected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public NavigationButtonBuilder onClick(Runnable onClick) {
            this.onClick = onClick;
            return this;
        }

        public NavigationButtonBuilder onSelect(Runnable onSelect) {
            this.onSelect = onSelect;
            return this;
        }

        public NavigationButtonBuilder panel(PluginPanel panel) {
            this.panel = panel;
            return this;
        }

        public NavigationButtonBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public NavigationButtonBuilder popup(Map<String, Runnable> popup) {
            this.popup = popup;
            return this;
        }

        public NavigationButton build() {
            boolean tab$value = this.tab$value;
            if (!this.tab$set) {
                tab$value = NavigationButton.$default$tab();
            }
            String tooltip$value = this.tooltip$value;
            if (!this.tooltip$set) {
                tooltip$value = NavigationButton.$default$tooltip();
            }
            return new NavigationButton(this.icon, tab$value, tooltip$value, this.onReady, this.selected, this.onClick, this.onSelect, this.panel, this.priority, this.popup);
        }

        public String toString() {
            return "NavigationButton.NavigationButtonBuilder(icon=" + this.icon + ", tab$value=" + this.tab$value + ", tooltip$value=" + this.tooltip$value + ", onReady=" + this.onReady + ", selected=" + this.selected + ", onClick=" + this.onClick + ", onSelect=" + this.onSelect + ", panel=" + this.panel + ", priority=" + this.priority + ", popup=" + this.popup + ")";
        }
    }
}

