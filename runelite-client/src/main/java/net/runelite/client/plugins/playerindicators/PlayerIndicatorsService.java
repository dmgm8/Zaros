/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.FriendsChatManager
 *  net.runelite.api.FriendsChatMember
 *  net.runelite.api.FriendsChatRank
 *  net.runelite.api.Player
 *  net.runelite.api.clan.ClanChannel
 *  net.runelite.api.clan.ClanChannelMember
 *  net.runelite.api.clan.ClanRank
 *  net.runelite.api.clan.ClanSettings
 *  net.runelite.api.clan.ClanTitle
 */
package net.runelite.client.plugins.playerindicators;

import java.awt.Color;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.Player;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.clan.ClanTitle;
import net.runelite.client.party.PartyService;
import net.runelite.client.plugins.playerindicators.PlayerIndicatorsConfig;
import net.runelite.client.util.Text;

@Singleton
public class PlayerIndicatorsService {
    private final Client client;
    private final PlayerIndicatorsConfig config;
    private final PartyService partyService;

    @Inject
    private PlayerIndicatorsService(Client client, PlayerIndicatorsConfig config, PartyService partyService) {
        this.config = config;
        this.client = client;
        this.partyService = partyService;
    }

    public void forEachPlayer(BiConsumer<Player, Color> consumer) {
        if (!(this.config.highlightOwnPlayer() || this.config.highlightFriendsChat() || this.config.highlightFriends() || this.config.highlightOthers() || this.config.highlightClanMembers() || this.config.highlightPartyMembers())) {
            return;
        }
        Player localPlayer = this.client.getLocalPlayer();
        for (Player player : this.client.getPlayers()) {
            boolean isTeamMember;
            if (player == null || player.getName() == null) continue;
            boolean isFriendsChatMember = player.isFriendsChatMember();
            boolean isClanMember = player.isClanMember();
            boolean bl = isTeamMember = localPlayer.getTeam() > 0 && localPlayer.getTeam() == player.getTeam();
            if (player == localPlayer) {
                if (!this.config.highlightOwnPlayer()) continue;
                consumer.accept(player, this.config.getOwnPlayerColor());
                continue;
            }
            if (this.partyService.isInParty() && this.config.highlightPartyMembers() && this.partyService.getMemberByDisplayName(player.getName()) != null) {
                consumer.accept(player, this.config.getPartyMemberColor());
                continue;
            }
            if (this.config.highlightFriends() && player.isFriend() && (!this.config.prioritizeTeamAndClan() || !isTeamMember && !isFriendsChatMember)) {
                consumer.accept(player, this.config.getFriendColor());
                continue;
            }
            if (this.config.highlightFriendsChat() && isFriendsChatMember) {
                consumer.accept(player, this.config.getFriendsChatMemberColor());
                continue;
            }
            if (this.config.highlightTeamMembers() && isTeamMember) {
                consumer.accept(player, this.config.getTeamMemberColor());
                continue;
            }
            if (this.config.highlightClanMembers() && isClanMember) {
                consumer.accept(player, this.config.getClanMemberColor());
                continue;
            }
            if (!this.config.highlightOthers() || isFriendsChatMember || isClanMember) continue;
            consumer.accept(player, this.config.getOthersColor());
        }
    }

    ClanTitle getClanTitle(Player player) {
        ClanChannel clanChannel = this.client.getClanChannel();
        ClanSettings clanSettings = this.client.getClanSettings();
        if (clanChannel == null || clanSettings == null) {
            return null;
        }
        ClanChannelMember member = clanChannel.findMember(player.getName());
        if (member == null) {
            return null;
        }
        ClanRank rank = member.getRank();
        return clanSettings.titleForRank(rank);
    }

    FriendsChatRank getFriendsChatRank(Player player) {
        FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
        if (friendsChatManager == null) {
            return FriendsChatRank.UNRANKED;
        }
        FriendsChatMember friendsChatMember = (FriendsChatMember)friendsChatManager.findByName(Text.removeTags(player.getName()));
        return friendsChatMember != null ? friendsChatMember.getRank() : FriendsChatRank.UNRANKED;
    }
}

