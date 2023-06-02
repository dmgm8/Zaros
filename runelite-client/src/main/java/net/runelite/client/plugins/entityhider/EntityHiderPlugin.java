/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GraphicsObject
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Projectile
 *  net.runelite.api.Renderable
 */
package net.runelite.client.plugins.entityhider;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.Renderable;
import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.NpcUtil;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.entityhider.EntityHiderConfig;

@PluginDescriptor(name="Entity Hider", description="Hide players, NPCs, and/or projectiles", tags={"npcs", "players", "projectiles"}, enabledByDefault=false)
public class EntityHiderPlugin
extends Plugin {
    @Inject
    private Client client;
    @Inject
    private EntityHiderConfig config;
    @Inject
    private Hooks hooks;
    @Inject
    private NpcUtil npcUtil;
    private boolean hideOthers;
    private boolean hideOthers2D;
    private boolean hideFriends;
    private boolean hideFriendsChatMembers;
    private boolean hideClanMembers;
    private boolean hideIgnoredPlayers;
    private boolean hideLocalPlayer;
    private boolean hideLocalPlayer2D;
    private boolean hideNPCs;
    private boolean hideNPCs2D;
    private boolean hideDeadNpcs;
    private boolean hidePets;
    private boolean hideAttackers;
    private boolean hideProjectiles;
    private final Hooks.RenderableDrawListener drawListener = (arg_0, arg_1) -> this.shouldDraw(arg_0, arg_1);

    @Provides
    EntityHiderConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(EntityHiderConfig.class);
    }

    @Override
    protected void startUp() {
        this.updateConfig();
        this.hooks.registerRenderableDrawListener(this.drawListener);
    }

    @Override
    protected void shutDown() {
        this.hooks.unregisterRenderableDrawListener(this.drawListener);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e) {
        if (e.getGroup().equals("entityhider")) {
            this.updateConfig();
        }
    }

    private void updateConfig() {
        this.hideOthers = this.config.hideOthers();
        this.hideOthers2D = this.config.hideOthers2D();
        this.hideFriends = this.config.hideFriends();
        this.hideFriendsChatMembers = this.config.hideFriendsChatMembers();
        this.hideClanMembers = this.config.hideClanChatMembers();
        this.hideIgnoredPlayers = this.config.hideIgnores();
        this.hideLocalPlayer = this.config.hideLocalPlayer();
        this.hideLocalPlayer2D = this.config.hideLocalPlayer2D();
        this.hideNPCs = this.config.hideNPCs();
        this.hideNPCs2D = this.config.hideNPCs2D();
        this.hideDeadNpcs = this.config.hideDeadNpcs();
        this.hidePets = this.config.hidePets();
        this.hideAttackers = this.config.hideAttackers();
        this.hideProjectiles = this.config.hideProjectiles();
    }

    @VisibleForTesting
    boolean shouldDraw(Renderable renderable, boolean drawingUI) {
        if (renderable instanceof Player) {
            Player player = (Player)renderable;
            Player local = this.client.getLocalPlayer();
            if (player.getName() == null) {
                return true;
            }
            if (player == local) {
                return !(!drawingUI ? this.hideLocalPlayer : this.hideLocalPlayer2D);
            }
            if (this.hideAttackers && player.getInteracting() == local) {
                return false;
            }
            if (player.isFriend()) {
                return !this.hideFriends;
            }
            if (player.isFriendsChatMember()) {
                return !this.hideFriendsChatMembers;
            }
            if (player.isClanMember()) {
                return !this.hideClanMembers;
            }
            if (this.client.getIgnoreContainer().findByName(player.getName()) != null) {
                return !this.hideIgnoredPlayers;
            }
            return !(!drawingUI ? this.hideOthers : this.hideOthers2D);
        }
        if (renderable instanceof NPC) {
            NPC npc = (NPC)renderable;
            if (npc.getComposition().isFollower() && npc != this.client.getFollower()) {
                return !this.hidePets;
            }
            if (this.npcUtil.isDying(npc) && this.hideDeadNpcs) {
                return false;
            }
            if (npc.getInteracting() == this.client.getLocalPlayer()) {
                boolean b = this.hideAttackers;
                if (this.hideNPCs2D || this.hideNPCs) {
                    b &= drawingUI ? this.hideNPCs2D : this.hideNPCs;
                }
                return !b;
            }
            return !(!drawingUI ? this.hideNPCs : this.hideNPCs2D);
        }
        if (renderable instanceof Projectile) {
            return !this.hideProjectiles;
        }
        if (renderable instanceof GraphicsObject) {
            if (!this.hideDeadNpcs) {
                return true;
            }
            switch (((GraphicsObject)renderable).getId()) {
                case 1562: 
                case 1563: 
                case 1564: 
                case 1565: 
                case 1566: 
                case 1567: {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}

