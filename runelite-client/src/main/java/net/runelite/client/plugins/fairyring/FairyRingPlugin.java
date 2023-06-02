/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.inject.Provides
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.ScriptEvent
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.events.WidgetLoaded
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.fairyring;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.util.Collection;
import java.util.TreeMap;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.ScriptEvent;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.game.chatbox.ChatboxTextInput;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.fairyring.FairyRingConfig;
import net.runelite.client.plugins.fairyring.FairyRings;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Fairy Rings", description="Show the location of the fairy ring teleport", tags={"teleportation"})
public class FairyRingPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(FairyRingPlugin.class);
    private static final String[] leftDial = new String[]{"A", "D", "C", "B"};
    private static final String[] middleDial = new String[]{"I", "L", "K", "J"};
    private static final String[] rightDial = new String[]{"P", "S", "R", "Q"};
    private static final int ENTRY_PADDING = 3;
    private static final String MENU_OPEN = "Open";
    private static final String MENU_CLOSE = "Close";
    @Inject
    private Client client;
    @Inject
    private FairyRingConfig config;
    @Inject
    private ChatboxPanelManager chatboxPanelManager;
    @Inject
    private ClientThread clientThread;
    private ChatboxTextInput searchInput = null;
    private Widget searchBtn;
    private Collection<CodeWidgets> codes = null;

    @Provides
    FairyRingConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(FairyRingConfig.class);
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        this.setWidgetTextToDestination();
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
        if (widgetLoaded.getGroupId() == 381) {
            this.setWidgetTextToDestination();
            Widget header = this.client.getWidget(WidgetInfo.FAIRY_RING_HEADER);
            if (header != null) {
                this.searchBtn = header.createChild(-1, 5);
                this.searchBtn.setSpriteId(1113);
                this.searchBtn.setOriginalWidth(17);
                this.searchBtn.setOriginalHeight(17);
                this.searchBtn.setOriginalX(11);
                this.searchBtn.setOriginalY(11);
                this.searchBtn.setHasListener(true);
                this.searchBtn.setAction(1, MENU_OPEN);
                this.searchBtn.setOnOpListener(new Object[]{this::menuOpen});
                this.searchBtn.setName("Search");
                this.searchBtn.revalidate();
                this.codes = null;
                if (this.config.autoOpen()) {
                    this.openSearch();
                }
            }
        }
    }

    private void menuOpen(ScriptEvent e) {
        this.openSearch();
        this.client.playSoundEffect(2266);
    }

    private void menuClose(ScriptEvent e) {
        this.updateFilter("");
        this.chatboxPanelManager.close();
        this.client.playSoundEffect(2266);
    }

    private void setWidgetTextToDestination() {
        Widget fairyRingTeleportButton = this.client.getWidget(WidgetInfo.FAIRY_RING_TELEPORT_BUTTON);
        if (fairyRingTeleportButton != null && !fairyRingTeleportButton.isHidden()) {
            String destination;
            try {
                FairyRings fairyRingDestination = this.getFairyRingDestination(this.client.getVarbitValue(3985), this.client.getVarbitValue(3986), this.client.getVarbitValue(3987));
                destination = fairyRingDestination.getDestination();
            }
            catch (IllegalArgumentException ex) {
                destination = "Invalid location";
            }
            fairyRingTeleportButton.setText(destination);
        }
    }

    private FairyRings getFairyRingDestination(int varbitValueDialLeft, int varbitValueDialMiddle, int varbitValueDialRight) {
        return FairyRings.valueOf(leftDial[varbitValueDialLeft] + middleDial[varbitValueDialMiddle] + rightDial[varbitValueDialRight]);
    }

    private void openSearch() {
        this.updateFilter("");
        this.searchBtn.setAction(1, MENU_CLOSE);
        this.searchBtn.setOnOpListener(new Object[]{this::menuClose});
        this.searchInput = this.chatboxPanelManager.openTextInput("Filter fairy rings").onChanged(s -> this.clientThread.invokeLater(() -> this.updateFilter((String)s))).onDone(s -> false).onClose(() -> {
            this.clientThread.invokeLater(() -> this.updateFilter(""));
            this.searchBtn.setOnOpListener(new Object[]{this::menuOpen});
            this.searchBtn.setAction(1, MENU_OPEN);
        }).build();
    }

    @Subscribe
    public void onGameTick(GameTick t) {
        boolean chatboxOpen;
        Widget fairyRingTeleportButton = this.client.getWidget(WidgetInfo.FAIRY_RING_TELEPORT_BUTTON);
        boolean fairyRingWidgetOpen = fairyRingTeleportButton != null && !fairyRingTeleportButton.isHidden();
        boolean bl = chatboxOpen = this.searchInput != null && this.chatboxPanelManager.getCurrentInput() == this.searchInput;
        if (!fairyRingWidgetOpen && chatboxOpen) {
            this.chatboxPanelManager.close();
        }
    }

    private void updateFilter(String filter) {
        filter = filter.toLowerCase();
        Widget list = this.client.getWidget(WidgetInfo.FAIRY_RING_LIST);
        Widget favorites = this.client.getWidget(WidgetInfo.FAIRY_RING_FAVORITES);
        if (list == null) {
            return;
        }
        if (this.codes != null && this.codes.stream().noneMatch(w -> {
            Widget codeWidget = w.getCode();
            if (codeWidget == null) {
                return false;
            }
            return list.getChild(codeWidget.getIndex()) == codeWidget;
        })) {
            this.codes = null;
        }
        if (this.codes == null) {
            TreeMap<Integer, CodeWidgets> codeMap = new TreeMap<Integer, CodeWidgets>();
            for (Widget w2 : list.getStaticChildren()) {
                if (w2.isSelfHidden()) continue;
                if (w2.getSpriteId() != -1) {
                    codeMap.computeIfAbsent(w2.getRelativeY(), k -> new CodeWidgets()).setFavorite(w2);
                    continue;
                }
                if (Strings.isNullOrEmpty((String)w2.getText())) continue;
                codeMap.computeIfAbsent(w2.getRelativeY(), k -> new CodeWidgets()).setDescription(w2);
            }
            for (Widget w2 : list.getDynamicChildren()) {
                if (w2.isSelfHidden()) continue;
                CodeWidgets c = codeMap.computeIfAbsent(w2.getRelativeY(), k -> new CodeWidgets());
                c.setCode(w2);
            }
            this.codes = codeMap.values();
        }
        int y = 0;
        if (favorites != null) {
            boolean hide = !filter.isEmpty();
            favorites.setHidden(hide);
            if (!hide) {
                y += favorites.getOriginalHeight() + 3;
            }
        }
        for (CodeWidgets c : this.codes) {
            boolean hidden;
            String code = Text.removeTags(c.getDescription().getName()).replaceAll(" ", "");
            String tags = null;
            if (!code.isEmpty()) {
                try {
                    FairyRings ring = FairyRings.valueOf(code);
                    tags = ring.getTags();
                }
                catch (IllegalArgumentException e) {
                    log.warn("Unable to find ring with code '{}'", (Object)code, (Object)e);
                }
            }
            boolean bl = hidden = !filter.isEmpty() && !Text.removeTags(c.getDescription().getText()).toLowerCase().contains(filter) && !code.toLowerCase().contains(filter) && (tags == null || !tags.contains(filter));
            if (c.getCode() != null) {
                c.getCode().setHidden(hidden);
                c.getCode().setOriginalY(y);
            }
            if (c.getFavorite() != null) {
                c.getFavorite().setHidden(hidden);
                c.getFavorite().setOriginalY(y);
            }
            c.getDescription().setHidden(hidden);
            c.getDescription().setOriginalY(y);
            if (hidden) continue;
            y += c.getDescription().getHeight() + 3;
        }
        if ((y -= 3) < 0) {
            y = 0;
        }
        int newHeight = 0;
        if (list.getScrollHeight() > 0) {
            newHeight = list.getScrollY() * y / list.getScrollHeight();
        }
        list.setScrollHeight(y);
        list.revalidateScroll();
        this.client.runScript(new Object[]{72, WidgetInfo.FAIRY_RING_LIST_SCROLLBAR.getId(), WidgetInfo.FAIRY_RING_LIST.getId(), newHeight});
    }

    private static class CodeWidgets {
        @Nullable
        private Widget favorite;
        @Nullable
        private Widget code;
        private Widget description;

        @Nullable
        public Widget getFavorite() {
            return this.favorite;
        }

        @Nullable
        public Widget getCode() {
            return this.code;
        }

        public Widget getDescription() {
            return this.description;
        }

        public void setFavorite(@Nullable Widget favorite) {
            this.favorite = favorite;
        }

        public void setCode(@Nullable Widget code) {
            this.code = code;
        }

        public void setDescription(Widget description) {
            this.description = description;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof CodeWidgets)) {
                return false;
            }
            CodeWidgets other = (CodeWidgets)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Widget this$favorite = this.getFavorite();
            Widget other$favorite = other.getFavorite();
            if (this$favorite == null ? other$favorite != null : !this$favorite.equals((Object)other$favorite)) {
                return false;
            }
            Widget this$code = this.getCode();
            Widget other$code = other.getCode();
            if (this$code == null ? other$code != null : !this$code.equals((Object)other$code)) {
                return false;
            }
            Widget this$description = this.getDescription();
            Widget other$description = other.getDescription();
            return !(this$description == null ? other$description != null : !this$description.equals((Object)other$description));
        }

        protected boolean canEqual(Object other) {
            return other instanceof CodeWidgets;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Widget $favorite = this.getFavorite();
            result = result * 59 + ($favorite == null ? 43 : $favorite.hashCode());
            Widget $code = this.getCode();
            result = result * 59 + ($code == null ? 43 : $code.hashCode());
            Widget $description = this.getDescription();
            result = result * 59 + ($description == null ? 43 : $description.hashCode());
            return result;
        }

        public String toString() {
            return "FairyRingPlugin.CodeWidgets(favorite=" + (Object)this.getFavorite() + ", code=" + (Object)this.getCode() + ", description=" + (Object)this.getDescription() + ")";
        }
    }
}

