/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  joptsimple.internal.Strings
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.IndexedSprite
 *  net.runelite.api.MessageNode
 *  net.runelite.api.Player
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.OverheadTextChanged
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.emojis;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
import joptsimple.internal.Strings;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.IndexedSprite;
import net.runelite.api.MessageNode;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.emojis.Emoji;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Emojis", description="Replaces common emoticons such as :) with their corresponding emoji in the chat", enabledByDefault=false)
public class EmojiPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(EmojiPlugin.class);
    private static final Pattern WHITESPACE_REGEXP = Pattern.compile("[\\s\\u00A0]");
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    private int modIconsStart = -1;

    @Override
    protected void startUp() {
        this.clientThread.invoke(() -> {
            if (this.client.getModIcons() == null) {
                return false;
            }
            this.loadEmojiIcons();
            return true;
        });
    }

    private void loadEmojiIcons() {
        if (this.modIconsStart != -1) {
            return;
        }
        Emoji[] emojis = Emoji.values();
        IndexedSprite[] modIcons = this.client.getModIcons();
        assert (modIcons != null);
        IndexedSprite[] newModIcons = Arrays.copyOf(modIcons, modIcons.length + emojis.length);
        this.modIconsStart = modIcons.length;
        for (int i = 0; i < emojis.length; ++i) {
            Emoji emoji = emojis[i];
            try {
                IndexedSprite sprite;
                BufferedImage image = emoji.loadImage();
                newModIcons[this.modIconsStart + i] = sprite = ImageUtil.getImageIndexedSprite(image, this.client);
                continue;
            }
            catch (Exception ex) {
                log.warn("Failed to load the sprite for emoji " + (Object)((Object)emoji), (Throwable)ex);
            }
        }
        log.debug("Adding emoji icons");
        this.client.setModIcons(newModIcons);
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (this.client.getGameState() != GameState.LOGGED_IN || this.modIconsStart == -1) {
            return;
        }
        switch (chatMessage.getType()) {
            case PUBLICCHAT: 
            case MODCHAT: 
            case FRIENDSCHAT: 
            case CLAN_CHAT: 
            case CLAN_GUEST_CHAT: 
            case CLAN_GIM_CHAT: 
            case PRIVATECHAT: 
            case PRIVATECHATOUT: 
            case MODPRIVATECHAT: {
                break;
            }
            default: {
                return;
            }
        }
        MessageNode messageNode = chatMessage.getMessageNode();
        String message = messageNode.getValue();
        String updatedMessage = this.updateMessage(message);
        if (updatedMessage == null) {
            return;
        }
        messageNode.setValue(updatedMessage);
    }

    @Subscribe
    public void onOverheadTextChanged(OverheadTextChanged event) {
        if (!(event.getActor() instanceof Player)) {
            return;
        }
        String message = event.getOverheadText();
        String updatedMessage = this.updateMessage(message);
        if (updatedMessage == null) {
            return;
        }
        event.getActor().setOverheadText(updatedMessage);
    }

    @Nullable
    String updateMessage(String message) {
        String[] messageWords = WHITESPACE_REGEXP.split(message);
        boolean editedMessage = false;
        for (int i = 0; i < messageWords.length; ++i) {
            String trigger = Text.removeFormattingTags(messageWords[i]);
            Emoji emoji = Emoji.getEmoji(trigger);
            if (emoji == null) continue;
            int emojiId = this.modIconsStart + emoji.ordinal();
            messageWords[i] = messageWords[i].replace(trigger, "<img=" + emojiId + ">");
            editedMessage = true;
        }
        if (!editedMessage) {
            return null;
        }
        return Strings.join((String[])messageWords, (String)" ");
    }
}

