/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Provider
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.IconID
 *  net.runelite.api.MenuAction
 *  net.runelite.api.Player
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.hiscore;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.SwingUtilities;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.IconID;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.hiscore.HiscoreEndpoint;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.hiscore.HiscoreConfig;
import net.runelite.client.plugins.hiscore.HiscorePanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

@PluginDescriptor(name="HiScore", description="Enable the HiScore panel and an optional Lookup option on players", tags={"panel", "players"}, loadWhenOutdated=true)
public class HiscorePlugin
extends Plugin {
    private static final String LOOKUP = "Lookup";
    private static final Pattern BOUNTY_PATTERN = Pattern.compile("<col=ff0000>You've been assigned a target: (.*)</col>");
    @Inject
    @Nullable
    private Client client;
    @Inject
    private Provider<MenuManager> menuManager;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private HiscoreConfig config;
    private NavigationButton navButton;
    private HiscorePanel hiscorePanel;
    private HiscoreEndpoint localHiscoreEndpoint;

    @Provides
    HiscoreConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(HiscoreConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.hiscorePanel = (HiscorePanel)this.injector.getInstance(HiscorePanel.class);
        BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "normal.png");
        this.navButton = NavigationButton.builder().tooltip("Hiscore").icon(icon).priority(5).panel(this.hiscorePanel).build();
        this.clientToolbar.addNavigation(this.navButton);
        if (this.config.playerOption() && this.client != null) {
            ((MenuManager)this.menuManager.get()).addPlayerMenuItem(LOOKUP);
        }
    }

    @Override
    protected void shutDown() throws Exception {
        this.hiscorePanel.shutdown();
        this.clientToolbar.removeNavigation(this.navButton);
        if (this.client != null) {
            ((MenuManager)this.menuManager.get()).removePlayerMenuItem(LOOKUP);
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("hiscore") && this.client != null) {
            ((MenuManager)this.menuManager.get()).removePlayerMenuItem(LOOKUP);
            if (this.config.playerOption()) {
                ((MenuManager)this.menuManager.get()).addPlayerMenuItem(LOOKUP);
            }
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        if (event.getType() != MenuAction.CC_OP.getId() && event.getType() != MenuAction.CC_OP_LOW_PRIORITY.getId() || !this.config.menuOption()) {
            return;
        }
        String option = event.getOption();
        int componentId = event.getActionParam1();
        int groupId = WidgetInfo.TO_GROUP((int)componentId);
        if (groupId == WidgetInfo.FRIENDS_LIST.getGroupId() && option.equals("Delete") || groupId == WidgetInfo.FRIENDS_CHAT.getGroupId() && (option.equals("Add ignore") || option.equals("Remove friend")) || groupId == WidgetInfo.CHATBOX.getGroupId() && (option.equals("Add ignore") || option.equals("Message")) || groupId == WidgetInfo.IGNORE_LIST.getGroupId() && option.equals("Delete") || (componentId == WidgetInfo.CLAN_MEMBER_LIST.getId() || componentId == WidgetInfo.CLAN_GUEST_MEMBER_LIST.getId()) && (option.equals("Add ignore") || option.equals("Remove friend")) || groupId == WidgetInfo.PRIVATE_CHAT_MESSAGE.getGroupId() && (option.equals("Add ignore") || option.equals("Message")) || groupId == 726 && (option.equals("Add friend") || option.equals("Remove friend") || option.equals("Remove ignore"))) {
            this.client.createMenuEntry(-2).setOption(LOOKUP).setTarget(event.getTarget()).setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier()).onClick(e -> {
                HiscoreEndpoint endpoint = this.findHiscoreEndpointFromPlayerName(e.getTarget());
                String target = Text.removeTags(e.getTarget());
                this.lookupPlayer(target, endpoint);
            });
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (event.getMenuAction() == MenuAction.RUNELITE_PLAYER && event.getMenuOption().equals(LOOKUP)) {
            Player player = event.getMenuEntry().getPlayer();
            if (player == null) {
                return;
            }
            String target = player.getName();
            HiscoreEndpoint endpoint = this.getWorldEndpoint();
            this.lookupPlayer(target, endpoint);
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (!this.config.bountylookup() || !event.getType().equals((Object)ChatMessageType.GAMEMESSAGE)) {
            return;
        }
        String message = event.getMessage();
        Matcher m = BOUNTY_PATTERN.matcher(message);
        if (m.matches()) {
            this.lookupPlayer(m.group(1), HiscoreEndpoint.NORMAL);
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        this.localHiscoreEndpoint = this.findHiscoreEndpointFromLocalPlayer();
    }

    private void lookupPlayer(String playerName, HiscoreEndpoint endpoint) {
        SwingUtilities.invokeLater(() -> {
            if (!this.navButton.isSelected()) {
                this.navButton.getOnSelect().run();
            }
            this.hiscorePanel.lookup(playerName, endpoint);
        });
    }

    HiscoreEndpoint getWorldEndpoint() {
        if (this.client != null) {
            return HiscoreEndpoint.fromWorldTypes(this.client.getWorldType());
        }
        return HiscoreEndpoint.NORMAL;
    }

    private HiscoreEndpoint findHiscoreEndpointFromLocalPlayer() {
        HiscoreEndpoint profile = this.getWorldEndpoint();
        if (profile != HiscoreEndpoint.NORMAL) {
            return profile;
        }
        if (this.client != null) {
            switch (this.client.getAccountType()) {
                case IRONMAN: {
                    return HiscoreEndpoint.IRONMAN;
                }
                case ULTIMATE_IRONMAN: {
                    return HiscoreEndpoint.ULTIMATE_IRONMAN;
                }
                case HARDCORE_IRONMAN: {
                    return HiscoreEndpoint.HARDCORE_IRONMAN;
                }
            }
        }
        return HiscoreEndpoint.NORMAL;
    }

    private HiscoreEndpoint findHiscoreEndpointFromPlayerName(String name) {
        if (name.contains(IconID.IRONMAN.toString())) {
            return HiscoreEndpoint.IRONMAN;
        }
        if (name.contains(IconID.ULTIMATE_IRONMAN.toString())) {
            return HiscoreEndpoint.ULTIMATE_IRONMAN;
        }
        if (name.contains(IconID.HARDCORE_IRONMAN.toString())) {
            return HiscoreEndpoint.HARDCORE_IRONMAN;
        }
        if (name.contains(IconID.LEAGUE.toString())) {
            return HiscoreEndpoint.LEAGUE;
        }
        return HiscoreEndpoint.NORMAL;
    }

    public HiscoreEndpoint getLocalHiscoreEndpoint() {
        return this.localHiscoreEndpoint;
    }
}

