/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.twitch;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.ChatboxInputListener;
import net.runelite.client.chat.CommandManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ChatboxInput;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.PrivateMessageInput;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.twitch.TwitchConfig;
import net.runelite.client.plugins.twitch.irc.TwitchIRCClient;
import net.runelite.client.plugins.twitch.irc.TwitchListener;
import net.runelite.client.task.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Twitch", description="Integrates Twitch chat", enabledByDefault=false, forceDisabled=true)
public class TwitchPlugin
extends Plugin
implements TwitchListener,
ChatboxInputListener {
    private static final Logger log = LoggerFactory.getLogger(TwitchPlugin.class);
    @Inject
    private TwitchConfig twitchConfig;
    @Inject
    private Client client;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private CommandManager commandManager;
    private TwitchIRCClient twitchIRCClient;

    @Override
    protected void startUp() {
        this.connect();
        this.commandManager.register(this);
    }

    @Override
    protected void shutDown() {
        if (this.twitchIRCClient != null) {
            this.twitchIRCClient.close();
            this.twitchIRCClient = null;
        }
        this.commandManager.unregister(this);
    }

    @Provides
    TwitchConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(TwitchConfig.class);
    }

    private synchronized void connect() {
        if (this.twitchIRCClient != null) {
            log.debug("Terminating Twitch client {}", (Object)this.twitchIRCClient);
            this.twitchIRCClient.close();
            this.twitchIRCClient = null;
        }
        if (!(Strings.isNullOrEmpty((String)this.twitchConfig.username()) || Strings.isNullOrEmpty((String)this.twitchConfig.oauthToken()) || Strings.isNullOrEmpty((String)this.twitchConfig.channel()))) {
            String channel = this.twitchConfig.channel().toLowerCase();
            if (!channel.startsWith("#")) {
                channel = "#" + channel;
            }
            log.debug("Connecting to Twitch as {}", (Object)this.twitchConfig.username());
            this.twitchIRCClient = new TwitchIRCClient(this, this.twitchConfig.username(), this.twitchConfig.oauthToken(), channel);
            this.twitchIRCClient.start();
        }
    }

    @Schedule(period=30L, unit=ChronoUnit.SECONDS, asynchronous=true)
    public void checkClient() {
        if (this.twitchIRCClient != null) {
            if (this.twitchIRCClient.isConnected()) {
                this.twitchIRCClient.pingCheck();
            }
            if (!this.twitchIRCClient.isConnected()) {
                log.debug("Reconnecting...");
                this.connect();
            }
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (!configChanged.getGroup().equals("twitch")) {
            return;
        }
        this.connect();
    }

    private void addChatMessage(String sender, String message) {
        String chatMessage = new ChatMessageBuilder().append(ChatColorType.NORMAL).append(message).build();
        this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHAT).sender("Twitch").name(sender).runeLiteFormattedMessage(chatMessage).timestamp((int)(System.currentTimeMillis() / 1000L)).build());
    }

    @Override
    public void privmsg(Map<String, String> tags, String message) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        String displayName = tags.get("display-name");
        this.addChatMessage(displayName, message);
    }

    @Override
    public void roomstate(Map<String, String> tags) {
        log.debug("Room state: {}", tags);
    }

    @Override
    public void usernotice(Map<String, String> tags, String message) {
        log.debug("Usernotice tags: {} message: {}", tags, (Object)message);
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        String sysmsg = tags.get("system-msg");
        this.addChatMessage("[System]", sysmsg);
    }

    @Override
    public boolean onChatboxInput(ChatboxInput chatboxInput) {
        String message = chatboxInput.getValue();
        if (message.startsWith("/t ")) {
            if ((message = message.substring(3)).isEmpty() || this.twitchIRCClient == null) {
                return true;
            }
            try {
                this.twitchIRCClient.privmsg(message);
                this.addChatMessage(this.twitchConfig.username(), message);
            }
            catch (IOException e) {
                log.warn("failed to send message", (Throwable)e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onPrivateMessageInput(PrivateMessageInput privateMessageInput) {
        return false;
    }
}

