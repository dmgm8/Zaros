/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.api.events.ScriptCallbackEvent
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 */
package net.runelite.client.plugins.keyremapping;

import com.google.inject.Provides;
import java.awt.Color;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.keyremapping.KeyRemappingConfig;
import net.runelite.client.plugins.keyremapping.KeyRemappingListener;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.ColorUtil;

@PluginDescriptor(name="Key Remapping", description="Allows use of WASD keys for camera movement with 'Press Enter to Chat', and remapping number keys to F-keys", tags={"enter", "chat", "wasd", "camera"}, enabledByDefault=false)
public class KeyRemappingPlugin
extends Plugin {
    private static final String PRESS_ENTER_TO_CHAT = "Press Enter to Chat...";
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private KeyManager keyManager;
    @Inject
    private KeyRemappingListener inputListener;
    private boolean typing;

    @Override
    protected void startUp() throws Exception {
        this.typing = false;
        this.keyManager.registerKeyListener(this.inputListener);
        this.clientThread.invoke(() -> {
            if (this.client.getGameState() == GameState.LOGGED_IN) {
                this.lockChat();
                this.client.setVarcStrValue(335, "");
            }
        });
    }

    @Override
    protected void shutDown() throws Exception {
        this.clientThread.invoke(() -> {
            if (this.client.getGameState() == GameState.LOGGED_IN) {
                this.unlockChat();
            }
        });
        this.keyManager.unregisterKeyListener(this.inputListener);
    }

    @Provides
    KeyRemappingConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(KeyRemappingConfig.class);
    }

    boolean chatboxFocused() {
        Widget chatboxParent = this.client.getWidget(WidgetInfo.CHATBOX_PARENT);
        if (chatboxParent == null || chatboxParent.getOnKeyListener() == null) {
            return false;
        }
        Widget worldMapSearch = this.client.getWidget(WidgetInfo.WORLD_MAP_SEARCH);
        return worldMapSearch == null || this.client.getVarcIntValue(190) != 1;
    }

    boolean isDialogOpen() {
        return this.isHidden(WidgetInfo.CHATBOX_MESSAGES) || this.isHidden(WidgetInfo.CHATBOX_TRANSPARENT_LINES) || !this.isHidden(WidgetInfo.BANK_PIN_CONTAINER);
    }

    boolean isOptionsDialogOpen() {
        return this.client.getWidget(WidgetInfo.DIALOG_OPTION) != null;
    }

    private boolean isHidden(WidgetInfo widgetInfo) {
        Widget w = this.client.getWidget(widgetInfo);
        return w == null || w.isSelfHidden();
    }

    @Subscribe
    public void onScriptCallbackEvent(ScriptCallbackEvent scriptCallbackEvent) {
        switch (scriptCallbackEvent.getEventName()) {
            case "setChatboxInput": {
                Widget chatboxInput = this.client.getWidget(WidgetInfo.CHATBOX_INPUT);
                if (chatboxInput == null || this.typing) break;
                this.setChatboxWidgetInput(chatboxInput, PRESS_ENTER_TO_CHAT);
                break;
            }
            case "blockChatInput": {
                if (this.typing) break;
                int[] intStack = this.client.getIntStack();
                int intStackSize = this.client.getIntStackSize();
                intStack[intStackSize - 1] = 1;
            }
        }
    }

    void lockChat() {
        Widget chatboxInput = this.client.getWidget(WidgetInfo.CHATBOX_INPUT);
        if (chatboxInput != null) {
            this.setChatboxWidgetInput(chatboxInput, PRESS_ENTER_TO_CHAT);
        }
    }

    void unlockChat() {
        Widget chatboxInput = this.client.getWidget(WidgetInfo.CHATBOX_INPUT);
        if (chatboxInput != null && this.client.getGameState() == GameState.LOGGED_IN) {
            boolean isChatboxTransparent = this.client.isResized() && this.client.getVarbitValue(4608) == 1;
            Color textColor = isChatboxTransparent ? JagexColors.CHAT_TYPED_TEXT_TRANSPARENT_BACKGROUND : JagexColors.CHAT_TYPED_TEXT_OPAQUE_BACKGROUND;
            this.setChatboxWidgetInput(chatboxInput, ColorUtil.wrapWithColorTag(this.client.getVarcStrValue(335) + "*", textColor));
        }
    }

    private void setChatboxWidgetInput(Widget widget, String input) {
        String text = widget.getText();
        int idx = text.indexOf(58);
        if (idx != -1) {
            String newText = text.substring(0, idx) + ": " + input;
            widget.setText(newText);
        }
    }

    boolean isTyping() {
        return this.typing;
    }

    void setTyping(boolean typing) {
        this.typing = typing;
    }
}

