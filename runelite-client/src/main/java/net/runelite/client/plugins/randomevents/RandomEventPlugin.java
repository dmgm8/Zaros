/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Actor
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.events.InteractingChanged
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.NpcDespawned
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.randomevents;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.randomevents.RandomEventConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Random Events", description="Notify when random events appear and remove talk/dismiss options on events that aren't yours.", enabledByDefault=false)
public class RandomEventPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(RandomEventPlugin.class);
    private static final Set<Integer> EVENT_NPCS = ImmutableSet.of((Object)6747, (Object)5426, (Object)307, (Object)314, (Object)322, (Object)6749, (Object[])new Integer[]{390, 6754, 6744, 6748, 5429, 326, 327, 5438, 5441, 6746, 5437, 5440, 6750, 6751, 6752, 6753, 5436, 5439, 380, 6738, 6755, 375, 376, 5510, 6743});
    private static final Set<String> EVENT_OPTIONS = ImmutableSet.of((Object)"Talk-to", (Object)"Dismiss");
    private static final int RANDOM_EVENT_TIMEOUT = 150;
    private NPC currentRandomEvent;
    private int lastNotificationTick = -150;
    @Inject
    private Client client;
    @Inject
    private Notifier notifier;
    @Inject
    private RandomEventConfig config;

    @Provides
    RandomEventConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(RandomEventConfig.class);
    }

    @Override
    protected void shutDown() throws Exception {
        this.lastNotificationTick = 0;
        this.currentRandomEvent = null;
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {
        Actor source = event.getSource();
        Actor target = event.getTarget();
        Player player = this.client.getLocalPlayer();
        if (player == null || target != player || player.getInteracting() == source || !(source instanceof NPC) || !EVENT_NPCS.contains(((NPC)source).getId())) {
            return;
        }
        log.debug("Random event spawn: {}", (Object)source.getName());
        this.currentRandomEvent = (NPC)source;
        if (this.client.getTickCount() - this.lastNotificationTick > 150) {
            this.lastNotificationTick = this.client.getTickCount();
            if (this.shouldNotify(this.currentRandomEvent.getId())) {
                this.notifier.notify("Random event spawned: " + this.currentRandomEvent.getName());
            }
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        NPC npc = npcDespawned.getNpc();
        if (npc == this.currentRandomEvent) {
            this.currentRandomEvent = null;
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        NPC npc;
        if (event.getType() >= MenuAction.NPC_FIRST_OPTION.getId() && event.getType() <= MenuAction.NPC_FIFTH_OPTION.getId() && EVENT_OPTIONS.contains(event.getOption()) && (npc = event.getMenuEntry().getNpc()) != null && EVENT_NPCS.contains(npc.getId()) && npc != this.currentRandomEvent && this.config.removeMenuOptions()) {
            this.client.setMenuEntries(Arrays.copyOf(this.client.getMenuEntries(), this.client.getMenuEntries().length - 1));
        }
    }

    private boolean shouldNotify(int id) {
        if (this.config.notifyAllEvents()) {
            return true;
        }
        switch (id) {
            case 6747: {
                return this.config.notifyBeekeeper();
            }
            case 6743: {
                return this.config.notifyDemon();
            }
            case 6748: {
                return this.config.notifyForester();
            }
            case 5429: {
                return this.config.notifyFrog();
            }
            case 326: 
            case 327: {
                return this.config.notifyGenie();
            }
            case 307: 
            case 314: {
                return this.config.notifyJekyll();
            }
            case 390: 
            case 6754: {
                return this.config.notifyBob();
            }
            case 6746: {
                return this.config.notifyGravedigger();
            }
            case 6750: 
            case 6751: 
            case 6752: 
            case 6753: {
                return this.config.notifyMoM();
            }
            case 6755: {
                return this.config.notifyQuiz();
            }
            case 6749: {
                return this.config.notifyDunce();
            }
            case 5510: {
                return this.config.notifySandwich();
            }
        }
        return false;
    }
}

