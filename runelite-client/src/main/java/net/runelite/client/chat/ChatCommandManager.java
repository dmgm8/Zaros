/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.events.ChatMessage
 */
package net.runelite.client.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.chat.ChatCommand;
import net.runelite.client.chat.ChatboxInputListener;
import net.runelite.client.chat.CommandManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ChatInput;
import net.runelite.client.events.ChatboxInput;
import net.runelite.client.events.PrivateMessageInput;

@Singleton
public class ChatCommandManager
implements ChatboxInputListener {
    private final Map<String, ChatCommand> commands = new ConcurrentHashMap<String, ChatCommand>();
    private final Client client;
    private final ScheduledExecutorService scheduledExecutorService;

    @Inject
    private ChatCommandManager(EventBus eventBus, CommandManager commandManager, Client client, ScheduledExecutorService scheduledExecutorService) {
        this.client = client;
        this.scheduledExecutorService = scheduledExecutorService;
        eventBus.register(this);
        commandManager.register(this);
    }

    public void registerCommand(String command, BiConsumer<ChatMessage, String> execute) {
        this.registerCommand(command, execute, null);
    }

    public void registerCommand(String command, BiConsumer<ChatMessage, String> execute, BiPredicate<ChatInput, String> input) {
        this.commands.put(command.toLowerCase(), new ChatCommand(command, false, execute, input));
    }

    public void registerCommandAsync(String command, BiConsumer<ChatMessage, String> execute) {
        this.registerCommandAsync(command, execute, null);
    }

    public void registerCommandAsync(String command, BiConsumer<ChatMessage, String> execute, BiPredicate<ChatInput, String> input) {
        this.commands.put(command.toLowerCase(), new ChatCommand(command, true, execute, input));
    }

    public void unregisterCommand(String command) {
        this.commands.remove(command.toLowerCase());
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        switch (chatMessage.getType()) {
            case PUBLICCHAT: 
            case MODCHAT: 
            case FRIENDSCHAT: 
            case PRIVATECHAT: 
            case MODPRIVATECHAT: 
            case PRIVATECHATOUT: 
            case CLAN_CHAT: 
            case CLAN_GUEST_CHAT: 
            case CLAN_GIM_CHAT: {
                break;
            }
            default: {
                return;
            }
        }
        String message = chatMessage.getMessage();
        String command = ChatCommandManager.extractCommand(message);
        ChatCommand chatCommand = this.commands.get(command.toLowerCase());
        if (chatCommand == null) {
            return;
        }
        if (chatCommand.isAsync()) {
            this.scheduledExecutorService.execute(() -> chatCommand.getExecute().accept(chatMessage, message));
        } else {
            chatCommand.getExecute().accept(chatMessage, message);
        }
    }

    @Override
    public boolean onChatboxInput(ChatboxInput chatboxInput) {
        String command;
        ChatCommand chatCommand;
        String message = chatboxInput.getValue();
        if (message.startsWith("/")) {
            message = message.substring(1);
        }
        if ((chatCommand = this.commands.get((command = ChatCommandManager.extractCommand(message)).toLowerCase())) == null) {
            return false;
        }
        BiPredicate<ChatInput, String> input = chatCommand.getInput();
        if (input == null) {
            return false;
        }
        return input.test(chatboxInput, message);
    }

    @Override
    public boolean onPrivateMessageInput(PrivateMessageInput privateMessageInput) {
        String message = privateMessageInput.getMessage();
        String command = ChatCommandManager.extractCommand(message);
        ChatCommand chatCommand = this.commands.get(command.toLowerCase());
        if (chatCommand == null) {
            return false;
        }
        BiPredicate<ChatInput, String> input = chatCommand.getInput();
        if (input == null) {
            return false;
        }
        return input.test(privateMessageInput, message);
    }

    private static String extractCommand(String message) {
        int idx = message.indexOf(32);
        if (idx == -1) {
            return message;
        }
        return message.substring(0, idx);
    }
}

