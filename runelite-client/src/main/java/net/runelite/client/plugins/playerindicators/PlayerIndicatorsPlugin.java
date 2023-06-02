/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.FriendsChatRank
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.Player
 *  net.runelite.api.clan.ClanTitle
 *  net.runelite.api.events.ClientTick
 *  net.runelite.api.events.ScriptPostFired
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.playerindicators;

import com.google.inject.Provides;
import java.awt.Color;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.clan.ClanTitle;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.party.PartyService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.playerindicators.PlayerIndicatorsConfig;
import net.runelite.client.plugins.playerindicators.PlayerIndicatorsMinimapOverlay;
import net.runelite.client.plugins.playerindicators.PlayerIndicatorsOverlay;
import net.runelite.client.plugins.playerindicators.PlayerIndicatorsService;
import net.runelite.client.plugins.playerindicators.PlayerIndicatorsTileOverlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;

@PluginDescriptor(name="Player Indicators", description="Highlight players on-screen and/or on the minimap", tags={"highlight", "minimap", "overlay", "players"})
public class PlayerIndicatorsPlugin
extends Plugin {
    private static final String TRADING_WITH_TEXT = "Trading with: ";
    private static final String TITLE_START = "<title>";
    private static final String TITLE_END = "</title>";
    private static final int TITLE_END_LENGTH = "</title>".length();
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private PlayerIndicatorsConfig config;
    @Inject
    private PlayerIndicatorsOverlay playerIndicatorsOverlay;
    @Inject
    private PlayerIndicatorsTileOverlay playerIndicatorsTileOverlay;
    @Inject
    private PlayerIndicatorsMinimapOverlay playerIndicatorsMinimapOverlay;
    @Inject
    private PlayerIndicatorsService playerIndicatorsService;
    @Inject
    private Client client;
    @Inject
    private ChatIconManager chatIconManager;
    @Inject
    private ClientThread clientThread;
    @Inject
    private PartyService partyService;

    @Provides
    PlayerIndicatorsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(PlayerIndicatorsConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.overlayManager.add(this.playerIndicatorsOverlay);
        this.overlayManager.add(this.playerIndicatorsTileOverlay);
        this.overlayManager.add(this.playerIndicatorsMinimapOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.playerIndicatorsOverlay);
        this.overlayManager.remove(this.playerIndicatorsTileOverlay);
        this.overlayManager.remove(this.playerIndicatorsMinimapOverlay);
    }

    @Subscribe
    public void onClientTick(ClientTick clientTick) {
        MenuEntry[] menuEntries;
        if (this.client.isMenuOpen()) {
            return;
        }
        for (MenuEntry entry : menuEntries = this.client.getMenuEntries()) {
            Decorations decorations;
            MenuAction type = entry.getType();
            if (type != MenuAction.WALK && type != MenuAction.WIDGET_TARGET_ON_PLAYER && type != MenuAction.ITEM_USE_ON_PLAYER && type != MenuAction.PLAYER_FIRST_OPTION && type != MenuAction.PLAYER_SECOND_OPTION && type != MenuAction.PLAYER_THIRD_OPTION && type != MenuAction.PLAYER_FOURTH_OPTION && type != MenuAction.PLAYER_FIFTH_OPTION && type != MenuAction.PLAYER_SIXTH_OPTION && type != MenuAction.PLAYER_SEVENTH_OPTION && type != MenuAction.PLAYER_EIGTH_OPTION && type != MenuAction.RUNELITE_PLAYER) continue;
            Player[] players = this.client.getCachedPlayers();
            Player player = null;
            int identifier = entry.getIdentifier();
            if (type == MenuAction.WALK) {
                --identifier;
            }
            if (identifier >= 0 && identifier < players.length) {
                player = players[identifier];
            }
            if (player == null || (decorations = this.getDecorations(player)) == null) continue;
            String oldTarget = entry.getTarget();
            String newTarget = this.decorateTarget(oldTarget, decorations);
            entry.setTarget(newTarget);
        }
    }

    private Decorations getDecorations(Player player) {
        ClanTitle clanTitle;
        FriendsChatRank rank;
        boolean isPartyMember;
        int image = -1;
        Color color = null;
        boolean bl = isPartyMember = this.partyService.isInParty() && player.getName() != null && this.config.highlightPartyMembers() && this.partyService.getMemberByDisplayName(player.getName()) != null;
        if (isPartyMember) {
            color = this.config.getPartyMemberColor();
        } else if (player.isFriend() && this.config.highlightFriends()) {
            color = this.config.getFriendColor();
        } else if (player.isFriendsChatMember() && this.config.highlightFriendsChat()) {
            color = this.config.getFriendsChatMemberColor();
        } else if (player.getTeam() > 0 && this.client.getLocalPlayer().getTeam() == player.getTeam() && this.config.highlightTeamMembers()) {
            color = this.config.getTeamMemberColor();
        } else if (player.isClanMember() && this.config.highlightClanMembers()) {
            color = this.config.getClanMemberColor();
        } else if (!player.isFriendsChatMember() && !player.isClanMember() && this.config.highlightOthers()) {
            color = this.config.getOthersColor();
        }
        if (player.isFriendsChatMember() && this.config.showFriendsChatRanks() && (rank = this.playerIndicatorsService.getFriendsChatRank(player)) != FriendsChatRank.UNRANKED) {
            image = this.chatIconManager.getIconNumber(rank);
        }
        if (player.isClanMember() && this.config.showClanChatRanks() && image == -1 && (clanTitle = this.playerIndicatorsService.getClanTitle(player)) != null) {
            image = this.chatIconManager.getIconNumber(clanTitle);
        }
        if (image == -1 && color == null) {
            return null;
        }
        return new Decorations(image, color);
    }

    private String decorateTarget(String oldTarget, Decorations decorations) {
        String newTarget = oldTarget;
        if (decorations.getColor() != null && this.config.colorPlayerMenu()) {
            int titleIdx = oldTarget.indexOf(TITLE_START);
            int idx = oldTarget.indexOf(62);
            if (idx != -1) {
                newTarget = oldTarget.substring(idx + 1);
            }
            String prefixTitle = null;
            if (titleIdx != -1 && titleIdx == idx + 1) {
                int titleEndIdx = newTarget.indexOf(TITLE_END);
                prefixTitle = newTarget.substring(0, titleEndIdx + TITLE_END_LENGTH);
                newTarget = newTarget.substring(titleEndIdx + TITLE_END_LENGTH);
            }
            newTarget = prefixTitle != null ? prefixTitle + ColorUtil.prependColorTag(newTarget, decorations.getColor()) : ColorUtil.prependColorTag(newTarget, decorations.getColor());
        }
        if (decorations.getImage() != -1) {
            newTarget = "<img=" + decorations.getImage() + ">" + newTarget;
        }
        return newTarget;
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired event) {
        if (event.getScriptId() == 755) {
            this.clientThread.invokeLater(() -> {
                Widget tradeTitle = this.client.getWidget(WidgetInfo.TRADE_WINDOW_HEADER);
                String header = tradeTitle.getText();
                String playerName = header.substring(TRADING_WITH_TEXT.length());
                Player targetPlayer = this.findPlayer(playerName);
                if (targetPlayer == null) {
                    return;
                }
                Decorations playerColor = this.getDecorations(targetPlayer);
                if (playerColor != null) {
                    tradeTitle.setText(TRADING_WITH_TEXT + ColorUtil.wrapWithColorTag(playerName, playerColor.color));
                }
            });
        }
    }

    private Player findPlayer(String name) {
        for (Player player : this.client.getPlayers()) {
            if (!player.getName().equals(name)) continue;
            return player;
        }
        return null;
    }

    private static final class Decorations {
        private final int image;
        private final Color color;

        public Decorations(int image, Color color) {
            this.image = image;
            this.color = color;
        }

        public int getImage() {
            return this.image;
        }

        public Color getColor() {
            return this.color;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Decorations)) {
                return false;
            }
            Decorations other = (Decorations)o;
            if (this.getImage() != other.getImage()) {
                return false;
            }
            Color this$color = this.getColor();
            Color other$color = other.getColor();
            return !(this$color == null ? other$color != null : !((Object)this$color).equals(other$color));
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getImage();
            Color $color = this.getColor();
            result = result * 59 + ($color == null ? 43 : ((Object)$color).hashCode());
            return result;
        }

        public String toString() {
            return "PlayerIndicatorsPlugin.Decorations(image=" + this.getImage() + ", color=" + this.getColor() + ")";
        }
    }
}

