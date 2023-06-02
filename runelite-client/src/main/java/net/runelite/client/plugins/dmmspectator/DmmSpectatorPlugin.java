/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.ScriptCallbackEvent
 *  net.runelite.api.events.VarbitChanged
 *  net.runelite.api.vars.AccountType
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.dmmspectator;

import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.vars.AccountType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.plugins.dmmspectator.DmmSpectatorButton;
import net.runelite.client.plugins.dmmspectator.DmmSpectatorPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Deadman Spectator Tools", tags={"spectate", "spectator", "deadman"}, hidden=true)
public class DmmSpectatorPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(DmmSpectatorPlugin.class);
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private EventBus eventBus;
    @Inject
    private ConfigManager configManager;
    @Inject
    private ChatMessageManager chatMessageManager;
    private NavigationButton navButton;
    private DmmSpectatorButton permadeathButton;
    private DmmSpectatorButton hotspotsButton;
    private DmmSpectatorButton finalsButton;
    private DmmSpectatorButton playerNamesButton;
    private DmmSpectatorButton publicChatButton;
    private AccountType previousGameMode = null;

    @Override
    protected void startUp() throws Exception {
        this.permadeathButton = new DmmSpectatorButton("Player tools");
        this.hotspotsButton = new DmmSpectatorButton("Hotspots");
        this.finalsButton = new DmmSpectatorButton("1v1s");
        this.playerNamesButton = new DmmSpectatorButton("Toggle player names");
        this.publicChatButton = new DmmSpectatorButton("Toggle public names");
        DmmSpectatorPanel panel = (DmmSpectatorPanel)this.injector.getInstance(DmmSpectatorPanel.class);
        BufferedImage icon = ImageUtil.loadImageResource(DevToolsPlugin.class, "devtools_icon.png");
        this.navButton = NavigationButton.builder().tooltip("Spectator tools").icon(icon).priority(1).panel(panel).build();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGIN_SCREEN && this.previousGameMode == AccountType.DEADMAN_SPECTATOR) {
            this.clientToolbar.removeNavigation(this.navButton);
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        AccountType accountType;
        if (event.getIndex() == VarPlayer.MISC_DATA_2.getId() && (accountType = this.client.getAccountType()) != this.previousGameMode) {
            if (accountType == AccountType.DEADMAN_SPECTATOR) {
                this.clientToolbar.addNavigation(this.navButton);
                this.client.setDisableMinimenu(false);
            } else {
                this.clientToolbar.removeNavigation(this.navButton);
                this.client.setDisableMinimenu(true);
            }
            this.previousGameMode = accountType;
        }
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent event) {
        String eventName = event.getEventName();
        int[] intStack = this.client.getIntStack();
        String[] stringStack = this.client.getStringStack();
        int intStackSize = this.client.getIntStackSize();
        int stringStackSize = this.client.getStringStackSize();
        switch (eventName) {
            case "deadman_spectator_transmit_hotspots": {
                String hotspots = stringStack[stringStackSize - 1];
                System.out.println("Received deadman_spectator_transmit_hotspots(" + hotspots + ")");
                break;
            }
        }
    }

    public Client getClient() {
        return this.client;
    }

    public ClientThread getClientThread() {
        return this.clientThread;
    }

    public ClientToolbar getClientToolbar() {
        return this.clientToolbar;
    }

    public OverlayManager getOverlayManager() {
        return this.overlayManager;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public ChatMessageManager getChatMessageManager() {
        return this.chatMessageManager;
    }

    public NavigationButton getNavButton() {
        return this.navButton;
    }

    public DmmSpectatorButton getPermadeathButton() {
        return this.permadeathButton;
    }

    public DmmSpectatorButton getHotspotsButton() {
        return this.hotspotsButton;
    }

    public DmmSpectatorButton getFinalsButton() {
        return this.finalsButton;
    }

    public DmmSpectatorButton getPlayerNamesButton() {
        return this.playerNamesButton;
    }

    public DmmSpectatorButton getPublicChatButton() {
        return this.publicChatButton;
    }

    public AccountType getPreviousGameMode() {
        return this.previousGameMode;
    }
}

