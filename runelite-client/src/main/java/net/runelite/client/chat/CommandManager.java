/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.events.CommandExecuted
 *  net.runelite.api.events.ScriptCallbackEvent
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.chat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatboxInputListener;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ChatboxInput;
import net.runelite.client.events.PrivateMessageInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class CommandManager {
    private static final Logger log = LoggerFactory.getLogger(CommandManager.class);
    private static final String RUNELITE_COMMAND = "runeliteCommand";
    private static final String CHATBOX_INPUT = "chatboxInput";
    private static final String PRIVATE_MESSAGE = "privateMessage";
    private final Client client;
    private final EventBus eventBus;
    private final ClientThread clientThread;
    private boolean sending;
    private final List<ChatboxInputListener> chatboxInputListenerList = new CopyOnWriteArrayList<ChatboxInputListener>();

    @Inject
    private CommandManager(Client client, EventBus eventBus, ClientThread clientThread) {
        this.client = client;
        this.eventBus = eventBus;
        this.clientThread = clientThread;
        eventBus.register(this);
    }

    public void register(ChatboxInputListener chatboxInputListener) {
        this.chatboxInputListenerList.add(chatboxInputListener);
    }

    public void unregister(ChatboxInputListener chatboxInputListener) {
        this.chatboxInputListenerList.remove(chatboxInputListener);
    }

    @Subscribe
    private void onScriptCallbackEvent(ScriptCallbackEvent event) {
        if (this.sending) {
            return;
        }
        switch (event.getEventName()) {
            case "runeliteCommand": {
                this.runCommand();
                break;
            }
            case "chatboxInput": {
                this.handleInput(event);
                break;
            }
            case "privateMessage": {
                this.handlePrivateMessage(event);
            }
        }
    }

    private void runCommand() {
        String typedText = this.client.getVarcStrValue(335).substring(2);
        log.debug("Command: {}", (Object)typedText);
        String[] split = typedText.split(" ");
        if (split.length == 0) {
            return;
        }
        String command = split[0];
        String[] args = Arrays.copyOfRange(split, 1, split.length);
        CommandExecuted commandExecuted = new CommandExecuted(command, args);
        this.eventBus.post((Object)commandExecuted);
    }

    private void handleInput(ScriptCallbackEvent event) {
        String[] stringStack = this.client.getStringStack();
        int[] intStack = this.client.getIntStack();
        int stringStackCount = this.client.getStringStackSize();
        int intStackCount = this.client.getIntStackSize();
        final String typedText = stringStack[stringStackCount - 1];
        final int chatType = intStack[intStackCount - 2];
        final int clanTarget = intStack[intStackCount - 1];
        ChatboxInput chatboxInput = new ChatboxInput(typedText, chatType){
            private boolean resumed;

            @Override
            public void resume() {
                if (this.resumed) {
                    return;
                }
                this.resumed = true;
                CommandManager.this.clientThread.invoke(() -> CommandManager.this.sendChatboxInput(typedText, chatType, clanTarget));
            }
        };
        boolean stop = false;
        for (ChatboxInputListener chatboxInputListener : this.chatboxInputListenerList) {
            stop |= chatboxInputListener.onChatboxInput(chatboxInput);
        }
        if (stop) {
            stringStack[stringStackCount - 1] = "";
        }
    }

    private void handlePrivateMessage(ScriptCallbackEvent event) {
        String[] stringStack = this.client.getStringStack();
        int[] intStack = this.client.getIntStack();
        int stringStackCount = this.client.getStringStackSize();
        int intStackCount = this.client.getIntStackSize();
        final String target = stringStack[stringStackCount - 2];
        final String message = stringStack[stringStackCount - 1];
        PrivateMessageInput privateMessageInput = new PrivateMessageInput(target, message){
            private boolean resumed;

            @Override
            public void resume() {
                if (this.resumed) {
                    return;
                }
                this.resumed = true;
                CommandManager.this.clientThread.invoke(() -> CommandManager.this.sendPrivmsg(target, message));
            }
        };
        boolean stop = false;
        for (ChatboxInputListener chatboxInputListener : this.chatboxInputListenerList) {
            stop |= chatboxInputListener.onPrivateMessageInput(privateMessageInput);
        }
        if (stop) {
            intStack[intStackCount - 1] = 1;
            this.client.setStringStackSize(stringStackCount - 2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void sendChatboxInput(String input, int chatType, int clanTarget) {
        this.sending = true;
        try {
            this.client.runScript(new Object[]{5517, input, chatType, clanTarget, 0, -1});
        }
        finally {
            this.sending = false;
        }
    }

    private void sendPrivmsg(String target, String message) {
        this.client.runScript(new Object[]{11004, target, message});
    }
}

