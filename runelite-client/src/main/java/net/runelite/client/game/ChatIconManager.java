/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.EnumComposition
 *  net.runelite.api.FriendsChatRank
 *  net.runelite.api.GameState
 *  net.runelite.api.IndexedSprite
 *  net.runelite.api.clan.ClanTitle
 */
package net.runelite.client.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.GameState;
import net.runelite.api.IndexedSprite;
import net.runelite.api.clan.ClanTitle;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.util.ImageUtil;

@Singleton
public class ChatIconManager {
    private static final Dimension IMAGE_DIMENSION = new Dimension(11, 11);
    private static final Color IMAGE_OUTLINE_COLOR = new Color(33, 33, 33);
    private final Client client;
    private final SpriteManager spriteManager;
    private BufferedImage[] friendsChatRankImages;
    private BufferedImage[] clanRankImages;
    private int friendsChatOffset = -1;
    private int clanOffset = -1;

    @Inject
    private ChatIconManager(Client client, SpriteManager spriteManager, ClientThread clientThread) {
        this.client = client;
        this.spriteManager = spriteManager;
        clientThread.invokeLater(() -> {
            if (client.getGameState().getState() >= GameState.LOGIN_SCREEN.getState()) {
                this.loadRankIcons();
                return true;
            }
            return false;
        });
    }

    @Nullable
    public BufferedImage getRankImage(FriendsChatRank friendsChatRank) {
        if (friendsChatRank == FriendsChatRank.UNRANKED) {
            return null;
        }
        return this.friendsChatRankImages[friendsChatRank.ordinal() - 1];
    }

    @Nullable
    public BufferedImage getRankImage(ClanTitle clanTitle) {
        int rank = clanTitle.getId();
        int idx = ChatIconManager.clanRankToIdx(rank);
        return this.clanRankImages[idx];
    }

    public int getIconNumber(FriendsChatRank friendsChatRank) {
        return this.friendsChatOffset == -1 ? -1 : this.friendsChatOffset + friendsChatRank.ordinal() - 1;
    }

    public int getIconNumber(ClanTitle clanTitle) {
        int rank = clanTitle.getId();
        return this.clanOffset == -1 ? -1 : this.clanOffset + ChatIconManager.clanRankToIdx(rank);
    }

    private void loadRankIcons() {
        int i;
        EnumComposition friendsChatIcons = this.client.getEnum(1543);
        EnumComposition clanIcons = this.client.getEnum(3798);
        Object[] modIcons = this.client.getModIcons();
        this.friendsChatOffset = modIcons.length;
        this.clanOffset = this.friendsChatOffset + friendsChatIcons.size();
        IndexedSprite blank = ImageUtil.getImageIndexedSprite(new BufferedImage(modIcons[0].getWidth(), modIcons[0].getHeight(), 2), this.client);
        modIcons = Arrays.copyOf(modIcons, this.friendsChatOffset + friendsChatIcons.size() + clanIcons.size());
        Arrays.fill(modIcons, this.friendsChatOffset, modIcons.length, (Object)blank);
        this.client.setModIcons((IndexedSprite[])modIcons);
        this.friendsChatRankImages = new BufferedImage[friendsChatIcons.size()];
        this.clanRankImages = new BufferedImage[clanIcons.size()];
        for (i = 0; i < friendsChatIcons.size(); ++i) {
            int fi = i;
            this.spriteManager.getSpriteAsync(friendsChatIcons.getIntValue(friendsChatIcons.getKeys()[i]), 0, sprite -> {
                IndexedSprite[] modIcons = this.client.getModIcons();
                this.friendsChatRankImages[fi] = ChatIconManager.friendsChatImageFromSprite(sprite);
                modIcons[this.friendsChatOffset + fi] = ImageUtil.getImageIndexedSprite(this.friendsChatRankImages[fi], this.client);
            });
        }
        for (i = 0; i < clanIcons.size(); ++i) {
            int key = clanIcons.getKeys()[i];
            int idx = ChatIconManager.clanRankToIdx(key);
            assert (idx >= 0 && idx < clanIcons.size());
            this.spriteManager.getSpriteAsync(clanIcons.getIntValue(key), 0, sprite -> {
                BufferedImage img;
                IndexedSprite[] modIcons = this.client.getModIcons();
                this.clanRankImages[idx] = img = ImageUtil.resizeCanvas(sprite, ChatIconManager.IMAGE_DIMENSION.width, ChatIconManager.IMAGE_DIMENSION.height);
                modIcons[this.clanOffset + idx] = ImageUtil.getImageIndexedSprite(img, this.client);
            });
        }
    }

    private static BufferedImage friendsChatImageFromSprite(BufferedImage sprite) {
        BufferedImage canvas = ImageUtil.resizeCanvas(sprite, ChatIconManager.IMAGE_DIMENSION.width, ChatIconManager.IMAGE_DIMENSION.height);
        return ImageUtil.outlineImage(canvas, IMAGE_OUTLINE_COLOR);
    }

    private static int clanRankToIdx(int key) {
        return key < 0 ? ~key : key + 5;
    }
}

