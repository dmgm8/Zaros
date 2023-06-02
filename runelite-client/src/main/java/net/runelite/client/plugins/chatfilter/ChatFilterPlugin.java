/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.CharMatcher
 *  com.google.common.base.Splitter
 *  com.google.common.collect.ImmutableSet
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.FriendsChatManager
 *  net.runelite.api.MessageNode
 *  net.runelite.api.Player
 *  net.runelite.api.clan.ClanChannel
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.OverheadTextChanged
 *  net.runelite.api.events.ScriptCallbackEvent
 *  org.apache.commons.lang3.StringUtils
 */
package net.runelite.client.plugins.chatfilter;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.MessageNode;
import net.runelite.api.Player;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.chatfilter.ChatFilterConfig;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.StringUtils;

@PluginDescriptor(name="Chat Filter", description="Censor user configurable words or patterns from chat", enabledByDefault=false)
public class ChatFilterPlugin
extends Plugin {
    private static final Splitter NEWLINE_SPLITTER = Splitter.on((String)"\n").omitEmptyStrings().trimResults();
    @VisibleForTesting
    static final String CENSOR_MESSAGE = "Hey, everyone, I just tried to say something very silly!";
    private static final Set<ChatMessageType> COLLAPSIBLE_MESSAGETYPES = ImmutableSet.of((Object)ChatMessageType.ENGINE, (Object)ChatMessageType.GAMEMESSAGE, (Object)ChatMessageType.ITEM_EXAMINE, (Object)ChatMessageType.NPC_EXAMINE, (Object)ChatMessageType.OBJECT_EXAMINE, (Object)ChatMessageType.SPAM, (Object[])new ChatMessageType[]{ChatMessageType.PUBLICCHAT, ChatMessageType.MODCHAT});
    private final CharMatcher jagexPrintableCharMatcher = Text.JAGEX_PRINTABLE_CHAR_MATCHER;
    private final List<Pattern> filteredPatterns = new ArrayList<Pattern>();
    private final List<Pattern> filteredNamePatterns = new ArrayList<Pattern>();
    private final LinkedHashMap<String, Duplicate> duplicateChatCache = new LinkedHashMap<String, Duplicate>(){
        private static final int MAX_ENTRIES = 100;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Duplicate> eldest) {
            return this.size() > 100;
        }
    };
    @Inject
    private Client client;
    @Inject
    private ChatFilterConfig config;

    @Provides
    ChatFilterConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ChatFilterConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        this.updateFilteredPatterns();
        this.client.refreshChat();
    }

    @Override
    protected void shutDown() throws Exception {
        this.filteredPatterns.clear();
        this.duplicateChatCache.clear();
        this.client.refreshChat();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        switch (gameStateChanged.getGameState()) {
            case HOPPING: 
            case LOGGING_IN: {
                this.duplicateChatCache.values().forEach(d -> {
                    d.messageId = -1;
                });
            }
        }
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent event) {
        Duplicate duplicateCacheEntry;
        boolean shouldCollapse;
        if (!"chatFilterCheck".equals(event.getEventName())) {
            return;
        }
        int[] intStack = this.client.getIntStack();
        int intStackSize = this.client.getIntStackSize();
        String[] stringStack = this.client.getStringStack();
        int stringStackSize = this.client.getStringStackSize();
        int messageType = intStack[intStackSize - 2];
        int messageId = intStack[intStackSize - 1];
        String message = stringStack[stringStackSize - 1];
        ChatMessageType chatMessageType = ChatMessageType.of((int)messageType);
        MessageNode messageNode = (MessageNode)this.client.getMessages().get((long)messageId);
        String name = messageNode.getName();
        int duplicateCount = 0;
        boolean blockMessage = false;
        switch (chatMessageType) {
            case PUBLICCHAT: 
            case MODCHAT: 
            case AUTOTYPER: 
            case PRIVATECHAT: 
            case MODPRIVATECHAT: 
            case FRIENDSCHAT: 
            case CLAN_CHAT: 
            case CLAN_GUEST_CHAT: 
            case CLAN_GIM_CHAT: {
                if (!this.shouldFilterPlayerMessage(Text.removeTags(name))) break;
                blockMessage = (message = this.censorMessage(name, message)) == null;
                break;
            }
            case GAMEMESSAGE: 
            case ENGINE: 
            case ITEM_EXAMINE: 
            case NPC_EXAMINE: 
            case OBJECT_EXAMINE: 
            case SPAM: 
            case CLAN_MESSAGE: 
            case CLAN_GUEST_MESSAGE: 
            case CLAN_GIM_MESSAGE: {
                if (!this.config.filterGameChat()) break;
                boolean bl = blockMessage = (message = this.censorMessage(null, message)) == null;
            }
        }
        boolean bl = chatMessageType == ChatMessageType.PUBLICCHAT || chatMessageType == ChatMessageType.MODCHAT ? this.config.collapsePlayerChat() : (shouldCollapse = COLLAPSIBLE_MESSAGETYPES.contains((Object)chatMessageType) && this.config.collapseGameChat());
        if (!blockMessage && shouldCollapse && (duplicateCacheEntry = this.duplicateChatCache.get(name + ":" + message)) != null && duplicateCacheEntry.messageId != -1) {
            blockMessage = duplicateCacheEntry.messageId != messageId || (chatMessageType == ChatMessageType.PUBLICCHAT || chatMessageType == ChatMessageType.MODCHAT) && this.config.maxRepeatedPublicChats() > 0 && duplicateCacheEntry.count > this.config.maxRepeatedPublicChats();
            duplicateCount = duplicateCacheEntry.count;
        }
        if (blockMessage) {
            intStack[intStackSize - 3] = 0;
        } else {
            if (duplicateCount > 1) {
                message = message + " (" + duplicateCount + ")";
            }
            stringStack[stringStackSize - 1] = message;
        }
    }

    @Subscribe
    public void onOverheadTextChanged(OverheadTextChanged event) {
        if (!(event.getActor() instanceof Player) || !this.shouldFilterPlayerMessage(event.getActor().getName())) {
            return;
        }
        String message = this.censorMessage(event.getActor().getName(), event.getOverheadText());
        if (message == null) {
            message = " ";
        }
        event.getActor().setOverheadText(message);
    }

    @Subscribe(priority=-2.0f)
    public void onChatMessage(ChatMessage chatMessage) {
        if (COLLAPSIBLE_MESSAGETYPES.contains((Object)chatMessage.getType())) {
            MessageNode messageNode = chatMessage.getMessageNode();
            String key = messageNode.getName() + ":" + messageNode.getValue();
            Duplicate duplicate = (Duplicate)this.duplicateChatCache.remove(key);
            if (duplicate == null) {
                duplicate = new Duplicate();
            }
            ++duplicate.count;
            duplicate.messageId = messageNode.getId();
            this.duplicateChatCache.put(key, duplicate);
        }
    }

    boolean shouldFilterPlayerMessage(String playerName) {
        boolean isMessageFromSelf = playerName.equals(this.client.getLocalPlayer().getName());
        return !(isMessageFromSelf || !this.config.filterFriends() && this.client.isFriended(playerName, false) || !this.config.filterFriendsChat() && this.isFriendsChatMember(playerName) || !this.config.filterClanChat() && this.isClanChatMember(playerName));
    }

    private boolean isFriendsChatMember(String name) {
        FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
        return friendsChatManager != null && friendsChatManager.findByName(name) != null;
    }

    private boolean isClanChatMember(String name) {
        ClanChannel clanChannel = this.client.getClanChannel();
        if (clanChannel != null && clanChannel.findMember(name) != null) {
            return true;
        }
        clanChannel = this.client.getGuestClanChannel();
        return clanChannel != null && clanChannel.findMember(name) != null;
    }

    String censorMessage(String username, String message) {
        String strippedMessage = this.jagexPrintableCharMatcher.retainFrom((CharSequence)message).replace('\u00a0', ' ').replaceAll("<lt>", "<").replaceAll("<gt>", ">");
        String strippedAccents = this.stripAccents(strippedMessage);
        assert (strippedMessage.length() == strippedAccents.length());
        if (username != null && this.shouldFilterByName(username)) {
            switch (this.config.filterType()) {
                case CENSOR_WORDS: {
                    return StringUtils.repeat((char)'*', (int)strippedMessage.length());
                }
                case CENSOR_MESSAGE: {
                    return CENSOR_MESSAGE;
                }
                case REMOVE_MESSAGE: {
                    return null;
                }
            }
        }
        boolean filtered = false;
        for (Pattern pattern : this.filteredPatterns) {
            Matcher m = pattern.matcher(strippedAccents);
            StringBuilder sb = new StringBuilder();
            int idx = 0;
            while (m.find()) {
                switch (this.config.filterType()) {
                    case CENSOR_WORDS: {
                        MatchResult matchResult = m.toMatchResult();
                        sb.append(strippedMessage, idx, matchResult.start()).append(StringUtils.repeat((char)'*', (int)matchResult.group().length()));
                        idx = m.end();
                        filtered = true;
                        break;
                    }
                    case CENSOR_MESSAGE: {
                        return CENSOR_MESSAGE;
                    }
                    case REMOVE_MESSAGE: {
                        return null;
                    }
                }
            }
            sb.append(strippedMessage.substring(idx));
            strippedMessage = sb.toString();
            assert (strippedMessage.length() == strippedAccents.length());
        }
        return filtered ? strippedMessage : message;
    }

    void updateFilteredPatterns() {
        this.filteredPatterns.clear();
        this.filteredNamePatterns.clear();
        Text.fromCSV(this.config.filteredWords()).stream().map(this::stripAccents).map(s -> Pattern.compile(Pattern.quote(s), 2)).forEach(this.filteredPatterns::add);
        NEWLINE_SPLITTER.splitToList((CharSequence)this.config.filteredRegex()).stream().map(this::stripAccents).map(ChatFilterPlugin::compilePattern).filter(Objects::nonNull).forEach(this.filteredPatterns::add);
        NEWLINE_SPLITTER.splitToList((CharSequence)this.config.filteredNames()).stream().map(this::stripAccents).map(ChatFilterPlugin::compilePattern).filter(Objects::nonNull).forEach(this.filteredNamePatterns::add);
    }

    private String stripAccents(String input) {
        return this.config.stripAccents() ? StringUtils.stripAccents((String)input) : input;
    }

    private static Pattern compilePattern(String pattern) {
        try {
            return Pattern.compile(pattern, 2);
        }
        catch (PatternSyntaxException ex) {
            return null;
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!"chatfilter".equals(event.getGroup())) {
            return;
        }
        this.updateFilteredPatterns();
        this.client.refreshChat();
    }

    @VisibleForTesting
    boolean shouldFilterByName(String playerName) {
        String sanitizedName = Text.standardize(playerName);
        for (Pattern pattern : this.filteredNamePatterns) {
            Matcher m = pattern.matcher(sanitizedName);
            if (!m.find()) continue;
            return true;
        }
        return false;
    }

    private static class Duplicate {
        int messageId;
        int count;

        private Duplicate() {
        }
    }
}

